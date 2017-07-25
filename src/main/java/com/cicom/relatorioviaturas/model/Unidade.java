package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_UNIDADE")
@Access(AccessType.PROPERTY)
public class Unidade implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty comandoDeArea = new SimpleStringProperty();
    private Set<PO> pos = new HashSet<>();

    public Unidade() {
    }

    public Unidade(String nome, String comandoArea) {
        this.setNome(nome);
        this.setComandoDeArea(comandoArea);
    }

    public Unidade(String nome, String comandoArea, Set<PO> pos) {
        this.setNome(nome);
        this.setComandoDeArea(comandoArea);
        this.setPos(pos);
    }

    /*
    GETTERS
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public int getId() {
        return this.id.get();
    }

    @Basic
    @NotNull
    @Column(name = "NOME", unique = true)
    public String getNome() {
        return this.nome.get();
    }

    @Basic
    @NotNull
    @JoinColumn(name = "COMANDO_AREA")
    public String getComandoDeArea() {
        return this.comandoDeArea.get();
    }

    @ManyToMany(targetEntity = PO.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "TBL_POS_UNIDADE", joinColumns = {
        @JoinColumn(name = "UNIDADE_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PO_ID")})
    public Set<PO> getPos() {
        return this.pos;
    }

    /*
    SETTERS
     */
    public void setId(int value) {
        this.id.set(value);
    }

    public void setNome(String value) {
        this.nome.set(value);
    }

    public void setComandoDeArea(String value) {
        this.comandoDeArea.set(value);
    }

    public void setPos(Set<PO> value) {
        this.pos = value;
    }

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    public StringProperty nomeProperty() {
        return this.nome;
    }
    
    public StringProperty comandoDeAreaProperty(){
        return this.comandoDeArea;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.nome);
        hash = 31 * hash + Objects.hashCode(this.comandoDeArea);
        return hash;
    }

    @Override
    public String toString() {
        return "Unidade{" + "id=" + id + ", nome=" + nome + ", comandante=" + comandoDeArea + ", pos=" + pos + '}';
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
        final Unidade other = (Unidade) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.comandoDeArea, other.comandoDeArea)) {
            return false;
        }
        return true;
    }

}
