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

 Date: 19/10/2026 18:49:45
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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '{noop}123456', '18670065655', 3, 1, '2026-01-16 15:18:31', '2026-01-30 13:13:40', 'admin', 'admin', 0);
INSERT INTO `admin` VALUES (2, 'admin_', '{noop}123456', '11144558452', 2, 1, '2026-01-30 16:09:15', '2026-10-19 02:26:35', 'admin', 'admin', 0);
INSERT INTO `admin` VALUES (3, 'admin3', '{noop}123456', '12548596524', 2, 1, '2026-01-30 16:10:04', '2026-10-19 00:48:49', 'admin', 'admin', 1);
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
INSERT INTO `carousel_image` VALUES (6, '屏幕截图 2026-10-18 224633.pngfd279b13-1a94-4b36-be2f-5478dfb22f13', 'test', 'http://127.0.0.1:9000/qisuo/1432cfcb-309f-42d0-a7ff-0963aa853d53.png', 1, 0, 'admin', 'admin', '2026-10-19 00:07:25', '2026-10-19 00:07:25');

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
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (27, 1, 38, 199.00, 1, 199.00, '2026-03-15 04:09:30', '2026-03-15 04:09:30', 3);
INSERT INTO `cart` VALUES (28, 1, 40, 399.00, 6, 2394.00, '2026-03-15 04:09:40', '2026-03-15 04:10:47', 2);
INSERT INTO `cart` VALUES (29, 1, 37, 1499.00, 1, 1499.00, '2026-03-15 04:10:39', '2026-03-15 04:10:39', 2);
INSERT INTO `cart` VALUES (31, 1, 41, 0.01, 15, 0.15, '2026-04-13 13:52:57', '2026-04-13 13:52:57', 1);
INSERT INTO `cart` VALUES (33, 2, 39, 299.00, 1, 299.00, '2026-10-16 16:55:43', '2026-10-16 16:55:43', 1);
INSERT INTO `cart` VALUES (34, 1, 14, 1899.00, 1, 1899.00, '2026-10-17 17:14:41', '2026-10-17 17:14:41', 3);
INSERT INTO `cart` VALUES (35, 1, 36, 699.00, 2, 1398.00, '2026-10-17 17:32:46', '2026-10-17 17:32:46', 1);
INSERT INTO `cart` VALUES (36, 1, 21, 6666.00, 2, 13332.00, '2026-10-17 17:33:10', '2026-10-17 17:33:10', 1);
INSERT INTO `cart` VALUES (37, 1, 24, 1099.00, 1, 1099.00, '2026-10-17 18:39:13', '2026-10-17 18:39:13', 1);
INSERT INTO `cart` VALUES (38, 1, 11, 1999.00, 1, 1999.00, '2026-10-17 18:40:21', '2026-10-17 18:40:21', 1);
INSERT INTO `cart` VALUES (39, 1, 34, 2199.00, 1, 2199.00, '2026-10-17 19:18:25', '2026-10-17 19:18:25', 2);
INSERT INTO `cart` VALUES (40, 1, 25, 799.00, 3, 2397.00, '2026-10-18 23:21:48', '2026-10-18 23:21:48', 2);

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
INSERT INTO `category` VALUES (29, 1111111, '阿斯顿', 1, 10, 1, 1, '/29', '2026-10-19 00:20:19', '2026-10-19 00:20:18', 0);
INSERT INTO `category` VALUES (30, 0, '阿斯顿', 1, 10, 0, 2, '/0/30', '2026-10-19 00:24:03', '2026-10-19 00:24:03', 0);
INSERT INTO `category` VALUES (31, 29, '阿斯顿', 1, 10, 0, 2, '/29/31', '2026-10-19 00:24:29', '2026-10-19 00:24:29', 0);
INSERT INTO `category` VALUES (32, 0, 'ceshi', 1, 10, 1, 1, '/32', '2026-10-19 02:27:49', '2026-10-19 02:27:57', 0);

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
INSERT INTO `home_background_image` VALUES (3, '12507859-f01b-4c21-9c35-bc152379204a', 'aaaaaaa', 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', 1, 0, '2026-01-30 16:35:43', '2026-10-19 02:28:34');
INSERT INTO `home_background_image` VALUES (4, 'a268eae4-c9c6-462f-9eb5-cf35f9069d96', '测试', 'http://127.0.0.1:9000/qisuo/0e1cc528-d05f-4b28-b551-b16dd1e45c33.jpg', 0, 1, '2026-01-30 17:36:07', '2026-01-30 17:37:14');
INSERT INTO `home_background_image` VALUES (5, '4235b3da-c9c7-405b-9fdc-df35fd8913bb', 'a', 'http://127.0.0.1:9000/qisuo/b41920ea-91e3-4f09-a89c-fedcddad8b70.png', 0, 1, '2026-10-18 23:55:25', '2026-10-18 23:55:47');
INSERT INTO `home_background_image` VALUES (6, '12ce8d27-2450-4cfc-8687-6e7e7adce710', 'qq', 'http://127.0.0.1:9000/qisuo/218443a8-7577-4229-9470-2d1aae2b8693.jpg', 0, 1, '2026-10-18 23:56:13', '2026-10-18 23:57:13');
INSERT INTO `home_background_image` VALUES (7, '0313be20-a93e-4332-8ff1-0bfdbf82ca4b', 'zz', 'http://127.0.0.1:9000/qisuo/b0eb66fa-03e1-4553-96e8-f4de62f20808.png', 0, 0, '2026-10-18 23:57:38', '2026-10-18 23:57:38');
INSERT INTO `home_background_image` VALUES (8, 'fd3ab919-beaf-4531-b88f-c8106d4575b4', '啊啊', 'http://127.0.0.1:9000/qisuo/204730e1-4c58-41d6-81e7-d15390ca9890.jpg', 0, 0, '2026-10-18 23:58:18', '2026-10-18 23:58:18');

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
INSERT INTO `merchant` VALUES (1, 'merchant001', '{noop}123456', '测试店铺01', 'http://127.0.0.1:9000/qisuo/800711f3-8b69-42b3-9b5c-c40d496045b6.png', '测试店铺000000', 'jycc', '18670465625', '123004156@qq.com', 1, '2026-01-16 15:18:31', '2026-10-19 02:25:31', 1, 1, 0);
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
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

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
INSERT INTO `order_item` VALUES (16, 19, 38, 199.00, 1, 199.00, '2026-10-17 17:14:45');
INSERT INTO `order_item` VALUES (17, 19, 14, 1899.00, 1, 1899.00, '2026-10-17 17:14:45');
INSERT INTO `order_item` VALUES (18, 20, 36, 699.00, 2, 1398.00, '2026-10-17 17:33:19');
INSERT INTO `order_item` VALUES (19, 20, 21, 6666.00, 1, 6666.00, '2026-10-17 17:33:19');
INSERT INTO `order_item` VALUES (20, 21, 24, 1099.00, 1, 1099.00, '2026-10-17 18:40:24');
INSERT INTO `order_item` VALUES (21, 21, 11, 1999.00, 1, 1999.00, '2026-10-17 18:40:24');
INSERT INTO `order_item` VALUES (22, 22, 40, 399.00, 1, 399.00, '2026-10-17 19:18:31');
INSERT INTO `order_item` VALUES (23, 22, 34, 2199.00, 1, 2199.00, '2026-10-17 19:18:31');
INSERT INTO `order_item` VALUES (24, 23, 21, 6666.00, 1, 6666.00, '2026-10-18 23:16:07');
INSERT INTO `order_item` VALUES (25, 24, 40, 399.00, 6, 2394.00, '2026-10-18 23:22:33');
INSERT INTO `order_item` VALUES (26, 24, 25, 799.00, 3, 2397.00, '2026-10-18 23:22:33');
INSERT INTO `order_item` VALUES (27, 25, 41, 0.01, 3, 0.03, '2026-10-19 02:15:23');

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
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'e500729b539e4c15b15a3fb30f86d011', 1, 1, 1, 9196.00, '已完成', 0, '2026-03-12 21:49:26', '2026-10-17 19:19:02');
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
INSERT INTO `orders` VALUES (18, 'bc41a4ca864040b2a5a3bfcc3ac54851', 2, 4, 1, 299.00, '已取消', 2, '2026-10-16 16:55:45', '2026-10-17 13:47:35');
INSERT INTO `orders` VALUES (19, 'eb1f81b91f754a7590abd607d61923da', 1, 1, 3, 2098.00, '已取消', 2, '2026-10-17 17:14:45', '2026-10-17 17:29:45');
INSERT INTO `orders` VALUES (20, '83b8c1edb61a49b99a83cae057fbaa7a', 1, 1, 1, 8064.00, '已取消', 2, '2026-10-17 17:33:19', '2026-10-17 17:48:18');
INSERT INTO `orders` VALUES (21, '5df74d13fff347f9b5f9cba1b33bf35a', 1, 1, 1, 3098.00, '已发货', 1, '2026-10-17 18:40:24', '2026-10-17 18:42:17');
INSERT INTO `orders` VALUES (22, 'a8dddc152ac64c7d93a7468a3d3514e5', 1, 1, 2, 2598.00, '已支付', 1, '2026-10-17 19:18:31', '2026-10-17 19:18:47');
INSERT INTO `orders` VALUES (23, '14c13b2482a849bf8371e9865da82f29', 1, 2, 1, 6666.00, '已取消', 2, '2026-10-18 23:16:07', '2026-10-18 23:31:07');
INSERT INTO `orders` VALUES (24, '8bfb9ba7d381477d8e6e5b90e190e95d', 1, 1, 2, 4791.00, '已取消', 2, '2026-10-18 23:22:33', '2026-10-18 23:37:33');
INSERT INTO `orders` VALUES (25, '2a083b27467c41b8a5a3c87bdffd7a1d', 1, 1, 1, 0.03, '已支付', 1, '2026-10-19 02:15:23', '2026-10-19 02:15:28');

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
INSERT INTO `product` VALUES (11, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木沙发', '优质实木沙发，舒适耐用', 1999.00, 49, 2599.00, 'SKU001', 1, 1, 1, 4.50, 0, 1, '2026-03-03 15:03:26', '2026-10-17 18:40:24', 0);
INSERT INTO `product` VALUES (12, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '简约茶几', '现代简约风格茶几', 599.00, 100, 799.00, 'SKU002', 1, 1, 1, 4.20, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (13, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '双人床', '1.8米实木双人床', 2999.00, 30, 3999.00, 'SKU003', 1, 0, 1, 4.70, 0, 2, '2026-03-03 15:03:26', '2026-10-17 17:15:59', 0);
INSERT INTO `product` VALUES (14, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '四门衣柜', '简约衣柜，大容量', 1899.00, 20, 2399.00, 'SKU004', 1, 0, 1, 4.30, 0, 2, '2026-03-03 15:03:26', '2026-10-17 17:29:45', 0);
INSERT INTO `product` VALUES (15, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '餐椅', '舒适餐椅，4把装', 499.00, 80, 699.00, 'SKU005', 1, 1, 1, 4.00, 0, 2, '2026-03-03 15:03:26', '2026-09-04 18:59:37', 0);
INSERT INTO `product` VALUES (16, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '书桌', '实木书桌', 899.00, 40, 1099.00, 'SKU006', 1, 0, 1, 4.40, 0, 1, '2026-03-03 15:03:26', '2026-03-31 20:25:49', 0);
INSERT INTO `product` VALUES (17, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '鞋柜', '简约鞋柜', 399.00, 120, 499.00, 'SKU007', 1, 1, 1, 4.10, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (18, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '床头柜', '实木床头柜', 299.00, 150, 399.00, 'SKU008', 1, 0, 1, 4.20, 0, 1, '2026-03-03 15:03:26', '2026-03-31 20:25:54', 0);
INSERT INTO `product` VALUES (19, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '电视柜', '简约电视柜', 799.00, 60, 999.00, 'SKU009', 1, 0, 1, 4.30, 0, 1, '2026-03-03 15:03:26', '2026-10-16 16:59:57', 0);
INSERT INTO `product` VALUES (20, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '书架', '实木书架', 699.00, 70, 899.00, 'SKU010', 1, 1, 1, 4.50, 0, 0, '2026-03-03 15:03:26', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (21, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '简约布艺沙发', '舒适布艺沙发，现代风格', 6666.00, 45, 2299.00, 'SKU011', 1, 0, 1, 4.30, 0, 1, '2026-03-03 15:10:52', '2026-10-18 23:31:07', 0);
INSERT INTO `product` VALUES (22, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '实木餐桌', '实木餐桌，可伸缩', 1299.00, 30, 1599.00, 'SKU012', 1, 1, 1, 4.60, 0, 3, '2026-03-03 15:10:52', '2026-03-14 23:15:22', 0);
INSERT INTO `product` VALUES (23, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '儿童床', '1.2米儿童床', 1599.00, 25, 1999.00, 'SKU013', 1, 1, 1, 4.40, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (24, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '双门衣柜', '简约双门衣柜', 1099.00, 39, 1399.00, 'SKU014', 1, 0, 1, 4.20, 0, 1, '2026-03-03 15:10:52', '2026-10-17 18:40:24', 0);
INSERT INTO `product` VALUES (25, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '办公椅', '人体工学办公椅', 799.00, 60, 999.00, 'SKU015', 1, 0, 1, 4.50, 0, 1, '2026-03-03 15:10:52', '2026-10-18 23:37:33', 0);
INSERT INTO `product` VALUES (26, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '电脑桌', '简约电脑桌', 599.00, 80, 799.00, 'SKU016', 1, 1, 1, 4.10, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (27, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '翻斗鞋柜', '超薄翻斗鞋柜', 499.00, 100, 649.00, 'SKU017', 1, 0, 1, 4.20, 0, 1, '2026-03-03 15:10:52', '2026-03-14 23:45:55', 0);
INSERT INTO `product` VALUES (28, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '智能床头柜', '带USB充电床头柜', 399.00, 120, 499.00, 'SKU018', 1, 1, 1, 4.30, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (29, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '悬挂电视柜', '简约悬挂式电视柜', 899.00, 35, 1199.00, 'SKU019', 1, 0, 1, 4.40, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (30, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '转角书架', 'L型转角书架', 799.00, 50, 999.00, 'SKU020', 1, 1, 1, 4.50, 0, 1, '2026-03-03 15:10:52', '2026-03-15 01:20:55', 0);
INSERT INTO `product` VALUES (31, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '真皮沙发', '头层牛皮沙发', 3999.00, 15, 4999.00, 'SKU021', 1, 1, 1, 4.80, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (32, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '大理石茶几', '天然大理石台面', 999.00, 20, 1299.00, 'SKU022', 1, 0, 1, 4.30, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (33, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '储物床', '高箱储物床', 2599.00, 21, 3299.00, 'SKU023', 1, 0, 1, 4.60, 0, 2, '2026-03-03 15:10:52', '2026-03-15 18:13:46', 0);
INSERT INTO `product` VALUES (34, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '推拉门衣柜', '移门衣柜', 2199.00, 24, 2799.00, 'SKU024', 1, 0, 1, 4.40, 0, 1, '2026-03-03 15:10:52', '2026-10-17 19:18:31', 0);
INSERT INTO `product` VALUES (35, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '吧台椅', '可升降吧台椅', 299.00, 150, 399.00, 'SKU025', 1, 1, 1, 4.10, 0, 0, '2026-03-03 15:10:52', '2026-03-14 22:58:13', 0);
INSERT INTO `product` VALUES (36, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '梳妆台', '带镜梳妆台', 699.00, 60, 899.00, 'SKU026', 1, 0, 1, 4.20, 0, 2, '2026-03-03 15:10:52', '2026-10-17 17:48:18', 0);
INSERT INTO `product` VALUES (37, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '餐边柜', '实木餐边柜', 1499.00, 30, 1899.00, 'SKU027', 1, 0, 1, 4.50, 0, 1, '2026-03-03 15:10:52', '2026-03-15 04:10:36', 0);
INSERT INTO `product` VALUES (38, 3, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '换鞋凳', '布艺换鞋凳', 199.00, 206, 299.00, 'SKU028', 1, 1, 1, 4.00, 0, 2, '2026-03-03 15:10:52', '2026-10-17 17:29:45', 0);
INSERT INTO `product` VALUES (39, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '置物架', '落地置物架', 299.00, 180, 399.00, 'SKU029', 1, 0, 1, 4.30, 0, 3, '2026-03-03 15:10:52', '2026-10-18 23:22:07', 0);
INSERT INTO `product` VALUES (40, 2, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', '穿衣镜', '实木穿衣镜', 399.00, 100, 499.00, 'SKU030', 1, 0, 1, 4.40, 0, 4, '2026-03-03 15:10:52', '2026-10-19 02:01:34', 0);
INSERT INTO `product` VALUES (41, 1, 'http://127.0.0.1:9000/qisuo/862f5d2c-6e2f-4ef6-bd91-5ce1938853fe.jpg', 'asd', '测试', 0.01, 97, 0.01, '20260306235544000001', 1, 0, 1, 0.00, 0, 3, '2026-03-06 23:55:44', '2026-10-19 02:15:23', 1);
INSERT INTO `product` VALUES (49, 1, 'http://127.0.0.1:9000/qisuo/5fcef301-02f7-4e83-89ba-67a680f9d011.jpg', '示例商品', '示例商品', 0.01, 1, 0.01, '20260331210235000001', 0, 0, 1, 0.00, 0, 0, '2026-03-31 21:02:35', '2026-10-19 02:19:48', 0);
INSERT INTO `product` VALUES (50, 1, 'http://127.0.0.1:9000/qisuo/a7c739ce-7954-4ad1-9670-54056200b45f.jpg', '1111', '11111', 0.03, 2, 0.01, '20261014151409000001', 0, 0, 1, 0.00, 0, 0, '2026-10-14 15:14:10', '2026-10-19 02:34:20', 0);
INSERT INTO `product` VALUES (51, 1, 'http://127.0.0.1:9000/qisuo/edb40b61-8530-4722-9fc5-42552904b172.png', '测试商品', '测试', 0.01, 999, 0.04, '20261019023504000001', 1, 0, 1, 0.00, 0, 0, '2026-10-19 02:35:04', '2026-10-19 02:35:04', 0);

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
INSERT INTO `product_attribute_value` VALUES (50, 9, '2026-10-19 02:34:18');
INSERT INTO `product_attribute_value` VALUES (50, 10, '2026-10-19 02:34:18');
INSERT INTO `product_attribute_value` VALUES (50, 28, '2026-10-14 15:14:10');
INSERT INTO `product_attribute_value` VALUES (50, 33, '2026-10-14 15:14:10');
INSERT INTO `product_attribute_value` VALUES (51, 50, '2026-10-19 02:35:04');

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
INSERT INTO `product_category` VALUES (50, 11, '2026-10-19 02:25:06');
INSERT INTO `product_category` VALUES (50, 12, '2026-10-19 02:20:10');
INSERT INTO `product_category` VALUES (50, 15, '2026-10-14 15:14:10');
INSERT INTO `product_category` VALUES (51, 14, '2026-10-19 02:35:04');

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
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口请求日志' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_oper_log` VALUES (12, '594a74d8-a5c7-463a-bf37-4b9968e4be4d', 'badf2f6a10714aae9ab31273b9a7dc47', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 89, 1, 1, NULL, 'null msg', '2026-10-17 17:14:10.014');
INSERT INTO `sys_oper_log` VALUES (13, '670467fa-3791-4e10-90fd-b1a1b4f55074', '1cc610f1406647c0ab6b2613ec374074', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":14,\"quantity\":1}', 40, 1, 1, NULL, 'null msg', '2026-10-17 17:14:41.599');
INSERT INTO `sys_oper_log` VALUES (14, 'e915d7d4-35bf-4e9c-b6b4-b19406db1476', '985a242344584965aa057878e1e60e2c', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":3,\"userAddressId\":1}}', 138, 1, 1, NULL, 'null msg', '2026-10-17 17:14:45.380');
INSERT INTO `sys_oper_log` VALUES (15, '27e236b0-6ec6-47a6-92b6-d93955134ca6', '830ff7aaa27e423c94e5ddcd08546535', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '::1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 356, 1, 1, NULL, 'null msg', '2026-10-17 17:32:26.227');
INSERT INTO `sys_oper_log` VALUES (16, 'a55ba77a-6c01-4817-8a04-88892847aaab', 'e83e3df3bd814797bf0c2bc992d0fa7d', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '::1', 1, 'USER', '{\"productId\":40,\"quantity\":1}', 19, 1, 1, NULL, 'null msg', '2026-10-17 17:32:38.919');
INSERT INTO `sys_oper_log` VALUES (17, '2c479b9c-fc13-484c-b36c-c67329e2514b', '9c9ecc62d2814b4784b72b59f43015c1', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '::1', 1, 'USER', '{\"productId\":36,\"quantity\":2}', 9, 1, 1, NULL, 'null msg', '2026-10-17 17:32:46.725');
INSERT INTO `sys_oper_log` VALUES (18, '2ebf12cc-4b08-4745-98d4-6be01929c41d', '2349bbe601504676bd2a6e49bfe501bd', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '::1', 1, 'USER', '{\"productId\":21,\"quantity\":1}', 14, 1, 1, NULL, 'null msg', '2026-10-17 17:33:10.331');
INSERT INTO `sys_oper_log` VALUES (19, 'd9073389-83a7-49c8-8c37-e7e7cc315f51', '8db576ddc3b149ed960dab795e31f98a', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '::1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":1,\"userAddressId\":1}}', 50, 1, 1, NULL, 'null msg', '2026-10-17 17:33:19.648');
INSERT INTO `sys_oper_log` VALUES (20, 'f46c69da-3f58-4394-8795-bf2da22813c3', 'e4bd3594c33c4cfd82137dc2f9f11b1d', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 22, 1, 1, NULL, 'null msg', '2026-10-17 18:39:04.691');
INSERT INTO `sys_oper_log` VALUES (21, 'd01c5e73-d85c-48f7-9052-6760d06b9c16', '08f0ea9e7d2e4977a7912e61ca5aa725', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":24,\"quantity\":1}', 15, 1, 1, NULL, 'null msg', '2026-10-17 18:39:13.580');
INSERT INTO `sys_oper_log` VALUES (22, '691b4b58-ba98-4ac0-915d-1e6e9b2a4bc9', 'ab5ee4ec71b3499797ef7af53da7ae9a', 'qi-suo-auth', '认证', '刷新令牌', '/api/auth/jwt/refresh', 'GET', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"refresh_token_key\":\"toke:user:cyj\"}', 15, 1, 1, NULL, 'null msg', '2026-10-17 18:39:17.954');
INSERT INTO `sys_oper_log` VALUES (23, 'd8f08fec-644c-4238-8b7b-9ab18fe797f5', '0efe6ebe08b94cc0ad6ba4c98b8e1c6b', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 9, 1, 1, NULL, 'null msg', '2026-10-17 18:39:29.931');
INSERT INTO `sys_oper_log` VALUES (24, '4617147c-e7be-49d9-b8c6-4874371fb42c', 'c108ba78932c460d98156fb3a9cda0af', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":1}', 3, 0, 0, NULL, 'null msg', '2026-10-17 18:39:45.902');
INSERT INTO `sys_oper_log` VALUES (25, 'e4512f94-14c1-4aef-9add-3bf47199b292', 'fc391d243b924c14a5e1ebf7bdd8cdd4', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":1}', 1, 0, 0, NULL, 'null msg', '2026-10-17 18:39:53.220');
INSERT INTO `sys_oper_log` VALUES (26, '880a769b-23bf-4cd0-990e-b06bc22485b3', '9983ebb283cb4aeeb236bbb7df0ce81a', 'qi-suo-auth', '认证', '刷新令牌', '/api/auth/jwt/refresh', 'GET', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"refresh_token_key\":\"toke:user:cyj\"}', 3, 1, 1, NULL, 'null msg', '2026-10-17 18:39:55.289');
INSERT INTO `sys_oper_log` VALUES (27, 'e8e694cc-bbdc-43cf-9614-a0bb366bff2e', '966f439cb50940e58b59de096497d2e2', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 11, 1, 1, NULL, 'null msg', '2026-10-17 18:40:10.979');
INSERT INTO `sys_oper_log` VALUES (28, 'b6bc662b-4ef9-49c8-b7c9-6af9375f6e84', 'fa6f2c67b4d340ff878c4523018a52df', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":11,\"quantity\":1}', 11, 1, 1, NULL, 'null msg', '2026-10-17 18:40:21.532');
INSERT INTO `sys_oper_log` VALUES (29, 'd3039a05-9181-444c-a055-c0613af3a8b9', 'be17093a1f9f4b948c2f0a5309100a9a', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":1,\"userAddressId\":1}}', 51, 1, 1, NULL, 'null msg', '2026-10-17 18:40:24.084');
INSERT INTO `sys_oper_log` VALUES (30, '1667711d-75c8-4e55-9972-b8a47d3eceaf', 'ad7048298e234173a179b3863e64705e', 'qi-suo-user', '订单', '支付订单', '/api/user/orders/pay', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":21}', 4, 1, 1, NULL, 'null msg', '2026-10-17 18:40:35.590');
INSERT INTO `sys_oper_log` VALUES (31, 'f02d6067-c0f8-41e5-a14f-2eb6358ac767', '9d2f4ca83767414a896a180359a21154', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":1}', 3, 0, 0, NULL, 'null msg', '2026-10-17 18:40:45.989');
INSERT INTO `sys_oper_log` VALUES (32, 'c2f87475-b8b5-4545-bd67-4dda6f081e59', '9abe681e47264fac8f0c26293a00700a', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":1}', 2, 0, 0, NULL, 'null msg', '2026-10-17 18:40:54.610');
INSERT INTO `sys_oper_log` VALUES (33, 'efa8dffc-ae8e-4c2a-802f-3da2134c0a4b', 'bb4b2b35483742dc997459f53eb0bf74', 'qi-suo-auth', '认证', '商家登录', '/api/auth/merchant/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantLoginDTO\":{\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\"}}', 15, 1, 1, NULL, 'null msg', '2026-10-17 18:41:45.448');
INSERT INTO `sys_oper_log` VALUES (34, '79654ca1-0ae7-42d2-a0e6-4793fbdbfa7a', '7719d52f5bc44ac19c79bdd4fd1a5153', 'qi-suo-merchant', '商品', '修改商品', '/api/merchant/product/updateProduct', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"merchantUpdateProductFormDTO\":{\"description\":\"11111\",\"id\":50,\"isNew\":false,\"originalPrice\":0.01,\"price\":0.03,\"productName\":\"1111\",\"stock\":1}}', 16, 1, 1, NULL, 'null msg', '2026-10-17 18:42:03.102');
INSERT INTO `sys_oper_log` VALUES (35, '10080b7a-e4d5-4fe3-9a02-41c6e14c7a66', '98cddefb08ee48d5bc1eead00ab5c4f0', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":50}', 6, 1, 1, NULL, 'null msg', '2026-10-17 18:42:05.737');
INSERT INTO `sys_oper_log` VALUES (36, '91e5c262-2b4c-44f9-aa54-b560353cdb8f', 'f522076d62a542e6acb378d5c53c319e', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":50}', 5, 1, 1, NULL, 'null msg', '2026-10-17 18:42:07.885');
INSERT INTO `sys_oper_log` VALUES (37, '659dadec-dd34-478c-bbc6-3ddc6d7c1aca', 'b11daf234e874e90aec419369c33e0de', 'qi-suo-merchant', '订单', '商家发货', '/api/merchant/orders/status', 'GET', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"orderId\":21}', 9, 1, 1, NULL, 'null msg', '2026-10-17 18:42:17.855');
INSERT INTO `sys_oper_log` VALUES (38, '58f098a9-49cb-444c-84d0-42501a56181a', '6ee2ce4dcfc743d085fed080674668d6', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 15, 1, 1, NULL, 'null msg', '2026-10-17 18:42:46.022');
INSERT INTO `sys_oper_log` VALUES (39, '8fc4f72e-64bb-416f-a990-dc8e846a6607', 'c9533f91388348b386a199d22bcaaf4a', 'qi-suo-auth', '认证', '刷新令牌', '/api/auth/jwt/refresh', 'GET', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"refresh_token_key\":\"toke:user:cyj\"}', 2, 1, 1, NULL, 'null msg', '2026-10-17 18:42:47.998');
INSERT INTO `sys_oper_log` VALUES (40, 'f4ab3418-a1fc-42a6-b0b0-c09d24841e99', '41a284fceecc43a5a380cf3fa161de90', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 8, 1, 1, NULL, 'null msg', '2026-10-17 18:43:04.636');
INSERT INTO `sys_oper_log` VALUES (41, '23c1659b-b131-4849-87df-28cce4e360ea', 'bcf7d68a859448189e1d9e0b3454c18d', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":21}', 1, 0, 0, NULL, 'null msg', '2026-10-17 18:43:17.113');
INSERT INTO `sys_oper_log` VALUES (42, '24877fae-f420-40d4-9005-2c1270fbfa3b', 'e12524f89c024c38a82931edd866d5c8', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":21}', 2, 0, 0, NULL, 'null msg', '2026-10-17 18:43:36.615');
INSERT INTO `sys_oper_log` VALUES (43, 'beb6b92f-30be-45d3-9186-2bc50472e74f', '9ee10d97d2ba406b80d41ef9929dfa55', 'qi-suo-merchant', '店铺信息', '修改店铺信息', '/api/merchant/Information/update', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"updateMerchantInformationFormDTO\":{\"contactEmail\":\"12300456@qq.com\",\"contactName\":\"jycc\",\"contactPhone\":\"13900139000\",\"description\":\"测试店铺000000\",\"merchantId\":1,\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\",\"shopLogoUrl\":\"http://127.0.0.1:9000/qisuo/41ca5c2a-7762-47fa-94e8-42204c88e670.jpg\",\"shopName\":\"测试店铺\",\"updateBy\":1,\"updateTime\":1792234251228}}', 8, 1, 1, NULL, 'null msg', '2026-10-17 18:50:51.256');
INSERT INTO `sys_oper_log` VALUES (44, '0e597fbc-d1c1-4189-8bcb-26088c924480', 'f432140100914a34a8af69598863fd7a', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":21}', 1, 0, 0, NULL, 'null msg', '2026-10-17 18:54:00.138');
INSERT INTO `sys_oper_log` VALUES (45, '777168e8-a62a-422e-b0e2-6a810fdbaddf', 'be460019e90140bcac556a643e29de2b', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":21,\"quantity\":1}', 24, 1, 1, NULL, 'null msg', '2026-10-17 19:18:18.562');
INSERT INTO `sys_oper_log` VALUES (46, 'ecc14550-23a5-4180-bc8b-bd132dc47f03', '50ef5fea406049d990af09e7900c4a0e', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":34,\"quantity\":1}', 17, 1, 1, NULL, 'null msg', '2026-10-17 19:18:25.762');
INSERT INTO `sys_oper_log` VALUES (47, '136eb941-abb3-424b-ac33-78b8d208c380', '8065cee0f5f440fd8eb0e0b7c7ab133b', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":2,\"userAddressId\":1}}', 66, 1, 1, NULL, 'null msg', '2026-10-17 19:18:31.396');
INSERT INTO `sys_oper_log` VALUES (48, '7a9eca06-3d68-4b6d-bfc0-7813744c9cc3', 'ab4b9b62d2b049cea7261db11d08ba41', 'qi-suo-user', '订单', '支付订单', '/api/user/orders/pay', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":22}', 4, 1, 1, NULL, 'null msg', '2026-10-17 19:18:47.310');
INSERT INTO `sys_oper_log` VALUES (49, '1b10c5b4-87d9-49ec-9f39-a22a199c0088', '7df59df16e424de5b27e90eeeac0bad7', 'qi-suo-user', '订单', '确认收货', '/api/user/orders/confirm', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":1}', 4, 1, 1, NULL, 'null msg', '2026-10-17 19:19:02.622');
INSERT INTO `sys_oper_log` VALUES (50, 'f27090bc-74f7-43ba-bded-78509d448bfb', '309a4f805fdf41a180e3d6fc51f8b875', 'qi-suo-auth', '认证', '刷新令牌', '/api/auth/jwt/refresh', 'GET', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"refresh_token_key\":\"toke:user:cyj\"}', 106, 1, 1, NULL, 'null msg', '2026-10-18 23:09:22.827');
INSERT INTO `sys_oper_log` VALUES (51, 'ef5bca8c-e096-4273-82e2-807f4243ac0f', '00dc0f3f4a2c46878c26a6c728cc9f64', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 234, 1, 1, NULL, 'null msg', '2026-10-18 23:09:37.806');
INSERT INTO `sys_oper_log` VALUES (52, 'd176e141-4927-49a0-ba02-e77de17a3363', '41b56a25eee64d2b875e4dedb3ec19e2', 'qi-suo-auth', '认证', '刷新令牌', '/api/auth/jwt/refresh', 'GET', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"refresh_token_key\":\"toke:admin:admin\"}', 5, 1, 1, NULL, 'null msg', '2026-10-18 23:09:53.984');
INSERT INTO `sys_oper_log` VALUES (53, '41f1d4a7-05ef-49ec-b0ab-0f84523b2424', '088bb44d702e47049d416d25d39ef8aa', 'qi-suo-auth', '认证', '刷新令牌', '/api/auth/jwt/refresh', 'GET', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"refresh_token_key\":\"toke:merchant:merchant001\"}', 3, 1, 1, NULL, 'null msg', '2026-10-18 23:10:00.230');
INSERT INTO `sys_oper_log` VALUES (54, '3e791259-36a4-4dfa-9d9f-43efa105d952', '68249534f6ac4498ab7649ce20994f4d', 'qi-suo-auth', '认证', '商家登录', '/api/auth/merchant/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantLoginDTO\":{\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\"}}', 22, 1, 1, NULL, 'null msg', '2026-10-18 23:11:36.511');
INSERT INTO `sys_oper_log` VALUES (55, 'e286e32a-54a4-4c39-847a-daf2b562f262', 'fd8da014415b4fc5b0de6dffcaea322a', 'qi-suo-auth', '认证', '管理员登录', '/api/auth/admin/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"adminLoginDTO\":{\"password\":\"***\",\"username\":\"admin\"}}', 9, 1, 1, NULL, 'null msg', '2026-10-18 23:11:52.074');
INSERT INTO `sys_oper_log` VALUES (56, 'b5fdd8e0-cfcf-4487-84c2-ab4cc7b998a2', '7464842a650c47ca84ed96bbeaa6ca51', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":41,\"quantity\":1}', 39, 1, 1, NULL, 'null msg', '2026-10-18 23:16:00.864');
INSERT INTO `sys_oper_log` VALUES (57, '0d115ee5-a136-4d65-998b-ed23d605525c', 'efe4d872265c402d94a38afa177d5b1e', 'qi-suo-user', '购物车', '删除购物车商品', '/api/user/cart/delete/41', 'DELETE', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":41}', 15, 1, 1, NULL, 'null msg', '2026-10-18 23:16:04.048');
INSERT INTO `sys_oper_log` VALUES (58, 'dad58b80-363d-441f-94a1-a356233f52e8', 'c80dd82edf994add84780892aea65a1a', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":1,\"userAddressId\":2}}', 82, 1, 1, NULL, 'null msg', '2026-10-18 23:16:07.702');
INSERT INTO `sys_oper_log` VALUES (59, '1229ff64-b0a0-48be-9add-17f78f5e4321', '567834e7ac82454693e94736a267937b', 'qi-suo-user', '用户信息', '修改个人信息', '/api/user/userInformationManagement/updateUserInformation', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"userInformationUpdateDTO\":{\"email\":\"1234566@cc.com\",\"imageUrl\":\"http://localhost:9000/test/preview.jpg\",\"password\":\"***\",\"phone\":\"18670465625\",\"username\":\"cyj\"}}', 18, 1, 1, NULL, 'null msg', '2026-10-18 23:17:23.551');
INSERT INTO `sys_oper_log` VALUES (60, '83c24a04-d573-4d8d-9ae0-43699bcec6af', '07efea7d5f1c40dfa37070d56fdab592', 'qi-suo-user', '用户信息', '新增收货地址', '/api/user/userInformationManagement/addUserAddress', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"userAddressDTO\":{\"detailAddress\":\"测试地址3\"}}', 6, 1, 1, NULL, 'null msg', '2026-10-18 23:17:57.993');
INSERT INTO `sys_oper_log` VALUES (61, '9fe6dde4-2c2e-4aaf-90c0-e904c1017684', '54292e3f4f9140bf9c9039597d1e9f93', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":41,\"quantity\":1}', 9, 1, 1, NULL, 'null msg', '2026-10-18 23:20:48.979');
INSERT INTO `sys_oper_log` VALUES (62, 'bc0ca5f5-8cb0-4f91-b64c-253e5385e009', '843f6334c56b42c991c494e2810853dc', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":41,\"quantity\":1}', 12, 1, 1, NULL, 'null msg', '2026-10-18 23:20:49.939');
INSERT INTO `sys_oper_log` VALUES (63, '7b30734a-774d-42c7-98e2-cb7268267721', 'd788e9fd286642d5bd887edf5affa837', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":41,\"quantity\":1}', 12, 1, 1, NULL, 'null msg', '2026-10-18 23:20:51.635');
INSERT INTO `sys_oper_log` VALUES (64, '62c32be3-3c84-4a88-beb6-430325d62834', 'dd42d136677a4ee6b8b44eb9f451153e', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":1}', 8, 1, 1, NULL, 'null msg', '2026-10-18 23:21:05.306');
INSERT INTO `sys_oper_log` VALUES (65, '71137a99-1c42-4c94-ba65-33eddf832d7f', '559e7c34a307437ba06a0ad56268e8c1', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":1}', 12, 1, 1, NULL, 'null msg', '2026-10-18 23:21:16.870');
INSERT INTO `sys_oper_log` VALUES (66, '8778ba31-b325-4055-a3d8-3be3d2d23e9f', '2dbd2e27cdca4e0398540f274ed5229d', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":3}', 12, 1, 1, NULL, 'null msg', '2026-10-18 23:21:21.585');
INSERT INTO `sys_oper_log` VALUES (67, '30cbba22-21c3-445d-ae99-51147b41b77b', '8f61f118b8e7460291ca64560e812007', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":2}', 11, 1, 1, NULL, 'null msg', '2026-10-18 23:21:22.860');
INSERT INTO `sys_oper_log` VALUES (68, '7e487a55-6f9a-4d7b-ba8b-c7fa445d9c52', '38900fe3c7524d4b8a7f898742981555', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":25,\"quantity\":1}', 11, 1, 1, NULL, 'null msg', '2026-10-18 23:21:48.622');
INSERT INTO `sys_oper_log` VALUES (69, '30f5eeec-8946-4014-9c21-d76a78b444e4', 'cfb60e041cde470a9f8219b25b64302b', 'qi-suo-user', '购物车', '加入购物车', '/api/user/cart/add', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":25,\"quantity\":1}', 13, 1, 1, NULL, 'null msg', '2026-10-18 23:21:50.494');
INSERT INTO `sys_oper_log` VALUES (70, '853085cd-9666-43bf-950c-ac85ee7fed37', 'faabddeb9af14dee9758a26be6e4e711', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":25,\"quantity\":3}', 15, 1, 1, NULL, 'null msg', '2026-10-18 23:22:23.401');
INSERT INTO `sys_oper_log` VALUES (71, '3f02402a-33bd-4367-a309-c2d41a79a326', 'af2c1c6bea064ac1b0d583db61489255', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":3}', 10, 1, 1, NULL, 'null msg', '2026-10-18 23:22:24.737');
INSERT INTO `sys_oper_log` VALUES (72, 'bd741f7d-99f5-4b49-bf0d-3347236c9f12', 'e60d34256c1c4833a5d94b6b53e06ff5', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":4}', 11, 1, 1, NULL, 'null msg', '2026-10-18 23:22:25.365');
INSERT INTO `sys_oper_log` VALUES (73, 'fc0bf9ae-f0e5-48db-83e5-91226f3ad85f', 'd54ccdea3ab64a63bef07dffb300abd3', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":5}', 11, 1, 1, NULL, 'null msg', '2026-10-18 23:22:26.152');
INSERT INTO `sys_oper_log` VALUES (74, '98117648-63c2-434c-9684-842e7f7c8dec', 'bd2c5a84d43441bb97d092cb87af1f3b', 'qi-suo-user', '购物车', '修改购物车商品数量', '/api/user/cart/quantity', 'PUT', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"productId\":40,\"quantity\":6}', 12, 1, 1, NULL, 'null msg', '2026-10-18 23:22:29.680');
INSERT INTO `sys_oper_log` VALUES (75, '9ebdedea-cf1c-4265-b87a-93b56117bec6', 'c8848da1a08a4875806b181b9a19bba8', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":2,\"userAddressId\":1}}', 53, 1, 1, NULL, 'null msg', '2026-10-18 23:22:33.784');
INSERT INTO `sys_oper_log` VALUES (76, '8fe2f05b-ca82-4128-914f-39077cbbfd67', '4df74ab91da84d8e8bcffb5761cdb8ab', 'qi-suo-merchant', '商品', '修改商品', '/api/merchant/product/updateProduct', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"merchantUpdateProductFormDTO\":{\"description\":\"11111\",\"id\":50,\"isNew\":true,\"originalPrice\":0.01,\"price\":0.03,\"productName\":\"1111\",\"stock\":2}}', 13, 1, 1, NULL, 'null msg', '2026-10-18 23:24:52.551');
INSERT INTO `sys_oper_log` VALUES (77, '4cae28fe-54b8-4a87-b0a9-732d98a33e6d', '781100eb1de34b1ab63d9656ab1c3948', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":50}', 7, 1, 1, NULL, 'null msg', '2026-10-18 23:36:14.763');
INSERT INTO `sys_oper_log` VALUES (78, '228c2ce3-a5b7-4055-9549-129bd38fb9f0', '88cd48310e204ac2b060fc5e5904e39e', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":50}', 5, 1, 1, NULL, 'null msg', '2026-10-18 23:36:15.580');
INSERT INTO `sys_oper_log` VALUES (79, '053d3ac8-bee1-4862-9ad7-cb71bd3497dc', '7a16b49b1f254681b0186723cc19b05f', 'qi-suo-merchant', '商品', '删除商品', '/api/merchant/product/deleteProduct', 'DELETE', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":41}', 6, 1, 1, NULL, 'null msg', '2026-10-18 23:36:18.478');
INSERT INTO `sys_oper_log` VALUES (80, '20951887-6aef-4ef8-b7b9-5e39204e8db2', '6cec6cd0270d45a3b6a57af18c54340f', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":50}', 5, 1, 1, NULL, 'null msg', '2026-10-18 23:37:28.158');
INSERT INTO `sys_oper_log` VALUES (81, 'c0a45343-b58d-4d57-bc20-b5a093768c4b', 'bc4780e609f64efda9365b439d20ce7d', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":49}', 5, 1, 1, NULL, 'null msg', '2026-10-18 23:37:29.492');
INSERT INTO `sys_oper_log` VALUES (82, 'c093c145-1982-416e-8132-325f8761c4d8', '0be1791b4a304ae3a203961509d54776', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 90, 1, 1, NULL, 'null msg', '2026-10-18 23:41:40.764');
INSERT INTO `sys_oper_log` VALUES (83, 'f36edccc-ca44-49a9-a80c-406de7e8f8e9', 'ac5e416b8ec741c6a81570da5cd56eb8', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 84, 1, 1, NULL, 'null msg', '2026-10-18 23:41:50.184');
INSERT INTO `sys_oper_log` VALUES (84, '9d20317d-a7e6-4466-bd77-80dbb7eb479b', 'd7b36cbe429d43df96ab9a4ec5634974', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 68, 1, 1, NULL, 'null msg', '2026-10-18 23:42:16.536');
INSERT INTO `sys_oper_log` VALUES (85, '2d4988c6-6fa4-43c8-bbb0-78efa96ed7fe', 'f316342d8e2f4933ac8fb2abc4d28c73', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 70, 1, 1, NULL, 'null msg', '2026-10-18 23:42:25.689');
INSERT INTO `sys_oper_log` VALUES (86, '63cf1340-05dd-466e-a8b2-85fe51292103', '6d5d1fb336d0420693424f7130effe5e', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 80, 1, 1, NULL, 'null msg', '2026-10-18 23:43:12.909');
INSERT INTO `sys_oper_log` VALUES (87, '9a0cba16-c2bf-49cc-b955-3cbff3f974c8', 'de151aa9db9e400c8916233085ee0561', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 71, 1, 1, NULL, 'null msg', '2026-10-18 23:44:30.504');
INSERT INTO `sys_oper_log` VALUES (88, 'aab0fc04-6645-488e-89bb-408af06085bf', 'ae5155ea0b47425bbac06f6f8b5e2cdf', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 53555, 1, 1, NULL, 'null msg', '2026-10-18 23:45:49.998');
INSERT INTO `sys_oper_log` VALUES (89, '47f46f96-ef9d-4616-b8bf-59eae2927028', 'c49be5d7e6c241718997bffbd4075209', 'qi-suo-merchant', '店铺信息', '修改店铺信息', '/api/merchant/Information/update', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"updateMerchantInformationFormDTO\":{\"contactEmail\":\"12300456@qq.com\",\"contactName\":\"jycc\",\"contactPhone\":\"13900139000\",\"description\":\"测试店铺000000\",\"merchantId\":1,\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\",\"shopLogoUrl\":\"http://127.0.0.1:9000/qisuo/41ca5c2a-7762-47fa-94e8-42204c88e670.jpg\",\"shopName\":\"测试店铺01\",\"updateBy\":1,\"updateTime\":1792338757751}}', 8, 1, 1, NULL, 'null msg', '2026-10-18 23:52:37.772');
INSERT INTO `sys_oper_log` VALUES (90, 'ec9aaeaf-fba6-456e-a405-65fdb092356f', '722de6038ee8464a9ca16e0286d293c2', 'qi-suo-merchant', '店铺信息', '修改店铺信息', '/api/merchant/Information/update', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"updateMerchantInformationFormDTO\":{\"contactEmail\":\"12300456@qq.com\",\"contactName\":\"jycc\",\"contactPhone\":\"13900131111\",\"description\":\"测试店铺000000\",\"merchantId\":1,\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\",\"shopLogoUrl\":\"http://127.0.0.1:9000/qisuo/41ca5c2a-7762-47fa-94e8-42204c88e670.jpg\",\"shopName\":\"测试店铺01\",\"updateBy\":1,\"updateTime\":1792338775335}}', 3, 1, 1, NULL, 'null msg', '2026-10-18 23:52:55.343');
INSERT INTO `sys_oper_log` VALUES (91, 'cc702124-d411-4375-b00d-84fe6a28bce3', '82769b61d8ae4d748df7c5de92ba68c2', 'qi-suo-merchant', '店铺信息', '修改店铺信息', '/api/merchant/Information/update', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"updateMerchantInformationFormDTO\":{\"contactEmail\":\"12300456@qq.com\",\"contactName\":\"jycc\",\"contactPhone\":\"18670465625\",\"description\":\"测试店铺000000\",\"merchantId\":1,\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\",\"shopLogoUrl\":\"http://127.0.0.1:9000/qisuo/41ca5c2a-7762-47fa-94e8-42204c88e670.jpg\",\"shopName\":\"测试店铺01\",\"updateBy\":1,\"updateTime\":1792338783751}}', 3, 1, 1, NULL, 'null msg', '2026-10-18 23:53:03.760');
INSERT INTO `sys_oper_log` VALUES (92, '9bd80e4e-43d3-4c09-853f-223db58e8d15', '5c5827a04caa4d219e0c17c3882929a5', 'qi-suo-merchant', '店铺信息', '修改店铺信息', '/api/merchant/Information/update', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"updateMerchantInformationFormDTO\":{\"contactEmail\":\"123004156@qq.com\",\"contactName\":\"jycc\",\"contactPhone\":\"18670465625\",\"description\":\"测试店铺000000\",\"merchantId\":1,\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\",\"shopLogoUrl\":\"http://127.0.0.1:9000/qisuo/41ca5c2a-7762-47fa-94e8-42204c88e670.jpg\",\"shopName\":\"测试店铺01\",\"updateBy\":1,\"updateTime\":1792338792167}}', 3, 1, 1, NULL, 'null msg', '2026-10-18 23:53:12.175');
INSERT INTO `sys_oper_log` VALUES (93, '57815337-ba47-486a-97fe-fd9aa2f45b23', 'b67ca55676dd42949011ed9c0da4a3b1', 'qi-suo-file', '图片', '新增主页背景图', '/api/file/image/newHomeBackGroundImage', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"description\":\"a\"}', 254, 1, 1, NULL, 'null msg', '2026-10-18 23:55:24.447');
INSERT INTO `sys_oper_log` VALUES (94, '9805026c-5770-4d50-b1e7-dc7ff9cdfc93', '227ae5e8106e46e4a7b8913fa56f5a81', 'qi-suo-file', '图片', '删除主页背景图', '/api/file/image/deleteHomeBackGroundImage/4235b3da-c9c7-405b-9fdc-df35fd8913bb', 'DELETE', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"imageUid\":\"4235b3da-c9c7-405b-9fdc-df35fd8913bb\"}', 10, 1, 1, NULL, 'null msg', '2026-10-18 23:55:47.489');
INSERT INTO `sys_oper_log` VALUES (95, '6ff54443-5211-4795-92e2-e44b39e8267e', 'a9ebce8c7e954a54a01aec1fcac0852c', 'qi-suo-file', '图片', '新增主页背景图', '/api/file/image/newHomeBackGroundImage', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"description\":\"qq\"}', 86, 1, 1, NULL, 'null msg', '2026-10-18 23:56:13.022');
INSERT INTO `sys_oper_log` VALUES (96, '1937e767-aeb8-40c5-93e5-c6e7f21fcb83', '1ddfc9cb6b324d0fb09ae387befd2247', 'qi-suo-file', '图片', '设为当前背景图', '/api/file/image/setNowHomeBackGroundImage/12507859-f01b-4c21-9c35-bc152379204a', 'PATCH', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"uid\":\"12507859-f01b-4c21-9c35-bc152379204a\"}', 8, 1, 1, NULL, 'null msg', '2026-10-18 23:57:01.928');
INSERT INTO `sys_oper_log` VALUES (97, '8e3946ab-ef08-4a58-88be-1965e971bf4e', 'ca034489290c4a708fd4cd1d233c13a2', 'qi-suo-file', '图片', '设为当前背景图', '/api/file/image/setNowHomeBackGroundImage/25253dda-2a25-4a79-ae83-fa6b5450e81e', 'PATCH', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"uid\":\"25253dda-2a25-4a79-ae83-fa6b5450e81e\"}', 2, 1, 1, NULL, 'null msg', '2026-10-18 23:57:04.089');
INSERT INTO `sys_oper_log` VALUES (98, '09860b23-8526-42dd-adc4-3c197fc55dea', '454e0e1dd4b543ed9c3a8fefe60fbaa6', 'qi-suo-file', '图片', '删除主页背景图', '/api/file/image/deleteHomeBackGroundImage/12ce8d27-2450-4cfc-8687-6e7e7adce710', 'DELETE', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"imageUid\":\"12ce8d27-2450-4cfc-8687-6e7e7adce710\"}', 6, 1, 1, NULL, 'null msg', '2026-10-18 23:57:13.965');
INSERT INTO `sys_oper_log` VALUES (99, '47eec10d-96b0-49be-b6ef-eb63a9151c84', '2b599b81dfb94b09ae29610cb076154c', 'qi-suo-file', '图片', '新增主页背景图', '/api/file/image/newHomeBackGroundImage', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"description\":\"zz\"}', 85, 1, 1, NULL, 'null msg', '2026-10-18 23:57:38.197');
INSERT INTO `sys_oper_log` VALUES (100, 'ea11254e-850e-4ee1-a2ba-3f7c2ba64da1', 'e7afe9b35f7a4dd19cbb47495dd34c60', 'qi-suo-file', '图片', '新增主页背景图', '/api/file/image/newHomeBackGroundImage', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"description\":\"啊啊\"}', 80, 1, 1, NULL, 'null msg', '2026-10-18 23:58:17.783');
INSERT INTO `sys_oper_log` VALUES (101, '46b5a4b8-e2b9-4718-95cd-845564bf133b', 'f826d90727b64b5f91f983dda5bef0bc', 'qi-suo-file', '图片', '新增轮播图', '/api/file/image/newCarouselImage', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"description\":\"test\"}', 257, 1, 1, NULL, 'null msg', '2026-10-19 00:07:25.099');
INSERT INTO `sys_oper_log` VALUES (102, '13c1e72f-d917-427b-8d4d-7bdf77987fc3', '320116a2e01b4db1b67398f96e175804', 'qi-suo-file', '图片', '禁用轮播图', '/api/file/image/setCarouselImageNotEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"imageUid\":\"屏幕截图 2026-10-18 224633.pngfd279b13-1a94-4b36-be2f-5478dfb22f13\"}', 24, 1, 1, NULL, 'null msg', '2026-10-19 00:07:40.224');
INSERT INTO `sys_oper_log` VALUES (103, 'b1782755-0edd-44b6-862c-d2887e79a064', '41fb73810c024ce8b992c444bf4c2297', 'qi-suo-admin', '分类', '新增分类', '/api/admin/category/newCategory', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"newCategoryDTO\":{\"categoryType\":1,\"level\":1,\"name\":\"阿斯顿\",\"parentId\":1111111}}', 24, 1, 1, NULL, 'null msg', '2026-10-19 00:20:18.797');
INSERT INTO `sys_oper_log` VALUES (104, 'a8006411-9829-49dc-b78b-f918ccc18b38', 'fd0ee9b6347f487d99867dc8aa02ed8c', 'qi-suo-admin', '分类', '启用或禁用分类', '/api/admin/category/setCategoryEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":29,\"pageNum\":1}', 8, 1, 1, NULL, 'null msg', '2026-10-19 00:21:59.716');
INSERT INTO `sys_oper_log` VALUES (105, '67c3e9b3-1120-4315-9af1-13985470d186', '2e0be78b8f874145be03a4783e5078bc', 'qi-suo-admin', '分类', '新增分类', '/api/admin/category/newCategory', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"newCategoryDTO\":{\"categoryType\":1,\"level\":2,\"name\":\"阿斯顿\",\"parentId\":0}}', 8, 1, 1, NULL, 'null msg', '2026-10-19 00:24:03.266');
INSERT INTO `sys_oper_log` VALUES (106, '01d4d390-8054-4cbd-81c2-ae04ca59322f', '28f1e72d684e4c01ba1608865d1708f1', 'qi-suo-admin', '分类', '新增分类', '/api/admin/category/newCategory', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"newCategoryDTO\":{\"categoryType\":1,\"level\":2,\"name\":\"阿斯顿\",\"parentId\":29}}', 8, 1, 1, NULL, 'null msg', '2026-10-19 00:24:29.396');
INSERT INTO `sys_oper_log` VALUES (107, 'febb863b-ee55-4d76-89bc-c98c42f5f0ce', '6a481d6fd7a24a2fb87110ed03e66a53', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 318, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:00:01.591');
INSERT INTO `sys_oper_log` VALUES (108, 'c81ccaa4-c8c2-49da-804f-c65d8427afe9', 'b530f9073f8e4255a0039e9c913b7c5d', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 5, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:00:08.861');
INSERT INTO `sys_oper_log` VALUES (109, '7f3dc6ab-4ebc-4932-8f1d-4a528242d9b0', 'c7f07b9f8d5c4bb4a830d0d9311a6c8c', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 7, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:00:58.843');
INSERT INTO `sys_oper_log` VALUES (110, '9f1a4786-0135-4af0-8925-030989c0152c', 'f1726e4aa39a437b99f4b43b92bcd601', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 5, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:01:12.187');
INSERT INTO `sys_oper_log` VALUES (111, '73b56b37-172f-45fa-9560-680edee8f55a', 'aced958a2e8c4f86b4bc64baaefb9538', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 5, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:01:49.181');
INSERT INTO `sys_oper_log` VALUES (112, 'bfbf77a7-e475-4d59-8c37-c3e50421e8fe', 'f89f327e812f43929ed394b85a712433', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 5, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:02:53.543');
INSERT INTO `sys_oper_log` VALUES (113, '1ee20005-e1fa-426e-86a3-496dd78375ce', '1356905e82d84330a64e533107f7d114', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 6, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:03:19.005');
INSERT INTO `sys_oper_log` VALUES (114, 'ee8c6754-6c7a-4393-9f5c-27b6042016d9', 'bc72f4e24a88422c9e7c7677b05e792e', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 18319, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:06:31.478');
INSERT INTO `sys_oper_log` VALUES (115, '649c6c41-908b-4f66-b5fc-d204f158baa5', 'bdab47ab6aeb4cadb073bec7827335f5', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 9039, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:07:07.424');
INSERT INTO `sys_oper_log` VALUES (116, '1e397b18-e93c-4454-8ff1-dc29736f91e9', '5b4144e0ec26496fa08136982231e570', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 41749, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:10:48.608');
INSERT INTO `sys_oper_log` VALUES (117, 'b8c23f67-3d9d-4eac-8358-2d6f6f8c13a1', '235523d83acc48b5a9fb15e04d126422', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 3400, 0, 0, 'java.lang.IllegalArgumentException', 'You have entered a password with no PasswordEncoder. If that is your intent, it should be prefixed with `{noop}`.', '2026-10-19 02:11:53.694');
INSERT INTO `sys_oper_log` VALUES (118, 'f2a2bd7e-7b11-47e0-a3c3-9f68d4737d4a', '556ba450ba4644aeaf7da23d0ba5241c', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 41, 1, 1, NULL, 'null msg', '2026-10-19 02:13:19.111');
INSERT INTO `sys_oper_log` VALUES (119, 'fc333632-b8f1-43ee-8780-d1ebf60d1a12', '521622d35fb943e7b1e3b4ef00d9efa8', 'qi-suo-user', '用户信息', '修改密码', '/api/user/userInformationManagement/changePassword', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"changePasswordDTO\":{\"newPassword\":\"***\",\"oldPassword\":\"***\"}}', 86, 1, 1, NULL, 'null msg', '2026-10-19 02:14:07.492');
INSERT INTO `sys_oper_log` VALUES (120, '06414f25-6f33-4d16-82ca-63b4f458166a', '1fb2787b02cb45abaa598f6c3d9140ed', 'qi-suo-user', '用户信息', '修改密码', '/api/user/userInformationManagement/changePassword', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"changePasswordDTO\":{\"newPassword\":\"***\",\"oldPassword\":\"***\"}}', 136, 1, 1, NULL, 'null msg', '2026-10-19 02:14:33.696');
INSERT INTO `sys_oper_log` VALUES (121, 'ac2f4ded-3d59-4878-8d2a-46bd74a5b16c', '7820994ead864092a3bfa2e92d1ee0b5', 'qi-suo-user', '用户信息', '修改密码', '/api/user/userInformationManagement/changePassword', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"changePasswordDTO\":{\"newPassword\":\"***\",\"oldPassword\":\"***\"}}', 68, 0, 0, NULL, 'null msg', '2026-10-19 02:14:42.898');
INSERT INTO `sys_oper_log` VALUES (122, '1289ce41-12ef-44cd-bcb1-2b27f6dea98d', '341327297bb948cfbc15c63fa0055bfc', 'qi-suo-user', '用户信息', '修改密码', '/api/user/userInformationManagement/changePassword', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"changePasswordDTO\":{\"newPassword\":\"***\",\"oldPassword\":\"***\"}}', 139, 1, 1, NULL, 'null msg', '2026-10-19 02:14:46.665');
INSERT INTO `sys_oper_log` VALUES (123, '2fc2dcbf-c5c7-41f7-91bb-25119d35a084', '03f87e72813c40608d4e532ffca6eb91', 'qi-suo-user', '订单', '创建订单', '/api/user/cart/createOrder', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"createOrderDTO\":{\"merchantId\":1,\"userAddressId\":1}}', 87, 1, 1, NULL, 'null msg', '2026-10-19 02:15:23.143');
INSERT INTO `sys_oper_log` VALUES (124, '6b0873ab-3fd7-4d67-b2e3-0a274c377e9b', '9ed108a79920405591ae57aefc8d2899', 'qi-suo-user', '订单', '支付订单', '/api/user/orders/pay', 'GET', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"orderId\":25}', 6, 1, 1, NULL, 'null msg', '2026-10-19 02:15:28.099');
INSERT INTO `sys_oper_log` VALUES (125, '2805be9e-9791-4ffa-9e0f-dbdc02fb8b83', '20f2febcf4ab4d9cb3f20b60a95fb5ba', 'qi-suo-auth', '认证', '退出登录', '/api/auth/logout', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '', 6, 1, 1, NULL, 'null msg', '2026-10-19 02:15:41.184');
INSERT INTO `sys_oper_log` VALUES (126, '80edf4b6-8072-4ebd-bcf5-ec94907687d5', 'd794dd3876704d1c9c502de3b7247036', 'qi-suo-auth', '认证', '商家登录', '/api/auth/merchant/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantLoginDTO\":{\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\"}}', 338, 0, 0, 'org.springframework.security.authentication.BadCredentialsException', '用户名或密码错误', '2026-10-19 02:16:16.649');
INSERT INTO `sys_oper_log` VALUES (127, 'e2ef570e-4755-438d-a577-747ce56b8ec9', 'd775f8f57fd5429bb57443fbc7c3ce8e', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 111, 1, 1, NULL, 'null msg', '2026-10-19 02:17:12.584');
INSERT INTO `sys_oper_log` VALUES (128, 'a11124f4-6394-4d9b-a4ae-7a97b47e4b01', '4f05e2040afa4b0887f0db74f3d4bf5b', 'qi-suo-user', '用户信息', '修改密码', '/api/user/userInformationManagement/changePassword', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"changePasswordDTO\":{\"newPassword\":\"***\",\"oldPassword\":\"***\"}}', 136, 1, 1, NULL, 'null msg', '2026-10-19 02:17:27.958');
INSERT INTO `sys_oper_log` VALUES (129, '2b50bb39-6334-490c-8384-0564682c3304', '0b3a3b2ea27b440caa688e8a185d9d38', 'qi-suo-auth', '认证', '退出登录', '/api/auth/logout', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '', 4, 1, 1, NULL, 'null msg', '2026-10-19 02:17:30.795');
INSERT INTO `sys_oper_log` VALUES (130, '7b927265-26d9-4901-a1da-6428f301503d', '4e00f8459bfa44e1a10667f2b27324d7', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 72, 0, 0, 'org.springframework.security.authentication.BadCredentialsException', '用户名或密码错误', '2026-10-19 02:17:43.939');
INSERT INTO `sys_oper_log` VALUES (131, '3512f064-cd24-422a-90bb-d6f8119d65ea', 'b6e4e58bb56b4e85ba4888b02853c443', 'qi-suo-auth', '认证', '用户登录', '/api/auth/user/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"userLoginDTO\":{\"password\":\"***\",\"username\":\"cyj\"}}', 78, 1, 1, NULL, 'null msg', '2026-10-19 02:17:55.739');
INSERT INTO `sys_oper_log` VALUES (132, '735d96e5-1cd8-4cd3-82d0-3cfd17e46f0a', '476949fcc5a44fbcbf7acd30ef0fe835', 'qi-suo-user', '用户信息', '修改密码', '/api/user/userInformationManagement/changePassword', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '{\"changePasswordDTO\":{\"newPassword\":\"***\",\"oldPassword\":\"***\"}}', 135, 1, 1, NULL, 'null msg', '2026-10-19 02:18:20.448');
INSERT INTO `sys_oper_log` VALUES (133, '86c4d2f0-2ee9-4196-a3fc-c10fd02cd577', 'e52a035f096e46b7877c2f262679a72a', 'qi-suo-auth', '认证', '退出登录', '/api/auth/logout', 'POST', '0:0:0:0:0:0:0:1', 1, 'USER', '', 3, 1, 1, NULL, 'null msg', '2026-10-19 02:18:30.222');
INSERT INTO `sys_oper_log` VALUES (134, '26ddb6b3-128f-4427-a023-a2963742433a', '850ec941a5904e90bf000d937c6584ad', 'qi-suo-auth', '认证', '商家登录', '/api/auth/merchant/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantLoginDTO\":{\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\"}}', 3, 0, 0, 'org.springframework.security.authentication.BadCredentialsException', '用户名或密码错误', '2026-10-19 02:18:59.469');
INSERT INTO `sys_oper_log` VALUES (135, 'b9804e6e-3ce5-4e3d-ab67-0f4748569f63', '86bfa2b5a9a84ecd92797e3601ef6dee', 'qi-suo-auth', '认证', '商家登录', '/api/auth/merchant/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantLoginDTO\":{\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\"}}', 9, 1, 1, NULL, 'null msg', '2026-10-19 02:19:34.758');
INSERT INTO `sys_oper_log` VALUES (136, '7e1ae1d8-b3c0-4d15-b690-d4aa6ea27738', '18d663bd502848f08bb07a60f8b48ba4', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":50,\"status\":1}', 14, 1, 1, NULL, 'null msg', '2026-10-19 02:19:47.021');
INSERT INTO `sys_oper_log` VALUES (137, '371117ce-8706-4f51-b864-a32caa2e27b3', '8cbe567ffcbf4e3591237231eddf12f2', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"productId\":49,\"status\":1}', 6, 1, 1, NULL, 'null msg', '2026-10-19 02:19:48.015');
INSERT INTO `sys_oper_log` VALUES (138, 'd783cb33-70c3-4998-9152-22e0da024e63', 'cd33c726543b407db37a4aa51795f14b', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '', 129, 1, 1, NULL, 'null msg', '2026-10-19 02:19:57.934');
INSERT INTO `sys_oper_log` VALUES (139, 'a4b6bba2-dae3-42a8-bdb3-abcf6f51b3cb', '157899c2b3504fc68668a5554fd97ff1', 'qi-suo-merchant', '商品', '新增商品分类', '/api/merchant/product/newProductCategory', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductCategoryDTO\":{\"categoryId\":12,\"productId\":50}}', 12, 1, 1, NULL, 'null msg', '2026-10-19 02:20:10.364');
INSERT INTO `sys_oper_log` VALUES (140, 'dbc71168-d8a3-4fd3-91f6-549ecba7a9d4', '60f841a0a3cf4dd4a299b0652ecd1b20', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"productId\":50}}', 143, 0, 0, NULL, 'null msg', '2026-10-19 02:20:29.347');
INSERT INTO `sys_oper_log` VALUES (141, '7c868021-e427-43b5-930c-927b88c69a5d', '8df2642ad68144438897b8a735ff7a8a', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"productId\":50}}', 3, 0, 0, NULL, 'null msg', '2026-10-19 02:20:37.368');
INSERT INTO `sys_oper_log` VALUES (142, '1ac35c44-e2d6-4825-a9f5-3ab113aad5e8', '4d0961d9ba38429b9617a19e1f22d201', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"productId\":50}}', 3, 0, 0, NULL, 'null msg', '2026-10-19 02:20:49.370');
INSERT INTO `sys_oper_log` VALUES (143, '8f3ba26a-9461-4534-9e3f-3877711d514b', '1f93a97144554241a94a85d3973855a8', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"productId\":50}}', 3, 0, 0, NULL, 'null msg', '2026-10-19 02:21:48.370');
INSERT INTO `sys_oper_log` VALUES (144, '6a8733a2-ddb7-4ac0-bce1-3fac44432207', '11822076aae34c8badfb8081fd916425', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"productId\":50}}', 3, 0, 0, NULL, 'null msg', '2026-10-19 02:24:04.894');
INSERT INTO `sys_oper_log` VALUES (145, '3e35a7d0-3039-42b0-b7d4-81106a2a01a3', 'aabacadbe3484dd6b4f689387a8c9049', 'qi-suo-merchant', '商品', '新增商品分类', '/api/merchant/product/newProductCategory', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductCategoryDTO\":{\"categoryId\":11,\"productId\":50}}', 6, 1, 1, NULL, 'null msg', '2026-10-19 02:25:06.366');
INSERT INTO `sys_oper_log` VALUES (146, '2ce0a36d-9c2d-4559-b99b-8fd27831e002', 'f0c76e2e3f0144048c690e55336c07d9', 'qi-suo-merchant', '商品', '修改商品', '/api/merchant/product/updateProduct', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"merchantUpdateProductFormDTO\":{\"description\":\"11111\",\"id\":50,\"isHot\":false,\"isNew\":false,\"originalPrice\":0.01,\"price\":0.03,\"productImageUrl\":\"http://127.0.0.1:9000/qisuo/a7c739ce-7954-4ad1-9670-54056200b45f.jpg\",\"productName\":\"1111\",\"stock\":2}}', 9, 1, 1, NULL, 'null msg', '2026-10-19 02:25:10.871');
INSERT INTO `sys_oper_log` VALUES (147, 'ec191620-aa63-44a4-b66b-23c7f0a997a5', 'e7cf9aa2065b4566a9bdae897dc4657f', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '192.168.56.1', -1, 'system', '', 94, 1, 1, NULL, 'null msg', '2026-10-19 02:25:31.536');
INSERT INTO `sys_oper_log` VALUES (148, 'f86cbab1-0ec2-4786-9544-bf9ce2d05a16', 'e7cf9aa2065b4566a9bdae897dc4657f', 'qi-suo-merchant', '店铺信息', '上传店铺Logo', '/api/merchant/Information/uploadLogo', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 178, 1, 1, NULL, 'null msg', '2026-10-19 02:25:31.465');
INSERT INTO `sys_oper_log` VALUES (149, '4e3672d2-3627-4657-9281-fd72ede7561c', '45100320bd27438dbf3e69a2eacdde1e', 'qi-suo-auth', '认证', '管理员登录', '/api/auth/admin/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"adminLoginDTO\":{\"password\":\"***\",\"username\":\"admin\"}}', 20, 1, 1, NULL, 'null msg', '2026-10-19 02:25:50.258');
INSERT INTO `sys_oper_log` VALUES (150, '7caf8b2b-2b20-4625-80ff-8f45364db3f7', '9fa16697a22f43d49bf6598453ad79b0', 'qi-suo-admin', '管理员', '禁用管理员', '/api/admin/adminManagement/setAdminNotEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"adminId\":2,\"pageNum\":1}', 32, 1, 1, NULL, 'null msg', '2026-10-19 02:26:33.717');
INSERT INTO `sys_oper_log` VALUES (151, 'a7fc1431-6059-4fd8-9f96-bc495fdfb617', '6873593548d847eab5d087094ceb634f', 'qi-suo-admin', '管理员', '启用管理员', '/api/admin/adminManagement/setAdminIsEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"adminId\":2,\"pageNum\":1}', 6, 1, 1, NULL, 'null msg', '2026-10-19 02:26:34.913');
INSERT INTO `sys_oper_log` VALUES (152, '0ff15292-085e-40ff-9687-e0f9fe23a524', 'b68ec3a0fa9d4d479ed3c702b9c68658', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '192.168.56.1', -1, 'system', '{\"productId\":50,\"status\":0}', 7, 1, 1, NULL, 'null msg', '2026-10-19 02:27:03.466');
INSERT INTO `sys_oper_log` VALUES (153, '4b4a2a3c-8ae5-45b0-970a-b70567e63d5d', 'f64cfb9c878a402d8c3832f9987ddfc4', 'qi-suo-merchant', '商品', '上下架商品', '/api/merchant/product/setStatus', 'PUT', '192.168.56.1', -1, 'system', '{\"productId\":50,\"status\":1}', 5, 1, 1, NULL, 'null msg', '2026-10-19 02:27:04.325');
INSERT INTO `sys_oper_log` VALUES (154, '077f7ead-ce7e-4b1d-9030-fb056ac6d3d7', '48113abd544e436c95acd6ed3f18ebe3', 'qi-suo-admin', '分类', '新增分类', '/api/admin/category/newCategory', 'POST', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"newCategoryDTO\":{\"categoryType\":1,\"level\":1,\"name\":\"ceshi\",\"parentId\":0}}', 37, 1, 1, NULL, 'null msg', '2026-10-19 02:27:48.927');
INSERT INTO `sys_oper_log` VALUES (155, 'c4dc3c4e-7ac7-4b7d-9860-0ed9c279644b', 'bc2e9c6db53e45fe9ee735d1d1899114', 'qi-suo-admin', '分类', '启用分类', '/api/admin/category/setCategoryEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"id\":32,\"pageNum\":1}', 8, 1, 1, NULL, 'null msg', '2026-10-19 02:27:56.796');
INSERT INTO `sys_oper_log` VALUES (156, 'e9153340-ac82-48a9-b539-fc910fb8c2ac', '80b7646e440e49bfaa474910cef25bea', 'qi-suo-file', '图片', '禁用主页背景图', '/api/file/image/setHomeBackGroundImageNotEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"imageUid\":\"12507859-f01b-4c21-9c35-bc152379204a\",\"pageNum\":1}', 31, 1, 1, NULL, 'null msg', '2026-10-19 02:28:28.394');
INSERT INTO `sys_oper_log` VALUES (157, '076c6da3-a46f-47a7-b14b-533d0c777ace', 'b8320bdda18f40788f62f55b61724d4a', 'qi-suo-file', '图片', '启用主页背景图', '/api/file/image/setHomeBackGroundImageEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"imageUid\":\"12507859-f01b-4c21-9c35-bc152379204a\",\"pageNum\":1}', 7, 1, 1, NULL, 'null msg', '2026-10-19 02:28:33.709');
INSERT INTO `sys_oper_log` VALUES (158, '9cc1d87c-5d4e-4b92-95e8-263267d8b600', '0f1cf6e80c434c6f8a4a08efabebb7e3', 'qi-suo-file', '图片', '启用轮播图', '/api/file/image/setCarouselImageEnabled', 'PUT', '0:0:0:0:0:0:0:1', 1, 'SystemADMIN', '{\"imageUid\":\"屏幕截图 2026-10-18 224633.pngfd279b13-1a94-4b36-be2f-5478dfb22f13\"}', 128, 1, 1, NULL, 'null msg', '2026-10-19 02:28:48.030');
INSERT INTO `sys_oper_log` VALUES (159, '27dc6832-cf4c-482a-bb99-5f91b5eb5cf8', '586f1f2d50444025aa405745fa26ccb4', 'qi-suo-auth', '认证', '退出登录', '/api/auth/logout', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '', 4, 1, 1, NULL, 'null msg', '2026-10-19 02:29:15.408');
INSERT INTO `sys_oper_log` VALUES (160, 'c99c2d25-9feb-433a-b4d6-470b6615eba1', '254a3bc6616c44e6bb85cf144a67b7b9', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '', 85, 1, 1, NULL, 'null msg', '2026-10-19 02:29:22.979');
INSERT INTO `sys_oper_log` VALUES (161, 'fc067091-a023-4e2f-9db1-e007213c8a8b', 'eb496e54483449d2950a8f862618cca5', 'qi-suo-auth', '认证', '商家注册', '/api/auth/merchant/register', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantRegisterDTO\":{\"contactEmail\":\"11111@qq.com\",\"contactName\":\"jyc\",\"contactPhone\":\"18877465555\",\"description\":\"test......\",\"merchantName\":\"测试店铺\",\"merchantPassword\":\"***\",\"shopLogoUrl\":\"http://127.0.0.1:9000/qisuo/61f7e934-b6c9-43d8-908a-ca8dfca9d192.png\",\"shopName\":\"测试\"}}', 149, 0, 0, NULL, 'null msg', '2026-10-19 02:31:01.675');
INSERT INTO `sys_oper_log` VALUES (162, '56ad7e2b-4338-42e7-bbe4-38dc019529a0', '620ff3e78fe943a99551fab00fcf264b', 'qi-suo-auth', '认证', '商家登录', '/api/auth/merchant/login', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '{\"merchantLoginDTO\":{\"merchantName\":\"merchant001\",\"merchantPassword\":\"***\"}}', 7, 1, 1, NULL, 'null msg', '2026-10-19 02:33:48.245');
INSERT INTO `sys_oper_log` VALUES (163, '56c06f3e-6588-4c8f-a74a-0650788abd84', '0893553ffec14aad90072320c6bb6d73', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"attributeValueId\":9,\"productId\":50}}', 14, 1, 1, NULL, 'null msg', '2026-10-19 02:34:17.968');
INSERT INTO `sys_oper_log` VALUES (164, '6c02e2f9-076a-447d-ba78-dbf3edb9f75f', 'f3f947cecbf64919bcb04a49bb08173e', 'qi-suo-merchant', '商品', '新增商品属性值', '/api/merchant/product/newProductAttributeValue', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductAttributeValueDTO\":{\"attributeValueId\":10,\"productId\":50}}', 4, 1, 1, NULL, 'null msg', '2026-10-19 02:34:18.000');
INSERT INTO `sys_oper_log` VALUES (165, '88ef698d-cebd-4133-a068-977d5f54b045', '6018a9690491443aa2b6ccb952685762', 'qi-suo-merchant', '商品', '修改商品', '/api/merchant/product/updateProduct', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"merchantUpdateProductFormDTO\":{\"description\":\"11111\",\"id\":50,\"isHot\":false,\"isNew\":false,\"originalPrice\":0.01,\"price\":0.03,\"productImageUrl\":\"http://127.0.0.1:9000/qisuo/a7c739ce-7954-4ad1-9670-54056200b45f.jpg\",\"productName\":\"1111\",\"stock\":2}}', 16, 1, 1, NULL, 'null msg', '2026-10-19 02:34:20.093');
INSERT INTO `sys_oper_log` VALUES (166, '2add4dd0-f4a5-4998-bc33-865d3c6c1258', '4ba0bb404ead4a449b663c2dad06d612', 'qi-suo-file', '文件', '上传文件', '/api/file/upload', 'POST', '0:0:0:0:0:0:0:1', -1, 'ROLE_ANONYMOUS', '', 77, 1, 1, NULL, 'null msg', '2026-10-19 02:34:45.067');
INSERT INTO `sys_oper_log` VALUES (167, '3912996a-89fb-4766-b14f-b3d4eafb8b3d', '5957bd95442b40f1887cff948b610c54', 'qi-suo-merchant', '商品', '新增商品', '/api/merchant/product/newProduct', 'POST', '0:0:0:0:0:0:0:1', 1, 'Merchant', '{\"newProductDTO\":{\"attributeAndValues\":[50],\"categoryIds\":[14],\"description\":\"测试\",\"isHot\":true,\"isNew\":false,\"originalPrice\":0.04,\"price\":0.01,\"productImageUrl\":\"http://127.0.0.1:9000/qisuo/edb40b61-8530-4722-9fc5-42552904b172.png\",\"productName\":\"测试商品\",\"stock\":999}}', 30, 1, 1, NULL, 'null msg', '2026-10-19 02:35:04.279');

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
INSERT INTO `user` VALUES (1, 'cyj', '{bcrypt}$2a$10$P1DyFhIiIkuu5W/n.Xo/3emLLpFCNOLdfHTZY/6rQ0rwb2o2x6huW', 'http://localhost:9000/test/preview.jpg', '18670465625', '1234566@cc.com', 0, '2026-01-15 11:27:42');
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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (1, 1, '测试地址1', 0, '2026-03-12 21:42:27', '2026-04-13 13:53:46');
INSERT INTO `user_address` VALUES (2, 1, '测试地址2', 0, '2026-03-15 03:37:37', '2026-03-15 03:37:37');
INSERT INTO `user_address` VALUES (4, 2, 'testtt', 0, '2026-10-16 16:27:30', '2026-10-16 16:27:30');
INSERT INTO `user_address` VALUES (5, 1, '测试地址3', 0, '2026-10-18 23:17:57', '2026-10-18 23:17:57');

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
