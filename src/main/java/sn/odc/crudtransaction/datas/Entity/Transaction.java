package sn.odc.crudtransaction.datas.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Accessors(chain = true)
public class Transaction extends BaseEntity {

    @NotBlank(message = "La description est obligatoire")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Le type de transaction est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    @Column(name = "amount", nullable = false)
    private Double amount;



    // Enum pour définir les types de transaction
    public enum TransactionType {
        REVENU,     // Entrée d'argent
        DEPENSE     // Sortie d'argent
    }

    // Méthode de construction fluide


    // Méthode pour calculer la valeur absolue du montant
    public Transaction normalizeAmount() {
        this.amount = Math.abs(this.amount);
        return this;
    }

    // Méthode pour inverser le type de transaction
    public Transaction inverseType() {
        this.type = (this.type == TransactionType.REVENU)
                ? TransactionType.DEPENSE
                : TransactionType.REVENU;
        return this;
    }
}
