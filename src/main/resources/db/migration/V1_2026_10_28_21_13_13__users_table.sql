-- Migration para a entidade: users
-- Criado em: 2026-10-28T21:13:13.667448943


CREATE TABLE `users` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `role_id` BIGINT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `cpf` varchar(255) UNIQUE NOT NULL,
  `ra` varchar(255) UNIQUE,
  `is_active` boolean DEFAULT true,
  `path_img` varchar(255),
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp DEFAULT (now()),
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);
