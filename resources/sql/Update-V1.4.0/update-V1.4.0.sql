-- Ajout de la colonne plui_procedure
ALTER TABLE plui_request ADD COLUMN plui_procedure CHARACTER VARYING(50);


-- View: pluievolution.detailed_plui_request

-- DROP VIEW pluievolution.detailed_plui_request;

-- On récrée la vue avec la colonne plui_procedure
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
