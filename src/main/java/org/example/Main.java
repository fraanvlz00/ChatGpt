package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Cargar el archivo JSON usando ClassLoader
            ClassLoader classLoader = Main.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("menu.json");
            if (inputStream == null) {
                throw new IOException("Archivo JSON no encontrado en el classpath");
            }
            JsonNode root = mapper.readTree(inputStream);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese el día (lunes, martes, miércoles, jueves, viernes): ");
            String dia = scanner.nextLine().toLowerCase();
            if (root.has(dia)) {
                JsonNode diaNode = root.get(dia);
                Almuerzo almuerzo = new Almuerzo();
                almuerzo.setDia(dia);

                almuerzo.setVegetariano(loadMenu(diaNode.get("vegetariano")));
                almuerzo.setEconomico(loadMenu(diaNode.get("economico")));
                almuerzo.setEjecutivo(loadMenu(diaNode.get("ejecutivo")));
                almuerzo.setBaes(loadMenu(diaNode.get("baes")));

                System.out.println("Ingrese el tipo de menú (vegetariano, economico, ejecutivo, baes): ");
                String tipoMenu = scanner.nextLine().toLowerCase();

                Menu menu = null;
                switch (tipoMenu) {
                    case "vegetariano":
                        menu = almuerzo.getVegetariano();
                        break;
                    case "economico":
                        menu = almuerzo.getEconomico();
                        break;
                    case "ejecutivo":
                        menu = almuerzo.getEjecutivo();
                        break;
                    case "baes":
                        menu = almuerzo.getBaes();
                        break;
                    default:
                        System.out.println("Tipo de menú no válido.");
                        System.exit(1);
                }

                if (menu != null) {
                    selectOption(scanner, "bebestibles", menu.getBebestibles());
                    selectOption(scanner, "plato de fondo", menu.getPlatoDeFondo());
                    selectOption(scanner, "ensalada", menu.getEnsalada());
                    selectOption(scanner, "postre", menu.getPostre());
                    selectOption(scanner, "sopa", menu.getSopa());
                    selectOption(scanner, "acompañamiento", menu.getAcompañamiento());
                }

            } else {
                System.out.println("El día no es válido.");
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Menu loadMenu(JsonNode node) {
        Menu menu = new Menu();
        if (node != null) {
            menu.setBebestibles(loadArray(node.get("bebestibles")));
            menu.setPlatoDeFondo(loadArray(node.get("platoDeFondo")));
            menu.setEnsalada(loadArray(node.get("ensalada")));
            menu.setPostre(loadArray(node.get("postre")));
            menu.setSopa(loadArray(node.get("sopa")));
            menu.setAcompañamiento(loadArray(node.get("acompañamiento")));
        }
        return menu;
    }

    private static String[] loadArray(JsonNode arrayNode) {
        if (arrayNode != null) {
            Iterator<JsonNode> elements = arrayNode.elements();
            String[] array = new String[arrayNode.size()];
            int i = 0;
            while (elements.hasNext()) {
                array[i++] = elements.next().asText();
            }
            return array;
        }
        return null;
    }

    private static void selectOption(Scanner scanner, String category, String[] options) {
        if (options != null && options.length > 0) {
            System.out.println("Seleccione " + category + ": ");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consumir nueva línea
            if (choice > 0 && choice <= options.length) {
                System.out.println("Usted ha seleccionado: " + options[choice - 1] + " para " + category);
            } else {
                System.out.println("Selección no válida para " + category);
            }
        }
    }
}


