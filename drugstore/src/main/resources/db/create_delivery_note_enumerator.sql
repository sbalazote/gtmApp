CREATE TABLE `institute`.`delivery_note_enumerator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deliveryNotePOS` int(11) NOT NULL,
  `lastDeliveryNoteNumber`  int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `fake` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;