/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import idrink.idrink.servicos.BebidaAlcoolicaServico;
import idrink.idrink.servicos.BebidaComumServico;
import idrink.idrink.servicos.BebidaServico;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bianca
 */
public class BebidaTest extends Teste{
    
    private BebidaAlcoolicaServico alcoolicaServico;
    private BebidaComumServico comumServico;
    
    
    @Before
    public void setUp() throws NamingException {
        alcoolicaServico = (BebidaAlcoolicaServico) container.getContext().lookup("java:global/classes/ejb/BebidaAlcoolicaServico!idrink.idrink.servicos.BebidaAlcoolicaServico");
        comumServico = (BebidaComumServico) container.getContext().lookup("java:global/classes/ejb/BebidaComumServico!idrink.idrink.servicos.BebidaComumServico");
    }
    
    @After
    public void tearDown() {
        alcoolicaServico = null;
        comumServico = null;
    }

    
}
