-- Migration para a entidade: bundles
-- Criado em: 2026-10-30T19:30:43.079355485

CREATE TABLE `bundles` (
  `id`  BIGINT PRIMARY KEY AUTO_INCREMENT,
  `organizer_by` bigint,
  `price` float,
  `description` varchar(255),
  `img_path` varchar(255),
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp DEFAULT (now()),
  FOREIGN KEY (`organizer_by`) REFERENCES `users` (`id`)
  
);