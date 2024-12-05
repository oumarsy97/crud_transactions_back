package sn.odc.crudtransaction.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import sn.odc.crudtransaction.datas.Entity.BaseEntity;
import sn.odc.crudtransaction.datas.Repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public abstract class BaseService<T extends BaseEntity, R extends BaseRepository<T>> {


    protected final R repository;
    public BaseService(R repository) {
        this.repository = repository;
    }

    // Méthodes CRUD de base
    public T create(T entity) {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("L'entité ne doit pas avoir d'ID préexistant");
        }
        return repository.save(entity);
    }

    public T update(T entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("L'entité doit avoir un ID pour être mise à jour");
        }
        return repository.save(entity);
    }

    public Optional<T> findById(UUID id) {
        return repository.findByIdAndIsActiveTrue(id);
    }

    public List<T> findAll() {
        return repository.findByIsActiveTrue();
    }

    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Méthodes de recherche avec Specification
    public List<T> findAll(Specification<T> spec) {
        return repository.findAll(spec);
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    // Soft delete
    public void delete(UUID id) {
        repository.softDeleteById(id);
    }

    public void delete(T entity) {
        repository.softDelete(entity);
    }

    // Réactivation
    public void reactivate(UUID id) {
        findById(id).ifPresent(repository::reactivate);
    }

    // Vérification d'existence
    public boolean existsById(UUID id) {
        return repository.findByIdAndIsActiveTrue(id).isPresent();
    }

    // Méthode à implémenter par les sous-classes pour des validations spécifiques
    protected abstract void validateEntity(T entity);
}
