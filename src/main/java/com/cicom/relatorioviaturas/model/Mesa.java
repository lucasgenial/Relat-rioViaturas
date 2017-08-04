package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_MESA")
@Access(AccessType.PROPERTY)
public class Mesa implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private Set<Unidade> unidades = new HashSet<>();
    private TipoMesa tipoMesa = new TipoMesa();
    private Boolean ativo;

    public Mesa() {
    }

    public Mesa(String nome, TipoMesa tipo, Boolean ativo) {
        this.setNome(nome);
        this.setTipoMesa(tipo);
        this.setAtivo(ativo);
    }

    public Mesa(String nome, Set<Unidade> unidades, TipoMesa tipo, Boolean ativo) {
        this.setNome(nome);
        this.setUnidades(unidades);
        this.setTipoMesa(tipo);
        this.setAtivo(ativo);
    }

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "TBL_MESA_UNIDADE",
            joinColumns = {
                @JoinColumn(name = "MESA_ID_FK")},
            inverseJoinColumns = {
                @JoinColumn(name = "UNIDADE_ID_FK")})
    public Set<Unidade> getUnidades() {
        return this.unidades;
    }

    @OneToOne(targetEntity = TipoMesa.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "TIPO_MESA")
    public TipoMesa getTipoMesa() {
        return this.tipoMesa;
    }

    @Column(name = "ATIVO")
    public Boolean isAtivo() {
        return ativo;
    }

    public void setId(int value) {
        this.id.set(value);
    }

    public void setNome(String value) {
        this.nome.set(value);
    }

    public void setUnidades(Set<Unidade> value) {
        this.unidades = value;
    }

    public void setTipoMesa(TipoMesa value) {
        this.tipoMesa = value;
    }

    public void setAtivo(Boolean value) {
        this.ativo = value;
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
        return "Mesa{" + "id=" + id + ", nome=" + nome + ", unidades=" + unidades.size() + ", tipoMesa=" + tipoMesa.getNome() + ", ativo=" + ativo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.nome);
        hash = 67 * hash + Objects.hashCode(this.unidades);
        hash = 67 * hash + Objects.hashCode(this.tipoMesa);
        hash = 67 * hash + Objects.hashCode(this.ativo);
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
        final Mesa other = (Mesa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.unidades, other.unidades)) {
            return false;
        }
        if (!Objects.equals(this.tipoMesa, other.tipoMesa)) {
            return false;
        }
        if (!Objects.equals(this.ativo, other.ativo)) {
            return false;
        }
        return true;
    }

}
