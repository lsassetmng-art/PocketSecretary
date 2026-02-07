#!/bin/sh
set -eu

ROOT="$HOME/erp-foundation"
RULE_DIR="$ROOT/pm_ai/rules"
RULE_FILE="$RULE_DIR/10_master_management.md"

# 1) ルート存在チェック（安全停止）
if [ ! -d "$ROOT" ]; then
  echo "❌ erp-foundation not found: $ROOT"
  exit 1
fi

# 2) rules ディレクトリ作成（既存は壊さない）
mkdir -p "$RULE_DIR"

# 3) 既に存在するなら何もしない（非破壊）
if [ -f "$RULE_FILE" ]; then
  echo "↪ rule already exists; skip: $RULE_FILE"
  exit 0
fi

# 4) ルール作成（catのみ）
cat <<'EOT' > "$RULE_FILE"
# PM指示書：マスタ管理（全業務共通）

## 1. 位置づけ
- マスタ管理は全業務（販売・購買・在庫・会計・人事等）の基盤である
- 業務トランザクションから直接編集してはならない

## 2. 対象マスタ（例）
- 商品マスタ
- 取引先マスタ
- 倉庫マスタ
- 勘定科目マスタ
- 税区分マスタ
- 部門マスタ

## 3. 実装原則
- CRUD は専用モジュールのみ
- 業務データからは参照（READ）のみ許可
- 論理削除を原則とする（is_active / valid_to 等）

## 4. 変更管理
- マスタ変更は必ず履歴を残す
- audit_trail / history テーブルを使用
- 変更理由（reason）を必須入力とする

## 5. 業務分離ルール
- 販売・出荷・請求は同一マスタを参照する
- 業務別にマスタを複製してはならない

## 6. AI実装への指示
- 新規業務 repo 作成時は必ず共通マスタ参照設計とする
- マスタ定義が無い場合は実装を止め、PMに確認すること

## 7. 禁止事項
- 業務テーブル内にマスタ項目を重複定義すること
- マスタIDではなく名称を業務キーにすること
EOT

echo "✅ installed: $RULE_FILE"
exit 0
