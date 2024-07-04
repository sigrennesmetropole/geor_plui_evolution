# Plugin PLUi Evolution

## I - Construction de l'application

Le projet _git_ est construit comme suit :

- `docker` : ce répertoire contient des propositions de fichiers Dockerfile pour la construction/modification des images dockers ainsi  qu'une proposition pour le fichier _docker-compose.yml_
- `georchestra-plui-evolution-api` : il s'agit du sous-projet maven contenant l'application et les controleurs
- `georchestra-plui-evolution-core` : il s'agit du sous-projet maven contenant les entités et les DAO
- `georchestra-plui-evolution-service` : il s'agit du sous-projet maven contenant les services métiers, les services techniques
- `resources` :  les resources avec notamment :
  - `sql` qui contient les fichiers SQL d'initialisation
  - `swagger`qui contient le fichier swagger permettant de générer l'ensemble des services REST du back-office
  
Le back-office est construit à partir de la commande maven
`mvn -DskipTest package`

Le résultat de cette construction est :
* Un fichier WAR `[projet]/georchestra-plui-evolution-api/target/georchestra-plui-evolution-api-1.0-SNAPSHOT.war` déployable directement dans Tomcat ou Jetty
* Un fichier SpringBoot JAR `[projet]/georchestra-plui-evolution-api/target/georchestra-plui-evolution-api-1.0-SNAPSHOT.jar`

## II - Installation

L'addon PLUi Evolution est conçu pour s'installer au sein d'une installation GeOrchestra existante mais la partie "backend" est indépendante de GeOrchestra.

#### II.1 - Base de données

L'installation peut être réalisée soit :
* Dans une base de données dédiée
* Dans un schéma d'une base de données existantes

Dans tous les cas, il faut en premier lieu créer un utilisateur Postgres _pluievolution_.

```sql
CREATE USER pluievolution WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;
```

Si l'installation est réalisée dans une base de données dédiée, il faut créer cette base :

```sql
CREATE DATABASE pluievolution
    WITH 
    OWNER = pluievolution
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
```

Il faut ensuite exécuter le script `[projet]/resources/sql/plui-evolution-initialisation.sql` en tant qu'administrateur postgres.

Ce script réalise les opérations suivantes :
* Création d'un schéma _pluievolution_
* Modification du user _pluievolution_ afin de lui affecter un search_path à _pluievolution,public_
* Création des extensions geospatiales nécessaires dans le schéma _pluievolution_
* Création des tables, index, séquences dans le schéma

#### II.2 - Déploiement de l'application _back-office_

Le back-office peut être démarrer :
* Soit dans un container Tomcat 9.

Il suffit alors de déposer le fichier WAR produit dans le répertoire webapps de Tomcat.

* Soit dans un container Jetty

Il suffit alors de copier le fichier WAR produit dans le répertoire webapps de Jetty puis de lancer Jetty

```sh
cp plui-evolution.war /var/lib/jetty/webapps/plui-evolution.war
java -Djava.io.tmpdir=/tmp/jetty \
      -Dgeorchestra.datadir=/etc/georchestra 	\
      -Xmx${XMX:-1G} -Xms${XMX:-1G}           \
      -jar /usr/local/jetty/start.jar"
```

* Soit en lançant l'application SpringBoot à partir du JAR 

```
java -jar plui-evolution.jar
```

**Remarque**: attention comme indiqué plus haut, l'application utilise la librairie "activity" qui créé ses propres tables au démarrage de l'application. Faut d'une configuration adéquate, ces tables peuvent atterrir dans le mauvais schéma. Il est donc important de dérouler le chapitre **II.1** avant toute chose.

La configuration du back-office de trouve dans le fichier `plui-evolution.properties`. Les principales propriétés sont :

```java
# TEMPORARY DIRECTORY
temporary.directory=${java.io.tmpdir}/plui-evolution

# LOG
logging.level.org.springframework=DEBUG
logging.level.org.georchestra=DEBUG

# SERVER 
server.port=<port applicatif pour l'exécution en springboot>

# BDD
spring.datasource.url=jdbc:postgresql://localhost:5432/pluievolution?ApplicationName=plui-evolution
spring.datasource.username=pluievolution
spring.datasource.password=pluievolution
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect = org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults = false

# UPLOAD
# Taille maximum des fichiers à importer
spring.servlet.multipart.max-file-size=10MB

attachment.max-count=5
attachment.mime-types=image/png,image/jpeg,image/tiff,application/pdf,text/plain,text/html,text/csv,application/vnd.dxf,application/vnd.dwg,\
	application/vnd.oasis.opendocument.text,application/vnd.oasis.opendocument.spreadsheet,application/vnd.oasis.opendocument.presentation,application/vnd.oasis.opendocument.graphics,\
	application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-powerpoint,\
	application/vnd.openxmlformats-officedocument.presentationml.presentation,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document

# EMAIL
mail.transport.protocol=smtp
mail.smtp.host=<host>
mail.smtp.auth=false
mail.smtp.port=<port>
mail.smtp.user=
mail.smtp.password=
mail.smtp.starttls.enable=true
mail.debug=false
mail.from=plui-evolution@rennesmetropole.fr

#LDAP
spring.ldap.urls=ldap://<host georchestra ldap>:<port>
spring.ldap.base=<la racine dn par exemple dc=georchestra,dc=org>
spring.ldap.username=<dn d'un compte ayant de droit de lecture sur le ldap par exemple cn=admin,dc=georchestra,dc=org>
spring.ldap.password=<mot de passe>

ldap.user.searchBase=ou=users
ldap.objectClass=person
ldap.attribute.login=cn
ldap.attribute.firstName=givenname
ldap.attribute.lastName=sn
ldap.attribute.organization=description
ldap.attribute.email=mail

# GENERATION
freemarker.clearCache=false
freemarker.baseDirectory=${java.io.tmpdir}/models
freemarker.basePackage=models
freemarker.cssFile=
freemarker.fontsPath=fonts

```

## III - Configuration

#### III.1 Configuration du certificat

Un script est lancé au déploiement de l'image docker de l'application qui ajoute un certificat donné au keystore.  
Afin d'ajouter le bon certificat au bon keystore, il est nécessaire de remplir les informations adéquates dans le fichier `properties` de l'application :

```yaml
# dossier contenant le certificat
server.trustcert.keystore.path=
# filename du certificat
server.trustcert.keystore.cert=
# nom de l'alias du certificat à insérer dans le keystore
server.trustcert.keystore.alias=
# chemin absolu du keystore dans le container docker
server.trustcert.keystore.store=
# mot de passe du keystore
server.trustcert.keystore.password=
```
Par exemple :
```
server.trustcert.keystore.path=/etc/georchestra/
server.trustcert.keystore.cert=plui-evolution.crt
server.trustcert.keystore.alias=certificat-plui-evolution
server.trustcert.keystore.store=/usr/local/openjdk-11/lib/security/cacerts
server.trustcert.keystore.password=changeit
```

Si les variables ne sont pas remplies, le certificat n'est pas ajouté au keystore et l'application démarre normalement.

