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

include_recipe "mongodb::default"
