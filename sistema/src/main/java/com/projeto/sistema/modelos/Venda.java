package com.projeto.sistema.modelos;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Venda")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String obs;
    private double valorTotal = 0.00;
    private double quantidadeTotal = 0.00;
    private Date dataVenda = new Date();

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario; // ✅ era "funcionarios" (plural) — não bate com th:field="*{funcionario}"

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public double getQuantidadeTotal() { return quantidadeTotal; }
    public void setQuantidadeTotal(double quantidadeTotal) { this.quantidadeTotal = quantidadeTotal; }

    public Funcionario getFuncionario() { return funcionario; } // ✅ era getFuncionarios()
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; } // ✅ era setFuncionarios()
    public Date getDataVenda() {
        return dataVenda;
    }
    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}