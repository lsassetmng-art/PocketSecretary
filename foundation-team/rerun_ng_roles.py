#!/usr/bin/env python3
import os
import time

BASE = os.path.expanduser("~/foundation-team")

ROLE_MAP = {
    "Java": "foundation-java-ai",
    "Security": "foundation-security-ai",
    "DB": "foundation-db-ai",
}

SLEEP_SECONDS = 20

REVIEWS_DIR = os.path.join(BASE, "foundation-leader-ai", "reviews")

def latest_review():
    if not os.path.isdir(REVIEWS_DIR):
        return None
    items = [f for f in os.listdir(REVIEWS_DIR) if f.startswith("cross-review-")]
    if not items:
        return None
    items.sort(reverse=True)
    return os.path.join(REVIEWS_DIR, items[0])

def parse_ng_roles(review_file):
    ng_roles = set()
    current_target = None
    with open(review_file, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if line.startswith("### Review Target:"):
                current_target = line.split(":")[1].strip()
            elif line.startswith("- Status: REVIEW_NEEDED") and current_target:
                ng_roles.add(current_target)
    return list(ng_roles)

def rerun(role_name):
    role_dir = ROLE_MAP.get(role_name)
    if not role_dir:
        return
    path = os.path.join(BASE, role_dir)
    print(f"\n=== RE-RUN: {role_name} ===")
    os.chdir(path)
    os.system("python mother.py")

def main():
    review = latest_review()
    if not review:
        print("No review file found. Abort.")
        return

    print("Using review:", review)
    ng_roles = parse_ng_roles(review)

    if not ng_roles:
        print("No NG roles. Nothing to re-run.")
        return

    print("NG roles:", ", ".join(ng_roles))

    for r in ng_roles:
        rerun(r)
        time.sleep(SLEEP_SECONDS)

    print("\n=== NG ROLES RE-RUN COMPLETED ===")

if __name__ == "__main__":
    main()
