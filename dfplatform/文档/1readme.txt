部署按照下面的操作
1、修改application.properties文件中的数据库地址
2、先执行sql脚本，2mysql5.7.26.sql
3、在服务器上clone代码
4、安装httpd,httpd安装.txt
5、在dfplatform同级目录下执行3restart.sh 启动服务

下面是demo，如果需要可以按照步骤执行
6、执行4demo.sql,完成demo数据初始化. 注意sql中you server ip 需要改为上面部署的服务地址
7、在dfplatform同级目录下执行5copyjar.sh 初始化动态加载jar包用于demo演示
8、执行6deletedemo.sql可以删除demo中的数据