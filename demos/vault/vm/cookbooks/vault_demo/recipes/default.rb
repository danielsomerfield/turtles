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

execute 'mongo demo --port 27017 /tmp/init-mongo.js' do
end

#TODO: lock down mongo

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
  not_if 'ls /tmp/vault-init.json'
end

execute 'pkill -KILL vault' do
  ignore_failure true
end

execute 'sleep 5; vault server -config=/etc/vault.conf 2>&1 > /tmp/vault-stdout.log &' do
end

execute 'cat /tmp/vault-init.json | jq ".keys[0]" | xargs vault unseal --address http://localhost:8200' do
end

cookbook_file '/home/vagrant/init-vault.sh' do
  source 'init-vault.sh'
  mode 700
end

execute '/home/vagrant/init-vault.sh' do
end

# Install the service
execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/demo-app-0.1.0.jar' do
  source 'demo-app-0.1.0.jar'
end

execute 'java -jar -Dsecret.service=vault /opt/service/demo-app-0.1.0.jar > /opt/service/demo.log &' do
end
