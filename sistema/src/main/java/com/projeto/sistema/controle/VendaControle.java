package com.projeto.sistema.controle;

import com.projeto.sistema.modelos.Venda;
import com.projeto.sistema.modelos.ItemVenda;
import com.projeto.sistema.modelos.Produto;
import com.projeto.sistema.repositorios.VendaRepositorio;
import com.projeto.sistema.repositorios.ItemVendaRepositorio;
import com.projeto.sistema.repositorios.ProdutoRepositorio;
import com.projeto.sistema.repositorios.ClienteRepositorio;
import com.projeto.sistema.repositorios.FuncionarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaControle {

    private final VendaRepositorio vendaRepositorio;
    private final ItemVendaRepositorio itemVendaRepositorio;
    private final ProdutoRepositorio produtoRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final FuncionarioRepositorio funcionarioRepositorio;

    private final List<ItemVenda> listaItemVenda = new ArrayList<>();

    private enum Acao { ITENS, SALVAR }

    @GetMapping("/cadastro")
    public String exibirCadastro(Model model) {
        popularModelo(model, new Venda(), new ItemVenda());
        return "administrativo/vendas/cadastro";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("listaVenda", vendaRepositorio.findAll());
        return "administrativo/vendas/lista";
    }

    @PostMapping("/salvar")
    public String salvar(
            @RequestParam Acao acao,
            @Validated Venda venda,
            BindingResult vendaResult,
            @Validated ItemVenda itemVenda,
            BindingResult itemResult,
            Model model,
            RedirectAttributes redirectAttrs
    ) {
        if (vendaResult.hasErrors() || (acao == Acao.ITENS && itemResult.hasErrors())) {
            popularModelo(model, venda, itemVenda);
            return "administrativo/vendas/cadastro";
        }

        if (acao == Acao.ITENS) {
            itemVenda.setSubtotal(itemVenda.getValor() * itemVenda.getQuantidade());
            listaItemVenda.add(itemVenda);
            venda.setValorTotal(venda.getValorTotal() + itemVenda.getSubtotal());
            popularModelo(model, venda, new ItemVenda());
            return "administrativo/vendas/cadastro";
        }

        if (acao == Acao.SALVAR) {
            try {
                vendaRepositorio.saveAndFlush(venda);

                for (ItemVenda item : listaItemVenda) {
                    item.setVenda(venda);
                    itemVendaRepositorio.saveAndFlush(item);

                    Produto produto = produtoRepositorio.findById(item.getProduto().getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Produto não encontrado. ID: " + item.getProduto().getId()));

                    produto.setEstoque(produto.getEstoque() - item.getQuantidade());
                    produtoRepositorio.saveAndFlush(produto);
                }

                listaItemVenda.clear();
                redirectAttrs.addFlashAttribute("mensagem", "Venda salva com sucesso!");
                return "redirect:/vendas/cadastro";

            } catch (Exception e) {
                model.addAttribute("erro", "Erro ao salvar venda: " + e.getMessage());
                popularModelo(model, venda, new ItemVenda());
                return "administrativo/vendas/cadastro";
            }
        }

        return "redirect:/vendas/cadastro";
    }

    private void popularModelo(Model model, Venda venda, ItemVenda itemVenda) {
        model.addAttribute("venda", venda);
        model.addAttribute("itemVenda", itemVenda);
        model.addAttribute("listaItemVenda", listaItemVenda);
        model.addAttribute("listaCliente", clienteRepositorio.findAll());
        model.addAttribute("listaFuncionario", funcionarioRepositorio.findAll());
        model.addAttribute("listaProduto", produtoRepositorio.findAll());
    }
}