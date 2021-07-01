PID=`ps -ef|grep dfplatform|grep -v grep|awk '{print $2}'`
echo $PID|xargs -r kill -9 
cd dfplatform
git pull
/root/apache-maven-3.5.0/bin/mvn  install     ##服务器maven地址 需要修改
cd target
nohup java -jar -Dserver.port=8180 dfplatform-1.0-SNAPSHOT.jar &    ##端口号可以修改，如果修改需要前端同步修改
