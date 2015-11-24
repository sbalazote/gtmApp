START TRANSACTION;

USE pharmacy_dev;

INSERT INTO PROVINCE (ID, NAME) VALUES (1,'CIUDAD AUTONOMA DE BUENOS AIRES');
INSERT INTO PROVINCE (ID, NAME) VALUES (2,'BUENOS AIRES');
INSERT INTO PROVINCE (ID, NAME) VALUES (3,'CATAMARCA');
INSERT INTO PROVINCE (ID, NAME) VALUES (4,'CHACO');
INSERT INTO PROVINCE (ID, NAME) VALUES (5,'CHUBUT');
INSERT INTO PROVINCE (ID, NAME) VALUES (6,'CORDOBA');
INSERT INTO PROVINCE (ID, NAME) VALUES (7,'CORRIENTES');
INSERT INTO PROVINCE (ID, NAME) VALUES (8,'ENTRE RIOS');
INSERT INTO PROVINCE (ID, NAME) VALUES (9,'FORMOSA');
INSERT INTO PROVINCE (ID, NAME) VALUES (10,'JUJUY');
INSERT INTO PROVINCE (ID, NAME) VALUES (11,'LA PAMPA');
INSERT INTO PROVINCE (ID, NAME) VALUES (12,'LA RIOJA');
INSERT INTO PROVINCE (ID, NAME) VALUES (13,'MENDOZA');
INSERT INTO PROVINCE (ID, NAME) VALUES (14,'MISIONES');
INSERT INTO PROVINCE (ID, NAME) VALUES (15,'NEUQUEN');
INSERT INTO PROVINCE (ID, NAME) VALUES (16,'RIO NEGRO');
INSERT INTO PROVINCE (ID, NAME) VALUES (17,'SALTA');
INSERT INTO PROVINCE (ID, NAME) VALUES (18,'SAN JUAN');
INSERT INTO PROVINCE (ID, NAME) VALUES (19,'SAN LUIS');
INSERT INTO PROVINCE (ID, NAME) VALUES (20,'SANTA CRUZ');
INSERT INTO PROVINCE (ID, NAME) VALUES (21,'SANTA FE');
INSERT INTO PROVINCE (ID, NAME) VALUES (22,'SANTIAGO DEL ESTERO');
INSERT INTO PROVINCE (ID, NAME) VALUES (23,'TIERRA DEL FUEGO');
INSERT INTO PROVINCE (ID, NAME) VALUES (24,'TUCUMAN');

insert into VAT_liability values (1, 'RI', 'Responsable Inscripto');
insert into VAT_liability values (2, 'RNI', 'Responsable no Inscripto');
insert into VAT_liability values (3, 'EX', 'Exento');
insert into VAT_liability values (4, 'NR', 'No Responsable');
insert into VAT_liability values (5, 'CF', 'Consumidor Final');

INSERT INTO `product_group` VALUES (1,0,'NINGUNO',1),(2,1,'P FARMACEUTICO',1),(3,2,'CADENA DE FRIO',1),(4,3,'PSICOTROPICO',1),(5,4,'DESCARTABLE',1),(6,5,'P.FARM (E)',1),(7,6,'CADENA DE FRIO (E)',1),(8,7,'SOLUCION',1);

insert into agent (id, code, description, active) values (1, 1,'NINGUNO', true);
insert into agent (id, code, description, active) values (2, 2,'LABORATORIO', true);
insert into agent (id, code, description, active) values (3, 3,'DROGUERIA', true);
insert into agent (id, code, description, active) values (4, 4,'DISTRIBUIDORA', true);
insert into agent (id, code, description, active) values (5, 5,'OPERADOR LOGISTICO', true);
insert into agent (id, code, description, active) values (6, 6,'ESTABLECIMIENTO ASIST.', true);
insert into agent (id, code, description, active) values (7, 7,'FARMACIA', true);
insert into agent (id, code, description, active) values (8, 8,'LAB. DE MEZCLA INTRAVENOSA', true);
insert into agent (id, code, description, active) values (9, 9,'DEPOSITO ESTATAL', true);

insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (1,3,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',4,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (2,11,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',7,4, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (3,23,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR VENCIMIENTO',7,4, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (4,29,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',7,4, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (5,39,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',3,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (6,51,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',7,3, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (7,61,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR VENCIMIENTO',7,3, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (8,69,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',7,3, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (9,75,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',7,6, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (10,80,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION',6,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (11,88,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR VENCIMIENTO',6,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (12,93,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR PROHIBICION',6,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (13,98,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',7,6, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (14,99,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',7,8, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (15,100,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',4,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (16,101,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',3,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (17,102,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',2,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (18,103,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',5,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (19,104,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION',7,4, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (20,105,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION',7,3, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (21,106,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION',7,2, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (22,107,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION',7,5, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (23,108,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',6,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (24,109,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',8,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (25,110,'CODIGO DETERIORADO/DESTRUIDO',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (26,111,'DISPENSACION DEL PRODUCTO AL PACIENTE',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (27,112,'PRODUCTO ROBADO/EXTRAVIADO',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (28,113,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR VENCIMIENTO',7,4, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (29,114,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR VENCIMIENTO',7,3, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (30,115,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR VENCIMIENTO',7,2, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (31,116,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR VENCIMIENTO',7,5, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (32,117,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR VENCIMIENTO',6,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (33,118,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR VENCIMIENTO',8,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (34,119,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR PROHIBICION',7,4, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (35,120,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR PROHIBICION',7,3, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (36,121,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR PROHIBICION',7,2, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (37,122,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR PROHIBICION',7,5, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (38,123,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',6,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (39,124,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',8,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (40,125,'DESTRUCCION DE MEDICAMENTO POR PROHIBICION',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (41,126,'DESTRUCCION DE MEDICAMENTO POR VENCIMIENTO',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (42,130,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',2,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (43,141,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',7,2, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (44,153,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR VENCIMIENTO',7,2, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (45,161,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',7,2, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (46,172,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',7,8, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (47,177,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION',8,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (48,185,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR VENCIMIENTO',8,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (49,190,'ENVIO DE PRODUCTO EN CARACTER DEVOLUCION POR PROHIBICION',8,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (50,197,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',5,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (51,207,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',7,5, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (52,218,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR VENCIMIENTO',7,5, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (53,224,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',7,5, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (54,239,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',9,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (55,252,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION',7,9, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (56,267,'RECEPCION DE PRODUCTO EN CARACTER DE DEVOLUCION POR PROHIBICION',7,9, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (57,277,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',9,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (58,436,'RETIRO DE MUESTRA POR AUTORIDAD SANITARIA JURISDICCIONAL',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (59,446,'RETIRO DE MUESTRA POR AUTORIDAD SANITARIA. ANMAT',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (60,518,'DISTRIBUCION DEL PRODUCTO A UN ESLABON POSTERIOR',7,9, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (61,528,'RECEPCION DE PRODUCTO DESDE UN ESLABON ANTERIOR',7,9, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (62,848,'FINALIZAR EMPAQUE POR FRACCIONAMIENTO',7,1, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (63,862,'PRÉSTAMO POR URGENCIA',7,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (64,863,'RECEPCIÓN DE PRÉSTAMO POR URGENCIA',7,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (65,864,'REPOSICIÓN DE PRÉSTAMO POR URGENCIA',7,7, true);
insert into event (id, code, description, origin_agent_id, destination_agent_id, active) values (66,865,'RECEPCIÓN DE REPOSICIÓN DE PRÉSTAMO POR URGENCIA',7,7, true);


insert into `delivery_note_enumerator` values (1,'0001','00000000',1,0);

INSERT INTO `concept` VALUES
(1,1,'PDT:TOMA DE INVENTARIO',1,1,1,'\0',1,1,0,1),
(2,2,'PDT:INGRESO POR COMPRAS',1,1,1,'\0',1,1,0,1),
(3,3,'PDT:ENVIO A SUCURSALES','\0',1,1,'\0',1,1,0,1),
(4,4,'PDT:DEVOLUCION A PROVEEDORES','\0',1,1,'\0',1,1,0,1),
(5,5,'PSI:TOMA DE INVENTARIO',1,1,1,'\0',1,1,0,1),
(6,6,'PSI:INGRESO POR COMPRAS',1,1,1,'\0',1,1,0,1),
(7,7,'PSI:ENVIO A SUCURSALES','\0',1,1,'\0',1,1,0,1),
(8,8,'PSI:DEVOLUCION A PROVEEDORES','\0',1,1,'\0',1,1,0,1),
(9,11,'DSK:INGRESO POR DEPOSITO',1,1,1,'\0',1,1,0,1),
(10,12,'DSK:INGRESO POR SUCURSAL',1,1,1,'\0','\0',1,0,1),
(11,13,'DSK:ENVIO A SUCURSALES','\0',1,1,'\0',1,1,0,1),
(12,14,'PDT:AJUSTE SOBRANTE INVENTARIO','\0',1,1,'\0',1,1,0,1),
(13,15,'PDT:AJUSTE FALTANTE INVENTARIO',1,1,1,'\0',1,1,0,1),
(14,16,'PDT:AJUSTE SOBRANTE ENVIO DEPO','\0',1,1,'\0',1,1,0,1),
(15,17,'PDT:AJUSTE FALTANTE ENVIO DEPO',1,1,1,'\0',1,1,0,1),
(16,18,'PDT:AJUSTE SOBRANTE ENVIO SUCU','\0',1,1,'\0',1,1,0,1),
(17,19,'PDT:AJUSTE FALTANTE ENVIO SUCU',1,1,1,'\0',1,1,0,1),
(18,20,'MAN:AJUSTE POR DEVOLUCION N/C',1,1,1,'\0',1,1,0,1),
(19,21,'CRF:INGRESO POR COMPRAS X',1,1,1,'\0',1,1,0,1),
(20,22,'CRF:ENVIO A SUCURSALES','\0',1,1,'\0',1,1,0,1),
(21,23,'CRF:DEVOLUCION A PROVEEDORES','\0',1,1,'\0',1,1,0,1),
(22,54,'SALIDA POR REMITO *CONSIGNADO','\0',1,1,'\0','\0',1,0,1),
(23,55,'SALIDA POR REMITO *EN FIRME','\0',1,1,'\0','\0',1,0,1),
(24,56,'INGRESO POR BAJA DE REMITO',1,1,1,'\0','\0',1,0,1),
(25,57,'EGRESO TRANSF. DE CONVENIO','\0',1,1,'\0','\0',1,0,1),
(26,58,'INGRESO TRANSF. DE CONVENIO',1,1,1,'\0','\0',1,0,1),
(27,60,'DEVOLUCION DE CLIENTE',1,1,1,'\0','\0',1,0,1),
(28,63,'DEVOLUCION DE SUCURSAL',1,1,1,'\0','\0',1,0,1),
(29,70,'EGRESO TRANSF. SUBDEPOSITO','\0',1,1,'\0','\0',1,0,1),
(30,71,'INGRESO TRANSF. SUBDEPOSITO',1,1,1,'\0','\0',1,0,1),
(31,101,'INGRESO - INICIO DE TRAZA',1,1,1,'\0',1,1,0,1),
(32,102,'MAN:INGRESO POR COMPRAS',1,1,1,'\0',1,1,0,1),
(33,103,'ENVIO A SUCURSALES','\0',1,1,'\0',1,1,0,1),
(34,104,'MAN:DEVOLUCION PROVEEDORES','\0',1,1,'\0',1,1,0,1),
(35,120,'INGRESO POR COMPRAS',1,1,1,'\0',1,1,0,1),
(36,140,'AJUSTE STOCK (-)','\0',1,1,'\0',1,1,0,1),
(37,150,'INGRESO - ANUL REMITO INTERNO',1,1,1,'\0','\0',1,0,1),
(38,151,'EGRESO - ANUL. REMITO INTERNO','\0',1,1,'\0','\0',1,0,1),
(39,152,'AJUSTE (-) ERROR EN CARGA','\0',1,1,'\0','\0',1,0,1),
(40,153,'ENVIO A DESTRUCCION (-)','\0',1,1,'\0',1,1,0,1),
(41,154,'AJUSTE (+) POR LTE EN -',1,1,1,'\0',1,1,0,1),
(42,155,'AJUSTE (-) POR LTE +','\0',1,1,'\0',1,1,0,1),
(43,156,'AJUSTE+POR ERR EN EVIO A SUC',1,1,1,'\0',1,1,0,1),
(44,157,'BAJA X CAMBIO LTE A SER','\0',1,1,'\0',1,1,0,1),
(45,158,'ALTA X CAMBIO LTE A SER',1,1,1,'\0',1,1,0,1),
(46,160,'DEVOLUCION LOTE Y VTO',1,1,1,'\0','\0',1,0,1),
(47,200,'MOVIMIENTOS EXTRACAPITA OSECAC','\0',1,1,'\0','\0',1,0,1),
(48,202,'RECALL ONCOLOGIA','\0',1,1,'\0','\0',1,0,1),
(49,203,'RECALL OSECAC','\0',1,1,'\0','\0',1,0,1);

insert into provider_type (id, code, description, active) values (1, 1,'MEDICAMENTOS', true);
insert into provider_type (id, code, description, active) values (2, 2,'DESCARTABLES', true);
insert into provider_type (id, code, description, active) values (3, 3,'EMBALAJES', true);

INSERT INTO `agreement` (id, code, description, order_label_printer, delivery_note_printer, delivery_note_concept_id, destruction_concept_id, active) VALUES
(1,1,'ONCOMED','C:/ONCOMED/rotulo/','C:/ONCOMED/deliveryNotes/',23,31,1),
(2,2,'OSECAC AC','C:/OSECACAC/rotulo/','C:/OSECACAC/deliveryNotes/',23,31,1),
(3,3,'EXTRACAPITA','C:/EXTRACAPITA/rotulo/','C:/EXTRACAPITA/deliveryNotes/',23,31,1),
(4,4,'PAC','C:/PAC/rotulo/','C:/PAC/deliveryNotes/',23,31,1),
(6,6,'OSECAC PPG','C:/OSECACPPG/rotulo/','C:/OSECACPPG/deliveryNotes/',23,31,1),
(7,7,'OTROS ONCOMED','C:/OTROS/rotulo/','C:/OTROS/deliveryNotes/',23,31,1);

insert into provisioning_request_state (id, description) values (1,'INGRESADO');
insert into provisioning_request_state (id, description) values (2,'AUTORIZADO');
insert into provisioning_request_state (id, description) values (3,'IMPRESO');
insert into provisioning_request_state (id, description) values (4,'ARMADO');
insert into provisioning_request_state (id, description) values (5,'REMITO IMPRESO');
insert into provisioning_request_state (id, description) values (6,'FACTURADO');
insert into provisioning_request_state (id, description) values (7,'ANULADO');
insert into provisioning_request_state (id, description) values (8,'CERRADO');

insert into logistics_operator (id, code, name, tax_id, corporate_name, province_id, locality, address, zip_code, phone, active)
values (1, 8686,'OCA','30-99991111-0','OCA. SA', 1,'Capital Federal','Segurola 417','1407','4567-8901', true);

insert into logistics_operator (id, code, name, tax_id, corporate_name, province_id, locality, address, zip_code, phone, active)
values (2, 8687,'OCA 2','30-99991112-0','OCA2. SA', 1,'Capital Federal','Rivadavia 7700','1507','4639-8888', true);

insert into serial_separation_mapping (id, code, separator_token) values (1,'G','010');
insert into serial_separation_mapping (id, code, separator_token) values (2,'S','21');
insert into serial_separation_mapping (id, code, separator_token) values (3,'B','10');
insert into serial_separation_mapping (id, code, separator_token) values (4,'E','17');

insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (1,13,10,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (2,13,10,6,5,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (3,13,10,6,8,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (4,13,20,6,8,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (5,13,8,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (6,13,2,6,5,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (7,13,3,6,5,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (8,13,8,6,5,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (9,13,5,6,6,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (10,13,8,6,7,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (11,13,10,6,5,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (12,13,20,6,5,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (13,13,10,6,6,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (14,13,12,6,6,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (15,13,10,6,7,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (16,13,12,6,7,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (17,13,10,6,8,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (18,13,12,6,8,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (19,13,20,6,8,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (20,13,10,6,9,'G-B-E-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (21,13,7,6,3,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (22,13,7,6,4,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (23,13,5,6,5,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (24,13,6,6,5,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (25,13,7,6,5,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (26,13,8,6,5,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (27,13,9,6,5,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (28,13,2,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (29,13,3,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (30,13,4,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (31,13,5,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (32,13,7,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (33,13,9,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (34,13,8,6,7,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (35,13,6,6,8,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (36,13,7,6,8,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (37,13,8,6,8,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (38,13,8,6,9,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (39,13,8,6,10,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (40,13,10,6,2,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (41,13,19,6,4,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (42,13,10,6,5,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (43,13,20,6,6,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (44,13,10,6,7,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (45,13,12,6,7,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (46,13,16,6,7,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (47,13,20,6,7,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (48,13,10,6,8,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (49,13,10,14,4,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (50,13,10,6,10,'G-E-B-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (51,13,7,6,6,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (52,13,12,6,5,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (53,13,12,6,6,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (54,13,12,6,8,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (55,13,12,6,9,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (56,13,19,6,4,'G-E-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (57,13,3,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (58,13,4,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (59,13,5,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (60,13,6,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (61,13,7,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (62,13,8,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (63,13,9,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (64,13,11,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (65,13,12,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (66,13,16,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (67,13,20,null,null,'G-S');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (68,13,10,null,6,'G-S-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (69,13,7,6,3,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (70,13,10,6,4,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (71,13,10,6,5,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (72,13,10,6,8,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (73,13,12,6,7,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (74,13,12,6,8,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (75,13,10,6,10,'G-S-B-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (76,13,10,6,null,'G-S-E');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (77,13,5,6,6,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (78,13,6,6,6,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (79,13,6,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (80,13,8,6,5,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (81,13,8,6,7,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (82,13,8,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (83,13,10,6,3,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (84,13,10,6,4,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (85,13,10,6,5,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (86,13,10,6,6,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (87,13,10,6,7,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (88,13,10,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (89,13,10,6,9,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (90,13,12,6,4,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (91,13,12,6,6,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (92,13,12,6,7,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (93,13,12,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (94,13,16,6,7,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (95,13,16,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (96,13,20,6,4,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (97,13,20,6,5,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (98,13,20,6,6,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (99,13,20,6,8,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (100,13,8,6,10,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (101,13,10,6,10,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (102,13,10,6,11,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (103,13,10,6,13,'G-S-E-B');
insert into provider_serialized_format (id,gtin_length,serial_number_length,expiration_date_length,batch_length,sequence) values (104,13,20,6,10,'G-S-E-B');


INSERT INTO `property` (id, code, name, tax_id, corporate_name, province_id, locality, address, zip_code, phone, mail, gln, agent_id, last_tag, self_serialized_tag_filepath, ANMAT_password, start_trace_concept_id, proxy,proxy_port,inform_proxy,supplying_concept_id, provisioning_require_authorization, print_picking_list)
VALUES (1,86,'FARMACIA','30686437228','FARMACIA',1,'C.A.B.A.','AGUERO 1223','1425','4963-1500',NULL,'7798169170001',7,0,'C:/selfSerializedTagsPrinter/',"ZrFFnPSO9FCCOwRq7/DYzg==",23,"","",0,1,0,0);

insert into `pharmacy_dev`.`role` values (1, 'INPUT', 'Ingreso');
insert into `pharmacy_dev`.`role` values (2, 'OUTPUT', 'Egreso');
insert into `pharmacy_dev`.`role` values (3, 'PROVISIONING_REQUEST', 'Pedido');
insert into `pharmacy_dev`.`role` values (4, 'PROVISIONING_REQUEST_UPDATE', 'Modificación de Pedidos');
insert into `pharmacy_dev`.`role` values (5, 'PROVISIONING_REQUEST_AUTHORIZATION', 'Autorización de Pedidos');
insert into `pharmacy_dev`.`role` values (6, 'PROVISIONING_REQUEST_CANCELLATION', 'Anulación de Pedidos');
insert into `pharmacy_dev`.`role` values (7, 'PROVISIONING_REQUEST_PRINT', 'Impresión de Hojas de Picking');
insert into `pharmacy_dev`.`role` values (8, 'ORDER_ASSEMBLY', 'Armado de Pedido');
insert into `pharmacy_dev`.`role` values (9, 'ORDER_ASSEMBLY_CANCELLATION', 'Anulación de Armado de Pedido');
insert into `pharmacy_dev`.`role` values (10, 'DELIVERY_NOTE_PRINT', 'Emitir Remito');
insert into `pharmacy_dev`.`role` values (11, 'DELIVERY_NOTE_CANCELLATION', 'Anulación de Remitos');
insert into `pharmacy_dev`.`role` values (12, 'ENTITY_ADMINISTRATION', 'Administración de Entidades');
insert into `pharmacy_dev`.`role` values (13, 'USER_ADMINISTRATION', 'Administración de Usuarios');
insert into `pharmacy_dev`.`role` values (14, 'SERIALIZED_RETURNS', 'Devolución de Series');
insert into `pharmacy_dev`.`role` values (15, 'INPUT_CANCELLATION', 'Anulación de Ingreso');
insert into `pharmacy_dev`.`role` values (16, 'INPUT_AUTHORIZATION', 'Autorización de Ingreso');
insert into `pharmacy_dev`.`role` values (17, 'OUTPUT_CANCELLATION', 'Anulación de Egreso');
insert into `pharmacy_dev`.`role` values (18, 'AGREEMENT_TRANSFER', 'Transferencia de Convenio');
insert into `pharmacy_dev`.`role` values (19, 'SUPPLYING', 'Dispensa');
insert into `pharmacy_dev`.`role` values (20, 'SUPPLYING_CANCELLATION', 'Anulación de Dispensa');
insert into `pharmacy_dev`.`role` values (21, 'PENDING_TRANSACTIONS', 'Transacciones Pendientes');
insert into `pharmacy_dev`.`role` values (22, 'LOGISTIC_OPERATOR_ASSIGNMENT', 'Asignacion de Operador Logistico');
insert into `pharmacy_dev`.`role` values (23, 'SEARCH_INPUTS', 'Busqueda de Ingresos');
insert into `pharmacy_dev`.`role` values (24, 'SEARCH_OUTPUTS', 'Busqueda de Egresos');
insert into `pharmacy_dev`.`role` values (25, 'SEARCH_PROVISIONING_REQUEST', 'Busqueda de Pedidos');
insert into `pharmacy_dev`.`role` values (26, 'SEARCH_SUPPLYING', 'Busqueda de Dispensas');
insert into `pharmacy_dev`.`role` values (27, 'SEARCH_DELIVERY_NOTE', 'Busqueda de Remitos');
insert into `pharmacy_dev`.`role` values (28, 'SEARCH_AUDIT', 'Auditoria');
insert into `pharmacy_dev`.`role` values (29, 'SEARCH_STOCK', 'Busqueda de Stock');
insert into `pharmacy_dev`.`role` values (30, 'SEARCH_SERIALIZED_PRODUCT', 'Traza por Serie');
insert into `pharmacy_dev`.`role` values (31, 'SEARCH_BATCH_EXPIRATEDATE_PRODUCT', 'Traza por Lote');
insert into `pharmacy_dev`.`role` values (32, 'AFFILIATE_ADMINISTRATION', 'Administracion de Afiliados');
insert into `pharmacy_dev`.`role` values (33, 'AGENT_ADMINISTRATION', 'Administracion de Agentes');
insert into `pharmacy_dev`.`role` values (34, 'CLIENT_ADMINISTRATION', 'Administracion de Clientes');
insert into `pharmacy_dev`.`role` values (35, 'CONCEPT_ADMINISTRATION', 'Administracion de Conceptos');
insert into `pharmacy_dev`.`role` values (36, 'AGREEMENT_ADMINISTRATION', 'Administracion de Convenios');
insert into `pharmacy_dev`.`role` values (37, 'EVENT_ADMINISTRATION', 'Administracion de Eventos');
insert into `pharmacy_dev`.`role` values (38, 'DELIVERY_LOCATION_ADMINISTRATION', 'Administracion de Lugares de Entrega');
insert into `pharmacy_dev`.`role` values (39, 'LOGISTIC_OPERATOR_ADMINISTRATION', 'Administracion de Operador Logistico');
insert into `pharmacy_dev`.`role` values (40, 'PRODUCT_ADMINISTRATION', 'Administracion de Productos');
insert into `pharmacy_dev`.`role` values (41, 'PROVIDER_ADMINISTRATION', 'Administracion de Proveedores');
insert into `pharmacy_dev`.`role` values (42, 'DELIVERY_NOTE_ENUMERATOR_ADMINISTRATION', 'Administracion de Puntos de Venta');
insert into `pharmacy_dev`.`role` values (43, 'PROVIDER_SERIALIZED_FORMAT_ADMINISTRATION', 'Administracion de Formatos de Serializacion');
insert into `pharmacy_dev`.`role` values (44, 'PROFILE_ADMINISTRATION', 'Administracion de Perfiles');
insert into `pharmacy_dev`.`role` values (45, 'PROPERTY_ADMINISTRATION', 'Administracion de Propiedades');

insert into `pharmacy_dev`.`profile` values (1, "admin");

insert into profile_role values (1, 1, 1);
insert into profile_role values (2, 1, 2);
insert into profile_role values (3, 1, 3);
insert into profile_role values (4, 1, 4);
insert into profile_role values (5, 1, 5);
insert into profile_role values (6, 1, 6);
insert into profile_role values (7, 1, 7);
insert into profile_role values (8, 1, 8);
insert into profile_role values (9, 1, 9);
insert into profile_role values (10, 1, 10);
insert into profile_role values (11, 1, 11);
insert into profile_role values (12, 1, 12);
insert into profile_role values (13, 1, 13);
insert into profile_role values (14, 1, 14);
insert into profile_role values (15, 1, 15);
insert into profile_role values (16, 1, 16);
insert into profile_role values (17, 1, 17);
insert into profile_role values (18, 1, 18);
insert into profile_role values (19, 1, 19);
insert into profile_role values (20, 1, 20);
insert into profile_role values (21, 1, 21);
insert into profile_role values (22, 1, 22);
insert into profile_role values (23, 1, 23);
insert into profile_role values (24, 1, 24);
insert into profile_role values (25, 1, 25);
insert into profile_role values (26, 1, 26);
insert into profile_role values (27, 1, 27);
insert into profile_role values (28, 1, 28);
insert into profile_role values (29, 1, 29);
insert into profile_role values (30, 1, 30);
insert into profile_role values (31, 1, 31);
insert into profile_role values (32, 1, 32);
insert into profile_role values (33, 1, 33);
insert into profile_role values (34, 1, 34);
insert into profile_role values (35, 1, 35);
insert into profile_role values (36, 1, 36);
insert into profile_role values (37, 1, 37);
insert into profile_role values (38, 1, 38);
insert into profile_role values (39, 1, 39);
insert into profile_role values (40, 1, 40);
insert into profile_role values (41, 1, 41);
insert into profile_role values (42, 1, 42);
insert into profile_role values (43, 1, 43);
insert into profile_role values (44, 1, 44);
insert into profile_role values (45, 1, 45);

insert into `pharmacy_dev`.`user` values (4, "admin", "fad198c1bfe1cc7052905de0fa0431b45ec10ca4", 1,1);

insert into audit_action values (1, 'Confirmado');
insert into audit_action values (2, 'Modificado');
insert into audit_action values (3, 'Autorizado');
insert into audit_action values (4, 'Anulado');

COMMIT;
