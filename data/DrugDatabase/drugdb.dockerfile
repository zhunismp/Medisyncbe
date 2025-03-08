FROM postgres

COPY ./init.sql /docker-entrypoint-initdb.d/
COPY ./monkey.sql /docker-entrypoint-initdb.d/

RUN chmod +x /docker-entrypoint-initdb.d/init.sql
RUN chmod +x /docker-entrypoint-initdb.d/monkey.sql

EXPOSE 5432