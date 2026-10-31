-- Migration para a entidade: roles
-- Criado em: 2026-10-28T20:59:03.737145607

CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name varchar(255) UNIQUE NOT NULL,
  initialism varchar(255) UNIQUE NOT NULL,
  description text,
  created_at timestamp DEFAULT (now()),
  updated_at timestamp DEFAULT (now()),
  deleted_at timestamp DEFAULT (now())
);
