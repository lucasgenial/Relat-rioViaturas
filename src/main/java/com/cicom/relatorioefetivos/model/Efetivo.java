package com.cicom.relatorioefetivos.model;

import java.io.Serializable;
import java.time.LocalTime;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Icaro Bastos
 */
@Entity
@Table(name = "TBL_EFETIVO")
@Access(AccessType.PROPERTY)
public class Efetivo implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private PO tipoPO;
    private StringProperty prefixo = new SimpleStringProperty();
    private boolean bcs;
    private boolean estadoGPS;
    private boolean gps;
    private boolean ht;
    private boolean audio;
    private StringProperty tomboHT = new SimpleStringProperty();
    private boolean camera;
    private boolean estadoCam;
    private LocalTime horaInicialPlantao;
    private LocalTime horaFinalPlantao;
    private LocalTime horaPausa1;
    private LocalTime horaPausa2;
    private boolean vtrBaixada;
    private LocalTime hrDaBaixa;
    private Set<ServidorFuncao> guarnicao = new HashSet<>();

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
    public boolean isEstadoGPS() {
        return this.estadoGPS;
    }

    @Basic
    @Column(name = "GPS")
    public boolean isGps() {
        return this.gps;
    }

    @Basic
    @Column(name = "CAMERA")
    public boolean isCamera() {
        return this.camera;
    }

    @Basic
    @Column(name = "ESTADO_CAM")
    public boolean isEstadoCam() {
        return this.estadoCam;
    }

    @Basic
    @Column(name = "HT")
    public boolean isHt() {
        return this.ht;
    }

    @Basic
    @Column(name = "PREFIXO")
    public String getPrefixo() {
        return this.prefixo.get();
    }

    @Basic
    @Column(name = "TOMBO_HT")
    public String getTomboHT() {
        return this.tomboHT.get();
    }

    @OneToOne(targetEntity = PO.class)
    @JoinColumn(name = "TIPO_PO_ID")
    public PO getTipoPO() {
        return this.tipoPO;
    }

    @OneToMany(targetEntity = ServidorFuncao.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinTable(name = "TBL_SERVIDORES_EFETIVO", joinColumns = {
        @JoinColumn(name = "EFETIVO_ID_FK")}, inverseJoinColumns = {
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
    @NotNull
    @Column(name = "HR_INICIO_PLANTAO")
    public LocalTime getHoraInicialPlantao() {
        return this.horaInicialPlantao;
    }

    @Basic
    @NotNull
    @Column(name = "HR_FIM_PLANTAO")
    public LocalTime getHoraFinalPlantao() {
        return this.horaFinalPlantao;
    }

    @Basic
    @Column(name = "HR_PAUSA1")
    public LocalTime getHoraPausa1() {
        return this.horaPausa1;
    }

    @Basic
    @Column(name = "HR_PAUSA2")
    public LocalTime getHoraPausa2() {
        return this.horaPausa2;
    }

    @Basic
    @Column(name = "HR_DA_BAIXA")
    public LocalTime getHrDaBaixa() {
        return this.hrDaBaixa;
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

    public void setEstadoGPS(boolean value) {
        this.estadoGPS = value;
    }

    public void setGps(boolean value) {
        this.gps = value;
    }

    public void setCamera(boolean value) {
        this.camera = value;
    }

    public void setEstadoCam(boolean value) {
        this.estadoCam = value;
    }

    public void setHt(boolean value) {
        this.ht = value;
    }

    public void setPrefixo(String value) {
        this.prefixo.set(value);
    }

    public void setTomboHT(String value) {
        this.tomboHT.set(value);
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

    public void setHoraInicialPlantao(LocalTime value) {
        this.horaInicialPlantao = value;
    }

    public void setHoraFinalPlantao(LocalTime value) {
        this.horaFinalPlantao = value;
    }

    public void setHoraPausa1(LocalTime value) {
        this.horaPausa1 = value;
    }

    public void setHoraPausa2(LocalTime value) {
        this.horaPausa2 = value;
    }

    public void setHrDaBaixa(LocalTime value) {
        this.hrDaBaixa = value;
    }

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    public StringProperty prefixoProperty() {
        return this.prefixo;
    }

    public StringProperty tomboHTProperty() {
        return this.tomboHT;
    }

    @Override
    public String toString() {
        return "Efetivo{" + "id=" + id + ", tipoPO=" + tipoPO.getNome() + ", prefixo=" + prefixo.getValue() + ", bcs="
                + bcs + ", estadoGPS=" + estadoGPS + ", gps=" + gps + ", ht=" + ht + ", audio=" + audio + ", tomboHT="
                + tomboHT.getValue() + ", camera=" + camera + ", estadoCam=" + estadoCam + ", horaInicialPlantao="
                + horaInicialPlantao + ", horaFinalPlantao=" + horaFinalPlantao + ", horaPausa1=" + horaPausa1 + ", horaPausa2="
                + horaPausa2 + ", vtrBaixada=" + vtrBaixada + ", hrDaBaixa=" + hrDaBaixa + ", guarnicao=" + guarnicao.size() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.tipoPO);
        hash = 17 * hash + Objects.hashCode(this.prefixo);
        hash = 17 * hash + (this.bcs ? 1 : 0);
        hash = 17 * hash + (this.estadoGPS ? 1 : 0);
        hash = 17 * hash + (this.gps ? 1 : 0);
        hash = 17 * hash + (this.ht ? 1 : 0);
        hash = 17 * hash + (this.audio ? 1 : 0);
        hash = 17 * hash + Objects.hashCode(this.tomboHT);
        hash = 17 * hash + (this.camera ? 1 : 0);
        hash = 17 * hash + (this.estadoCam ? 1 : 0);
        hash = 17 * hash + Objects.hashCode(this.horaInicialPlantao);
        hash = 17 * hash + Objects.hashCode(this.horaFinalPlantao);
        hash = 17 * hash + Objects.hashCode(this.horaPausa1);
        hash = 17 * hash + Objects.hashCode(this.horaPausa2);
        hash = 17 * hash + (this.vtrBaixada ? 1 : 0);
        hash = 17 * hash + Objects.hashCode(this.hrDaBaixa);
        hash = 17 * hash + Objects.hashCode(this.guarnicao);
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
        final Efetivo other = (Efetivo) obj;
        if (this.bcs != other.bcs) {
            return false;
        }
        if (this.estadoGPS != other.estadoGPS) {
            return false;
        }
        if (this.gps != other.gps) {
            return false;
        }
        if (this.ht != other.ht) {
            return false;
        }
        if (this.audio != other.audio) {
            return false;
        }
        if (this.camera != other.camera) {
            return false;
        }
        if (this.estadoCam != other.estadoCam) {
            return false;
        }
        if (this.vtrBaixada != other.vtrBaixada) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tipoPO, other.tipoPO)) {
            return false;
        }
        if (!Objects.equals(this.prefixo, other.prefixo)) {
            return false;
        }
        if (!Objects.equals(this.tomboHT, other.tomboHT)) {
            return false;
        }
        if (!Objects.equals(this.horaInicialPlantao, other.horaInicialPlantao)) {
            return false;
        }
        if (!Objects.equals(this.horaFinalPlantao, other.horaFinalPlantao)) {
            return false;
        }
        if (!Objects.equals(this.horaPausa1, other.horaPausa1)) {
            return false;
        }
        if (!Objects.equals(this.horaPausa2, other.horaPausa2)) {
            return false;
        }
        if (!Objects.equals(this.hrDaBaixa, other.hrDaBaixa)) {
            return false;
        }
        if (!Objects.equals(this.guarnicao, other.guarnicao)) {
            return false;
        }
        return true;
    }

}
