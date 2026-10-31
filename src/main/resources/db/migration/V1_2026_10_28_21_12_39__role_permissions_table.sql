-- Migration para a entidade: role_permissions
-- Criado em: 2026-10-28T21:12:39.528893926


CREATE TABLE role_permissions (
  `role_id` BIGINT,
  `permission_id` BIGINT,
  PRIMARY KEY (`role_id`, `permission_id`),
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`)
);
