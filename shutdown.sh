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

shutdown(){
    getJadyerServerPID
    echo "================================================================================================================"
    if [ $jadyerServerPID -ne 0 ]; then
        echo -n "Stopping $APP_MAIN(PID=$jadyerServerPID)..."
        kill -9 $jadyerServerPID
        if [ $? -eq 0 ]; then
            echo "[Success]"
	        echo "================================================================================================================"
        else
            echo "[Failed]"
	        echo "================================================================================================================"
        fi
        getJadyerServerPID
        if [ $jadyerServerPID -ne 0 ]; then
            shutdown
        fi
    else
        echo "$APP_MAIN is not running"
	    echo "================================================================================================================"
    fi
}

shutdown