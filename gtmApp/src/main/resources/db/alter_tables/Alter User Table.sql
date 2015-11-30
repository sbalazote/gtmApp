CREATE TABLE `drugstore`.`profile` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
DROP TABLE `drugstore`.`user_role`;

CREATE TABLE `drugstore`.`profile_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profile_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_profile_role_profile_idx` (`profile_id`),
  KEY `fk_user_role_role_idx` (`role_id`),
  CONSTRAINT `fk_profile_role_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_profile_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  ALTER TABLE `drugstore`.`user`
  ADD COLUMN `profile_id` int(11),
  ADD KEY `fk_user_profile_idx` (`profile_id`),
  ADD CONSTRAINT `fk_user_profile` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION