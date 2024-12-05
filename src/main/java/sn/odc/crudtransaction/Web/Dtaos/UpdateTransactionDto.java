package sn.odc.crudtransaction.Web.Dtaos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import sn.odc.crudtransaction.datas.Entity.Transaction.TransactionType;

@Getter
@Setter
public class UpdateTransactionDto {
    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le type de transaction est obligatoire")
    private TransactionType type;

    @NotNull(message = "Le montant est obligatoire")
    @PositiveOrZero(message = "Le montant doit être positif ou nul")
    private Double amount;
}
