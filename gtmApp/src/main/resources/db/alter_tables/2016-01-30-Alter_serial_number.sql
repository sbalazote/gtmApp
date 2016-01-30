ALTER TABLE pharmacy_dev.input_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE pharmacy_dev.stock MODIFY serial_number VARBINARY(30);
ALTER TABLE pharmacy_dev.output_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE pharmacy_dev.supplying_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE pharmacy_dev.order_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE pharmacy_dev.agreement_transfer_detail MODIFY serial_number VARBINARY(30);