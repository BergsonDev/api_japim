-- Migration para a entidade: bundle_events
-- Criado em: 2026-10-30T19:31:19.940528044

CREATE TABLE `bundle_events` (
  `event_id` bigint NOT NULL,
  `bundle_id` bigint NOT NULL,
  `created_at` timestamp DEFAULT (now()),
  `updated_at` timestamp DEFAULT (now()),
  `deleted_at` timestamp DEFAULT (now()),
PRIMARY KEY (`event_id`, `bundle_id`),
  FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  FOREIGN KEY (`bundle_id`) REFERENCES `bundles` (`id`)
);