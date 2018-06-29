/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.BebidaComum;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.Valid;

/**
 *
 * @author bianca
 */
@Stateless(name = "ejb/BebidaComumServico")
@LocalBean
public class BebidaComumServico extends BebidaServico<BebidaComum>{

    @PostConstruct
    public void init() {
        super.setClasse(BebidaComum.class);
    }
    
    @Override
    public BebidaComum criar() {
        return new BebidaComum();
    }

    public List<BebidaComum> getBebidasComuns(){
        return super.consultarEntidades(null, BebidaComum.BEBIDAS_COMUNS);
    }
    
    public List<BebidaComum> getBebidasPorAcucar(@Valid Integer gramasAcucar){
        return super.consultarEntidades(new Object[]{gramasAcucar}, BebidaComum.BEBIDAS_POR_ACUCAR);
    }
    
}
