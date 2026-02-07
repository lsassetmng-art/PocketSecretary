#!/usr/bin/env python3
import os
from datetime import datetime

BASE_DIR = os.path.expanduser("~/foundation-team")
REVIEW_DIR = os.path.join(BASE_DIR, "foundation-leader-ai", "reviews")
OUT_DIR = os.path.join(BASE_DIR, "foundation-leader-ai", "reports")

ROLES = ["Java", "DB", "Security", "Test", "XML"]

def latest_review():
    files = [f for f in os.listdir(REVIEW_DIR) if f.startswith("cross-review-")]
    if not files:
        return None
    files.sort(reverse=True)
    return os.path.join(REVIEW_DIR, files[0])

def parse_ng(md):
    result = {r: [] for r in ROLES}
    current_target = None

    for line in md.splitlines():
        line = line.strip()
        if line.startswith("### Review Target:"):
            current_target = line.split(":")[1].strip()
        elif current_target and line.startswith("-") and "ng" in line.lower():
            result[current_target].append(line.replace("-", "").strip())

    return result

def main():
    review = latest_review()
    if not review:
        print("❌ No review found")
        return

    with open(review, "r", encoding="utf-8") as f:
        md = f.read()

    ng = parse_ng(md)

    os.makedirs(OUT_DIR, exist_ok=True)
    out_file = os.path.join(
        OUT_DIR,
        f"leader-ng-summary-{datetime.now().strftime('%Y%m%d-%H%M%S')}.md"
    )

    with open(out_file, "w", encoding="utf-8") as f:
        f.write("# Leader NG Summary\n")
        f.write(f"- Source: {os.path.basename(review)}\n")
        f.write(f"- Generated: {datetime.now().isoformat()}\n\n")

        for role, items in ng.items():
            f.write(f"## {role}\n")
            if not items:
                f.write("- ✅ No NG detected\n\n")
                continue
            for i in set(items):
                f.write(f"- ❌ {i}\n")
            f.write("\n")

    print("=== LEADER NG SUMMARY GENERATED ===")
    print("Output:", out_file)

if __name__ == "__main__":
    main()
