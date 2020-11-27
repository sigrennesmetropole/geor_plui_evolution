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

-- Table: pluievolution.geographic_area

-- DROP TABLE pluievolution.geographic_area;

CREATE TABLE pluievolution.geographic_area
(
    id bigint NOT NULL,
    geometry geometry,
    nom character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT geographic_area_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.geographic_area
    OWNER to pluievolution;

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
    redmine_id character varying(255) COLLATE pg_catalog."default",
    subject character varying(30) COLLATE pg_catalog."default" NOT NULL,
    uuid uuid NOT NULL,
    status_id bigint,
    type_id bigint,
    CONSTRAINT plui_request_pkey PRIMARY KEY (id),
    CONSTRAINT fkmkl6jtq3ukb1bmnly4kr8pjne FOREIGN KEY (status_id)
        REFERENCES pluievolution.request_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkt0a4k5nlvslcgrs29x4jir3kb FOREIGN KEY (type_id)
        REFERENCES pluievolution.request_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.plui_request
    OWNER to pluievolution;

-- Table: pluievolution.request_status

-- DROP TABLE pluievolution.request_status;

CREATE TABLE pluievolution.request_status
(
    id bigint NOT NULL DEFAULT nextval('request_status_id_seq'::regclass),
    value character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT request_status_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.request_status
    OWNER to pluievolution;

-- Table: pluievolution.request_type

-- DROP TABLE pluievolution.request_type;

CREATE TABLE pluievolution.request_type
(
    id bigint NOT NULL DEFAULT nextval('request_type_id_seq'::regclass),
    value character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT request_type_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.request_type
    OWNER to pluievolution;

-- Table: pluievolution.request_type

-- DROP TABLE pluievolution.request_type;

CREATE TABLE pluievolution.request_type
(
    id bigint NOT NULL DEFAULT nextval('request_type_id_seq'::regclass),
    value character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT request_type_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE pluievolution.request_type
    OWNER to pluievolution;
