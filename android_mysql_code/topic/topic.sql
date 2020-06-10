-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 
-- 伺服器版本： 10.4.6-MariaDB
-- PHP 版本： 7.3.9

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `topic`
--
CREATE DATABASE IF NOT EXISTS `topic` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `topic`;

-- --------------------------------------------------------

--
-- 資料表結構 `test_db`
--
-- 建立時間： 
-- 最後更新： 
--

DROP TABLE IF EXISTS `test_db`;
CREATE TABLE IF NOT EXISTS `test_db` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RFID` bigint(20) NOT NULL,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `specification` text COLLATE utf8_unicode_ci NOT NULL,
  `num` int(11) NOT NULL,
  `field` text COLLATE utf8_unicode_ci NOT NULL,
  `remarks` text COLLATE utf8_unicode_ci NOT NULL,
  `datetime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE current_timestamp(),
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 資料表的關聯 `test_db`:
--

--
-- 傾印資料表的資料 `test_db`
--

INSERT INTO `test_db` (`ID`, `RFID`, `name`, `specification`, `num`, `field`, `remarks`, `datetime`) VALUES
(1, 202006091123, 'iphoneX', '64GB', 999, '10A', '玫瑰金', '0000-00-00 00:00:00'),
(2, 202006091124, 'samsung S11+', '128GB', 999, '10B', '藍', '2020-06-09 02:13:16'),
(3, 202006091126, 'SONY XPERIA 1 II', '256GB', 999, '11A', '預購', '2020-06-09 16:27:19');


--
-- 元資料
--
USE `phpmyadmin`;

--
-- 資料表 test_db 的元資料
--

--
-- 資料庫 topic 的元資料
--
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
