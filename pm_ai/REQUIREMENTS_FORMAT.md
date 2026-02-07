# PM-UseCase-AI 要件入力フォーマット（requirements.md）

## 目的
自然言語の要件から、業務行為（UseCase）を抽出し、domain別に整理して
- spec/usecases.schema.yaml（正本）
- reports/usecase_map.yaml（対応表）
- reports/team_instructions.md（チーム指示）
を自動生成する。

## 書き方（最小）
- 1行1要件（箇条書き推奨）
- 「〜できる」「〜する」「〜したい」などの“行為”が含まれる文章にする
- 画面名ではなく業務行為を書く（ログイン画面、注文画面…ではなく「ログインする」「受注を登録する」）

## 任意：domainヒント
文頭に [domain:xxx] を付けると、そのdomainに寄せて分類する
例:
- [domain:auth] ログインできるようにする
- [domain:order] 受注を登録できる
- [domain:billing] 請求書を作成できる

## 禁止（書かない）
- company_id / user_id / DBテーブル名 / SQL / API URL / Supabase / SQLite
