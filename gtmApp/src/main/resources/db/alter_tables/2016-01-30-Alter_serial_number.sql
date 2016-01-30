ALTER TABLE input_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE stock MODIFY serial_number VARBINARY(30);
ALTER TABLE output_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE supplying_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE order_detail MODIFY serial_number VARBINARY(30);
ALTER TABLE agreement_transfer_detail MODIFY serial_number VARBINARY(30);