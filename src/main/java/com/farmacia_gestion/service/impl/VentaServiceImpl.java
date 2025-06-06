package com.farmacia_gestion.service.impl;

import com.farmacia_gestion.model.Venta;
import com.farmacia_gestion.model.DetalleVenta;
import com.farmacia_gestion.model.Producto;
import com.farmacia_gestion.model.Cliente;
import com.farmacia_gestion.repository.VentaRepository;
import com.farmacia_gestion.repository.DetalleVentaRepository;
import com.farmacia_gestion.repository.ProductoRepository;
import com.farmacia_gestion.repository.ClienteRepository;
import com.farmacia_gestion.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository,
                            DetalleVentaRepository detalleVentaRepository,
                            ProductoRepository productoRepository,
                            ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    @Transactional
    public Venta save(Venta venta) {
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                detalle.setVenta(venta);
            }
        }
        venta.calculateTotal();
        return ventaRepository.save(venta);
    }

    @Override
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public List<Venta> findSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return ventaRepository.findByFechaVentaBetween(startDate, endDate);
    }

    @Override
    public List<Venta> findSalesByClientName(String clientName) {
        return ventaRepository.findByClienteNombreContainingIgnoreCase(clientName);
    }

    @Override
    @Transactional
    public Venta createSale(Long clienteId, Map<Long, Integer> productosConCantidades) {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDateTime.now());

        if (clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
            venta.setCliente(cliente);
        }

        List<DetalleVenta> detalles = new ArrayList<>();
        double totalVenta = 0.0;

        for (Map.Entry<Long, Integer> entry : productosConCantidades.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
    //        detalle.calculateSubtotal();

            detalles.add(detalle);
            totalVenta += detalle.getSubtotal();

            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
        }

        venta.setDetalles(detalles);
        venta.setTotal(totalVenta);

        return ventaRepository.save(venta);
    }
}