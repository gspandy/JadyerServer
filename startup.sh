#http://blog.csdn.net/jadyer/article/details/7960802
#!/bin/sh
JAVA_HOME="/usr/java/jdk1.6.0_32"
JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms2048m -Xmx2048m"
APP_LOG=/app/code/JadyerLog
APP_HOME=/app/code/JadyerServer
APP_MAIN=com.jadyer.server.MainApp
CLASSPATH=$APP_HOME/bin
for jadyerServerJar in "$APP_HOME"/lib/*.jar;
do
   CLASSPATH="$CLASSPATH":"$jadyerServerJar"
done

jadyerServerPID=0

getJadyerServerPID(){
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        jadyerServerPID=`echo $javaps | awk '{print $1}'`
    else
        jadyerServerPID=0
    fi
}

startup(){
    getJadyerServerPID
    echo "================================================================================================================"
    if [ $jadyerServerPID -ne 0 ]; then
        echo "$APP_MAIN already started(PID=$jadyerServerPID)"
	    echo "================================================================================================================"
    else
        echo -n "Starting $APP_MAIN"
        nohup $JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAIN > $APP_LOG/nohup.log &
        getJadyerServerPID
        if [ $jadyerServerPID -ne 0 ]; then
            echo "(PID=$jadyerServerPID)...[Success]"
	        echo "================================================================================================================"
        else
            echo "[Failed]"
	        echo "================================================================================================================"
        fi
    fi
}

startup