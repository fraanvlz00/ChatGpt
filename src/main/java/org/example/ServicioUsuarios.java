package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

public class ServicioUsuarios {
    private JsonNode root;
    private ObjectMapper mapper;

    public ServicioUsuarios() throws IOException {
        mapper = new ObjectMapper();
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("usuarios.json");
        if (inputStream == null) {
            throw new IOException("Archivo JSON no encontrado en el classpath");
        }
        root = mapper.readTree(inputStream);
    }

    public Usuario iniciarSesion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese su correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.println("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        Iterator<JsonNode> usuarios = root.get("usuarios").elements();
        while (usuarios.hasNext()) {
            JsonNode usuarioNode = usuarios.next();
            if (usuarioNode.get("correoElectronico").asText().equals(correo) &&
                    usuarioNode.get("contraseña").asText().equals(contraseña)) {
                Usuario usuario = new Usuario();
                usuario.setCorreoElectronico(correo);
                usuario.setContraseña(contraseña);
                System.out.println("Inicio de sesión exitoso.");
                return usuario;
            }
        }

        System.out.println("Correo electrónico o contraseña incorrectos.");
        return null;
    }
}
