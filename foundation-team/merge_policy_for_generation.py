#!/usr/bin/env python3
import os
from datetime import datetime

BASE_DIR = os.getcwd()

ROLES = [
    "foundation-java-ai",
    "foundation-db-ai",
    "foundation-security-ai",
    "foundation-test-ai",
    "foundation-xml-ai",
    "foundation-leader-ai",
]

MERGED_NAME = "policy_merged.txt"

def merge_policy(role_dir):
    policy = os.path.join(role_dir, "policy.yaml")
    policy_next = os.path.join(role_dir, "policy_next.yaml")
    merged = os.path.join(role_dir, MERGED_NAME)

    if not os.path.isfile(policy):
        return False, "policy.yaml not found"

    with open(policy, "r", encoding="utf-8") as f:
        base = f.read()

    next_txt = ""
    if os.path.isfile(policy_next):
        with open(policy_next, "r", encoding="utf-8") as f:
            next_txt = f.read()

    with open(merged, "w", encoding="utf-8") as f:
        f.write("### BASE POLICY (immutable)\n")
        f.write(base)
        f.write("\n\n")

        if next_txt.strip():
            f.write("### ADDITIONAL CONSTRAINTS (from policy_next)\n")
            f.write(f"# merged_at: {datetime.now().isoformat()}\n")
            f.write(next_txt)
            f.write("\n")

    return True, merged

def main():
    print("=== POLICY MERGE FOR GENERATION ===")

    for role in ROLES:
        role_dir = os.path.join(BASE_DIR, role)
        if not os.path.isdir(role_dir):
            continue

        ok, info = merge_policy(role_dir)
        if ok:
            print(f"✅ merged: {role}/{MERGED_NAME}")
        else:
            print(f"⚠ skip {role}: {info}")

    print("=== COMPLETED ===")

if __name__ == "__main__":
    main()
