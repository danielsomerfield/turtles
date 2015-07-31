#!/bin/sh
if [ "$#" -ne 1 ]; then
  echo "Application name required";
  exit 1;
fi

app_name=${BASH_ARGV[0]}
cookbook_dir="../${app_name}/vm/cookbooks/${app_name}_demo"

if [ ! -d $cookbook_dir ]; then
  echo "No directory at $cookbook_dir";
  exit 1;
fi

./gradlew clean build

jar_name="demo-app-0.1.0.jar"
jar_source_path=build/libs/$jar_name

echo `pwd`

if [ ! -f $jar_source_path ]; then
    echo "No directory at $jar_source_path";
    exit 1;
fi

cp $jar_source_path $cookbook_dir/files/default/$jar_name

cd $cookbook_dir; kitchen converge
