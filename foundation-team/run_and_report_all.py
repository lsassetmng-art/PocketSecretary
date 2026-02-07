#!/usr/bin/env python3
import os
import subprocess
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

REPORT_DIR = os.path.join(BASE_DIR, "reports")
os.makedirs(REPORT_DIR, exist_ok=True)

def latest_run(role_dir):
    runs_dir = os.path.join(role_dir, "runs")
    if not os.path.isdir(runs_dir):
        return None
    runs = sorted(
        [d for d in os.listdir(runs_dir) if d.startswith("run-")],
        reverse=True
    )
    return runs[0] if runs else None

def rerun_role(role):
    role_dir = os.path.join(BASE_DIR, role)
    policy_next = os.path.join(role_dir, "policy_next.yaml")
    mother_py = os.path.join(role_dir, "mother.py")

    if not os.path.isfile(policy_next):
        return "SKIP", None

    if not os.path.isfile(mother_py):
        return "ERROR", None

    try:
        subprocess.run(
            ["python", "mother.py"],
            cwd=role_dir,
            check=True
        )
        return "OK", latest_run(role_dir)
    except subprocess.CalledProcessError:
        return "FAILED", latest_run(role_dir)

def main():
    now = datetime.now()
    report_name = f"cross-review-{now.strftime('%Y%m%d-%H%M%S')}.md"
    report_path = os.path.join(REPORT_DIR, report_name)

    results = {}

    print("=== RERUN & REPORT START ===")
    for role in ROLES:
        status, run = rerun_role(role)
        results[role] = {
            "status": status,
            "run": run
        }
        print(f"{role}: {status}")

    # =========================
    # Generate one-page report
    # =========================
    with open(report_path, "w", encoding="utf-8") as f:
        f.write(f"# Cross Review Report (All Roles)\n")
        f.write(f"- Generated: {now.strftime('%Y-%m-%d %H:%M:%S')}\n\n")

        for role, info in results.items():
            f.write(f"## Role: {role}\n")
            f.write(f"- Status: {info['status']}\n")
            if info["run"]:
                f.write(f"- Latest run: {info['run']}\n")
            else:
                f.write(f"- Latest run: none\n")

            if info["status"] == "OK":
                f.write(f"- Positives:\n  - pass\n")
            elif info["status"] == "SKIP":
                f.write(f"- Notes:\n  - policy_next.yaml not found\n")
            else:
                f.write(f"- Risks:\n  - ng\n")
            f.write("\n")

    print("\n=== REPORT GENERATED ===")
    print(report_path)
    print("=== ALL DONE ===")

if __name__ == "__main__":
    main()
