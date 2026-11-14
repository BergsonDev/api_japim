-- Migration para a entidade: certificates
-- Criado em: 2026-10-30T19:31:39.270612735

CREATE TABLE `certificates` (
  `id` bigint not null AUTO_INCREMENT primary key,
  `event_id` bigint not null,
  `user_id` bigint not null,
  `date_generated` timestamp not null,
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
