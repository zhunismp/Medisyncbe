# -- stage 1: Generate certs
FROM alpine:latest AS certs

RUN apk --update add ca-certificates

# -- stage 2: Build the app
FROM golang:1.23 AS builder

ENV GOSUMDB=off

WORKDIR /app

COPY . .

# Avoid checksum err
RUN rm go.sum

RUN go mod tidy

WORKDIR /app/cmd

RUN go build -o /app/bin/main .

FROM debian:bookworm-slim

WORKDIR /root/

COPY --from=builder /app/bin/main .

COPY --from=certs /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt

EXPOSE 8080

CMD ["./main"]
