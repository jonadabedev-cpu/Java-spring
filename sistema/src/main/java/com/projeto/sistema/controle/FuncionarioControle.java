package com.projeto.sistema.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.projeto.sistema.modelos.Funcionario;
import com.projeto.sistema.repositorios.FuncionarioRepositorio;
import com.projeto.sistema.repositorios.EstadoRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FuncionarioControle {

    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;

    @Autowired
    private EstadoRepositorio cidadeRepositorio;

    @GetMapping("/cadastroFuncionario")
    public ModelAndView cadastrar(Funcionario funcionario) {
        ModelAndView mv = new ModelAndView("administrativo/funcionarios/cadastro");
        mv.addObject("funcionario", funcionario);
        mv.addObject("listaCidade", cidadeRepositorio.findAll());
        return mv;
    }

    @GetMapping("/listaFuncionario")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/funcionarios/lista");
        mv.addObject("listaFuncionario", funcionarioRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarFuncionario/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Funcionario funcionario = funcionarioRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. ID: " + id));
        return cadastrar(funcionario);
    }

    @PostMapping("/removerFuncionario/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Funcionario funcionario = funcionarioRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado. ID: " + id));
        funcionarioRepositorio.delete(funcionario);
        return listar();
    }

    @PostMapping("/salvarFuncionario")
    public ModelAndView salvar(@Validated Funcionario funcionario, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(funcionario);
        }
        funcionarioRepositorio.saveAndFlush(funcionario);
        return cadastrar(new Funcionario());
    }
}