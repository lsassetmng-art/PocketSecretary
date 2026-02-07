#!/usr/bin/env python3
import os
import re
from datetime import datetime

BASE = os.getcwd()

SCAN_DIRS = [
    "app/src/main/java",
    "app/src/main/kotlin",
]

RULES = [
    {
        "id": "COMPANY_ID_ARG",
        "desc": "company_id is passed as method/constructor argument",
        "pattern": re.compile(r"\b(companyId|company_id)\b\s*[,\)]"),
        "severity": "HIGH",
    },
    {
        "id": "COMPANY_ID_DIRECT",
        "desc": "direct company_id reference (non-SessionManager)",
        "pattern": re.compile(r"\b(companyId|company_id)\b"),
        "severity": "HIGH",
    },
    {
        "id": "SUPABASE_COMPANY_SEND",
        "desc": "company_id sent to Supabase/API",
        "pattern": re.compile(r"(put|add)\s*\(\s*\"company_id\"", re.IGNORECASE),
        "severity": "CRITICAL",
    },
]

ALLOW_PATTERNS = [
    re.compile(r"SessionManager\.requireCompanyId"),
    re.compile(r"SessionManager\.initCompanyId"),
    re.compile(r"SessionManager\.clear"),
]

RESULTS = []

def is_allowed(line):
    return any(p.search(line) for p in ALLOW_PATTERNS)

def scan_file(path):
    try:
        with open(path, "r", encoding="utf-8") as f:
            for i, line in enumerate(f, start=1):
                if is_allowed(line):
                    continue
                for rule in RULES:
                    if rule["pattern"].search(line):
                        RESULTS.append({
                            "file": path,
                            "line": i,
                            "rule": rule["id"],
                            "severity": rule["severity"],
                            "content": line.strip(),
                        })
    except Exception:
        pass

def main():
    print("=== PHASE1 AUDIT: SessionManager Invariant ===")

    for d in SCAN_DIRS:
        root = os.path.join(BASE, d)
        if not os.path.isdir(root):
            continue
        for dirpath, _, filenames in os.walk(root):
            for fn in filenames:
                if fn.endswith((".java", ".kt")):
                    scan_file(os.path.join(dirpath, fn))

    out_dir = os.path.join(BASE, "foundation-leader-ai", "audits")
    os.makedirs(out_dir, exist_ok=True)
    out_file = os.path.join(
        out_dir,
        f"audit-phase1-{datetime.now().strftime('%Y%m%d-%H%M%S')}.md"
    )

    with open(out_file, "w", encoding="utf-8") as f:
        f.write("# Phase1 Audit Report (SessionManager)\n")
        f.write(f"- Generated: {datetime.now().isoformat()}\n\n")

        if not RESULTS:
            f.write("✅ Phase1 invariant satisfied. No violations found.\n")
        else:
            f.write("❌ Phase1 violations detected:\n\n")
            for r in RESULTS:
                f.write(
                    f"- [{r['severity']}] {r['rule']} "
                    f"{r['file']}:{r['line']}\n"
                    f"  `{r['content']}`\n"
                )

    print("=== AUDIT COMPLETED ===")
    print("Output:", out_file)

    if RESULTS:
        print("❗ Phase1 NOT clean")
    else:
        print("✅ Phase1 CLEAN")

if __name__ == "__main__":
    main()
