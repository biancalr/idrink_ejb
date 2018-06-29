/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.BebidaAlcoolica;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;

/**
 *
 * @author bianca
 */
@Stateless(name = "ejb/BebidaAlcoolicaServico")
@LocalBean
@ValidateOnExecution(type = ExecutableType.ALL)
public class BebidaAlcoolicaServico extends BebidaServico<BebidaAlcoolica>{

    @PostConstruct
    public void init() {
        super.setClasse(BebidaAlcoolica.class);
    }
    
    @Override
    public BebidaAlcoolica criar() {
        return new BebidaAlcoolica();
    }

    public List<BebidaAlcoolica> getBebidasAlcoolicas(){
        return super.consultarEntidades(null, BebidaAlcoolica.BEBIDAS_ALCOOLICAS);
    }
    
    public List<BebidaAlcoolica> getBebidasPorTeor(Double teorAlcoolico){
        return super.consultarEntidades(new Object[]{teorAlcoolico}, BebidaAlcoolica.BEBIDAS_POR_TEOR);
    }

    
}
