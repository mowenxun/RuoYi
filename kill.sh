# 先删除半小时轮询检查all.log大小的进程
PID='ps -ef|grep ry.sh|grep -v grep|awk '{print $2}''
if [ x"$PID" != x"" ]; then
	    echo "ry.sh is running...,kill thread"
	    kill -TERM $PID
	    echo "kill thread success"
	else
	   echo "ry.sh is notRunning..."
	fi