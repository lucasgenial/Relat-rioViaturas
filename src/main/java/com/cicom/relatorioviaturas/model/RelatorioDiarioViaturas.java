package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_RELATORIO_VIATURAS")
@Access(AccessType.PROPERTY)
public class RelatorioDiarioViaturas implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private Unidade unidade;
    private List<Viatura> viaturas = new ArrayList<>();

    /*
    GETTERS
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public int getId() {
        return this.id.get();
    }

    @ManyToOne(targetEntity = Unidade.class)
    @JoinColumn(name = "UNIDADE")
    public Unidade getUnidade() {
        return this.unidade;
    }
    
    @OneToMany(targetEntity = Viatura.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinTable(name = "TBL_VIATURAS_POR_RELATORIO", joinColumns = {
        @JoinColumn(name = "RELATORIO_VIATURAS_ID_FK")}, inverseJoinColumns = {
        @JoinColumn(name = "VIATURA_ID_FK")})
    public List<Viatura> getViaturas() {
        return this.viaturas;
    }

    /*
    SETTERS
     */
    public void setId(int value) {
        this.id.set(value);
    }

    public void setUnidade(Unidade value) {
        this.unidade = value;
    }

    public void setViaturas(List<Viatura> value) {
        this.viaturas = value;
    }

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RelatorioDiarioViaturas{" + "id=" + id + ", unidade=" + unidade + ", viaturas=" + viaturas + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.unidade);
        hash = 59 * hash + Objects.hashCode(this.viaturas);
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
        final RelatorioDiarioViaturas other = (RelatorioDiarioViaturas) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.unidade, other.unidade)) {
            return false;
        }
        if (!Objects.equals(this.viaturas, other.viaturas)) {
            return false;
        }
        return true;
    }

}
