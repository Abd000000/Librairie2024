-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mer. 24 avr. 2024 à 20:04
-- Version du serveur : 5.7.24
-- Version de PHP : 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `librairie-fofana-abdoulaye`
--

-- --------------------------------------------------------

--
-- Structure de la table `adherent`
--

CREATE TABLE `adherent` (
  `id_adherent` int(4) NOT NULL,
  `nom` varchar(42) DEFAULT NULL,
  `prenom` varchar(42) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `adherent`
--

INSERT INTO `adherent` (`id_adherent`, `nom`, `prenom`, `email`) VALUES
(1, 'Mac', 'John', 'macjohn@example.com'),
(2, 'With', 'Mac', 'withmac@example.com'),
(3, 'Brown', 'Giornojovana', 'browngio@example.com'),
(4, 'Mike', 'Pires', 'polodav@example.com'),
(5, 'Polo', 'David', 'davidtaylor@example.com'),
(7, 'Maca', 'John', 'macjohn@example.com');

-- --------------------------------------------------------

--
-- Structure de la table `auteur`
--

CREATE TABLE `auteur` (
  `id_auteur` int(4) NOT NULL,
  `nom` varchar(42) DEFAULT NULL,
  `prenom` varchar(42) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `description` varchar(400) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `auteur`
--

INSERT INTO `auteur` (`id_auteur`, `nom`, `prenom`, `date_naissance`, `description`) VALUES
(1, 'Dostoevsky', 'Fyodor', '1821-11-11', 'Russian novelist, short story writer, essayist, journalist and philosopher.'),
(2, 'Camus', 'Albert', '1913-11-07', 'French philosopher, author, and journalist.'),
(3, 'Roth', 'Veronica', '1988-08-19', 'American novelist and short story writer.'),
(4, 'Nietzsche', 'Friedrich', '1844-10-15', 'German philosopher, cultural critic, composer, poet, and philologist.'),
(5, 'Zweig', 'Stefan', '1881-11-28', 'Austrian novelist, playwright, journalist and biographer.'),
(6, 'Orwell', 'George', '1903-06-25', 'English novelist, essayist, journalist and critic.'),
(7, 'Tolkien', 'J.R.R.', '1892-01-03', 'English writer, poet, philologist, and university professor.'),
(8, 'Murakami', 'Haruki', '1949-01-12', 'Japanese writer.'),
(9, 'Rowling', 'J.K.', '1965-07-31', 'British author, philanthropist, film producer, television producer, and screenwriter.'),
(10, 'Saint-Exupéry', 'Antoine de', '1900-06-29', 'French writer, poet, aristocrat, journalist, and pioneering aviator.');

-- --------------------------------------------------------

--
-- Structure de la table `emprunts`
--

CREATE TABLE `emprunts` (
  `id_emprunt` int(11) NOT NULL,
  `id_adherent` int(11) DEFAULT NULL,
  `id_livre` int(11) DEFAULT NULL,
  `date_emprunt` date DEFAULT NULL,
  `date_retour` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `emprunts`
--

INSERT INTO `emprunts` (`id_emprunt`, `id_adherent`, `id_livre`, `date_emprunt`, `date_retour`) VALUES
(23, 1, 1, '2024-03-19', '2024-04-02'),
(24, -1, 2, '2024-03-23', '2024-04-20'),
(25, -1, 2, '2024-04-14', '2024-05-12');

-- --------------------------------------------------------

--
-- Structure de la table `livre`
--

CREATE TABLE `livre` (
  `id_livre` int(4) NOT NULL,
  `titre` varchar(255) DEFAULT NULL,
  `prix` int(4) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `id_auteur` int(4) NOT NULL,
  `disponibilite` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `livre`
--

INSERT INTO `livre` (`id_livre`, `titre`, `prix`, `genre`, `id_auteur`, `disponibilite`) VALUES
(1, 'Le Joueur d\'échecs', 15, 'Fiction', 1, NULL),
(2, 'L\'étranger', 12, 'Philosophical fiction', 2, 19),
(3, 'Divergent', 10, 'Young adult fiction', 3, NULL),
(4, 'Thus Spoke Zarathustra', 18, 'Philosophy', 4, NULL),
(5, 'Beyond Good and Evil', 20, 'Philosophy', 4, NULL),
(6, 'The Metamorphosis', 14, 'Fiction', 5, NULL),
(7, 'Nineteen Eighty-Four', 16, 'Dystopian', 6, NULL),
(8, 'The Hobbit', 18, 'Fantasy', 7, NULL),
(9, 'Norwegian Wood', 15, 'Novel', 8, NULL),
(10, 'Harry Potter and the Philosopher\'s Stone', 20, 'Fantasy', 9, NULL),
(11, 'The Little Prince', 13, 'Novella', 10, NULL),
(12, 'Crimes et Hibrahima', 140, NULL, 1, 50);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `adherent`
--
ALTER TABLE `adherent`
  ADD PRIMARY KEY (`id_adherent`);

--
-- Index pour la table `auteur`
--
ALTER TABLE `auteur`
  ADD PRIMARY KEY (`id_auteur`);

--
-- Index pour la table `emprunts`
--
ALTER TABLE `emprunts`
  ADD PRIMARY KEY (`id_emprunt`),
  ADD KEY `id_adherent` (`id_adherent`),
  ADD KEY `fk_id_livre` (`id_livre`);

--
-- Index pour la table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`id_livre`),
  ADD KEY `id_auteur` (`id_auteur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `adherent`
--
ALTER TABLE `adherent`
  MODIFY `id_adherent` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `auteur`
--
ALTER TABLE `auteur`
  MODIFY `id_auteur` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `emprunts`
--
ALTER TABLE `emprunts`
  MODIFY `id_emprunt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT pour la table `livre`
--
ALTER TABLE `livre`
  MODIFY `id_livre` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
