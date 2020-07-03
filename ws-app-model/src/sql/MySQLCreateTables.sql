-- ----------------------------------------------------------------------------
-- Bikes Model
-------------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE Book;
DROP TABLE Bike;

-- --------------------------------- Bike ------------------------------------
CREATE TABLE Bike ( bikeId BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(512) COLLATE latin1_bin NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    startDate DATETIME NOT NULL,
    price FLOAT NOT NULL,
    units SMALLINT(4) NOT NULL,
    creationDate DATETIME NOT NULL,
    numberOfRates MEDIUMINT(9),
    avgRate INT(11),
    CONSTRAINT BikePK PRIMARY KEY(bikeId), 
    CONSTRAINT validPrice CHECK ( price >= 0 AND price <= 1000) ) ENGINE = InnoDB;

-- --------------------------------- Book ------------------------------------

CREATE TABLE Book ( bookId BIGINT NOT NULL AUTO_INCREMENT,
    bikeId BIGINT NOT NULL,
    email VARCHAR(40) COLLATE latin1_bin NOT NULL,
    creditCard VARCHAR(16) NOT NULL,
    initDate DATETIME NOT NULL,
    endDate DATETIME NOT NULL,
    numberBikes SMALLINT(6) NOT NULL,
    bookDate DATETIME NOT NULL,
    bookRate TINYINT(2),
    CONSTRAINT BookPK PRIMARY KEY(bookId),
    CONSTRAINT BookBikeIdFK FOREIGN KEY(bikeId)
        REFERENCES Bike(bikeId) ON DELETE CASCADE ) ENGINE = InnoDB;
