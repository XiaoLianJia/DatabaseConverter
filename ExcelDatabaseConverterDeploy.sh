#!/bin/sh

echo "执行[ExcelDatabaseConverter]启动脚本。"

APP="/home/ExcelDatabaseConverter.jar";

# shellcheck disable=SC2009
PID=$(ps -ef | grep ${APP} | grep -v grep | awk '{print $2}')

echo "${PID}"

if [ -z "${PID}" ]
then
  echo "${APP} 没有在运行，直接执行启动指令。"
else
  echo "${APP} 正在运行。"
  kill -9 "${PID}"
  echo "${APP} 停止运行。"
fi

sleep 2

if test -e ${APP}
then
  nohup java -jar ${APP} > /dev/null 2>&1 &
  echo "${APP} 启动..."
else
  echo "${APP} 文件不存在，请检查。"
fi