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

/**
 *
 * @author bianca
 */
@Entity
@DiscriminatorValue(value = "NAO")
public class BebidaComum extends Bebida implements Serializable {

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