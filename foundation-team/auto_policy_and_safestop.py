#!/usr/bin/env python3
import os
import re
from datetime import datetime

BASE_DIR = os.path.expanduser("~/foundation-team")
REVIEW_DIR = os.path.join(BASE_DIR, "foundation-leader-ai", "reviews")

ROLES = {
    "Java": "foundation-java-ai",
    "DB": "foundation-db-ai",
    "Security": "foundation-security-ai",
    "Test": "foundation-test-ai",
    "XML": "foundation-xml-ai",
}

NG_LIMIT = 3  # この回数を超えたら safe-stop

def latest_review():
    files = [f for f in os.listdir(REVIEW_DIR) if f.startswith("cross-review-")]
    if not files:
        return None
    files.sort(reverse=True)
    return os.path.join(REVIEW_DIR, files[0])

def parse_review(md_text):
    """
    return:
      issues = {
        "Java": ["ng", "missing check"],
        ...
      }
    """
    issues = {}
    current_target = None

    for line in md_text.splitlines():
        if line.startswith("### Review Target:"):
            current_target = line.split(":")[1].strip()
            issues.setdefault(current_target, [])
        elif current_target:
            if "Status: REVIEW_NEEDED" in line or "- ng" in line.lower():
                issues[current_target].append("ng")

    return issues

def ng_count_file(role_dir):
    return os.path.join(role_dir, ".ng_count")

def load_ng_count(role_dir):
    path = ng_count_file(role_dir)
    if not os.path.exists(path):
        return 0
    with open(path, "r") as f:
        return int(f.read().strip())

def save_ng_count(role_dir, count):
    with open(ng_count_file(role_dir), "w") as f:
        f.write(str(count))

def write_policy_next(role_dir, role_name, issues):
    policy_next = os.path.join(role_dir, "policy_next.yaml")
    with open(policy_next, "w", encoding="utf-8") as f:
        f.write("# AUTO GENERATED POLICY NEXT\n")
        f.write(f"# Role: {role_name}\n")
        f.write(f"# Generated: {datetime.now().isoformat()}\n")
        f.write("additional_constraints:\n")
        for i in issues:
            f.write(f"  - {i}\n")

def main():
    review_file = latest_review()
    if not review_file:
        print("❌ No review file found")
        return

    with open(review_file, "r", encoding="utf-8") as f:
        md_text = f.read()

    issues = parse_review(md_text)

    print("=== AUTO POLICY NEXT & SAFE-STOP ===")

    for role_name, dir_name in ROLES.items():
        role_dir = os.path.join(BASE_DIR, dir_name)
        role_issues = issues.get(role_name, [])

        if not role_issues:
            # NGなし → カウンタリセット
            save_ng_count(role_dir, 0)
            print(f"✅ {role_name}: OK (counter reset)")
            continue

        # NGあり
        count = load_ng_count(role_dir) + 1
        save_ng_count(role_dir, count)

        if count >= NG_LIMIT:
            print(f"🛑 {role_name}: SAFE-STOP (NG x {count})")
            continue

        write_policy_next(role_dir, role_name, role_issues)
        print(f"⚠ {role_name}: policy_next.yaml generated (NG x {count})")

    print("\n=== COMPLETED ===")

if __name__ == "__main__":
    main()
