/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.entidades;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bianca
 */
@Entity
@Table(name = "TB_ITEM_SELECIONADO")
@NamedQueries(
        {
            @NamedQuery(
                    name = Item.ITEM_POR_ID,
                    query = "SELECT i FROM Item i WHERE i.id = ?1"
            ),
            @NamedQuery(
                    name = Item.BEBIDA_DO_ITEM,
                    query = "SELECT i.bebida.id FROM Item i WHERE i.id = ?1"
            )
        }
)
public class Item implements Serializable{
    
    public static final String ITEM_POR_ID = "ItemPorId";    
    public static final String BEBIDA_DO_ITEM = "BebidaDoItem"; 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            optional = false)
    @JoinColumn(name = "ID_PEDIDO", referencedColumnName = "ID",
            nullable = false)
    private Pedido pedido;
    @NotNull
    @Min(value = 0, message = "{idrink.Item.quantidade}")
    @Column(name = "NUM_QUANTIDADE")
    private Integer quantidade;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            optional = false, orphanRemoval = false)
    @JoinColumn(name = "ID_BEBIDA", referencedColumnName = "ID", 
            nullable = false)
    private Bebida bebida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Bebida getBebida() {
        return bebida;
    }

    public void setBebida(Bebida bebida) {
        this.bebida = bebida;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        pedido.addItem(this);
        this.pedido = pedido;
    }
    
    public boolean temBebida() {
        if (this.bebida == null) {
            return false;
        }else{
            return true;
        }
    }
    
    public void adicionarBebida(Bebida bebida, Integer quantidade){
        this.bebida = bebida;
        this.quantidade = quantidade;
        this.bebida.subDoEstoque(quantidade);
    }
    
    public void atualizarItem(Bebida bebida, Integer quantidade){
         if(temBebida() == true){
             this.removerBebida();
         }
             this.adicionarBebida(bebida, quantidade);
    }
    
    public void removerBebida(){
        this.bebida.addNoEstoque(quantidade);
        this.bebida = null;
    }
    
    public Double getSubtotal(){
        return this.bebida.getPreco() * this.quantidade;
    }


    
    
}