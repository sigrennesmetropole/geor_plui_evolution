CREATE SCHEMA IF NOT EXISTS pluievolution AUTHORIZATION pluievolution;

-- SET search_path TO pluievolution;
ALTER ROLE pluievolution SET search_path TO pluievolution,public;

-- Ajout des extensions dans le schéma
CREATE EXTENSION IF NOT EXISTS postgis SCHEMA pluievolution;
CREATE EXTENSION IF NOT EXISTS postgis_topology SCHEMA pluievolution;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch SCHEMA pluievolution;
CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder SCHEMA pluievolution;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pluievolution;


-- DROP SEQUENCE pluievolution.user__id_seq;
CREATE SEQUENCE pluievolution.user__id_seq;
ALTER SEQUENCE pluievolution.user__id_seq OWNER TO pluievolution;
    
-- DROP TABLE pluievolution.user_;
CREATE TABLE pluievolution.user_
(
    id bigint NOT NULL DEFAULT nextval('pluievolution.user__id_seq'::regclass),
    login character varying(100)  NOT NULL,
    email character varying(150)  NOT NULL,
    first_name character varying(150) ,
    last_name character varying(150) ,
    CONSTRAINT user__pkey PRIMARY KEY (id)
)
WITH (OIDS = FALSE);
ALTER TABLE pluievolution.user_ OWNER to pluievolution;
    