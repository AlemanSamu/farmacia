package com.farmacia_gestion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta(Cliente cliente, LocalDateTime fechaVenta) {
        this.cliente = cliente;
        this.fechaVenta = fechaVenta;
        this.total = 0.0;
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaVenta == null) {
            this.fechaVenta = LocalDateTime.now();
        }
        calculateTotal();
    }

    public void addDetalleVenta(DetalleVenta detalle) {
        if (this.detalles == null) { // Asegurar que la lista no sea null
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
        detalle.setVenta(this);
        calculateTotal();
    }

    public void removeDetalleVenta(DetalleVenta detalle) {
        if (this.detalles != null) {
            this.detalles.remove(detalle);
            detalle.setVenta(null);
            calculateTotal();
        }
    }

    public void calculateTotal() {
        if (this.detalles == null || this.detalles.isEmpty()) { // <--- AÑADIR ESTA VERIFICACIÓN
            this.total = 0.0;
            return;
        }
        this.total = this.detalles.stream()
                             .mapToDouble(DetalleVenta::getSubtotal)
                             .sum();
    }
}