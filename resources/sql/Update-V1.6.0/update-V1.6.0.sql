-- Msie à jour des coordonnées d'établissements

-- Mairie d'Orgères
UPDATE geographic_etablissement SET geometry = ST_GeomFromText('POINT(1351898 7210058)', '3948') WHERE codeinsee = '35208';
-- Mairie du Rheu
UPDATE geographic_etablissement SET geometry = ST_GeomFromText('POINT(1343220 7222302)', '3948') WHERE codeinsee = '35240';
-- Mairie de l'Hermittage
UPDATE geographic_etablissement SET geometry = ST_GeomFromText('POINT(1341619 7225381)', '3948') WHERE codeinsee = '35131';
-- Hôtel Rennes Métropole
UPDATE geographic_etablissement SET geometry = ST_GeomFromText('POINT(1351986 7220927)', '3948') WHERE codeinsee = '243500139';

