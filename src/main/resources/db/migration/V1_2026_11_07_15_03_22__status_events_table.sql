-- Migration para a entidade: status_events
-- Criado em: 2026-11-07T15:03:22.806040635

CREATE TABLE `status_events` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `initialism` varchar(10) not null,
  `description` text,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp DEFAULT (now())
);