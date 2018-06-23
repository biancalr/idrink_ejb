/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.Item;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
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

/**
 *
 * @author bianca
 * @param <T>
 */
@Stateless
@LocalBean
@TransactionManagement(CONTAINER)
@TransactionAttribute(REQUIRED)
public class ItemServico<T extends Item> {

    @PersistenceContext(name = "idrink_ejb", type = TRANSACTION)
    protected EntityManager entityManager;
    protected Class<T> classe;

    @TransactionAttribute(NOT_SUPPORTED)
    protected void setClasse(@NotNull Class<T> classe) {
        this.classe = classe;
    }

    @TransactionAttribute(SUPPORTS)
    public boolean existe(@NotNull T item) {
        TypedQuery query
                = entityManager.createNamedQuery(Item.ITEM_POR_ID, classe);
        query.setParameter(1, item.getId());
        return !query.getResultList().isEmpty();
    }

    @TransactionAttribute(SUPPORTS)
    public Item criar() {
        return new Item();
    }

    public void persistir(@Valid T item) {
        if (!existe(item)) {
            entityManager.persist(item);
            entityManager.flush();
        }
    }

    public void atualizar(@Valid T item) {
        if (existe(item)) {
            entityManager.merge(item);
            entityManager.flush();
        }
    }

    public void excluir(@Valid T item) {
        if (existe(item)) {
            T emi = entityManager.merge(item);
            entityManager.remove(emi);
            entityManager.flush();
        }
    }

    @TransactionAttribute(SUPPORTS)
    public T consultarPorId(@NotNull Long id) {
        return entityManager.find(classe, id);
    }
    
    @TransactionAttribute(SUPPORTS)
    public T consultaBebidaDoItem(@NotNull Long idItem){
        if(existe(consultarPorId(idItem))){
            return consultarEntidade(new Object[]{idItem}, Item.BEBIDA_DO_ITEM);
        }else{
            return null;
        }
    }
    
    @TransactionAttribute(SUPPORTS)
    public T consultaPedidoDoItem(@NotNull Long idItem){
        if(existe(consultarPorId(idItem))){
            return consultarEntidade(new Object[]{idItem}, 
                    "SELECT i.pedido.id FROM Item i WHERE i.id = ?1");
        }else{
            return null;
        }
    }

    @TransactionAttribute(SUPPORTS)
    public T consultarEntidade(Object[] parametros, String nomeQuery) {
        TypedQuery<T> query = entityManager.createNamedQuery(nomeQuery, classe);

        int i = 1;
        for (Object parametro : parametros) {
            query.setParameter(i++, parametro);
        }

        return query.getSingleResult();
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
