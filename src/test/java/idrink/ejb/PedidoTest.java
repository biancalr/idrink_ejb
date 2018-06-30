/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import static idrink.ejb.Teste.container;
import idrink.idrink.entidades.BebidaAlcoolica;
import idrink.idrink.entidades.BebidaComum;
import idrink.idrink.entidades.Cliente;
import idrink.idrink.entidades.Item;
import idrink.idrink.entidades.Pedido;
import idrink.idrink.entidades.StatusCompra;
import idrink.idrink.servicos.BebidaAlcoolicaServico;
import idrink.idrink.servicos.BebidaComumServico;
import idrink.idrink.servicos.ClienteServico;
import idrink.idrink.servicos.ItemServico;
import idrink.idrink.servicos.PedidoServico;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * java.lang.StackOverflowError
 *	at idrink.idrink.entidades.Item.setPedido(Item.java:90)
 *	at idrink.idrink.entidades.Pedido.addItem(Pedido.java:137)
 * 
 * 
 *
 * @author bianca
 */
public class PedidoTest extends Teste {

    private PedidoServico pedidoServico;
    private ItemServico itemServico;
    private BebidaAlcoolicaServico alcoolicaServico;
    private BebidaComumServico comumServico;
    private ClienteServico clienteServico;
    
    /**
     * 
     * @throws NamingException 
     */
    @Before
    public void setUp() throws NamingException {
        pedidoServico = (PedidoServico) container.getContext().lookup("java:global/classes/ejb/PedidoServico!idrink.idrink.servicos.PedidoServico");
        clienteServico = (ClienteServico) container.getContext().lookup("java:global/classes/ejb/ClienteServico!idrink.idrink.servicos.ClienteServico");
        itemServico = (ItemServico) container.getContext().lookup("java:global/classes/ejb/ItemServico!idrink.idrink.servicos.ItemServico");
        alcoolicaServico = (BebidaAlcoolicaServico) container.getContext().lookup("java:global/classes/ejb/BebidaAlcoolicaServico!idrink.idrink.servicos.BebidaAlcoolicaServico");
        comumServico = (BebidaComumServico) container.getContext().lookup("java:global/classes/ejb/BebidaComumServico!idrink.idrink.servicos.BebidaComumServico");
    }
    
    @After
    public void tearDown() {
        itemServico = null;
        pedidoServico = null;
        alcoolicaServico = null;
        comumServico = null;
        clienteServico = null;
    }

    /**
     * 
     * @param dia
     * @param mes
     * @param ano
     * @return The data
     */
    protected Date getData(int dia, int mes, int ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.YEAR, ano);
        return calendar.getTime();
    }

    /**
     * Verifica a existencia de um pedido
     */
    @Test
    public void existePedido() {
        Pedido pedido = pedidoServico.criar();
        pedido.setId(new Long(10));
        assertTrue(pedidoServico.existe(pedido));
    }

    /**
     * Verifica quais pedidos foram feitos naquela data
     */
    @Test
    public void pedidoPorData() {
        List<Pedido> pedidos = pedidoServico.consultarPedidosPorData(getData(2, Calendar.APRIL, 2018));
        assertEquals(3, pedidos.size());
        assertTrue(pedidos.get(0).getId() == 4);
        assertTrue(pedidos.get(1).getId() == 7);
        assertTrue(pedidos.get(2).getId() == 9);
    }
    
    @Test
    public void adicionarPedido() {
        
    }
    
    @Test
    public void atualizar() {

    }

    @Test
    public void excluir() {

    }

    @Test
    public void excluirItem() {

    }

    @Test
    public void adicionarItemAoPedido() {

    }



}
