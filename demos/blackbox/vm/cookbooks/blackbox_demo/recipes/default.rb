# include_recipe 'build-essential::default'
#include_recipe "mongodb::default"
#include_recipe "java::default"
#include_recipe 'yum-epel'

#package deps
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
  creates '/home/vagrant/rpmbuild-stack_blackbox/stack_blackbox-1.0-1.x86_64.rpm'
  command 'make packages-rpm'
  cwd '/home/vagrant/blackbox_src'
end

package 'stack_blackbox' do
  source '/home/vagrant/rpmbuild-stack_blackbox/stack_blackbox-1.0-1.x86_64.rpm'
end

# execute 'mongo blackbox-demo --port 27017 /tmp/init-mongo.js' do
# end

# Install blackbox


# Install the service
# execute 'pkill java || true' do
# end
#
# directory '/opt/service' do
# end
#
# cookbook_file '/opt/service/blackbox-demo-0.1.0.jar' do
#   source 'blackbox-demo-0.1.0.jar'
# end
#
# execute 'java -jar /opt/service/blackbox-demo-0.1.0.jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 > /opt/service/blackbox-demo.log &' do
# end
