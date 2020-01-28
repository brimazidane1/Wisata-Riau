-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 12 Jul 2017 pada 11.11
-- Versi Server: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wisata_riau`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `book`
--

CREATE TABLE `book` (
  `id_booking` varchar(15) NOT NULL,
  `nama_wisata` varchar(30) NOT NULL,
  `nama_pemesan` varchar(30) NOT NULL,
  `jumlah_tiket` int(4) NOT NULL,
  `tanggal` datetime NOT NULL,
  `total` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `book`
--

INSERT INTO `book` (`id_booking`, `nama_wisata`, `nama_pemesan`, `jumlah_tiket`, `tanggal`, `total`) VALUES
('17070900001', 'Boombara', 'dane', 2, '2017-07-09 03:40:07', 150000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `data_book`
--

CREATE TABLE `data_book` (
  `id_booking` varchar(15) NOT NULL,
  `tanggal` date NOT NULL,
  `nama_wisata` varchar(50) NOT NULL,
  `total` int(11) NOT NULL,
  `cash` int(12) NOT NULL,
  `kembali` int(12) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `data_book`
--

INSERT INTO `data_book` (`id_booking`, `tanggal`, `nama_wisata`, `total`, `cash`, `kembali`) VALUES
('17070900001', '2017-07-09', 'Boombara', 150000, 300000, 150000),
('17070900001', '2017-07-09', 'Boombara', 150000, 500000, 350000),
('17070900001', '2017-07-09', 'Boombara', 150000, 200000, 50000),
('17070900001', '2017-07-09', 'Boombara', 150000, 200000, 50000),
('17071000001', '2017-07-10', 'Riau Fantasi', 100000, 200000, 100000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `list`
--

CREATE TABLE `list` (
  `id_wisata` int(3) NOT NULL,
  `nama_wisata` varchar(50) NOT NULL,
  `wilayah` varchar(50) NOT NULL,
  `gambar` varchar(255) NOT NULL,
  `harga` double NOT NULL,
  `ket` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `list`
--

INSERT INTO `list` (`id_wisata`, `nama_wisata`, `wilayah`, `gambar`, `harga`, `ket`) VALUES
(1, 'Boombara', 'Kampar', 'C:\\Users\\DANE-PC\\Documents\\NetBeansProjects\\Wisata Riau\\src\\gambar\\boombara.jpg', 75000, 'Waterpark'),
(2, 'Istana Siak Sri Indrapura', 'Siak', 'C:\\Users\\DANE-PC\\Documents\\NetBeansProjects\\Wisata Riau\\src\\gambar\\siak.jpg', 50000, 'Historic Buildings'),
(3, 'Alam Mayang', 'Pekanbaru', 'C:\\Users\\DANE-PC\\Documents\\NetBeansProjects\\Wisata Riau\\src\\gambar\\alammayang.jpg', 30000, 'Taman Wisata'),
(4, 'Riau Fantasi', 'Kampar', 'C:\\Users\\DANE-PC\\Documents\\NetBeansProjects\\Wisata Riau\\src\\gambar\\labersa.jpg', 50000, 'Theme park'),
(5, 'Benteng Tujuh Lapis', 'Rokan Hulu', 'C:\\Users\\DANE-PC\\Documents\\NetBeansProjects\\Wisata Riau\\src\\gambar\\benteng.jpg', 40000, 'Tourist attraction');

-- --------------------------------------------------------

--
-- Struktur dari tabel `testi`
--

CREATE TABLE `testi` (
  `nama` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `komen` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `testi`
--

INSERT INTO `testi` (`nama`, `email`, `komen`) VALUES
('Dane', 'dane@gmail.com', 'Mantapp!');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id_booking`);

--
-- Indexes for table `data_book`
--
ALTER TABLE `data_book`
  ADD KEY `id_booking` (`id_booking`);

--
-- Indexes for table `list`
--
ALTER TABLE `list`
  ADD PRIMARY KEY (`id_wisata`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `list`
--
ALTER TABLE `list`
  MODIFY `id_wisata` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
