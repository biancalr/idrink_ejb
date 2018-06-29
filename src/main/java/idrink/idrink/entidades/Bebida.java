/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.entidades;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bianca
 */
@Entity
@Table(name = "TB_BEBIDA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ALCOOLICO",
        discriminatorType = DiscriminatorType.STRING, length = 3)
@Access(AccessType.FIELD)
@NamedQueries(
        {
            @NamedQuery(
                    name = Bebida.BEBIDA_POR_ID,
                    query = "SELECT b FROM Bebida b WHERE b.id = ?1"
            ),
            @NamedQuery(
                    name = Bebida.BEBIDA_POR_NOME,
                    query = "SELECT b FROM Bebida b WHERE b.nome = ?1"
            ),
            @NamedQuery(
                    name = Bebida.BEBIDA_POR_PRECO,
                    query = "SELECT b FROM Bebida b WHERE b.preco < ?1"
            )
        }
)
public class Bebida implements Serializable {

    public static final String BEBIDA_POR_ID = "BebidaPorId";  
    public static final String BEBIDA_POR_NOME = "BebidaPorNome";  
    public static final String BEBIDA_POR_PRECO = "BebidaPorPreco";  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "TXT_NOME")
    protected String nome;
    @NotNull
    @DecimalMin(value = "0.1", message = "{idrink.Bebida.preco}")
    @Column(name = "NUM_PRECO")
    protected Double preco;
    @NotNull
    @Min(value = 0, message = "{idrink.Bebida.estoque}")
    @Column(name = "NUM_ESTOQUE")
    protected Integer estoque;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }
    
    public void addNoEstoque(Integer alteracao) {
        this.estoque += alteracao;
    }
    
    public void subDoEstoque(Integer alteracao) {
        this.estoque -= alteracao;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bebida other = (Bebida) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bebida: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Nome:");
        sb.append(this.nome);
        sb.append("\n Preco:");
        sb.append(this.preco);
        sb.append("\n");
        return sb.toString();
    }
    
}