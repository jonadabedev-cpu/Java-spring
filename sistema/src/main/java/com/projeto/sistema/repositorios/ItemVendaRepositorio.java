package com.projeto.sistema.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelos.ItemVenda;

public interface ItemVendaRepositorio extends JpaRepository<ItemVenda, Long> {

    List<ItemVenda> findByVenda_Id(Long id);

}