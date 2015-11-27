delimiter //

CREATE PROCEDURE update_from_alfabeta_proc (description VARCHAR(100), price DECIMAL(13,4), code INT, gtin VARCHAR(20), cold BIT)
COMMENT 'Procedimiento para actualizar desde archivo de alfabeta'
    BEGIN
    DECLARE product_id INT;
    DECLARE product_code INT;
    DECLARE product_description VARCHAR(100);
    IF NOT EXISTS (SELECT 1 FROM product AS p WHERE p.code = code LIMIT 1) THEN
        /*INSERT INTO log_message(msg) VALUES ((SELECT 'No existe el producto'));*/
		INSERT INTO product(code, description, brand_id, monodrug_id, group_id, drug_category_id, cold, inform_anmat, type, active) VALUES (code, description, 1, 1, 1, 1, cold, false, "BE", false);
        SET @last_id_in_product = LAST_INSERT_ID();
        INSERT INTO product_gtin(number, product_id, date) SELECT gtin, @last_id_in_product, NOW() FROM dual WHERE NOT EXISTS (SELECT number FROM product_gtin WHERE number = gtin) LIMIT 1;
        INSERT INTO product_price(price, product_id, date) VALUES (price, @last_id_in_product, NOW());
	ELSE
		SELECT p.id, p.code, p.description INTO product_id, product_code, product_description FROM product AS p WHERE p.code = code LIMIT 1;
		IF NOT EXISTS (SELECT 1 FROM product AS p, product_gtin AS pg WHERE p.id = pg.product_id and pg.number = gtin) THEN
			/*INSERT INTO log_message(msg) SELECT CONCAT('Existe el producto pero no con el gtin con ','id: ', product_id, ' code: ', product_code, ' descr: ', product_description, ' gtin: ', IFNULL(gtin, ''));*/
			INSERT INTO product_gtin(number, product_id, date) SELECT gtin, product_id, NOW() FROM dual WHERE NOT EXISTS (SELECT number FROM product_gtin WHERE number = gtin) LIMIT 1;
			INSERT INTO product_price(price, product_id, date) VALUES (price, product_id, NOW());
        ELSE
			/*INSERT INTO log_message(msg) SELECT CONCAT('Existe el producto y el gtin con ', 'id: ', product_id, ' code: ', product_code, ' descr: ', product_description, ' gtin: ', IFNULL(gtin, ''));*/
			INSERT INTO product_price(price, product_id, date) VALUES (price, product_id, NOW());
        END IF;
    END IF;

    END//

delimiter ;