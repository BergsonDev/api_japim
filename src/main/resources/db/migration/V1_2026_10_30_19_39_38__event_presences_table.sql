-- Migration para a entidade: event_presences
-- Criado em: 2026-10-30T19:39:38.299347615

CREATE TABLE `event_presences` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `event_id` bigint NOT NULL,
  `participant_id` bigint NOT NULL,
  `confirmed_by_id` bigint NOT NULL,
  `date_presence` timestamp NOT NULL,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp DEFAULT (now()),
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`participant_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`confirmed_by_id`) REFERENCES `users` (`id`)
);