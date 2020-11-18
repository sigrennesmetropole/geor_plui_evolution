FROM jetty:9.3-jre8

ADD georchestra-plui-evolution-1.0-SNAPSHOT.war /var/lib/jetty/webapps/plui-evolution.war
ADD etc/ /etc

CMD [ "sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty \
      ${ADD_JAVA_OPTS}                                   \
      -Xmx${XMX:-1G} -Xms${XMX:-1G}                      \
      -jar /usr/local/jetty/start.jar" ]