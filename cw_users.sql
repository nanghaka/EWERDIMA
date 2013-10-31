-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 31, 2013 at 07:02 AM
-- Server version: 5.1.41
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `loginscript`
--

-- --------------------------------------------------------

--
-- Table structure for table `cw_users`
--

CREATE TABLE IF NOT EXISTS `cw_users` (
  `userid` int(25) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(25) NOT NULL DEFAULT '',
  `last_name` varchar(25) NOT NULL DEFAULT '',
  `email_address` varchar(50) NOT NULL DEFAULT '',
  `username` varchar(25) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `info` varchar(50) NOT NULL,
  `last_loggedin` varchar(100) NOT NULL DEFAULT 'never',
  `user_level` enum('1','2','3','4','5') NOT NULL DEFAULT '1',
  `forgot` varchar(100) DEFAULT NULL,
  `status` enum('live','suspended','pending') NOT NULL DEFAULT 'live',
  PRIMARY KEY (`userid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='Membership Information' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `cw_users`
--

INSERT INTO `cw_users` (`userid`, `first_name`, `last_name`, `email_address`, `username`, `password`, `info`, `last_loggedin`, `user_level`, `forgot`, `status`) VALUES
(1, 'Administrative', '<insert>', '<insert>', 'Administrative', '82844bcd79cf87398b530075894a4aae', 'Administrative', '31/10/13 6:02:19', '5', NULL, 'live'),
(2, 'Daniel', 'Nanghaka', 'dndannang@gmail.com', 'dnanghaka', '82844bcd79cf87398b530075894a4aae', 'Things are fine ', '31/10/13 6:34:48', '1', NULL, 'live');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
