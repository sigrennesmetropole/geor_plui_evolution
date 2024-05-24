FROM jetty:9.4.31-jdk11
MAINTAINER fni18300@open-groupe.com
ENV TZ=Europe/Paris

RUN mkdir -p /opt/pluievolution/data/models

ADD /var/lib/jetty/webapps/plui-evolution.war /var/lib/jetty/webapps/pluievolution.war
#ADD etc/ /etc

ENV PROPERTIES_PATH /etc/georchestra/plui-evolution/plui-evolution.properties

ADD entrypoint.sh /opt/pluievolution/entrypoint.sh
ENTRYPOINT [ "/bin/bash", "/opt/pluievolution/entrypoint.sh" ]

CMD ["sh", "-c", "exec java -Djava.io.tmpdir=/tmp/jetty \
  ${ADD_JAVA_OPTS} \
  -Dgeorchestra.datadir=/etc/georchestra \
  -Duser.language=fr -Duser.country=FR \
  -Xmx${XMX:-1G} -Xms${XMX:-1G} \
  -jar /usr/local/jetty/start.jar"]




  