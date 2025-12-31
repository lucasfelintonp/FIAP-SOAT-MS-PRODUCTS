package br.com.fiap.fastfood.inventory.infrastructure.database.adapters;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.application.dtos.GetUnitDTO;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.InventoryEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.entities.UnitEntityJPA;
import br.com.fiap.fastfood.inventory.infrastructure.database.repositories.InventoryRepository;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryAdapter implements InventoryDatasource {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryAdapter(
        InventoryRepository inventoryRepository
    ){
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<GetInventoryDTO> findAll() {
        var inventoryItems = inventoryRepository.findAll();

        return inventoryItems.stream().map(this::inventoryEntityToDto).toList();
    }

    private GetInventoryDTO inventoryEntityToDto(InventoryEntityJPA entityJPA) {
        return new GetInventoryDTO(
            entityJPA.getId(),
            entityJPA.getName(),
            unitEntityToDto(entityJPA.getUnit()),
            entityJPA.getQuantity(),
            entityJPA.getMinimumQuantity(),
            entityJPA.getNotes(),
            entityJPA.getCreatedAt(),
            entityJPA.getUpdatedAt()
        );
    }

    private GetUnitDTO unitEntityToDto(UnitEntityJPA entityJPA) {
        return new GetUnitDTO(
            entityJPA.getId(),
            entityJPA.getName(),
            entityJPA.getAbbreviation());
    }
}
