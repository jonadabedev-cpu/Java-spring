package com.projeto.sistema.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelos.ItemEntrada;

public interface ItemEntradaRepositorio extends JpaRepository<ItemEntrada, Long> {

    List<ItemEntrada> findByEntradaId(long id);

}