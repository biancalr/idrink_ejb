/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import idrink.idrink.entidades.Cliente;
import idrink.idrink.servicos.ClienteServico;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bianca
 */
public class ClienteTest extends Teste{
    
    private ClienteServico clienteServico;
    
    @After
    public void tearDown() {
        clienteServico = null;
    }
    
    @Before
    public void setUp() throws NamingException{
        clienteServico = (ClienteServico) container.getContext().lookup("java:global/classes/ejb/ClienteServico!idrink.idrink.servicos.ClienteServico");
        
    }
    
    @Test
    public void existeCliente(){
        Cliente cliente = clienteServico.criar();
        cliente.setCpf("50.670-210");
        assertTrue(clienteServico.existe(cliente));
    }
    
    
    
}
