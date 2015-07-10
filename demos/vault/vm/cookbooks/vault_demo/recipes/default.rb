# include_recipe 'build-essential::default'
include_recipe "mongodb::default"
include_recipe "java::default"
include_recipe 'yum-epel'

#package deps
package 'unzip' do
end

package 'jq' do
end

# Configure mongodb
cookbook_file '/tmp/init-mongo.js' do
  source 'init-mongo.js'
end

execute 'mongo vault-demo --port 27017 /tmp/init-mongo.js' do
end

# Install Vault
remote_file '/tmp/vault_0.1.2_linux_amd64.zip' do
  source 'https://dl.bintray.com/mitchellh/vault/vault_0.1.2_linux_amd64.zip'
  action :create
end

execute 'unzip -o -d /usr/bin /tmp/vault_0.1.2_linux_amd64.zip' do
end

file '/usr/bin/vault' do
  mode '555'
  owner 'root'
  group 'root'
end

cookbook_file '/etc/vault.conf' do
  source 'vault.conf'
end

execute 'pkill -KILL vault' do
  ignore_failure true
end

execute 'sleep 5; vault server -config=/etc/vault.conf 2>&1 > /tmp/vault-stdout.log &' do
end

execute 'sleep 5; curl -f -XPUT --data "{\"secret_shares\":1, \"secret_threshold\":1}" http://localhost:8200/v1/sys/init > /tmp/vault-init.json' do
  not_if 'sleep 5; vault status --address http://localhost:8200 2>&1 | grep Sealed'
end

execute 'cat /tmp/vault-init.json | jq ".keys[0]" | xargs vault unseal --address http://localhost:8200' do
end

execute 'export VAULT_TOKEN=`cat /tmp/vault-init.json | jq ".root_token"`' do
end

execute 'vault auth-enable --address http://localhost:8200 app-id' do
  not_if 'vault auth -methods --address http://localhost:8200 2>&1 | grep app-id'
end

#Write the app-id
execute 'vault write --address http://localhost:8200 auth/app-id/map/app-id/vault_demo value=root display_name=vault_demo' do
end

#Pair to a user
execute 'vault write --address http://localhost:8200 auth/app-id/map/user-id/vault_demo value=vault_demo' do
end

# Key 1: 3147b3e4f458811c093c712ec13ec29fdc9ae3bccc55eb3a4444bf85c8a8aeb0
# Initial Root Token: ed19f24a-c200-a4b6-a728-4788f8044fd4

#TODO: install certificate

# Install the service
execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/vault-demo-0.1.0.jar -Dvault.appId' do
  source 'vault-demo-0.1.0.jar'
end

execute 'java -jar /opt/service/vault-demo-0.1.0.jar > /opt/service/vault-demo.log &' do
end
