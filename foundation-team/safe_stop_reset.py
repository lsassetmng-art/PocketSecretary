#!/usr/bin/env python3
import os

BASE_DIR = os.path.expanduser("~/foundation-team")

ROLES = [
    "foundation-java-ai",
    "foundation-db-ai",
    "foundation-security-ai",
    "foundation-test-ai",
    "foundation-xml-ai",
    "foundation-leader-ai",
]

RESET_POLICY_NEXT = False  # True にすると policy_next.yaml も削除

def main():
    print("=== SAFE-STOP RESET START ===")

    for role in ROLES:
        role_dir = os.path.join(BASE_DIR, role)

        ng_file = os.path.join(role_dir, ".ng_count")
        policy_next = os.path.join(role_dir, "policy_next.yaml")

        if os.path.exists(ng_file):
            os.remove(ng_file)
            print(f"✔ reset ng_count: {role}")

        if RESET_POLICY_NEXT and os.path.exists(policy_next):
            os.remove(policy_next)
            print(f"✔ removed policy_next.yaml: {role}")

    print("=== SAFE-STOP RESET DONE ===")

if __name__ == "__main__":
    main()
