.PHONY: up down restart logs build

DOCKER_COMPOSE=docker compose -f docker/docker-compose.yml

up:
	$(DOCKER_COMPOSE) up -d

down:
	$(DOCKER_COMPOSE) down

restart: down up

logs:
	$(DOCKER_COMPOSE) logs -f

build:
	$(DOCKER_COMPOSE) up --build -d