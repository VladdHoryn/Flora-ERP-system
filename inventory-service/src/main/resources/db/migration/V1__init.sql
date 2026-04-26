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

-- =====================================================
-- PLANT_AVAILABILITY
-- =====================================================
CREATE TABLE plant_availability (
                                    plant_id BIGINT NOT NULL,
                                    reservation_id BIGINT NULL,
                                    status VARCHAR(20) NOT NULL,

                                    PRIMARY KEY (plant_id)
) ENGINE=InnoDB;


-- =====================================================
-- RESERVATION
-- =====================================================
CREATE TABLE reservation (
                             id BIGINT NOT NULL AUTO_INCREMENT,

                             status VARCHAR(20) NOT NULL,
                             created_at DATETIME(6) NOT NULL,
                             expires_at DATETIME(6) NOT NULL,

                             PRIMARY KEY (id)
) ENGINE=InnoDB;


-- =====================================================
-- PLANT_INVENTORY
-- =====================================================
CREATE TABLE plant_inventory (
                                 id BIGINT NOT NULL AUTO_INCREMENT,

                                 plant_type VARCHAR(50) NOT NULL,
                                 plants_name VARCHAR(255) NOT NULL,
                                 age BIGINT,

                                 total_quantity BIGINT NOT NULL,
                                 reserved_quantity BIGINT NOT NULL,
                                 available_quantity BIGINT NOT NULL,

                                 PRIMARY KEY (id)
) ENGINE=InnoDB;