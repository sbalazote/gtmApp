ALTER TABLE agreement DROP picking_filepath;
ALTER TABLE agreement CHANGE order_label_filepath order_label_printer VARCHAR(255);
ALTER TABLE agreement CHANGE delivery_note_filepath delivery_note_printer VARCHAR(255);
ALTER TABLE property ADD inform_anmat BIT DEFAULT 1 NOT NULL;
ALTER TABLE property ADD VAT_liability_id INT(11) DEFAULT 1 NOT NULL;
ALTER TABLE property ADD KEY `fk_property_VAT_liability_idx` (`VAT_liability_id`);
ALTER TABLE property ADD CONSTRAINT `fk_property_VAT_liability` FOREIGN KEY (`VAT_liability_id`) REFERENCES `VAT_liability` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;