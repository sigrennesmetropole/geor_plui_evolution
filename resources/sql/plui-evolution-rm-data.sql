INSERT INTO pluievolution.user_ ( login, first_name, last_name, email) VALUES ( 't.brule', 'Thomas', 'Brulé', 't.brule@rennesmetropole.fr');

-- Insertion des enumération de Status
INSERT INTO pluievolution.request_status (id, value) VALUES (1, 'STATUT_NOUVEAU');
INSERT INTO pluievolution.request_status (id, value) VALUES (2, 'STATUT_ANALYSE_EN_COURS');
INSERT INTO pluievolution.request_status (id, value) VALUES (3, 'STATUT_PRODUCTION_EN_COURS');
INSERT INTO pluievolution.request_status (id, value) VALUES (4, 'STATUT_EN_ATTENTE_VALIDATION_COMMUNE');
INSERT INTO pluievolution.request_status (id, value) VALUES (5, 'STATUT_VALIDE_COMMUNE');
INSERT INTO pluievolution.request_status (id, value) VALUES (6, 'STATUT_DEMANDE_NON_RECEVABLE');
INSERT INTO pluievolution.request_status (id, value) VALUES (7, 'STATUT_DEMANDE_REFORMULEE');

-- Insertion des énumérations de PluiRequest
INSERT INTO pluievolution.request_type (id, value) VALUES (1, 'TYPE_COMMUNE');
INSERT INTO pluievolution.request_type (id, value) VALUES (2, 'TYPE_INTERCOMMUNE');
INSERT INTO pluievolution.request_type (id, value) VALUES (3, 'TYPE_METROPOLITAIN');

-- Insertion des differentes communes
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (749,'Mairie de le Verger', '01010000206C0F0000F6285C9FD1553441643BDF936A8A5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (734,'Mairie de Brécé', '01010000206C0F0000ED0DBE4074D93441643BDF8BBD8C5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (750,'Mairie de Montgermont', '01010000206C0F00002731083CF29634412CD49AF2A5925B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (757,'Mairie de Parthenay-de-Bretagne', '01010000206C0F000089D2DEA0807634411D5A64E70D975B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (745,'Mairie de l''Hermitage', '01010000206C0F00008D976EF29B7834418331A280AC8F5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (767,'Mairie de Vern-sur-Seiche', '01010000206C0F00002D211F3448B53441075F984418865B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (736,'Mairie de Cesson-Sévigné', '01010000206C0F000087A7570AD1B6344132E6AE393E8E5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (746,'Mairie de La Chapelle-des-Fougeretz', '01010000206C0F0000D5096862C19234412CD49AAE07955B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (737,'Mairie de Chantepie', '01010000206C0F0000226C78EAEEB03441736891A5EC8A5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (747,'Mairie de la Chapelle-Thouarault', '01010000206C0F0000A60A4615836A344122FDF6B5D88F5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (738,'Mairie de Chartres-de-Bretagne', '01010000206C0F00008F5374F47A963441AF94659827865B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (748,'Mairie de Le Rheu', '01010000206C0F00008BFD65B7D87E3441643BDF97008D5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (756,'Mairie de Pacé', '01010000206C0F000072F90F49478634410F0BB55EE9915B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (739,'Mairie de Chavagne', '01010000206C0F0000645DDCA6087F3441C4B12E92C1875B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (740,'Mairie de Chevaigné', '01010000206C0F00002B18957443B13441333333035E985B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (758,'Mairie de Pont-Péan', '01010000206C0F0000992A18450C963441341136ECE1825B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (741,'Mairie de Cintré', '01010000206C0F000021B072D83D68344176711BB5EF8D5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (751,'Mairie de Mordelles', '01010000206C0F00003CBD5256CB6E34414F4013ED478A5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (759,'Mairie de Saint-Armel', '01010000206C0F0000A395D69ECEB634410B3815CABD825B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (765,'Mairie de Saint-Sulpice-la-Forêt', '01010000206C0F000060E5D0F27FC034415A643B1FDD985B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (762,'Mairie de Saint-Grégoire', '01010000206C0F000039454742B7A03441302AA96749925B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (742,'Mairie de Clayes', '01010000206C0F00002B189524B56F34412DB29D3F7F955B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (752,'Mairie de Nouvoitou', '01010000206C0F0000857CD00357C5344127C286678E855B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (760,'Mairie de Saint-Erblon', '01010000206C0F00001748504C1FA634414F1E16326E835B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (766,'Mairie de Thorigné-Fouillard', '01010000206C0F00006A4DF3AEEABD344189D2DE58CB915B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (743,'Mairie de Corps-Nuds', '01010000206C0F00007F6ABCB492B7344123DBF9E6EC7E5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (761,'Mairie de Saint-Gilles', '01010000206C0F0000547424B71A763441211FF46C04935B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (744,'Mairie de Gévezé', '01010000206C0F00003108AC3C9A8334418FE4F24F039A5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (754,'Mairie de Noyal-Châtillon-sur-Seiche', '01010000206C0F0000EA95B29CB2A33441516B9AC725865B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (735,'Mairie de Bruz', '01010000206C0F0000BBB88D96C68A34417FFB3AB08A845B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (768,'Mairie de Vezin-le-Coquet', '01010000206C0F0000A54E40B3A78A3441F1F44A11D68E5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (731,'Mairie d''Acigné', '01010000206C0F0000D3BCE33433CB34413BDF4F41718F5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (732,'Mairie de Betton', '01010000206C0F000084D2BCF8A8AD344154B854D32F955B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (733,'Mairie de Bourgbarré', '01010000206C0F000031992AE85AB0344109F9A07BC3805B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (763,'Mairie de Saint-Jacques-de-la-Lande', '01010000206C0F0000B9CFE09DE395344160F44B64788A5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (755,'Mairie d''Orgères', '01010000206C0F000029C46D64CAA034412B0E8CC225815B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (1242,'Mairie de Miniac-sous-Bécherel', '01010000206C0F00008104C57F3E5C34419318042E8CA15B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (1241,'Mairie de La Chapelle-Chaussée', '01010000206C0F0000CB2F8355557234419472ADDEBB9F5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (1155,'Mairie de Laillé', '01010000206C0F0000C3D32B75BD903441F853E3D17A7F5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (1243,'Mairie de Langan', '01010000206C0F0000234A7B534F71344114D044D4DE9C5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (1240,'Mairie de Romillé', '01010000206C0F0000DE6AE7377865344163450D53E9995B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (5120,'Mairie de Rennes', '01010000206C0F00009A9999993CA034417B14AEE7AA8D5B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (1244,'Mairie de Bécherel', '01010000206C0F00003CD807B24958344153A48CC7F0A25B41');
INSERT INTO pluievolution.geographic_area (id, nom, geometry) VALUES (5022,'Rennes Métropole', '01010000206C0F0000B81E85AB32A134417B14AEC7AF8B5B41');
