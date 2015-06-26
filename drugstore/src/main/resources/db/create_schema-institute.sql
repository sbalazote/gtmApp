START TRANSACTION;

DROP SCHEMA IF EXISTS `institute`;

/*La primera vez hay que comentar esto */
/*DROP USER gtm;*/

CREATE DATABASE institute;
CREATE USER gtm IDENTIFIED BY 'Gtm4pPlsNt';
GRANT ALL PRIVILEGES ON `institute`.* TO `gtm`@`localhost` IDENTIFIED BY 'Gtm4pPlsNt';

USE institute;

CREATE TABLE `institute`.`agent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(100) NOT NULL,
  `origin_agent_id` int(11) NOT NULL,
  `destination_agent_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_event_agent_1_idx` (`origin_agent_id`),
  KEY `fk_event_agent_2_idx` (`destination_agent_id`),
  CONSTRAINT `fk_event_agent_1` FOREIGN KEY (`origin_agent_id`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_event_agent_2` FOREIGN KEY (`destination_agent_id`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(60) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product_drug_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product_brand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product_monodrug` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`provider_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`concept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `delivery_note_POS` varchar(30) NOT NULL,
  `last_delivery_note_number` int(7) NOT NULL,
  `input` bit(1) NOT NULL,
  `print_delivery_note` bit(1) NOT NULL,
  `delivery_note_copies` int(11) NOT NULL DEFAULT '0',
  `refund` bit(1) NOT NULL,
  `inform_anmat` bit(1) NOT NULL,
  `active` bit(1) NOT NULL,
  `client` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`agreement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `number_of_delivery_note_details_per_page` int(11) NOT NULL DEFAULT 10,
  `order_label_filepath` varchar(255) NOT NULL,
  `delivery_note_filepath` varchar(255) NOT NULL,
  `picking_filepath` varchar(255) NOT NULL,
  `delivery_note_concept_id` int(11) NOT NULL,
  `destruction_concept_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  CONSTRAINT `fk_agreement_delivery_note_concept` FOREIGN KEY (`delivery_note_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agreement_destruction_concept` FOREIGN KEY (`destruction_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`concept_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_concept_event_concept_idx` (`concept_id`),
  KEY `fk_concept_event_event_idx` (`event_id`),
  CONSTRAINT `fk_concept_event_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_concept_event_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(100) NOT NULL,
  `brand_id` int(11) NOT NULL,
  `monodrug_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `drug_category_id` int(11) NOT NULL,
  `cold` bit(1) NOT NULL,
  `inform_anmat` bit(1) NOT NULL,
  `type` varchar(10) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_product_product_brand_idx` (`brand_id`),
  KEY `fk_product_product_monodrug_idx` (`monodrug_id`),
  KEY `fk_product_product_group_idx` (`group_id`),
  KEY `fk_product_product_drug_category_idx` (`drug_category_id`),
  CONSTRAINT `fk_product_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `product_brand` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_product_drug_category` FOREIGN KEY (`drug_category_id`) REFERENCES `product_drug_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_product_group` FOREIGN KEY (`group_id`) REFERENCES `product_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_product_monodrug` FOREIGN KEY (`monodrug_id`) REFERENCES `product_monodrug` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product_gtin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(20) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`),
  KEY `fk_product_gtin_product_idx` (`product_id`),
  CONSTRAINT `fk_product_gtin_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`product_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` decimal(13,4) NOT NULL,
  `product_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_price_product_idx` (`product_id`),
  CONSTRAINT `fk_product_price_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`province` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`VAT_liability` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acronym` varchar(5) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`provider` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `tax_id` varchar(15) NOT NULL,
  `corporate_name` varchar(45) NOT NULL,
  `province_id` int(11) NOT NULL,
  `locality` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `zip_code` varchar(10) NOT NULL,
  `VAT_liability_id` int(11) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `gln` varchar(45) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_provider_province_idx` (`province_id`),
  KEY `fk_provider_VAT_liability_idx` (`VAT_liability_id`),
  KEY `fk_provider_provider_type_idx` (`type_id`),
  KEY `fk_provider_agent_idx` (`agent_id`),
  CONSTRAINT `fk_provider_provider_type` FOREIGN KEY (`type_id`) REFERENCES `provider_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provider_agent` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provider_VAT_liability` FOREIGN KEY (`VAT_liability_id`) REFERENCES `VAT_liability` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provider_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `tax_id` varchar(15) NOT NULL,
  `corporate_name` varchar(45) NOT NULL,
  `province_id` int(11) NOT NULL,
  `locality` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `zip_code` varchar(10) NOT NULL,
  `VAT_liability_id` int(11) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `medical_insurance_code` int(11),
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_client_province_idx` (`province_id`),
  KEY `fk_client_VAT_liability_idx` (`VAT_liability_id`),
  CONSTRAINT `fk_client_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_VAT_liability` FOREIGN KEY (`VAT_liability_id`) REFERENCES `VAT_liability` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`delivery_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `tax_id` varchar(15) NOT NULL,
  `corporate_name` varchar(60) NOT NULL,
  `province_id` int(11) NOT NULL,
  `locality` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `zip_code` varchar(10) NOT NULL,
  `VAT_liability_id` int(11) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `gln` varchar(45) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_delivery_location_province_idx` (`province_id`),
  KEY `fk_delivery_location_VAT_liability_idx` (`VAT_liability_id`),
  KEY `fk_delivery_location_agent_idx` (`agent_id`),
  CONSTRAINT `fk_delivery_location_agent` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_delivery_location_VAT_liability` FOREIGN KEY (`VAT_liability_id`) REFERENCES `VAT_liability` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_delivery_location_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`client_delivery_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `delivery_location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_delivery_location_client_idx` (`client_id`),
  KEY `fk_client_delivery_location_delivery_location_idx` (`delivery_location_id`),
  CONSTRAINT `fk_client_delivery_location_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_delivery_location_delivery_location` FOREIGN KEY (`delivery_location_id`) REFERENCES `delivery_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`affiliate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `document_type` varchar(10) DEFAULT NULL,
  `document` varchar(15) DEFAULT NULL,
  `client_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_affiliate_client_idx` (`client_id`),
  CONSTRAINT `fk_affiliate_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`logistics_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `tax_id` varchar(15) NOT NULL,
  `corporate_name` varchar(45) NOT NULL,
  `province_id` int(11) NOT NULL,
  `locality` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `zip_code` varchar(10) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_logistics_operator_province_idx` (`province_id`),
  CONSTRAINT `fk_logistics_operator_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `agreement_id` int(11) NOT NULL,
  `gtin_id` int(11),
  `serial_number` varchar(30) DEFAULT NULL,
  `batch` varchar(30) NOT NULL,
  `expiration_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_stock_product_idx` (`product_id`),
  KEY `fk_stock_agreement_idx` (`agreement_id`),
  KEY `fk_stock_gtin_idx` (`gtin_id`),
  CONSTRAINT `fk_stock_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_stock_agreement` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_stock_product_gtin` FOREIGN KEY (`gtin_id`) REFERENCES `product_gtin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`input` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL,
  `provider_id` int(11),
  `delivery_location_id` int(11),
  `agreement_id` int(11) NOT NULL,
  `delivery_note_number` varchar(30) DEFAULT NULL,
  `purchase_order_number` varchar(30) DEFAULT NULL,
  `date` date NOT NULL,
  `transaction_code_anmat` varchar(100),
  `cancelled` bit(1) NOT NULL,
  `inform_anmat` bit(1) NOT NULL,
  `informed` bit(1) NOT NULL,
  `forced_input` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_input_concept_idx` (`concept_id`),
  KEY `fk_input_provider_idx` (`provider_id`),
  KEY `fk_input_agreement_idx` (`agreement_id`),
  CONSTRAINT `fk_input_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_agreement` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_delivery_location` FOREIGN KEY (`delivery_location_id`) REFERENCES `delivery_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`input_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `input_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `gtin_id` int(11),
  `serial_number` varchar(30) DEFAULT NULL,
  `batch` varchar(30) NOT NULL,
  `expiration_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_input_detail_input_idx` (`input_id`),
  KEY `fk_input_detail_product_idx` (`product_id`),
  KEY `fk_input_detail_gtin_idx` (`gtin_id`),
  CONSTRAINT `fk_input_detail_input` FOREIGN KEY (`input_id`) REFERENCES `input` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_detail_product_gtin` FOREIGN KEY (`gtin_id`) REFERENCES `product_gtin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`output` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL,
  `provider_id` int(11),
  `delivery_location_id` int(11),
  `agreement_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `cancelled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_output_concept_idx` (`concept_id`),
  KEY `fk_output_provider_idx` (`provider_id`),
  KEY `fk_output_agreement_idx` (`agreement_id`),
  CONSTRAINT `fk_output_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_output_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_output_agreement` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_output_delivery_location` FOREIGN KEY (`delivery_location_id`) REFERENCES `delivery_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION  
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`output_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `output_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `gtin_id` int(11),
  `serial_number` varchar(30) DEFAULT NULL,
  `batch` varchar(30) NOT NULL,
  `expiration_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_output_detail_output_idx` (`output_id`),
  KEY `fk_output_detail_product_idx` (`product_id`),
  KEY `fk_output_detail_gtin_idx` (`gtin_id`),
  CONSTRAINT `fk_output_detail_output` FOREIGN KEY (`output_id`) REFERENCES `output` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_output_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_output_detail_product_gtin` FOREIGN KEY (`gtin_id`) REFERENCES `product_gtin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`supplying` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `affiliate_id` int(11) NOT NULL,
  `agreement_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `cancelled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplying_client_idx` (`client_id`),
  KEY `fk_supplying_affiliate_idx` (`affiliate_id`),
  KEY `fk_supplying_agreement_idx` (`agreement_id`),
  CONSTRAINT `fk_supplying_client_idx` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_supplying_affiliate_idx` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_supplying_agreement_idx` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`supplying_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `supplying_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `gtin_id` int(11),
  `serial_number` varchar(30) DEFAULT NULL,
  `batch` varchar(30) NOT NULL,
  `expiration_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  `in_stock` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_supplying_detail_supplying_idx` (`supplying_id`),
  KEY `fk_supplying_detail_product_idx` (`product_id`),
  KEY `fk_supplying_detail_gtin_idx` (`gtin_id`),
  CONSTRAINT `fk_supplying_detail_supplying_idx` FOREIGN KEY (`supplying_id`) REFERENCES `supplying` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_supplying_detail_product_idx` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_supplying_detail_gtin_idx` FOREIGN KEY (`gtin_id`) REFERENCES `product_gtin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`provisioning_request_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`provisioning_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agreement_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `delivery_location_id` int(11) NOT NULL,
  `logistics_operator_id` int(11) DEFAULT NULL,
  `affiliate_id` int(11) NOT NULL,
  `delivery_date` date NOT NULL,
  `comment` varchar(100) DEFAULT NULL,
  `state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_provisioning_request_agreement_idx` (`agreement_id`),
  KEY `fk_provisioning_request_client_idx` (`client_id`),
  KEY `fk_provisioning_request_delivery_location_idx` (`delivery_location_id`),
  KEY `fk_provisioning_request_logistics_operator_idx` (`logistics_operator_id`),
  KEY `fk_provisioning_request_affiliate_idx` (`affiliate_id`),
  KEY `fk_provisioning_request_provisioning_request_state_idx` (`state_id`),
  CONSTRAINT `fk_provisioning_request_provisioning_request_state` FOREIGN KEY (`state_id`) REFERENCES `provisioning_request_state` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provisioning_request_affiliate` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provisioning_request_agreement` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provisioning_request_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provisioning_request_delivery_location` FOREIGN KEY (`delivery_location_id`) REFERENCES `delivery_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provisioning_request_logistics_operator` FOREIGN KEY (`logistics_operator_id`) REFERENCES `logistics_operator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`provisioning_request_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provisioning_request_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_provisioning_request_detail_provisioning_request_idx` (`provisioning_request_id`),
  KEY `fk_provisioning_request_detail_product_idx` (`product_id`),
  CONSTRAINT `fk_provisioning_request_detail_provisioning_request` FOREIGN KEY (`provisioning_request_id`) REFERENCES `provisioning_request` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provisioning_request_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provisioning_request_id` int(11) NOT NULL,
  `cancelled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_provisioning_request` FOREIGN KEY (`provisioning_request_id`) REFERENCES `provisioning_request` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `gtin_id` int(11),
  `serial_number` varchar(30) DEFAULT NULL,
  `batch` varchar(30) NOT NULL,
  `expiration_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_detail_order_idx` (`order_id`),
  KEY `fk_order_detail_product_idx` (`product_id`),
  KEY `fk_order_detail_gtin_idx` (`gtin_id`),
  CONSTRAINT `fk_order_detail_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_detail_product_gtin` FOREIGN KEY (`gtin_id`) REFERENCES `product_gtin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`delivery_note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(30) NOT NULL,
  `date` datetime NOT NULL,
  `transaction_code_anmat` varchar(100),
  `cancelled` bit(1) NOT NULL,
  `inform_anmat` bit(1) NOT NULL,
  `informed` bit(1) NOT NULL,
  `fake` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`delivery_note_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `delivery_note_id` int(11)  NOT NULL,
  `order_detail_id` int(11) ,
  `output_detail_id` int(11),
  `supplying_detail_id` int(11),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_delivery_note_detail_delivery_note` FOREIGN KEY (`delivery_note_id`) REFERENCES `delivery_note` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_delivery_note_detail_order_detail` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_delivery_note_detail_output_detail` FOREIGN KEY (`output_detail_id`) REFERENCES `output_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_delivery_note_detail_supplying_detail` FOREIGN KEY (`supplying_detail_id`) REFERENCES `supplying_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`serial_separation_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(5) NOT NULL,
  `separator_token` varchar(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`provider_serialized_format` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gtin_length` int(11) NOT NULL,
  `serial_number_length` int(11),
  `expiration_date_length` int(11),
  `batch_length` int(11),
  `sequence` varchar(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_role_user_idx` (`user_id`),
  KEY `fk_user_role_role_idx` (`role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `tax_id` varchar(15) NOT NULL,
  `corporate_name` varchar(45) NOT NULL,
  `province_id` int(11) NOT NULL,
  `locality` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `zip_code` varchar(10) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `mail` varchar(45) DEFAULT NULL,
  `gln` varchar(45) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `days_ago_pending_transactions` int(11) NOT NULL DEFAULT 7,
  `last_tag` int(7) NOT NULL,
  `self_serialized_tag_filepath` varchar(255) NOT NULL,
  `ANMAT_name` varchar(45) NOT NULL DEFAULT 'pruebasws',
  `ANMAT_password` varchar(100) NOT NULL,
  `start_trace_concept_id` int(11),
  `supplying_concept_id` int(11),
  `proxy` varchar(45),
  `proxy_port` varchar(45),
  `inform_proxy` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_institute_property_province_idx` (`province_id`),
  KEY `fk_institute_property_agent_idx` (`agent_id`),
  CONSTRAINT `fk_institute_property_agent` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_institute_property_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_institute_property_start_trace_concept` FOREIGN KEY (`start_trace_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_institute_property_supplying_concept` FOREIGN KEY (`supplying_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`audit_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `institute`.`audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11)  NOT NULL,
  `operation_id` int(11)  NOT NULL,
  `action_id` int(11)  NOT NULL,
  `date` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_audit_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_audit_action` FOREIGN KEY (`action_id`) REFERENCES `audit_action` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`agreement_transfer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `origin_agreement_id` int(11) NOT NULL,
  `destination_agreement_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agreement_transfer_agreement_1_idx` (`origin_agreement_id`),
  KEY `fk_agreement_transfer_agreement_2_idx` (`destination_agreement_id`),
  CONSTRAINT `fk_agreement_transfer_agreement_1` FOREIGN KEY (`origin_agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agreement_transfer_agreement_2` FOREIGN KEY (`destination_agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`agreement_transfer_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agreement_transfer_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `gtin_id` int(11),
  `serial_number` varchar(30) DEFAULT NULL,
  `batch` varchar(30) NOT NULL,
  `expiration_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agreement_transfer_detail_agreement_transfer_idx` (`agreement_transfer_id`),
  KEY `fk_agreement_transfer_detail_product_idx` (`product_id`),
  KEY `fk_agreement_transfer_detail_gtin_idx` (`gtin_id`),
  CONSTRAINT `fk_agreement_transfer_detail_agreement_transfer` FOREIGN KEY (`agreement_transfer_id`) REFERENCES `agreement_transfer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agreement_transfer_detail_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agreement_transfer_detail_product_gtin` FOREIGN KEY (`gtin_id`) REFERENCES `product_gtin` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institute`.`delivery_note_enumerator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryNotePOS` int(11) NOT NULL,
  `lastDeliveryNoteNumber`  int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `fake` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

COMMIT;
