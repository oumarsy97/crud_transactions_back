package sn.odc.crudtransaction.Web.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.crudtransaction.Services.BaseService;
import sn.odc.crudtransaction.datas.Entity.BaseEntity;

import java.util.List;
import java.util.UUID;

public abstract class BaseController<T extends BaseEntity, S extends BaseService<T, ?>> {

    protected final S service;

    public BaseController(S service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle entité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entité créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<T> create(@RequestBody T entity) {
        T createdEntity = service.create(entity);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une entité existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entité mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    public ResponseEntity<T> update(@PathVariable UUID id, @RequestBody T entity) {
        entity.setId(id);
        T updatedEntity = service.update(entity);
        return ResponseEntity.ok(updatedEntity);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une entité par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entité trouvée"),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    public ResponseEntity<T> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les entités actives")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des entités")
    })
    public ResponseEntity<List<T>> findAll() {
        List<T> entities = service.findAll();

        return ResponseEntity.ok(entities);
    }

    @GetMapping("/page")
    @Operation(summary = "Récupérer les entités avec pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page d'entités")
    })
    public ResponseEntity<Page<T>> findAllPaginated(Pageable pageable) {
        Page<T> entities = service.findAll(pageable);
        return ResponseEntity.ok(entities);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete d'une entité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entité supprimée"),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivate")
    @Operation(summary = "Réactiver une entité")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entité réactivée"),
            @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    public ResponseEntity<Void> reactivate(@PathVariable UUID id) {
        service.reactivate(id);
        return ResponseEntity.ok().build();
    }
}