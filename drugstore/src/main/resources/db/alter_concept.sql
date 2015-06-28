ALTER TABLE `institute`.`concept` 
DROP COLUMN `last_delivery_note_number`,
DROP COLUMN `delivery_note_POS`,
ADD COLUMN `delivery_note_enumerator_id` INT(11) AFTER `client`,
ADD INDEX `fk_concept_delivery_note_enumerator_idx` (`delivery_note_enumerator_id` ASC);
ALTER TABLE `institute`.`concept` 
ADD CONSTRAINT `fk_concept_delivery_note_enumerator`
  FOREIGN KEY (`delivery_note_enumerator_id`)
  REFERENCES `institute`.`delivery_note_enumerator` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  