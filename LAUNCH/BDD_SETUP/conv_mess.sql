-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  lun. 15 fév. 2021 à 13:36
-- Version du serveur :  5.7.21
-- Version de PHP :  5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `conv_mess`
--

-- --------------------------------------------------------

--
-- Structure de la table `conversation`
--

DROP TABLE IF EXISTS `conversation`;
CREATE TABLE IF NOT EXISTS `conversation` (
  `idConv` int(11) NOT NULL AUTO_INCREMENT,
  `User1` varchar(20) NOT NULL,
  `User2` varchar(20) NOT NULL,
  PRIMARY KEY (`idConv`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `conversation`
--

INSERT INTO `conversation` (`idConv`, `User1`, `User2`) VALUES
(1, 'clem', 'jc'),
(28, 'clem', 'test');

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE IF NOT EXISTS `message` (
  `id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL,
  `data` varchar(255) NOT NULL,
  `loginEmetteur` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `message`
--

INSERT INTO `message` (`id`, `timestamp`, `data`, `loginEmetteur`) VALUES
(1, '2021-01-12 19:07:06', 'salut', 'clem'),
(1, '2021-02-11 16:17:32', 'test', 'clem'),
(1, '2021-02-11 16:20:25', 'rre', 'clem'),
(1, '2021-02-15 10:16:33', '11', 'clem'),
(1, '2021-02-15 10:32:45', '22', 'clem'),
(1, '2021-02-15 10:47:56', '33', 'clem'),
(1, '2021-02-15 10:49:39', 'test', 'jc'),
(1, '2021-02-15 10:54:16', '44', 'clem'),
(1, '2021-02-15 10:54:18', '44', 'clem'),
(1, '2021-02-15 10:54:20', '44', 'clem'),
(1, '2021-02-15 10:54:24', '55', 'jc'),
(1, '2021-02-15 10:54:25', '55', 'jc'),
(1, '2021-02-15 10:57:17', 'salut mec', 'clem'),
(1, '2021-02-15 10:57:33', 'ah super on dirait que ca marche', 'clem'),
(1, '2021-02-15 10:59:58', 'lalala', 'clem'),
(28, '2021-02-15 11:00:07', 'bonjour', 'clem'),
(28, '2021-02-15 11:01:07', 'salut', 'test'),
(1, '2021-02-15 13:30:26', 'salut', 'jc'),
(1, '2021-02-15 13:30:32', 'salut', 'clem');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
