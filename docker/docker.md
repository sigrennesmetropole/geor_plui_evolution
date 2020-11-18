# Pour les utilisateurs dockers :

Il est possible de générer une image docker en utilisant la commande suivante :

```
$ mvn clean package -DskipTests -Pdocker package
```

Si vous utilisez l'image docker vaut devez prendre en compte 2 éléments :

# Servlet container

L'image docker est basée sur Jetty pas sur Tomcat.

# Configuration

Il faut configurer l'application correctement. Voici un extrait du fichier de configuration a modifier en fonction de l'installation :

```
## Version de l'application
application.version=V0.0.1

# LOG
logging.level.org.springframework=INFO
logging.level.org.georchestra=INFO

# SERVER 
server.port=8082

# BDD
spring.datasource.url=jdbc:postgresql://database:35432/georchestra?ApplicationName=plui-evolution
spring.datasource.username=plui-evolution
spring.datasource.password=plui-evolution

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=false

spring.security.user.name=admin
spring.security.user.password={noop}<le mot de passe>
spring.security.user.roles=USER


# EMAIL
mail.transport.protocol=smtp
mail.smtp.host=<le serveur de courriel>
mail.smtp.auth=false
mail.smtp.port=<le port du serveur de courriel:25?>
mail.smtp.user=
mail.smtp.password=
mail.smtp.starttls.enable=true
mail.debug=false
mail.from=plui-evolution@rennesmetropole.fr

#LDAP
spring.ldap.urls=ldap://ldap:30389
spring.ldap.base=dc=georchestra,dc=org
spring.ldap.username=cn=admin,dc=georchestra,dc=org
spring.ldap.password=<le mot de passe>

ldap.user.searchBase=ou=users
ldap.objectClass=person
ldap.attribute.login=cn
ldap.attribute.firstName=givenname
ldap.attribute.lastName=sn
ldap.attribute.organization=description
ldap.attribute.email=mail


```