package com.projeto.sistema.modelos;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class ItemEntrada implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double quantidade;
    private double valor;
    private double valorCusto;
    private double quantidadeTotal;
    @ManyToOne
    private Entrada entrada;
    @ManyToOne
    private Produto produto;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public double getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        this.valor = valor;
    }
    public Entrada getEntrada() {
        return entrada;
    }
    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }
    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public double getValorCusto() {
        return valorCusto;
    }
    public void setValorCusto(double valorCusto) {
        this.valorCusto = valorCusto;
    }
    public double getQuantidadeTotal() {
        return quantidadeTotal;
    }
    public void setQuantidadeTotal(double quantidadeTotal) {
        this.quantidadeTotal = quantidadeTotal;
    }
    

    
    
}
