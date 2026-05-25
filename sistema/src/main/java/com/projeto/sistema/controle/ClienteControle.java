package com.projeto.sistema.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.projeto.sistema.modelos.Cliente;
import com.projeto.sistema.repositorios.ClienteRepositorio;
import com.projeto.sistema.repositorios.EstadoRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClienteControle {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroCliente")
    public ModelAndView cadastrar(Cliente cliente) {
        ModelAndView mv = new ModelAndView("administrativo/clientes/cadastro");
        mv.addObject("cliente", cliente);
        mv.addObject("listaCidade", estadoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/listaCliente")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/clientes/lista");
        mv.addObject("listaCliente", clienteRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarCliente/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. ID: " + id));
        return cadastrar(cliente);
    }

    @PostMapping("/removerCliente/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. ID: " + id));
        clienteRepositorio.delete(cliente);
        return listar();
    }

    @PostMapping("/salvarCliente")
    public ModelAndView salvar(@Validated Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(cliente);
        }
        clienteRepositorio.saveAndFlush(cliente);
        return cadastrar(new Cliente());
    }
}