# include_recipe 'build-essential::default'
include_recipe "mongodb::default"
include_recipe "java::default"
include_recipe 'yum-epel'

#package deps
package 'rng-tools' do
end

package 'git' do
end

package 'ruby-devel' do
end

package 'gcc' do
end

gem_package 'fpm' do
end

package 'rpm-build' do
end

git 'clone blackbox' do
  repository 'https://github.com/StackExchange/blackbox.git'
  revision 'stable'
  destination '/home/vagrant/blackbox_src'
end

execute 'build rpm' do
  creates '/root/rpmbuild-stack_blackbox/stack_blackbox-1.0-1.noarch.rpm'
  command 'make packages-rpm'
  cwd '/home/vagrant/blackbox_src'
end

package 'stack_blackbox' do
  source '/root/rpmbuild-stack_blackbox/stack_blackbox-1.0-1.noarch.rpm'
end

cookbook_file '/tmp/init-mongo.js' do
  source 'init-mongo.js'
end

execute 'mongo demo --port 27017 /tmp/init-mongo.js' do
end

#TODO: lock down mongo

# Install the service
execute 'pkill java || true' do
end

directory '/opt/service' do
end

cookbook_file '/opt/service/demo-app-0.1.0.jar' do
  source 'demo-app-0.1.0.jar'
end

#Note: that you would never really want to provision this way. It should be an out-of-band process.
execute 'Extract keys' do
  creates '/root/.gnupg'
  cwd '/root'
  command 'tar xvfz /tmp/kitchen/cookbooks/blackbox_demo/files/default/demo-keys.tar.gz'
end

execute 'Correct permissions on keys' do
  command 'chmod -R 700 /root/.gnupg'
end

git '/opt/service/conf' do
  repository 'https://github.com/danielsomerfield/blackbox_demo_files.git'
  revision 'master'
  action :export
end

#TODO: delete keys

execute 'Decrypt' do
  cwd '/opt/service/conf'
  command '/usr/blackbox/bin/blackbox_postdeploy'
end

execute 'java -Dsecret.service=blackbox -jar /opt/service/demo-app-0.1.0.jar > /opt/service/demo.log &' do
end

execute 'curl -f "http://localhost:8080/health"' do
  retries 10
  retry_delay 2
end

directory 'delete the conf dir' do
  path '/opt/service/conf'
  recursive true
  action :delete
end
