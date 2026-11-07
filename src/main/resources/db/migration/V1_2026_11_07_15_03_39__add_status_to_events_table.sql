-- Migration para a entidade: add_status_to_events
-- Criado em: 2026-11-07T15:03:39.139634287

ALTER TABLE `events` ADD COLUMN `status_event_id` BIGINT NOT NULL AFTER `created_by_id`; 
ALTER TABLE `events` ADD CONSTRAINT `fk_events_status` FOREIGN KEY (`status_event_id`) REFERENCES `status_events`(`id`);
