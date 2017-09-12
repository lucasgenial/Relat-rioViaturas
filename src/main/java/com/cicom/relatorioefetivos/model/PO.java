package com.cicom.relatorioefetivos.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private Boolean status;

    public PO() {
    }

    public PO(String nome) {
        this.setNome(nome);
    }

    public PO(String nome, Boolean ativo) {
        this.setNome(nome);
        this.setStatus(ativo);
    }

    public PO(String nome, Set<Funcionalidade> funcionalidades, Caracteristica caracteristica, Boolean ativo) {
        this.setNome(nome);
        this.setStatus(ativo);
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

    @OneToOne(targetEntity = Caracteristica.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CARACTERISTICA")
    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    @OneToMany(targetEntity = Funcionalidade.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "TBL_FUNCIONALIDADE_PO", joinColumns = {
        @JoinColumn(name = "PO_ID_FK")}, inverseJoinColumns = {
        @JoinColumn(name = "FUNCIONALIDADE_ID_FK")})
    public Set<Funcionalidade> getFuncionalidades() {
        return funcionalidades;
    }

    @Column(name = "STATUS")
    public Boolean getStatus() {
        return status;
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

    public void setStatus(Boolean status) {
        this.status = status;
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
        return "PO{" + "id=" + id + ", nome=" + nome + ", unidades=" + unidades + ", caracteristica=" + caracteristica + ", funcionalidades=" + funcionalidades + ", ativo=" + status + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.nome);
        hash = 23 * hash + Objects.hashCode(this.unidades);
        hash = 23 * hash + Objects.hashCode(this.caracteristica);
        hash = 23 * hash + Objects.hashCode(this.funcionalidades);
        hash = 23 * hash + Objects.hashCode(this.status);
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
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

}
