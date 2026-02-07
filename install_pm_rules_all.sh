#!/bin/sh
set -eu

ROOT="$HOME/erp-foundation"
RULE_DIR="$ROOT/pm_ai/rules"

# ---- guard ----
if [ ! -d "$ROOT" ]; then
  echo "❌ erp-foundation not found: $ROOT"
  exit 1
fi

mkdir -p "$RULE_DIR"

create_if_absent() {
  FILE="$1"
  CONTENT="$2"
  if [ -f "$FILE" ]; then
    echo "↪ exists; skip: $(basename "$FILE")"
    return 0
  fi
  printf "%s\n" "$CONTENT" > "$FILE"
  echo "✅ created: $(basename "$FILE")"
}

# ---- 00_common ----
create_if_absent "$RULE_DIR/00_common.md" "
# PM指示書：共通ルール

## 目的
- PM指示は AI/人間の**一次入力**である
- 判断は PM、実装は業務repo

## 基本原則
- 指示は md で残す
- 既存データを壊さない
- working tree が汚れていたら停止

## 禁止
- 自己判断でのマスタ配置
- 仕様未記載の実装開始
"

# ---- 10_master_management ----
create_if_absent "$RULE_DIR/10_master_management.md" "
# PM指示書：マスタ管理（全業務共通）

## 位置づけ
- マスタは全業務の基盤
- 業務トランザクションから直接編集禁止

## 対象
- 商品 / 取引先 / 倉庫 / 勘定科目 / 税区分 / 部門

## 実装原則
- READ専用参照
- 論理削除（is_active 等）
- 履歴必須（reason）

## 禁止
- 業務repoに共通マスタ作成
- 数量/金額/状態を持たせる
"

# ---- 20_sales ----
create_if_absent "$RULE_DIR/20_sales.md" "
# PM指示書：販売（Sales）

## 責務
- 見積/受注/価格条件
- 金額確定はしない

## 参照
- 共通マスタ（商品/顧客/税）

## 連携
- 出荷へはイベント/指示で連携

## 禁止
- 在庫数量の直接更新
"

# ---- 30_shipping ----
create_if_absent "$RULE_DIR/30_shipping.md" "
# PM指示書：出荷（Shipping）

## 責務
- 出庫/配送/数量確定

## 参照
- 商品/倉庫マスタ

## 連携
- 請求へ数量確定イベント

## 禁止
- 価格/金額の計算
"

# ---- 40_billing ----
create_if_absent "$RULE_DIR/40_billing.md" "
# PM指示書：請求（Billing）

## 責務
- 請求書/売掛/締め
- 金額確定

## 参照
- 税/通貨/顧客マスタ

## 連携
- 会計へ仕訳連携

## 禁止
- 数量確定ロジック
"

# ---- 50_purchase ----
create_if_absent "$RULE_DIR/50_purchase.md" "
# PM指示書：購買（Purchase）

## 責務
- 発注/仕入

## 参照
- 取引先/商品マスタ

## 禁止
- 販売価格への影響
"

# ---- 60_inventory ----
create_if_absent "$RULE_DIR/60_inventory.md" "
# PM指示書：在庫（Inventory）

## 責務
- 在庫移動/棚卸

## 参照
- 商品/倉庫マスタ

## 禁止
- 金額計算
"

# ---- 70_accounting ----
create_if_absent "$RULE_DIR/70_accounting.md" "
# PM指示書：会計（Accounting）

## 責務
- 仕訳/元帳/決算

## 参照
- 勘定科目/税マスタ

## 禁止
- 業務イベントの直接生成
"

# ---- 80_hr ----
create_if_absent "$RULE_DIR/80_hr.md" "
# PM指示書：人事（HR）

## 責務
- 勤怠/人員

## 参照
- 部門/社員マスタ

## 禁止
- 会計処理の直接実装
"

# ---- 90_audit ----
create_if_absent "$RULE_DIR/90_audit.md" "
# PM指示書：監査（Audit）

## 責務
- 監査ログ/証跡

## 原則
- 参照専用
- 改変不可

## 禁止
- 業務データの更新
"

echo "🎉 all PM rules installed"
exit 0
