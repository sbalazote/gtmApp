ALTER TABLE `institute`.`input_detail` 
ADD COLUMN `transaction_code_anmat` VARCHAR(100) NULL DEFAULT NULL AFTER `amount`,
ADD COLUMN `inform_anmat` BIT(1) NOT NULL AFTER `transaction_code_anmat`,
ADD COLUMN `informed` BIT(1) NOT NULL AFTER `inform_anmat`;