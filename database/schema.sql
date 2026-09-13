-- Schema only. No records, accounts, provider keys, or historical data.
-- Create an empty local database before importing this file.

CREATE TABLE `infra_api_access_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `user_id` bigint NOT NULL DEFAULT 0 ,
  `user_type` tinyint NOT NULL DEFAULT 0 ,
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `request_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `response_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL ,
  `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_agent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `operate_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `operate_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `operate_type` tinyint NULL DEFAULT 0 ,
  `begin_time` datetime NOT NULL ,
  `end_time` datetime NOT NULL ,
  `duration` int NOT NULL ,
  `result_code` int NOT NULL DEFAULT 0 ,
  `result_msg` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_api_error_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_id` bigint NOT NULL DEFAULT 0 ,
  `user_type` tinyint NOT NULL DEFAULT 0 ,
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `request_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `request_params` varchar(8000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_agent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_time` datetime NOT NULL ,
  `exception_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `exception_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_root_cause_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_stack_trace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_class_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_file_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_method_name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `exception_line_number` int NOT NULL ,
  `process_status` tinyint NOT NULL ,
  `process_time` datetime NULL DEFAULT NULL ,
  `process_user_id` int NULL DEFAULT 0 ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_codegen_column`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `table_id` bigint NOT NULL ,
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `data_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `nullable` bit(1) NOT NULL ,
  `primary_key` bit(1) NOT NULL ,
  `ordinal_position` int NOT NULL ,
  `java_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `java_field` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `example` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `create_operation` bit(1) NOT NULL ,
  `update_operation` bit(1) NOT NULL ,
  `list_operation` bit(1) NOT NULL ,
  `list_operation_condition` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '=' ,
  `list_operation_result` bit(1) NOT NULL ,
  `html_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_codegen_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `data_source_config_id` bigint NOT NULL ,
  `scene` tinyint NOT NULL DEFAULT 1 ,
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `class_comment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_type` tinyint NOT NULL DEFAULT 1 ,
  `front_type` tinyint NOT NULL ,
  `parent_menu_id` bigint NULL DEFAULT NULL ,
  `master_table_id` bigint NULL DEFAULT NULL ,
  `sub_join_column_id` bigint NULL DEFAULT NULL ,
  `sub_join_many` bit(1) NULL DEFAULT NULL ,
  `tree_parent_column_id` bigint NULL DEFAULT NULL ,
  `tree_name_column_id` bigint NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `type` tinyint NOT NULL ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `visible` bit(1) NOT NULL ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_data_source_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `config_id` bigint NULL DEFAULT NULL ,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `size` int NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_file_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `storage` tinyint NOT NULL ,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `master` bit(1) NOT NULL ,
  `config` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_file_content`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `config_id` bigint NOT NULL ,
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `content` mediumblob NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_job`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `status` tinyint NOT NULL ,
  `handler_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `handler_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `cron_expression` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `retry_count` int NOT NULL DEFAULT 0 ,
  `retry_interval` int NOT NULL DEFAULT 0 ,
  `monitor_timeout` int NOT NULL DEFAULT 0 ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `infra_job_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `job_id` bigint NOT NULL ,
  `handler_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `handler_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `execute_index` tinyint NOT NULL DEFAULT 1 ,
  `begin_time` datetime NOT NULL ,
  `end_time` datetime NULL DEFAULT NULL ,
  `duration` int NULL DEFAULT NULL ,
  `status` tinyint NOT NULL ,
  `result` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `parent_id` bigint NOT NULL DEFAULT 0 ,
  `sort` int NOT NULL DEFAULT 0 ,
  `leader_user_id` bigint NULL DEFAULT NULL ,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `status` tinyint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `sort` int NOT NULL DEFAULT 0 ,
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `color_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_dict_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `deleted_time` datetime NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `log_type` bigint NOT NULL ,
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `user_id` bigint NOT NULL DEFAULT 0 ,
  `user_type` tinyint NOT NULL DEFAULT 0 ,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `result` tinyint NOT NULL ,
  `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_agent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_mail_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `port` int NOT NULL ,
  `ssl_enable` bit(1) NOT NULL DEFAULT b'0' ,
  `starttls_enable` bit(1) NOT NULL DEFAULT b'0' ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_mail_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NULL DEFAULT NULL ,
  `user_type` tinyint NULL DEFAULT NULL ,
  `to_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `account_id` bigint NOT NULL ,
  `from_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_id` bigint NOT NULL ,
  `template_code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `template_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_content` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `send_status` tinyint NOT NULL DEFAULT 0 ,
  `send_time` datetime NULL DEFAULT NULL ,
  `send_message_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `send_exception` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_mail_template`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `account_id` bigint NOT NULL ,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `content` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `status` tinyint NOT NULL ,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `type` tinyint NOT NULL ,
  `sort` int NOT NULL DEFAULT 0 ,
  `parent_id` bigint NOT NULL DEFAULT 0 ,
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '#' ,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `component_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `visible` bit(1) NOT NULL DEFAULT b'1' ,
  `keep_alive` bit(1) NOT NULL DEFAULT b'1' ,
  `always_show` bit(1) NOT NULL DEFAULT b'1' ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `type` tinyint NOT NULL ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_notify_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `template_id` bigint NOT NULL ,
  `template_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_nickname` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_type` int NOT NULL ,
  `template_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `read_status` bit(1) NOT NULL ,
  `read_time` datetime NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_notify_template`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `type` tinyint NOT NULL ,
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `status` tinyint NOT NULL ,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_oauth2_access_token`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `user_info` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `refresh_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `expires_time` datetime NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_access_token`(`access_token` ASC) USING BTREE,
  INDEX `idx_refresh_token`(`refresh_token` ASC) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_oauth2_approve`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `approved` bit(1) NOT NULL DEFAULT b'0' ,
  `expires_time` datetime NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_oauth2_client`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `status` tinyint NOT NULL ,
  `access_token_validity_seconds` int NOT NULL ,
  `refresh_token_validity_seconds` int NOT NULL ,
  `redirect_uris` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `authorized_grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `auto_approve_scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `authorities` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `resource_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_oauth2_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `expires_time` datetime NOT NULL ,
  `redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_oauth2_refresh_token`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `refresh_token` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `scopes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `expires_time` datetime NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_operate_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `trace_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `user_id` bigint NOT NULL ,
  `user_type` tinyint NOT NULL DEFAULT 0 ,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `sub_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `biz_id` bigint NOT NULL ,
  `action` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `success` bit(1) NOT NULL DEFAULT b'1' ,
  `extra` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `request_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `request_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `user_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `user_agent` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `sort` int NOT NULL ,
  `status` tinyint NOT NULL ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `sort` int NOT NULL ,
  `data_scope` tinyint NOT NULL DEFAULT 1 ,
  `data_scope_dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `status` tinyint NOT NULL ,
  `type` tinyint NOT NULL ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `role_id` bigint NOT NULL ,
  `menu_id` bigint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_sms_channel`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `signature` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `status` tinyint NOT NULL ,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `api_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `api_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `callback_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_sms_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `create_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `scene` tinyint NOT NULL ,
  `today_index` tinyint NOT NULL ,
  `used` tinyint NOT NULL ,
  `used_time` datetime NULL DEFAULT NULL ,
  `used_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mobile`(`mobile` ASC) USING BTREE 
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_sms_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `channel_id` bigint NOT NULL ,
  `channel_code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_id` bigint NOT NULL ,
  `template_code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_type` tinyint NOT NULL ,
  `template_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `template_params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `api_template_id` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `user_id` bigint NULL DEFAULT NULL ,
  `user_type` tinyint NULL DEFAULT NULL ,
  `send_status` tinyint NOT NULL DEFAULT 0 ,
  `send_time` datetime NULL DEFAULT NULL ,
  `api_send_code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `api_send_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `api_request_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `api_serial_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `receive_status` tinyint NOT NULL DEFAULT 0 ,
  `receive_time` datetime NULL DEFAULT NULL ,
  `api_receive_code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `api_receive_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_sms_template`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `type` tinyint NOT NULL ,
  `status` tinyint NOT NULL ,
  `code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `name` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `api_template_id` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `channel_id` bigint NOT NULL ,
  `channel_code` varchar(63) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_social_client`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `social_type` tinyint NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `agent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `status` tinyint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_social_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT ,
  `type` tinyint NOT NULL ,
  `openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `raw_token_info` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `raw_user_info` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `state` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_social_user_bind`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `user_type` tinyint NOT NULL ,
  `social_type` tinyint NOT NULL ,
  `social_user_id` bigint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_tenant`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `contact_user_id` bigint NULL DEFAULT NULL ,
  `contact_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `contact_mobile` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `website` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `package_id` bigint NOT NULL ,
  `expire_time` datetime NOT NULL ,
  `account_count` int NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_tenant_package`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `menu_ids` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_user_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL DEFAULT 0 ,
  `post_id` bigint NOT NULL DEFAULT 0 ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `user_id` bigint NOT NULL ,
  `role_id` bigint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `system_users`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `dept_id` bigint NULL DEFAULT NULL ,
  `post_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `sex` tinyint NULL DEFAULT 0 ,
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `status` tinyint NOT NULL DEFAULT 0 ,
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `login_date` datetime NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `project_demo01_contact`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `sex` tinyint(1) NOT NULL ,
  `birthday` datetime NOT NULL ,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `project_demo02_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `parent_id` bigint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `project_demo03_course`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `student_id` bigint NOT NULL ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `score` tinyint NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `project_demo03_grade`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `student_id` bigint NOT NULL ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `teacher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `project_demo03_student`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
  `sex` tinyint NOT NULL ,
  `birthday` datetime NOT NULL ,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' ,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `deleted` bit(1) NOT NULL DEFAULT b'0' ,
  `tenant_id` bigint NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
                                       `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `BLOB_DATA` blob NULL,
                                       PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                       INDEX `SCHED_NAME`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
                                       CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_CALENDARS`  (
                                   `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                   `CALENDAR` blob NOT NULL,
                                   PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
                                       `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                       `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                       PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                       CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
                                        `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `FIRED_TIME` bigint NOT NULL,
                                        `SCHED_TIME` bigint NOT NULL,
                                        `PRIORITY` int NOT NULL,
                                        `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                        `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                        `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                        `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                        `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                        PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
                                        INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_JOB_DETAILS`  (
                                     `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                     `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                     `JOB_DATA` blob NULL,
                                     PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
                                     INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
                                     INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_LOCKS`  (
                               `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                               `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                               PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
                                             `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                             `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                             PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
                                         `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `LAST_CHECKIN_TIME` bigint NOT NULL,
                                         `CHECKIN_INTERVAL` bigint NOT NULL,
                                         PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
                                         `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `REPEAT_COUNT` bigint NOT NULL,
                                         `REPEAT_INTERVAL` bigint NOT NULL,
                                         `TIMES_TRIGGERED` bigint NOT NULL,
                                         PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                         CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
                                          `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                          `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                          `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                          `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                          `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                          `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                          `INT_PROP_1` int NULL DEFAULT NULL,
                                          `INT_PROP_2` int NULL DEFAULT NULL,
                                          `LONG_PROP_1` bigint NULL DEFAULT NULL,
                                          `LONG_PROP_2` bigint NULL DEFAULT NULL,
                                          `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
                                          `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
                                          `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                          `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                          PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                          CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `QRTZ_TRIGGERS`  (
                                  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL,
                                  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL,
                                  `PRIORITY` int NULL DEFAULT NULL,
                                  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                  `START_TIME` bigint NOT NULL,
                                  `END_TIME` bigint NULL DEFAULT NULL,
                                  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                                  `MISFIRE_INSTR` smallint NULL DEFAULT NULL,
                                  `JOB_DATA` blob NULL,
                                  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
                                  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME` ASC, `CALENDAR_NAME` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
                                  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
                                  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `ai_api_key`
(
    `id`          bigint                                                         NOT NULL AUTO_INCREMENT ,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `api_key`     varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `platform`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `status`      int                                                            NOT NULL ,
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`   bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_chat_conversation`
(
    `id`             bigint                                                         NOT NULL AUTO_INCREMENT ,
    `user_id`        bigint                                                         NOT NULL ,
    `role_id`        bigint                                                         NULL     DEFAULT NULL ,
    `title`          varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `model_id`       bigint                                                         NOT NULL ,
    `model`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `pinned`         bit(1)                                                         NOT NULL ,
    `pinned_time`    datetime                                                       NULL     DEFAULT NULL ,
    `system_message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `temperature`    double                                                         NOT NULL ,
    `max_tokens`     int                                                            NOT NULL ,
    `max_contexts`   int                                                            NOT NULL ,
    `creator`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT NULL ,
    `create_time`    datetime                                                       NULL     DEFAULT NULL ,
    `updater`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT NULL ,
    `update_time`    datetime                                                       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`        bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`      bigint                                                         NULL     DEFAULT NULL ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_chat_message`
(
    `id`              bigint                                                          NOT NULL AUTO_INCREMENT ,
    `conversation_id` bigint                                                          NOT NULL ,
    `reply_id`        bigint                                                          NULL     DEFAULT NULL ,
    `user_id`         bigint                                                          NOT NULL ,
    `role_id`         bigint                                                          NULL     DEFAULT NULL ,
    `type`            varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NOT NULL ,
    `model`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NOT NULL ,
    `model_id`        bigint                                                          NOT NULL ,
    `content`         varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `use_context`     bit(1)                                                          NOT NULL DEFAULT b'0' ,
    `segment_ids`     varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `creator`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL     DEFAULT NULL ,
    `create_time`     datetime                                                        NULL     DEFAULT NULL ,
    `updater`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci    NULL     DEFAULT NULL ,
    `update_time`     datetime                                                        NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`         bit(1)                                                          NOT NULL DEFAULT b'0' ,
    `tenant_id`       bigint                                                          NULL     DEFAULT NULL ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_chat_role`
(
    `id`             bigint                                                         NOT NULL AUTO_INCREMENT ,
    `user_id`        bigint                                                         NULL     DEFAULT NULL ,
    `model_id`       bigint                                                         NULL     DEFAULT NULL ,
    `name`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `avatar`         varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `category`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT NULL ,
    `sort`           int                                                            NOT NULL DEFAULT 0 ,
    `description`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `system_message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `knowledge_ids`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `tool_ids`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `public_status`  bit(1)                                                         NOT NULL ,
    `status`         tinyint                                                        NULL     DEFAULT NULL ,
    `creator`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time`    datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time`    datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`        bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`      bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_image`
(
    `id`            bigint                                                         NOT NULL AUTO_INCREMENT ,
    `user_id`       bigint                                                         NOT NULL ,
    `prompt`        varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `platform`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `model_id`      bigint                                                         NULL     DEFAULT NULL ,
    `model`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `width`         int                                                            NOT NULL ,
    `height`        int                                                            NOT NULL ,
    `status`        tinyint                                                        NOT NULL ,
    `finish_time`   datetime                                                       NULL     DEFAULT NULL ,
    `error_message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `public_status` bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `pic_url`       varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `options`       json                                                           NULL ,
    `task_id`       varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `buttons`       varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `creator`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time`   datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time`   datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`       bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`     bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_knowledge`
(
    `id`                   bigint                                                        NOT NULL AUTO_INCREMENT ,
    `name`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `description`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     NULL ,
    `embedding_model_id`   bigint                                                        NOT NULL ,
    `embedding_model`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `top_k`                int                                                           NOT NULL ,
    `similarity_threshold` double                                                        NOT NULL ,
    `status`               tinyint                                                       NOT NULL ,
    `creator`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' ,
    `create_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' ,
    `update_time`          datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`              bit(1)                                                        NOT NULL DEFAULT b'0' ,
    `tenant_id`            bigint                                                        NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_knowledge_document`
(
    `id`                 bigint                                                         NOT NULL AUTO_INCREMENT ,
    `knowledge_id`       bigint                                                         NOT NULL ,
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `url`                varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `content`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          NOT NULL ,
    `content_length`     int                                                            NOT NULL ,
    `tokens`             int                                                            NOT NULL ,
    `segment_max_tokens` int                                                            NOT NULL ,
    `retrieval_count`    int                                                            NOT NULL DEFAULT 0 ,
    `status`             tinyint                                                        NOT NULL ,
    `creator`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time`        datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time`        datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`            bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`          bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_knowledge_segment`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT ,
    `knowledge_id`    bigint                                                        NOT NULL ,
    `document_id`     bigint                                                        NOT NULL ,
    `content`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NOT NULL ,
    `content_length`  int                                                           NOT NULL ,
    `vector_id`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `tokens`          int                                                           NOT NULL ,
    `retrieval_count` int                                                           NOT NULL DEFAULT 0 ,
    `status`          tinyint                                                       NOT NULL ,
    `creator`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' ,
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' ,
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`         bit(1)                                                        NOT NULL DEFAULT b'0' ,
    `tenant_id`       bigint                                                        NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_mind_map`
(
    `id`                bigint                                                         NOT NULL AUTO_INCREMENT ,
    `user_id`           bigint                                                         NOT NULL ,
    `prompt`            text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          NOT NULL ,
    `generated_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          NULL ,
    `platform`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `model_id`          bigint                                                         NOT NULL ,
    `model`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `error_message`     varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `creator`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time`       datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time`       datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`           bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`         bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_model`
(
    `id`           bigint                                                       NOT NULL AUTO_INCREMENT ,
    `key_id`       bigint                                                       NOT NULL ,
    `name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `model`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `platform`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `type`         tinyint                                                      NOT NULL ,
    `sort`         int                                                          NOT NULL ,
    `status`       tinyint                                                      NOT NULL ,
    `temperature`  double                                                       NULL     DEFAULT NULL ,
    `max_tokens`   int                                                          NULL     DEFAULT NULL ,
    `max_contexts` int                                                          NULL     DEFAULT NULL ,
    `creator`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' ,
    `create_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT '' ,
    `update_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`      bit(1)                                                       NOT NULL DEFAULT b'0' ,
    `tenant_id`    bigint                                                       NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_music`
(
    `id`            bigint                                                         NOT NULL AUTO_INCREMENT ,
    `user_id`       bigint                                                         NOT NULL ,
    `title`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `lyric`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `image_url`     varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `audio_url`     varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `video_url`     varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `status`        tinyint                                                        NOT NULL ,
    `description`   varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `prompt`        varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `platform`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `model_id`      bigint                                                         NOT NULL ,
    `model`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NOT NULL ,
    `generate_mode` tinyint                                                        NOT NULL ,
    `tags`          varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `duration`      double                                                         NULL     DEFAULT NULL ,
    `public_status` bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `task_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `error_message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `creator`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time`   datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time`   datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`       bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`     bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_tool`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT ,
    `name`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL ,
    `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `status`      tinyint                                                       NOT NULL ,
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' ,
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT '' ,
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)                                                        NOT NULL DEFAULT b'0' ,
    `tenant_id`   bigint                                                        NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `ai_write`
(
    `id`                bigint                                                         NOT NULL AUTO_INCREMENT ,
    `user_id`           bigint                                                         NOT NULL ,
    `type`              int                                                            NULL     DEFAULT NULL ,
    `platform`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `model_id`          bigint                                                         NOT NULL ,
    `model`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `prompt`            varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL ,
    `generated_content` varchar(5120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `original_content`  varchar(5120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL ,
    `length`            tinyint                                                        NULL     DEFAULT NULL ,
    `format`            tinyint                                                        NULL     DEFAULT NULL ,
    `tone`              tinyint                                                        NULL     DEFAULT NULL ,
    `language`          tinyint                                                        NULL     DEFAULT NULL ,
    `error_message`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL     DEFAULT NULL ,
    `creator`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `create_time`       datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updater`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   NULL     DEFAULT '' ,
    `update_time`       datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`           bit(1)                                                         NOT NULL DEFAULT b'0' ,
    `tenant_id`         bigint                                                         NOT NULL DEFAULT 0 ,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `blog_sys_user`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT ,
    `username`      VARCHAR(50)  NOT NULL ,
    `password`      VARCHAR(100) NOT NULL ,
    `email`         VARCHAR(100) DEFAULT '' ,
    -- 角色 & 权限 --
    `role`          TINYINT      DEFAULT 0 ,
    `is_locked`     TINYINT      DEFAULT 0 ,
    `reputation`    INT          DEFAULT 100 ,
    `blog_count`    INT          DEFAULT 0 ,
    `like_count`    INT          DEFAULT 0 ,
    `invite_code`   VARCHAR(20)  DEFAULT NULL ,
    -- 审核相关 --
    `real_name`     VARCHAR(50)  DEFAULT '' ,
    `id_card_front` VARCHAR(255) DEFAULT '' ,
    `id_card_back`  VARCHAR(255) DEFAULT '' ,
    `audit_status`  TINYINT      DEFAULT 0 ,
    `audit_comment` VARCHAR(200) DEFAULT '' ,
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_post`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT ,
    `user_id`       BIGINT       NOT NULL ,
    -- 内容 & 状态 --
    `title`         VARCHAR(255) NOT NULL ,
    `content`       LONGTEXT     NOT NULL ,
    `status`        TINYINT      DEFAULT 0 ,
    `is_top`        TINYINT      DEFAULT 0 ,
    `top_time`      DATETIME     DEFAULT NULL ,
    `visibility`    TINYINT      DEFAULT 0 ,
    `view_count`    INT          DEFAULT 0 ,
    `like_count`    INT          DEFAULT 0 ,
    -- 审核日志 --
    `audit_user_id` BIGINT       DEFAULT NULL ,
    `audit_time`    DATETIME     DEFAULT NULL ,
    `audit_comment` VARCHAR(200) DEFAULT '' ,
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ,
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_is_top` (`is_top`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_category`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT ,
    `name`      VARCHAR(50) NOT NULL ,
    `parent_id` BIGINT DEFAULT 0 ,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_tag`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT ,
    `name` VARCHAR(50) NOT NULL ,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_post_tag`
(
    `id`          BIGINT   NOT NULL AUTO_INCREMENT ,
    `post_id`     BIGINT   NOT NULL ,
    `tag_id`      BIGINT   NOT NULL ,
    -- 下面是公共字段
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `create_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `update_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)   NOT NULL                                            DEFAULT b'0' ,
    `tenant_id`   bigint   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_tag_id` (`tag_id`),
    KEY `idx_post_id` (`post_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_comment`
(
    `id`            bigint                                                       NOT NULL AUTO_INCREMENT ,
    `post_id`       bigint                                                       NOT NULL ,
    `user_id`       bigint                                                       NOT NULL ,
    `content`       text                                                         NOT NULL ,
    `status`        tinyint                                                      NOT NULL DEFAULT '0' ,
    `audit_user_id` bigint                                                                DEFAULT NULL ,
    `create_time`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `update_time`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `creator`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
    `updater`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
    `deleted`       bit(1)                                                       NOT NULL DEFAULT b'0' ,
    `tenant_id`     bigint                                                       NOT NULL DEFAULT '0' ,
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`) ,
    KEY `idx_user_id` (`user_id`) 
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci ;

CREATE TABLE `blog_comment_reply`
(
    `id`               bigint                                                       NOT NULL AUTO_INCREMENT ,
    `parent_id`        bigint                                                       NOT NULL ,
    `post_id`          bigint                                                       NOT NULL ,
    `user_id`          bigint                                                       NOT NULL ,
    `reply_to_user_id` bigint                                                                DEFAULT NULL ,
    `content`          text                                                         NOT NULL ,
    `status`           tinyint                                                      NOT NULL DEFAULT '0' ,
    `audit_user_id`    bigint                                                                DEFAULT NULL ,
    `create_time`      datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `update_time`      datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `creator`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' ,
    `deleted`          bit(1)                                                       NOT NULL DEFAULT b'0' ,
    `tenant_id`        bigint                                                       NOT NULL DEFAULT '0' ,
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`) ,
    KEY `idx_post_id` (`post_id`) ,
    KEY `idx_user_id` (`user_id`) 
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci ;

CREATE TABLE `blog_sys_file`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT ,
    `file_name`   VARCHAR(255) NOT NULL ,
    `file_key`    VARCHAR(255) NOT NULL ,
    `file_size`   BIGINT   DEFAULT 0 ,
    `user_id`     BIGINT       NOT NULL ,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_key` (`file_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `sys_invite_code`
(
    `code`        VARCHAR(20) NOT NULL ,
    `creator_id`  BIGINT      NOT NULL ,
    `used_by`     BIGINT   DEFAULT NULL ,
    `expire_time` DATETIME DEFAULT NULL ,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (`code`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_used_by` (`used_by`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_report`
(
    `id`             BIGINT NOT NULL AUTO_INCREMENT ,
    `post_id`        BIGINT NOT NULL ,
    `user_id`        BIGINT NOT NULL ,
    `type`           TINYINT  DEFAULT 0 ,
    `content`        TEXT ,
    `status`         TINYINT  DEFAULT 0 ,
    `handle_user_id` BIGINT   DEFAULT NULL ,
    `handle_time`    DATETIME DEFAULT NULL ,
    `create_time`    DATETIME DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_sys_operation_log`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT NOT NULL ,
    `type`        TINYINT  DEFAULT 0 ,
    `target_id`   BIGINT   DEFAULT NULL ,
    `detail`      TEXT ,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE `blog_carousel`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT ,
    `image`       varchar(255) NOT NULL ,
    `title`       varchar(64)                                                  DEFAULT NULL ,
    `description` varchar(512)                                                 DEFAULT NULL ,
    `link`        varchar(255)                                                 DEFAULT NULL ,
    `sort`        int                                                          DEFAULT '0' ,
    `status`      tinyint      NOT NULL                                        DEFAULT '0' ,

    -- 下面是公共字段
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `create_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `update_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)       NOT NULL                                        DEFAULT b'0' ,
    `tenant_id`   bigint       NOT NULL                                        DEFAULT '0' ,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;

CREATE TABLE `blog_post_category`
(
    `id`          BIGINT   NOT NULL ,
    `post_id`     BIGINT   NOT NULL ,
    `category_id` BIGINT   NOT NULL ,
    -- 下面是公共字段
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `create_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `update_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)   NOT NULL                                            DEFAULT b'0' ,
    `tenant_id`   bigint   NOT NULL                                            DEFAULT '0' ,
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_category_id` (`category_id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 ;

CREATE TABLE blog_praise
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    blog_id       INT      NOT NULL,
    user_id       INT      NOT NULL,
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `create_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `update_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)   NOT NULL                                            DEFAULT b'0' ,
    `tenant_id`   bigint   NOT NULL                                            DEFAULT '0' ,
    KEY `idx_blog_id` (`blog_id`),
    KEY `idx_user_id` (`user_id`)
);

CREATE TABLE blog_es_recode
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `create_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ,
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `update_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`     bit(1)   NOT NULL                                            DEFAULT b'0' ,
    `tenant_id`   bigint   NOT NULL                                            DEFAULT '0' 
);

CREATE TABLE `chat_messages_group`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT ,
    `sender_id`    BIGINT     NOT NULL ,
    `sender_avatar` varchar(150) default 'https://example.invalid/resource' ,
    `sender_name` varchar(20) default '匿名' ,
    `receiver_group`  INT     NOT NULL ,
    `text`      TEXT            NOT NULL ,
    `message_type` TINYINT         NOT NULL                                     DEFAULT 1 ,
    `creator`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `updater`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' ,
    `create_time`  datetime        NOT NULL                                     DEFAULT CURRENT_TIMESTAMP ,
    `update_time`  datetime        NOT NULL                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
    `deleted`      bit(1)          NOT NULL                                     DEFAULT b'0' ,
    PRIMARY KEY (`id`),
    INDEX `idx_sender_receiver` (`sender_id`, `receiver_group`)-- 查询双方聊天记录的联合索引
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci ;
