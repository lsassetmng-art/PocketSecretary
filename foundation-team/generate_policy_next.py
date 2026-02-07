#!/usr/bin/env python3
import os
import re
from datetime import datetime

BASE_DIR = os.getcwd()
LEADER_REVIEW_DIR = os.path.join(BASE_DIR, "foundation-leader-ai", "reviews")

ROLES = [
    "foundation-java-ai",
    "foundation-db-ai",
    "foundation-security-ai",
    "foundation-test-ai",
    "foundation-xml-ai",
    "foundation-leader-ai",
]

def latest_review_file():
    if not os.path.isdir(LEADER_REVIEW_DIR):
        print("❌ reviews directory not found")
        exit(1)

    files = [
        f for f in os.listdir(LEADER_REVIEW_DIR)
        if f.startswith("cross-review-") and f.endswith(".md")
    ]
    if not files:
        print("❌ cross-review file not found")
        exit(1)

    files.sort()
    return os.path.join(LEADER_REVIEW_DIR, files[-1])

def parse_review(md_text):
    results = {}
    current_reviewer = None
    current_target = None
    block = []

    for line in md_text.splitlines():
        if line.startswith("## Reviewer:"):
            current_reviewer = line.replace("## Reviewer:", "").strip()
        elif line.startswith("### Review Target:"):
            current_target = line.replace("### Review Target:", "").strip()
            block = []
        elif current_target:
            block.append(line)

            if "Status: REVIEW_NEEDED" in line or "- ng" in line:
                key = current_target.lower()
                results.setdefault(key, []).append(
                    f"Detected issue by {current_reviewer}"
                )

    return results

def write_policy_next(role_dir, issues):
    policy_path = os.path.join(BASE_DIR, role_dir, "policy_next.yaml")
    os.makedirs(os.path.dirname(policy_path), exist_ok=True)

    with open(policy_path, "w", encoding="utf-8") as f:
        f.write("=== POLICY NEXT ===\n")
        f.write(f"generated_at: {datetime.now().isoformat()}\n")
        f.write("status: IMPROVEMENT_REQUIRED\n")
        f.write("issues:\n")
        for issue in issues:
            f.write(f"  - {issue}\n")

    print(f"✅ policy_next.yaml generated -> {policy_path}")

def main():
    review_file = latest_review_file()
    with open(review_file, "r", encoding="utf-8") as f:
        md_text = f.read()

    parsed = parse_review(md_text)

    for role in ROLES:
        key = role.replace("foundation-", "").replace("-ai", "")
        issues = parsed.get(key, [])

        if issues:
            write_policy_next(role, issues)
        else:
            print(f"ℹ no issues for {role} (policy_next not required)")

    print("\n=== POLICY NEXT GENERATION COMPLETED ===")

if __name__ == "__main__":
    main()



