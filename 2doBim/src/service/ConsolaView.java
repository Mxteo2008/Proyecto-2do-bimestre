/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import controller.ActivoController;
import controller.MantenimientoController;
import model.Activo;
import model.Hardware;
import model.Licencia;
import model.Periferico;
import repository.RepositorioException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Sexxxrvio
 */
public class ConsolaView {

    private final Scanner scanner = new Scanner(System.in);
    private final ActivoController activoController;
    private final MantenimientoController mantenimientoController;

    public ConsolaView(ActivoController activoController, MantenimientoController mantenimientoController) {
        this.activoController = activoController;
        this.mantenimientoController = mantenimientoController;
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opcion: ");
            try {
                procesarOpcion(opcion);
            } catch (RepositorioException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n=== Lab-Inventario ===");
        System.out.println("1. Registrar activo");
        System.out.println("2. Listar activos");
        System.out.println("3. Buscar activo por id");
        System.out.println("4. Eliminar activo");
        System.out.println("5. Costo de mantenimiento total");
        System.out.println("6. Reporte por tipo");
        System.out.println("0. Salir");
    }

    private void procesarOpcion(int opcion) throws RepositorioException {
        switch (opcion) {
            case 1: registrarActivo(); break;
            case 2: listarActivos(); break;
            case 3: buscarActivo(); break;
            case 4: eliminarActivo(); break;
            case 5: mostrarCostoTotal(); break;
            case 6: mantenimientoController.mostrarReportePorTipo(); break;
            case 0: System.out.println("Saliendo..."); break;
            default: System.out.println("Opcion invalida");
        }
    }

    private void registrarActivo() throws RepositorioException {
        System.out.println("Tipo de activo: 1) Hardware  2) Periferico  3) Licencia");
        int tipo = leerEntero("Tipo: ");
        String nombre = leerTexto("Nombre: ");
        double costoBase = leerDouble("Costo base: ");

        Activo nuevo;
        switch (tipo) {
            case 1:
                String componente = leerTexto("Tipo de componente: ");
                int vidaUtil = leerEntero("Vida util (anios): ");
                nuevo = new Hardware(0, nombre, LocalDate.now(), costoBase, componente, vidaUtil);
                break;
            case 2:
                String conexion = leerTexto("Tipo de conexion: ");
                nuevo = new Periferico(0, nombre, LocalDate.now(), costoBase, conexion);
                break;
            case 3:
                int anios = leerEntero("Vigencia (anios): ");
                double renovacion = leerDouble("Costo renovacion anual: ");
                nuevo = new Licencia(0, nombre, LocalDate.now(), costoBase,
                        LocalDate.now().plusYears(anios), renovacion);
                break;
            default:
                throw new IllegalArgumentException("Tipo invalido");
        }

        activoController.crear(nuevo);
        System.out.println("Activo registrado con id " + nuevo.getId());
    }

    private void listarActivos() throws RepositorioException {
        List<Activo> activos = activoController.listar();
        if (activos.isEmpty()) {
            System.out.println("No hay activos registrados.");
        }
        for (Activo a : activos) {
            System.out.println(a);
        }
    }

    private void buscarActivo() throws RepositorioException {
        int id = leerEntero("Id a buscar: ");
        activoController.buscar(id)
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("No existe activo con ese id."));
    }

    private void eliminarActivo() throws RepositorioException {
        int id = leerEntero("Id a eliminar: ");
        boolean eliminado = activoController.eliminar(id);
        System.out.println(eliminado ? "Eliminado." : "No se encontro el activo.");
    }

    private void mostrarCostoTotal() throws RepositorioException {
        System.out.printf("Costo de mantenimiento total: %.2f%n",
                mantenimientoController.costoMantenimientoTotal());
    }

    private int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Ingrese un numero valido.");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.println("Ingrese un numero valido.");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}

