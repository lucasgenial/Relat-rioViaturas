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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_VIATURA")
@Access(AccessType.PROPERTY)
public class Viatura implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private boolean audio;
    private boolean bcs;
    private StringProperty estado = new SimpleStringProperty();
    private boolean gps;
    private StringProperty ht = new SimpleStringProperty();
    private StringProperty prefixo = new SimpleStringProperty();
    private PO tipoPO;
    private Set<ServidorFuncao> guarnicao = new HashSet<>();
    private boolean vtrBaixada;
    private StringProperty hrDaBaixa = new SimpleStringProperty();
    
    
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
    @Column(name = "AUDIO")
    public boolean isAudio() {
        return this.audio;
    }

    @Basic
    @Column(name = "BCS")
    public boolean isBcs() {
        return this.bcs;
    }

    @Basic
    @Column(name = "ESTADO")
    public String getEstado() {
        return this.estado.get();
    }

    @Basic
    @Column(name = "GPS")
    public boolean isGps() {
        return this.gps;
    }

    @Basic
    @Column(name = "HT")
    public String getHt() {
        return this.ht.get();
    }

    @Basic
    @Column(name = "PREFIXO")
    public String getPrefixo() {
        return this.prefixo.get();
    }

    @OneToOne(targetEntity = PO.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "TIPO_PO_ID")
    public PO getTipoPO() {
        return this.tipoPO;
    }
    
    @OneToMany(targetEntity = ServidorFuncao.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "TBL_SERVIDORES_VIATURAS", joinColumns = {
        @JoinColumn(name = "VIATURA_ID_FK")}, inverseJoinColumns = {
        @JoinColumn(name = "SERVIDOR_ID_FK")})
    public Set<ServidorFuncao> getGuarnicao() {
        return this.guarnicao;
    }

    @Basic
    @Column(name = "BAIXADO")
    public boolean isVtrBaixada() {
        return this.vtrBaixada;
    }
    
    @Basic
    @Column(name = "HR_DA_BAIXA")
    public String getHrDaBaixa() {
        return this.hrDaBaixa.get();
    }
    
    /*
    SETTERS
     */
    public void setId(int value) {
        this.id.set(value);
    }

    public void setAudio(boolean value) {
        this.audio = value;
    }

    public void setBcs(boolean value) {
        this.bcs = value;
    }

    public void setEstado(String value) {
        this.estado.set(value);
    }

    public void setGps(boolean value) {
        this.gps = value;
    }

    public void setHt(String value) {
        this.ht.set(value);
    }

    public void setPrefixo(String value) {
        this.prefixo.set(value);
    }

    public void setTipoPO(PO value) {
        this.tipoPO = value;
    }

    public void setGuarnicao(Set<ServidorFuncao> value) {
        this.guarnicao = value;
    }

    public void setVtrBaixada(boolean value) {
        this.vtrBaixada = value;
    }
    
    public void setHrDaBaixa(String value) {
        this.hrDaBaixa.set(value);
    }
    
    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    public StringProperty estadoProperty() {
        return this.estado;
    }

    public StringProperty htProperty() {
        return this.ht;
    }

    public StringProperty prefixoProperty() {
        return this.prefixo;
    }

     public StringProperty hrDaBaixaProperty() {
        return this.hrDaBaixa;
    }

    @Override
    public String toString() {
        return "Viatura{" + "id=" + id + ", audio=" + audio + ", bcs=" + bcs + ", estado=" + estado + ", gps=" + gps + ", ht=" + ht + ", prefixo=" + prefixo + ", tipoPO=" + tipoPO.getNome() + ", Qtd guarnicao=" + guarnicao.size() + ", vtrBaixada=" + vtrBaixada + ", hrDaBaixa=" + hrDaBaixa + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + (this.audio ? 1 : 0);
        hash = 23 * hash + (this.bcs ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.estado);
        hash = 23 * hash + (this.gps ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.ht);
        hash = 23 * hash + Objects.hashCode(this.prefixo);
        hash = 23 * hash + Objects.hashCode(this.tipoPO);
        hash = 23 * hash + Objects.hashCode(this.guarnicao);
        hash = 23 * hash + (this.vtrBaixada ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(this.hrDaBaixa);
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
        final Viatura other = (Viatura) obj;
        if (this.audio != other.audio) {
            return false;
        }
        if (this.bcs != other.bcs) {
            return false;
        }
        if (this.gps != other.gps) {
            return false;
        }
        if (this.vtrBaixada != other.vtrBaixada) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.estado, other.estado)) {
            return false;
        }
        if (!Objects.equals(this.ht, other.ht)) {
            return false;
        }
        if (!Objects.equals(this.prefixo, other.prefixo)) {
            return false;
        }
        if (!Objects.equals(this.tipoPO, other.tipoPO)) {
            return false;
        }
        if (!Objects.equals(this.guarnicao, other.guarnicao)) {
            return false;
        }
        if (!Objects.equals(this.hrDaBaixa, other.hrDaBaixa)) {
            return false;
        }
        return true;
    }

    
}
