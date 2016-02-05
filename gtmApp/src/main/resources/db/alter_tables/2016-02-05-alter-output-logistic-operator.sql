ALTER TABLE output ADD logistics_operator_id INT(11) NULL;
ALTER TABLE output
ADD CONSTRAINT fk_output_logistics_operator
FOREIGN KEY (logistics_operator_id) REFERENCES logistics_operator (id);