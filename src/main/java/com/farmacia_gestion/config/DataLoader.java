package com.farmacia_gestion.config;

import com.farmacia_gestion.model.*;
import com.farmacia_gestion.repository.*;
import com.farmacia_gestion.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ClienteRepository clienteRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public DataLoader(UserService userService, RoleRepository roleRepository,
                      ClienteRepository clienteRepository, ProveedorRepository proveedorRepository,
                      ProductoRepository productoRepository, VentaRepository ventaRepository,
                      DetalleVentaRepository detalleVentaRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.clienteRepository = clienteRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Iniciando carga de datos de prueba...");

        // 1. Roles (Solo ADMINISTRADOR y VENDEDOR)
        Role adminRole = roleRepository.findByName("ADMINISTRADOR").orElse(null);
        if (adminRole == null) {
            adminRole = roleRepository.save(new Role("ADMINISTRADOR"));
            System.out.println("Rol 'ADMINISTRADOR' creado.");
        } else {
            System.out.println("Rol 'ADMINISTRADOR' ya existe.");
        }

        Role vendedorRole = roleRepository.findByName("VENDEDOR").orElse(null); // Cambiado de EMPLEADO a VENDEDOR
        if (vendedorRole == null) {
            vendedorRole = roleRepository.save(new Role("VENDEDOR")); // Cambiado de EMPLEADO a VENDEDOR
            System.out.println("Rol 'VENDEDOR' creado.");
        } else {
            System.out.println("Rol 'VENDEDOR' ya existe.");
        }

        // Recuperar los roles gestionados para asignarlos a los usuarios
        Role managedAdminRole = roleRepository.findByName("ADMINISTRADOR")
                                              .orElseThrow(() -> new RuntimeException("ADMINISTRADOR role not found after creation."));
        Role managedVendedorRole = roleRepository.findByName("VENDEDOR") // Cambiado de EMPLEADO a VENDEDOR
                                                 .orElseThrow(() -> new RuntimeException("VENDEDOR role not found after creation."));

        // 2. Usuarios
        if (userService.findByUsername("admin") == null) {
            User admin = new User("Samuel", "Aleman", "admin", "adminpass");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(managedAdminRole);
            admin.setRoles(adminRoles);
            userService.save(admin);
            System.out.println("Usuario 'admin' creado con rol ADMINISTRADOR.");
        } else {
            System.out.println("Usuario 'admin' ya existe.");
        }

        // Otros usuarios se crean como VENDEDOR
        if (userService.findByUsername("user") == null) {
            User user = new User("Juan", "Perez", "user", "userpass");
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(managedVendedorRole); // Asignado al rol VENDEDOR
            user.setRoles(userRoles);
            userService.save(user);
            System.out.println("Usuario 'user' creado con rol VENDEDOR.");
        } else {
            System.out.println("Usuario 'user' ya existe.");
        }

        if (userService.findByUsername("maria") == null) {
            User maria = new User("Maria", "Gonzalez", "maria", "mariapass");
            Set<Role> mariaRoles = new HashSet<>();
            mariaRoles.add(managedVendedorRole); // Asignado al rol VENDEDOR
            maria.setRoles(mariaRoles);
            userService.save(maria);
            System.out.println("Usuario 'maria' creado con rol VENDEDOR.");
        } else {
            System.out.println("Usuario 'maria' ya existe.");
        }

        // 3. Proveedores
        System.out.println("Cargando proveedores...");
        Proveedor proveedor1 = new Proveedor(null, "Farmaceuticos Unidos", "Juan Castro", "3101234567", "contacto@fu.com", "Calle 10 # 20-30", null);
        Proveedor proveedor2 = new Proveedor(null, "Distribuciones Salud", "Maria Lopez", "3209876543", "info@dsalud.com", "Avenida Siempre Viva 742", null);
        Proveedor proveedor3 = new Proveedor(null, "Medicina Global S.A.", "Pedro Sanchez", "3005551234", "ventas@mglobal.com", "Carrera 50 # 15-80", null);

        List<Proveedor> proveedores = proveedorRepository.saveAll(Arrays.asList(proveedor1, proveedor2, proveedor3));
        proveedor1 = proveedores.get(0);
        proveedor2 = proveedores.get(1);
        proveedor3 = proveedores.get(2);
        System.out.println("Proveedores guardados.");

        // 4. Productos
        System.out.println("Cargando productos...");
        Producto producto1 = new Producto(null, "Paracetamol 500mg", "Analgésico y antipirético", 1500.0, 100, "Analgésicos", LocalDate.of(2026, 12, 31), proveedor1);
        Producto producto2 = new Producto(null, "Amoxicilina 250mg", "Antibiótico de amplio espectro", 8500.0, 50, "Antibióticos", LocalDate.of(2025, 10, 15), proveedor1);
        Producto producto3 = new Producto(null, "Vitamina C 1000mg", "Suplemento vitamínico", 12000.0, 75, "Vitaminas", LocalDate.of(2027, 6, 1), proveedor2);
        Producto producto4 = new Producto(null, "Ibuprofeno 400mg", "Antiinflamatorio no esteroideo", 2500.0, 120, "Antiinflamatorios", LocalDate.of(2026, 8, 20), proveedor2);
        Producto producto5 = new Producto(null, "Gel Antibacterial", "Alcohol glicerinado para manos", 7000.0, 200, "Higiene", null, proveedor3);
        Producto producto6 = new Producto(null, "Mascarillas Quirúrgicas", "Protección respiratoria", 10000.0, 300, "Insumos Médicos", null, proveedor3);

        List<Producto> productos = productoRepository.saveAll(Arrays.asList(producto1, producto2, producto3, producto4, producto5, producto6));
        producto1 = productos.get(0);
        producto2 = productos.get(1);
        producto3 = productos.get(2);
        producto4 = productos.get(3);
        producto5 = productos.get(4);
        producto6 = productos.get(5);
        System.out.println("Productos guardados.");

        // 5. Clientes
        System.out.println("Cargando clientes...");
        Cliente cliente1 = new Cliente(null, "Carlos", "Gómez", "1018456789", "3001112233", "carlos.gomez@example.com", "Carrera 8 # 5-10");
        Cliente cliente2 = new Cliente(null, "Ana", "Martínez", "1019000111", "3154445566", "ana.martinez@example.com", "Calle 12 # 45-67");
        Cliente cliente3 = new Cliente(null, "Luis", "Ramírez", "1020333444", "3107778899", "luis.ramirez@example.com", "Avenida 30 # 1-99");

        List<Cliente> clientes = clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        cliente1 = clientes.get(0);
        cliente2 = clientes.get(1);
        cliente3 = clientes.get(2);
        System.out.println("Clientes guardados.");

        // 6. Ventas y Detalle de Ventas
        System.out.println("Cargando ventas...");

        // Venta 1
        Venta venta1 = new Venta(cliente1, LocalDateTime.now().minusDays(5));
        DetalleVenta dv1_1 = new DetalleVenta(producto1, 2, producto1.getPrecio());
        DetalleVenta dv1_2 = new DetalleVenta(producto3, 1, producto3.getPrecio());
        venta1.addDetalleVenta(dv1_1);
        venta1.addDetalleVenta(dv1_2);
        ventaRepository.save(venta1);
        System.out.println("Venta 1 guardada con detalles.");

        // Venta 2
        Venta venta2 = new Venta(cliente2, LocalDateTime.now().minusDays(2));
        DetalleVenta dv2_1 = new DetalleVenta(producto2, 1, producto2.getPrecio());
        DetalleVenta dv2_2 = new DetalleVenta(producto4, 3, producto4.getPrecio());
        venta2.addDetalleVenta(dv2_1);
        venta2.addDetalleVenta(dv2_2);
        ventaRepository.save(venta2);
        System.out.println("Venta 2 guardada con detalles.");

        // Venta 3 (sin cliente)
        Venta venta3 = new Venta(null, LocalDateTime.now());
        DetalleVenta dv3_1 = new DetalleVenta(producto5, 5, producto5.getPrecio());
        DetalleVenta dv3_2 = new DetalleVenta(producto6, 10, producto6.getPrecio());
        venta3.addDetalleVenta(dv3_1);
        venta3.addDetalleVenta(dv3_2);
        ventaRepository.save(venta3);
        System.out.println("Venta 3 (sin cliente) guardada con detalles.");

        System.out.println("Carga de datos de prueba finalizada exitosamente.");
    }
}