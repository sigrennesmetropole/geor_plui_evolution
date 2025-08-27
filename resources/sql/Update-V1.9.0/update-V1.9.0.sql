-- Ajout de deux nouvelles colonnes dans les requêtes PLUI : concertation et approbation
ALTER TABLE pluievolution.plui_request ADD COLUMN IF NOT EXISTS concertation text;
ALTER TABLE pluievolution.plui_request ADD COLUMN IF NOT EXISTS approbation text;

-- On crée une table pour stocker la configuration.
CREATE TABLE IF NOT EXISTS pluievolution.configuration(
    code varchar(30) primary key not null,
    valeur varchar(100)
);

-- On ajoute les deux colonnes dans la vue
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
       pr.plui_procedure,
       pr.approbation,
       pr.concertation
FROM pluievolution.plui_request pr
         LEFT JOIN pluievolution.geographic_area ga ON pr.area_id = ga.id;

ALTER TABLE pluievolution.detailed_plui_request
    OWNER TO pluievolution;