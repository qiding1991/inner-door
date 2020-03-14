 mvn clean package --settings=/Users/qiding/Devtools/apache-maven-3.5.4/conf/settings-home.xml -Dmaven.test.skip=true
 scp target/qiding-inner-map.jar root@39.106.77.97:/home/java/
 ssh root@39.106.77.97