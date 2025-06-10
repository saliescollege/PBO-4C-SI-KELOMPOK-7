-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 10, 2025 at 11:15 PM
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

--
-- Dumping data for table `diagnosa`
--

INSERT INTO `diagnosa` (`diagnosa_id`, `pasien_id`, `diagnosa`, `histopatologi`) VALUES
(1, 1, 'tes', 'tes');

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

--
-- Dumping data for table `dokter`
--

INSERT INTO `dokter` (`dokter_id`, `nama`, `spesialisasi`, `pendidikan`, `legalitas`) VALUES
(1, 'dr. Andini Paramitha, Sp.PD-KHOM', 'Penyakit Dalam - Hematologi Onkologi Medik', 'FK UI, Subspesialis RSCM', 'STR: 0123456789'),
(2, 'dr. Rendra Gunawan, Sp.A(K)', 'Anak - Hematologi Onkologi', 'FK UGM, Subspesialis RSUP Sardjito', 'STR: 1234567890'),
(3, 'dr. Lilis Hartanti, Sp.B(K)Onk', 'Bedah Onkologi', 'FK Unpad, Subspesialis RS Hasan Sadikin', 'STR: 2345678901'),
(4, 'dr. Dion Subekti, Sp.Onk.Rad', 'Onkologi Radiasi', 'FK Unair, Subspesialis RSUD Dr. Soetomo', 'STR: 3456789012'),
(5, 'dr. Feliks Aditya, Sp.PD', 'Penyakit Dalam', 'FK Universitas Brawijaya', 'STR: 4567890123'),
(6, 'dr. Mei Lestari, Sp.KN', 'Kedokteran Nuklir', 'FK UI, Spesialisasi RS Kanker Dharmais', 'STR: 5678901234'),
(7, 'dr. Yusuf Alamsyah, Sp.OG(K)Onk', 'Kandungan - Onkologi Ginekologi', 'FK Unhas, Subspesialis RSUP Wahidin Sudirohusodo', 'STR: 6789012345'),
(8, 'dr. Intan Nuraini, dr. umum', 'Dokter Umum (berlisensi kemoterapi dasar)', 'FK Universitas Islam Indonesia, Sertifikasi Kanker Dasar', 'STR: 9012345678');

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

--
-- Dumping data for table `jadwal_dokter`
--

INSERT INTO `jadwal_dokter` (`jadwal_id`, `dokter_id`, `hari`, `jam_mulai`, `jam_selesai`) VALUES
(1, 1, 'SENIN', '07:00:00', '08:00:00'),
(2, 1, 'SENIN', '08:30:00', '09:30:00'),
(3, 1, 'SENIN', '10:00:00', '11:00:00'),
(4, 1, 'SENIN', '11:30:00', '12:30:00'),
(5, 4, 'SENIN', '13:00:00', '14:00:00'),
(6, 4, 'SENIN', '14:30:00', '15:30:00');

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

--
-- Dumping data for table `pasien`
--

INSERT INTO `pasien` (`pasien_id`, `nama_lengkap`, `alamat`, `no_telepon`, `tanggal_lahir`, `jenis_kelamin`, `dokter_id`, `created_at`) VALUES
(1, 'tes', 'tes', 'tes', '2000-01-01', 'L', 1, '2025-06-10 19:14:25');

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

--
-- Dumping data for table `periksa_fisik`
--

INSERT INTO `periksa_fisik` (`periksa_id`, `pasien_id`, `tekanan_darah`, `suhu_tubuh`, `denyut_nadi`, `berat_badan`, `hb`, `leukosit`, `trombosit`, `sgot_sgpt`, `ureum_kreatinin`, `tanggal_periksa`) VALUES
(1, 1, '12', 12, 12, 12, 12, 12, 12, 'SGOT 12', 'Ureum 12', '2025-06-10 19:14:25');

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

--
-- Dumping data for table `rencana_terapi`
--

INSERT INTO `rencana_terapi` (`terapi_id`, `pasien_id`, `jenis_kemoterapi`, `dosis`, `siklus`, `premedikasi`, `akses_vena`, `dokter_id`, `tanggal_dibuat`) VALUES
(1, 1, 'tes', '12', 3, 'tes', 'tes', 1, '2025-06-11');

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
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `full_name`, `email`, `phone`, `created_at`) VALUES
(1, 'user', '202cb962ac59075b964b07152d234b70', 'user', 'user@gmail.com', '082736482983', '2025-06-08 20:56:22'),
(2, 'user1', '81dc9bdb52d04dc20036dbd8313ed055', 'User Satu', 'user@gmail.com', '08765437829', '2025-06-09 00:04:18');

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
  MODIFY `diagnosa_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `dokter`
--
ALTER TABLE `dokter`
  MODIFY `dokter_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `evaluasi_kemo`
--
ALTER TABLE `evaluasi_kemo`
  MODIFY `evaluasi_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  MODIFY `jadwal_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `jadwal_terapi`
--
ALTER TABLE `jadwal_terapi`
  MODIFY `jadwal_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pasien`
--
ALTER TABLE `pasien`
  MODIFY `pasien_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `periksa_fisik`
--
ALTER TABLE `periksa_fisik`
  MODIFY `periksa_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `rencana_terapi`
--
ALTER TABLE `rencana_terapi`
  MODIFY `terapi_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

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
  ADD CONSTRAINT `jadwal_dokter_ibfk_2` FOREIGN KEY (`dokter_id`) REFERENCES `dokter` (`dokter_id`);

--
-- Constraints for table `jadwal_terapi`
--
ALTER TABLE `jadwal_terapi`
  ADD CONSTRAINT `jadwal_terapi_ibfk_1` FOREIGN KEY (`terapi_id`) REFERENCES `rencana_terapi` (`terapi_id`),
  ADD CONSTRAINT `jadwal_terapi_ibfk_2` FOREIGN KEY (`jadwal_dokter_id`) REFERENCES `jadwal_dokter` (`jadwal_id`);

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
