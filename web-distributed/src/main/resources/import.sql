CREATE TABLE `city` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(64) NULL DEFAULT NULL,
	`state` VARCHAR(64) NULL DEFAULT NULL,
	`country` VARCHAR(64) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
ENGINE=InnoDB
;
INSERT INTO `city` (`id`, `name`, `state`, `country`) VALUES (1, 'San Francisco', 'CA', 'US');
