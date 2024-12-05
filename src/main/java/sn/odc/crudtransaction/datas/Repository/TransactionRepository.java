package sn.odc.crudtransaction.datas.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import sn.odc.crudtransaction.datas.Entity.Transaction;
import sn.odc.crudtransaction.datas.Entity.Transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends BaseRepository<Transaction> {

    // Recherche par type de transaction
    List<Transaction> findByTypeAndIsActiveTrue(TransactionType type);

    // Recherche par plage de dates


    // Méthode de specification pour filtrer les transactions
    default Specification<Transaction> hasType(TransactionType type) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), type);
    }

    // Méthode de specification pour filtrer par montant
    default Specification<Transaction> amountGreaterThan(Double amount) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("amount"), amount);
    }

    // Méthode combinant des specifications
    default List<Transaction> findTransactions(
            TransactionType type,
            Double minAmount,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        Specification<Transaction> spec = Specification.where(null);

        if (type != null) {
            spec = spec.and(hasType(type));
        }

        if (minAmount != null) {
            spec = spec.and(amountGreaterThan(minAmount));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("transactionDate"), startDate, endDate)
            );
        }

        return findAll(spec);
    }
}