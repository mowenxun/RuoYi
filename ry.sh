#!/bin/bash

AppName=ruoyi-admin.jar

#JVM参数
JVM_OPTS="-Dname=$AppName  -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=1024m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"
APP_HOME=`pwd`
LOG_PATH=$APP_HOME/logs/all.log

if [ "$1" = "" ];
then
    echo -e "\033[0;31m 未输入操作名 \033[0m  \033[0;34m {start|stop|restart|status} \033[0m"
    exit 1
fi

if [ "$AppName" = "" ];
then
    echo -e "\033[0;31m 未输入应用名 \033[0m"
    exit 1
fi

function start()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`

	if [ x"$PID" != x"" ]; then
	    echo "$AppName is running..."
	else
	  BUILD_ID=DONTKILLME
		nohup java -jar  $JVM_OPTS ruoyi-admin/target/$AppName > $LOG_PATH 2>&1 &
		echo "Start $AppName success..."
	fi
}

function stop()
{
    echo "Stop $AppName"
	
	PID=""
	query(){
		PID=`ps -ef |grep java|grep $AppName|grep -v grep|awk '{print $2}'`
	}

	query
	if [ x"$PID" != x"" ]; then
		kill -TERM $PID
		echo "$AppName (pid:$PID) exiting..."
		while [ x"$PID" != x"" ]
		do
			sleep 1
			query
		done
		echo "$AppName exited."
	else
		echo "$AppName already stopped."
	fi
}

function restart()
{
    stop
    sleep 2
    start
}

function status()
{
    PID=`ps -ef |grep java|grep $AppName|grep -v grep|wc -l`
    if [ $PID != 0 ];then
        echo "$AppName is running..."
    else
        echo "$AppName is not running..."
    fi
}

# 控制日志行数，避免程序运行久了log文件过大
limit_count_log(){
    local logfile=$1
    local maxline=$2
    echo "$(/bin/date +%F_%T) 文件路径=$logfile"
    echo "$(/bin/date +%F_%T) 允许的最大行数=$maxline"

    if [ ! -f "$logfile" ]; then
    sudo touch $logfile
    fi
    linecount=`/usr/bin/wc -l $logfile|awk '{print $1}'`;
    echo  "$(/bin/date +%F_%T) 目标文件行数：$linecount"
    if [ ${linecount} -gt ${maxline} ];then
        sudo cp /dev/null $logfile
        echo "$(/bin/date +%F_%T) 行数超过$maxline行清除成功"
    else
        echo "$(/bin/date +%F_%T) 行数未超过$maxline行"
    fi
}

case $1 in
    start)
    start;;
    stop)
    stop;;
    restart)
    restart;;
    status)
    status;;
    *)

esac


# kill -9 $(ps -ef|grep ry.sh|grep -v grep|awk '{print $2}')
nohup
while true
do
    limit_count_log $LOG_PATH 100000
    sleep 1800
    echo "======================="
done > myfile.out 2>&1 &
