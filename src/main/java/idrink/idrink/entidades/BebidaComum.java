/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

/**
 *
 * @author bianca
 */
@Entity
@DiscriminatorValue(value = "NAO")
@NamedQueries(
        {
            @NamedQuery(
                    name = BebidaComum.BEBIDAS_COMUNS,
                    query = "SELECT bc FROM BebidaComum bc ORDER BY bc.id"
            ),
            @NamedQuery(
                    name = BebidaComum.BEBIDAS_POR_ACUCAR,
                    query = "SELECT bc.nome, bc.acucar, bc.preco FROM BebidaComum bc WHERE bc.acucar < ?1 ORDER BY bc.acucar"
            )
        }
)
public class BebidaComum extends Bebida implements Serializable {

    public static final String BEBIDAS_COMUNS = "bebidasComuns";
    public static final String BEBIDAS_POR_ACUCAR = "bebidasPorAcucar";
    @Min(value = 0, message = "{idrink.BebidaComum.acucar}")
    @Column(name = "QUANT_GRAMAS_ACUCAR", nullable = true)
    private Integer acucar;
    
    public Integer getAcucar() {
        return acucar;
    }

    public void setAcucar(Integer gramas) {
        this.acucar = gramas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" Alcoólica: Nao\n Percentual Teor Açúcar:");
        sb.append(this.acucar);
        return sb.toString();
    }
}