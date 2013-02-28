-- phpMyAdmin SQL Dump
-- version 3.3.7deb5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 28. Februar 2013 um 19:06
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
  `crdate` timestamp NULL DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `route_id` int(10) unsigned DEFAULT NULL,
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rb_route_details`
--

CREATE TABLE IF NOT EXISTS `rb_route_details` (
  `rating_count` int(10) unsigned DEFAULT NULL,
  `avarage_rating` int(11) DEFAULT NULL,
  `avarage_categorie` varchar(20) DEFAULT NULL,
  `route_id` int(10) unsigned DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rb_user`
--

CREATE TABLE IF NOT EXISTS `rb_user` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `user_password` varchar(45) DEFAULT NULL,
  `route_count` int(10) unsigned zerofill DEFAULT NULL,
  `flash_count` int(10) unsigned zerofill DEFAULT NULL,
  `redpoint_count` int(10) unsigned zerofill DEFAULT NULL,
  `notclimbed_count` int(10) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tx_dihlroutes_routelist`
--

CREATE TABLE IF NOT EXISTS `tx_dihlroutes_routelist` (
  `uid` int(10) unsigned NOT NULL,
  `pid` int(11) NOT NULL DEFAULT '0',
  `tstamp` int(11) NOT NULL DEFAULT '0',
  `crdate` int(11) NOT NULL DEFAULT '0',
  `cruser_id` int(11) NOT NULL DEFAULT '0',
  `sorting` int(10) NOT NULL DEFAULT '0',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `hidden` tinyint(4) NOT NULL DEFAULT '0',
  `sektor` int(11) NOT NULL DEFAULT '0',
  `boltrow` int(11) NOT NULL DEFAULT '0',
  `unr` int(11) NOT NULL DEFAULT '0',
  `color` tinytext,
  `length` int(11) NOT NULL DEFAULT '0',
  `uiaa` int(11) NOT NULL DEFAULT '0',
  `createdby` tinytext,
  `dateon` int(11) NOT NULL DEFAULT '0',
  `tr` tinyint(3) NOT NULL DEFAULT '0',
  `remarks` text,
  `dateoff` int(11) NOT NULL DEFAULT '0',
  `protection` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `parent` (`pid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tx_dihlroutes_sektor`
--

CREATE TABLE IF NOT EXISTS `tx_dihlroutes_sektor` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT '0',
  `tstamp` int(11) NOT NULL DEFAULT '0',
  `crdate` int(11) NOT NULL DEFAULT '0',
  `cruser_id` int(11) NOT NULL DEFAULT '0',
  `sorting` int(10) NOT NULL DEFAULT '0',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `hidden` tinyint(4) NOT NULL DEFAULT '0',
  `sektor` tinytext,
  PRIMARY KEY (`uid`),
  KEY `parent` (`pid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=78 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tx_dihlroutes_uiaa`
--

CREATE TABLE IF NOT EXISTS `tx_dihlroutes_uiaa` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL DEFAULT '0',
  `tstamp` int(11) NOT NULL DEFAULT '0',
  `crdate` int(11) NOT NULL DEFAULT '0',
  `cruser_id` int(11) NOT NULL DEFAULT '0',
  `sorting` int(10) NOT NULL DEFAULT '0',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `hidden` tinyint(4) NOT NULL DEFAULT '0',
  `uiaa` tinytext,
  PRIMARY KEY (`uid`),
  KEY `parent` (`pid`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=202 ;
