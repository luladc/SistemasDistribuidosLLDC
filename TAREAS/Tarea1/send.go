package main

import (
    "context"
    "log"
    "time"
    amqp "github.com/rabbitmq/amqp091-go"
)

func main() {
    // 1. Entrar al correo (Conectar a RabbitMQ local)
    conn, _ := amqp.Dial("amqp://guest:guest@localhost:5672/")
    defer conn.Close()

    // 2. Abrir una ventanilla (Abrir canal)
    ch, _ := conn.Channel()
    defer ch.Close()

    // 3. Crear o ubicar la bandeja (Declarar la cola)
    q, _ := ch.QueueDeclare("hola", false, false, false, false, nil)

    // 4. Dejar la carta (Publicar mensaje)
    ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
    defer cancel()

    mensaje := "Hello World!"
    ch.PublishWithContext(ctx, "", q.Name, false, false, amqp.Publishing{
        ContentType: "text/plain",
        Body:        []byte(mensaje),
    })
    
    log.Printf("Mensaje enviado")
}