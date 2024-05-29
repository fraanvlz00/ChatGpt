package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServicioUsuarios servicioUsuarios = new ServicioUsuarios();
        Usuario usuario = servicioUsuarios.iniciarSesion();
        if (usuario != null) {
            new ServicioAlmuerzo().iniciar(usuario);
        }
    }
}
