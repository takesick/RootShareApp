# RouteShareApp

## アプリの概要
1. ルートの作成
2. ルートおよび写真の投稿・シェア
3. 他人のルートの保存
以上の3つの機能を目指したアプリです。

## 使用した言語およびサービス
1. Android Java(Kotlinではありません)
2. GCP：
    - FirebaseAuth： ユーザーのログイン管理
    - Firestore： NoSQLベースのデータベースである
    - Firestrage： 画像の保存用にストレージ
3. Realm： SQLiteベースのAndroidの内部データベース

## API
1. GoogleMapAPI：
    - PlaceAPIで地図と位置情報の取得
    - StaticsMapAPIでタイムライン用の静的地図データの取得
2. AlgoliaAPI： データベース上での全文検索機能用(Firestoreには全文検索機能がないため)

## コメント
ご閲覧いただきありがとうございます。<br>
開発中のため、汚いコードや挙動のおかしな点が多々あります。<br>
申し訳ございません、順次修正致します。<br>
<br>
ご意見・ご指摘ございましたら何でもお申し付けください。
