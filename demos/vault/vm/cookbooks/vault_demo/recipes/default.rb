# include_recipe 'build-essential::default'
include_recipe "mongodb::default"
include_recipe "java::default"
include_recipe 'yum-epel'

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

package 'unzip' do
end

package 'jq' do
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

execute 'pkill vault' do
  ignore_failure true
end

execute 'vault server -config=/etc/vault.conf &' do
end

execute 'curl -f -v -XPUT --data "{\"secret_shares\":1, \"secret_threshold\":1}" http://localhost:8200/v1/sys/init > /tmp/vault-init.json' do
  not_if 'curl -v http://localhost:8200/v1/sys/init | jq ".initialized=true"'
end

# Install the service
execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/vault-demo-0.1.0.jar' do
  source 'vault-demo-0.1.0.jar'
end

execute 'java -jar /opt/service/vault-demo-0.1.0.jar > /opt/service/vault-demo.log &' do
end
