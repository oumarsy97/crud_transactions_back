package sn.odc.crudtransaction.Web.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.crudtransaction.Services.TransactionService;
import sn.odc.crudtransaction.Web.Controller.interfaces.TransactionInterfaceContl;
import sn.odc.crudtransaction.Web.Dtaos.CreateTransactionDto;
import sn.odc.crudtransaction.Web.Dtaos.UpdateTransactionDto;
import sn.odc.crudtransaction.datas.Entity.Transaction;
import sn.odc.crudtransaction.datas.Entity.Transaction.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "API de gestion des transactions")
public class TransactionController extends BaseController<Transaction, TransactionService> implements TransactionInterfaceContl {

    public TransactionController(TransactionService service) {
        super(service);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Rechercher des transactions par type")
    public ResponseEntity<List<Transaction>> getTransactionsByType(
            @PathVariable TransactionType type
    ) {
        List<Transaction> transactions = service.findByType(type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche avancée de transactions")
    public ResponseEntity<List<Transaction>> searchTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ) {
        List<Transaction> transactions = service.searchTransactions(type, minAmount, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/total/{type}")
    @Operation(summary = "Calculer le total des transactions par type")
    public ResponseEntity<Double> calculateTotal(
            @PathVariable TransactionType type
    ) {
        Double total = service.calculateTotal(type);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/balance")
    @Operation(summary = "Calculer le solde total")
    public ResponseEntity<Double> calculateBalance() {
        Double balance = service.calculateBalance();
        return ResponseEntity.ok(balance);
    }

    @Override
    @PostMapping("/createTransaction")
    @Operation(summary = "Créer une nouvelle transaction")
    public ResponseEntity<Transaction> createTransaction(
            @Valid @RequestBody CreateTransactionDto transactionDto
    ) {
        Transaction transaction = new Transaction()
                .setDescription(transactionDto.getDescription())
                .setType(transactionDto.getType())
                .setAmount(transactionDto.getAmount())
                .normalizeAmount(); // Normaliser le montant

        Transaction savedTransaction = service.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @Override
    @PatchMapping("/updateTransaction/{id}")
    @Operation(summary = "Mettre à jour partiellement une transaction")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTransactionDto transactionDto
    ) {
        Transaction transaction = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transactionDto.getDescription() != null) {
            transaction.setDescription(transactionDto.getDescription());
        }
        if (transactionDto.getType() != null) {
            transaction.setType(transactionDto.getType());
        }
        if (transactionDto.getAmount() != null) {
            transaction.setAmount(transactionDto.getAmount())
                    .normalizeAmount();
        }

        Transaction updatedTransaction = service.update(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }
}