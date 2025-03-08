.PHONY: up down restart logs build

# DOCKER_COMPOSE=docker compose -f docker/docker-compose.yml
DOCKER_COMPOSE= docker compose
COMPOSE_PATH=./docker/docker-compose.yml

start:
	$(DOCKER_COMPOSE) up -d

stop:
	$(DOCKER_COMPOSE) down

reset: down up

logs:
	$(DOCKER_COMPOSE) logs -f

build:
	build-drugapi build-scheduler build-drug-db

build-drugapi:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} build be

build-scheduler:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} build cron

build-drugdb:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} build drugdb