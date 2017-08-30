package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
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
    private LocalTime horaInicialPlantao;
    private LocalTime horaFinalPlantao;
    private LocalTime horaPausa1;
    private LocalTime horaPausa2;
    private Boolean ativo;

    public ServidorFuncao(Servidor servidor, Funcao funcao, LocalTime horaInicialPlantao, LocalTime horaFinalPlantao, Boolean ativo) {
        this.servidor = servidor;
        this.horaInicialPlantao = horaInicialPlantao;
        this.horaFinalPlantao = horaFinalPlantao;        
        this.funcao = funcao;
        this.ativo = ativo;
    }
    
    public ServidorFuncao(Servidor servidor, Funcao funcao, LocalTime horaInicialPlantao, LocalTime horaFinalPlantao, 
            LocalTime pausa1, LocalTime pausa2, Boolean ativo) {
        this.servidor = servidor;
        this.horaInicialPlantao = horaInicialPlantao;
        this.horaFinalPlantao = horaFinalPlantao;
        this.horaPausa1 = pausa1;
        this.horaPausa2 = pausa2;
        this.funcao = funcao;
        this.ativo = ativo;
    }

    public ServidorFuncao() {
        this.ativo = true;
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
    
    @Basic
    @NotNull
    @Column(name = "HORA_INICIO_PLANTAO")
    public LocalTime getHoraInicialPlantao() {
        return this.horaInicialPlantao;
    }

    @Basic
    @NotNull
    @Column(name = "HORA_FINAL_PLANTAO")
    public LocalTime getHoraFinalPlantao() {
        return this.horaFinalPlantao;
    }
    
    @Basic
    @Column(name = "HORA_PAUSA_1")
    public LocalTime getHoraPausa1() {
        return this.horaPausa1;
    }
    
    @Basic
    @Column(name = "HORA_PAUSA_2")
    public LocalTime getHoraPausa2() {
        return this.horaPausa2;
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
        return "ServidorFuncao{" + "id=" + id + ", servidor=" + servidor.getNome() + ", funcao=" + funcao.getNome() + ","
                + " horaInicialPlantao=" + horaInicialPlantao + ", horaFinalPlantao=" + horaFinalPlantao + ", "
                + "horaPausa1=" + horaPausa1 + ", horaPausa2=" + horaPausa2 + ", ativo=" + ativo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.servidor);
        hash = 83 * hash + Objects.hashCode(this.funcao);
        hash = 83 * hash + Objects.hashCode(this.horaInicialPlantao);
        hash = 83 * hash + Objects.hashCode(this.horaFinalPlantao);
        hash = 83 * hash + Objects.hashCode(this.horaPausa1);
        hash = 83 * hash + Objects.hashCode(this.horaPausa2);
        hash = 83 * hash + Objects.hashCode(this.ativo);
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
        if (!Objects.equals(this.horaInicialPlantao, other.horaInicialPlantao)) {
            return false;
        }
        if (!Objects.equals(this.horaFinalPlantao, other.horaFinalPlantao)) {
            return false;
        }
        if (!Objects.equals(this.ativo, other.ativo)) {
            return false;
        }
        return true;
    }
    
    
    
}
