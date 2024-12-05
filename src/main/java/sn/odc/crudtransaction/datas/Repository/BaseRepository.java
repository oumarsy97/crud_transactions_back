package sn.odc.crudtransaction.datas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import sn.odc.crudtransaction.datas.Entity.BaseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends
        JpaRepository<T, UUID>,
        JpaSpecificationExecutor<T> {

    // Recherche des entités actives
    List<T> findByIsActiveTrue();

    // Recherche par ID et actif
    Optional<T> findByIdAndIsActiveTrue(UUID id);

    // Soft delete (désactivation)
    default void softDelete(T entity) {
        System.out.println("deleting...1 " + entity.getId());
        entity.deactivate();
        save(entity);
    }

    // Soft delete par ID
    default void softDeleteById(UUID id) {
    Optional<T> entity = findByIdAndIsActiveTrue(id);
        entity.ifPresent(this::softDelete);
    }

    // Réactivation d'une entité
    default void reactivate(T entity) {
        entity.activate();
        save(entity);
    }
}