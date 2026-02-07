#!/usr/bin/env python3
import yaml
from pathlib import Path
from datetime import datetime

BASE_DIR = Path(__file__).parent
POLICY_FILE = BASE_DIR / "policy" / "policy.yaml"
RUNS_DIR = BASE_DIR / "runs"

def main():
    if not POLICY_FILE.exists():
        raise RuntimeError("policy.yaml not found")

    RUNS_DIR.mkdir(exist_ok=True)

    with open(POLICY_FILE, "r", encoding="utf-8") as f:
        policy = yaml.safe_load(f)

    ts = datetime.now().strftime("%Y%m%d-%H%M%S")
    run_dir = RUNS_DIR / f"run-{ts}"
    run_dir.mkdir()

    with open(run_dir / "output.txt", "w", encoding="utf-8") as f:
        f.write("=== SYSTEM ===\n")
        f.write(yaml.dump(policy, allow_unicode=True))

    with open(run_dir / "eval.yaml", "w", encoding="utf-8") as f:
        f.write("result: NOT_EXECUTED\n")

    print(f"Prepared run: {run_dir}")

if __name__ == "__main__":
    main()
