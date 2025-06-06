package com.farmacia_gestion.controller.web;

import com.farmacia_gestion.model.Cliente;
import com.farmacia_gestion.model.Producto;
import com.farmacia_gestion.model.Venta;
import com.farmacia_gestion.service.ClienteService;
import com.farmacia_gestion.service.ProductoService;
import com.farmacia_gestion.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    @Autowired
    public VentaController(VentaService ventaService, ClienteService clienteService, ProductoService productoService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listVentas(Model model) {
        model.addAttribute("ventas", ventaService.findAll());
        return "ventas/list";
    }

    @GetMapping("/new")
    public String showVentaForm(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("productos", productoService.findAll());
        return "ventas/form";
    }

    @PostMapping("/save")
    public String saveVenta(@RequestParam(value = "clienteId", required = false) Long clienteId,
                            @RequestParam Map<String, String> allRequestParams,
                            RedirectAttributes redirectAttributes) {

        Map<Long, Integer> productosConCantidades = new HashMap<>();
        for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
            if (entry.getKey().startsWith("producto_") && !entry.getValue().isEmpty()) {
                try {
                    Long productoId = Long.parseLong(entry.getKey().replace("producto_", ""));
                    Integer cantidad = Integer.parseInt(entry.getValue());
                    if (cantidad > 0) {
                        productosConCantidades.put(productoId, cantidad);
                    }
                } catch (NumberFormatException e) {
                    redirectAttributes.addFlashAttribute("error", "Error en el formato de la cantidad o ID del producto.");
                    return "redirect:/ventas/new";
                }
            }
        }

        if (productosConCantidades.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Debe agregar al menos un producto a la venta.");
            return "redirect:/ventas/new";
        }

        try {
            ventaService.createSale(clienteId, productosConCantidades);
            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ventas/new";
        }

        return "redirect:/ventas";
    }

    @GetMapping("/delete/{id}")
    public String deleteVenta(@PathVariable Long id) {
        ventaService.deleteById(id);
        return "redirect:/ventas";
    }

    @GetMapping("/search")
    public String searchVentas(@RequestParam(value = "clienteNombre", required = false) String clienteNombre,
                               Model model) {
        if (clienteNombre != null && !clienteNombre.isEmpty()) {
            model.addAttribute("ventas", ventaService.findSalesByClientName(clienteNombre));
        } else {
            model.addAttribute("ventas", ventaService.findAll());
        }
        return "ventas/list";
    }
}