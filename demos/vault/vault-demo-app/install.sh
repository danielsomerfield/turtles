#!/bin/sh

./gradlew clean build
cd ../vm/cookbooks/vault_demo; kitchen converge