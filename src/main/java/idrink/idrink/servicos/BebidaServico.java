/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.Bebida;
import java.util.List;
import javax.annotation.PostConstruct;
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
@TransactionManagement(CONTAINER)
@TransactionAttribute(REQUIRED)
public abstract class BebidaServico <T extends Bebida>{
    
    @PersistenceContext(name = "idrink_ejb", type = TRANSACTION)
    protected EntityManager entityManager;
    protected Class<T> classe;
    
    @TransactionAttribute(NOT_SUPPORTED)
    protected void setClasse(@NotNull Class<T> classe) {
        this.classe = classe;
    }
    
    @PostConstruct
    public void init() {
        setClasse((Class<T>) Bebida.class);
    }
    
    @TransactionAttribute(SUPPORTS)
    public boolean existe(@NotNull T bebida) {
        return true;
    }
    
    @TransactionAttribute(SUPPORTS)
    public abstract T criar();

    public void persistir(@Valid T bebida) {
        if (!existe(bebida)) {
            entityManager.persist(bebida);
            entityManager.flush();
        }
    }
    
    public void atualizar(@Valid T bebida) {
        if (existe(bebida)) {
            entityManager.merge(bebida);
            entityManager.flush();
            entityManager.clear();
        }
    }
    
    public void excluir(@Valid T bebida) {
        if (existe(bebida)) {
            T emb = entityManager.merge(bebida);
            entityManager.remove(emb);
            entityManager.flush();
            entityManager.clear();
        }
    }
    
    @TransactionAttribute(SUPPORTS)
    public T consultarPorId(@NotNull Long id) {
        return entityManager.find(classe, id);
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