package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_SERVIDOR_FUNCAO")
@Access(AccessType.PROPERTY)
public class ServidorFuncao implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private Servidor servidor;
    private Funcao funcao;
    private Boolean ativo;

    public ServidorFuncao(Servidor servidor, Funcao funcao, Boolean ativo) {
        this.servidor = servidor;
        this.funcao = funcao;
        this.ativo = ativo;
    }

    public ServidorFuncao() {
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
    @OneToOne(targetEntity = Servidor.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "SERVIDOR_FK")
    public Servidor getServidor() {
        return this.servidor;
    }

    @NotNull
    @OneToOne(targetEntity = Funcao.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "FUNCAO_FK")
    public Funcao getFuncao() {
        return this.funcao;
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

    public void setServidor(Servidor value) {
        this.servidor = value;
    }

    public void setFuncao(Funcao value) {
        this.funcao = value;
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

    @Override
    public String toString() {
        return "ServidorFuncao{" + "id=" + id + ", servidor=" + servidor + ", funcao=" + funcao + ", ativo=" + ativo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.servidor);
        hash = 41 * hash + Objects.hashCode(this.funcao);
        hash = 41 * hash + Objects.hashCode(this.ativo);
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
        final ServidorFuncao other = (ServidorFuncao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.servidor, other.servidor)) {
            return false;
        }
        if (!Objects.equals(this.funcao, other.funcao)) {
            return false;
        }
        if (!Objects.equals(this.ativo, other.ativo)) {
            return false;
        }
        return true;
    }
}
