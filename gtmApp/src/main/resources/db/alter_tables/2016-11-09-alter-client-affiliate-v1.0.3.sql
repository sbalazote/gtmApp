ALTER TABLE institute.affiliate ADD sex VARCHAR(1) NULL;
ALTER TABLE institute.affiliate ADD address VARCHAR(200) NULL;
ALTER TABLE institute.affiliate ADD locality VARCHAR(250) NULL;
ALTER TABLE institute.affiliate ADD number VARCHAR(10) NULL;
ALTER TABLE institute.affiliate ADD floor VARCHAR(5) NULL;
ALTER TABLE institute.affiliate ADD apartment VARCHAR(5) NULL;
ALTER TABLE institute.affiliate ADD zip_code VARCHAR(8) NULL;
ALTER TABLE institute.affiliate ADD phone VARCHAR(30) NULL;

ALTER TABLE institute.client_affiliate ADD associate_number VARCHAR(30) NULL;

ALTER TABLE institute.client_affiliate DROP FOREIGN KEY `fk_client_affiliate_affiliate`;
ALTER TABLE institute.provisioning_request DROP FOREIGN KEY `fk_provisioning_request_affiliate`;
ALTER TABLE institute.supplying DROP FOREIGN KEY `fk_supplying_affiliate_idx`;

ALTER TABLE institute.client_affiliate DROP FOREIGN KEY `fk_client_affiliate_client`;
ALTER TABLE institute.client_delivery_location DROP FOREIGN KEY `fk_client_delivery_location_client`;
ALTER TABLE institute.provisioning_request DROP FOREIGN KEY `fk_provisioning_request_client`;
ALTER TABLE institute.supplying DROP FOREIGN KEY `fk_supplying_client_idx`;

ALTER TABLE institute.affiliate CHANGE id affiliate_id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE institute.client CHANGE id client_id INT(11) NOT NULL AUTO_INCREMENT;

CREATE INDEX affiliate_id_idx ON institute.affiliate (affiliate_id);
CREATE INDEX client_id_idx ON institute.client (client_id);

ALTER TABLE institute.client_affiliate ADD CONSTRAINT `fk_client_affiliate_affiliate` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate`(affiliate_id) ON DELETE NO ACTION ON UPDATE NO ACTION ;
ALTER TABLE institute.provisioning_request ADD CONSTRAINT `fk_provisioning_request_affiliate` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate`(affiliate_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE institute.supplying ADD CONSTRAINT `fk_supplying_affiliate_idx` FOREIGN KEY (`affiliate_id`) REFERENCES `affiliate`(affiliate_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE institute.client_affiliate ADD CONSTRAINT `fk_client_affiliate_client` FOREIGN KEY (`client_id`) REFERENCES `client`(client_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE institute.client_delivery_location ADD CONSTRAINT `fk_client_delivery_location_client` FOREIGN KEY (`client_id`) REFERENCES `client`(client_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE institute.provisioning_request ADD CONSTRAINT `fk_provisioning_request_client` FOREIGN KEY (`client_id`) REFERENCES `client`(client_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE institute.supplying ADD CONSTRAINT `fk_supplying_client_idx` FOREIGN KEY (`client_id`) REFERENCES `client`(client_id) ON DELETE NO ACTION ON UPDATE NO ACTION;