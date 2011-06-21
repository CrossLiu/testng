# This script will update serviceloader.jar with the latest version of TmpSuiteListener.class,
# which is used by test.serviceloader.ServiceLoaderTest.
# Run this script after building TestNG and its tests with ant

j=${PWD}
rm -rf /tmp/sl
mkdir /tmp/sl
cd /tmp/sl
jar xvf ${j}/serviceloader.jar
cp ${j}/../../../target/test-classes/test/tmp/TmpSuiteListener.class test/tmp
jar cvf ${j}/serviceloader.jar .
