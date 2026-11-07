-- Migration para a entidade: event_volunteers
-- Criado em: 2026-10-30T19:23:01.027016929

CREATE TABLE `event_volunteers` (
  `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `event_id` bigint,
  `volunteer_id` bigint,
  `count_presence` int,
  `date_inscription` timestamp DEFAULT (now()),
  `deleted_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`volunteer_id`) REFERENCES `users` (`id`)
  
);