ALTER TABLE profile_role DROP FOREIGN KEY fk_profile_role_role;
ALTER TABLE profile_role
ADD CONSTRAINT fk_profile_role_role
FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE audit DROP FOREIGN KEY fk_audit_role;
ALTER TABLE audit
ADD CONSTRAINT fk_audit_role
FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE ON UPDATE CASCADE;
DELETE FROM role WHERE id = 12 LIMIT 1;
DELETE FROM role WHERE id = 17 LIMIT 1;
DELETE FROM role WHERE id = 20 LIMIT 1;
UPDATE role t SET t.`id` = 12 WHERE t.`id` = 13;
UPDATE role t SET t.`id` = 13 WHERE t.`id` = 14;
UPDATE role t SET t.`id` = 14 WHERE t.`id` = 15;
UPDATE role t SET t.`id` = 15 WHERE t.`id` = 16;
INSERT INTO role VALUES (16, 'PRODUCT_DESTRUCTION', 'Destrucción de Mercadería');
UPDATE role t SET t.`id` = 17 WHERE t.`id` = 18;
UPDATE role t SET t.`id` = 18 WHERE t.`id` = 19;
UPDATE role t SET t.`id` = 19 WHERE t.`id` = 21;
UPDATE role t SET t.`id` = 20 WHERE t.`id` = 22;
UPDATE role t SET t.`id` = 21 WHERE t.`id` = 23;
UPDATE role t SET t.`id` = 22 WHERE t.`id` = 24;
UPDATE role t SET t.`id` = 23 WHERE t.`id` = 25;
UPDATE role t SET t.`id` = 24 WHERE t.`id` = 26;
UPDATE role t SET t.`id` = 25 WHERE t.`id` = 27;
UPDATE role t SET t.`id` = 26 WHERE t.`id` = 28;
UPDATE role t SET t.`id` = 27 WHERE t.`id` = 29;
UPDATE role t SET t.`id` = 28 WHERE t.`id` = 30;
UPDATE role t SET t.`id` = 29 WHERE t.`id` = 31;
UPDATE role t SET t.`id` = 30 WHERE t.`id` = 32;
UPDATE role t SET t.`id` = 31 WHERE t.`id` = 33;
UPDATE role t SET t.`id` = 32 WHERE t.`id` = 34;
UPDATE role t SET t.`id` = 33 WHERE t.`id` = 35;
UPDATE role t SET t.`id` = 34 WHERE t.`id` = 36;
UPDATE role t SET t.`id` = 35 WHERE t.`id` = 37;
UPDATE role t SET t.`id` = 36 WHERE t.`id` = 38;
UPDATE role t SET t.`id` = 37 WHERE t.`id` = 39;
UPDATE role t SET t.`id` = 38 WHERE t.`id` = 40;
UPDATE role t SET t.`id` = 39 WHERE t.`id` = 41;
UPDATE role t SET t.`id` = 40 WHERE t.`id` = 42;
UPDATE role t SET t.`id` = 41 WHERE t.`id` = 43;
UPDATE role t SET t.`id` = 42 WHERE t.`id` = 44;
UPDATE role t SET t.`id` = 43 WHERE t.`id` = 45;
UPDATE role t SET t.`id` = 44 WHERE t.`id` = 46;