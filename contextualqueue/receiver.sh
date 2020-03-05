CLASSPATH=$HOME/src/main/resources
CLASSPATH=$CLASSPATH:$HOME/bin
CLASSPATH=$CLASSPATH:$HOME/lib/activemq-client-5.15.11.jar
CLASSPATH=$CLASSPATH:$HOME/lib/jms-1.1.jar
CLASSPATH=$CLASSPATH:$HOME/lib/geronimo-j2ee-management_1.1_spec-1.0.1.jar
CLASSPATH=$CLASSPATH:$HOME/lib/slf4j-log4j12-1.7.21.jar
CLASSPATH=$CLASSPATH:$HOME/lib/slf4j-api-1.7.21.jar
CLASSPATH=$CLASSPATH:$HOME/lib/log4j-1.2.17.jar
CLASSPATH=$CLASSPATH:$HOME/lib/hawtbuf-1.9.jar

java -cp $CLASSPATH eventpatterns.contextualqueue.Receiver 
