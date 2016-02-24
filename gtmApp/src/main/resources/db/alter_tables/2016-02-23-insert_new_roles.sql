insert into role values (45, 'FAKE_DELIVERY_NOTE_PRINT', 'Impresión de Remito Propio');
insert into role values (46, 'FORCED_INPUT', 'Ingreso Forzado');
insert into role values (47, 'FORCED_INPUT_UPDATE', 'Modificación de Ingreso Forzado');

update audit a set a.role_id = 47 where a.role_id = 1 and a.action_id = 3;
update audit a set a.role_id = 19 where a.role_id = 10 and a.action_id = 3;
update audit a set a.role_id = 4 where a.role_id = 3 and a.action_id = 2;
update audit a set a.role_id = 20 where a.role_id = 8 and a.action_id = 2;
update audit a set a.role_id = 46 where a.role_id = 15 and a.action_id = 3;

ALTER TABLE audit DROP FOREIGN KEY fk_audit_audit_action;
DROP INDEX fk_audit_audit_action ON audit;
ALTER TABLE audit DROP action_id;

DROP TABLE audit_action;