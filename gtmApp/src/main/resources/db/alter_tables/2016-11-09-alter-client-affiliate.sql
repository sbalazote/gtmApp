ALTER TABLE drugstore_dev.affiliate ADD sex VARCHAR(1) NULL;
ALTER TABLE drugstore_dev.affiliate ADD address VARCHAR(200) NULL;
ALTER TABLE drugstore_dev.affiliate ADD locality VARCHAR(250) NULL;
ALTER TABLE drugstore_dev.affiliate ADD number VARCHAR(10) NULL;
ALTER TABLE drugstore_dev.affiliate ADD floor VARCHAR(5) NULL;
ALTER TABLE drugstore_dev.affiliate ADD apartment VARCHAR(5) NULL;
ALTER TABLE drugstore_dev.affiliate ADD zip_code VARCHAR(8) NULL;
ALTER TABLE drugstore_dev.affiliate ADD phone VARCHAR(30) NULL;

ALTER TABLE drugstore_dev.client_affiliate ADD associate_number VARCHAR(30) NULL;