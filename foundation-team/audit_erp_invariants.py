#!/usr/bin/env python3
import os
import re
from datetime import datetime

BASE_DIR = os.getcwd()

# スキャン対象（必要に応じて追加）
SCAN_DIRS = [
    "app",
    "db",
    "supabase",
]

# 検出ルール
RULES = [
    {
        "id": "COMPANY_ID_ARG",
        "desc": "company_id passed as method argument",
        "pattern": re.compile(r"\bcompany_id\s*,|\bcompany_id\s*\)"),
        "severity": "HIGH",
    },
    {
        "id": "COMPANY_ID_DIRECT",
        "desc": "direct company_id reference (non-SessionManager)",
        "pattern": re.compile(r"\bcompany_id\b"),
        "severity": "HIGH",
    },
    {
        "id": "SQL_COMPANY_FILTER",
        "desc": "application-side company_id SQL filter",
        "pattern": re.compile(r"WHERE\s+.*company_id", re.IGNORECASE),
        "severity": "HIGH",
    },
    {
        "id": "SQLITE_SOURCE_OF_TRUTH",
        "desc": "SQLite treated as source of truth",
        "pattern": re.compile(r"sqlite.*(master|source|truth)", re.IGNORECASE),
        "severity": "MEDIUM",
    },
    {
        "id": "SUPABASE_BYPASS",
        "desc": "Supabase RLS bypass indicator",
        "pattern": re.compile(r"(service_key|admin key|bypass rls)", re.IGNORECASE),
        "severity": "CRITICAL",
    },
]

EXCLUDE_PATTERNS = [
    re.compile(r"SessionManager\.getCompanyId"),
]

RESULTS = []

def should_exclude(line):
    return any(p.search(line) for p in EXCLUDE_PATTERNS)

def scan_file(path):
    with open(path, "r", errors="ignore") as f:
        for i, line in enumerate(f, start=1):
            if should_exclude(line):
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

def main():
    print("=== ERP INVARIANTS AUDIT START ===")

    for d in SCAN_DIRS:
        root = os.path.join(BASE_DIR, d)
        if not os.path.isdir(root):
            continue
        for dirpath, _, filenames in os.walk(root):
            for fn in filenames:
                if fn.endswith((".java", ".kt", ".xml", ".sql", ".md", ".txt")):
                    scan_file(os.path.join(dirpath, fn))

    out_dir = os.path.join(BASE_DIR, "foundation-leader-ai", "audits")
    os.makedirs(out_dir, exist_ok=True)
    out_file = os.path.join(
        out_dir,
        f"audit-{datetime.now().strftime('%Y%m%d-%H%M%S')}.md"
    )

    with open(out_file, "w", encoding="utf-8") as f:
        f.write("# ERP Invariants Audit Report\n")
        f.write(f"- Generated: {datetime.now().isoformat()}\n\n")

        if not RESULTS:
            f.write("✅ No violations detected.\n")
        else:
            for r in RESULTS:
                f.write(
                    f"- [{r['severity']}] {r['rule']} "
                    f"{r['file']}:{r['line']}\n"
                    f"  `{r['content']}`\n"
                )

    print("=== AUDIT COMPLETED ===")
    print("Output:", out_file)

if __name__ == "__main__":
    main()
