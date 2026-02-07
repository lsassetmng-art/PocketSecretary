#!/usr/bin/env python3
import os
import re
import sys
from datetime import datetime

ROOT = os.getcwd()
REQ = os.path.join(ROOT, "pm_ai", "requirements.md")
POL = os.path.join(ROOT, "pm_ai", "pm_policy.yaml")
OUT_SPEC = os.path.join(ROOT, "spec", "usecases.schema.yaml")
OUT_MAP = os.path.join(ROOT, "reports", "usecase_map.yaml")
OUT_INSTR = os.path.join(ROOT, "reports", "team_instructions.md")

os.makedirs(os.path.dirname(OUT_SPEC), exist_ok=True)
os.makedirs(os.path.dirname(OUT_MAP), exist_ok=True)
os.makedirs(os.path.dirname(OUT_INSTR), exist_ok=True)

FORBID = {"company_id","user_id","sql","sqlite","supabase","http","https","table","ddl"}

# domain hint: [domain:xxx]
DOMAIN_HINT_RE = re.compile(r"^\s*-\s*\[domain:([a-zA-Z0-9_-]+)\]\s*(.*)$")

# Very small deterministic mapper: JP phrase -> (domain,name,type)
# Keep conservative; you can extend mappings later.
MAPPINGS = [
    (r"(ログイン|認証)", "auth", "AuthenticateUser", "command"),
    (r"(ログアウト)", "auth", "Logout", "command"),
    (r"(セッション).*(更新|リフレッシュ)", "auth", "RefreshSession", "command"),
    (r"(セッション).*(取得|確認)", "auth", "GetCurrentSession", "query"),

    (r"(受注).*(登録|作成)", "order", "CreateOrder", "command"),
    (r"(受注).*(確定)", "order", "ConfirmOrder", "command"),
    (r"(受注).*(取消|キャンセル)", "order", "CancelOrder", "command"),
    (r"(受注).*(一覧)", "order", "GetOrderList", "query"),
    (r"(受注).*(詳細)", "order", "GetOrderDetail", "query"),

    (r"(出荷).*(登録|作成)", "shipping", "CreateShipping", "command"),
    (r"(出荷).*(完了|確定)", "shipping", "CompleteShipping", "command"),
    (r"(出荷).*(一覧)", "shipping", "GetShippingList", "query"),
    (r"(出荷).*(詳細)", "shipping", "GetShippingDetail", "query"),

    (r"(請求書|請求).*(作成|発行)", "billing", "CreateInvoice", "command"),
    (r"(請求書|請求).*(確定|締め|確定する)", "billing", "FinalizeInvoice", "command"),
    (r"(請求書|請求).*(一覧)", "billing", "GetInvoiceList", "query"),
    (r"(請求書|請求).*(詳細)", "billing", "GetInvoiceDetail", "query"),

    (r"(通知).*(一覧)", "notification", "GetNotificationList", "query"),
    (r"(通知).*(既読)", "notification", "MarkNotificationAsRead", "command"),

    (r"(監査|証跡).*(一覧|取得)", "audit", "GetAuditTrail", "query"),
    (r"(操作ログ).*(一覧|取得)", "audit", "GetOperationLog", "query"),
]

def forbid_check(text: str) -> None:
    low = text.lower()
    for t in FORBID:
        if t in low:
            raise SystemExit(f"FORBIDDEN token detected in requirements: {t}")

def detect(line: str, domain_hint: str | None):
    text = line.strip()
    if not text:
        return None
    # strip leading "- "
    text = re.sub(r"^\s*-\s*", "", text)
    forbid_check(text)

    # mapping match
    for pat, dom, name, typ in MAPPINGS:
        if re.search(pat, text):
            if domain_hint and domain_hint != dom:
                # Respect explicit hint by overriding domain only if allowed; name stays.
                dom = domain_hint
            return {"domain": dom, "name": name, "type": typ, "source": text}

    # fallback: if cannot map, produce a safe placeholder query under hinted domain or system
    # We keep deterministic: UnknownRequirement1,2...
    return {"domain": (domain_hint or "system"), "name": None, "type": "query", "source": text}

def pascal_safe(s: str) -> str:
    s = re.sub(r"[^A-Za-z0-9]+", " ", s).title().replace(" ", "")
    if not s:
        s = "Unknown"
    if not re.match(r"^[A-Za-z]", s):
        s = "U" + s
    return s

def main():
    if not os.path.isfile(REQ):
        raise SystemExit("pm_ai/requirements.md not found")

    lines = open(REQ, "r", encoding="utf-8").read().splitlines()

    unknown_count = 0
    usecases = []
    mapping_rows = []
    seen = set()

    for raw in lines:
        raw = raw.rstrip()
        if not raw or raw.strip().startswith("#"):
            continue

        domain_hint = None
        body = raw

        m = DOMAIN_HINT_RE.match(raw)
        if m:
            domain_hint = m.group(1).strip()
            body = "- " + m.group(2).strip()

        d = detect(body, domain_hint)
        if not d:
            continue

        if d["name"] is None:
            unknown_count += 1
            d["name"] = f"UnknownRequirement{unknown_count}"

        key = (d["domain"], d["name"])
        if key in seen:
            mapping_rows.append((d["source"], d["domain"], d["name"], d["type"], "dedup"))
            continue

        seen.add(key)
        usecases.append({"domain": d["domain"], "name": d["name"], "type": d["type"]})
        mapping_rows.append((d["source"], d["domain"], d["name"], d["type"], "ok"))

    # Write spec/usecases.schema.yaml (no extra fields)
    with open(OUT_SPEC, "w", encoding="utf-8") as f:
        f.write("usecases:\n\n")
        for uc in usecases:
            f.write(f"  - domain: {uc['domain']}\n")
            f.write(f"    name: {uc['name']}\n")
            f.write(f"    type: {uc['type']}\n\n")

    # Write reports/usecase_map.yaml
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    with open(OUT_MAP, "w", encoding="utf-8") as f:
        f.write(f"generated_at: \"{now}\"\n")
        f.write("items:\n")
        for src, dom, name, typ, st in mapping_rows:
            f.write("  - requirement: |\n")
            f.write("      " + src.replace("\n"," ") + "\n")
            f.write(f"    domain: {dom}\n")
            f.write(f"    usecase: {name}\n")
            f.write(f"    type: {typ}\n")
            f.write(f"    status: {st}\n")

    # Write reports/team_instructions.md
    teams = {}
    for uc in usecases:
        teams.setdefault(uc["domain"], []).append(uc["name"])

    with open(OUT_INSTR, "w", encoding="utf-8") as f:
        f.write(f"# Team Instructions (Auto)\n\nGenerated: {now}\n\n")
        for dom in sorted(teams.keys()):
            f.write(f"## {dom} team\n")
            for name in teams[dom]:
                f.write(f"- Implement UseCase: {name}\n")
            f.write("\n")

    print("OK: Generated")
    print(f"- {OUT_SPEC}")
    print(f"- {OUT_MAP}")
    print(f"- {OUT_INSTR}")

if __name__ == "__main__":
    main()
