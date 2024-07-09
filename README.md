# Man10BundleCanceler
バンドルのアクションをキャンセルするプラグインです。
設定はconfig.ymlに保存されます。

## Permissions
権限について
mbcanceler.opがあり、OPにデフォルトで付与されています。
権限は各種コマンド実行のみです。

## Commands
コマンド一覧です

### mbcanceler [on/off]
システムを[起動/終了]します

### mbcanceler world
今いるワールドをキャンセル対象に追加します
既に追加されている場合は削除します

### mbcanceler worldmode [on/off]
ワールドごとの制限を[ON/OFF]します

### mbcanceler mode [in/out/all]
キャンセルするアクションを選択できます
inで収納のみ禁止、outで取り出しのみ禁止、allでどちらも禁止します

### mbcanceler state
現在の設定を表示します
