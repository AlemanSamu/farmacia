package com.farmacia_gestion.controller.web;

import com.farmacia_gestion.model.Producto;
import com.farmacia_gestion.service.ProductoService;
import com.farmacia_gestion.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProveedorService proveedorService;

    @Autowired
    public ProductoController(ProductoService productoService, ProveedorService proveedorService) {
        this.productoService = productoService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listProductos(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "productos/list";
    }

    @GetMapping("/new")
    public String showProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("proveedores", proveedorService.findAll());
        return "productos/form";
    }

    @PostMapping("/save")
    public String saveProducto(@ModelAttribute("producto") Producto producto) {
        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String editProductoForm(@PathVariable Long id, Model model) {
        Producto producto = productoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("producto", producto);
        model.addAttribute("proveedores", proveedorService.findAll());
        return "productos/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return "redirect:/productos";
    }

    @GetMapping("/search")
    public String searchProductos(@RequestParam(value = "nombre", required = false) String nombre, Model model) {
        if (nombre != null && !nombre.isEmpty()) {
            model.addAttribute("productos", productoService.searchByNombre(nombre));
        } else {
            model.addAttribute("productos", productoService.findAll());
        }
        return "productos/list";
    }

    @GetMapping("/low-stock")
    public String showLowStockProducts(Model model) {
        model.addAttribute("productos", productoService.findLowStockProducts());
        return "productos/list";
    }
}