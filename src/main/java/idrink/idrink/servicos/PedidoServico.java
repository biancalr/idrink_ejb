/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.Cliente;
import idrink.idrink.entidades.Pedido;
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
@Stateless(name = "ejb/PedidoServico")
@LocalBean
@TransactionManagement(CONTAINER)
@TransactionAttribute(REQUIRED)
public class PedidoServico<T extends Pedido> {

    @PersistenceContext(name = "idrink_ejb", type = TRANSACTION)
    protected EntityManager entityManager;
    protected Class<T> classe;

    @TransactionAttribute(NOT_SUPPORTED)
    protected void setClasse(@NotNull Class<T> classe) {
        this.classe = classe;
    }

    @TransactionAttribute(SUPPORTS)
    public boolean existe(@NotNull T pedido) {
        TypedQuery query
                = entityManager.createNamedQuery(Pedido.PEDIDO_POR_ID, classe);
        query.setParameter(1, pedido.getId());
        return !query.getResultList().isEmpty();
    }

    @TransactionAttribute(SUPPORTS)
    public Pedido criar() {
        return new Pedido();
    }

    public void persistir(@Valid T pedido) {
        if (!existe(pedido)) {
            entityManager.persist(pedido);
            entityManager.flush();
        }
    }

    public void atualizar(@Valid T pedido) {
        if (existe(pedido)) {
            entityManager.merge(pedido);
            entityManager.flush();
        }
    }

    public void excluir(@Valid T pedido) {
        if (existe(pedido)) {
            T emp = entityManager.merge(pedido);
            entityManager.remove(emp);
            entityManager.flush();
        }
    }

    @TransactionAttribute(SUPPORTS)
    public T consultarPorId(@NotNull Long id) {
        return entityManager.find(classe, id);
    }

    @TransactionAttribute(SUPPORTS)
    public T consultarDono(@NotNull Long idPedido) {
        return consultarEntidade(new Object[]{idPedido}, Pedido.PEDIDO_DONO);
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
