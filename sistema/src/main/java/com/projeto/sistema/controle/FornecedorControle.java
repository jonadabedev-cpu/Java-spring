package com.projeto.sistema.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.projeto.sistema.modelos.Fornecedor;
import com.projeto.sistema.repositorios.FornecedorRepositorio;
import com.projeto.sistema.repositorios.EstadoRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FornecedorControle {

    @Autowired
    private FornecedorRepositorio fornecedorRepositorio;

    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroFornecedor")
    public ModelAndView cadastrar(Fornecedor fornecedor) {
        ModelAndView mv = new ModelAndView("administrativo/fornecedores/cadastro");
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("listaCidade", estadoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/listaFornecedor")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/fornecedores/lista");
        mv.addObject("listaFornecedor", fornecedorRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarFornecedor/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Fornecedor fornecedor = fornecedorRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. ID: " + id));
        return cadastrar(fornecedor);
    }

    @PostMapping("/removerFornecedor/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Fornecedor fornecedor = fornecedorRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. ID: " + id));
        fornecedorRepositorio.delete(fornecedor);
        return listar();
    }

    @PostMapping("/salvarFornecedor")
    public ModelAndView salvar(@Validated Fornecedor fornecedor, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(fornecedor);
        }
        fornecedorRepositorio.saveAndFlush(fornecedor);
        return cadastrar(new Fornecedor());
    }
}