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
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty matricula = new SimpleStringProperty();
    private Instituicao instituicao = new Instituicao();
    private StringProperty grauHierarquico = new SimpleStringProperty();

    private Sexo sexo = new Sexo();
    private StringProperty observacao = new SimpleStringProperty();
    private byte[] foto;
    private Boolean ativo;

    public Servidor() {
    }

    public Servidor(String nome, String matricula, Instituicao instituicao, String grauHierarquico, Sexo sexo, String observacao, Boolean ativo) {
        this.setNome(nome);
        this.setGrauHierarquico(grauHierarquico);
        this.setMatricula(matricula);
        this.setObservacao(observacao);
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
    @Column(name = "GRAU HIERARQUICO")
    public String getGrauHierarquico() {
        return this.grauHierarquico.get();
    }

    @Basic
    @NotNull
    @Column(name = "MATRICULA", unique = true)
    public String getMatricula() {
        return this.matricula.get();
    }

    @Basic
    @NotNull
    @Column(name = "INSTITUICAO")
    public Instituicao getInstituicao() {
        return this.instituicao;
    }

    @Basic
    @NotNull
    @Column(name = "SEXO")
    public Sexo getSexo() {
        return this.sexo;
    }

    @Column(name = "ATIVO")
    public Boolean getAtivo() {
        return ativo;
    }

    @Basic(optional = true)
    @Column(name = "FOTO")
    public byte[] getFoto() {
        return foto;
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

    public void setGrauHierarquico(String value) {
        this.grauHierarquico.set(value);
    }

    public void setMatricula(String value) {
        this.matricula.set(value);
    }

    public void setInstituicao(Instituicao value) {
        this.instituicao = value;
    }

    public void setSexo(Sexo value) {
        this.sexo = sexo;
    }

    public void setObservacao(String observacao) {
        this.observacao.set(observacao);
    }

    public void setFoto(byte[] value) {
        this.foto = value;
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

    public StringProperty hierarquiaProperty() {
        return this.grauHierarquico;
    }

    public StringProperty matriculaProperty() {
        return this.matricula;
    }

    public StringProperty observacaoProperty() {
        return this.observacao;
    }

    @Override
    public String toString() {
        return "Servidor{" + "id=" + id + ", nome=" + nome + ", matricula=" + matricula + ", instituicao=" + instituicao + ", grauHierarquico=" + grauHierarquico + ", sexo=" + sexo + ", ativo=" + ativo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.nome);
        hash = 23 * hash + Objects.hashCode(this.matricula);
        hash = 23 * hash + Objects.hashCode(this.instituicao);
        hash = 23 * hash + Objects.hashCode(this.grauHierarquico);
        hash = 23 * hash + Objects.hashCode(this.sexo);
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
        final Servidor other = (Servidor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        if (!Objects.equals(this.instituicao, other.instituicao)) {
            return false;
        }
        if (!Objects.equals(this.grauHierarquico, other.grauHierarquico)) {
            return false;
        }
        if (!Objects.equals(this.sexo, other.sexo)) {
            return false;
        }
        if (!Objects.equals(this.ativo, other.ativo)) {
            return false;
        }
        return true;
    }

}
