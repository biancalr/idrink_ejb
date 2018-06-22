/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.Cliente;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import javax.ejb.TransactionManagement;
import static javax.ejb.TransactionManagementType.CONTAINER;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static javax.persistence.PersistenceContextType.TRANSACTION;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

/**
 *
 * @author bianca
 * @param <T>
 */
@Stateless(name = "ejb/ClienteServico")
@LocalBean
@TransactionManagement(CONTAINER)
@TransactionAttribute(REQUIRED)
public class ClienteServico <T extends Cliente>{
    
    @PersistenceContext(name = "idrink_ejb", type = TRANSACTION)
    protected EntityManager entityManager;
    protected Class<T> classe;
    
    @TransactionAttribute(NOT_SUPPORTED)
    protected void setClasse(@NotNull Class<T> classe){
        this.classe = classe;
    }
    
    @TransactionAttribute(SUPPORTS)
    public boolean existe(@NotNull T cliente){
        TypedQuery query = 
                entityManager.createNamedQuery(Cliente.CLIENTE_POR_CPF, classe);
        query.setParameter(1, cliente.getCpf());
        return !query.getResultList().isEmpty();
    }
    
    @TransactionAttribute(SUPPORTS)
    public Cliente criar(@Valid T cliente){
        return new Cliente();
    }
    
    public void persistir(@Valid T cliente){
        if(!existe(cliente)){
            entityManager.persist(cliente);
            entityManager.flush();
        }
    }
    
    public void atualizar(@Valid T cliente){
        if (existe(cliente)) {
            entityManager.merge(cliente);
            entityManager.flush();
        }
    }
    
    public void excluir(@Valid T cliente){
        if (existe(cliente)) {
            T emc = entityManager.merge(cliente);
            entityManager.remove(emc);
            entityManager.flush();
        }
    }
    
    @TransactionAttribute(SUPPORTS)
    protected T consultarPorCPF(@CPF String cpf) {
        TypedQuery<T> query = 
                entityManager.createNamedQuery(Cliente.CLIENTE_POR_CPF, classe);

        query.setParameter(1, cpf);

        return query.getSingleResult();
    }
    
    @TransactionAttribute(SUPPORTS)
    protected List<T> consultarNomesClientes(){
          return consultarEntidades(new Object[0], Cliente.CLIENTE_POR_CARTAO);

    }
    
    @TransactionAttribute(SUPPORTS)
    protected List<T> consultarClientesPorCartao(@NotBlank String bandeira){
          return consultarEntidades(new Object[] {bandeira}, Cliente.CLIENTE_POR_CARTAO);

    }
    
    @TransactionAttribute(SUPPORTS)
    protected List<T> consultarPedidoPorClientes(@NotBlank String nomeCLiente){
          return consultarEntidades(new Object[] {nomeCLiente}, Cliente.PEDIDO_POR_CLIENTE);

    }
    
    @TransactionAttribute(SUPPORTS)
    protected List<T> consultarEntidades(Object[] parametros, String nomeQuery) {
        TypedQuery<T> query = entityManager.createNamedQuery(nomeQuery, classe);

        int i = 1;
        for (Object parametro : parametros) {
            query.setParameter(i++, parametro);
        }

        return query.getResultList();
    }
    
    
}
