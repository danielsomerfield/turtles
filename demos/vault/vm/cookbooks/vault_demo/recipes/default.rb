include_recipe "mongodb::default"
include_recipe "java::default"

remote_file '/tmp/vault_0.1.2_linux_amd64.zip' do
  source 'https://dl.bintray.com/mitchellh/vault/vault_0.1.2_linux_amd64.zip'
  action :create
end

package 'unzip' do
end

execute 'unzip -o -d /usr/bin /tmp/vault_0.1.2_linux_amd64.zip' do
end

file '/usr/bin/vault' do
  mode '555'
  owner 'root'
  group 'root'
end

execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/vault-demo-0.1.0.jar' do
  source 'vault-demo-0.1.0.jar'
end

execute 'java -jar /opt/service/vault-demo-0.1.0.jar > /opt/service/vault-demo.log &' do
end
