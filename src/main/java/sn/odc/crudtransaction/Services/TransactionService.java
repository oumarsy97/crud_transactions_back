package sn.odc.crudtransaction.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.odc.crudtransaction.datas.Entity.Transaction;
import sn.odc.crudtransaction.datas.Entity.Transaction.TransactionType;
import sn.odc.crudtransaction.datas.Repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService extends BaseService<Transaction, TransactionRepository> {

    @Autowired
    private TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository repository) {
        super(repository);
    }

    // Validation spécifique aux transactions
    @Override
    protected void validateEntity(Transaction transaction) {
        // Validation du montant
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        // Validation de la description
        if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description est obligatoire");
        }
    }

    // Méthode spécifique : Création de transaction avec validation
    @Override
    public Transaction create(Transaction transaction) {
        // Validation avant création
        validateEntity(transaction);

        // Normalisation du montant (valeur absolue)
        transaction.normalizeAmount();
        return super.create(transaction);
    }

    // Méthodes de recherche spécifiques
    public List<Transaction> findByType(TransactionType type) {
        return transactionRepository.findByTypeAndIsActiveTrue(type);
    }



    // Méthode de recherche avancée avec specification
    public List<Transaction> searchTransactions(
            TransactionType type,
            Double minAmount,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        Specification<Transaction> spec = Specification.where(null);

        if (type != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type)
            );
        }

        if (minAmount != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount)
            );
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("transactionDate"), startDate, endDate)
            );
        }

        return findAll(spec);
    }

    // Calculer le total des revenus ou dépenses
    public Double calculateTotal(TransactionType type) {
        List<Transaction> transactions = findByType(type);
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // Calculer le solde (revenus - dépenses)
    public Double calculateBalance() {
        Double revenus = calculateTotal(TransactionType.REVENU);
        Double depenses = calculateTotal(TransactionType.DEPENSE);
        return revenus - depenses;
    }
}