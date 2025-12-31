package br.com.fiap.fastfood.inventory.application.gateways;

import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.domain.entities.InventoryEntity;
import br.com.fiap.fastfood.inventory.domain.entities.UnitEntity;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;

import java.util.List;

public class InventoryGateway {

    private final InventoryDatasource datasource;

    public InventoryGateway(InventoryDatasource datasource) {
        this.datasource = datasource;
    }

    public List<InventoryEntity> findAll() {

        var result = datasource.findAll();

        return result.stream()
            .map(this::dtoToInventoryEntity
            ).toList();
    }

    private InventoryEntity dtoToInventoryEntity(GetInventoryDTO dto) {

        return new InventoryEntity(dto.id(),
            dto.name(),
            new UnitEntity(
                dto.unit().id(),
                dto.unit().name(),
                dto.unit().abbreviation()
            ),
            dto.quantity(),
            dto.minimum_quantity(),
            dto.notes(),
            dto.created_at(),
            dto.updated_at()
        );

    }
}
