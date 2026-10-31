-- Migration para a entidade: certificates
-- Criado em: 2026-10-30T19:31:39.270612735

CREATE TABLE `certificates` (
  `id` bigint,
  `event_id` bigint,
  `user_id` bigint,
  `date_generated` timestamp,
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
