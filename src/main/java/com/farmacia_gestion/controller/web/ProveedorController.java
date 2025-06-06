package com.farmacia_gestion.controller.web;

import com.farmacia_gestion.model.Proveedor;
import com.farmacia_gestion.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.findAll());
        return "proveedores/list";
    }

    @GetMapping("/new")
    public String showProveedorForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/form";
    }

    @PostMapping("/save")
    public String saveProveedor(@ModelAttribute("proveedor") Proveedor proveedor) {
        proveedorService.save(proveedor);
        return "redirect:/proveedores";
    }

    @GetMapping("/edit/{id}")
    public String editProveedorForm(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier Id:" + id));
        model.addAttribute("proveedor", proveedor);
        return "proveedores/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProveedor(@PathVariable Long id) {
        proveedorService.deleteById(id);
        return "redirect:/proveedores";
    }

    @GetMapping("/search")
    public String searchProveedores(@RequestParam(value = "nombre", required = false) String nombre, Model model) {
        if (nombre != null && !nombre.isEmpty()) {
            model.addAttribute("proveedores", proveedorService.searchByNombre(nombre));
        } else {
            model.addAttribute("proveedores", proveedorService.findAll());
        }
        return "proveedores/list";
    }
}