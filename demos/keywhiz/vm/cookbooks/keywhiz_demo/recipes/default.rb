include_recipe "mongodb::default"
include_recipe "java::default"
include_recipe 'yum-epel'

execute 'pkill java || true' do
end

#package deps
package 'git' do
end

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

# Install keywhiz
git 'clone keywhiz' do
  repository 'https://github.com/square/keywhiz.git'
  revision 'master'
  destination '/tmp/keywhiz_src'
end

remote_file '/tmp/apache-maven-3.3.3-bin.tar.gz' do
  source 'http://apache.go-parts.com/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz'
end

execute 'tar xvfz /tmp/apache-maven-3.3.3-bin.tar.gz' do
  cwd '/tmp'
  creates '/tmp/apache-maven-3.3.3/'
end

cookbook_file '/usr/lib/jvm/jdk1.8.0_31/jre/lib/security/local_policy.jar' do
  source 'jce/local_policy.jar'
end

cookbook_file '/usr/lib/jvm/jdk1.8.0_31/jre/lib/security/US_export_policy.jar' do
  source 'jce/US_export_policy.jar'
end

mvn_path="/tmp/apache-maven-3.3.3/bin/mvn"

execute "#{mvn_path} -DskipTests package -am -pl server -P h2" do
    cwd '/tmp/keywhiz_src'
end

execute 'java -jar server/target/keywhiz-server-*-SNAPSHOT-shaded.jar migrate server/src/main/resources/keywhiz-development.yaml' do
    cwd '/tmp/keywhiz_src'
end

#TODO: Bootstrapping in dev mode. This should be replaced with initializing key material and bootstrap admin.
execute 'java -jar server/target/keywhiz-server-*-SNAPSHOT-shaded.jar db-seed server/src/main/resources/keywhiz-development.yaml' do
    cwd '/tmp/keywhiz_src'
end

execute "#{mvn_path} package -am -pl cli" do
    cwd '/tmp/keywhiz_src'
end


execute 'Start the keywhiz-server' do
  cwd '/tmp/keywhiz_src'
  command 'java -jar server/target/keywhiz-server-*-SNAPSHOT-shaded.jar server server/src/main/resources/keywhiz-development.yaml > /opt/service/keywhiz.log &'
end

# Set up the keywhiz


# Install the service
directory '/opt/service' do
end

cookbook_file '/opt/service/demo-app-0.1.0.jar' do
  source 'demo-app-0.1.0.jar'
end

execute 'java -Dsecret.service=keywhiz -jar /opt/service/demo-app-0.1.0.jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 > /opt/service/demo.log &' do
end
