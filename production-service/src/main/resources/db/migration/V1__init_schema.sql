-- =====================================================
-- DATABASE
-- =====================================================
CREATE DATABASE IF NOT EXISTS production_service_db;
USE production_service_db;

-- =====================================================
-- PLANT_BATCH
-- =====================================================
CREATE TABLE plant_batch (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             plant_type VARCHAR(50) NOT NULL,
                             plants_name VARCHAR(255) NOT NULL,
                             location VARCHAR(255) NOT NULL,
                             planted_at DATE NOT NULL,
                             total_count INT NOT NULL,

                             PRIMARY KEY (id)
) ENGINE=InnoDB;

-- =====================================================
-- PLANT
-- =====================================================
CREATE TABLE plant (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       batch_id BIGINT NOT NULL,
                       plant_type VARCHAR(50) NOT NULL,
                       name VARCHAR(255) NOT NULL,

    -- discriminator (JPA inheritance)
                       dtype VARCHAR(31) NOT NULL,

    -- GrowthStage (Embedded)
                       age INT,
                       height DOUBLE,

    -- HealthStatus
                       `condition` VARCHAR(20),

    -- ===== Conifer fields =====
                       plant_description VARCHAR(500),
                       propagation_type VARCHAR(100),
                       height_at_ten_years DOUBLE,
                       needle_color VARCHAR(100),
                       crown_shape VARCHAR(100),
                       annual_growth_rate DOUBLE,
                       crown_width_at_maturity DOUBLE,
                       soil_requirements VARCHAR(255),
                       frost_resistance VARCHAR(100),
                       light_requirements VARCHAR(100),

    -- ===== FruitTree fields =====
                       fruit_weight DOUBLE,
                       height_at_maturity DOUBLE,
                       ripening_period VARCHAR(100),
                       variety_advantages VARCHAR(500),
                       fruit_taste VARCHAR(100),
                       harvest_time VARCHAR(100),
                       storage_duration_days INT,
                       recommended_pollinators VARCHAR(255),

                       PRIMARY KEY (id),

                       CONSTRAINT fk_plant_batch
                           FOREIGN KEY (batch_id)
                               REFERENCES plant_batch(id)
                               ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- OUTBOX EVENT (Outbox Pattern)
-- =====================================================
CREATE TABLE outbox_event (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,

                              event_id VARCHAR(36) NOT NULL,

                              aggregate_type VARCHAR(255) NOT NULL,
                              aggregate_id VARCHAR(255) NOT NULL,
                              event_type VARCHAR(255) NOT NULL,

                              payload LONGTEXT NOT NULL,

                              status VARCHAR(20) NOT NULL,

                              created_at DATETIME(6) NOT NULL,
                              sent_at DATETIME(6) NULL,

                              CONSTRAINT uq_outbox_event_event_id UNIQUE (event_id)
);

CREATE TABLE plant_diseases (
                                plant_id BIGINT NOT NULL,
                                name VARCHAR(255) NOT NULL,

                                CONSTRAINT fk_plant_diseases_plant
                                    FOREIGN KEY (plant_id)
                                        REFERENCES plant(id)
                                        ON DELETE CASCADE,

                                CONSTRAINT uk_plant_disease UNIQUE (plant_id, name)
) ENGINE=InnoDB;

CREATE TABLE plant_treatments (
                                  plant_id BIGINT NOT NULL,
                                  description VARCHAR(255) NOT NULL,

                                  CONSTRAINT fk_plant_treatments_plant
                                      FOREIGN KEY (plant_id)
                                          REFERENCES plant(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT uk_plant_treatment UNIQUE (plant_id, description)
) ENGINE=InnoDB;