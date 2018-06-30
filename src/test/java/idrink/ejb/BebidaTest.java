/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.ejb;

import idrink.idrink.entidades.Bebida;
import idrink.idrink.entidades.BebidaAlcoolica;
import idrink.idrink.entidades.BebidaComum;
import idrink.idrink.servicos.BebidaAlcoolicaServico;
import idrink.idrink.servicos.BebidaComumServico;
import java.util.List;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author bianca
 */
public class BebidaTest extends Teste {

    private BebidaAlcoolicaServico alcoolicaServico;
    private BebidaComumServico comumServico;

    /**
     *
     * @throws NamingException
     */
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

    @Test
    public void existeBebida() {
        BebidaComum bc = comumServico.criar();
        bc.setId(new Long(3));
        assertTrue(comumServico.existe(bc));
    }

    @Test
    public void persistirBebidaAlcoolicaValida() {
        BebidaAlcoolica ba = alcoolicaServico.criar();
        ba.setNome("Hidromel");
        ba.setEstoque(20);
        ba.setPreco(37.70);
        ba.setTeor(12.5);

        alcoolicaServico.persistir(ba);
        ba = alcoolicaServico.consultarPorId(new Long(11));
        assertNotNull(ba.getId());

    }

    @Test
    public void persistirBebidaComumValida() {
        BebidaComum bc = comumServico.criar();
        bc.setNome("Leite");
        bc.setEstoque(20);
        bc.setPreco(5.5);
        bc.setAcucar(12);

        //O valor long é 11 apenas para a execução do teste isolado
        //Caso não, o valor é 12
        comumServico.persistir(bc);
        bc = comumServico.consultarPorId(new Long(11));
        assertNotNull(bc.getId());

    }

    @Test
    public void persistirBebidaComumInvalida() {
        BebidaComum bc = comumServico.criar();
        bc.setNome("Leite");
        bc.setEstoque(-1);//estoque invalido
        bc.setPreco(-3.9);//preco invalido
        bc.setAcucar(-3);//acucar invalido

        try {
            comumServico.persistir(bc);
            assertTrue(false);
        } catch (EJBException ex) {
            assertTrue(ex.getCause() instanceof ConstraintViolationException);
            ConstraintViolationException causa
                    = (ConstraintViolationException) ex.getCause();
            int quantidadeErros = 0;
            for (ConstraintViolation erroValidacao : causa.getConstraintViolations()) {
                assertThat(erroValidacao.getMessage(),
                        CoreMatchers.anyOf(startsWith("Valor de estoque invalido"),
                                startsWith("Valor do preco invalido"),
                                startsWith("Valor do acucar invalido.")));
                quantidadeErros++;
            }
            assertEquals(3, quantidadeErros);
        }

    }

    @Test
    public void persistirBebidaAlcoolicaInvalida() {
        BebidaAlcoolica ba = alcoolicaServico.criar();
        ba.setNome("Hidromel");
        ba.setEstoque(-20);//estoque invalido
        ba.setPreco(0.0);//preco invalido
        ba.setTeor(-5.0);//valor invalido

        try {
            alcoolicaServico.persistir(ba);
            assertTrue(false);
        } catch (EJBException ex) {
            assertTrue(ex.getCause() instanceof ConstraintViolationException);
            ConstraintViolationException causa
                    = (ConstraintViolationException) ex.getCause();
            int quantidadeErros = 0;
            for (ConstraintViolation erroValidacao : causa.getConstraintViolations()) {
                assertThat(erroValidacao.getMessage(),
                        CoreMatchers.anyOf(startsWith("Valor de estoque invalido"),
                                startsWith("Valor do preco invalido"),
                                startsWith("Valor do alcool invalido.")));
                quantidadeErros++;
            }
            assertEquals(3, quantidadeErros);
        }

    }

    @Test
    public void atualizar() {
        

    }

    /**
     * Refere-se às bebidas com quantidade de acucar menor que o parametro
     */
    @Test
    public void getBebidasPorAcucar() {
        List<BebidaComum> bebidas = comumServico.getBebidasPorAcucar(20);
        assertEquals(2, bebidas.size());
        
    }

    /**
     * Refere-se às bebidas com teor alcoolico menor que o parametro
     */
    @Test
    public void getBebidasPorAlcool() {
        List<BebidaAlcoolica> bebidas = alcoolicaServico.getBebidasPorTeor(30.0);
        assertEquals(3, bebidas.size());
    }

}
