#!/usr/bin/env python3
import os
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

INVARIANTS = """【ERP 共通基盤の不変条件（変更不可）】

1. company_id は SessionManager 以外から取得してはならない
   - 引数渡し・Repository直参照・static参照は禁止
   - 未初期化時は Fail Fast（例外）

2. Supabase RLS を前提とする
   - アプリ側で company_id を多重フィルタしない
   - service key / RLS 回避は禁止

3. SQLite はキャッシュ用途のみ
   - 正本データ不可
   - 業務判断の根拠に使用禁止

これらに違反する実装・提案・レビューは即 NG とする。
"""

MARKER_START = "### ERP INVARIANTS START"
MARKER_END   = "### ERP INVARIANTS END"

def inject(policy_path):
    with open(policy_path, "r", encoding="utf-8") as f:
        original = f.read()

    # すでに注入済みならスキップ
    if MARKER_START in original:
        return False

    header = (
        f"{MARKER_START}\n"
        f"# injected_at: {datetime.now().isoformat()}\n"
        f"{INVARIANTS}\n"
        f"{MARKER_END}\n\n"
    )

    with open(policy_path, "w", encoding="utf-8") as f:
        f.write(header + original)

    return True

def main():
    print("=== INJECT ERP INVARIANTS START ===")

    for role in ROLES:
        policy = os.path.join(BASE_DIR, role, "policy.yaml")
        if not os.path.isfile(policy):
            print(f"⚠ SKIP {role}: policy.yaml not found")
            continue

        injected = inject(policy)
        if injected:
            print(f"✅ injected: {role}")
        else:
            print(f"ℹ already injected: {role}")

    print("=== COMPLETED ===")

if __name__ == "__main__":
    main()
