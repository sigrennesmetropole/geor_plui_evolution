FROM jetty:9.4.31-jdk11

ADD georchestra-plui-evolution-1.0-SNAPSHOT.war /var/lib/jetty/webapps/plui-evolution.war
#ADD etc/ /etc

CMD ["sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty \
  ${ADD_JAVA_OPTS} \
  -Dgeorchestra.datadir=/etc/georchestra \
  -Duser.language=fr -Duser.country=FR \
  -Xmx${XMX:-1G} -Xms${XMX:-1G} \
  -jar /usr/local/jetty/start.jar"]      