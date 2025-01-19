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

EXPOSE 8080

CMD ["./main"]
