/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50620
 Source Host           : localhost
 Source Database       : iwap5

 Target Server Type    : MySQL
 Target Server Version : 50620
 File Encoding         : utf-8

 Date: 03/01/2016 11:15:57 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `SYS_ACCOUNT`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ACCOUNT`;
CREATE TABLE `SYS_ACCOUNT` (
  `acct_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `acct_pwd` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `acct_status` char(1) COLLATE utf8_bin DEFAULT NULL,
  `acct_inv_dt` datetime DEFAULT NULL,
  `acct_nm` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `org_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `acct_phone` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  `acct_addr` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `acct_zipcode` varchar(6) COLLATE utf8_bin DEFAULT NULL,
  `acct_email` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `acct_ver_nm` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `last_login_tm` datetime DEFAULT NULL,
  `acct_crt_tm` datetime DEFAULT NULL,
  `acct_crt` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `acct_mdf_tm` datetime DEFAULT NULL,
  `acct_mdf` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `acct_ext1` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `acct_ext2` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `acct_ext3` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `acct_ext4` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `acct_ext5` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `pwd_err_count` int(11) DEFAULT NULL COMMENT '密码错误次数',
  `last_mdf_pwd_dt` date DEFAULT NULL COMMENT '最后修改密码日期',
  PRIMARY KEY (`acct_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `SYS_ACCOUNT`
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ACCOUNT` VALUES ('admin', 'cfa33edb77656627f1a0b4c71ce545ca', '1', null, 'admin', '0001', null, null, null, null, null, null, null, null, '2016-01-11 00:00:00', 'admin', null, null, null, null, null, '0', '2016-01-11'), ('test', '3343dda304429f74ad78783de8437829', '1', null, 'test', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test2', '3ae3ac3987040f4e6ca9d1c1e61e66ad', '1', null, 'test2', '0001', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test3', '1786bacdebf3460ccaec95c179052b0e', '1', null, 'test3', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test4', '410fc251e810063dbd6b967f979bf2fd', '1', null, 'test4', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test5', '4ef2d89d46bf386a1636cc4927643ada', '1', null, 'test5', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test6', 'f51b08213cf641b5326bf8eb83f61c59', '1', null, 'test6', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test7', 'fbbc1ad203f8b9926e9a1aef3384b875', '1', null, 'test7', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null), ('test8', 'e901f7a0ea778ad7f56c3c243475833c', '1', null, 'test8', '0002', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', null);
COMMIT;

DROP TABLE IF EXISTS `SYS_ACCT_PREFRC`;
CREATE TABLE `SYS_ACCT_PREFRC` (
  `acct_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `prefrc_category` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `setting_key` varchar(128) COLLATE utf8_bin NOT NULL,
  `setting_val` varchar(512) COLLATE utf8_bin NOT NULL,
  `remark` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `remark1` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `remark2` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `remark3` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`acct_id`,`prefrc_category`,`setting_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ----------------------------
-- Records of sys_acct_prefrc
-- ----------------------------
INSERT INTO `sys_acct_prefrc` VALUES ('admin', '1', '1', '1', null, null, null, null);
INSERT INTO `sys_acct_prefrc` VALUES ('test1', '1', '1', '1', null, null, null, null);


-- ----------------------------
--  Table structure for `sys_acct_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_acct_role`;
CREATE TABLE `sys_acct_role` (
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `acct_id` varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`role_id`,`acct_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `sys_acct_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_acct_role` VALUES ('admin', 'admin');
COMMIT;

-- ----------------------------
--  Table structure for `sys_data_item`
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_item`;
CREATE TABLE `sys_data_item` (
  `di_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `di_title` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `di_comment` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `di_def_val` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `di_type` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `di_length` int(11) DEFAULT NULL,
  `di_desc` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `di_precision` int(11) DEFAULT NULL,
  `di_editor` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`di_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `sys_data_item`
-- ----------------------------
BEGIN;
INSERT INTO `sys_data_item` VALUES ('acct_addr', '', '', null, 'varchar', '256', '', null, null), ('acct_crt', '', '', null, 'varchar', '64', '', null, null), ('acct_crt_tm', '', '', null, 'datetime', null, '', null, null), ('acct_email', '', '', null, 'varchar', '256', '', null, null), ('acct_ext1', '', '', null, 'varchar', '32', '', null, null), ('acct_ext2', '', '', null, 'varchar', '64', '', null, null), ('acct_ext3', '', '', null, 'varchar', '128', '', null, null), ('acct_ext4', '', '', null, 'varchar', '256', '', null, null), ('acct_ext5', '', '', null, 'varchar', '256', '', null, null), ('acct_id', '', '', null, 'varchar', '64', '', null, null), ('acct_inv_dt', '', '', null, 'datetime', null, '', null, null), ('acct_mdf', '', '', null, 'varchar', '64', '', null, null), ('acct_mdf_tm', '', '', null, 'datetime', null, '', null, null), ('acct_nm', '', '', null, 'varchar', '32', '', null, null), ('acct_phone', '', '', null, 'varchar', '11', '', null, null), ('acct_pwd', '', '', null, 'varchar', '256', '', null, null), ('acct_status', '', '', null, 'char', '1', '', null, null), ('acct_ver_nm', '', '', null, 'varchar', '128', '', null, null), ('acct_zipcode', '', '', null, 'varchar', '6', '', null, null), ('big_icon', '', '', null, 'varchar', '128', '', null, null), ('cache_size', '', '', null, 'int', null, '', null, null), ('col_name', '', '', null, 'varchar', '32', '', null, null), ('crt_date', '', '', null, 'datetime', null, '', null, null), ('crt_usr', '', '', null, 'varchar', '64', '', null, null), ('day_reset', '', '', null, 'char', '1', '', null, null), ('dict_id', '', '', null, 'varchar', '64', '', null, null), ('dict_key', '', '', null, 'varchar', '32', '', null, null), ('dict_val', '', '', null, 'varchar', '32', '', null, null), ('disest_clazz', '', '', null, 'varchar', '256', '', null, null), ('disest_flag', '', '', null, 'char', '1', '', null, null), ('di_comment', '', '', null, 'varchar', '128', '', null, null), ('di_def_val', '', '', null, 'varchar', '64', '', null, null), ('di_desc', '', '', null, 'varchar', '128', '', null, null), ('di_editor', '', '', null, 'varchar', '512', '', null, null), ('di_id', '', '', null, 'varchar', '64', '', null, null), ('di_length', '', '', null, 'int', null, '', null, null), ('di_precision', '', '', null, 'int', null, '', null, null), ('di_title', '', '', null, 'varchar', '64', '', null, null), ('di_type', '', '', null, 'varchar', '10', '', null, null), ('enable_flag', '', '', null, 'char', '1', '', null, null), ('flow_num', '', '', null, 'int', null, '', null, null), ('help_page', '', '', null, 'varchar', '128', '', null, null), ('help_title', '', '', null, 'varchar', '128', '', null, null), ('last_login_tm', '', '', null, 'datetime', null, '', null, null), ('login_flag', '', '', null, 'char', '1', '', null, null), ('log_flag', '', '', null, 'char', '1', '', null, null), ('mdf_date', '', '', null, 'datetime', null, '', null, null), ('mdf_usr', '', '', null, 'varchar', '64', '', null, null), ('module_hide', '', '', null, 'char', '1', '', null, null), ('module_id', '', '', null, 'varchar', '128', '', null, null), ('module_nm', '', '', null, 'varchar', '64', '', null, null), ('module_opcode', '', '', null, 'varchar', '64', '', null, null), ('module_order', '', '', null, 'int', null, '', null, null), ('module_target', '', '', null, 'varchar', '10', '', null, null), ('module_type', '', '', null, 'char', '1', '', null, null), ('module_url', '', '', null, 'varchar', '128', '', null, null), ('module_valid', '', '', null, 'char', '1', '', null, null), ('month_reset', '', '', null, 'char', '1', '', null, null), ('next_id', '', '', null, 'decimal', null, '', null, null), ('no_id', '', '', '', 'varchar', '64', '', null, null), ('no_length', '', '', null, 'int', null, '', null, null), ('no_name', '', '', null, 'varchar', '64', '', null, null), ('no_status', '', '', null, 'char', '1', '', null, null), ('order_val', '', '', null, 'int', null, '', null, null), ('org_crt_dt', '', '', null, 'date', null, '', null, null), ('org_ful_nm', '', '', null, 'varchar', '128', '', null, null), ('org_id', '', '', null, 'varchar', '64', '', null, null), ('org_lvl', '', '', null, 'int', null, '', null, null), ('org_nm', '', '', null, 'varchar', '64', '', null, null), ('org_order', '', '', null, 'int', null, '', null, null), ('org_path', '', '', null, 'varchar', '1024', '', null, null), ('org_pid', '', '', null, 'varchar', '64', '', null, null), ('org_rmk', '', '', null, 'varchar', '512', '', null, null), ('org_rmk1', '', '', null, 'varchar', '512', '', null, null), ('org_rmk2', '', '', null, 'varchar', '512', '', null, null), ('org_rmk3', '', '', null, 'varchar', '512', '', null, null), ('org_status', '', '', null, 'char', '1', '', null, null), ('param_en_nm', '', '', '', 'varchar', '64', '', null, null), ('param_val', '', '', null, 'varchar', '256', '', null, null), ('param_zh_nm', '', '', null, 'varchar', '128', '', null, null), ('pmodule_id', '', '', null, 'varchar', '128', '', null, null), ('postfix', '', '', null, 'varchar', '64', '', null, null), ('prefix', '', '', null, 'varchar', '64', '', null, null), ('prefrc_category', '', '', '', 'varchar', '32', '', null, null), ('remark', '', '', null, 'varchar', '512', '', null, null), ('req_tm', '', '', null, 'datetime', null, '', null, null), ('resp_tm', '', '', null, 'datetime', null, '', null, null), ('role_desc', '', '', null, 'varchar', '128', '', null, null), ('role_enabled', '', '', '1', 'varchar', '1', '', null, null), ('role_exclude', '', '', null, 'varchar', '512', '', null, null), ('role_extend', '', '', null, 'varchar', '64', '', null, null), ('role_id', '', '', null, 'varchar', '64', '', null, null), ('role_nm', '', '', null, 'varchar', '64', '', null, null), ('role_type', '', '', '0', 'varchar', '1', '', null, null), ('setting_key', '', '', null, 'varchar', '128', '', null, null), ('small_icon', '', '', null, 'varchar', '128', '', null, null), ('SYS_ACCT_PREFRC', '', '', null, 'varchar', '512', '', null, null), ('sys_org', '', '', null, 'date', null, '', null, null), ('sys_org_his', '', '', '0000-00-00', 'date', null, '', null, null), ('sys_param', '', '', null, 'varchar', '256', '', null, null), ('sys_tx_conf', '', '', '', 'varchar', '20', '', null, null), ('sys_tx_dtl', '', '', null, 'varchar', '64', '', null, null), ('tab_name', '', '', null, 'varchar', '64', '', null, null), ('token_flag', '', '', null, 'char', '1', '', null, null), ('tx_dt', '', '', null, 'date', null, '', null, null), ('tx_dtl_rmk1', '', '', null, 'varchar', '256', '', null, null), ('tx_dtl_rmk2', '', '', null, 'varchar', '256', '', null, null), ('tx_dtl_rmk3', '', '', null, 'varchar', '256', '', null, null), ('tx_flag', '', '', null, 'char', '1', '', null, null), ('tx_id', '', '', '', 'varchar', '64', '', null, null), ('tx_nm', '', '', null, 'varchar', '40', '', null, null), ('tx_rmk', '', '', null, 'varchar', '256', '', null, null), ('tx_rmk1', '', '', null, 'varchar', '256', '', null, null), ('tx_rmk2', '', '', null, 'varchar', '256', '', null, null), ('tx_rmk3', '', '', null, 'varchar', '256', '', null, null), ('year_reset', '', '', null, 'char', '1', '', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `dict_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `tab_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `col_name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `dict_key` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `dict_val` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `order_val` int(11) DEFAULT NULL,
  `enable_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`dict_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `sys_imp_cfg`
-- ----------------------------
DROP TABLE IF EXISTS `sys_imp_cfg`;
CREATE TABLE `sys_imp_cfg` (
  `imp_id` char(64) NOT NULL,
  `imp_nm` char(32) DEFAULT NULL COMMENT '导入名称',
  `tbl_en_nm` char(32) DEFAULT NULL COMMENT '表英文名称',
  `imp_tp` char(1) DEFAULT '0' COMMENT '导入类型',
  `start_pt` int(11) DEFAULT '1' COMMENT '开始行位置',
  `sheet_idx` int(11) DEFAULT NULL COMMENT '工作簿位置',
  `start_col` int(11) DEFAULT NULL COMMENT '开始列位置',
  `allow_row` varchar(128) DEFAULT NULL COMMENT '导入行校验',
  `before_proc` varchar(128) DEFAULT NULL COMMENT '导入之前处理逻辑',
  `after_proc` varchar(128) DEFAULT NULL COMMENT '导入之后处理逻辑',
  PRIMARY KEY (`imp_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
BEGIN;
INSERT INTO `sys_imp_cfg` (`imp_id`, `imp_nm`, `tbl_en_nm`, `imp_tp`, `start_pt`, `sheet_idx`, `start_col`, `allow_row`, `before_proc`, `after_proc`) VALUES ('000001', '用户数据导入', 'sys_account', '0', '1', NULL, '0', NULL, 'com.nantian.iwap.app.imp.impl.UserBeforeProcess', NULL);
INSERT INTO `sys_imp_cfg` (`imp_id`, `imp_nm`, `tbl_en_nm`, `imp_tp`, `start_pt`, `sheet_idx`, `start_col`, `allow_row`, `before_proc`, `after_proc`) VALUES ('000002', '用户数据导入', 'sys_account', '1', NULL, NULL, NULL, NULL, 'com.nantian.iwap.app.imp.impl.UserBeforeProcess', NULL);
COMMIT;

-- ----------------------------
--  Table structure for `sys_imp_cfg_dtl`
-- ----------------------------
DROP TABLE IF EXISTS `sys_imp_cfg_dtl`;
CREATE TABLE `sys_imp_cfg_dtl` (
  `imp_id` char(64) DEFAULT NULL,
  `dtl_id` char(64) NOT NULL,
  `fld_nm` char(30) DEFAULT NULL COMMENT '字段英文名称',
  `fld_caption` char(64) DEFAULT NULL COMMENT '字段中文名称',
  `PK_Flg` char(1) DEFAULT NULL COMMENT '是否主键 0-是1否',
  `Sort_Val` int(11) DEFAULT NULL COMMENT '排序值',
  `data_tp` char(1) DEFAULT NULL COMMENT '数据类型',
  `def_val` char(128) DEFAULT NULL COMMENT '默认值',
  `def_val_tp` char(1) DEFAULT NULL COMMENT '默认值类型',
  `allow_blank` char(1) DEFAULT NULL COMMENT '是否允许为空',
  PRIMARY KEY (`dtl_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
BEGIN;
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000001', 'acct_id', '用户ID', '0', '0', '0', NULL, NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000002', 'acct_pwd', '用户密码', '1', '1', '0', NULL, NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000003', 'acct_nm', '用户名称', '1', '2', '0', NULL, NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000004', 'org_id', '机构ID', '1', '3', '0', NULL, NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000005', 'acct_crt', '创建人', '1', '4', '0', '${ACCT_ID}', '0', '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000006', 'acct_crt_tm', '创建时间', '1', '5', '0', '${systime}', '0', '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000001', '000007', 'acct_status', '用户状态', '1', '6', '0', '1', '0', '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000008', 'acct_id', '用户ID', '0', '0', '0', 'B1', NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000009', 'acct_pwd', '用户密码', '1', '1', '0', 'B2', NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000010', 'acct_nm', '用户名称', '1', '2', '0', 'D1', NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000011', 'org_id', '机构ID', '1', '3', '0', 'D2', NULL, '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000012', 'acct_crt', '创建人', '1', '4', '0', '${ACCT_ID}', '0', '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000013', 'acct_crt_tm', '创建时间', '1', '5', '0', '${systime}', '0', '0');
INSERT INTO `sys_imp_cfg_dtl` (`imp_id`, `dtl_id`, `fld_nm`, `fld_caption`, `PK_Flg`, `Sort_Val`, `data_tp`, `def_val`, `def_val_tp`, `allow_blank`) VALUES ('000002', '000014', 'acct_status', '用户状态', '1', '6', '0', '1', '0', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_no`
-- ----------------------------
DROP TABLE IF EXISTS `sys_no`;
CREATE TABLE `sys_no` (
  `no_id` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `no_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `no_length` int(11) DEFAULT NULL,
  `prefix` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `postfix` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `next_id` decimal(18,0) DEFAULT NULL,
  `year_reset` char(1) COLLATE utf8_bin DEFAULT NULL,
  `month_reset` char(1) COLLATE utf8_bin DEFAULT NULL,
  `day_reset` char(1) COLLATE utf8_bin DEFAULT NULL,
  `cache_size` int(11) DEFAULT NULL,
  `no_status` char(1) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`no_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `sys_org`
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `org_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `org_nm` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `org_ful_nm` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `org_pid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `org_path` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `org_lvl` int(11) DEFAULT NULL,
  `org_status` char(1) COLLATE utf8_bin DEFAULT NULL,
  `org_rmk` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_order` int(11) DEFAULT NULL,
  `org_crt_dt` date DEFAULT NULL,
  `org_inv_dt` date DEFAULT NULL,
  `org_rmk1` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_rmk2` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_rmk3` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`org_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
BEGIN;
INSERT INTO `sys_org` VALUES ('0005', '越秀行', '越秀行', '0002', '0001.0002.0005', '3', '1', null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('0002', '广州行', '广州行', '0001', '0001.0002', '2', '1', null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('0001', '省行', '省行', '', '0001', '1', '1', null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('0003', '深圳行', '深圳行', '0001', '0001.0003', '2', '1', null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('0004', '佛山行', '佛山行', '0001', '0001.0004', '2', '1', null, null, null, null, null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_org_his`
-- ----------------------------
DROP TABLE IF EXISTS `sys_org_his`;
CREATE TABLE `sys_org_his` (
  `org_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `org_nm` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `org_ful_nm` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `org_pid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `org_path` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `org_lvl` int(11) DEFAULT NULL,
  `org_status` char(1) COLLATE utf8_bin DEFAULT NULL,
  `org_rmk` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_order` int(11) DEFAULT NULL,
  `org_crt_dt` date DEFAULT NULL,
  `org_inv_dt` date NOT NULL DEFAULT '0000-00-00',
  `org_rmk1` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_rmk2` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_rmk3` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`org_id`,`org_inv_dt`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `sys_param`
-- ----------------------------
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param` (
  `param_en_nm` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `param_zh_nm` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `param_val` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `enable_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `crt_date` datetime DEFAULT NULL,
  `crt_usr` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `mdf_date` datetime DEFAULT NULL,
  `mdf_usr` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `remark1` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `remark2` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `remark3` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`param_en_nm`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `sys_param`
-- ----------------------------
BEGIN;
INSERT INTO `sys_param` VALUES ('11', 'test11111', '1234', '0', '2015-12-11 16:15:28', 'admin', '2015-12-11 16:15:28', 'admin', null, null, null), ('1111', '222', '33', '1', '2015-12-21 09:39:52', 'admin', '2015-12-21 09:39:52', 'admin', null, null, null), ('11111', '2221', '33', '1', '2015-12-21 09:39:58', 'admin', '2015-12-21 09:39:58', 'admin', null, null, null), ('111111', '22211', '33', '1', '2015-12-21 09:40:03', 'admin', '2015-12-21 09:40:03', 'admin', null, null, null), ('1111111', '222111', '33', '1', '2015-12-21 09:40:08', 'admin', '2015-12-21 09:40:08', 'admin', null, null, null), ('11111111', '2221111', '33', '1', '2015-12-21 09:40:13', 'admin', '2015-12-21 09:40:13', 'admin', null, null, null), ('111111111', '22211111', '33', '1', '2015-12-21 09:40:18', 'admin', '2015-12-21 09:40:18', 'admin', null, null, null), ('1111111111', '222111111', '33', '1', '2015-12-21 09:40:23', 'admin', '2015-12-21 09:40:23', 'admin', null, null, null), ('11111111111', '2221111111', '33', '1', '2015-12-21 09:40:28', 'admin', '2015-12-21 09:40:28', 'admin', null, null, null), ('1122', 'test11111', '1234', '0', '2015-12-11 16:15:32', 'admin', '2015-12-11 16:15:32', 'admin', null, null, null), ('1122333', 'test11111', '1234', '0', '2015-12-11 16:15:37', 'admin', '2015-12-11 16:15:37', 'admin', null, null, null), ('112233344', 'test11111', '1234', '0', '2015-12-11 16:15:41', 'admin', '2015-12-11 16:15:41', 'admin', null, null, null), ('1122333447', 'test11111', '1234', '0', '2015-12-11 16:15:47', 'admin', '2015-12-11 16:15:47', 'admin', null, null, null), ('11223334475', 'test11111', '1234', '0', '2015-12-11 16:15:52', 'admin', '2015-12-11 16:15:52', 'admin', null, null, null), ('112233344756', 'test11111', '1234', '0', '2015-12-11 16:15:57', 'admin', '2015-12-11 16:15:57', 'admin', null, null, null), ('123232', '1232132', '444444', '1', '2016-01-11 14:42:35', 'admin', '2016-01-11 14:42:35', 'admin', null, null, null), ('defa', '1111', '333', '1', '2015-09-02 00:00:00', 'admin', '2015-09-02 00:00:00', 'admin', null, null, null), ('def_val1', 'cdmo', '10', '1', '2015-09-02 00:00:00', 'admin', '2015-09-02 00:00:00', 'admin', null, null, null), ('qaz', '1qa', '111', '0', '2015-12-21 16:41:22', 'admin', '2015-12-21 16:41:22', 'admin', null, null, null), ('qaz1', '1qa', '111', '0', '2015-12-21 16:41:31', 'admin', '2015-12-21 16:41:31', 'admin', null, null, null), ('qaz2', '1qa', '111', '0', '2015-12-21 16:41:37', 'admin', '2015-12-21 16:41:37', 'admin', null, null, null), ('TEST', 'test11111', '1234', '1', null, null, '2016-01-11 14:43:05', 'admin', null, null, null), ('TEST111', 'test11111', '1234', '0', '2015-12-11 16:05:13', 'admin', '2015-12-11 16:05:13', 'admin', null, null, null);
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `role_nm` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `role_desc` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `role_type` varchar(1) COLLATE utf8_bin DEFAULT '0',
  `role_enabled` varchar(1) COLLATE utf8_bin DEFAULT '1',
  `role_extend` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `role_exclude` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `org_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('admin', '超级管理员', '超级管理员', '0', '1', null, null,'0001');
INSERT INTO `sys_role` VALUES ('test2', 'test2', 'test2', '0', '1', null, null, '0001');

COMMIT;

-- ----------------------------
--  Table structure for `sys_role_module`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_module`;
CREATE TABLE `sys_role_module` (
  `module_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`module_id`,`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_role_module
-- ----------------------------
INSERT INTO `sys_role_module` VALUES ('2016030300000001', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030300000002', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030300000004', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000001', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000002', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000003', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000004', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000006', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000007', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000008', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000009', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000010', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000011', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000012', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000013', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000015', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000017', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016030900000018', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031000000002', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000001', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000002', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000003', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000004', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000005', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000006', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000009', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000010', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000011', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000012', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031200000001', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031500000002', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000013', 'admin');
INSERT INTO `sys_role_module` VALUES ('2016031100000014', 'admin');



-- ----------------------------
--  Table structure for `sys_tx_conf`
-- ----------------------------
DROP TABLE IF EXISTS `sys_tx_conf`;
CREATE TABLE `sys_tx_conf` (
  `tx_cd` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '',
  `tx_nm` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  `login_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `token_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `flow_num` int(11) DEFAULT NULL,
  `log_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `disest_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `disest_clazz` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `op_code` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',
  `param_opcode` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `tx_rmk1` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `tx_rmk2` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `tx_rmk3` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`tx_cd`,`op_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_tx_conf
-- ----------------------------
INSERT INTO `sys_tx_conf` VALUES ('sysParam', '新增系统参数', null, null, null, '1', null, null, 'insert', 'actionType', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('sysParam', '删除系统参数', null, null, null, '1', null, null, 'delete', 'actionType', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('sysParam', '修改系统参数', null, null, null, '1', null, null, 'update', 'actionType', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('menuMg', '新增菜单', null, null, null, '1', null, null, 'add', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('menuMg', '删除菜单', null, null, null, '1', null, null, 'remove', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('menuMg', '修改菜单', null, null, null, '1', null, null, 'save', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('roleMg', '新增角色', null, null, null, '1', null, null, 'insert', 'actionType', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('roleMg', '删除角色', null, null, null, '1', null, null, 'delete', 'actionType', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('roleMg', '修改角色', null, null, null, '1', null, null, 'update', 'actionType', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('userMg', '新增用户', null, null, null, '1', null, null, 'add', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('userMg', '删除用户', null, null, null, '1', null, null, 'remove', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('userMg', '修改用户', null, null, null, '1', null, null, 'save', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('userMg', '用户授权', null, null, null, '1', null, null, 'save_grant', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('department', '新增机构', null, null, null, '1', null, null, 'add', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('department', '删除机构', null, null, null, '1', null, null, 'remove', 'option', null, null, null);
INSERT INTO `sys_tx_conf` VALUES ('department', '修改机构', null, null, null, '1', null, null, 'save', 'option', null, null, null);

-- ----------------------------
--  Table structure for `sys_tx_dtl`
-- ----------------------------
DROP TABLE IF EXISTS `sys_tx_dtl`;
CREATE TABLE `sys_tx_dtl` (
  `tx_id` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `tx_cd` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `op_code`	varchar(64),
  `tx_flag` char(1) COLLATE utf8_bin DEFAULT NULL,
  `err_code`	varchar(32),
  `err_msg`	varchar(256),
  `tx_rmk` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `tx_dt` date DEFAULT NULL,
  `acct_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `req_tm` datetime DEFAULT NULL,
  `resp_tm` datetime DEFAULT NULL,
  `tx_dtl_rmk1` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `tx_dtl_rmk2` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `tx_dtl_rmk3` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`tx_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
--  Table structure for `sys_module`
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module` (
  `module_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `module_opcode` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `module_nm` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `pmodule_id` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `module_url` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `module_type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '0-分类1-功能2-操作',
  `module_valid` char(1) COLLATE utf8_bin DEFAULT NULL,
  `big_icon` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `small_icon` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `help_page` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `help_title` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `module_order` int(11) DEFAULT NULL,
  `module_hide` char(1) COLLATE utf8_bin DEFAULT NULL,
  `module_target` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `param_opcode` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`module_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('2016030900000001', 'sysParam', '系统参数管理', '2016030300000001', null, '1', '0', null, null, '', '', '200', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016030900000002', 'insert', '新增系统参数', '2016030900000001', null, '2', '0', null, null, '', '', '210', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016030300000002', 'menuMg', '菜单管理', '2016030300000001', null, '1', '0', null, null, null, null, '300', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016030900000003', 'delete', '删除系统参数', '2016030900000001', null, '2', '0', null, null, '', '', '220', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016030900000004', 'update', '修改系统参数', '2016030900000001', null, '2', '0', null, null, '', '', '230', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016030900000006', 'add', '新增菜单', '2016030300000002', null, '2', '0', null, null, '', '', '320', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016030300000001', null, '系统管理', null, null, '0', '0', null, null, null, null, '100', '0', null, null);
INSERT INTO `sys_module` VALUES ('2016030900000007', 'remove', '删除菜单', '2016030300000002', null, '2', '0', null, null, '', '', '330', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016030900000008', 'save', '修改菜单', '2016030300000002', null, '2', '0', null, null, '', '', '340', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016030900000009', 'roleMg', '角色管理', '2016030300000001', null, '1', '0', null, null, '', '', '400', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016030900000010', 'insert', '新增角色', '2016030900000009', null, '2', '0', null, null, '', '', '410', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016030900000011', 'delete', '删除角色', '2016030900000009', null, '2', '0', null, null, '', '', '420', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016030900000012', 'update', '修改角色', '2016030900000009', null, '2', '0', null, null, '', '', '430', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016030900000013', 'userMg', '用户管理', '2016030300000001', null, '1', '0', null, null, '', '', '500', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016030900000017', 'add', '新增用户', '2016030900000013', null, '2', '0', null, null, '', '', '510', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016030900000015', 'remove', '删除用户', '2016030900000013', null, '2', '0', null, null, '', '', '520', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016030900000018', 'save', '修改用户', '2016030900000013', null, '2', '0', null, null, '', '', '530', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016030300000004', 'show', '查看菜单', '2016030300000002', null, '2', '0', null, null, null, null, '350', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016031000000002', 'logQry', '日志查询', '2016030300000001', null, '1', '0', null, null, '', '', '700', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016031100000001', 'query_grant', '授权查询', '2016030900000013', null, '2', '0', null, null, '', '', '540', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016031100000002', 'save_grant', '授权保存', '2016030900000013', null, '2', '0', null, null, '', '', '550', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016031100000003', 'department', '机构管理', '2016030300000001', null, '1', '0', null, null, '', '', '600', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016031100000004', 'add', '新增机构', '2016031100000003', null, '2', '0', null, null, '', '', '610', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016031100000005', 'remove', '删除机构', '2016031100000003', null, '2', '0', null, null, '', '', '620', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016031100000006', 'save', '修改机构', '2016031100000003', null, '2', '0', null, null, '', '', '630', '0', null, 'option');
INSERT INTO `sys_module` VALUES ('2016031100000009', 'query_grant', '查询模块授权', '2016030900000009', null, '2', '0', null, null, '', '', '450', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016031200000001', 'prefrcMg', '用户偏好管理', '2016030300000001', null, '1', '0', null, null, '', '', '800', '0', null, '');
INSERT INTO `sys_module` VALUES ('2016031500000002', 'query_sys_org', '查询机构树', '2016030900000009', null, '2', '0', null, null, '', '', '560', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016031100000010', 'save_grant', '保存模块授权', '2016030900000009', null, '2', '0', null, null, null, null, '460', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016031100000011', 'query_sys_module', '查询系统模块树', '2016030900000009', null, '2', '0', null, null, null, null, '470', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('2016031100000012', 'query_sys_org', '查询机构树', '2016030900000009', null, '2', '0', null, null, null, null, '480', '0', null, 'actionType');
INSERT INTO `sys_module` VALUES ('00000000000000000001', 'remove', '删除用户偏好', '2016031200000001', NULL, '2', '0', NULL, NULL, '', '', '810', '0', NULL, 'option');
INSERT INTO `sys_module` VALUES ('2016031100000013', 'importData', '数据导入', '2016030300000001', NULL, '1', '0', NULL, NULL, NULL, NULL, '800', '1', NULL, NULL);
INSERT INTO `sys_module` VALUES ('2016031100000014', 'exportData', '数据导出', '2016030300000001', NULL, '1', '0', NULL, NULL, NULL, NULL, '900', '1', NULL, NULL);
