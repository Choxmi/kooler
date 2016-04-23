-- phpMyAdmin SQL Dump
-- version 4.3.8
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 25, 2015 at 02:07 AM
-- Server version: 5.5.40-36.1
-- PHP Version: 5.4.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `kimutai_woo`
--

-- --------------------------------------------------------

--
-- Table structure for table `banners`
--

CREATE TABLE IF NOT EXISTS `banners` (
  `banner_id` int(9) unsigned NOT NULL,
  `banner_collection_id` int(9) unsigned NOT NULL,
  `name` varchar(128) NOT NULL,
  `enable_date` date NOT NULL,
  `disable_date` date NOT NULL,
  `image` varchar(64) NOT NULL,
  `link` varchar(128) DEFAULT NULL,
  `new_window` tinyint(1) NOT NULL DEFAULT '0',
  `sequence` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `banners`
--

INSERT INTO `banners` (`banner_id`, `banner_collection_id`, `name`, `enable_date`, `disable_date`, `image`, `link`, `new_window`, `sequence`) VALUES
(1, 1, 'Earrings', '0000-00-00', '0000-00-00', 'banner1.jpg', '', 0, 0),
(2, 1, 'Shoe Banner', '0000-00-00', '0000-00-00', 'banner2.jpg', '', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `countries`
--

CREATE TABLE IF NOT EXISTS `countries` (
  `id` int(9) unsigned NOT NULL,
  `sequence` int(11) NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL,
  `iso_code_2` varchar(2) NOT NULL,
  `iso_code_3` varchar(3) NOT NULL,
  `address_format` text NOT NULL,
  `zip_required` int(1) NOT NULL DEFAULT '0',
  `status` int(1) NOT NULL DEFAULT '1',
  `tax` float(10,2) NOT NULL DEFAULT '0.00'
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `countries`
--

INSERT INTO `countries` (`id`, `sequence`, `name`, `iso_code_2`, `iso_code_3`, `address_format`, `zip_required`, `status`, `tax`) VALUES
(1, 0, 'Kenya', 'KE', 'KEN', '', 0, 1, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `country_zones`
--

CREATE TABLE IF NOT EXISTS `country_zones` (
  `id` int(11) unsigned NOT NULL,
  `country_id` int(9) unsigned NOT NULL,
  `code` varchar(32) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  `tax` float(10,2) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `country_zones`
--

INSERT INTO `country_zones` (`id`, `country_id`, `code`, `name`, `status`, `tax`) VALUES
(1, 1, '1', 'Nairobi', 1, 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE IF NOT EXISTS `customers` (
  `id` int(9) unsigned NOT NULL,
  `firstname` varchar(32) NOT NULL,
  `lastname` varchar(32) NOT NULL,
  `email` varchar(128) NOT NULL,
  `email_subscribe` tinyint(1) NOT NULL DEFAULT '0',
  `phone` varchar(32) NOT NULL,
  `company` varchar(128) NOT NULL,
  `default_billing_address` int(9) NOT NULL,
  `default_shipping_address` int(9) NOT NULL,
  `ship_to_bill_address` enum('false','true') NOT NULL DEFAULT 'true',
  `password` varchar(40) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `group_id` int(11) NOT NULL DEFAULT '1',
  `confirmed` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
  `id` int(10) unsigned NOT NULL,
  `order_number` varchar(60) NOT NULL,
  `customer_id` int(9) unsigned DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `ordered_on` datetime NOT NULL,
  `shipped_on` datetime NOT NULL,
  `tax` float(10,2) NOT NULL,
  `total` float(10,2) NOT NULL,
  `subtotal` float(10,2) NOT NULL,
  `gift_card_discount` float(10,2) NOT NULL,
  `coupon_discount` float(10,2) NOT NULL,
  `shipping` float(10,2) NOT NULL,
  `shipping_notes` text NOT NULL,
  `shipping_method` tinytext NOT NULL,
  `notes` text,
  `payment_info` text NOT NULL,
  `referral` text NOT NULL,
  `company` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `phone` varchar(40) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `ship_company` varchar(255) DEFAULT NULL,
  `ship_firstname` varchar(255) DEFAULT NULL,
  `ship_lastname` varchar(255) DEFAULT NULL,
  `ship_email` varchar(255) DEFAULT NULL,
  `ship_phone` varchar(40) DEFAULT NULL,
  `ship_address1` varchar(255) DEFAULT NULL,
  `ship_address2` varchar(255) DEFAULT NULL,
  `ship_city` varchar(255) DEFAULT NULL,
  `ship_zip` varchar(11) DEFAULT NULL,
  `ship_zone` varchar(255) DEFAULT NULL,
  `ship_zone_id` int(11) DEFAULT NULL,
  `ship_country` varchar(255) DEFAULT NULL,
  `ship_country_code` varchar(10) DEFAULT NULL,
  `ship_country_id` int(9) unsigned DEFAULT NULL,
  `bill_company` varchar(255) DEFAULT NULL,
  `bill_firstname` varchar(255) DEFAULT NULL,
  `bill_lastname` varchar(255) DEFAULT NULL,
  `bill_email` varchar(255) DEFAULT NULL,
  `bill_phone` varchar(40) DEFAULT NULL,
  `bill_address1` varchar(255) DEFAULT NULL,
  `bill_address2` varchar(255) DEFAULT NULL,
  `bill_city` varchar(255) DEFAULT NULL,
  `bill_zip` varchar(11) DEFAULT NULL,
  `bill_zone` varchar(255) DEFAULT NULL,
  `bill_zone_id` int(9) unsigned DEFAULT NULL,
  `bill_country` varchar(255) DEFAULT NULL,
  `bill_country_code` varchar(10) DEFAULT NULL,
  `bill_country_id` int(9) unsigned DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `order_number`, `customer_id`, `status`, `ordered_on`, `shipped_on`, `tax`, `total`, `subtotal`, `gift_card_discount`, `coupon_discount`, `shipping`, `shipping_notes`, `shipping_method`, `notes`, `payment_info`, `referral`, `company`, `firstname`, `lastname`, `phone`, `email`, `ship_company`, `ship_firstname`, `ship_lastname`, `ship_email`, `ship_phone`, `ship_address1`, `ship_address2`, `ship_city`, `ship_zip`, `ship_zone`, `ship_zone_id`, `ship_country`, `ship_country_code`, `ship_country_id`, `bill_company`, `bill_firstname`, `bill_lastname`, `bill_email`, `bill_phone`, `bill_address1`, `bill_address2`, `bill_city`, `bill_zip`, `bill_zone`, `bill_zone_id`, `bill_country`, `bill_country_code`, `bill_country_id`) VALUES
(30, '1423164383', 0, 'Order Placed', '2015-02-05 13:26:22', '0000-00-00 00:00:00', 0.00, 94.00, 79.00, 0.00, 0.00, 0.00, ' Brigitte Bailey Skyler Shift Dress 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', '', '', '', '', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(29, '1423120262', 0, 'Order Placed', '2015-02-05 01:11:02', '0000-00-00 00:00:00', 0.00, 125.00, 110.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\nKettle	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'Nsnd', 'Xndn', '5/5/', '', '', 'Nsnd', 'Xndn', '', '5/5/', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'Nsnd', 'Xndn', '', '5/5/', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(28, '1423107435', 0, 'Order Placed', '2015-02-04 21:37:15', '0000-00-00 00:00:00', 0.00, 190.00, 175.00, 0.00, 0.00, 0.00, 'Kettle	-	\nDefault: Variation\n\nVolcomm Grin sneakers	-	\nSize: Size 8\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'Test', 'Tedt', '31548769', 'test@yee.com', '', 'Test', 'Tedt', 'test@yee.com', '31548769', 'Street 52', '', 'Test', '800600', 'Zabol', 32, 'Afghanistan', '', 1, '', 'Test', 'Tedt', 'test@yee.com', '31548769', 'Street 52', '', 'Test', '800600', 'Zabol', 32, 'Afghanistan', '', 1),
(27, '1423051293', 0, 'Order Placed', '2015-02-04 06:01:33', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'bdbdn', 'nssn', '475245', 'bban@yhg.com', '', 'bdbdn', 'nssn', 'bban@yhg.com', '475245', '', '', '', '', 'Khulna', 323, 'Bangladesh', '', 18, '', 'bdbdn', 'nssn', 'bban@yhg.com', '475245', '', '', '', '', 'Khulna', 323, 'Bangladesh', '', 18),
(26, '1423050804', 0, 'Order Placed', '2015-02-04 05:53:23', '0000-00-00 00:00:00', 0.00, 243.00, 228.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\nKettle	-	\nDefault: Variation\n\n BCBGeneration La Vie Boheme Backpack 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'bdbdn', 'nssn', '475245', 'bban@yhg.com', '', 'bdbdn', 'nssn', 'bban@yhg.com', '475245', '', '', '', '', 'Khulna', 323, 'Bangladesh', '', 18, '', 'bdbdn', 'nssn', 'bban@yhg.com', '475245', '', '', '', '', 'Khulna', 323, 'Bangladesh', '', 18),
(24, '1422261686', 0, 'Order Placed', '2015-01-26 02:41:26', '0000-00-00 00:00:00', 0.00, 2110.00, 2095.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\nKettle	-	\nDefault: Variation\n\n Filson Large Carry On Travel Bag 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'Pick From Shop', 'Referal', '', 'John', 'Doe', '0701449003', 'gilbertkimutai@gmail.com', '', 'John', 'Doe', 'gilbertkimutai@gmail.com', '0701449003', '1710', '', 'Kericho', '20200', 'Western Isles', 3609, 'United Kingdom', '', 222, '', 'John', 'Doe', 'gilbertkimutai@gmail.com', '0701449003', '1710', '', 'Kericho', '20200', 'Western Isles', 3609, 'United Kingdom', '', 222),
(25, '1422265911', 0, 'Order Placed', '2015-01-26 03:51:50', '0000-00-00 00:00:00', 0.00, 104.00, 89.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n Brigitte Bailey Skyler Shift Dress 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'John', 'Doe', '0701449003', 'gilbertkimutai@gmail.com', '', 'John', 'Doe', 'gilbertkimutai@gmail.com', '0701449003', '1710', '', 'Kericho', '20200', 'Aberdeen', 3513, 'United Kingdom', '', 222, '', 'John', 'Doe', 'gilbertkimutai@gmail.com', '0701449003', '1710', '', 'Kericho', '20200', 'Aberdeen', 3513, 'United Kingdom', '', 222),
(31, '1423168766', 0, 'Order Placed', '2015-02-05 14:39:26', '0000-00-00 00:00:00', 0.00, 410.00, 395.00, 0.00, 0.00, 0.00, ' Filson Large Carry On Travel Bag 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'ggfk', 'gjjk', '9898989898', '', '', 'ggfk', 'gjjk', '', '9898989898', 'fhnlut\nggmm', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'ggfk', 'gjjk', '', '9898989898', 'fhnlut\nggmm', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(32, '1423235578', 0, 'Order Placed', '2015-02-06 09:12:58', '0000-00-00 00:00:00', 0.00, 94.00, 79.00, 0.00, 0.00, 0.00, ' Brigitte Bailey Skyler Shift Dress 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', '', '', '', '', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(33, '1423243135', 0, 'Order Placed', '2015-02-06 11:18:54', '0000-00-00 00:00:00', 0.00, 510.00, 495.00, 0.00, 0.00, 0.00, 'Kettle	-	\nDefault: Variation\n\n Filson Large Carry On Travel Bag 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'sunil', 'kumar', '9711901901', 'sk161282@gmail.com', '', 'sunil', 'kumar', 'sk161282@gmail.com', '9711901901', 'delhi', '', 'Delhi', '110032', 'Nairobi', 1, 'Kenya', '', 1, '', 'sunil', 'kumar', 'sk161282@gmail.com', '9711901901', 'delhi', '', 'Delhi', '110032', 'Nairobi', 1, 'Kenya', '', 1),
(34, '1423545047', 0, 'Order Placed', '2015-02-09 23:10:47', '0000-00-00 00:00:00', 0.00, 94.00, 79.00, 0.00, 0.00, 0.00, ' Brigitte Bailey Skyler Shift Dress 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'sssss', 'hhhhhh', '95658686868', '', '', 'sssss', 'hhhhhh', '', '95658686868', 'hdhdudjd', '', 'ccccc', '111000', 'Nairobi', 1, 'Kenya', '', 1, '', 'sssss', 'hhhhhh', '', '95658686868', 'hdhdudjd', '', 'ccccc', '111000', 'Nairobi', 1, 'Kenya', '', 1),
(35, '1423638626', 0, 'Order Placed', '2015-02-11 01:10:26', '0000-00-00 00:00:00', 0.00, 143.00, 128.00, 0.00, 0.00, 0.00, ' Mosey Duffy 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'sunil', 'kumar', '9711901901', 'sk161282@gmail.com', '', 'sunil', 'kumar', 'sk161282@gmail.com', '9711901901', 'delhi', '', 'Delhi', '110032', 'Nairobi', 1, 'Kenya', '', 1, '', 'sunil', 'kumar', 'sk161282@gmail.com', '9711901901', 'delhi', '', 'Delhi', '110032', 'Nairobi', 1, 'Kenya', '', 1),
(36, '1423887483', 0, 'Order Placed', '2015-02-13 22:18:03', '0000-00-00 00:00:00', 0.00, 487.00, 472.00, 0.00, 0.00, 0.00, ' BCBGeneration La Vie Boheme Backpack 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'Justin', 'kuang', '92984956', '', '', 'Justin', 'kuang', '', '92984956', 'Jurong West', '', 'Singapore', '406706', 'Nairobi', 1, 'Kenya', '', 1, '', 'Justin', 'kuang', '', '92984956', 'Jurong West', '', 'Singapore', '406706', 'Nairobi', 1, 'Kenya', '', 1),
(37, '1424020397', 0, 'Order Placed', '2015-02-15 11:13:16', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Black\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'Deneme', 'Denem', '05434664', '', '', 'Deneme', 'Denem', '', '05434664', 'Merkez', '', '?stanbul', '34818', 'Nairobi', 1, 'Kenya', '', 1, '', 'Deneme', 'Denem', '', '05434664', 'Merkez', '', '?stanbul', '34818', 'Nairobi', 1, 'Kenya', '', 1),
(38, '1424052893', 0, 'Order Placed', '2015-02-15 20:14:52', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'Pick From Shop', 'Referal', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', '', '', '', '', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(39, '1424190750', 0, 'Order Placed', '2015-02-17 10:32:30', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'sihaib', 'bej', '35959', '', '', 'sihaib', 'bej', '', '35959', 'vsyeb', '', '', '', '', 0, 'Kenya', '', 1, '', 'sihaib', 'bej', '', '35959', 'vsyeb', '', '', '', '', 0, 'Kenya', '', 1),
(40, '1424380735', 0, 'Order Placed', '2015-02-19 15:18:55', '0000-00-00 00:00:00', 0.00, 7915.00, 7900.00, 0.00, 0.00, 0.00, ' Filson Large Carry On Travel Bag 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'Pick From Shop', 'Referal', '', 'hshs', 'hshsb', '74487', '', '', 'hshs', 'hshsb', '', '74487', 'hdjdjjjdjbd', '', 'hshhs', '8784', 'Nairobi', 1, 'Kenya', '', 1, '', 'hshs', 'hshsb', '', '74487', 'hdjdjjjdjbd', '', 'hshhs', '8784', 'Nairobi', 1, 'Kenya', '', 1),
(41, '1424429269', 0, 'Order Placed', '2015-02-20 04:47:49', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'Pick From Shop', 'Referal', '', 'ffgyh', 'ghuu', '8555', 'honob@cVH.h', '', 'ffgyh', 'ghuu', 'honob@cVH.h', '8555', 'jhhhjkbjvj', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'ffgyh', 'ghuu', 'honob@cVH.h', '8555', 'jhhhjkbjvj', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(42, '1424429410', 0, 'Order Placed', '2015-02-20 04:50:10', '0000-00-00 00:00:00', 0.00, 94.00, 79.00, 0.00, 0.00, 0.00, ' Brigitte Bailey Skyler Shift Dress 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'ffgyh', 'ghuu', '8555', 'honob@cVH.h', '', 'ffgyh', 'ghuu', 'honob@cVH.h', '8555', 'jhhhjkbjvj', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'ffgyh', 'ghuu', 'honob@cVH.h', '8555', 'jhhhjkbjvj', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(43, '1424456570', 0, 'Order Placed', '2015-02-20 12:22:49', '0000-00-00 00:00:00', 0.00, 115.00, 100.00, 0.00, 0.00, 0.00, 'Kettle	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'An', 'Hb', '300358449', '', '', 'An', 'Hb', '', '300358449', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'An', 'Hb', '', '300358449', '', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(44, '1424488273', 0, 'Order Placed', '2015-02-20 21:11:13', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Black\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'teste', 'gfg', '8555', 'paulorvojr@gmail.con', '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1, '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1),
(45, '1424488364', 0, 'Order Placed', '2015-02-20 21:12:44', '0000-00-00 00:00:00', 0.00, 94.00, 79.00, 0.00, 0.00, 0.00, ' Brigitte Bailey Skyler Shift Dress 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'teste', 'gfg', '8555', 'paulorvojr@gmail.con', '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1, '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1),
(46, '1424488427', 0, 'Order Placed', '2015-02-20 21:13:47', '0000-00-00 00:00:00', 0.00, 90.00, 75.00, 0.00, 0.00, 0.00, 'Volcomm Grin sneakers	-	\nSize: Size 8\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'teste', 'gfg', '8555', 'paulorvojr@gmail.con', '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1, '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1),
(47, '1424489101', 0, 'Order Placed', '2015-02-20 21:25:00', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'Pick From Shop', 'Referal', '', 'teste', 'gfg', '8555', 'paulorvojr@gmail.con', '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1, '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1),
(48, '1424489228', 0, 'Order Placed', '2015-02-20 21:27:08', '0000-00-00 00:00:00', 0.00, 25.00, 10.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'teste', 'gfg', '8555', 'paulorvojr@gmail.con', '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1, '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1),
(49, '1424489559', 0, 'Order Placed', '2015-02-20 21:32:38', '0000-00-00 00:00:00', 0.00, 215.00, 200.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'Pick From Shop', 'Referal', '', 'teste', 'gfg', '8555', 'paulorvojr@gmail.con', '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1, '', 'teste', 'gfg', 'paulorvojr@gmail.con', '8555', 'cgcv', '', 'vcg', '22760151', 'Nairobi', 1, 'Kenya', '', 1),
(50, '1424491043', 0, 'Order Placed', '2015-02-20 21:57:23', '0000-00-00 00:00:00', 0.00, 1200.00, 1185.00, 0.00, 0.00, 0.00, ' Filson Large Carry On Travel Bag 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'ggy', '', '633', '', '', 'ggy', '', '', '633', 'gggg', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'ggy', '', '', '633', 'gggg', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(51, '1424491382', 0, 'Order Placed', '2015-02-20 22:03:01', '0000-00-00 00:00:00', 0.00, 1595.00, 1580.00, 0.00, 0.00, 0.00, ' Filson Large Carry On Travel Bag 	-	\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'ggy', '', '633', '', '', 'ggy', '', '', '633', 'gggg', '', '', '', 'Nairobi', 1, 'Kenya', '', 1, '', 'ggy', '', '', '633', 'gggg', '', '', '', 'Nairobi', 1, 'Kenya', '', 1),
(52, '1424531792', 0, 'Order Placed', '2015-02-21 09:16:32', '0000-00-00 00:00:00', 0.00, 100.00, 85.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\nVolcomm Grin sneakers	-	\nSize: Size 8\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', 'sunil', 'kumar', '9711901901', 'sk161282@gmail.com', '', 'sunil', 'kumar', 'sk161282@gmail.com', '9711901901', 'delhi', '', 'Delhi', '110032', 'Nairobi', 1, 'Kenya', '', 1, '', 'sunil', 'kumar', 'sk161282@gmail.com', '9711901901', 'delhi', '', 'Delhi', '110032', 'Nairobi', 1, 'Kenya', '', 1),
(53, '1424539337', 0, 'Order Placed', '2015-02-21 11:22:16', '0000-00-00 00:00:00', 0.00, 75.00, 60.00, 0.00, 0.00, 0.00, 'Classic Converse Shoes	-	\nColor: Red\nDefault: Variation\n\n', 'FLATRATE SHIPPING', '', 'C.O.D', 'Referal', '', '????', '???????', '68590855', '', '', '????', '???????', '', '68590855', '??????? 1 234567890', '', '?????', '77180', 'Nairobi', 1, 'Kenya', '', 1, '', '????', '???????', '', '68590855', '??????? 1 234567890', '', '?????', '77180', 'Nairobi', 1, 'Kenya', '', 1),
(54, '1424699717', 0, 'Order Placed', '2015-02-23 07:55:17', '0000-00-00 00:00:00', 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 0, '', '', 0, '', '', '', '', '', '', '', '', '', '', 0, '', '', 0);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE IF NOT EXISTS `order_items` (
  `id` int(9) unsigned NOT NULL,
  `order_id` int(9) unsigned NOT NULL,
  `product_id` int(9) unsigned NOT NULL,
  `quantity` int(11) NOT NULL,
  `contents` longtext NOT NULL,
  `attr` longtext NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `quantity`, `contents`, `attr`) VALUES
(21, 1422261686, 2746, 1, '', '\nDefault: Variation\n'),
(20, 1422261686, 2713, 2, '', '\nColor: Red\nDefault: Variation\n'),
(22, 1422261686, 2766, 5, '', '\nDefault: Variation\n'),
(23, 1422265911, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(24, 1422265911, 2779, 1, '', '\nDefault: Variation\n'),
(25, 1423050804, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(26, 1423050804, 2746, 1, '', '\nDefault: Variation\n'),
(27, 1423050804, 2760, 1, '', '\nDefault: Variation\n'),
(28, 1423051293, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(29, 1423107435, 2746, 1, '', '\nDefault: Variation\n'),
(30, 1423107435, 2750, 1, '', '\nSize: Size 8\nDefault: Variation\n'),
(31, 1423120262, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(32, 1423120262, 2746, 1, '', '\nDefault: Variation\n'),
(33, 1423164383, 2779, 1, '', '\nDefault: Variation\n'),
(34, 1423168766, 2766, 1, '', '\nDefault: Variation\n'),
(35, 1423235578, 2779, 1, '', '\nDefault: Variation\n'),
(36, 1423243135, 2746, 1, '', '\nDefault: Variation\n'),
(37, 1423243135, 2766, 1, '', '\nDefault: Variation\n'),
(38, 1423545047, 2779, 1, '', '\nDefault: Variation\n'),
(39, 1423638626, 2756, 1, '', '\nDefault: Variation\n'),
(40, 1423887483, 2760, 4, '', '\nDefault: Variation\n'),
(41, 1424020397, 2713, 1, '', '\nColor: Black\nDefault: Variation\n'),
(42, 1424052893, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(43, 1424190750, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(44, 1424380735, 2766, 20, '', '\nDefault: Variation\n'),
(45, 1424429269, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(46, 1424429410, 2779, 1, '', '\nDefault: Variation\n'),
(47, 1424456570, 2746, 1, '', '\nDefault: Variation\n'),
(48, 1424488273, 2713, 1, '', '\nColor: Black\nDefault: Variation\n'),
(49, 1424488364, 2779, 1, '', '\nDefault: Variation\n'),
(50, 1424488427, 2750, 1, '', '\nSize: Size 8\nDefault: Variation\n'),
(51, 1424489101, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(52, 1424489228, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(53, 1424489559, 2713, 20, '', '\nColor: Red\nDefault: Variation\n'),
(54, 1424491043, 2766, 3, '', '\nDefault: Variation\n'),
(55, 1424491382, 2766, 4, '', '\nDefault: Variation\n'),
(56, 1424531792, 2713, 1, '', '\nColor: Red\nDefault: Variation\n'),
(57, 1424531792, 2750, 1, '', '\nSize: Size 8\nDefault: Variation\n'),
(58, 1424539337, 2713, 6, '', '\nColor: Red\nDefault: Variation\n'),
(59, 1424699717, 0, 0, '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `banners`
--
ALTER TABLE `banners`
  ADD PRIMARY KEY (`banner_id`);

--
-- Indexes for table `countries`
--
ALTER TABLE `countries`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `country_zones`
--
ALTER TABLE `country_zones`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `banners`
--
ALTER TABLE `banners`
  MODIFY `banner_id` int(9) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `countries`
--
ALTER TABLE `countries`
  MODIFY `id` int(9) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `country_zones`
--
ALTER TABLE `country_zones`
  MODIFY `id` int(11) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `id` int(9) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=55;
--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(9) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=60;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
