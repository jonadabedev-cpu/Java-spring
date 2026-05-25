package com.projeto.sistema.controle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.projeto.sistema.modelos.Entrada;
import com.projeto.sistema.modelos.ItemEntrada;
import com.projeto.sistema.modelos.Produto;
import com.projeto.sistema.repositorios.EntradaRepositorio;
import com.projeto.sistema.repositorios.FornecedorRepositorio;
import com.projeto.sistema.repositorios.FuncionarioRepositorio;
import com.projeto.sistema.repositorios.ItemEntradaRepositorio;
import com.projeto.sistema.repositorios.ProdutoRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EntradaControle {

    // ✅ Corrigido: nome da variável em camelCase minúsculo
    @Autowired
    private EntradaRepositorio entradaRepositorio;
    @Autowired
    private ItemEntradaRepositorio itemEntradaRepositorio;
    @Autowired
    private ProdutoRepositorio produtoRepositorio;
    @Autowired
    private FuncionarioRepositorio funcionarioRepositorio;
    @Autowired
    private FornecedorRepositorio fornecedorRepositorio;

    private List<ItemEntrada> listaItemEntrada = new ArrayList<ItemEntrada>();

    @GetMapping("/cadastroEntrada")
    public ModelAndView cadastrar(Entrada entrada, ItemEntrada itemEntrada) {
        // ✅ Corrigido: caminho do template em minúsculo
        ModelAndView mv = new ModelAndView("administrativo/entradas/cadastro");
        mv.addObject("entrada", entrada);
        mv.addObject("itemEntrada", itemEntrada);
        mv.addObject("listaItemEntrada", this.listaItemEntrada);
        mv.addObject("listaFuncionario", funcionarioRepositorio.findAll());
        mv.addObject("listaFornecedor", fornecedorRepositorio.findAll());
        mv.addObject("listaProduto", produtoRepositorio.findAll());

        return mv;
    }

    @GetMapping("/listaEntrada")
    public ModelAndView listar() {
        // ✅ Corrigido: caminho do template em minúsculo
        ModelAndView mv = new ModelAndView("administrativo/entradas/lista");
        // ✅ Corrigido: usando entradaRepositorio (minúsculo)
        mv.addObject("listantrada", entradaRepositorio.findAll());
        return mv;
    }

    // @GetMapping("/editarEntrada/{id}")
    // public ModelAndView editar(@PathVariable("id") Long id) {
     //✅ Corrigido: variável local em minúsculo + orElseThrow no lugar de get()
     //Entrada entrada = entradaRepositorio.findById(id)
     //.orElseThrow(() -> new IllegalArgumentException("Entrada não encontrada. ID:
     //" + id));
     //return cadastrar(entrada);
     //}

    // ✅ Corrigido: removido via POST para não expor ação destrutiva em GET
    // @PostMapping("/removerEntrada/{id}")
    // public ModelAndView remover(@PathVariable("id") Long id) {
    // // ✅ Corrigido: variável local em minúsculo + orElseThrow
    // Entrada entrada = entradaRepositorio.findById(id)
    // .orElseThrow(() -> new IllegalArgumentException("Entrada não encontrada. ID:
    // " + id));
    // entradaRepositorio.delete(entrada);
    // return listar();
    // }

    @PostMapping("/salvarEntradas")
    public ModelAndView salvar(String acao, @Validated Entrada entrada, ItemEntrada itemEntrada, BindingResult result) {

        if (result.hasErrors()) {
            return cadastrar(entrada, itemEntrada);
        }

        if ("itens".equals(acao)) {
            this.listaItemEntrada.add(itemEntrada);
            entrada.setValorTotal(entrada.getValorTotal() + itemEntrada.getValor() * itemEntrada.getQuantidade());
            
        } else if ("salvar".equals(acao)) {

            entradaRepositorio.saveAndFlush(entrada);

            for (ItemEntrada it : listaItemEntrada) {

                it.setEntrada(entrada);
                itemEntradaRepositorio.saveAndFlush(it);

                Produto produto = produtoRepositorio.findById(it.getProduto().getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

                produto.setEstoque(produto.getEstoque() + it.getQuantidade());
                produto.setPrecoVenda(it.getValor());
                produto.setPrecoCusto(it.getValorCusto());

                produtoRepositorio.saveAndFlush(produto);
            }

            // Limpa a lista APÓS o loop
            this.listaItemEntrada = new ArrayList<>();

            return cadastrar(new Entrada(), new ItemEntrada());
        }

        return cadastrar(entrada, new ItemEntrada());

    }

}