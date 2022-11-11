-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 11, 2020 at 08:23 PM
-- Server version: 10.4.8-MariaDB
-- PHP Version: 7.3.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dole_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_table`
--

CREATE TABLE `admin_table` (
  `a_id` int(99) NOT NULL,
  `a_name` varchar(20) NOT NULL,
  `a_username` varchar(20) NOT NULL,
  `a_password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `admin_table`
--

INSERT INTO `admin_table` (`a_id`, `a_name`, `a_username`, `a_password`) VALUES
(1, 'admin', 'admin', '$2y$10$W.wPmwhI1lWOwsKfQSesfuaEap7OhWvZYtkETXDry52tYVUMbwPiG');

-- --------------------------------------------------------

--
-- Table structure for table `attendees_table`
--

CREATE TABLE `attendees_table` (
  `at_id` int(99) NOT NULL,
  `jf_id` int(99) NOT NULL,
  `js_id` int(99) NOT NULL,
  `p_id` int(99) DEFAULT 0,
  `at_attend` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `attendees_table`
--

INSERT INTO `attendees_table` (`at_id`, `jf_id`, `js_id`, `p_id`, `at_attend`) VALUES
(41, 42, 117, 1, 1),
(42, 42, 118, 1, 1),
(44, 43, 118, NULL, 0),
(45, 43, 117, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `education_table`
--

CREATE TABLE `education_table` (
  `ed_id` int(99) NOT NULL,
  `ed_level` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `education_table`
--

INSERT INTO `education_table` (`ed_id`, `ed_level`) VALUES
(1, 'Gradeschool'),
(2, 'Junior Highschool'),
(3, 'Senior Highschool'),
(4, 'College'),
(5, 'Post Graduate');

-- --------------------------------------------------------

--
-- Table structure for table `employer_scanned_table`
--

CREATE TABLE `employer_scanned_table` (
  `e_scanned_id` int(99) NOT NULL,
  `vacancy_id` int(99) NOT NULL,
  `jobfair_id` int(99) NOT NULL,
  `jobseeker_id` int(99) NOT NULL,
  `employer_id` int(99) NOT NULL,
  `hstatus_id` int(99) NOT NULL,
  `jv_location` varchar(99) NOT NULL,
  `js_gender` varchar(99) NOT NULL,
  `comments` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `employer_scanned_table`
--

INSERT INTO `employer_scanned_table` (`e_scanned_id`, `vacancy_id`, `jobfair_id`, `jobseeker_id`, `employer_id`, `hstatus_id`, `jv_location`, `js_gender`, `comments`) VALUES
(56, 110, 42, 117, 38, 2, 'Local', 'Male', 'gud'),
(57, 110, 42, 118, 38, 1, 'Local', 'Male', 'güd'),
(58, 111, 42, 117, 39, 1, 'Local', 'Male', 'hahahahha'),
(59, 111, 42, 118, 39, 2, 'Local', 'Male', 'yyyyyy\n');

-- --------------------------------------------------------

--
-- Table structure for table `employer_table`
--

CREATE TABLE `employer_table` (
  `e_id` int(99) NOT NULL,
  `e_username` varchar(30) NOT NULL,
  `e_password` text NOT NULL,
  `e_email` varchar(40) NOT NULL,
  `e_cname` varchar(100) NOT NULL,
  `e_caddress` varchar(40) NOT NULL,
  `e_cnumber` varchar(15) NOT NULL,
  `e_cperson` varchar(50) NOT NULL,
  `e_validation` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `employer_table`
--

INSERT INTO `employer_table` (`e_id`, `e_username`, `e_password`, `e_email`, `e_cname`, `e_caddress`, `e_cnumber`, `e_cperson`, `e_validation`) VALUES
(1, 'employer', '$2y$10$JF/J80tiy36gBy0GJvybre2iH1KF5EEkJCFohmj0hIbK6VWNmorQG', 'employer@gmail.com', 'sampleC', 'pasonanca', '099191', 'aziz', 1),
(38, 'dms1234', '$2y$10$2pUmMhW1tcqtstrc6ODTFexthgQWKTZrHB9ILP8w3WJINWHspAxjG', 'dms@gmail.com', 'Donya Maria', 'Tetuan', '09563286594', 'Gregy', 0),
(39, 'jobee12', '$2y$10$Tq0/f650vXkIHLMMdYMQQ.qsf5WsYjg/wfzgZhraQPS5vFxokeBG.', 'jobee@gmail.com', 'Jollibee', 'Tetuan', '090909090909', 'Hetty', 0);

-- --------------------------------------------------------

--
-- Table structure for table `hstatus_table`
--

CREATE TABLE `hstatus_table` (
  `hs_id` int(99) NOT NULL,
  `hiring_status` varchar(99) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hstatus_table`
--

INSERT INTO `hstatus_table` (`hs_id`, `hiring_status`) VALUES
(1, 'HotS'),
(2, 'Qualified'),
(3, 'Near-Hire'),
(4, 'Not Qualified'),
(5, 'Undefined');

-- --------------------------------------------------------

--
-- Table structure for table `jobfair_employers`
--

CREATE TABLE `jobfair_employers` (
  `jfe_id` int(99) NOT NULL,
  `jobfair_id` int(99) NOT NULL,
  `employer_id` int(99) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobfair_employers`
--

INSERT INTO `jobfair_employers` (`jfe_id`, `jobfair_id`, `employer_id`) VALUES
(13, 42, 38),
(14, 42, 39),
(15, 44, 39),
(16, 44, 38),
(17, 44, 1);

-- --------------------------------------------------------

--
-- Table structure for table `jobfair_table`
--

CREATE TABLE `jobfair_table` (
  `jf_id` int(99) NOT NULL,
  `jf_name` varchar(200) NOT NULL,
  `jf_date` date NOT NULL,
  `jf_location` varchar(50) NOT NULL,
  `jf_description` text NOT NULL,
  `jf_pattendees` int(99) NOT NULL,
  `jf_status` int(1) NOT NULL DEFAULT 0,
  `start_time` varchar(99) DEFAULT NULL,
  `stop_time` varchar(99) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobfair_table`
--

INSERT INTO `jobfair_table` (`jf_id`, `jf_name`, `jf_date`, `jf_location`, `jf_description`, `jf_pattendees`, `jf_status`, `start_time`, `stop_time`) VALUES
(42, 'kccjf', '2020-02-09', 'kcc', 'ahdheheh\n', 1, 2, '2020/02/09  20:41', NULL),
(43, 'mercedes job fair', '2020-02-14', 'mercedes', 'guud', 1, 1, NULL, NULL),
(44, 'Kcc jobfair', '2020-02-10', 'kcc', 'KCc mall de zamobnga\n', 1, 1, '2020/02/10  14:05', NULL),
(56, 'aaaaaa', '2020-02-11', 'asdasdasdasdada', 'asdasdasdas', 0, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jobseeker_credentials`
--

CREATE TABLE `jobseeker_credentials` (
  `js_credentials_id` int(88) NOT NULL,
  `jobseeker_id` int(99) NOT NULL,
  `jobseeker_skills_id` int(99) NOT NULL,
  `education_id` int(99) NOT NULL,
  `job_preferred` varchar(50) NOT NULL,
  `other_skill` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobseeker_credentials`
--

INSERT INTO `jobseeker_credentials` (`js_credentials_id`, `jobseeker_id`, `jobseeker_skills_id`, `education_id`, `job_preferred`, `other_skill`) VALUES
(40, 117, 4, 4, 'Programmer', 'Driving'),
(41, 118, 1, 4, 'cleaner', 'dota');

-- --------------------------------------------------------

--
-- Table structure for table `jobseeker_table`
--

CREATE TABLE `jobseeker_table` (
  `js_id` int(99) NOT NULL,
  `js_username` varchar(30) NOT NULL,
  `js_password` text NOT NULL,
  `js_email` varchar(40) NOT NULL,
  `js_first_name` varchar(50) NOT NULL,
  `js_last_name` varchar(50) NOT NULL,
  `js_contactno` varchar(15) NOT NULL,
  `js_address` varchar(40) NOT NULL,
  `js_gender` varchar(10) NOT NULL,
  `js_dateofbirth` date NOT NULL,
  `js_course` varchar(50) DEFAULT NULL,
  `js_school` varchar(50) NOT NULL,
  `js_joblocation` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobseeker_table`
--

INSERT INTO `jobseeker_table` (`js_id`, `js_username`, `js_password`, `js_email`, `js_first_name`, `js_last_name`, `js_contactno`, `js_address`, `js_gender`, `js_dateofbirth`, `js_course`, `js_school`, `js_joblocation`) VALUES
(117, 'tanmartius', '$2y$10$g6RLxcgSi77zliGY1E616OQjwuWZP/sR5gENZINsnpJoCq3i98l9q', 'tanmartius@gmail.com', 'Martius', 'Tan', '09953279342', 'Tetuan', 'Male', '2017-12-20', 'BSIT', 'adzu', 'zamboanga'),
(118, 'enrian1', '$2y$10$9Xz/oCAms8ovrXt1bRq/P..zOQ8rf.scPNT7y46EZOF2R4GGAXDTq', 'enian@gmail.com', 'Ian', 'Enriquez', '0956328754', 'Tetuan', 'Male', '2020-02-09', 'Pol Sci', 'wmsu', 'Here lang');

-- --------------------------------------------------------

--
-- Table structure for table `jobvacancy_skills_table`
--

CREATE TABLE `jobvacancy_skills_table` (
  `jv_skill_id` int(99) NOT NULL,
  `jobvacancy_id` int(99) NOT NULL,
  `skills_id` int(99) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobvacancy_skills_table`
--

INSERT INTO `jobvacancy_skills_table` (`jv_skill_id`, `jobvacancy_id`, `skills_id`) VALUES
(133, 110, 4),
(134, 111, 9);

-- --------------------------------------------------------

--
-- Table structure for table `jobvacancy_table`
--

CREATE TABLE `jobvacancy_table` (
  `jv_id` int(99) NOT NULL,
  `jv_jf_id` int(99) NOT NULL,
  `jv_e_id` int(99) NOT NULL,
  `jv_title` varchar(255) NOT NULL,
  `jv_skill_o` varchar(99) DEFAULT NULL,
  `jv_education` int(99) NOT NULL,
  `jv_location` varchar(50) NOT NULL,
  `jv_vacancy_count` varchar(99) NOT NULL,
  `jv_description` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobvacancy_table`
--

INSERT INTO `jobvacancy_table` (`jv_id`, `jv_jf_id`, `jv_e_id`, `jv_title`, `jv_skill_o`, `jv_education`, `jv_location`, `jv_vacancy_count`, `jv_description`) VALUES
(110, 42, 38, 'Programmer', 'Driving', 4, 'Local', '30', 'we need ü'),
(111, 42, 39, 'Swimmer', 'Gardner', 4, 'Local', '50', 'hellö'),
(113, 44, 1, 'Swimmer', 'Gardner', 4, 'Local', '50', 'hellö');

-- --------------------------------------------------------

--
-- Table structure for table `partner_table`
--

CREATE TABLE `partner_table` (
  `p_id` int(99) NOT NULL,
  `p_name` varchar(20) NOT NULL,
  `p_username` varchar(40) NOT NULL,
  `p_password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `partner_table`
--

INSERT INTO `partner_table` (`p_id`, `p_name`, `p_username`, `p_password`) VALUES
(1, 'PESO', 'partner', '$2y$10$l.xf9a9W2CXJ4/h3xaIAyuS/Xms9hgaFe.CyySQul1IPbKxxt46ui');

-- --------------------------------------------------------

--
-- Table structure for table `skills_table`
--

CREATE TABLE `skills_table` (
  `skills_id` int(99) NOT NULL,
  `skills` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `skills_table`
--

INSERT INTO `skills_table` (`skills_id`, `skills`) VALUES
(1, 'Auto Mechanic'),
(2, 'Beautician'),
(3, 'Carpentry Work'),
(4, 'Computer Literate'),
(5, 'Domestic Chores'),
(6, 'Driver'),
(7, 'Electrician'),
(8, 'Embroidery'),
(9, 'Gardening'),
(10, 'Masonry'),
(11, 'Painter/Artist'),
(12, 'Painting Jobs'),
(13, 'Photography'),
(14, 'Plumbing'),
(15, 'Sewing Dresses'),
(16, 'Stenography'),
(17, 'Tailoring');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_table`
--
ALTER TABLE `admin_table`
  ADD PRIMARY KEY (`a_id`);

--
-- Indexes for table `attendees_table`
--
ALTER TABLE `attendees_table`
  ADD PRIMARY KEY (`at_id`),
  ADD KEY `jf_id` (`jf_id`),
  ADD KEY `js_id` (`js_id`),
  ADD KEY `p_id` (`p_id`);

--
-- Indexes for table `education_table`
--
ALTER TABLE `education_table`
  ADD PRIMARY KEY (`ed_id`);

--
-- Indexes for table `employer_scanned_table`
--
ALTER TABLE `employer_scanned_table`
  ADD PRIMARY KEY (`e_scanned_id`),
  ADD KEY `vacancy_id` (`vacancy_id`),
  ADD KEY `jobseeker_id` (`jobseeker_id`),
  ADD KEY `employer_id` (`employer_id`),
  ADD KEY `hstatus_id` (`hstatus_id`),
  ADD KEY `jobfair_id` (`jobfair_id`);

--
-- Indexes for table `employer_table`
--
ALTER TABLE `employer_table`
  ADD PRIMARY KEY (`e_id`);

--
-- Indexes for table `hstatus_table`
--
ALTER TABLE `hstatus_table`
  ADD PRIMARY KEY (`hs_id`);

--
-- Indexes for table `jobfair_employers`
--
ALTER TABLE `jobfair_employers`
  ADD PRIMARY KEY (`jfe_id`),
  ADD KEY `jobfair_id` (`jobfair_id`),
  ADD KEY `employer_id` (`employer_id`);

--
-- Indexes for table `jobfair_table`
--
ALTER TABLE `jobfair_table`
  ADD PRIMARY KEY (`jf_id`);

--
-- Indexes for table `jobseeker_credentials`
--
ALTER TABLE `jobseeker_credentials`
  ADD PRIMARY KEY (`js_credentials_id`),
  ADD KEY `jobseeker_id` (`jobseeker_id`),
  ADD KEY `jobseeker_skills_id` (`jobseeker_skills_id`),
  ADD KEY `education_id` (`education_id`);

--
-- Indexes for table `jobseeker_table`
--
ALTER TABLE `jobseeker_table`
  ADD PRIMARY KEY (`js_id`);

--
-- Indexes for table `jobvacancy_skills_table`
--
ALTER TABLE `jobvacancy_skills_table`
  ADD PRIMARY KEY (`jv_skill_id`),
  ADD KEY `jobvacancy_id` (`jobvacancy_id`),
  ADD KEY `skills_id` (`skills_id`);

--
-- Indexes for table `jobvacancy_table`
--
ALTER TABLE `jobvacancy_table`
  ADD PRIMARY KEY (`jv_id`),
  ADD KEY `jv_jf_id` (`jv_jf_id`),
  ADD KEY `jv_e_id` (`jv_e_id`),
  ADD KEY `jv_education` (`jv_education`);

--
-- Indexes for table `partner_table`
--
ALTER TABLE `partner_table`
  ADD PRIMARY KEY (`p_id`);

--
-- Indexes for table `skills_table`
--
ALTER TABLE `skills_table`
  ADD PRIMARY KEY (`skills_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_table`
--
ALTER TABLE `admin_table`
  MODIFY `a_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `attendees_table`
--
ALTER TABLE `attendees_table`
  MODIFY `at_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `education_table`
--
ALTER TABLE `education_table`
  MODIFY `ed_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `employer_scanned_table`
--
ALTER TABLE `employer_scanned_table`
  MODIFY `e_scanned_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `employer_table`
--
ALTER TABLE `employer_table`
  MODIFY `e_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `hstatus_table`
--
ALTER TABLE `hstatus_table`
  MODIFY `hs_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `jobfair_employers`
--
ALTER TABLE `jobfair_employers`
  MODIFY `jfe_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `jobfair_table`
--
ALTER TABLE `jobfair_table`
  MODIFY `jf_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `jobseeker_credentials`
--
ALTER TABLE `jobseeker_credentials`
  MODIFY `js_credentials_id` int(88) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `jobseeker_table`
--
ALTER TABLE `jobseeker_table`
  MODIFY `js_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT for table `jobvacancy_skills_table`
--
ALTER TABLE `jobvacancy_skills_table`
  MODIFY `jv_skill_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=137;

--
-- AUTO_INCREMENT for table `jobvacancy_table`
--
ALTER TABLE `jobvacancy_table`
  MODIFY `jv_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=114;

--
-- AUTO_INCREMENT for table `partner_table`
--
ALTER TABLE `partner_table`
  MODIFY `p_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `skills_table`
--
ALTER TABLE `skills_table`
  MODIFY `skills_id` int(99) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attendees_table`
--
ALTER TABLE `attendees_table`
  ADD CONSTRAINT `attendees_table_ibfk_1` FOREIGN KEY (`jf_id`) REFERENCES `jobfair_table` (`jf_id`),
  ADD CONSTRAINT `attendees_table_ibfk_2` FOREIGN KEY (`js_id`) REFERENCES `jobseeker_table` (`js_id`),
  ADD CONSTRAINT `attendees_table_ibfk_3` FOREIGN KEY (`p_id`) REFERENCES `partner_table` (`p_id`);

--
-- Constraints for table `employer_scanned_table`
--
ALTER TABLE `employer_scanned_table`
  ADD CONSTRAINT `employer_scanned_table_ibfk_1` FOREIGN KEY (`vacancy_id`) REFERENCES `jobvacancy_table` (`jv_id`),
  ADD CONSTRAINT `employer_scanned_table_ibfk_2` FOREIGN KEY (`jobseeker_id`) REFERENCES `jobseeker_table` (`js_id`),
  ADD CONSTRAINT `employer_scanned_table_ibfk_3` FOREIGN KEY (`employer_id`) REFERENCES `employer_table` (`e_id`),
  ADD CONSTRAINT `employer_scanned_table_ibfk_4` FOREIGN KEY (`hstatus_id`) REFERENCES `hstatus_table` (`hs_id`),
  ADD CONSTRAINT `employer_scanned_table_ibfk_5` FOREIGN KEY (`jobfair_id`) REFERENCES `jobfair_table` (`jf_id`);

--
-- Constraints for table `jobfair_employers`
--
ALTER TABLE `jobfair_employers`
  ADD CONSTRAINT `jobfair_employers_ibfk_1` FOREIGN KEY (`jobfair_id`) REFERENCES `jobfair_table` (`jf_id`),
  ADD CONSTRAINT `jobfair_employers_ibfk_2` FOREIGN KEY (`employer_id`) REFERENCES `employer_table` (`e_id`);

--
-- Constraints for table `jobseeker_credentials`
--
ALTER TABLE `jobseeker_credentials`
  ADD CONSTRAINT `jobseeker_credentials_ibfk_1` FOREIGN KEY (`jobseeker_id`) REFERENCES `jobseeker_table` (`js_id`),
  ADD CONSTRAINT `jobseeker_credentials_ibfk_2` FOREIGN KEY (`jobseeker_skills_id`) REFERENCES `skills_table` (`skills_id`),
  ADD CONSTRAINT `jobseeker_credentials_ibfk_3` FOREIGN KEY (`education_id`) REFERENCES `education_table` (`ed_id`);

--
-- Constraints for table `jobvacancy_skills_table`
--
ALTER TABLE `jobvacancy_skills_table`
  ADD CONSTRAINT `jobvacancy_skills_table_ibfk_1` FOREIGN KEY (`jobvacancy_id`) REFERENCES `jobvacancy_table` (`jv_id`),
  ADD CONSTRAINT `jobvacancy_skills_table_ibfk_2` FOREIGN KEY (`skills_id`) REFERENCES `skills_table` (`skills_id`);

--
-- Constraints for table `jobvacancy_table`
--
ALTER TABLE `jobvacancy_table`
  ADD CONSTRAINT `jobvacancy_table_ibfk_1` FOREIGN KEY (`jv_jf_id`) REFERENCES `jobfair_table` (`jf_id`),
  ADD CONSTRAINT `jobvacancy_table_ibfk_2` FOREIGN KEY (`jv_e_id`) REFERENCES `employer_table` (`e_id`),
  ADD CONSTRAINT `jobvacancy_table_ibfk_4` FOREIGN KEY (`jv_education`) REFERENCES `education_table` (`ed_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
