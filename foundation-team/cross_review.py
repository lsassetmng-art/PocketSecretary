#!/usr/bin/env python3
import os
from datetime import datetime

BASE = os.path.expanduser("~/foundation-team")

# レビュー対象（相互）
ROLES = {
    "Java": "foundation-java-ai",
    "Security": "foundation-security-ai",
    "DB": "foundation-db-ai",
}

# どの観点で相互レビューするか
REVIEW_MATRIX = {
    "Java": ["Security", "DB"],
    "Security": ["Java", "DB"],
    "DB": ["Java", "Security"],
}

KEYWORDS_RISK = ["todo", "tbd", "未", "課題", "ng", "error", "fixme"]
KEYWORDS_OK = ["ok", "pass", "complete", "完了", "対応済"]

def latest_output(role_dir):
    runs = os.path.join(BASE, role_dir, "runs")
    if not os.path.isdir(runs):
        return None, "runs not found"
    items = [d for d in os.listdir(runs) if d.startswith("run-")]
    if not items:
        return None, "no runs"
    items.sort(reverse=True)
    out = os.path.join(runs, items[0], "output.txt")
    if not os.path.isfile(out):
        return None, "output not found"
    with open(out, "r", encoding="utf-8") as f:
        return f.read(), items[0]

def assess(text):
    lc = text.lower()
    risks = [k for k in KEYWORDS_RISK if k in lc]
    oks = [k for k in KEYWORDS_OK if k in lc]
    status = "OK" if not risks else "REVIEW_NEEDED"
    return status, risks, oks

def main():
    now = datetime.now().strftime("%Y-%m-%d %H:%M")
    report = []
    report.append("# Cross Review Report (Java ↔ Security ↔ DB)")
    report.append(f"- Generated: {now}")
    report.append("")

    cache = {}
    for r, d in ROLES.items():
        content, meta = latest_output(d)
        cache[r] = (content, meta)

    for src, targets in REVIEW_MATRIX.items():
        report.append(f"## Reviewer: {src}")
        src_content, src_meta = cache[src]
        if src_content is None:
            report.append(f"- Source missing: {src_meta}")
            report.append("")
            continue

        for tgt in targets:
            tgt_content, tgt_meta = cache[tgt]
            report.append(f"### Review Target: {tgt}")
            if tgt_content is None:
                report.append(f"- Target missing: {tgt_meta}")
                continue

            status, risks, oks = assess(tgt_content)
            report.append(f"- Latest run: {tgt_meta}")
            report.append(f"- Status: {status}")
            if risks:
                report.append("- Risks:")
                for r in risks:
                    report.append(f"  - {r}")
            else:
                report.append("- Risks: none")

            if oks:
                report.append("- Positives:")
                for o in oks[:5]:
                    report.append(f"  - {o}")

            report.append("")

        report.append("")

    out_dir = os.path.join(BASE, "foundation-leader-ai", "reviews")
    os.makedirs(out_dir, exist_ok=True)
    out_file = os.path.join(out_dir, f"cross-review-{datetime.now().strftime('%Y%m%d-%H%M%S')}.md")

    with open(out_file, "w", encoding="utf-8") as f:
        f.write("\n".join(report))

    print("=== CROSS REVIEW DONE ===")
    print("Output:", out_file)

if __name__ == "__main__":
    main()
