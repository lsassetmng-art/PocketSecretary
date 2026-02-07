#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
mother.py (single-file)
- Read policy.yaml
- Auto-run AI twice (rerun check)
- Write output.txt + eval.yaml (100% auto)
- Compare runs, propose & apply self-improvements (policy_next.yaml)
- Optional auto-retry once with improved policy

Usage:
  ./mother.py run            # default (auto-run if OPENAI_API_KEY set)
  ./mother.py prompt         # prints prompts + creates run folder placeholders
  ./mother.py report         # shows last runs summary + improvement suggestions

Env:
  OPENAI_API_KEY   required for full auto-run
  OPENAI_MODEL     default: gpt-4o-mini
  OPENAI_BASE_URL  default: https://api.openai.com/v1
"""

import os, sys, json, time, re, datetime
from pathlib import Path
from typing import Any, Dict, List, Tuple, Optional

import yaml
import requests

BASE = Path.home() / "mother-ai"
POLICY = BASE / "policy.yaml"
RUNS = BASE / "runs"
POLICY_NEXT = BASE / "policy_next.yaml"
INDEX = RUNS / "_index.json"

# -----------------------------
# Helpers
# -----------------------------
def die(msg: str, code: int = 1) -> None:
    print(msg, file=sys.stderr)
    sys.exit(code)

def load_yaml(path: Path) -> Dict[str, Any]:
    if not path.exists():
        die(f"Not found: {path}")
    with path.open("r", encoding="utf-8") as f:
        return yaml.safe_load(f) or {}

def dump_yaml(path: Path, data: Dict[str, Any]) -> None:
    path.write_text(yaml.safe_dump(data, sort_keys=False, allow_unicode=True), encoding="utf-8")

def now_run_id() -> str:
    return datetime.datetime.now().strftime("run-%Y%m%d-%H%M%S")

def norm_text(s: str) -> str:
    s = (s or "").replace("\r\n", "\n").strip()
    s = re.sub(r"\n{3,}", "\n\n", s)
    return s

def ensure_dirs() -> None:
    BASE.mkdir(parents=True, exist_ok=True)
    RUNS.mkdir(parents=True, exist_ok=True)

def read_index() -> Dict[str, Any]:
    if INDEX.exists():
        try:
            return json.loads(INDEX.read_text(encoding="utf-8"))
        except Exception:
            return {"runs": []}
    return {"runs": []}

def write_index(idx: Dict[str, Any]) -> None:
    INDEX.write_text(json.dumps(idx, ensure_ascii=False, indent=2), encoding="utf-8")

def safe_get(d: Dict[str, Any], *keys, default=None):
    cur = d
    for k in keys:
        if not isinstance(cur, dict) or k not in cur:
            return default
        cur = cur[k]
    return cur

# -----------------------------
# Prompt building (policy -> SYSTEM/USER)
# -----------------------------
def policy_required_fields(policy: Dict[str, Any]) -> List[str]:
    missing = []
    if not safe_get(policy, "domain", "business", default="").strip():
        missing.append("domain.business")
    if not safe_get(policy, "technology", "platform", default="").strip():
        missing.append("technology.platform")
    return missing

def build_prompts(policy: Dict[str, Any]) -> Tuple[str, str]:
    domain = policy.get("domain", {}) or {}
    tech = policy.get("technology", {}) or {}
    exp = policy.get("expectations", {}) or {}

    business = str(domain.get("business", "")).strip()
    scope = str(domain.get("scope", "")).strip()
    platform = str(tech.get("platform", "")).strip()
    environment = str(tech.get("environment", "")).strip()

    principles = policy.get("principles", []) or []
    requirements = policy.get("requirements", []) or []
    prohibitions = policy.get("prohibitions", []) or []

    output_type = str(exp.get("output_type", "DDL")).strip()
    completeness = str(exp.get("completeness", "executable_only")).strip()
    explanation = str(exp.get("explanation", "forbid")).strip()

    sys_lines = []
    sys_lines.append("あなたは『AI社員（自動生成）』である。")
    sys_lines.append(f"担当スコープ：{scope or '（未指定）'}。")
    sys_lines.append(f"唯一の目的：{business or '（未指定）'}に関する {output_type} を方針どおり生成する。")
    sys_lines.append("")
    sys_lines.append("【絶対原則（principles）】")
    for p in principles:
        sys_lines.append(f"- {p}")
    sys_lines.append("")
    sys_lines.append("【必須要件（requirements）】")
    for r in requirements:
        sys_lines.append(f"- {r}")
    sys_lines.append("")
    sys_lines.append("【禁止事項（prohibitions）】")
    for x in prohibitions:
        sys_lines.append(f"- {x}")
    sys_lines.append("")
    sys_lines.append("【出力方針（expectations）】")
    sys_lines.append(f"- output_type: {output_type}")
    sys_lines.append(f"- completeness: {completeness}")
    sys_lines.append(f"- explanation: {explanation}")
    sys_lines.append("")
    sys_lines.append("【重要】explanation が forbid の場合、成果物（コード）以外は出力しない。")
    sys_lines.append("入力不足・矛盾があれば作業を停止し、不足点のみを列挙して終了する（推測禁止）。")
    system_prompt = "\n".join(sys_lines).strip()

    user_lines = []
    user_lines.append("以下の条件に基づき作業せよ。")
    user_lines.append("")
    user_lines.append(f"業務ドメイン：{business}")
    user_lines.append(f"技術プラットフォーム：{platform}")
    if environment:
        user_lines.append(f"環境：{environment}")
    user_lines.append("")
    user_lines.append("方針（要点）：")
    for p in principles:
        user_lines.append(f"- {p}")
    for r in requirements:
        user_lines.append(f"- {r}")
    user_lines.append("")
    if explanation == "forbid":
        user_lines.append("出力は成果物（コード）だけ。説明文は禁止。")
    else:
        user_lines.append("必要なら短い説明を付けてもよい。")

    user_prompt = "\n".join(user_lines).strip()
    return system_prompt, user_prompt

# -----------------------------
# OpenAI call (Chat Completions)
# -----------------------------
def openai_chat(system_prompt: str, user_prompt: str, temperature: float = 0.1) -> str:
    api_key = os.environ.get("OPENAI_API_KEY", "").strip()
    if not api_key:
        die("OPENAI_API_KEY is not set. (Full auto-run requires an API key)")

    model = os.environ.get("OPENAI_MODEL", "gpt-4o-mini").strip()
    base_url = os.environ.get("OPENAI_BASE_URL", "https://api.openai.com/v1").rstrip("/")
    url = f"{base_url}/chat/completions"

    headers = {"Authorization": f"Bearer {api_key}", "Content-Type": "application/json"}
    payload = {
        "model": model,
        "temperature": temperature,
        "messages": [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt},
        ],
    }
    r = requests.post(url, headers=headers, data=json.dumps(payload), timeout=120)
    if r.status_code != 200:
        die(f"OpenAI API error: {r.status_code}\n{r.text}")
    data = r.json()
    try:
        return data["choices"][0]["message"]["content"]
    except Exception:
        die(f"Unexpected response:\n{data}")

# -----------------------------
# Evaluation (100% auto -> eval.yaml)
# Steps:
# 0 output exists
# 1 format expectation (DDL etc)
# 2 prohibitions + no-guess
# 3 (optional) exec check placeholder (can't execute SQL here reliably)
# 4 rerun check (exact match for now)
# 5 criteria checks
# -----------------------------
def evaluate(policy: Dict[str, Any], out1: str, out2: Optional[str]) -> Dict[str, Any]:
    exp = policy.get("expectations", {}) or {}
    automation = policy.get("automation", {}) or {}

    principles = policy.get("principles", []) or []
    prohibitions = policy.get("prohibitions", []) or []
    requirements = policy.get("requirements", []) or []

    output_type = str(exp.get("output_type", "DDL")).strip().upper()
    completeness = str(exp.get("completeness", "executable_only")).strip()
    explanation = str(exp.get("explanation", "forbid")).strip()
    rerun_check = bool(automation.get("rerun_check", True))
    auto_hire_threshold = float(automation.get("auto_hire_threshold", 0.8))
    retry_on_safe_stop = bool(automation.get("retry_on_safe_stop", True))

    def fail(step: str, reason: str) -> Dict[str, Any]:
        return {
            "result": "FAIL",
            "fail_step": step,
            "fail_reason": reason,
            "confidence_score": 0.0,
            "next_action": "REJECT",
            "safe_stop": False,
        }

    t1 = norm_text(out1)
    if not t1:
        return fail("STEP-0", "出力が空")

    # detect safe-stop (input missing) style
    missing = policy_required_fields(policy)
    safe_stop = False
    if missing:
        # If missing fields exist, correct behavior is to stop, not generate code.
        # If code appears anyway -> FAIL (guessing).
        code_like = "CREATE TABLE" in t1.upper() or "class " in t1 or "def " in t1
        if code_like:
            return fail("STEP-2", f"入力不足({', '.join(missing)})なのに成果物を生成（推測の疑い）")
        # if it lists missing or says cannot proceed -> safe stop
        if any(k in t1 for k in ["不足", "足り", "揃わない", "入力不足", "不足しています"]):
            safe_stop = True

    # STEP-1 format expectation (if not safe-stop)
    if not safe_stop:
        if output_type == "DDL":
            if "CREATE TABLE" not in t1.upper():
                return fail("STEP-1", "DDL指定だが CREATE TABLE が見当たらない")
        # explanation forbid: disallow obvious narrative if code exists
        if explanation == "forbid":
            if re.search(r"(説明|理由|ポイント|まとめ|結論|注意)", t1) and ("CREATE TABLE" in t1.upper()):
                return fail("STEP-1", "説明文が混在（explanation=forbid）")

    # STEP-2 prohibitions + no-guess
    for bad in prohibitions:
        b = str(bad).strip()
        if not b:
            continue
        if b in t1:
            return fail("STEP-2", f"禁止事項に該当: {b}")

    if any(str(p).strip().lower() == "no guess" for p in principles):
        if re.search(r"(たぶん|おそらく|仮に|一般的に|推測)", t1):
            return fail("STEP-2", "No guess 違反（推測表現）")

    # STEP-4 rerun check (exact match for deterministic guarantee)
    if rerun_check and (out2 is not None) and (not safe_stop):
        t2 = norm_text(out2)
        if not t2:
            return fail("STEP-4", "再実行出力が空")
        if t2 != t1:
            return fail("STEP-4", "再現性NG（2回の出力が一致しない）")

    # STEP-5 criteria (minimal)
    if not safe_stop and output_type == "DDL":
        if completeness == "executable_only":
            if re.search(r"(TODO|FIXME|TBD)", t1, re.IGNORECASE):
                return fail("STEP-5", "未完要素が含まれる")

    # PASS scoring
    confidence = 0.9
    next_action = "HIRE" if confidence >= auto_hire_threshold else "RETRY"
    # safe-stop is not a failure; if retry enabled, we ask for more inputs -> RETRY
    if safe_stop:
        confidence = 0.7
        next_action = "RETRY" if retry_on_safe_stop else "REJECT"

    return {
        "result": "PASS",
        "fail_step": "NONE",
        "fail_reason": "",
        "confidence_score": float(confidence),
        "next_action": next_action,
        "safe_stop": safe_stop,
    }

# -----------------------------
# Self-improvement (compare runs -> policy_next.yaml)
# Heuristic improvements:
# - If frequent STEP-4 rerun mismatch: add principle "Deterministic output" and lower temperature (internal)
# - If frequent STEP-1 explanation mixed: enforce explanation forbid + add prohibition keywords
# - If frequent STEP-2 guess: add explicit prohibitions for guessy phrases
# - If frequent safe_stop: add requirements hints to policy (business/platform must be present)
# Never edits policy.yaml directly; writes policy_next.yaml
# -----------------------------
def summarize_runs(idx: Dict[str, Any], last_n: int = 10) -> Dict[str, Any]:
    runs = idx.get("runs", [])[-last_n:]
    total = len(runs)
    fails = [r for r in runs if r.get("result") == "FAIL"]
    by_step: Dict[str, int] = {}
    for r in fails:
        step = r.get("fail_step", "UNKNOWN")
        by_step[step] = by_step.get(step, 0) + 1
    safe_stops = sum(1 for r in runs if r.get("safe_stop") is True)
    pass_count = sum(1 for r in runs if r.get("result") == "PASS")
    return {
        "total": total,
        "pass": pass_count,
        "fail": len(fails),
        "safe_stop": safe_stops,
        "fail_by_step": by_step,
    }

def improve_policy(policy: Dict[str, Any], summary: Dict[str, Any]) -> Tuple[Dict[str, Any], List[str]]:
    reasons: List[str] = []
    p2 = json.loads(json.dumps(policy))  # deep copy via json

    principles = p2.get("principles", []) or []
    prohibitions = p2.get("prohibitions", []) or []
    exp = p2.get("expectations", {}) or {}

    fail_by_step = summary.get("fail_by_step", {}) or {}
    total = summary.get("total", 0)
    safe_stop_count = summary.get("safe_stop", 0)

    def add_unique(lst: List[Any], item: Any) -> None:
        if item not in lst:
            lst.append(item)

    # If rerun mismatch common
    if fail_by_step.get("STEP-4", 0) >= 1:
        add_unique(principles, "Deterministic output (same input -> same output)")
        reasons.append("再現性NGが検出されたため、決定論的出力原則を追加")

    # Explanation mixed
    if fail_by_step.get("STEP-1", 0) >= 1:
        exp["explanation"] = "forbid"
        add_unique(prohibitions, "説明")
        add_unique(prohibitions, "理由")
        reasons.append("説明混在が検出されたため、explanation=forbid を強制し禁止語を追加")

    # Guessing
    if fail_by_step.get("STEP-2", 0) >= 1:
        add_unique(prohibitions, "たぶん")
        add_unique(prohibitions, "おそらく")
        add_unique(prohibitions, "仮に")
        add_unique(prohibitions, "一般的に")
        reasons.append("推測表現が検出されたため、推測語を禁止事項に追加")

    # Safe-stop frequent -> strengthen requirements text
    if total > 0 and safe_stop_count / max(total, 1) >= 0.5:
        req = p2.get("requirements", []) or []
        add_unique(req, "domain.business must be provided")
        add_unique(req, "technology.platform must be provided")
        p2["requirements"] = req
        reasons.append("安全停止が多いため、必須入力要件を明文化")

    p2["principles"] = principles
    p2["prohibitions"] = prohibitions
    p2["expectations"] = exp
    return p2, reasons

# -----------------------------
# Run pipeline
# -----------------------------
def create_run_folder() -> Path:
    rid = now_run_id()
    run_dir = RUNS / rid
    run_dir.mkdir(parents=True, exist_ok=True)
    return run_dir

def write_run_files(run_dir: Path, output: str, eval_obj: Dict[str, Any], meta: Dict[str, Any]) -> None:
    (run_dir / "output.txt").write_text(output, encoding="utf-8")
    dump_yaml(run_dir / "eval.yaml", {"evaluation_result": eval_obj})
    (run_dir / "meta.json").write_text(json.dumps(meta, ensure_ascii=False, indent=2), encoding="utf-8")

def index_append(run_dir: Path, eval_obj: Dict[str, Any], safe_stop: bool) -> None:
    idx = read_index()
    idx.setdefault("runs", [])
    idx["runs"].append({
        "run_id": run_dir.name,
        "ts": datetime.datetime.now().isoformat(timespec="seconds"),
        "result": eval_obj.get("result", ""),
        "fail_step": eval_obj.get("fail_step", ""),
        "confidence_score": eval_obj.get("confidence_score", 0.0),
        "next_action": eval_obj.get("next_action", ""),
        "safe_stop": safe_stop,
    })
    write_index(idx)

def make_eval_obj(policy: Dict[str, Any], ev: Dict[str, Any]) -> Dict[str, Any]:
    # Minimal “agent_id” derived from scope
    scope = str(safe_get(policy, "domain", "scope", default="agent")).strip().lower().replace(" ", "_")
    agent_id = f"auto-{scope or 'agent'}"
    phase = "PROBATION"
    return {
        "agent_id": agent_id,
        "phase": phase,
        "result": ev["result"],
        "fail_step": ev.get("fail_step", ""),
        "fail_reason": ev.get("fail_reason", ""),
        "confidence_score": float(ev.get("confidence_score", 0.0)),
        "next_action": ev.get("next_action", "RETRY"),
    }

def cmd_prompt(policy: Dict[str, Any]) -> None:
    system_prompt, user_prompt = build_prompts(policy)
    run_dir = create_run_folder()
    (run_dir / "output.txt").write_text(
        "OPENAI_API_KEY が未設定でもOK。\n\n=== SYSTEM ===\n"
        + system_prompt + "\n\n=== USER ===\n" + user_prompt + "\n",
        encoding="utf-8"
    )
    dump_yaml(run_dir / "eval.yaml", {"evaluation_result": {
        "agent_id": "auto-agent",
        "phase": "PROBATION",
        "result": "",
        "fail_step": "",
        "fail_reason": "",
        "confidence_score": 0.0,
        "next_action": ""
    }})
    print(f"Run folder prepared: {run_dir}")
    print("Prompts written into output.txt (copy from there).")

def cmd_report() -> None:
    ensure_dirs()
    idx = read_index()
    summary = summarize_runs(idx, last_n=10)
    print(json.dumps(summary, ensure_ascii=False, indent=2))
    if POLICY.exists():
        policy = load_yaml(POLICY)
        improved, reasons = improve_policy(policy, summary)
        if reasons:
            print("\n[Suggested improvements -> policy_next.yaml]")
            for r in reasons:
                print(f"- {r}")
            dump_yaml(POLICY_NEXT, improved)
            print(f"\nWrote: {POLICY_NEXT}")
        else:
            print("\nNo improvements suggested (last 10 runs).")

def cmd_run(policy: Dict[str, Any]) -> None:
    ensure_dirs()
    missing = policy_required_fields(policy)
    system_prompt, user_prompt = build_prompts(policy)

    # If required fields missing, we still can run; model should safe-stop; evaluation handles it.
    model = os.environ.get("OPENAI_MODEL", "gpt-4o-mini").strip()
    temp = 0.1
    meta_base = {
        "model": model,
        "temperature": temp,
        "missing_policy_fields": missing,
    }

    run_dir = create_run_folder()

    # Try auto-run
    api_key = os.environ.get("OPENAI_API_KEY", "").strip()
    if not api_key:
        # prompt-only fallback
        out = "OPENAI_API_KEY 未設定のため自動実行できません。\n\n=== SYSTEM ===\n" + system_prompt + "\n\n=== USER ===\n" + user_prompt + "\n"
        ev_obj = {
            "agent_id": "auto-agent",
            "phase": "PROBATION",
            "result": "FAIL",
            "fail_step": "STEP-3",
            "fail_reason": "自動実行不可（OPENAI_API_KEY 未設定）",
            "confidence_score": 0.0,
            "next_action": "RETRY",
        }
        write_run_files(run_dir, out, ev_obj, {**meta_base, "mode": "prompt-only"})
        index_append(run_dir, ev_obj, safe_stop=False)
        print(f"Prepared prompts in: {run_dir / 'output.txt'}")
        print(f"Run folder: {run_dir}")
        return

    # Full auto-run x2 (rerun check)
    out1 = openai_chat(system_prompt, user_prompt, temperature=temp)
    time.sleep(1)
    out2 = openai_chat(system_prompt, user_prompt, temperature=temp)

    # Evaluate
    ev = evaluate(policy, out1, out2)
    ev_obj = make_eval_obj(policy, ev)

    # Write output.txt with both runs
    out_blob = "=== OUTPUT (run1) ===\n" + out1 + "\n\n=== OUTPUT (run2) ===\n" + out2 + "\n"
    write_run_files(run_dir, out_blob, ev_obj, {**meta_base, "mode": "auto", "system_prompt": system_prompt, "user_prompt": user_prompt})
    index_append(run_dir, ev_obj, safe_stop=bool(ev.get("safe_stop", False)))

    # Self-improvement: compare last runs and write policy_next.yaml if needed
    idx = read_index()
    summary = summarize_runs(idx, last_n=10)
    improved, reasons = improve_policy(policy, summary)
    if reasons:
        dump_yaml(POLICY_NEXT, improved)

    # Optional one auto-retry if FAIL and we created policy_next.yaml
    auto_retry = True
    if auto_retry and ev_obj["result"] == "FAIL" and POLICY_NEXT.exists():
        improved_policy = load_yaml(POLICY_NEXT)
        # only retry if improvements exist and won't loop; do it once
        out1b = openai_chat(*build_prompts(improved_policy), temperature=temp)
        time.sleep(1)
        out2b = openai_chat(*build_prompts(improved_policy), temperature=temp)
        evb = evaluate(improved_policy, out1b, out2b)
        ev_obj_b = make_eval_obj(improved_policy, evb)

        run_dir_b = create_run_folder()
        out_blob_b = "=== OUTPUT (run1) ===\n" + out1b + "\n\n=== OUTPUT (run2) ===\n" + out2b + "\n"
        write_run_files(run_dir_b, out_blob_b, ev_obj_b, {**meta_base, "mode": "auto-retry", "used_policy": "policy_next.yaml"})
        index_append(run_dir_b, ev_obj_b, safe_stop=bool(evb.get("safe_stop", False)))

    # Print final status
    print("=== DONE ===")
    print(f"Run folder: {run_dir}")
    print(f"- {run_dir / 'output.txt'}")
    print(f"- {run_dir / 'eval.yaml'}")
    if POLICY_NEXT.exists():
        print(f"- Suggested next policy: {POLICY_NEXT}")
    if INDEX.exists():
        print(f"- Index: {INDEX}")

def main():
    ensure_dirs()
    if not POLICY.exists():
        die(f"policy.yaml not found: {POLICY}\nCreate it first in ~/mother-ai/policy.yaml")

    cmd = sys.argv[1] if len(sys.argv) > 1 else "run"
    policy = load_yaml(POLICY)

    if cmd == "run":
        cmd_run(policy)
    elif cmd == "prompt":
        cmd_prompt(policy)
    elif cmd == "report":
        cmd_report()
    else:
        die("Usage: ./mother.py [run|prompt|report]")

if __name__ == "__main__":
    main()
