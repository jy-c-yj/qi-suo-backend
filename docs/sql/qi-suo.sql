/*
 Navicat Premium Data Transfer

 Source Server         : JYC
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : qi-suo

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 16/10/2026 17:11:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `admin_type` tinyint(0) NOT NULL DEFAULT 1 COMMENT '管理员类型：1-普通管理员，2-超级管理员，3-系统管理员',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`admin_id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone`) USING BTREE,
  INDEX `idx_admin_type`(`admin_type`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '{noop}123456', '18670065655', 3, 1, '2026-01-16 15:18:31', '2026-01-30 13:13:40', 'admin', 'admin', 0);
INSERT INTO `admin` VALUES (2, 'admin2', '123456', '11144558452', 1, 1, '2026-01-30 16:09:15', '2026-04-13 14:46:32', 'admin', 'admin', 0);
INSERT INTO `admin` VALUES (3, 'admin3', '12345678', '12548596524', 2, 1, '2026-01-30 16:10:04', '2026-04-13 14:00:49', 'admin', 'admin', 1);
INSERT INTO `admin` VALUES (4, '', '', '', 1, 1, '2026-01-30 16:10:30', '2026-01-30 16:29:04', 'admin', 'admin', 1);
INSERT INTO `admin` VALUES (5, 'cyj', '{bcrypt}$2a$10$qso2QFJvw4TSE/kcijvuQOHcMjT7p6O5gOfi9CtjSuksMHePZsPFK', '1111111111', 2, 1, '2026-01-30 16:44:26', '2026-04-13 15:19:16', 'admin', 'admin', 0);
INSERT INTO `admin` VALUES (6, 'admin4', '{bcrypt}$2a$10$KcD6L6Sjl6UWEtubbXyB9uDxiDtGBG3YUN8z2rVXfcwZSXGkwG77i', '111111111', 1, 1, '2026-10-16 16:05:27', '2026-10-16 16:05:27', 'admin', 'admin', 0);

-- ----------------------------
-- Table structure for attribute
-- ----------------------------
DROP TABLE IF EXISTS `attribute`;
CREATE TABLE `attribute`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `attribute_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `attribute_type` tinyint(0) NULL DEFAULT 1 COMMENT '属性类型：1-规格属性，2-销售属性',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_attribute_type`(`attribute_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attribute
-- ----------------------------
INSERT INTO `attribute` VALUES (1, '材质', 1, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (2, '颜色', 1, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (3, '尺寸', 1, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (4, '承重', 1, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (5, '重量', 1, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (6, '风格', 2, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (7, '适用空间', 2, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (8, '功能特点', 2, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (9, '适用人群', 2, '2026-01-26 17:51:38');
INSERT INTO `attribute` VALUES (10, '安装方式', 2, '2026-01-26 17:51:38');

-- ----------------------------
-- Table structure for attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `attribute_value`;
CREATE TABLE `attribute_value`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
  `attribute_id` int(0) NOT NULL COMMENT '属性ID',
  `value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性值',
  `color_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '颜色代码',
  `sort_order` int(0) NULL DEFAULT 0 COMMENT '排序值',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_attribute`(`attribute_id`) USING BTREE,
  INDEX `idx_sort`(`sort_order`) USING BTREE,
  CONSTRAINT `attribute_value_ibfk_1` FOREIGN KEY (`attribute_id`) REFERENCES `attribute` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '属性值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attribute_value
-- ----------------------------
INSERT INTO `attribute_value` VALUES (1, 1, '实木', NULL, 10, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (2, 1, '板材', NULL, 20, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (3, 1, '金属', NULL, 30, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (4, 1, '布艺', NULL, 40, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (5, 1, '皮质', NULL, 50, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (6, 1, '玻璃', NULL, 60, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (7, 1, '藤编', NULL, 70, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (8, 1, '塑料', NULL, 80, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (9, 2, '白色', '#FFFFFF', 10, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (10, 2, '黑色', '#000000', 20, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (11, 2, '灰色', '#808080', 30, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (12, 2, '棕色', '#A52A2A', 40, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (13, 2, '原木色', '#DEB887', 50, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (14, 2, '米白色', '#F5F5DC', 60, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (15, 2, '深蓝色', '#00008B', 70, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (16, 2, '绿色', '#008000', 80, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (17, 2, '红色', '#FF0000', 90, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (18, 3, '单人位', NULL, 10, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (19, 3, '双人位', NULL, 20, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (20, 3, '三人位', NULL, 30, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (21, 3, 'L型', NULL, 40, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (22, 3, '1.5米', NULL, 50, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (23, 3, '1.8米', NULL, 60, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (24, 3, '2.0米', NULL, 70, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (25, 3, '2.2米', NULL, 80, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (26, 3, '小户型', NULL, 90, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (27, 3, '常规款', NULL, 100, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (28, 6, '现代简约', NULL, 10, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (29, 6, '北欧风格', NULL, 20, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (30, 6, '中式古典', NULL, 30, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (31, 6, '美式乡村', NULL, 40, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (32, 6, '工业风格', NULL, 50, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (33, 6, '日式原木', NULL, 60, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (34, 6, '欧式奢华', NULL, 70, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (35, 6, '轻奢风格', NULL, 80, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (36, 7, '客厅', NULL, 10, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (37, 7, '卧室', NULL, 20, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (38, 7, '餐厅', NULL, 30, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (39, 7, '书房', NULL, 40, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (40, 7, '儿童房', NULL, 50, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (41, 7, '阳台', NULL, 60, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (42, 7, '办公室', NULL, 70, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (43, 7, '店铺', NULL, 80, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (44, 8, '带储物', NULL, 10, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (45, 8, '可折叠', NULL, 20, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (46, 8, '可拆洗', NULL, 30, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (47, 8, '可调节', NULL, 40, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (48, 8, '多功能', NULL, 50, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (49, 8, '环保材质', NULL, 60, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (50, 8, '静音设计', NULL, 70, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (51, 8, '防刮耐磨', NULL, 80, '2026-01-26 17:51:46');
INSERT INTO `attribute_value` VALUES (52, 8, '防水防潮', NULL, 90, '2026-01-26 17:51:46');

-- ----------------------------
-- Table structure for carousel_image
-- ----------------------------
DROP TABLE IF EXISTS `carousel_image`;
CREATE TABLE `carousel_image`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一标识符，可用于前端展示',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片描述',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL地址',
  `is_enabled` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用：1-启用，0-禁用',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：1-已删除，0-未删除',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `created_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modified_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uid`(`uid`) USING BTREE,
  INDEX `idx_enabled_deleted`(`is_enabled`, `is_deleted`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '轮播图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of carousel_image
-- ----------------------------
INSERT INTO `carousel_image` VALUES (1, '1111', 'test11112222', 'http://localhost:9000/qisuo/da017f10-624f-448c-9938-04761fb85058.jpg', 0, 1, NULL, 'admin', '2026-01-28 11:47:28', '2026-01-28 15:36:32');
INSERT INTO `carousel_image` VALUES (2, 'lb-1.jpgb2807d3f-adad-4a16-8130-0c6e679e3722', 'test1', 'http://127.0.0.1:9000/qisuo/3e3a315a-eaaf-44d4-aeb3-3917a5491338.jpg', 1, 0, 'admin', 'admin', '2026-01-28 16:11:35', '2026-01-28 16:11:35');
INSERT INTO `carousel_image` VALUES (3, 'lb-2.jpga9a1babb-c3b5-4da8-b24e-f4863b4b889c', 'test22', 'http://127.0.0.1:9000/qisuo/6a2cc5fc-77ae-4e04-94d5-3a04732bf00c.jpg', 1, 0, 'admin', 'admin', '2026-01-28 16:13:58', '2026-01-28 16:13:58');
INSERT INTO `carousel_image` VALUES (4, 'lb-3.jpg67e2023e-d6f4-4676-9794-89215fae8efa', 'test3', 'http://127.0.0.1:9000/qisuo/0be8de1c-13dd-4caf-a41e-8b03629082d7.jpg', 1, 0, 'admin', 'admin', '2026-01-29 14:23:39', '2026-01-29 14:23:39');
INSERT INTO `carousel_image` VALUES (5, 'lb-4.jpg27be4562-49fe-4bd3-a6e3-0e9d302933da', 'test4', 'http://127.0.0.1:9000/qisuo/d5bc6d4e-5ad3-4d66-924d-fe75206894f7.jpg', 1, 0, 'admin', 'admin', '2026-01-29 14:23:54', '2026-01-29 14:23:54');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `quantity` int(0) NOT NULL DEFAULT 1 COMMENT '数量',
  `total_price` decimal(10, 2) NOT NULL COMMENT '总价',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `merchant_id` bigint(0) NOT NULL COMMENT '商家id\r\n商家id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (27, 1, 38, 199.00, 1, 199.00, '2026-03-15 04:09:30', '2026-03-15 04:09:30', 3);
INSERT INTO `cart` VALUES (28, 1, 40, 399.00, 4, 1596.00, '2026-03-15 04:09:40', '2026-03-15 04:10:47', 2);
INSERT INTO `cart` VALUES (29, 1, 37, 1499.00, 1, 1499.00, '2026-03-15 04:10:39', '2026-03-15 04:10:39', 2);
INSERT INTO `cart` VALUES (31, 1, 41, 0.01, 11, 0.11, '2026-04-13 13:52:57', '2026-04-13 13:52:57', 1);
INSERT INTO `cart` VALUES (33, 2, 39, 299.00, 1, 299.00, '2026-10-16 16:55:43', '2026-10-16 16:55:43', 1);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` int(0) NULL DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `category_type` int(0) NOT NULL COMMENT '分类类型：1-空间分类，2-风格分类，3-材质分类，4-功能分类',
  `sort_order` int(0) NULL DEFAULT 0 COMMENT '排序值，越小越靠前',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `level` tinyint(0) NULL DEFAULT 1 COMMENT '分类层级',
  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类路径，如：/1/2/3',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_category_type`(`category_type`) USING BTREE,
  INDEX `idx_sort_order`(`sort_order`) USING BTREE,
  INDEX `idx_path`(`path`(255)) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 0, '客厅家具', 1, 10, 1, 1, '/1', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (2, 0, '卧室家具', 1, 20, 1, 1, '/2', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (3, 0, '餐厅家具', 1, 30, 1, 1, '/3', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (4, 0, '书房家具', 1, 40, 1, 1, '/4', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (5, 0, '儿童家具', 1, 50, 1, 1, '/5', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (6, 1, '沙发', 1, 1, 1, 2, '/1/6', '2026-01-26 17:51:13', '2026-02-09 16:22:11', 1);
INSERT INTO `category` VALUES (7, 1, '电视柜', 1, 2, 1, 2, '/1/7', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (8, 1, '茶几', 1, 3, 1, 2, '/1/8', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (9, 1, '鞋柜', 1, 4, 1, 2, '/1/9', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (10, 1, '客厅储物柜', 1, 5, 1, 2, '/1/10', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (11, 2, '床', 1, 1, 1, 2, '/2/11', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (12, 2, '衣柜', 1, 2, 1, 2, '/2/12', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (13, 2, '床头柜', 1, 3, 0, 2, '/2/13', '2026-01-26 17:51:13', '2026-02-09 15:38:59', 1);
INSERT INTO `category` VALUES (14, 2, '梳妆台', 1, 4, 1, 2, '/2/14', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (15, 2, '卧室储物柜', 1, 5, 1, 2, '/2/15', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (16, 0, '现代简约', 2, 10, 1, 1, '/16', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (17, 0, '北欧风格', 2, 20, 1, 1, '/17', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (18, 0, '中式古典', 2, 30, 1, 1, '/18', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (19, 0, '美式乡村', 2, 40, 1, 1, '/19', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (20, 0, '工业风格', 2, 50, 1, 1, '/20', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (21, 0, '日式原木', 2, 60, 1, 1, '/21', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (22, 0, '多功能家具', 4, 10, 1, 1, '/22', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (23, 0, '小户型专用', 4, 20, 1, 1, '/23', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (24, 0, '环保材质', 4, 30, 1, 1, '/24', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (25, 0, '可折叠', 4, 40, 1, 1, '/25', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (26, 0, '带储物功能', 4, 50, 1, 1, '/26', '2026-01-26 17:51:13', '2026-01-26 17:51:13', 0);
INSERT INTO `category` VALUES (27, 0, '测试', 1, 10, 0, 1, '/27', '2026-02-09 16:33:20', '2026-02-09 16:33:19', 0);
INSERT INTO `category` VALUES (28, 1, '测试分类', 1, 10, 0, 2, '/1/28', '2026-02-27 13:59:57', '2026-02-27 13:59:57', 0);

-- ----------------------------
-- Table structure for home_background_image
-- ----------------------------
DROP TABLE IF EXISTS `home_background_image`;
CREATE TABLE `home_background_image`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID/唯一标识',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '图片描述',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片URL地址',
  `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uid`(`uid`) USING BTREE,
  INDEX `idx_status`(`is_enabled`, `is_deleted`) USING BTREE,
  INDEX `idx_created`(`created_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '首页背景图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of home_background_image
-- ----------------------------
INSERT INTO `home_background_image` VALUES (1, 'home_001', '默认背景图片1', 'http://localhost:9000/qisuo/zzz.png', 1, 0, '2026-01-29 11:24:30', '2026-01-29 12:20:38');
INSERT INTO `home_background_image` VALUES (2, '25253dda-2a25-4a79-ae83-fa6b5450e81e', 'test1', 'http://127.0.0.1:9000/qisuo/fad6d4bb-1a1e-4ea2-881e-ae22939f8a01.jpg', 1, 0, '2026-01-30 16:51:36', '2026-01-30 16:51:36');
INSERT INTO `home_background_image` VALUES (3, '12507859-f01b-4c21-9c35-bc152379204a', 'aaaaaaa', 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', 1, 0, '2026-01-30 16:35:43', '2026-01-30 16:35:50');
INSERT INTO `home_background_image` VALUES (4, 'a268eae4-c9c6-462f-9eb5-cf35f9069d96', '测试', 'http://127.0.0.1:9000/qisuo/0e1cc528-d05f-4b28-b551-b16dd1e45c33.jpg', 0, 1, '2026-01-30 17:36:07', '2026-01-30 17:37:14');

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant`  (
  `merchant_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `merchant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家名',
  `merchant_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家密码',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺名称',
  `shop_logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '店铺Logo',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '店铺描述',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '商家状态：0-待审核，1-正常，2-关闭，3-禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`merchant_id`) USING BTREE,
  UNIQUE INDEX `uk_shop_name`(`shop_name`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商家表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of merchant
-- ----------------------------
INSERT INTO `merchant` VALUES (1, 'merchant001', '{noop}123456', '测试店铺', 'http://127.0.0.1:9000/qisuo/41ca5c2a-7762-47fa-94e8-42204c88e670.jpg', '测试店铺000000', 'jyc', '13900139000', '12300456@qq.com', 1, '2026-01-16 15:18:31', '2026-04-13 13:55:45', 1, 1, 0);
INSERT INTO `merchant` VALUES (2, '张三电子产品', '{noop}123456', '张三电子旗舰店', 'https://example.com/logos/shop1.jpg', '专业销售各类电子产品，包括手机、电脑、平板等，提供全国联保服务。', '张三', '13800138001', 'zhangsan@example.com', 1, '2026-02-11 12:51:20', '2026-02-11 12:51:20', 1, 1, 0);
INSERT INTO `merchant` VALUES (3, '李四家具城', '{noop}123456', '李四家具城', 'https://example.com/logos/shop2.png', '专注中高端家具销售20年，主营沙发、床、餐桌椅等全屋家具定制。', '李四', '13900139002', 'lisi@example.com', 1, '2026-02-11 12:51:20', '2026-02-11 12:51:20', 1, 1, 0);
INSERT INTO `merchant` VALUES (4, '王五服装店', '{noop}123456', '王五时尚服饰', 'https://example.com/logos/shop3.jpg', '主营时尚女装、男装及配饰，紧跟潮流趋势，提供优质购物体验。', '王五', '13700137003', 'wangwu@example.com', 1, '2026-02-11 12:51:20', '2026-02-11 12:51:20', 1, 2, 0);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(0) NOT NULL COMMENT '所属订单ID',
  `product_id` bigint(0) NOT NULL COMMENT '商品ID',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `quantity` int(0) NOT NULL DEFAULT 1 COMMENT '数量',
  `total_price` decimal(10, 2) NOT NULL COMMENT '总价（单价*数量）',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 13, 2999.00, 2, 5998.00, '2026-03-12 21:49:26');
INSERT INTO `order_item` VALUES (2, 1, 2, 1599.00, 2, 3198.00, '2026-03-12 21:49:26');
INSERT INTO `order_item` VALUES (3, 2, 38, 199.00, 2, 398.00, '2026-03-15 02:53:18');
INSERT INTO `order_item` VALUES (4, 3, 38, 199.00, 2, 398.00, '2026-03-15 02:59:20');
INSERT INTO `order_item` VALUES (5, 9, 40, 399.00, 4, 1596.00, '2026-04-13 13:44:46');
INSERT INTO `order_item` VALUES (6, 9, 37, 1499.00, 1, 1499.00, '2026-04-13 13:44:46');
INSERT INTO `order_item` VALUES (7, 10, 41, 0.01, 2, 0.02, '2026-04-13 13:53:23');
INSERT INTO `order_item` VALUES (8, 11, 41, 0.01, 1, 0.01, '2026-04-13 14:44:00');
INSERT INTO `order_item` VALUES (9, 12, 41, 0.01, 2, 0.02, '2026-04-13 14:48:05');
INSERT INTO `order_item` VALUES (10, 13, 41, 0.01, 1, 0.01, '2026-04-13 14:56:24');
INSERT INTO `order_item` VALUES (11, 14, 41, 0.01, 1, 0.01, '2026-04-13 15:03:00');
INSERT INTO `order_item` VALUES (12, 15, 41, 0.01, 1, 0.01, '2026-04-13 15:05:14');
INSERT INTO `order_item` VALUES (13, 16, 41, 0.01, 2, 0.02, '2026-04-13 15:14:03');
INSERT INTO `order_item` VALUES (14, 17, 41, 0.01, 1, 0.01, '2026-04-13 15:17:33');
INSERT INTO `order_item` VALUES (15, 18, 39, 299.00, 1, 299.00, '2026-10-16 16:55:45');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单号',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `user_address_id` bigint(0) NULL DEFAULT NULL COMMENT '用户地址ID',
  `merchant_id` bigint(0) NOT NULL COMMENT '商家ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总价',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '未支付' COMMENT '订单状态：未支付、已支付、已发货、已完成、已取消等',
  `stock_state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '库存状态：0-未占用 1-已占用待归还 2-已归还',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_merchant_id`(`merchant_id`) USING BTREE,
  INDEX `idx_status_created_at`(`status`, `created_at`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'e500729b539e4c15b15a3fb30f86d011', 1, 1, 1, 9196.00, '已发货', 0, '2026-03-12 21:49:26', '2026-03-12 21:52:11');
INSERT INTO `orders` VALUES (2, 'e5d1f88d38ed4caabdf8a53901d56d9d', 1, 1, 3, 398.00, '已支付', 0, '2026-03-15 02:53:18', '2026-03-15 03:17:59');
INSERT INTO `orders` VALUES (3, 'a1e9287a5ed74ca395c88ed1d8ec039a', 1, 1, 3, 398.00, '已支付', 0, '2026-03-15 02:59:20', '2026-03-15 03:18:11');
INSERT INTO `orders` VALUES (4, 'c918236548164b9f8a0ed9d550bbd0be', 1, 2, 2, 0.00, '已支付', 0, '2026-03-15 04:10:51', '2026-03-15 04:10:58');
INSERT INTO `orders` VALUES (5, '69df5440f91741deb446e44e66e94834', 1, 1, 2, 0.00, '已取消', 0, '2026-04-13 13:39:53', '2026-10-16 15:52:15');
INSERT INTO `orders` VALUES (6, 'd5a0810ceb0c4c82b433ef08b872c07b', 1, 1, 2, 0.00, '已取消', 0, '2026-04-13 13:40:32', '2026-10-16 15:52:15');
INSERT INTO `orders` VALUES (7, '42b4995b8615424da667a1181bf87db8', 1, 1, 2, 0.00, '已取消', 0, '2026-04-13 13:41:14', '2026-10-16 15:52:15');
INSERT INTO `orders` VALUES (8, 'e2a453be520c48ef8e9c8578fd57c75a', 1, 1, 2, 0.00, '已取消', 0, '2026-04-13 13:44:13', '2026-10-16 15:52:15');
INSERT INTO `orders` VALUES (9, '64e9244df8294d4fb24d3e09b7a574c3', 1, 1, 2, 3095.00, '已支付', 0, '2026-04-13 13:44:46', '2026-04-13 13:44:57');
INSERT INTO `orders` VALUES (10, 'f70a29042d1b49c8a0fdd74b755a1676', 1, 1, 1, 0.02, '已发货', 0, '2026-04-13 13:53:23', '2026-04-13 13:53:32');
INSERT INTO `orders` VALUES (11, '693f8d8891fc41d68d804e315cc26962', 1, 1, 1, 0.01, '已发货', 0, '2026-04-13 14:44:00', '2026-04-13 14:44:08');
INSERT INTO `orders` VALUES (12, '558e58d5caea4fe88b0d410740f0ec54', 1, 2, 1, 0.02, '已发货', 0, '2026-04-13 14:48:05', '2026-04-13 14:48:14');
INSERT INTO `orders` VALUES (13, '4fe08e48bbb34ff98b7b766693beee9d', 1, 2, 1, 0.01, '已发货', 0, '2026-04-13 14:56:24', '2026-04-13 14:56:32');
INSERT INTO `orders` VALUES (14, '5c196f7e25eb40c39a3d171593ee0b78', 1, 2, 1, 0.01, '已支付', 0, '2026-04-13 15:03:00', '2026-04-13 15:03:06');
INSERT INTO `orders` VALUES (15, '333f7f9bf20241e391be3227ee6941d9', 1, 2, 1, 0.01, '已发货', 0, '2026-04-13 15:05:14', '2026-04-13 15:05:21');
INSERT INTO `orders` VALUES (16, '857d4d99be5f4556bbd9ac7fd6053cf3', 1, 2, 1, 0.02, '已发货', 0, '2026-04-13 15:14:03', '2026-04-13 15:14:14');
INSERT INTO `orders` VALUES (17, '0ebf2e4ab7e643c89df43b4b3e609eb2', 1, 2, 1, 0.01, '已发货', 0, '2026-04-13 15:17:33', '2026-04-13 15:17:40');
INSERT INTO `orders` VALUES (18, 'bc41a4ca864040b2a5a3bfcc3ac54851', 2, 4, 1, 299.00, '未支付', 1, '2026-10-16 16:55:45', '2026-10-16 16:55:45');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `merchant_id` int(0) NOT NULL COMMENT '商户ID',
  `product_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详细描述',
  `price` decimal(10, 2) NOT NULL COMMENT '销售价格',
  `stock` int(0) NULL DEFAULT 0 COMMENT '库存数量',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品SKU',
  `is_hot` tinyint(1) NULL DEFAULT 0 COMMENT '是否热销',
  `is_new` tinyint(1) NULL DEFAULT 0 COMMENT '是否新品',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态：1-上架，2-下架',
  `rating` decimal(3, 2) NULL COMMENT '平均评分',
  `sales_count` int(0) NULL DEFAULT 0 COMMENT '销售数量',
  `view_count` int(0) NULL DEFAULT 0 COMMENT '浏览次数',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sku`(`sku`) USING BTREE,
  INDEX `idx_merchant`(`merchant_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_hot_new`(`is_hot`, `is_new`) USING BTREE,
  INDEX `idx_price`(`price`) USING BTREE,
  INDEX `idx_sales`(`sales_count`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '北欧简约布艺沙发', '三人位布艺沙发，可拆洗设计，北欧风格，适合中小户型客厅', 2999.00, 50, 3999.00, 'SOFA001', 1, 0, 1, 0.00, 0, 1, '2026-01-26 17:51:54', '2026-10-14 18:14:49', 0);
INSERT INTO `product` VALUES (2, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木电视柜', '橡木实木电视柜，1.8米长，现代简约设计，带储物功能', 1599.00, 30, 1999.00, 'TV001', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (3, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '现代简约双人床', '1.8米双人床，带储物功能，现代风格，静音设计', 3599.00, 25, 4599.00, 'BED001', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (4, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木餐桌椅套装', '一桌四椅实木餐桌套装，原木色，北欧风格，环保材质', 4999.00, 15, 5999.00, 'TABLE001', 1, 0, 1, 0.00, 0, 1, '2026-01-26 17:51:54', '2026-03-31 20:26:03', 0);
INSERT INTO `product` VALUES (5, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '儿童书桌椅套装', '可调节高度儿童书桌椅套装，环保材质，防刮耐磨', 1299.00, 40, 1699.00, 'KIDS001', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (6, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '办公转椅', '人体工学办公转椅，可调节高度和角度，透气网布', 899.00, 60, 1199.00, 'CHAIR001', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (7, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '布艺单人沙发', '北欧风格布艺单人沙发，可拆洗，小户型专用', 799.00, 35, 999.00, 'SOFA002', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (8, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '简约床头柜', '现代简约床头柜，带抽屉，静音轨道', 399.00, 80, 499.00, 'NIGHT001', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (9, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木书柜', '橡木实木书柜，多层储物空间，环保油漆', 2199.00, 20, 2799.00, 'BOOK001', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (10, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '可折叠餐桌', '小户型可折叠餐桌，节省空间，多功能设计', 1299.00, 25, 1599.00, 'TABLE002', 1, 0, 1, 0.00, 0, 0, '2026-01-26 17:51:54', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (11, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木沙发', '优质实木沙发，舒适耐用', 1999.00, 50, 2599.00, 'SKU001', 1, 1, 1, 4.50, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (12, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '简约茶几', '现代简约风格茶几', 599.00, 100, 799.00, 'SKU002', 1, 1, 1, 4.20, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (13, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '双人床', '1.8米实木双人床', 2999.00, 30, 3999.00, 'SKU003', 1, 0, 1, 4.70, 0, 1, '2026-03-03 15:03:26', '2026-03-15 01:21:49', 0);
INSERT INTO `product` VALUES (14, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '四门衣柜', '简约衣柜，大容量', 1899.00, 20, 2399.00, 'SKU004', 1, 0, 1, 4.30, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (15, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '餐椅', '舒适餐椅，4把装', 499.00, 80, 699.00, 'SKU005', 1, 1, 1, 4.00, 0, 2, '2026-03-03 15:03:26', '2026-09-04 18:59:37', 0);
INSERT INTO `product` VALUES (16, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '书桌', '实木书桌', 899.00, 40, 1099.00, 'SKU006', 1, 0, 1, 4.40, 0, 1, '2026-03-03 15:03:26', '2026-03-31 20:25:49', 0);
INSERT INTO `product` VALUES (17, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '鞋柜', '简约鞋柜', 399.00, 120, 499.00, 'SKU007', 1, 1, 1, 4.10, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (18, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '床头柜', '实木床头柜', 299.00, 150, 399.00, 'SKU008', 1, 0, 1, 4.20, 0, 1, '2026-03-03 15:03:26', '2026-03-31 20:25:54', 0);
INSERT INTO `product` VALUES (19, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '电视柜', '简约电视柜', 799.00, 60, 999.00, 'SKU009', 1, 0, 1, 4.30, 0, 1, '2026-03-03 15:03:26', '2026-10-16 16:59:57', 0);
INSERT INTO `product` VALUES (20, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '书架', '实木书架', 699.00, 70, 899.00, 'SKU010', 1, 1, 1, 4.50, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (21, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '简约布艺沙发', '舒适布艺沙发，现代风格', 6666.00, 45, 2299.00, 'SKU011', 1, 0, 1, 4.30, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (22, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木餐桌', '实木餐桌，可伸缩', 1299.00, 30, 1599.00, 'SKU012', 1, 1, 1, 4.60, 0, 3, '2026-03-03 15:10:52', '2026-03-14 23:15:22', 0);
INSERT INTO `product` VALUES (23, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '儿童床', '1.2米儿童床', 1599.00, 25, 1999.00, 'SKU013', 1, 1, 1, 4.40, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (24, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '双门衣柜', '简约双门衣柜', 1099.00, 40, 1399.00, 'SKU014', 1, 0, 1, 4.20, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (25, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '办公椅', '人体工学办公椅', 799.00, 60, 999.00, 'SKU015', 1, 0, 1, 4.50, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (26, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '电脑桌', '简约电脑桌', 599.00, 80, 799.00, 'SKU016', 1, 1, 1, 4.10, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (27, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '翻斗鞋柜', '超薄翻斗鞋柜', 499.00, 100, 649.00, 'SKU017', 1, 0, 1, 4.20, 0, 1, '2026-03-03 15:10:52', '2026-03-14 23:45:55', 0);
INSERT INTO `product` VALUES (28, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '智能床头柜', '带USB充电床头柜', 399.00, 120, 499.00, 'SKU018', 1, 1, 1, 4.30, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (29, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '悬挂电视柜', '简约悬挂式电视柜', 899.00, 35, 1199.00, 'SKU019', 1, 0, 1, 4.40, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (30, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '转角书架', 'L型转角书架', 799.00, 50, 999.00, 'SKU020', 1, 1, 1, 4.50, 0, 1, '2026-03-03 15:10:52', '2026-03-15 01:20:55', 0);
INSERT INTO `product` VALUES (31, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '真皮沙发', '头层牛皮沙发', 3999.00, 15, 4999.00, 'SKU021', 1, 1, 1, 4.80, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (32, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '大理石茶几', '天然大理石台面', 999.00, 20, 1299.00, 'SKU022', 1, 0, 1, 4.30, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (33, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '储物床', '高箱储物床', 2599.00, 21, 3299.00, 'SKU023', 1, 0, 1, 4.60, 0, 2, '2026-03-03 15:10:52', '2026-03-15 18:13:46', 0);
INSERT INTO `product` VALUES (34, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '推拉门衣柜', '移门衣柜', 2199.00, 25, 2799.00, 'SKU024', 1, 0, 1, 4.40, 0, 1, '2026-03-03 15:10:52', '2026-04-13 15:11:56', 0);
INSERT INTO `product` VALUES (35, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '吧台椅', '可升降吧台椅', 299.00, 150, 399.00, 'SKU025', 1, 1, 1, 4.10, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (36, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '梳妆台', '带镜梳妆台', 699.00, 60, 899.00, 'SKU026', 1, 0, 1, 4.20, 0, 1, '2026-03-03 15:10:52', '2026-03-15 01:18:44', 0);
INSERT INTO `product` VALUES (37, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '餐边柜', '实木餐边柜', 1499.00, 30, 1899.00, 'SKU027', 1, 0, 1, 4.50, 0, 1, '2026-03-03 15:10:52', '2026-03-15 04:10:36', 0);
INSERT INTO `product` VALUES (38, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '换鞋凳', '布艺换鞋凳', 199.00, 206, 299.00, 'SKU028', 1, 1, 1, 4.00, 0, 2, '2026-03-03 15:10:52', '2026-10-14 18:34:18', 0);
INSERT INTO `product` VALUES (39, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '置物架', '落地置物架', 299.00, 179, 399.00, 'SKU029', 1, 0, 1, 4.30, 0, 2, '2026-03-03 15:10:52', '2026-10-16 16:55:45', 0);
INSERT INTO `product` VALUES (40, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '穿衣镜', '实木穿衣镜', 399.00, 101, 499.00, 'SKU030', 1, 0, 1, 4.40, 0, 2, '2026-03-03 15:10:52', '2026-10-16 16:27:48', 0);
INSERT INTO `product` VALUES (41, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', 'asd', '测试', 0.01, 100, 0.01, '20260306235544000001', 1, 0, 1, 0.00, 0, 2, '2026-03-06 23:55:44', '2026-04-13 15:13:55', 0);
INSERT INTO `product` VALUES (49, 1, 'http://127.0.0.1:9000/qisuo/5fcef301-02f7-4e83-89ba-67a680f9d011.jpg', '示例商品', '示例商品', 0.01, 1, 0.01, '20260331210235000001', 0, 0, 1, 0.00, 0, 0, '2026-03-31 21:02:35', '2026-03-31 21:02:35', 0);
INSERT INTO `product` VALUES (50, 1, 'http://127.0.0.1:9000/qisuo/a7c739ce-7954-4ad1-9670-54056200b45f.jpg', '1111', '11111', 0.01, 1, 0.01, '20261014151409000001', 0, 0, 1, 0.00, 0, 0, '2026-10-14 15:14:10', '2026-10-14 15:14:10', 0);

-- ----------------------------
-- Table structure for product_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `product_attribute_value`;
CREATE TABLE `product_attribute_value`  (
  `product_id` int(0) NOT NULL COMMENT '商品ID',
  `attribute_value_id` int(0) NOT NULL COMMENT '属性值ID',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`product_id`, `attribute_value_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  INDEX `idx_attribute_value`(`attribute_value_id`) USING BTREE,
  CONSTRAINT `product_attribute_value_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_attribute_value_ibfk_2` FOREIGN KEY (`attribute_value_id`) REFERENCES `attribute_value` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性值关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_attribute_value
-- ----------------------------
INSERT INTO `product_attribute_value` VALUES (1, 4, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (1, 11, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (1, 20, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (1, 29, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (1, 36, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (1, 46, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (2, 1, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (2, 13, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (2, 23, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (2, 28, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (2, 36, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (2, 44, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 1, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 9, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 23, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 28, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 37, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 44, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (3, 50, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (4, 1, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (4, 13, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (4, 27, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (4, 29, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (4, 38, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (4, 49, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 8, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 15, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 26, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 28, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 40, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 47, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 49, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (5, 51, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (6, 4, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (6, 10, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (6, 27, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (6, 28, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (6, 42, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (6, 47, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (7, 4, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (7, 14, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (7, 18, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (7, 29, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (7, 36, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (7, 46, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 2, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 9, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 27, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 28, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 37, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 44, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (8, 50, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 1, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 13, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 27, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 29, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 39, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 44, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (9, 49, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 2, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 9, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 26, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 28, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 38, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 45, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (10, 48, '2026-01-26 17:54:48');
INSERT INTO `product_attribute_value` VALUES (11, 1, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (11, 11, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (11, 20, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (11, 46, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (12, 6, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (12, 9, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (12, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (12, 44, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (13, 1, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (13, 13, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (13, 23, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (13, 44, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (14, 2, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (14, 9, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (14, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (14, 49, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (15, 1, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (15, 13, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (15, 18, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (15, 45, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (16, 1, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (16, 13, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (16, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (16, 44, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (17, 2, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (17, 9, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (17, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (17, 44, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (18, 1, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (18, 13, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (18, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (18, 44, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (19, 2, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (19, 9, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (19, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (19, 44, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (20, 1, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (20, 13, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (20, 27, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (20, 49, '2026-03-03 15:07:18');
INSERT INTO `product_attribute_value` VALUES (21, 4, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (21, 11, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (21, 20, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (21, 39, '2026-03-06 23:01:57');
INSERT INTO `product_attribute_value` VALUES (22, 1, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (22, 13, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (22, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (22, 45, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (23, 1, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (23, 13, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (23, 22, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (23, 49, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (24, 2, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (24, 9, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (24, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (24, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (25, 5, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (25, 10, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (25, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (25, 47, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (26, 2, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (26, 9, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (26, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (26, 45, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (27, 2, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (27, 9, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (27, 26, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (27, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (28, 2, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (28, 11, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (28, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (28, 48, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (29, 2, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (29, 9, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (29, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (29, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (30, 1, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (30, 13, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (30, 26, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (30, 49, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (31, 5, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (31, 10, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (31, 20, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (31, 50, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (32, 6, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (32, 9, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (32, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (32, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (33, 1, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (33, 13, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (33, 23, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (33, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (34, 2, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (34, 9, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (34, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (34, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (35, 3, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (35, 10, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (35, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (35, 47, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (36, 1, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (36, 13, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (36, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (36, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (37, 1, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (37, 12, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (37, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (37, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (38, 4, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (38, 14, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (38, 26, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (38, 44, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (39, 3, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (39, 10, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (39, 26, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (39, 45, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (40, 6, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (40, 13, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (40, 27, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (40, 48, '2026-03-03 15:10:52');
INSERT INTO `product_attribute_value` VALUES (41, 1, '2026-03-06 23:55:44');
INSERT INTO `product_attribute_value` VALUES (49, 1, '2026-03-31 21:02:35');
INSERT INTO `product_attribute_value` VALUES (49, 2, '2026-03-31 21:02:35');
INSERT INTO `product_attribute_value` VALUES (50, 28, '2026-10-14 15:14:10');
INSERT INTO `product_attribute_value` VALUES (50, 33, '2026-10-14 15:14:10');

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `product_id` int(0) NOT NULL COMMENT '商品ID',
  `category_id` int(0) NOT NULL COMMENT '分类ID',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`product_id`, `category_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE,
  INDEX `idx_category`(`category_id`) USING BTREE,
  CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `product_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES (1, 6, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (1, 17, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (1, 23, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (1, 26, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (2, 7, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (2, 16, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (2, 26, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (3, 11, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (3, 16, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (3, 24, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (3, 26, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (4, 9, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (4, 17, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (4, 24, '2026-01-26 17:52:01');
INSERT INTO `product_category` VALUES (11, 6, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (11, 16, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (12, 8, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (12, 16, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (13, 11, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (13, 17, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (13, 26, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (14, 12, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (14, 16, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (14, 24, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (15, 3, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (15, 21, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (15, 25, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (16, 4, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (16, 16, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (16, 26, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (17, 9, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (17, 16, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (17, 26, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (18, 2, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (18, 17, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (18, 26, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (19, 7, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (19, 16, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (19, 26, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (20, 4, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (20, 21, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (20, 24, '2026-03-03 15:06:29');
INSERT INTO `product_category` VALUES (21, 6, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (21, 7, '2026-03-06 22:41:20');
INSERT INTO `product_category` VALUES (21, 9, '2026-03-06 22:44:48');
INSERT INTO `product_category` VALUES (21, 11, '2026-03-06 22:41:49');
INSERT INTO `product_category` VALUES (21, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (22, 3, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (22, 21, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (22, 22, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (23, 5, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (23, 17, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (23, 24, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (24, 2, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (24, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (24, 26, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (25, 4, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (25, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (25, 22, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (26, 4, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (26, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (26, 25, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (27, 9, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (27, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (27, 23, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (28, 2, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (28, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (28, 22, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (29, 7, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (29, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (29, 23, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (30, 4, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (30, 17, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (30, 24, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (31, 6, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (31, 20, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (32, 8, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (32, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (32, 22, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (33, 11, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (33, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (33, 26, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (34, 12, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (34, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (34, 26, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (35, 3, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (35, 20, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (35, 25, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (36, 14, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (36, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (36, 26, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (37, 3, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (37, 19, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (37, 26, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (38, 9, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (38, 17, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (38, 23, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (39, 10, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (39, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (39, 25, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (40, 2, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (40, 16, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (40, 22, '2026-03-03 15:10:52');
INSERT INTO `product_category` VALUES (41, 12, '2026-03-06 23:55:44');
INSERT INTO `product_category` VALUES (49, 7, '2026-03-31 21:02:35');
INSERT INTO `product_category` VALUES (50, 15, '2026-10-14 15:14:10');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色代码，唯一标识，如：ADMIN, USER, EDITOR',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称，如：系统管理员、普通用户',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '角色描述',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否激活',
  `created_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `created_by` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '创建人ID',
  `updated_by` int(0) UNSIGNED NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code`) USING BTREE,
  INDEX `idx_role_code`(`role_code`) USING BTREE,
  INDEX `idx_is_active`(`is_active`) USING BTREE,
  INDEX `idx_created_at`(`created_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'USER', '普通用户', '普通用户角色，拥有基本权限', 1, '2026-01-15 11:31:06', '2026-01-15 11:31:06', 1, 1);
INSERT INTO `role` VALUES (2, 'Merchant', '商家', '商家角色，拥有商品管理、订单管理等权限', 1, '2026-01-15 11:31:06', '2026-01-16 14:28:23', 1, 1);
INSERT INTO `role` VALUES (3, 'OrdinaryAdmin', '普通管理员', '系统管理员角色，拥有系统管理所有权限', 1, '2026-01-15 11:31:06', '2026-01-16 14:28:55', 1, 1);
INSERT INTO `role` VALUES (4, 'SuperAdmin', '超级管理员', '系统管理员角色，拥有系统管理所有权限', 1, '2026-01-16 14:34:24', '2026-01-16 14:34:38', 1, 1);
INSERT INTO `role` VALUES (5, 'SystemAdmin', '系统管理员', '顶级系统管理员', 1, '2026-01-30 09:49:31', '2026-01-30 09:49:40', 1, 1);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `message_id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MQ 消息 id，幂等去重用',
  `trace_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '全局链路 id，网关生成',
  `service_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '来源服务，如 qi-suo-user',
  `module` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '@Log(module)',
  `operation` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '@Log(desc)',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求路径',
  `http_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'GET/POST/PUT/DELETE',
  `client_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '客户端 ip（XFF 首段）',
  `user_id` bigint(0) NOT NULL DEFAULT -1 COMMENT '用户 id，-1 表未登录/取不到',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色，逗号分隔',
  `request_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '脱敏后的请求参数 json，截断 4096 字符',
  `cost_ms` int(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '耗时毫秒',
  `biz_code` int(0) NOT NULL DEFAULT 0 COMMENT 'Result.code，1 成功 0 失败',
  `success` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1 成功 0 失败',
  `error_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常类全限定名',
  `error_msg` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '异常消息 / 业务失败信息',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '请求发生时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_message_id`(`message_id`) USING BTREE,
  INDEX `idx_trace_id`(`trace_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_user_time`(`user_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口请求日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1, '6a6009e9-fb3e-4b0d-8645-56a4fed01a34', 'a41215c87dae4009807bdfd2d366d55a', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":2,\"pageNum\":1}', 146, 1, 1, NULL, 'null msg', '2026-10-16 16:23:22.925');
INSERT INTO `sys_oper_log` VALUES (2, '5a51b79c-07f7-4c71-a1af-bdb010d40131', '079aa097c71c4b75a6965b30a96d7b1d', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":1,\"pageNum\":1}', 9, 1, 1, NULL, 'null msg', '2026-10-16 16:24:02.014');
INSERT INTO `sys_oper_log` VALUES (3, 'c49bf829-35ed-4eaa-a27b-bad7d93907ce', '02f02f0f16af4701ad7737d98483d070', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":2,\"pageNum\":1}', 11, 1, 1, NULL, 'null msg', '2026-10-16 16:24:02.720');
INSERT INTO `sys_oper_log` VALUES (4, 'e8054462-7cc7-434e-917e-4eaee0b160c4', '7359bd78741f4677bfd39d7f722e55a0', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":2,\"pageNum\":1}', 7, 1, 1, NULL, 'null msg', '2026-10-16 16:24:03.340');
INSERT INTO `sys_oper_log` VALUES (5, '877977a5-d4e4-465b-b976-47b88ea083ae', '47706976d70f4013ad7f2dbb21a0be0f', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":1,\"pageNum\":1}', 7, 1, 1, NULL, 'null msg', '2026-10-16 16:24:04.293');
INSERT INTO `sys_oper_log` VALUES (6, '51a0e47a-7255-4e45-b116-0eca0f6d837f', '9741be536c9f42df8c1ef0cca6426c10', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":2,\"pageNum\":1}', 16, 1, 1, NULL, 'null msg', '2026-10-16 16:55:00.658');
INSERT INTO `sys_oper_log` VALUES (7, '1e0592d0-07fc-4d84-8b35-6b32e28cc124', 'b421db31f5c54b2ca1996923b2c34d39', 'qi-suo-admin', '用户管理', '启用或禁用用户', '/api/admin/userManagement/status', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":1,\"pageNum\":1}', 8, 1, 1, NULL, 'null msg', '2026-10-16 16:55:01.481');
INSERT INTO `sys_oper_log` VALUES (8, 'b37a70dc-99ae-48ee-8aad-43177dd9b9ca', '934d6c3c02164e74ae9e6556a7ad6143', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 2, 'USER', '{\"productId\":39,\"quantity\":1}', 27, 1, 1, NULL, 'null msg', '2026-10-16 16:55:39.105');
INSERT INTO `sys_oper_log` VALUES (9, '3defead6-edc1-4801-b7b3-227b018356ec', 'b6cc162aefa5496eadbba7e2f07aa974', 'qi-suo-user', '购物车', '删除购物车商品', '/api/user/cart/delete/39', 'DELETE', '0:0:0:0:0:0:0:1', 2, 'USER', '{\"productId\":39}', 14, 1, 1, NULL, 'null msg', '2026-10-16 16:55:40.798');
INSERT INTO `sys_oper_log` VALUES (10, '20e21e2d-0496-4204-a9e3-cd7e571b2361', '9e9652d5023048fba4a808a1b9d82d5c', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 2, 'USER', '{\"productId\":39,\"quantity\":1}', 7, 1, 1, NULL, 'null msg', '2026-10-16 16:55:43.043');
INSERT INTO `sys_oper_log` VALUES (11, 'f7a92ea1-ad9d-4fc3-bec7-40791bbc4462', '0710e55398ad453a97714f65bd729629', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 2, 'USER', '{\"createOrderDTO\":{\"merchantId\":1,\"userAddressId\":4}}', 80, 1, 1, NULL, 'null msg', '2026-10-16 16:55:45.688');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名，唯一',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密存储）',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户状态：0-正常，1-禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  UNIQUE INDEX `uk_email`(`email`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'cyj', '{noop}123456', 'http://localhost:9000/test/preview.jpg', '18670465625', '123456@cc.com', 0, '2026-01-15 11:27:42');
INSERT INTO `user` VALUES (2, 'mxy', '{bcrypt}$2a$10$AU4HDQY/v6kUxKqE2jg.JecjITh7MYwjXOMtdAEhR0bCADmfyZ1Lq', 'http://127.0.0.1:9000/qisuo/da017f10-624f-448c-9938-04761fb85058.jpg', '18670465625', '123456@qq.com', 0, '2026-01-26 10:59:10');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细地址',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
  `created_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_at` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (1, 1, '测试地址1', 0, '2026-03-12 21:42:27', '2026-04-13 13:53:46');
INSERT INTO `user_address` VALUES (2, 1, '测试地址2', 0, '2026-03-15 03:37:37', '2026-03-15 03:37:37');
INSERT INTO `user_address` VALUES (4, 2, 'testtt', 0, '2026-10-16 16:27:30', '2026-10-16 16:27:30');

-- ----------------------------
-- Table structure for user_merchant
-- ----------------------------
DROP TABLE IF EXISTS `user_merchant`;
CREATE TABLE `user_merchant`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(0) UNSIGNED NOT NULL COMMENT '用户ID，关联user表的id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名，关联user表的username',
  `merchant_id` bigint(0) NOT NULL COMMENT '商家ID，关联merchant表的merchant_id',
  `merchant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商家名，关联merchant表的merchant_name',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺名称，关联merchant表的shop_name',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_merchant`(`user_id`, `merchant_id`) USING BTREE COMMENT '用户和商家的唯一组合',
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_merchant_id`(`merchant_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户商家关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_merchant
-- ----------------------------

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关联ID，主键',
  `user_id` bigint(0) UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` int(0) UNSIGNED NOT NULL COMMENT '角色ID',
  `is_active` tinyint(1) NULL DEFAULT 1 COMMENT '是否生效',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id`, `role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_is_active`(`is_active`) USING BTREE,
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1, 1);
INSERT INTO `user_role` VALUES (2, 2, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
