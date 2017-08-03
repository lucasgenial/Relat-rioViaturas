package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Lucas Matos e Souza
 */
@Entity
@Table(name = "TBL_SEXO")
@Access(AccessType.PROPERTY)
public class Sexo implements Serializable {
    
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    
    public Sexo() {
    }
    
    public Sexo(String nome) {
        this.setNome(nome);
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public int getId() {
        return id.get();
    }
    
    @Basic
    @NotNull
    @Column(name = "NOME", unique = true)
    public String getNome() {
        return nome.get();
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

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return id;
    }
    
    public StringProperty nomeProperty() {
        return nome;
    }
    
    @Override
    public String toString() {
        return "Sexo{" + "id=" + id + ", nome=" + nome + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.nome);
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
        final Sexo other = (Sexo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }
    
}
