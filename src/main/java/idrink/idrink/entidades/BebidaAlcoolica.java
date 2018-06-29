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

/**
 *
 * @author bianca
 */
@Entity
@DiscriminatorValue(value = "SIM")
@NamedQueries(
        {
            @NamedQuery(
                    name = BebidaAlcoolica.BEBIDAS_POR_TEOR,
                    query = "SELECT ba.nome, ba.teor, ba.preco FROM BebidaAlcoolica ba ORDER BY ba.teor"
            ),
            @NamedQuery(
                    name = BebidaAlcoolica.BEBIDAS_ALCOOLICAS,
                    query = "SELECT ba FROM BebidaAlcoolica ba ORDER BY ba.id"
            )
        }
)
public class BebidaAlcoolica extends Bebida implements Serializable {
    
    public static final String BEBIDAS_POR_TEOR = "selecionarPorTeor";
    public static final String BEBIDAS_ALCOOLICAS = "bebidasAlcoolicas";
    @Column(name = "PERCENT_TEOR_ALCOOL", nullable = true)
    private Float teor;

    public Float getTeor() {
        return teor;
    }

    public void setTeor(Float teor) {
        this.teor = teor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" Alcoólica: SIM\n Percentual Teor Alcoólico:");
        sb.append(this.teor);
        return sb.toString();
    }
}