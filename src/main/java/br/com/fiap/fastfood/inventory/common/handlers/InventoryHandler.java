package br.com.fiap.fastfood.inventory.common.handlers;

import br.com.fiap.fastfood.inventory.application.controllers.InventoryController;
import br.com.fiap.fastfood.inventory.application.dtos.GetInventoryDTO;
import br.com.fiap.fastfood.inventory.infrastructure.interfaces.InventoryDatasource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Estoque", description = "Operações relacionadas ao itens de estoque")
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryHandler {

    private final InventoryController controller;

    public InventoryHandler(
        InventoryDatasource inventoryDatasource
    ) {
        this.controller = new InventoryController(
            inventoryDatasource
        );
    }

    @Operation(summary = "Buscar item de estoque", description = "Busca uma lista dos itens de estoque")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Itens encontrados"),
        @ApiResponse(responseCode = "404", description = "Nenhum estoque encontrado")
    })
    @GetMapping
    public ResponseEntity<List<GetInventoryDTO>> searchInventory() {
        var items = controller.searchInventory();

        return ResponseEntity.ok(items);
    }
}
