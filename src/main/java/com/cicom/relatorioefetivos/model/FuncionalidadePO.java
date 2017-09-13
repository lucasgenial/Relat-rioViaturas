package com.cicom.relatorioefetivos.model;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_FUNCIONALIDADE_PO")
@Access(AccessType.PROPERTY)
public class FuncionalidadePO implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private Funcionalidade funcionalidade;
    private BooleanProperty situacao = new SimpleBooleanProperty();
    private BooleanProperty status = new SimpleBooleanProperty();
    private StringProperty tombo = new SimpleStringProperty();

    public FuncionalidadePO() {
    }

    public FuncionalidadePO(Funcionalidade funcionalidade) {
        this.setFuncionalidade(funcionalidade);
        this.setSituacao(true);
        this.setStatus(true);
        this.setTombo("");
    }

    public FuncionalidadePO(Funcionalidade funcionalidade, boolean situacao, boolean status, String tombo) {
        this.setFuncionalidade(funcionalidade);
        this.setSituacao(situacao);
        this.setStatus(status);
        this.setTombo(tombo);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public int getId() {
        return this.id.get();
    }

    @OneToOne(targetEntity = Funcionalidade.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "FUNCIONALIDADE_ID", nullable = false)
    public Funcionalidade getFuncionalidade() {
        return this.funcionalidade;
    }

    @Column(name = "SITUACAO")
    public boolean getSituacao() {
        return this.situacao.get();
    }

    @Column(name = "STATUS")
    public boolean getStatus() {
        return this.status.get();
    }

    @Column(name = "TOMBO")
    public String getTombo() {
        return this.tombo.get();
    }

    public void setId(int value) {
        this.id.set(value);
    }

    public void setFuncionalidade(Funcionalidade funcionalidade) {
        this.funcionalidade = funcionalidade;
    }

    public void setSituacao(boolean value) {
        this.situacao.set(value);
    }

    public void setStatus(boolean value) {
        status.set(value);
    }

    public void setTombo(String value) {
        this.tombo.set(value);
    }

    public BooleanProperty situacaoProperty() {
        return situacao;
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return "FuncionalidadePO{" + "id=" + id + ", funcionalidade="
                + funcionalidade.getNome() + ", situacao=" + situacao.get() + ", status="
                + status.get() + ", tombo=" + tombo.get() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.funcionalidade);
        hash = 97 * hash + Objects.hashCode(this.situacao);
        hash = 97 * hash + Objects.hashCode(this.status);
        hash = 97 * hash + Objects.hashCode(this.tombo);
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
        final FuncionalidadePO other = (FuncionalidadePO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.funcionalidade, other.funcionalidade)) {
            return false;
        }
        if (!Objects.equals(this.situacao, other.situacao)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.tombo, other.tombo)) {
            return false;
        }
        return true;
    }

}
