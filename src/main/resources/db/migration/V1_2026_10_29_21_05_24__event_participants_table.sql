-- Migration para a entidade: event_participants
-- Criado em: 2026-10-29T21:05:24.944431822
CREATE TABLE `event_participants` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `event_id` BIGINT,
  `participant_id` BIGINT,
  `count_presence` int,         
  `date_inscription` timestamp DEFAULT (now()),
  `date_payment` datetime,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`participant_id`) REFERENCES `users` (`id`)
);