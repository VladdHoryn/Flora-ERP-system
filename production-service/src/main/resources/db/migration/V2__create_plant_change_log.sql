CREATE TABLE plant_change_log (
                                  id BIGINT NOT NULL AUTO_INCREMENT,

                                  plant_id BIGINT,
                                  batch_id BIGINT,

                                  plant_type VARCHAR(50),
                                  plants_name VARCHAR(255),
                                  age BIGINT,

                                  quantity_change INT NOT NULL,
                                  change_type VARCHAR(20),

                                  created_at DATETIME(6),

                                  PRIMARY KEY (id)
) ENGINE=InnoDB;