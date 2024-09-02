FROM jetty:9.4.55-jdk17-eclipse-temurin
USER root
ENV TZ=Europe/Paris

RUN apt-get update && apt-get upgrade -y

ADD plui-evolution.war /var/lib/jetty/webapps/plui-evolution.war

ENV PROPERTIES_PATH /etc/georchestra/plui-evolution/plui-evolution.properties

ADD entrypoint.sh /opt/pluievolution/entrypoint.sh
ENTRYPOINT [ "/bin/bash", "/opt/pluievolution/entrypoint.sh" ]

CMD ["sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty \
  ${ADD_JAVA_OPTS} \
  -Dgeorchestra.datadir=/etc/georchestra \
  -Duser.language=fr -Duser.country=FR \
  -Xmx${XMX:-1G} -Xms${XMX:-1G} \
  -jar /usr/local/jetty/start.jar"]




  