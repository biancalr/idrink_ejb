/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import idrink.idrink.entidades.Cartao;
import idrink.idrink.entidades.Cliente;
import idrink.idrink.entidades.Endereco;
import idrink.idrink.entidades.Pedido;
import idrink.idrink.servicos.ClienteServico;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bianca
 */
public class ClienteTest extends Teste {

    private ClienteServico clienteServico;

    public ClienteTest() {
        super();
    }

    @After
    public void tearDown() {
        clienteServico = null;
    }

    @Before
    public void setUp() throws NamingException {
        clienteServico = (ClienteServico) container.getContext().lookup("java:global/classes/ejb/ClienteServico!idrink.idrink.servicos.ClienteServico");

    }

    protected Date getData(int dia, int mes, int ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.YEAR, ano);
        return calendar.getTime();
    }

    @Test
    public void existeCliente() {
        Cliente cliente = clienteServico.criar();
        cliente.setCpf("80825728410");
        assertTrue(clienteServico.existe(cliente));
    }

    @Test
    public void persistir() {

        Cliente cliente = clienteServico.criar();
        cliente.setNome("Xuxa");
        cliente.setCpf("66666666666");
        cliente.setTelefone("3016-2564");
        cliente.setLogin("Xuxis");
        cliente.setEmail("xuxa@gmail.com");
        cliente.setSenha("xu6666");
        criarEndereco(cliente);
        criarCartao(cliente);
        
        clienteServico.persistir(cliente);
        cliente = clienteServico.consultarPorCPF(cliente.getCpf());
        assertNotNull(cliente.getId());
        assertNotNull(cliente.getCartao().getId());
        assertNotNull(cliente.getEndereco());

    }
    
    @Test
    public void getClientePorCPF() {
        assertNotNull(clienteServico.consultarPorCPF("48485484703"));
    }

    @Test
    public void atualizar() {
        //CPF de Batman Morcego
        Cliente cliente = clienteServico.consultarPorCPF("78782922306");
        cliente.setSenha("IronManSucks"); //nova senha
        cliente.setLogin("I'mBatman"); //novo login
        clienteServico.atualizar(cliente);
        cliente = clienteServico.consultarPorCPF("78782922306");
        assertEquals("IronManSucks", cliente.getSenha());
        assertEquals("I'mBatman", cliente.getLogin());
    }

    @Test
    public void excluir() {
        //CPF de Sicrano Silva
        Cliente cliente = clienteServico.consultarPorCPF("74070704400");
        assertTrue(clienteServico.existe(cliente));
        assertNotNull(cliente.getCartao().getId());
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 0; i < cliente.getPedidos().size(); i++) {
            pedidos.add(cliente.getPedidos().get(i));
        }
        assertEquals(2, pedidos.size());
        cliente.removerPedido(pedidos.get(0));
        cliente.removerPedido(pedidos.get(1));
        clienteServico.excluir(cliente);
        assertNull(clienteServico.consultarPorCPF("74070704400"));
        assertNull(cliente.getCartao().getId());
        for (int i = 0; i < cliente.getPedidos().size(); i++) {
            pedidos.add(cliente.getPedidos().get(i));
            assertNull(pedidos.get(i).getId());
        }
        
    }

    @Test
    public void persistirInvalido() {
        Cliente cliente = clienteServico.criar();
        cliente.setCpf("21276205503");
        cliente.setEmail("jose_gmail,com");//email incorreto
        cliente.setLogin("joses");
        cliente.setTelefone("99590-7123");
        cliente.setSenha("!teStE123@");

        Endereco endereco = new Endereco();
        endereco.setBairro("Engenho do Meio");
        endereco.setCep("50640120");//cep incorreto
        endereco.setCidade("Recife");
        endereco.setComplemento("Apto 301");
        endereco.setEstado("PE");//estado incorreto
        endereco.setLogradouro("Rua da Igreja");
        endereco.setNumero(120);
        cliente.setEndereco(endereco);

        try {
            clienteServico.persistir(cliente);
            assertTrue(false);
        } catch (EJBException ex) {
            assertTrue(ex.getCause() instanceof ConstraintViolationException);
            ConstraintViolationException causa
                    = (ConstraintViolationException) ex.getCause();
            int quantidadeErros = 0;
            for (ConstraintViolation erroValidacao : causa.getConstraintViolations()) {
                assertThat(erroValidacao.getMessage(),
                        CoreMatchers.anyOf(startsWith("Não pode estar em branco"),
                                startsWith("CEP invalido"), 
                                startsWith("Email invalido"), 
                                startsWith("Estado invalido")));
                quantidadeErros++;
            }
        assertEquals(3, quantidadeErros);
        }

    }
    
    @Test
    public void clientesPorCartao(){
        List<Cliente> clientes = clienteServico.consultarClientesPorCartao("VISA");
        assertEquals(1, clientes.size());
        assertTrue(clientes.get(0).getId() == 1);
    }

    private Endereco criarEndereco(Cliente cliente) {
        Endereco endereco = new Endereco();
        endereco.setCep("50690-220");
        endereco.setEstado("Pernambuco");
        endereco.setCidade("Recife");
        endereco.setBairro("Iputinga");
        endereco.setLogradouro("Rua Iolanda Rodrigues Sobral");
        endereco.setNumero(550);
        endereco.setComplemento("Apto. 109");
        cliente.setEndereco(endereco);
        return endereco;
    }

    private void criarCartao(Cliente cliente) {
        Cartao cartao = new Cartao();
        cartao.setBandeira("VISA");
        cartao.setNumero("1888828188900044");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2019);
        c.set(Calendar.MONTH, Calendar.AUGUST);
        c.set(Calendar.DAY_OF_MONTH, 04);
        cartao.setDataExpiracao(c.getTime());
        cliente.setCartao(cartao);
    }


}