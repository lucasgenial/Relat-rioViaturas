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
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_PO")
@Access(AccessType.PROPERTY)
public class PO implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private Set<Unidade> unidades = new HashSet<>();
    private Caracteristica caracteristica;
    private Set<Funcionalidade> funcionalidades = new HashSet<>();
    private Boolean ativo;

    public PO() {
    }

    public PO(String nome) {
        this.setNome(nome);
    }

    public PO(String nome, Boolean ativo) {
        this.setNome(nome);
        this.setAtivo(ativo);
    }

    public PO(String nome, Set<Funcionalidade> funcionalidades, Caracteristica caracteristica, Boolean ativo) {
        this.setNome(nome);
        this.setAtivo(ativo);
        this.setCaracteristica(caracteristica);
        this.setFuncionalidades(funcionalidades);
    }

    public PO(String nome, Set<Unidade> unidades) {
        this.setNome(nome);
        this.setUnidades(unidades);
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

    @NotNull
    @Column(name = "NOME", unique = true)
    public String getNome() {
        return this.nome.get();
    }

    @ManyToMany(mappedBy = "pos", targetEntity = Unidade.class, fetch = FetchType.LAZY)
    public Set<Unidade> getUnidades() {
        return this.unidades;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "CARACTERISTICA")
    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection(fetch = FetchType.LAZY, targetClass = Funcionalidade.class)   
    @Column(name = "FUNCIONALIDADES", length = 30)   
    @JoinTable(name = "FUNCIONALIDADES_PO")
    public Set<Funcionalidade> getFuncionalidade() {
        return funcionalidades;
    }

    @Column(name = "ATIVO")
    public Boolean getAtivo() {
        return ativo;
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

    public void setUnidades(Set<Unidade> value) {
        this.unidades = value;
    }

    public void setCaracteristica(Caracteristica value) {
        this.caracteristica = value;
    }

    public void setFuncionalidades(Set<Funcionalidade> value) {
        this.funcionalidades = value;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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

    @Override
    public String toString() {
        return "PO{" + "id=" + id + ", nome=" + nome + ", unidades=" + unidades + ", caracteristica=" + caracteristica + ", funcionalidades=" + funcionalidades + ", ativo=" + ativo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.nome);
        hash = 23 * hash + Objects.hashCode(this.unidades);
        hash = 23 * hash + Objects.hashCode(this.caracteristica);
        hash = 23 * hash + Objects.hashCode(this.funcionalidades);
        hash = 23 * hash + Objects.hashCode(this.ativo);
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
        final PO other = (PO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.unidades, other.unidades)) {
            return false;
        }
        if (this.caracteristica != other.caracteristica) {
            return false;
        }
        if (!Objects.equals(this.funcionalidades, other.funcionalidades)) {
            return false;
        }
        if (!Objects.equals(this.ativo, other.ativo)) {
            return false;
        }
        return true;
    }

}
