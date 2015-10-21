CREATE TABLE `institute`.`provider_logistics_operator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `provider_id` int(11) NOT NULL,
  `logistics_operator_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_provider_logistics_operator_provider_idx` (`provider_id`),
  KEY `fk_provider_logistics_operator_logistics_operator_idx` (`logistics_operator_id`),
  CONSTRAINT `fk_provider_logistics_operator_provider` FOREIGN KEY (`provider_id`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_provider_logistics_operator_logistics_operator` FOREIGN KEY (`logistics_operator_id`) REFERENCES `logistics_operator` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE institute.input ADD logistics_operator_id INT(11) DEFAULT NULL  NULL;
ALTER TABLE institute.input
ADD KEY `fk_input_logistics_operator_idx` (`logistics_operator_id`);

ALTER TABLE institute.logistics_operator ADD gln VARCHAR(45) NOT NULL;