/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fig.grupo7;

/**
 *
 * @author LLDC
 */

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class Protocolo {
    private Protocolo() {
    }

    static String codificar(String texto, int caracteres) {
        return codificar(texto, caracteres, -1, "");
    }

    static String codificar(String texto, int caracteres, int palabras, String paridad) {
        String textoCodificado = Base64.getEncoder().encodeToString(
                texto.getBytes(StandardCharsets.UTF_8));
        return textoCodificado + "|" + caracteres + "|" + palabras + "|" + paridad;
    }

    static Mensaje decodificar(String datos) {
        String[] partes = datos.split("\\|", -1);
        if (partes.length != 4) {
            throw new IllegalArgumentException("Formato de mensaje inválido");
        }
        String texto = new String(Base64.getDecoder().decode(partes[0]), StandardCharsets.UTF_8);
        int caracteres = Integer.parseInt(partes[1]);
        int palabras = Integer.parseInt(partes[2]);
        return new Mensaje(texto, caracteres, palabras, partes[3]);
    }

    record Mensaje(String texto, int caracteres, int palabras, String paridad) {
    }
}