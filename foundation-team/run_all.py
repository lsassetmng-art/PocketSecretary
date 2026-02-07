#!/usr/bin/env python3
import os
import json
import time
import requests
from datetime import datetime

# =========================
# Global Config
# =========================
API_KEY = os.environ.get("OPENAI_API_KEY")
if not API_KEY:
    print("OPENAI_API_KEY 未設定のため自動実行できません。")
    exit(1)

MODEL = "gpt-4o-mini"
API_URL = "https://api.openai.com/v1/chat/completions"

BASE_DIR = os.path.expanduser("~/foundation-team")

ROLES = [
    "foundation-java-ai",
    "foundation-db-ai",
    "foundation-security-ai",
    "foundation-test-ai",
    "foundation-xml-ai",
    "foundation-leader-ai",
]

SLEEP_SECONDS = 20  # rate limit safety

# =========================
# Core Logic
# =========================
def run_role(role_dir: str):
    role_path = os.path.join(BASE_DIR, role_dir)
    policy_path = os.path.join(role_path, "policy.yaml")
    runs_dir = os.path.join(role_path, "runs")

    if not os.path.exists(policy_path):
        print(f"[SKIP] policy.yaml not found: {role_dir}")
        return

    os.makedirs(runs_dir, exist_ok=True)

    with open(policy_path, "r", encoding="utf-8") as f:
        policy = f.read()

    run_id = datetime.now().strftime("run-%Y%m%d-%H%M%S")
    run_dir = os.path.join(runs_dir, run_id)
    os.makedirs(run_dir, exist_ok=True)

    output_file = os.path.join(run_dir, "output.txt")
    meta_file = os.path.join(run_dir, "meta.json")

    messages = [
        {"role": "system", "content": policy},
        {"role": "user", "content": "方針に従い、実行可能な成果物のみを生成せよ。"},
    ]

    print(f"\n=== RUNNING ROLE: {role_dir} ===")

    response = requests.post(
        API_URL,
        headers={
            "Authorization": f"Bearer {API_KEY}",
            "Content-Type": "application/json",
        },
        json={
            "model": MODEL,
            "messages": messages,
            "temperature": 0,
        },
        timeout=60,
    )

    if response.status_code != 200:
        print("[ERROR]", role_dir, response.status_code)
        print(response.text)
        return

    data = response.json()
    content = data["choices"][0]["message"]["content"]

    # remove ``` if present
    if content.startswith("```"):
        content = content.split("```")[1]

    with open(output_file, "w", encoding="utf-8") as f:
        f.write(content)

    with open(meta_file, "w", encoding="utf-8") as f:
        json.dump(
            {
                "role": role_dir,
                "run_id": run_id,
                "model": MODEL,
                "time": datetime.now().isoformat(),
            },
            f,
            indent=2,
            ensure_ascii=False,
        )

    print("DONE:", role_dir)
    print(" output:", output_file)

# =========================
# Entry Point
# =========================
print("=== FOUNDATION TEAM : RUN ALL (Python) ===")

for role in ROLES:
    run_role(role)
    time.sleep(SLEEP_SECONDS)

print("\n=== ALL ROLES COMPLETED ===")
