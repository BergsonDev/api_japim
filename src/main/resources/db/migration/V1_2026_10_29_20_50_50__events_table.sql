-- Migration para a entidade: events
-- Criado em: 2026-10-29T20:50:50.935327333

CREATE TABLE `events` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `short_description` varchar(255) NOT NULL,
  `description` text,
  `path_img` varchar(255),
  `is_payment` boolean DEFAULT false,
  `value` float,
  `max_participants` int NOT NULL,
  `max_volunteers` int NOT NULL,
  `date_start` date NOT NULL,
  `hour_start` time NOT NULL,
  `date_end` date NOT NULL,
  `hour_end` time NOT NULL,
  `workload` time NOT NULL,
  `created_by` BIGINT NOT NULL,
  `confirmed_by` BIGINT NOT NULL,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp DEFAULT (now()),
  FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  FOREIGN KEY (`confirmed_by`) REFERENCES `users` (`id`)
);