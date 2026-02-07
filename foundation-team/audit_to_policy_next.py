#!/usr/bin/env python3
import os
from datetime import datetime

BASE = os.path.expanduser("~/foundation-team")
AUDIT_DIR = os.path.join(BASE, "foundation-leader-ai", "audits")

# ロール名 → ディレクトリ
ROLE_MAP = {
    "Java": "foundation-java-ai",
    "DB": "foundation-db-ai",
    "Security": "foundation-security-ai",
    "Test": "foundation-test-ai",
    "XML": "foundation-xml-ai",
    "Leader": "foundation-leader-ai",
}

def latest_audit():
    if not os.path.isdir(AUDIT_DIR):
        return None
    files = [f for f in os.listdir(AUDIT_DIR) if f.startswith("audit-") and f.endswith(".md")]
    if not files:
        return None
    files.sort(reverse=True)
    return os.path.join(AUDIT_DIR, files[0])

def detect_role(line):
    l = line.lower()
    if "java" in l: return "Java"
    if "security" in l: return "Security"
    if "db" in l or "sql" in l: return "DB"
    if "xml" in l: return "XML"
    if "leader" in l: return "Leader"
    return None

def append_policy_next(role_dir, items):
    path = os.path.join(BASE, role_dir, "policy_next.yaml")
    os.makedirs(os.path.dirname(path), exist_ok=True)

    with open(path, "a", encoding="utf-8") as f:
        if os.path.getsize(path) == 0:
            f.write("# AUTO-GENERATED POLICY NEXT (from audit)\n")
            f.write(f"# Generated: {datetime.now().isoformat()}\n")
            f.write("additional_constraints:\n")
        for it in items:
            f.write(f"  - {it}\n")

def main():
    audit = latest_audit()
    if not audit:
        print("❌ No audit file found")
        return

    print("Using audit:", audit)

    by_role = {k: [] for k in ROLE_MAP.keys()}

    with open(audit, "r", encoding="utf-8") as f:
        for line in f:
            if line.strip().startswith("- ["):
                role = detect_role(line)
                if role:
                    by_role[role].append(line.strip())

    for role, items in by_role.items():
        if not items:
            continue
        role_dir = ROLE_MAP[role]
        append_policy_next(role_dir, items)
        print(f"✅ policy_next updated: {role}")

    print("=== AUDIT → POLICY NEXT COMPLETED ===")

if __name__ == "__main__":
    main()
