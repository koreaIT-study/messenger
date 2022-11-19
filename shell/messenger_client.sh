#!/bin/sh

stop_server() {
  PROC=`docker ps -aq  -f 'NAME=messenger-client'`
  if [[ -n $PROC ]]; then
    echo "Process is running."
    docker stop $PROC
    docker rm $PROC
    echo "Process stoped"
  else
    echo "Process is not running."
  fi
}

start_server() {
  echo "container start~~"
  docker run -it --name messenger-client -d -p 8081:8081 -v /home/mshmsh0814/storage/logs:/tmp/logs --network server-net shmin7777/messenger-client
}

# input argument check & execute
while [ $# -gt 0 ]
do
    case "$1" in
        start) start_server;;
        stop) stop_server;;
        restart) stop_server
                 start_server;;
        *) echo "check arguments plz!!!"
    esac
    shift
done
