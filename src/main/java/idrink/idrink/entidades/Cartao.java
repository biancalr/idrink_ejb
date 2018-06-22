/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.entidades;

import idrink.idrink.validadores.ValidaBandeira;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bianca
 */
@Entity
@Table(name = "TB_CARTAO")
public class Cartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @ValidaBandeira
    @Column(name = "TXT_BANDEIRA")
    private String bandeira;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EXPIRACAO")
    private Date dataExpiracao;
    @NotBlank
    @Size(min = 16, max = 16, message = "{idrink.Cartao.numero}")
    @Column(name = "TXT_NUMERO")
    private String numero;

    public Long getId() {
        return id;
    }
    
    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
        final Cartao other = (Cartao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cartao: \n");
        sb.append(" Número:");
        sb.append(this.numero);
        sb.append("\n Bandeira:");
        sb.append(this.bandeira);
        sb.append("\n Data de Expiração:");
        sb.append(this.dataExpiracao);
        sb.append("\n");
        return sb.toString();
    }
    
}
