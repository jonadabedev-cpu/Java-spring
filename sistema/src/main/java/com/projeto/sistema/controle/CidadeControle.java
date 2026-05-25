package com.projeto.sistema.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.projeto.sistema.modelos.Cidade;
import com.projeto.sistema.repositorios.CidadeRepositorio;
import com.projeto.sistema.repositorios.EstadoRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CidadeControle {

    // ✅ Corrigido: nome da variável em camelCase minúsculo
    @Autowired
    private CidadeRepositorio cidadeRepositorio;
    @Autowired
    private EstadoRepositorio estadoRepositorio;

    @GetMapping("/cadastroCidade")
    public ModelAndView cadastrar(Cidade cidade) {
        // ✅ Corrigido: caminho do template em minúsculo
        ModelAndView mv = new ModelAndView("administrativo/cidades/cadastro");
        mv.addObject("cidade", cidade);
        mv.addObject("listaEstado", estadoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/listaCidade")
    public ModelAndView listar() {
        // ✅ Corrigido: caminho do template em minúsculo
        ModelAndView mv = new ModelAndView("administrativo/cidades/lista");
        // ✅ Corrigido: usando cidadeRepositorio (minúsculo)
        mv.addObject("listaCidade", cidadeRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarCidade/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        // ✅ Corrigido: variável local em minúsculo + orElseThrow no lugar de get()
        Cidade cidade = cidadeRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada. ID: " + id));
        return cadastrar(cidade);
    }

    // ✅ Corrigido: removido via POST para não expor ação destrutiva em GET
    @PostMapping("/removerCidade/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        // ✅ Corrigido: variável local em minúsculo + orElseThrow
        Cidade cidade = cidadeRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cidade não encontrada. ID: " + id));
        cidadeRepositorio.delete(cidade);
        return listar();
    }

    @PostMapping("/salvarCidades")
    // ✅ Corrigido: parâmetro em minúsculo
    public ModelAndView salvar(@Validated Cidade cidade, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(cidade);
        }
        cidadeRepositorio.saveAndFlush(cidade);
        return cadastrar(new Cidade());
    }
}
