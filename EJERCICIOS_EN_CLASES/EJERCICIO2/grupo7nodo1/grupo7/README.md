# Aplicación distribuida con sockets UDP

La aplicación está formada por tres nodos que se ejecutan como procesos independientes:

`Nodo1 → Nodo2 → Nodo3 → Nodo1`

## Ejecución

Desde la carpeta `ago20`:

1. Compilar: `javac grupo7\*.java`
2. En una terminal del computador del Nodo 3, iniciar `java grupo7.Nodo3 <IP_DEL_NODO_1>`.
3. En una terminal del computador del Nodo 2, iniciar `java grupo7.Nodo2 <IP_DEL_NODO_3>`.
4. En una tercera terminal del computador del Nodo 1, iniciar `java grupo7.Nodo1 <IP_DEL_NODO_2>`.
5. Escribir una palabra o frase en el Nodo 1.

## Puertos

- Nodo 1: `7001`
- Nodo 2: `7002`
- Nodo 3: `7003`

Cada computador debe usar la dirección IPv4 privada de la computadora destino, no `localhost` ni `127.0.0.1`.

El protocolo interno codifica el texto original con Base64 para evitar conflictos con el separador de campos y usa UTF-8 para conservar tildes y otros caracteres. El Nodo 3 considera como vocales `a, e, i, o, u`, sus vocales acentuadas y `ü`.
