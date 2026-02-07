#!/usr/bin/env python3
import os
from datetime import datetime

BASE = os.path.expanduser("~/foundation-team")

ROLES = {
    "Java": "foundation-java-ai",
    "DB": "foundation-db-ai",
    "Security": "foundation-security-ai",
    "Test": "foundation-test-ai",
    "XML": "foundation-xml-ai",
}

def latest_output(role_dir):
    runs = os.path.join(BASE, role_dir, "runs")
    if not os.path.isdir(runs):
        return None, "runs/ not found"
    items = [d for d in os.listdir(runs) if d.startswith("run-")]
    if not items:
        return None, "no runs"
    items.sort(reverse=True)
    out = os.path.join(runs, items[0], "output.txt")
    if not os.path.isfile(out):
        return None, "output.txt not found"
    with open(out, "r", encoding="utf-8") as f:
        return f.read().strip(), items[0]

def summarize(text, max_lines=6):
    lines = [l for l in text.splitlines() if l.strip()]
    if not lines:
        return ["(no content)"]
    return lines[:max_lines]

def main():
    now = datetime.now().strftime("%Y-%m-%d %H:%M")
    report = []
    report.append("# Foundation Team Progress Report")
    report.append(f"- Generated: {now}")
    report.append("")

    blockers = []
    next_actions = []

    for role, dirn in ROLES.items():
        content, meta = latest_output(dirn)
        report.append(f"## {role}")
        report.append(f"- Latest run: {meta}")
        if content is None:
            report.append("- Status: ❌ No output")
            report.append("")
            blockers.append(f"{role}: no output")
            continue

        bullets = summarize(content)
        report.append("- Highlights:")
        for b in bullets:
            report.append(f"  - {b}")

        lc = content.lower()
        if any(k in lc for k in ["todo", "tbd", "未", "課題", "ng", "error"]):
            blockers.append(f"{role}: unresolved items")
        if any(k in lc for k in ["next", "次", "対応", "実装", "review"]):
            next_actions.append(f"{role}: follow-up required")

        report.append("")

    report.append("## Overall Status")
    if blockers:
        report.append("- Blockers:")
        for b in blockers:
            report.append(f"  - {b}")
    else:
        report.append("- Blockers: none")

    report.append("")
    report.append("## Next Actions")
    if next_actions:
        for n in next_actions:
            report.append(f"- {n}")
    else:
        report.append("- No immediate actions")

    out_dir = os.path.join(BASE, "foundation-leader-ai", "reports")
    os.makedirs(out_dir, exist_ok=True)
    out_file = os.path.join(out_dir, f"onepage-{datetime.now().strftime('%Y%m%d-%H%M%S')}.md")

    with open(out_file, "w", encoding="utf-8") as f:
        f.write("\\n".join(report))

    print("=== ONE-PAGE REPORT DONE ===")
    print("Output:", out_file)

if __name__ == "__main__":
    main()
