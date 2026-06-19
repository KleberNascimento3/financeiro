-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: financeiro
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cartoes`
--

DROP TABLE IF EXISTS `cartoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cartoes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dia_fechamento` int DEFAULT NULL,
  `dia_vencimento` int DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cartoes`
--

LOCK TABLES `cartoes` WRITE;
/*!40000 ALTER TABLE `cartoes` DISABLE KEYS */;
INSERT INTO `cartoes` VALUES (1,12,20,'Carrefour'),(2,12,20,'Sicoob'),(3,12,20,'MercadoPago'),(4,28,6,'Nubank');
/*!40000 ALTER TABLE `cartoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativa` bit(1) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,_binary '','Carro'),(2,_binary '','Pessoal'),(3,_binary '','Renda'),(4,_binary '','Casa');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compromissos`
--

DROP TABLE IF EXISTS `compromissos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compromissos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativo` bit(1) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `dia_vencimento` int DEFAULT NULL,
  `parcelas_restantes` int DEFAULT NULL,
  `quantidade_parcelas` int DEFAULT NULL,
  `tipo` enum('PARCELADO','RECORRENTE','UNICO') DEFAULT NULL,
  `valor` decimal(38,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compromissos`
--

LOCK TABLES `compromissos` WRITE;
/*!40000 ALTER TABLE `compromissos` DISABLE KEYS */;
/*!40000 ALTER TABLE `compromissos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eventos_financeiros`
--

DROP TABLE IF EXISTS `eventos_financeiros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventos_financeiros` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `indice` int DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `data_evento` date DEFAULT NULL,
  `tipo` varchar(20) DEFAULT NULL,
  `encerrado` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventos_financeiros`
--

LOCK TABLES `eventos_financeiros` WRITE;
/*!40000 ALTER TABLE `eventos_financeiros` DISABLE KEYS */;
INSERT INTO `eventos_financeiros` VALUES (1,36,'Vale Junho','2026-06-15','VALE',_binary '\0'),(2,37,'Pagamento Junho','2026-06-30','PAGAMENTO',_binary '\0'),(3,38,'Vale Julho','2026-07-15','VALE',_binary '\0'),(4,39,'Pagamento Julho','2026-07-30','PAGAMENTO',_binary '\0'),(5,40,'Vale Agosto','2026-08-14','VALE',_binary '\0'),(6,41,'Pagamento Agosto','2026-08-31','PAGAMENTO',_binary '\0');
/*!40000 ALTER TABLE `eventos_financeiros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lancamentos`
--

DROP TABLE IF EXISTS `lancamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lancamentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `categoria` varchar(255) DEFAULT NULL,
  `data_pagamento` date DEFAULT NULL,
  `data_vencimento` date DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `status` enum('ADIADO','PAGO','PENDENTE') DEFAULT NULL,
  `tipo` enum('DESPESA','RECEITA') DEFAULT NULL,
  `valor` decimal(38,2) DEFAULT NULL,
  `evento_id` bigint DEFAULT NULL,
  `categoria_id` bigint DEFAULT NULL,
  `compromisso_id` bigint DEFAULT NULL,
  `numero_parcela` int DEFAULT NULL,
  `parcelado` bit(1) DEFAULT NULL,
  `recorrente` bit(1) DEFAULT NULL,
  `total_parcelas` int DEFAULT NULL,
  `cartao_id` bigint DEFAULT NULL,
  `dia_recorrencia` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_lancamento_evento` (`evento_id`),
  KEY `FK5rnduby8gv23wl52on9pvpxwo` (`categoria_id`),
  KEY `FKn5m98n2aqsbnogjkisklakk2b` (`compromisso_id`),
  KEY `FKpuml03o0cqlyfayoeir51qdbu` (`cartao_id`),
  CONSTRAINT `FK5rnduby8gv23wl52on9pvpxwo` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `fk_lancamento_evento` FOREIGN KEY (`evento_id`) REFERENCES `eventos_financeiros` (`id`),
  CONSTRAINT `FKn5m98n2aqsbnogjkisklakk2b` FOREIGN KEY (`compromisso_id`) REFERENCES `compromissos` (`id`),
  CONSTRAINT `FKpuml03o0cqlyfayoeir51qdbu` FOREIGN KEY (`cartao_id`) REFERENCES `cartoes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lancamentos`
--

LOCK TABLES `lancamentos` WRITE;
/*!40000 ALTER TABLE `lancamentos` DISABLE KEYS */;
INSERT INTO `lancamentos` VALUES (68,NULL,NULL,'2026-06-15','VALE','PENDENTE','RECEITA',1740.00,1,3,NULL,NULL,_binary '\0',_binary '\0',NULL,NULL,NULL),(69,NULL,NULL,'2026-06-15','Alternador 3/5','PENDENTE','DESPESA',90.00,1,1,NULL,NULL,_binary '\0',_binary '\0',3,NULL,NULL),(70,NULL,NULL,'2026-07-17','Alternador 4/5','PENDENTE','DESPESA',90.00,3,1,NULL,NULL,_binary '\0',_binary '\0',3,3,NULL),(71,NULL,NULL,'2026-08-17','Alternador 5/5','PENDENTE','DESPESA',90.00,5,1,NULL,NULL,_binary '\0',_binary '\0',3,3,NULL),(75,NULL,NULL,'2026-07-15','VALE','PENDENTE','RECEITA',1740.00,3,3,NULL,NULL,_binary '\0',_binary '\0',NULL,NULL,NULL),(76,NULL,NULL,'2026-08-14','VALE','PENDENTE','RECEITA',1740.00,5,3,NULL,NULL,_binary '\0',_binary '\0',NULL,NULL,NULL),(77,NULL,NULL,'2026-06-17','VAP 1/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,1,_binary '',_binary '\0',12,3,NULL),(78,NULL,NULL,'2026-07-17','VAP 2/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,2,_binary '',_binary '\0',12,3,NULL),(79,NULL,NULL,'2026-08-17','VAP 3/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,3,_binary '',_binary '\0',12,3,NULL),(80,NULL,NULL,'2026-09-17','VAP 4/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,4,_binary '',_binary '\0',12,3,NULL),(81,NULL,NULL,'2026-10-17','VAP 5/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,5,_binary '',_binary '\0',12,3,NULL),(82,NULL,NULL,'2026-11-17','VAP 6/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,6,_binary '',_binary '\0',12,3,NULL),(83,NULL,NULL,'2026-12-17','VAP 7/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,7,_binary '',_binary '\0',12,3,NULL),(84,NULL,NULL,'2027-01-17','VAP 8/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,8,_binary '',_binary '\0',12,3,NULL),(85,NULL,NULL,'2027-02-17','VAP 9/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,9,_binary '',_binary '\0',12,3,NULL),(86,NULL,NULL,'2027-03-17','VAP 10/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,10,_binary '',_binary '\0',12,3,NULL),(87,NULL,NULL,'2027-04-17','VAP 11/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,11,_binary '',_binary '\0',12,3,NULL),(88,NULL,NULL,'2027-05-17','VAP 12/12','PENDENTE','DESPESA',32.18,NULL,4,NULL,12,_binary '',_binary '\0',12,3,NULL),(89,NULL,NULL,'2026-06-17','VAP 1/12 1/12','PENDENTE','DESPESA',2.68,1,4,NULL,1,_binary '',_binary '\0',12,3,NULL),(90,NULL,NULL,'2026-07-17','VAP 1/12 2/12','PENDENTE','DESPESA',2.68,1,4,NULL,2,_binary '',_binary '\0',12,3,NULL),(91,NULL,NULL,'2026-08-17','VAP 1/12 3/12','PENDENTE','DESPESA',2.68,1,4,NULL,3,_binary '',_binary '\0',12,3,NULL),(92,NULL,NULL,'2026-09-17','VAP 1/12 4/12','PENDENTE','DESPESA',2.68,1,4,NULL,4,_binary '',_binary '\0',12,3,NULL),(93,NULL,NULL,'2026-10-17','VAP 1/12 5/12','PENDENTE','DESPESA',2.68,1,4,NULL,5,_binary '',_binary '\0',12,3,NULL),(94,NULL,NULL,'2026-11-17','VAP 1/12 6/12','PENDENTE','DESPESA',2.68,1,4,NULL,6,_binary '',_binary '\0',12,3,NULL),(95,NULL,NULL,'2026-12-17','VAP 1/12 7/12','PENDENTE','DESPESA',2.68,1,4,NULL,7,_binary '',_binary '\0',12,3,NULL),(96,NULL,NULL,'2027-01-17','VAP 1/12 8/12','PENDENTE','DESPESA',2.68,1,4,NULL,8,_binary '',_binary '\0',12,3,NULL),(97,NULL,NULL,'2027-02-17','VAP 1/12 9/12','PENDENTE','DESPESA',2.68,1,4,NULL,9,_binary '',_binary '\0',12,3,NULL),(98,NULL,NULL,'2027-03-17','VAP 1/12 10/12','PENDENTE','DESPESA',2.68,1,4,NULL,10,_binary '',_binary '\0',12,3,NULL),(99,NULL,NULL,'2027-04-17','VAP 1/12 11/12','PENDENTE','DESPESA',2.68,1,4,NULL,11,_binary '',_binary '\0',12,3,NULL),(100,NULL,NULL,'2027-05-17','VAP 1/12 12/12','PENDENTE','DESPESA',2.68,1,4,NULL,12,_binary '',_binary '\0',12,3,NULL),(101,NULL,NULL,'2026-06-17','VAP 3/12 1/12','PENDENTE','DESPESA',2.68,1,4,NULL,1,_binary '',_binary '\0',12,3,NULL),(102,NULL,NULL,'2026-07-17','VAP 3/12 2/12','PENDENTE','DESPESA',2.68,1,4,NULL,2,_binary '',_binary '\0',12,3,NULL),(103,NULL,NULL,'2026-08-17','VAP 3/12 3/12','PENDENTE','DESPESA',2.68,1,4,NULL,3,_binary '',_binary '\0',12,3,NULL),(104,NULL,NULL,'2026-09-17','VAP 3/12 4/12','PENDENTE','DESPESA',2.68,1,4,NULL,4,_binary '',_binary '\0',12,3,NULL),(105,NULL,NULL,'2026-10-17','VAP 3/12 5/12','PENDENTE','DESPESA',2.68,1,4,NULL,5,_binary '',_binary '\0',12,3,NULL),(106,NULL,NULL,'2026-11-17','VAP 3/12 6/12','PENDENTE','DESPESA',2.68,1,4,NULL,6,_binary '',_binary '\0',12,3,NULL),(107,NULL,NULL,'2026-12-17','VAP 3/12 7/12','PENDENTE','DESPESA',2.68,1,4,NULL,7,_binary '',_binary '\0',12,3,NULL),(108,NULL,NULL,'2027-01-17','VAP 3/12 8/12','PENDENTE','DESPESA',2.68,1,4,NULL,8,_binary '',_binary '\0',12,3,NULL),(109,NULL,NULL,'2027-02-17','VAP 3/12 9/12','PENDENTE','DESPESA',2.68,1,4,NULL,9,_binary '',_binary '\0',12,3,NULL),(110,NULL,NULL,'2027-03-17','VAP 3/12 10/12','PENDENTE','DESPESA',2.68,1,4,NULL,10,_binary '',_binary '\0',12,3,NULL),(111,NULL,NULL,'2027-04-17','VAP 3/12 11/12','PENDENTE','DESPESA',2.68,1,4,NULL,11,_binary '',_binary '\0',12,3,NULL),(112,NULL,NULL,'2027-05-17','VAP 3/12 12/12','PENDENTE','DESPESA',2.68,1,4,NULL,12,_binary '',_binary '\0',12,3,NULL);
/*!40000 ALTER TABLE `lancamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lancamentos_recorrentes`
--

DROP TABLE IF EXISTS `lancamentos_recorrentes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lancamentos_recorrentes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ativo` bit(1) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  `status` enum('ADIADO','PAGO','PENDENTE') DEFAULT NULL,
  `tipo` enum('DESPESA','RECEITA') DEFAULT NULL,
  `valor` decimal(38,2) DEFAULT NULL,
  `cartao_id` bigint DEFAULT NULL,
  `categoria_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKphnncay7mqx0tja23cgj4v99c` (`cartao_id`),
  KEY `FK4kpsisd4ip0oexhanfo409918` (`categoria_id`),
  CONSTRAINT `FK4kpsisd4ip0oexhanfo409918` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `FKphnncay7mqx0tja23cgj4v99c` FOREIGN KEY (`cartao_id`) REFERENCES `cartoes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lancamentos_recorrentes`
--

LOCK TABLES `lancamentos_recorrentes` WRITE;
/*!40000 ALTER TABLE `lancamentos_recorrentes` DISABLE KEYS */;
/*!40000 ALTER TABLE `lancamentos_recorrentes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr8oo98o39ykr4hi57md9nibmw` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-19 14:20:22
