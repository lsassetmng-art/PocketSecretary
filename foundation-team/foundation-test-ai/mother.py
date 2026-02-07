#!/usr/bin/env python3
import os, yaml
from pathlib import Path
from datetime import datetime
from openai import OpenAI

BASE_DIR = Path(__file__).parent
POLICY_FILE = BASE_DIR / "policy" / "policy.yaml"
TASK_FILE   = BASE_DIR / "task.yaml"
RUNS_DIR    = BASE_DIR / "runs"

def main():
    if not POLICY_FILE.exists():
        raise RuntimeError("policy.yaml not found")
    if not TASK_FILE.exists():
        raise RuntimeError("task.yaml not found")
    if "OPENAI_API_KEY" not in os.environ:
        raise RuntimeError("OPENAI_API_KEY not set")

    RUNS_DIR.mkdir(exist_ok=True)

    with open(POLICY_FILE, "r", encoding="utf-8") as f:
        policy = yaml.safe_load(f)
    with open(TASK_FILE, "r", encoding="utf-8") as f:
        task = yaml.safe_load(f)

    ts = datetime.now().strftime("%Y%m%d-%H%M%S")
    run_dir = RUNS_DIR / f"run-{ts}"
    run_dir.mkdir()

    prompt = (
        "=== SYSTEM ===\n" + yaml.dump(policy, allow_unicode=True) +
        "\n=== TASK ===\n" + yaml.dump(task, allow_unicode=True)
    )

    client = OpenAI()

    resp = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            {"role": "system", "content": "You are a professional engineer for this role."},
            {"role": "user", "content": prompt}
        ],
        temperature=0.1,
    )

    content = resp.choices[0].message.content

    with open(run_dir / "output.txt", "w", encoding="utf-8") as f:
        f.write(content)

    with open(run_dir / "eval.yaml", "w", encoding="utf-8") as f:
        f.write("result: GENERATED\n")

    print(f"[OK] Generated: {run_dir}")

if __name__ == "__main__":
    main()
