CREATE TABLE `institute`.`client_affiliate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `affiliate_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_client_affiliate_client_idx` (`client_id`),
  KEY `fk_client_affiliate_affiliate_idx` (`affiliate_id`),
  CONSTRAINT `fk_client_affiliate_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_affiliate_affiliate` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE institute.affiliate DROP FOREIGN KEY fk_affiliate_client;
DROP INDEX fk_affiliate_client_idx ON institute.affiliate;
ALTER TABLE institute.affiliate DROP client_id;