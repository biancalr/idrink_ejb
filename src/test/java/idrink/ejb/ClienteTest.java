/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import idrink.idrink.entidades.Cartao;
import idrink.idrink.entidades.Cliente;
import idrink.idrink.entidades.Endereco;
import idrink.idrink.servicos.ClienteServico;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.persistence.TypedQuery;
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
        cliente.setCpf("50.670-210");
        assertTrue(clienteServico.existe(cliente));
    }

    @Test
    public void persistir() {

        Cliente cliente = clienteServico.criar();
        cliente.setCpf("212.762.055-03");
        cliente.setEmail("jose@gmail.com");
        cliente.setLogin("joses");
        cliente.setTelefone("995907123");
        cliente.setSenha("!teStE123@");

        Endereco endereco = new Endereco();
        endereco.setBairro("Engenho do Meio");
        endereco.setCep("50.640-120");
        endereco.setCidade("Recife");
        endereco.setComplemento("Apto 301");
        endereco.setEstado("PE");
        endereco.setLogradouro("Rua da Igreja");
        endereco.setNumero(120);
        cliente.setEndereco(endereco);

        Cartao cartao = new Cartao();
        cartao.setNumero("5462014816361274");
        cartao.setDataExpiracao(getData(15, Calendar.FEBRUARY, 2022));
        cartao.setBandeira("SODEXO");
        cliente.setCartao(cartao);

        if (!clienteServico.existe(cliente)) {
            clienteServico.persistir(cliente);
            assertNotNull(cliente.getId());

        }

    }

    @Test
    public void atualizar() {
        Cliente cliente = clienteServico.consultarPorId(new Long(1));
        cliente.setSenha("outraSenha");
        clienteServico.atualizar(cliente);
        cliente = clienteServico.consultarPorId(new Long(1));
        assertEquals("outraSenha", cliente.getSenha());
    }

    @Test
    public void excluir() {
        Cliente cliente = clienteServico.consultarPorId(new Long(2));
        clienteServico.excluir(cliente);
        assertNull(clienteServico.consultarPorId(new Long(2)));
    }

    @Test
    public void persistirInValido() {
        Cliente cliente = clienteServico.criar();
        cliente.setCpf("212.762.055-03");
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
            for (ConstraintViolation erroValidacao : causa.getConstraintViolations()) {
                assertThat(erroValidacao.getMessage(),
                        CoreMatchers.anyOf(startsWith("Email invalido"),
                                startsWith("CEP invalido"), 
                                startsWith("Estado invalido")));
            }
        }

    }
    
    @Test
    public void clientesPorCartao(){
        List<Cliente> clientes = clienteServico.consultarClientes("AMEX");
        assertEquals(clientes.size(), 2);
        assertTrue(clientes.get(0).getId() == 4);
        assertTrue(clientes.get(0).getId() == 6);
    }


}
