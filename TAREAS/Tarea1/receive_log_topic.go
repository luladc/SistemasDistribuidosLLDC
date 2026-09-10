package main

import (
    "log"
    "os"
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

    // Crear una cola temporal exclusiva
    q, err := ch.QueueDeclare("", false, false, true, false, nil)
    failOnError(err, "Fallo al declarar una cola")

    if len(os.Args) < 2 {
        log.Printf("Uso: %s [binding_key]...", os.Args[0])
        os.Exit(1)
    }

    // Vincular la cola al exchange con cada patrón ingresado
    for _, s := range os.Args[1:] {
        log.Printf("Vinculando cola %s al exchange con clave %s", q.Name, s)
        err = ch.QueueBind(q.Name, s, "logs_topic", false, nil)
        failOnError(err, "Fallo al vincular la cola")
    }

    msgs, err := ch.Consume(q.Name, "", true, false, false, false, nil)
    failOnError(err, "Fallo al registrar un consumidor")

    var forever chan struct{}
    go func() {
        for d := range msgs {
            log.Printf(" [x] %s:%s", d.RoutingKey, d.Body)
        }
    }()

    log.Printf("Esperando logs. Para salir presiona CTRL+C")
    <-forever
}