/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import static idrink.ejb.Teste.container;
import idrink.idrink.entidades.Bebida;
import idrink.idrink.entidades.Item;
import idrink.idrink.entidades.Pedido;
import idrink.idrink.servicos.BebidaServico;
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
 *
 * @author bianca
 */
public class PedidoTest extends Teste{
    
    private PedidoServico pedidoServico;
    private ItemServico itemServico;
    private BebidaServico bebidaServico;
    
    @Before
    public void setUp() throws NamingException {
        pedidoServico = (PedidoServico) container.getContext().lookup("java:global/classes/ejb/PedidoServico!idrink.idrink.servicos.PedidoServico");
        itemServico = (ItemServico) container.getContext().lookup("java:global/classes/ejb/ItemServico!idrink.idrink.servicos.ItemServico");
        bebidaServico = (BebidaServico) container.getContext().lookup("java:global/classes/ejb/BebidaServico!idrink.idrink.servicos.BebidaServico");
    }
    
    @After
    public void tearDown() {
        itemServico = null;
        pedidoServico = null;
    }
    
    protected Date getData(int dia, int mes, int ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.YEAR, ano);
        return calendar.getTime();
    }
    
    @Test
    public void existePedido() {
        Pedido pedido = pedidoServico.criar();
        pedido.setId(new Long(10));
        assertTrue(pedidoServico.existe(pedido));
    }

    @Test
    public void atualizar(){
        Pedido pedido = pedidoServico.consultarPorId(new Long(5));
        pedido.setDataPedido(getData(25, Calendar.JUNE, 2018));
        pedidoServico.atualizar(pedido);
        pedido = pedidoServico.consultarPorId(new Long(5));
        assertEquals(getData(25, Calendar.JUNE, 2018), pedido.getDataPedido());
    }
    
    @Test
    public void excluir() {
        Long id = (long) 12;
        Pedido pedido = pedidoServico.consultarPorId(id);
        Item item = itemServico.consultarPorId(pedido.getItensSelecionados().get(0).getId());
        pedidoServico.excluir(pedido);
        assertNull(pedidoServico.consultarPorId(pedido.getId()));
        assertNull(itemServico.consultarPorId(pedido.getItensSelecionados().get(0).getId()));
        
    }
    
    @Test
    public void excluirItem() {
        Pedido pedido = pedidoServico.consultarPorId(new Long(9));
        Item item = itemServico.consultarPorId(pedido.getItensSelecionados().get(0).getId());
        itemServico.excluir(item);
        assertNull(itemServico.consultarPorId(item.getId()));
        assertNull(pedido.getItensSelecionados().get(0).getId());
        
    }
    
    @Test
    public void persistirItem(){
        Bebida bebida = bebidaServico.consultarPorId(new Long(7));
        Item item = itemServico.criar();
        Pedido pedido = pedidoServico.consultarPorId(new Long(8));
        item.setBebida(bebida);
        item.setQuantidade(3);
        pedido.addItem(item);
        pedidoServico.atualizar(pedido);
        pedido = pedidoServico.consultarPorId(new Long(8));
        assertEquals(4, pedido.getItensSelecionados().size());
        assertTrue(pedido.getItensSelecionados().get(0).getId() == 12);
        assertTrue(pedido.getItensSelecionados().get(1).getId() == 13);
        assertTrue(pedido.getItensSelecionados().get(2).getId() == 14);
        assertTrue(pedido.getItensSelecionados().get(3).getId() == 15);
        
        
    }
    
    @Test
    public void pedidoPorData(){
        List<Pedido> pedidos = pedidoServico.consultarPedidosPorData(getData(2, Calendar.APRIL, 2018));
        assertEquals(3, pedidos.size());
        assertTrue(pedidos.get(0).getId() == 4);
        assertTrue(pedidos.get(1).getId() == 7);
        assertTrue(pedidos.get(2).getId() == 9);
        
    }
    
    
    
    
}