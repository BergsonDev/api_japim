-- Migration para a entidade: event_volunteers
-- Criado em: 2026-10-30T19:23:01.027016929

CREATE TABLE `event_volunteers` (
  `event_id` bigint,
  `volunteer_id` bigint,
  `count_presence` bigint,
  `date_inscription` timestamp DEFAULT (now()),
  PRIMARY KEY (`event_id`, `volunteer_id`),
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`volunteer_id`) REFERENCES `users` (`id`)
  
);