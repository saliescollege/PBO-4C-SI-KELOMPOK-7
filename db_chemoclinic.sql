-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 29, 2025 at 07:59 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_chemoclinic`
--

-- --------------------------------------------------------

--
-- Table structure for table `diagnosa`
--

CREATE TABLE `diagnosa` (
  `diagnosa_id` int(11) NOT NULL,
  `pasien_id` int(11) NOT NULL,
  `diagnosa` text NOT NULL,
  `histopatologi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dokter`
--

CREATE TABLE `dokter` (
  `dokter_id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `spesialisasi` varchar(100) NOT NULL,
  `pendidikan` text NOT NULL,
  `legalitas` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `evaluasi_kemo`
--

CREATE TABLE `evaluasi_kemo` (
  `evaluasi_id` int(11) NOT NULL,
  `jadwal_id` int(11) NOT NULL,
  `kondisi_post_terapi` text NOT NULL,
  `efek_samping` text NOT NULL,
  `catatan` text NOT NULL,
  `tanggal_evaluasi` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_dokter`
--

CREATE TABLE `jadwal_dokter` (
  `jadwal_id` int(11) NOT NULL,
  `dokter_id` int(11) NOT NULL,
  `hari` enum('SENIN','SELASA','RABU','KAMIS','JUMAT','SABTU','MINGGU') NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_terapi`
--

CREATE TABLE `jadwal_terapi` (
  `jadwal_id` int(11) NOT NULL,
  `jadwal_dokter_id` int(11) NOT NULL,
  `terapi_id` int(11) NOT NULL,
  `sesi_ke` int(11) NOT NULL,
  `tanggal_terapi` date NOT NULL,
  `jam_terapi` time NOT NULL,
  `ruangan` enum('RJ-1','RJ-2','RJ-3','KI-1','KI-2','OSK') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pasien`
--

CREATE TABLE `pasien` (
  `pasien_id` int(11) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `alamat` text NOT NULL,
  `no_telepon` varchar(20) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `jenis_kelamin` enum('L','P') NOT NULL,
  `dokter_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `periksa_fisik`
--

CREATE TABLE `periksa_fisik` (
  `periksa_id` int(11) NOT NULL,
  `pasien_id` int(11) NOT NULL,
  `tekanan_darah` varchar(20) NOT NULL,
  `suhu_tubuh` float NOT NULL,
  `denyut_nadi` float NOT NULL,
  `berat_badan` float NOT NULL,
  `hb` float NOT NULL,
  `leukosit` float NOT NULL,
  `trombosit` float NOT NULL,
  `sgot_sgpt` varchar(50) NOT NULL,
  `ureum_kreatinin` varchar(50) NOT NULL,
  `tanggal_periksa` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rencana_terapi`
--

CREATE TABLE `rencana_terapi` (
  `terapi_id` int(11) NOT NULL,
  `pasien_id` int(11) NOT NULL,
  `jenis_kemoterapi` varchar(100) NOT NULL,
  `dosis` varchar(100) NOT NULL,
  `siklus` int(11) NOT NULL,
  `premedikasi` text NOT NULL,
  `akses_vena` text NOT NULL,
  `dokter_id` int(11) NOT NULL,
  `tanggal_dibuat` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `diagnosa`
--
ALTER TABLE `diagnosa`
  ADD PRIMARY KEY (`diagnosa_id`),
  ADD KEY `pasien_id` (`pasien_id`);

--
-- Indexes for table `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`dokter_id`);

--
-- Indexes for table `evaluasi_kemo`
--
ALTER TABLE `evaluasi_kemo`
  ADD PRIMARY KEY (`evaluasi_id`),
  ADD KEY `jadwal_id` (`jadwal_id`);

--
-- Indexes for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  ADD PRIMARY KEY (`jadwal_id`),
  ADD KEY `dokter_id` (`dokter_id`);

--
-- Indexes for table `jadwal_terapi`
--
ALTER TABLE `jadwal_terapi`
  ADD PRIMARY KEY (`jadwal_id`),
  ADD KEY `terapi_id` (`terapi_id`),
  ADD KEY `jadwal_dokter_id` (`jadwal_dokter_id`);

--
-- Indexes for table `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`pasien_id`),
  ADD KEY `dokter_id` (`dokter_id`);

--
-- Indexes for table `periksa_fisik`
--
ALTER TABLE `periksa_fisik`
  ADD PRIMARY KEY (`periksa_id`),
  ADD KEY `pasien_id` (`pasien_id`);

--
-- Indexes for table `rencana_terapi`
--
ALTER TABLE `rencana_terapi`
  ADD PRIMARY KEY (`terapi_id`),
  ADD KEY `pasien_id` (`pasien_id`),
  ADD KEY `dokter_id` (`dokter_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `diagnosa`
--
ALTER TABLE `diagnosa`
  MODIFY `diagnosa_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dokter`
--
ALTER TABLE `dokter`
  MODIFY `dokter_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `evaluasi_kemo`
--
ALTER TABLE `evaluasi_kemo`
  MODIFY `evaluasi_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  MODIFY `jadwal_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jadwal_terapi`
--
ALTER TABLE `jadwal_terapi`
  MODIFY `jadwal_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pasien`
--
ALTER TABLE `pasien`
  MODIFY `pasien_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `periksa_fisik`
--
ALTER TABLE `periksa_fisik`
  MODIFY `periksa_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rencana_terapi`
--
ALTER TABLE `rencana_terapi`
  MODIFY `terapi_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `diagnosa`
--
ALTER TABLE `diagnosa`
  ADD CONSTRAINT `diagnosa_ibfk_1` FOREIGN KEY (`pasien_id`) REFERENCES `pasien` (`pasien_id`);

--
-- Constraints for table `evaluasi_kemo`
--
ALTER TABLE `evaluasi_kemo`
  ADD CONSTRAINT `evaluasi_kemo_ibfk_1` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal_terapi` (`jadwal_id`);

--
-- Constraints for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  ADD CONSTRAINT `jadwal_dokter_ibfk_1` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal_terapi` (`jadwal_dokter_id`),
  ADD CONSTRAINT `jadwal_dokter_ibfk_2` FOREIGN KEY (`dokter_id`) REFERENCES `dokter` (`dokter_id`);

--
-- Constraints for table `jadwal_terapi`
--
ALTER TABLE `jadwal_terapi`
  ADD CONSTRAINT `jadwal_terapi_ibfk_1` FOREIGN KEY (`terapi_id`) REFERENCES `rencana_terapi` (`terapi_id`);

--
-- Constraints for table `pasien`
--
ALTER TABLE `pasien`
  ADD CONSTRAINT `pasien_ibfk_1` FOREIGN KEY (`dokter_id`) REFERENCES `dokter` (`dokter_id`);

--
-- Constraints for table `periksa_fisik`
--
ALTER TABLE `periksa_fisik`
  ADD CONSTRAINT `periksa_fisik_ibfk_1` FOREIGN KEY (`pasien_id`) REFERENCES `pasien` (`pasien_id`);

--
-- Constraints for table `rencana_terapi`
--
ALTER TABLE `rencana_terapi`
  ADD CONSTRAINT `rencana_terapi_ibfk_1` FOREIGN KEY (`pasien_id`) REFERENCES `pasien` (`pasien_id`),
  ADD CONSTRAINT `rencana_terapi_ibfk_2` FOREIGN KEY (`dokter_id`) REFERENCES `dokter` (`dokter_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
