# include_recipe 'build-essential::default'
include_recipe "mongodb::default"
include_recipe "java::default"
include_recipe 'yum-epel'

#package deps
package 'rng-tools' do
end

package 'git' do
end

# package 'ruby-devel' do
# end

package 'gcc' do
end

package 'gcc-c++' do
end

cookbook_file '/tmp/init-mongo.js' do
  source 'init-mongo.js'
end

execute 'mongo demo --port 27017 /tmp/init-mongo.js' do
end

#TODO: lock down mongo

# Install git-crypt
package 'openssl-devel' do
end

GIT_CRYPT_SRC_HOME='/home/vagrant/git-crypt_src'

git 'clone git-crypt' do
  repository 'https://github.com/AGWA/git-crypt.git'
  revision 'master'
  destination GIT_CRYPT_SRC_HOME
end

execute 'make && make install' do
  cwd GIT_CRYPT_SRC_HOME
  creates '/usr/local/bin/git-crypt'
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
  command 'tar xvfz /tmp/kitchen/cookbooks/git-crypt_demo/files/default/demo-keys.tar.gz'
end

execute 'Correct permissions on keys' do
  command 'chmod -R 700 /root/.gnupg'
end

git '/opt/service/conf' do
  repository 'https://github.com/danielsomerfield/git-crypt_demo_files.git'
  revision 'master'
  action :checkout
end

execute 'Unlock files' do
  cwd '/opt/service/conf'
  command '/usr/local/bin/git-crypt unlock'
end

execute 'java -Dsecret.service=git-crypt -jar /opt/service/demo-app-0.1.0.jar > /opt/service/demo.log &' do
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
