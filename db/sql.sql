DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


DROP TABLE IF EXISTS `task_params`;
CREATE TABLE `task_params` (
`job_id` VARCHAR(32) NOT NULL,
    `step_execution_id` VARCHAR(32) NOT NULL,
    `key_name` varchar(6) NOT NULL,
    `val` varchar(3000) NOT NULL,
    INDEX `job_id` (`job_id`),
    INDEX `step_execution_id` (`step_execution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;