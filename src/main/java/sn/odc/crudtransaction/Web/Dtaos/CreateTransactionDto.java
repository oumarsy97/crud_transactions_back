package sn.odc.crudtransaction.Web.Dtaos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import sn.odc.crudtransaction.datas.Entity.Transaction.TransactionType;

@Getter
public class CreateTransactionDto {
    // Getters et Setters
    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le type de transaction est obligatoire")
    private TransactionType type;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double amount;

    public CreateTransactionDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public CreateTransactionDto setType(TransactionType type) {
        this.type = type;
        return this;
    }

    public CreateTransactionDto setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}