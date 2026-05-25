package com.projeto.sistema.controle;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.sistema.modelos.Produto;
import com.projeto.sistema.repositorios.CidadeRepositorio;
import com.projeto.sistema.repositorios.ProdutoRepositorio;

@Controller
public class ProdutoControle {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private CidadeRepositorio cidadeRepositorio; // ✅ necessário para popular o select de cidades

    @GetMapping("/cadastroProduto")
    public ModelAndView cadastrar(Produto produto) {
        ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
        mv.addObject("produto", produto);
        mv.addObject("listaCidade", cidadeRepositorio.findAll()); // ✅ adicionado
        return mv;
    }

    @GetMapping("/listaProduto")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/produtos/lista");
        mv.addObject("listaProduto", produtoRepositorio.findAll());
        return mv;
    }

    @GetMapping("/editarProduto/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        return cadastrar(produto.get());
    }

    @PostMapping("/removerProduto/{id}") // ✅ era @GetMapping
    public ModelAndView remover(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        produtoRepositorio.delete(produto.get());
        return listar();
    }

    @PostMapping("/salvarProduto") // ✅ era "/salvarProdutos" (com 's')
    public ModelAndView salvar(@Validated Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(produto);
        }
        produtoRepositorio.saveAndFlush(produto);
        return cadastrar(new Produto());
    }
}