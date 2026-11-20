-- Migration para a entidade: requester_presences
-- Criado em: 2026-11-20T14:10:18.428272471

CREATE TABLE `requester_presences`(
    code VARCHAR(15) PRIMARY KEY NOT NULL,
    date TIMESTAMP NOT NULL,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`event_id`) REFERENCES `events`(`id`)
)