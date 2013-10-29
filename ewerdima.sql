SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `ewerdima` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `ewerdima` ;

-- -----------------------------------------------------
-- Table `ewerdima`.`users`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ewerdima`.`users` (
  `idusers` INT NOT NULL AUTO_INCREMENT ,
  `Firstname` VARCHAR(45) NULL ,
  `Lastname` VARCHAR(45) NULL ,
  `UserContact` VARCHAR(20) NULL ,
  `email` VARCHAR(45) NULL ,
  `password` VARCHAR(45) NULL ,
  `DoB` DATE NULL ,
  `AccessLevel` ENUM('Normal User','Administrator','Households') NULL ,
  `unique_id` VARCHAR(45) NULL ,
  `encrypted_password` VARCHAR(45) NULL ,
  `salt` VARCHAR(45) NULL ,
  `created_at` DATETIME NULL ,
  `userscol` VARCHAR(45) NULL ,
  PRIMARY KEY (`idusers`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewerdima`.`UserLoc`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ewerdima`.`UserLoc` (
  `idLocateUsers` INT NOT NULL AUTO_INCREMENT ,
  `UserLat` FLOAT(8) NULL ,
  `UserLong` FLOAT(8) NULL ,
  `UserNeighborhood` VARCHAR(255) NULL ,
  `UserTownCity` VARCHAR(255) NULL ,
  `UserCountyState` VARCHAR(255) NULL ,
  `UserCountry` VARCHAR(45) NULL ,
  `UserAdress` VARCHAR(255) NULL ,
  `Accuracy` FLOAT(6) NULL ,
  `UserAltitude` FLOAT(6) NULL ,
  `UserDirection` VARCHAR(45) NULL ,
  `UserSpeed` VARCHAR(45) NULL ,
  PRIMARY KEY (`idLocateUsers`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewerdima`.`HardwaresInstalled`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ewerdima`.`HardwaresInstalled` (
  `idHardware` INT NOT NULL AUTO_INCREMENT ,
  `HardwareLat` FLOAT(8) NULL ,
  `HardwareLong` FLOAT(8) NULL ,
  `HardwareNeighborhood` VARCHAR(255) NULL ,
  `HardwareTownCity` VARCHAR(255) NULL ,
  `HardwareCountyState` VARCHAR(255) NULL ,
  `HardwareCountry` VARCHAR(45) NULL ,
  `HardwareAdress` VARCHAR(255) NULL ,
  `Accuracy` FLOAT(6) NULL ,
  `HardwareAltitude` FLOAT(6) NULL ,
  `HardwareDirection` VARCHAR(45) NULL ,
  `HardwareSpeed` VARCHAR(45) NULL ,
  `HardwaresStatus` VARCHAR(45) NULL ,
  `HardwaresOwner` VARCHAR(45) NULL ,
  `HardwaresOwnerContactNo` VARCHAR(20) NULL ,
  PRIMARY KEY (`idHardware`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ewerdima`.`Disasters`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `ewerdima`.`Disasters` (
  `idDisaster` INT NOT NULL AUTO_INCREMENT ,
  `DisasterLat` FLOAT(8) NULL ,
  `DisasterLong` FLOAT(8) NULL ,
  `DisasterNeighborhood` VARCHAR(255) NULL ,
  `DisasterTownCity` VARCHAR(255) NULL ,
  `DisasterCountyState` VARCHAR(255) NULL ,
  `DisasterCountry` VARCHAR(45) NULL ,
  `DisasterAdress` VARCHAR(255) NULL ,
  `Accuracy` FLOAT(6) NULL ,
  `DisasterAltitude` FLOAT(6) NULL ,
  `DisasterDirection` VARCHAR(45) NULL ,
  `DisasterSpeed` VARCHAR(45) NULL ,
  PRIMARY KEY (`idDisaster`) )
ENGINE = InnoDB;

USE `ewerdima` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
