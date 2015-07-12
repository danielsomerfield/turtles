#!/bin/sh

export VAULT_TOKEN=`cat /tmp/vault-init.json | jq ".root_token"`

vault auth -methods --address http://localhost:8200 2>&1 | grep app-id

if [ $? != 0 ]; then
  vault auth-enable --address http://localhost:8200 app-id || exit 1;
fi

vault write --address http://localhost:8200 auth/app-id/map/app-id/vault_demo value=root display_name=vault_demo || exit 1;
vault write --address http://localhost:8200 auth/app-id/map/user-id/vault_demo value=vault_demo || exit 1;
vault write --address http://localhost:8200 secret/vault-demo/mongo password=vault-demo || exit 1;
