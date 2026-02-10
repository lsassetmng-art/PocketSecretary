# Phase A1 – Prefs 正式化（正本）

## 目的
- 設定値の単一真実源を確立
- UI / Core が直接 SharedPreferences を触らない構造にする

## 実装内容
- PrefsStore IF 確定
- AndroidPrefsStore SharedPreferences 実装
- Onboarding / Secretary / Voice / Notification を永続化

## ルール
- UI はまだ復活させない
- Prefs 参照は必ず PrefsStore 経由