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
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_SERVIDOR")
@Access(AccessType.PROPERTY)
public class Servidor implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty hierarquia = new SimpleStringProperty();
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty nomeGuerra = new SimpleStringProperty();
    private StringProperty matricula = new SimpleStringProperty();
    private Boolean ativo;

    public Servidor() {
    }

    public Servidor(String nome, String nomeGuerra, String hierarquia, String matricula) {
        this.setNome(nome);
        this.setNomeGuerra(nomeGuerra);
        this.setHierarquia(hierarquia);
        this.setMatricula(matricula);

    }

     public Servidor(String nome, String nomeGuerra, String hierarquia, String matricula, Boolean ativo) {
        this.setNome(nome);
        this.setNomeGuerra(nomeGuerra);
        this.setHierarquia(hierarquia);
        this.setMatricula(matricula);
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
    @Column(name = "NOME")
    public String getNome() {
        return this.nome.get();
    }

    @Basic
    @NotNull
    @Column(name = "NOME_GUERRA")
    public String getNomeGuerra() {
        return this.nomeGuerra.get();
    }

    @Basic
    @NotNull
    @Column(name = "HIERARQUIA")
    public String getHierarquia() {
        return this.hierarquia.get();
    }

    @Basic
    @NotNull
    @Column(name = "MATRICULA", unique = true)
    public String getMatricula() {
        return this.matricula.get();
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

    public void setNomeGuerra(String value) {
        this.nomeGuerra.set(value);
    }

    public void setHierarquia(String value) {
        this.hierarquia.set(value);
    }

    public void setMatricula(String value) {
        this.matricula.set(value);
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

    public StringProperty nomeGuerraProperty() {
        return this.nomeGuerra;
    }

    public StringProperty hierarquiaProperty() {
        return this.hierarquia;
    }

    public StringProperty matriculaProperty() {
        return this.matricula;
    }

    @Override
    public String toString() {
        return "Servidor{" + "id=" + id + ", hierarquia=" + hierarquia + ", nome=" + nome + ", nomeGuerra=" + nomeGuerra + ", matricula=" + matricula + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.hierarquia);
        hash = 17 * hash + Objects.hashCode(this.nome);
        hash = 17 * hash + Objects.hashCode(this.nomeGuerra);
        hash = 17 * hash + Objects.hashCode(this.matricula);
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
        final Servidor other = (Servidor) obj;

        if (!Objects.equals(this.hierarquia, other.hierarquia)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.nomeGuerra, other.nomeGuerra)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        return true;
    }

    

}
