package br.com.fiap.fastfood.inventory.infrastructure.interfaces;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;

import java.util.List;

public interface InventoryDatasource {

    List<GetInventoryDTO> findAll();
}
