ALTER TABLE `institute`.`property` 
DROP FOREIGN KEY `fk_institute_property_start_trace_concept`,
DROP FOREIGN KEY `fk_institute_property_supplying_concept`;
ALTER TABLE `institute`.`property` 
CHANGE COLUMN `start_trace_concept_id` `start_trace_concept_id` INT(11) NULL ,
CHANGE COLUMN `supplying_concept_id` `supplying_concept_id` INT(11) NULL ;
ALTER TABLE `institute`.`property` 
ADD CONSTRAINT `fk_institute_property_start_trace_concept`
  FOREIGN KEY (`start_trace_concept_id`)
  REFERENCES `institute`.`concept` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_institute_property_supplying_concept`
  FOREIGN KEY (`supplying_concept_id`)
  REFERENCES `institute`.`concept` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
