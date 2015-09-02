#http://blog.csdn.net/jadyer/article/details/7960802
#!/bin/sh
JAVA_HOME="/usr/java/jdk1.6.0_32"
APP_MAIN=com.jadyer.server.MainApp

jadyerServerPID=0

getJadyerServerPID(){
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        jadyerServerPID=`echo $javaps | awk '{print $1}'`
    else
        jadyerServerPID=0
    fi
}

getServerStatus(){
    getJadyerServerPID
    echo "================================================================================================================"
    if [ $jadyerServerPID -ne 0 ]; then
        echo "$APP_MAIN is running(PID=$jadyerServerPID)"
        echo "================================================================================================================"
    else
        echo "$APP_MAIN is not running"
        echo "================================================================================================================"
    fi
}

getServerStatus