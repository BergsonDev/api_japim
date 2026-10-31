-- Migration para a entidade: event_speakers
-- Criado em: 2026-10-29T21:04:03.872929781
CREATE TABLE `event_speakers` (
  `event_id` BIGINT NOT NULL,
  `speaker_id` BIGINT NOT NULL,
  `count_presence` int,
  `date_inscription` timestamp DEFAULT (now()),
  PRIMARY KEY (`event_id`, `speaker_id`),
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`speaker_id`) REFERENCES `users` (`id`)
);