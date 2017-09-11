package com.cicom.relatorioefetivos.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "TBL_RELATORIO_EFETIVO")
@Access(AccessType.PROPERTY)
public class RelatorioDiarioEfetivo implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private Unidade unidade;
    private Set<Efetivo> efetivos = new HashSet<>();

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
    
    @OneToMany(targetEntity = Efetivo.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(name = "TBL_EFETIVO_POR_RELATORIO", joinColumns = {
        @JoinColumn(name = "RELATORIO_EFETIVO_ID_FK")}, inverseJoinColumns = {
        @JoinColumn(name = "EFETIVO_ID_FK")})
    public Set<Efetivo> getEfetivos() {
        return this.efetivos;
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

    public void setEfetivos(Set<Efetivo> value) {
        this.efetivos = value;
    }

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RelatorioDiarioEfetivos{" + "id=" + id + ", unidade=" + unidade + ", efetivo=" + efetivos + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.unidade);
        hash = 59 * hash + Objects.hashCode(this.efetivos);
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
        final RelatorioDiarioEfetivo other = (RelatorioDiarioEfetivo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.unidade, other.unidade)) {
            return false;
        }
        if (!Objects.equals(this.efetivos, other.efetivos)) {
            return false;
        }
        return true;
    }

}
