package com.farmacia_gestion.controller.web;

import com.farmacia_gestion.model.Cliente;
import com.farmacia_gestion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listClientes(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        return "clientes/list";
    }

    @GetMapping("/new")
    public String showClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/save")
    public String saveCliente(@ModelAttribute("cliente") Cliente cliente) {
        if (cliente.getDni() != null && !cliente.getDni().isEmpty()) {
            Cliente existingCliente = clienteService.findByDni(cliente.getDni());
            if (existingCliente != null && (cliente.getId() == null || !existingCliente.getId().equals(cliente.getId()))) {
                return "redirect:/clientes/new?errorDniDuplicado";
            }
        }
        clienteService.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/edit/{id}")
    public String editClienteForm(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("cliente", cliente);
        return "clientes/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCliente(@PathVariable Long id) {
        clienteService.deleteById(id);
        return "redirect:/clientes";
    }

    @GetMapping("/search")
    public String searchClientes(@RequestParam(value = "nombre", required = false) String nombre, Model model) {
        if (nombre != null && !nombre.isEmpty()) {
            model.addAttribute("clientes", clienteService.searchByNombre(nombre));
        } else {
            model.addAttribute("clientes", clienteService.findAll());
        }
        return "clientes/list";
    }
}