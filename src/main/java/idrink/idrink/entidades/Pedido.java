/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package idrink.idrink.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bianca
 */
@Entity
@Table(name = "TB_PEDIDO")
@NamedQueries(
        {
            @NamedQuery(
                    name = Pedido.BEBIDA_DE_PEDIDO,
                    query = "SELECT p.id, i.id, i.quantidade, b FROM Pedido p, Item i, Bebida b WHERE i.bebida.id = b.id AND p.itensSelecionados.id = i.id AND i.id = ?1"
            ),
            @NamedQuery(
                    name = Pedido.PEDIDO_DONO,
                    query = "SELECT c FROM Pedido p, Cliente c WHERE c.id = p.cliente.id AND p.id = ?1"
            ),
            @NamedQuery(
                    name = Pedido.PEDIDO_CEP_ENTREGA,
                    query = "SELECT c.nome, p, e FROM Cliente c, c.endereco e, Pedido p WHERE c.pedidos.id = p.id AND e.cep = ?1"
            )
        }
)
public class Pedido implements Serializable {

    public static final String BEBIDA_DE_PEDIDO = "BebidaDePedido";    
    public static final String PEDIDO_DONO = "PedidoDono";    
    public static final String PEDIDO_CEP_ENTREGA = "CEPEntregaDoPedido";    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "DT_PEDIDO")
    @Temporal(TemporalType.DATE)
    private Date dataPedido;
    @NotNull
    @Column(name = "HR_PEDIDO")
    @Temporal(TemporalType.TIME)
    private Date horaPedido;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            orphanRemoval = true, mappedBy = "pedido")
    private List<Item> itensSelecionados;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            optional = false)
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID",
            nullable = false)
    private Cliente cliente;
    @Enumerated(EnumType.STRING)
    @Column(name = "TXT_STATUS_COMPRA", nullable = false)
    private StatusCompra statusCompra;

    public Pedido() {
        this.setDataPedido(new Date());
        this.setHoraPedido(new Date());
    }
    
    @PrePersist
    public void set() {
        this.setDataPedido(new Date());
        this.setHoraPedido(new Date());
    }

    public Long getId() {
        return id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Date getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(Date horaPedido) {
        this.horaPedido = horaPedido;
    }

    public List<Item> getItensSelecionados() {
        return itensSelecionados;
    }

    public boolean temItens() {
        return !this.itensSelecionados.isEmpty();
    }

    public void addItem(Item item) {
        if (this.itensSelecionados == null) {
            this.itensSelecionados = new ArrayList<>();
        }
        item.setPedido(this);
        this.itensSelecionados.add(item);
    }

    public boolean removerItem(Item item) {
        item.removerBebida();
        return this.itensSelecionados.remove(item);
    }
    
    public Double getTotal(){
        Double total = null;
        for (Item item : itensSelecionados) {
            total += item.getSubtotal();
        }
        return total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatusCompra getStatusCompra() {
        return statusCompra;
    }

    public void setStatusCompra(StatusCompra statusCompra) {
        this.statusCompra = statusCompra;
    }
    
    public void atualizaStatusCompra(){
        if(cliente.getCartao().getDataExpiracao().compareTo(new Date()) < 0){
            this.statusCompra = StatusCompra.NEGADO;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pedido other = (Pedido) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pedido: \n");
        sb.append(" ID:");
        sb.append(this.id);
        sb.append("\n Cliente:");
        sb.append(this.cliente.getNome());
        sb.append("\n Data do Pedido:");
        sb.append(this.dataPedido);
        sb.append("\n Hora do pedido:");
        sb.append(this.horaPedido);
        sb.append("\n Itens Selecionados: ");
        for (int i = 0; i < this.itensSelecionados.size(); i++) {
            sb.append(" \n");
            sb.append(this.itensSelecionados.get(i).toString());
        }
        return sb.toString();
    }

}
