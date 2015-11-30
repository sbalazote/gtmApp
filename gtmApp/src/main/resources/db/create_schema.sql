START TRANSACTION;

/*Nombre de la base de datos */
DROP SCHEMA IF EXISTS `drugstore`;

/*Nombre de la base de datos */
CREATE DATABASE drugstore;

/*Nombre de la base de datos */
GRANT ALL PRIVILEGES ON drugstore.* TO `gtm`@`localhost` IDENTIFIED BY 'Gtm4pPlsNt';

/*Nombre de la base de datos */
USE drugstore;

CREATE TABLE `agent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `event` (
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

CREATE TABLE `product_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(60) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_drug_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_brand` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_monodrug` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `provider_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `delivery_note_enumerator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryNotePOS` int(11) NOT NULL,
  `lastDeliveryNoteNumber`  int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `fake` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `concept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `input` bit(1) NOT NULL,
  `print_delivery_note` bit(1) NOT NULL,
  `delivery_note_copies` int(11) NOT NULL DEFAULT '0',
  `refund` bit(1) NOT NULL,
  `inform_anmat` bit(1) NOT NULL,
  `active` bit(1) NOT NULL,
  `client` bit(1) NOT NULL,
  `delivery_note_enumerator_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_concept_delivery_note_enumerator` (`delivery_note_enumerator_id`),
  CONSTRAINT `fk_concept_delivery_note_enumerator` FOREIGN KEY (`delivery_note_enumerator_id`) REFERENCES `delivery_note_enumerator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `agreement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `number_of_delivery_note_details_per_page` int(11) NOT NULL DEFAULT 10,
  `order_label_printer` varchar(255) NOT NULL,
  `picking_list` BIT DEFAULT 1 NOT NULL,
  `delivery_note_printer` varchar(255) NOT NULL,
  `delivery_note_concept_id` int(11) NOT NULL,
  `destruction_concept_id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  CONSTRAINT `fk_agreement_delivery_note_concept` FOREIGN KEY (`delivery_note_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agreement_destruction_concept` FOREIGN KEY (`destruction_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `concept_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_concept_event_concept_idx` (`concept_id`),
  KEY `fk_concept_event_event_idx` (`event_id`),
  CONSTRAINT `fk_concept_event_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_concept_event_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product` (
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

CREATE TABLE `product_gtin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(20) DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number_UNIQUE` (`number`),
  KEY `fk_product_gtin_product_idx` (`product_id`),
  CONSTRAINT `fk_product_gtin_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` decimal(13,4) NOT NULL,
  `product_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_product_price_product_idx` (`product_id`),
  CONSTRAINT `fk_product_price_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `province` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `VAT_liability` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acronym` varchar(5) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `provider` (
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

CREATE TABLE `client` (
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

CREATE TABLE `delivery_location` (
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

CREATE TABLE `client_delivery_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `delivery_location_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_delivery_location_client_idx` (`client_id`),
  KEY `fk_client_delivery_location_delivery_location_idx` (`delivery_location_id`),
  CONSTRAINT `fk_client_delivery_location_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_delivery_location_delivery_location` FOREIGN KEY (`delivery_location_id`) REFERENCES `delivery_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `affiliate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `document_type` varchar(10) DEFAULT NULL,
  `document` varchar(15) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `logistics_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `tax_id` varchar(15) NOT NULL,
  `corporate_name` varchar(45) NOT NULL,
  `gln` varchar(45) NOT NULL,
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

CREATE TABLE `stock` (
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

CREATE TABLE `input` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `concept_id` int(11) NOT NULL,
  `provider_id` int(11),
  `logistics_operator_id` INT(11) DEFAULT NULL,
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
  KEY `fk_input_logistics_operator_idx` (`logistics_operator_id`),
  CONSTRAINT `fk_input_concept` FOREIGN KEY (`concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_agreement` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_logistics_operator` FOREIGN KEY (`logistics_operator_id`) REFERENCES `logistics_operator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_input_delivery_location` FOREIGN KEY (`delivery_location_id`) REFERENCES `delivery_location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `input_detail` (
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

CREATE TABLE `output` (
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

CREATE TABLE `output_detail` (
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

CREATE TABLE `supplying` (
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

CREATE TABLE `supplying_detail` (
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

CREATE TABLE `provisioning_request_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `provisioning_request` (
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

CREATE TABLE `provisioning_request_detail` (
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

CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provisioning_request_id` int(11) NOT NULL,
  `cancelled` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_order_provisioning_request` FOREIGN KEY (`provisioning_request_id`) REFERENCES `provisioning_request` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `order_detail` (
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

CREATE TABLE `delivery_note` (
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

CREATE TABLE `delivery_note_detail` (
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

CREATE TABLE `serial_separation_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(5) NOT NULL,
  `separator_token` varchar(5) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `provider_serialized_format` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gtin_length` int(11) NOT NULL,
  `serial_number_length` int(11),
  `expiration_date_length` int(11),
  `batch_length` int(11),
  `sequence` varchar(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `profile` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `password` varchar(100) NOT NULL,
  `active` bit(1) NOT NULL,
  `profile_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_user_profile_idx` (`profile_id`),
  CONSTRAINT `fk_user_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `description` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `profile_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profile_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_profile_role_profile_idx` (`profile_id`),
  KEY `fk_user_role_role_idx` (`role_id`),
  CONSTRAINT `fk_profile_role_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_profile_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `property` (
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
  `provisioning_require_authorization` bit(1) NOT NULL,
  `print_picking_list` bit(1) NOT NULL,
  `inform_anmat` bit(1) DEFAULT 1 NOT NULL,
  `VAT_liability_id` int(11) DEFAULT 1 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_drugstore_property_province_idx` (`province_id`),
  KEY `fk_drugstore_property_agent_idx` (`agent_id`),
  KEY `fk_property_VAT_liability_idx` (`VAT_liability_id`),
  CONSTRAINT `fk_drugstore_property_agent` FOREIGN KEY (`agent_id`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_drugstore_property_province` FOREIGN KEY (`province_id`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_drugstore_property_start_trace_concept` FOREIGN KEY (`start_trace_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_drugstore_property_supplying_concept` FOREIGN KEY (`supplying_concept_id`) REFERENCES `concept` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_property_VAT_liability` FOREIGN KEY (`VAT_liability_id`) REFERENCES `VAT_liability` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `audit_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `audit` (
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

CREATE TABLE `agreement_transfer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `origin_agreement_id` int(11) NOT NULL,
  `destination_agreement_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_agreement_transfer_agreement_1_idx` (`origin_agreement_id`),
  KEY `fk_agreement_transfer_agreement_2_idx` (`destination_agreement_id`),
  CONSTRAINT `fk_agreement_transfer_agreement_1` FOREIGN KEY (`origin_agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_agreement_transfer_agreement_2` FOREIGN KEY (`destination_agreement_id`) REFERENCES `agreement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `agreement_transfer_detail` (
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

CREATE TABLE `client_affiliate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `affiliate_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_affiliate_client_idx` (`client_id`),
  KEY `fk_client_affiliate_affiliate_idx` (`affiliate_id`),
  CONSTRAINT `fk_client_affiliate_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_affiliate_affiliate` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `provider_logistics_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_id` int(11) NOT NULL,
  `logistics_operator_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_provider_logistics_operator_provider_idx` (`provider_id`),
  KEY `fk_provider_logistics_operator_logistics_operator_idx` (`logistics_operator_id`),
  CONSTRAINT `fk_provider_logistics_operator_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provider_logistics_operator_logistics_operator` FOREIGN KEY (`logistics_operator_id`) REFERENCES `logistics_operator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `delivery_note_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


delimiter //

CREATE PROCEDURE update_from_alfabeta_proc (description VARCHAR(100), price DECIMAL(13,4), code INT, gtin VARCHAR(20), cold BIT)
COMMENT 'Procedimiento para actualizar desde archivo de alfabeta'
    BEGIN
    DECLARE product_id INT;
    DECLARE product_code INT;
    DECLARE product_description VARCHAR(100);
    IF NOT EXISTS (SELECT 1 FROM product AS p WHERE p.code = code LIMIT 1) THEN
        /*INSERT INTO log_message(msg) VALUES ((SELECT 'No existe el producto'));*/
		INSERT INTO product(code, description, brand_id, monodrug_id, group_id, drug_category_id, cold, inform_anmat, type, active) VALUES (code, description, 1, 1, 1, 1, cold, false, "BE", false);
        SET @last_id_in_product = LAST_INSERT_ID();
        INSERT INTO product_gtin(number, product_id, date) SELECT gtin, @last_id_in_product, NOW() FROM dual WHERE NOT EXISTS (SELECT number FROM product_gtin WHERE number = gtin) LIMIT 1;
        INSERT INTO product_price(price, product_id, date) VALUES (price, @last_id_in_product, NOW());
	ELSE
		SELECT p.id, p.code, p.description INTO product_id, product_code, product_description FROM product AS p WHERE p.code = code LIMIT 1;
		IF NOT EXISTS (SELECT 1 FROM product AS p, product_gtin AS pg WHERE p.id = pg.product_id and pg.number = gtin) THEN
			/*INSERT INTO log_message(msg) SELECT CONCAT('Existe el producto pero no con el gtin con ','id: ', product_id, ' code: ', product_code, ' descr: ', product_description, ' gtin: ', IFNULL(gtin, ''));*/
			INSERT INTO product_gtin(number, product_id, date) SELECT gtin, product_id, NOW() FROM dual WHERE NOT EXISTS (SELECT number FROM product_gtin WHERE number = gtin) LIMIT 1;
			INSERT INTO product_price(price, product_id, date) VALUES (price, product_id, NOW());
        ELSE
			/*INSERT INTO log_message(msg) SELECT CONCAT('Existe el producto y el gtin con ', 'id: ', product_id, ' code: ', product_code, ' descr: ', product_description, ' gtin: ', IFNULL(gtin, ''));*/
			INSERT INTO product_price(price, product_id, date) VALUES (price, product_id, NOW());
        END IF;
    END IF;

    END//

delimiter ;


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
INSERT INTO `delivery_note_config` VALUES (82, 'NUMBEROFITEMS_Y', 270);
INSERT INTO `delivery_note_config` VALUES (83, 'NUMBEROFITEMS_PRINT', 1);


COMMIT;
