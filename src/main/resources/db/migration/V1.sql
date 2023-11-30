CREATE DATABASE IF NOT EXISTS `app`;
USE `app`;

DROP TABLE IF EXISTS `task_params`;
CREATE TABLE `task_params` (
  `job_id` varchar(32) NOT NULL,
  `key_name` mediumtext DEFAULT NULL,
  `val` mediumtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;