# Phase A0 – Build Unblock（正本）

状態: 完了 / 固定

## 目的
- javac / manifest / encoding / 参照エラーを全遮断
- assembleDebug を **必ず通す** 状態を作る

## 内容
- Activity / Core / Infra は最小スタブ
- UI / R.id / 外部API / Voice / Notification は未実装
- UTF-8 BOM 完全排除
- 機能実装は禁止（ビルド専用フェーズ）

## ルール
- このフェーズのコードは **変更禁止**
- 以降の作業は Phase A1 以降で行うこと