CREATE TABLE `delivery_note_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `delivery_note_config` VALUES (1, 'FONT_SIZE', 12);

INSERT INTO `delivery_note_config` VALUES (2, 'NUMBER_X', 164);
INSERT INTO `delivery_note_config` VALUES (3, 'NUMBER_Y', 16);
INSERT INTO `delivery_note_config` VALUES (4, 'NUMBER_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (5, 'DATE_X', 158);
INSERT INTO `delivery_note_config` VALUES (6, 'DATE_Y', 21);
INSERT INTO `delivery_note_config` VALUES (7, 'DATE_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (8, 'ISSUER_CORPORATENAME_X', 13);
INSERT INTO `delivery_note_config` VALUES (9, 'ISSUER_CORPORATENAME_Y', 45);
INSERT INTO `delivery_note_config` VALUES (10, 'ISSUER_CORPORATENAME_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (11, 'ISSUER_ADDRESS_X', 25);
INSERT INTO `delivery_note_config` VALUES (12, 'ISSUER_ADDRESS_Y', 52);
INSERT INTO `delivery_note_config` VALUES (13, 'ISSUER_ADDRESS_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (14, 'ISSUER_LOCALITY_X', 25);
INSERT INTO `delivery_note_config` VALUES (15, 'ISSUER_LOCALITY_Y', 57);
INSERT INTO `delivery_note_config` VALUES (16, 'ISSUER_LOCALITY_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (17, 'ISSUER_ZIPCODE_X', 43);
INSERT INTO `delivery_note_config` VALUES (18, 'ISSUER_ZIPCODE_Y', 57);
INSERT INTO `delivery_note_config` VALUES (19, 'ISSUER_ZIPCODE_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (20, 'ISSUER_PROVINCE_X', 58);
INSERT INTO `delivery_note_config` VALUES (21, 'ISSUER_PROVINCE_Y', 57);
INSERT INTO `delivery_note_config` VALUES (22, 'ISSUER_PROVINCE_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (23, 'ISSUER_VATLIABILITY_X', 158);
INSERT INTO `delivery_note_config` VALUES (24, 'ISSUER_VATLIABILITY_Y', 43);
INSERT INTO `delivery_note_config` VALUES (25, 'ISSUER_VATLIABILITY_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (26, 'ISSUER_TAX_X', 158);
INSERT INTO `delivery_note_config` VALUES (27, 'ISSUER_TAX_Y', 53);
INSERT INTO `delivery_note_config` VALUES (28, 'ISSUER_TAX_PRINT', 1);


INSERT INTO `delivery_note_config` VALUES (29, 'ISSUER_GLN_X', 13);
INSERT INTO `delivery_note_config` VALUES (30, 'ISSUER_GLN_Y', 108);
INSERT INTO `delivery_note_config` VALUES (31, 'ISSUER_GLN_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (32, 'DELIVERYLOCATION_CORPORATENAME_X', 43);
INSERT INTO `delivery_note_config` VALUES (33, 'DELIVERYLOCATION_CORPORATENAME_Y', 69);
INSERT INTO `delivery_note_config` VALUES (34, 'DELIVERYLOCATION_CORPORATENAME_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (35, 'DELIVERYLOCATION_ADDRESS_X', 45);
INSERT INTO `delivery_note_config` VALUES (36, 'DELIVERYLOCATION_ADDRESS_Y', 74);
INSERT INTO `delivery_note_config` VALUES (37, 'DELIVERYLOCATION_ADDRESS_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (38, 'DELIVERYLOCATION_LOCALITY_X', 38);
INSERT INTO `delivery_note_config` VALUES (39, 'DELIVERYLOCATION_LOCALITY_Y', 78);
INSERT INTO `delivery_note_config` VALUES (40, 'DELIVERYLOCATION_LOCALITY_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (41, 'DELIVERYLOCATION_ZIPCODE_X', 56);
INSERT INTO `delivery_note_config` VALUES (42, 'DELIVERYLOCATION_ZIPCODE_Y', 78);
INSERT INTO `delivery_note_config` VALUES (43, 'DELIVERYLOCATION_ZIPCODE_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (44, 'DELIVERYLOCATION_PROVINCE_X', 71);
INSERT INTO `delivery_note_config` VALUES (45, 'DELIVERYLOCATION_PROVINCE_Y', 78);
INSERT INTO `delivery_note_config` VALUES (46, 'DELIVERYLOCATION_PROVINCE_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (47, 'DELIVERYLOCATION_VATLIABILITY_X', 158);
INSERT INTO `delivery_note_config` VALUES (48, 'DELIVERYLOCATION_VATLIABILITY_Y', 72);
INSERT INTO `delivery_note_config` VALUES (49, 'DELIVERYLOCATION_VATLIABILITY_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (50, 'DELIVERYLOCATION_TAX_X', 158);
INSERT INTO `delivery_note_config` VALUES (51, 'DELIVERYLOCATION_TAX_Y', 82);
INSERT INTO `delivery_note_config` VALUES (52, 'DELIVERYLOCATION_TAX_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (53, 'DELIVERYLOCATION_GLN_X', 60);
INSERT INTO `delivery_note_config` VALUES (54, 'DELIVERYLOCATION_GLN_Y', 108);
INSERT INTO `delivery_note_config` VALUES (55, 'DELIVERYLOCATION_GLN_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (56, 'AFFILIATE_X', 13);
INSERT INTO `delivery_note_config` VALUES (57, 'AFFILIATE_Y', 100);
INSERT INTO `delivery_note_config` VALUES (58, 'AFFILIATE_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (59, 'ORDER_X', 13);
INSERT INTO `delivery_note_config` VALUES (60, 'ORDER_Y', 104);
INSERT INTO `delivery_note_config` VALUES (61, 'ORDER_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (62, 'PRODUCT_DETAILS_Y', 115);

INSERT INTO `delivery_note_config` VALUES (63, 'PRODUCT_DESCRIPTION_X', 13);
INSERT INTO `delivery_note_config` VALUES (64, 'PRODUCT_DESCRIPTION_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (65, 'PRODUCT_MONODRUG_X', 60);
INSERT INTO `delivery_note_config` VALUES (66, 'PRODUCT_MONODRUG_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (67, 'PRODUCT_BRAND_X', 90);
INSERT INTO `delivery_note_config` VALUES (68, 'PRODUCT_BRAND_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (69, 'PRODUCT_AMOUNT_X', 190);
INSERT INTO `delivery_note_config` VALUES (70, 'PRODUCT_AMOUNT_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (71, 'PRODUCT_BATCHEXPIRATIONDATE_X', 13);
INSERT INTO `delivery_note_config` VALUES (72, 'PRODUCT_BATCHEXPIRATIONDATE_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (73, 'SERIAL_COLUMN1_X', 13);
INSERT INTO `delivery_note_config` VALUES (74, 'SERIAL_COLUMN2_X', 60);
INSERT INTO `delivery_note_config` VALUES (75, 'SERIAL_COLUMN3_X', 110);
INSERT INTO `delivery_note_config` VALUES (76, 'SERIAL_COLUMN4_X', 160);
INSERT INTO `delivery_note_config` VALUES (77, 'SERIAL_COLUMN1_PRINT', 1);
INSERT INTO `delivery_note_config` VALUES (78, 'SERIAL_COLUMN2_PRINT', 1);
INSERT INTO `delivery_note_config` VALUES (79, 'SERIAL_COLUMN3_PRINT', 1);
INSERT INTO `delivery_note_config` VALUES (80, 'SERIAL_COLUMN4_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (81, 'NUMBEROFITEMS_X', 10);
INSERT INTO `delivery_note_config` VALUES (82, 'NUMBEROFITEMS_Y', 270);1
INSERT INTO `delivery_note_config` VALUES (83, 'NUMBEROFITEMS_PRINT', 1);

INSERT INTO `delivery_note_config` VALUES (84, 'LOGISTICS_OPERATOR_X', 10);
INSERT INTO `delivery_note_config` VALUES (85, 'LOGISTICS_OPERATOR_Y', 280);
INSERT INTO `delivery_note_config` VALUES (86, 'LOGISTICS_OPERATOR_PRINT', 1);