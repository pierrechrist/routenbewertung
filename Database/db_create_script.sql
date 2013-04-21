-- phpMyAdmin SQL Dump
-- version 3.3.7deb5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 21. April 2013 um 23:10
-- Server Version: 5.1.49
-- PHP-Version: 5.3.3-7+squeeze14

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `dav_rb`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rb_ratings`
--

CREATE TABLE IF NOT EXISTS `rb_ratings` (
  `rating` int(10) unsigned DEFAULT NULL,
  `howclimbed` varchar(20) DEFAULT NULL,
  `categorie` varchar(20) DEFAULT NULL,
  `crdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(10) unsigned DEFAULT NULL,
  `route_id` int(10) unsigned DEFAULT NULL,
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=128 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rb_route_details`
--

CREATE TABLE IF NOT EXISTS `rb_route_details` (
  `avarage_rating` int(11) DEFAULT NULL,
  `avarage_categorie` varchar(20) DEFAULT NULL,
  `route_id` int(10) unsigned NOT NULL DEFAULT '0',
  UNIQUE KEY `route_id` (`route_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rb_user`
--

CREATE TABLE IF NOT EXISTS `rb_user` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `encrypted_password` varchar(80) NOT NULL,
  `salt` varchar(45) NOT NULL,
  `crdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=29 ;

--
-- Daten für Tabelle `rb_user`
--

INSERT INTO `rb_user` (`user_name`, `user_email`, `encrypted_password`, `salt`) VALUES
('admin', 'heinrich.robert84@gmx.de', '6FECk+TZvLNgtdXL4zxAzWeq8fA5YjA2YzBlMjFk', '9b06c0e21d');
