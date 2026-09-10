package main

import (
    "context"
    "log"
    "os"
    "strings"
    "time"
    amqp "github.com/rabbitmq/amqp091-go"
)

func failOnError(err error, msg string) {
    if err != nil { log.Panicf("%s: %s", msg, err) }
}

func main() {
    conn, err := amqp.Dial("amqp://guest:guest@localhost:5672/")
    failOnError(err, "Fallo al conectar a RabbitMQ")
    defer conn.Close()

    ch, err := conn.Channel()
    failOnError(err, "Fallo al abrir un canal")
    defer ch.Close()

    err = ch.ExchangeDeclare("logs_topic", "topic", true, false, false, false, nil)
    failOnError(err, "Fallo al declarar el exchange")

    ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
    defer cancel()

    routingKey := "anonymous.info"
    if len(os.Args) > 1 { routingKey = os.Args[1] }
    
    body := "Hello World!"
    if len(os.Args) > 2 { body = strings.Join(os.Args[2:], " ") }

    err = ch.PublishWithContext(ctx, "logs_topic", routingKey, false, false, amqp.Publishing{
        ContentType: "text/plain",
        Body:        []byte(body),
    })
    failOnError(err, "Fallo al publicar un mensaje")

    log.Printf("Enviado %s: '%s'", routingKey, body)
}