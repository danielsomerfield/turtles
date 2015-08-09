# include_recipe 'build-essential::default'
include_recipe "mongodb::default"
include_recipe "java::default"
include_recipe 'yum-epel'

#package deps
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
  revision 'debian'
  destination GIT_CRYPT_SRC_HOME
end

execute 'make && make install' do
  cwd GIT_CRYPT_SRC_HOME
end

# Install the service
execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/demo-app-0.1.0.jar' do
  source 'demo-app-0.1.0.jar'
end

# execute 'java -Dsecret.service=git-crypt -jar /opt/service/demo-app-0.1.0.jar > /opt/service/demo.log &' do
# end
