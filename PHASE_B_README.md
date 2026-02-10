# Phase B – Secretary Core（正本）

## 目的
- 秘書という概念を Core 層で成立させる
- UI / Prefs / Notification から独立したロジックを持つ

## 実装内容
- Secretary（ID = String）
- SecretaryCatalog（一覧・取得）
- SecretarySpeech（文言生成）

## ルール
- enum 禁止
- Android API 参照禁止
- 副作用禁止（pure logic）