# RouteShareApp

**・アプリの概要**<br>
1.ルートの作成
2.ルートおよび写真の投稿・シェア
3.他人のルートの保存
以上の3つの機能を目指したアプリです。

**・使用技術**<br>
1.Android Java(Kotlinではありません)
2.GCP：FirebaseAuthでユーザーのログイン管理、NoSQLベースのデータベースであるFirestore、画像の保存用にストレージFirestrage
3.Realm：SQLiteベースのAndroidの内部データベース

**・API**<br>
1.GoogleMapAPI：PlaceAPIで地図と位置情報の取得、StaticsMapAPIでタイムライン用の静的地図データの取得
2.AlgoliaAPI：データベース上での全文検索機能用(Firestoreには全文検索機能がないため)
