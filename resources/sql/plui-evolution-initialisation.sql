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

-- Table: pluievolution.user_

-- DROP TABLE pluievolution.user_;

CREATE TABLE pluievolution.user_
(
    id bigint NOT NULL DEFAULT nextval('user__id_seq'::regclass),
    email character varying(150) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(150) COLLATE pg_catalog."default",
    last_name character varying(150) COLLATE pg_catalog."default",
    login character varying(100) COLLATE pg_catalog."default" NOT NULL,
    organization character varying(150) COLLATE pg_catalog."default",
    roles character varying(1024) COLLATE pg_catalog."default",
    CONSTRAINT user__pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.user_
    OWNER to pluievolution;

-- SEQUENCE: pluievolution.geographic_area_id_seq

-- DROP SEQUENCE pluievolution.geographic_area_id_seq;

CREATE SEQUENCE pluievolution.geographic_area_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE pluievolution.geographic_area_id_seq
    OWNER TO pluievolution;


-- Table: pluievolution.geographic_area

-- DROP TABLE pluievolution.geographic_area;

CREATE TABLE pluievolution.geographic_area
(
    id bigint NOT NULL DEFAULT nextval('geographic_area_id_seq'::regclass),
    codeinsee character varying(10) COLLATE pg_catalog."default",
    geometry geometry,
    nom character varying(255) COLLATE pg_catalog."default",
    identifiant_redmine character varying(63) COLLATE pg_catalog."default",
    CONSTRAINT geographic_area_pkey PRIMARY KEY (id),
    CONSTRAINT uk_geographic_area_codeinsee UNIQUE (codeinsee),
    CONSTRAINT uk_geographic_area_nom UNIQUE (nom)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.geographic_area
    OWNER to pluievolution;


-- SEQUENCE: pluievolution.geographic_etablissement_id_seq

-- DROP SEQUENCE pluievolution.geographic_etablissement_id_seq;

CREATE SEQUENCE pluievolution.geographic_etablissement_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE pluievolution.geographic_etablissement_id_seq
    OWNER TO pluievolution;

-- Table: pluievolution.geographic_etablissement

-- DROP TABLE pluievolution.geographic_etablissement;

CREATE TABLE pluievolution.geographic_etablissement
(
    id bigint NOT NULL DEFAULT nextval('geographic_etablissement_id_seq'::regclass),
    codeinsee character varying(10) COLLATE pg_catalog."default",
    geometry geometry,
    nom character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT geographic_etablissement_pkey PRIMARY KEY (id),
    CONSTRAINT uk_geographic_etablissement_codeinsee UNIQUE (codeinsee)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.geographic_etablissement
    OWNER to pluievolution;



-- SEQUENCE: pluievolution.plui_request_id_seq

-- DROP SEQUENCE pluievolution.plui_request_id_seq;

CREATE SEQUENCE pluievolution.plui_request_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE pluievolution.plui_request_id_seq
    OWNER TO pluievolution;


-- Table: pluievolution.plui_request

-- DROP TABLE pluievolution.plui_request;

CREATE TABLE pluievolution.plui_request
(
    id bigint NOT NULL DEFAULT nextval('plui_request_id_seq'::regclass),
    comment character varying(1024) COLLATE pg_catalog."default",
    creation_date timestamp without time zone NOT NULL,
    geometry geometry,
    initiator character varying(150) COLLATE pg_catalog."default",
    object character varying(300) COLLATE pg_catalog."default" NOT NULL,
    redmine_id integer,
    status character varying(50) COLLATE pg_catalog."default",
    subject character varying(130) COLLATE pg_catalog."default" NOT NULL,
    plui_procedure character varying(50) COLLATE pg_catalog."default",
    type character varying(20) COLLATE pg_catalog."default",
    uuid uuid NOT NULL,
    area_id bigint,
    CONSTRAINT plui_request_pkey PRIMARY KEY (id),
    CONSTRAINT fk_plui_request_area_id FOREIGN KEY (area_id)
        REFERENCES pluievolution.geographic_area (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.plui_request
    OWNER to pluievolution;

-- Mise à jour du champ subject de plui_request à 130 caractères
-- Suppression de la vue, modification de la colonne et re-creation de la vue
DROP VIEW IF EXISTS pluievolution.detailed_plui_request;
ALTER TABLE pluievolution.plui_request ALTER COLUMN subject TYPE varchar(130);



-- View: pluievolution.detailed_plui_request

-- DROP VIEW pluievolution.detailed_plui_request;

CREATE OR REPLACE VIEW pluievolution.detailed_plui_request
AS
SELECT pr.id,
       pr.comment,
       pr.creation_date,
       pr.geometry,
       pr.initiator,
       pr.object,
       pr.status,
       pr.subject,
       pr.type,
       pr.uuid,
       ga.codeinsee,
       ga.nom AS nom_area,
       pr.redmine_id,
       pr.plui_procedure
FROM plui_request pr
         LEFT JOIN geographic_area ga ON pr.area_id = ga.id;

ALTER TABLE pluievolution.detailed_plui_request
    OWNER TO pluievolution;



