#!/bin/sh

./gradlew clean build
cd ../vm/cookbooks/blackbox_demo; kitchen converge
