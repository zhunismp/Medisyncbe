.PHONY: up down restart logs build build-drugapi build-scheduler build-drugdb

DOCKER_COMPOSE= docker compose
COMPOSE_PATH=./docker/docker-compose.yml

start:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} up -d

stop: 
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} down

reset: stop start

logs:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} logs -f

build: build-drugapi build-scheduler build-drugdb

build-drugapi:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} build be

build-scheduler:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} build cron

build-drugdb:
	$(DOCKER_COMPOSE) -f ${COMPOSE_PATH} build drugdb