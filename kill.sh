# 先删除半小时轮询检查all.log大小的进程
kill -9 $(ps -ef|grep ry.sh|grep -v grep|awk '{print $2}')