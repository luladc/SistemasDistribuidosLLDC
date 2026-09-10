package main

import (
    "log"
    amqp "github.com/rabbitmq/amqp091-go"
)

func main() {
    // 1 y 2. Conectar y abrir ventanilla
    conn, _ := amqp.Dial("amqp://guest:guest@localhost:5672/")
    defer conn.Close()
    ch, _ := conn.Channel()
    defer ch.Close()

    // 3. Asegurar que la bandeja existe
    q, _ := ch.QueueDeclare("hola", false, false, false, false, nil)

    // 4. Quedarse escuchando (Consumir)
    mensajes, _ := ch.Consume(q.Name, "", true, false, false, false, nil)

    var forever chan struct{}
    go func() {
        for d := range mensajes {
            log.Printf(" [x] Acabo de leer: %s", d.Body)
        }
    }()

    log.Printf(" Esperando mensajes")
    <-forever
}