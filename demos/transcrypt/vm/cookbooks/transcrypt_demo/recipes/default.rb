# include_recipe 'build-essential::default'
include_recipe "mongodb::default"
include_recipe "java::default"

#package deps
package 'rng-tools' do
end

package 'git' do
end

cookbook_file '/tmp/init-mongo.js' do
  source 'init-mongo.js'
end

execute 'mongo demo --port 27017 /tmp/init-mongo.js' do
end

#TODO: lock down mongo

# Install transcrypt
package 'openssl' do

end

git 'clone transcrypt' do
  destination '/opt/transcrypt'
  repository 'https://github.com/elasticdog/transcrypt.git'
  revision 'master'
  action :sync
end

link '/usr/bin/transcrypt' do
  to '/opt/transcrypt/transcrypt'
end

# Install the service
execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/demo-app-0.1.0.jar' do
  source 'demo-app-0.1.0.jar'
end

#Note: that you would never really want to provision this way. It should be an out-of-band process.
cookbook_file '/opt/service/demo-app-0.1.0.jar' do
  source 'demo-app-0.1.0.jar'
end

execute 'Extract keys' do
  creates '/root/.gnupg'
  cwd '/root'
  command 'tar xvfz /tmp/kitchen/cookbooks/transcrypt_demo/files/default/demo-keys.tar.gz'
end

execute 'Correct permissions on keys' do
  command 'chmod -R 700 /root/.gnupg'
end

git 'clone encrypted files' do
  destination '/opt/service/conf'
  repository 'https://github.com/danielsomerfield/transcrypt_demo_files.git'
  revision 'master'
  action :checkout
end

#You would obiously never use the password here. The environment variable should be injected by an external process.
execute 'Unlock files' do
  cwd '/opt/service/conf'
  command "transcrypt -c aes-256-cbc -p 'LHKYB+jVRq2O5DANWoklSGvvNxDMcqZ+8ssrqTw4'"
end

execute 'java -Dsecret.service=transcrypt -jar /opt/service/demo-app-0.1.0.jar > /opt/service/demo.log &' do
end

execute 'curl -f "http://localhost:8080/health"' do
  retries 30
  retry_delay 2
end

directory 'delete the conf dir' do
  path '/opt/service/conf'
  recursive true
  action :delete
end
