package com.farmacia_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_ventas")
@Data
@NoArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal; // Este campo no debe ser nulo

    // Constructor personalizado para la creación de detalles de venta
    public DetalleVenta(Producto producto, Integer cantidad, Double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        // <-- AÑADIR ESTO: Calcular el subtotal inmediatamente al construir
        this.subtotal = (this.cantidad != null && this.precioUnitario != null) ?
                        (double)this.cantidad * this.precioUnitario : 0.0;
    }

    @PrePersist
    @PreUpdate
    protected void ensureSubtotalCalculation() { // Renombrado para claridad
        if (this.cantidad != null && this.precioUnitario != null) {
            this.subtotal = (double)this.cantidad * this.precioUnitario;
        } else {
            this.subtotal = 0.0;
        }
    }

    // Los getters y setters son generados por @Data de Lombok.
}