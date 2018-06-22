/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.servicos;

import idrink.idrink.entidades.Pedido;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.ejb.TransactionManagement;
import static javax.ejb.TransactionManagementType.CONTAINER;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 *
 * @author bianca
 */
@Stateless(name = "ejb/PedidoServico")
@LocalBean
@TransactionManagement(CONTAINER)
@TransactionAttribute(REQUIRED)
public class PedidoServico <T extends Pedido>{
    
    @PersistenceContext(name = "pedido_ejb", type = TRANSACTION)
    protected EntityManager entityManager;
    protected Class<T> classe;
    
}
