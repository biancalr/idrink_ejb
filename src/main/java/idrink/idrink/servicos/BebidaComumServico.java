/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.Bebida;
import idrink.idrink.entidades.BebidaComum;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bianca
 */
@Stateless(name = "ejb/BebidaComumServico")
@LocalBean
public class BebidaComumServico extends BebidaServico<BebidaComum>{
    
    @PostConstruct
    @Override
    public void init() {
        setClasse(BebidaComum.class);
    }
    
    /**
     * 
     * @return new BebidaComum()
     */
    @Override
    public BebidaComum criar() {
        return new BebidaComum();
    }
    
    /**
     *
     * @param bc
     * @return
     */
    @Override
    public boolean existe(@NotNull BebidaComum bc){
        TypedQuery query = 
                entityManager.createNamedQuery(Bebida.BEBIDA_POR_ID, classe);
        query.setParameter(1, bc.getId());
        return !query.getResultList().isEmpty();
    }

    public List<BebidaComum> getBebidasComuns(){
        return super.consultarEntidades(null, BebidaComum.BEBIDAS_COMUNS);
    }
    
    public List<BebidaComum> getBebidasPorAcucar(@Valid Integer gramasAcucar){
        return (List<BebidaComum>)super.consultarEntidades(new Object[]{gramasAcucar}, BebidaComum.BEBIDAS_POR_ACUCAR);
//        TypedQuery query = 
//                entityManager.createNamedQuery(BebidaComum.BEBIDAS_POR_ACUCAR, BebidaComum.class);
//        query.setParameter(1, gramasAcucar);
//        return (List<BebidaComum>)query.getResultList();
    }
    
}
