INSERT INTO pluievolution.user_ ( login, first_name, last_name, email) VALUES ( 't.brule', 'Thomas', 'Brulé', 't.brule@rennesmetropole.fr');

-- Insertion des enumération de Status
INSERT INTO pluievolution.status (id, value) VALUES (1, 'STATUT_NOUVEAU');
INSERT INTO pluievolution.status (id, value) VALUES (2, 'STATUT_ANALYSE_EN_COURS');
INSERT INTO pluievolution.status (id, value) VALUES (3, 'STATUT_EN_ATTENTE_VALIDATION_COMMUNE');
INSERT INTO pluievolution.status (id, value) VALUES (4, 'STATUT_VALIDE_COMMUNE');
INSERT INTO pluievolution.status (id, value) VALUES (5, 'STATUT_DEMANDE_NON_RECEVABLE');
INSERT INTO pluievolution.status (id, value) VALUES (6, 'STATUT_DEMANDE_REFORMULEE');

-- Insertion des énumérations de PluiRequest
INSERT INTO pluievolution.request_type (id, value) VALUES (1, 'TYPE_COMMUNE');
INSERT INTO pluievolution.request_type (id, value) VALUES (2, 'TYPE_INTERCOMMUNE');
INSERT INTO pluievolution.request_type (id, value) VALUES (3, 'TYPE_METROPOLITAIN');
