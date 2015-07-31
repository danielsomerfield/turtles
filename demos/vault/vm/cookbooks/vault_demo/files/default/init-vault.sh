#!/bin/sh

export VAULT_TOKEN=`cat /tmp/vault-init.json | jq ".root_token"`

vault auth -methods --address http://localhost:8200 2>&1 | grep app-id

if [ $? != 0 ]; then
  vault auth-enable --address http://localhost:8200 app-id || exit 1;
fi

vault write --address http://localhost:8200 auth/app-id/map/app-id/demo value=root display_name=demo || exit 1;
vault write --address http://localhost:8200 auth/app-id/map/user-id/demo value=demo || exit 1;
vault write --address http://localhost:8200 secret/demo/mongo password=demo || exit 1;
