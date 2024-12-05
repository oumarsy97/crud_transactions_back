package sn.odc.crudtransaction.Web.Controller.interfaces;

import org.springframework.http.ResponseEntity;
import sn.odc.crudtransaction.Web.Dtaos.CreateTransactionDto;
import sn.odc.crudtransaction.Web.Dtaos.UpdateTransactionDto;
import sn.odc.crudtransaction.datas.Entity.Transaction;

import java.util.UUID;

public interface TransactionInterfaceContl {
    // Interface pour les méthodes du contrôleur TransactionController
    public ResponseEntity<Transaction> createTransaction(CreateTransactionDto transaction);
    public ResponseEntity<Transaction> updateTransaction(UUID id, UpdateTransactionDto transaction);
}
