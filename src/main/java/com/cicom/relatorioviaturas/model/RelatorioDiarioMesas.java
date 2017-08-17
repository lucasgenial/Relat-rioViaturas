package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * @author Lucas Matos
 */
@Entity
@Table(name = "TBL_RELATORIO_MESAS")
@Access(AccessType.PROPERTY)
public class RelatorioDiarioMesas implements Serializable {

    private IntegerProperty id = new SimpleIntegerProperty();
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private LocalTime horaInicial;
    private LocalTime horaFinal;
    private Mesa mesa;
    private Set<RelatorioDiarioViaturas> relatorioDiarioViaturas = new HashSet<>();
    private Set<ServidorFuncao> servidores = new HashSet<>();

    public RelatorioDiarioMesas() {
    }

    public RelatorioDiarioMesas(LocalDate dataInicial, LocalDate dataFinal, LocalTime horaInicial, LocalTime horaFinal, Mesa mesa) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.mesa = mesa;
    }

    public RelatorioDiarioMesas(LocalDate dataInicial, LocalDate dataFinal, LocalTime horaInicial, LocalTime horaFinal, Mesa mesa, Set<ServidorFuncao> servidor) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.mesa = mesa;
        this.servidores = servidor;
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
    @Column(name = "DATA_INICIAL")
    public LocalDate getDataInicial() {
        return this.dataInicial;
    }

    @Basic
    @NotNull
    @Column(name = "HORA_INICIAL")
    public LocalTime getHoraInicial() {
        return this.horaInicial;
    }

    @Basic
    @NotNull
    @Column(name = "DATA_FINAL")
    public LocalDate getDataFinal() {
        return this.dataFinal;
    }

    @Basic
    @NotNull
    @Column(name = "HORA_FINAL")
    public LocalTime getHoraFinal() {
        return this.horaFinal;
    }

    @OneToOne(targetEntity = Mesa.class)
    @JoinColumn(name = "MESA")
    public Mesa getMesa() {
        return this.mesa;
    }

    @OneToMany(targetEntity = RelatorioDiarioViaturas.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "TBL_RELATORIO_MESAS_VIATURAS", joinColumns = {
        @JoinColumn(name = "RELATORIO_MESAS_ID_FK")}, inverseJoinColumns = {
        @JoinColumn(name = "RELATORIO_VIATURAS_ID_FK")})
    public Set<RelatorioDiarioViaturas> getRelatorioDiarioViaturas() {
        return this.relatorioDiarioViaturas;
    }

    @OneToMany(targetEntity = ServidorFuncao.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "TBL_SERVIDORES_RELATORIO_MESAS", joinColumns = {
        @JoinColumn(name = "RELATORIO_MESAS_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SERVIDOR_ID")})
    public Set<ServidorFuncao> getServidores() {
        return this.servidores;
    }

    @Transient
    public ServidorFuncao getSupervisor() {
        for (ServidorFuncao ser : servidores) {
            if (ser.getFuncao().getNome().equalsIgnoreCase("supervisor")) {
                return ser;
            }
        }
        return null;
    }

    @Transient
    public ServidorFuncao getOperador() {
        for (ServidorFuncao ser : servidores) {
            if (ser.getFuncao().getNome().equalsIgnoreCase("operador")) {
                return ser;
            }
        }
        return null;
    }

    @Transient
    public String getHorarioMesa() {
        return horaInicial.format(DateTimeFormatter.ISO_TIME) + " - " 
                + horaFinal.format(DateTimeFormatter.ISO_TIME);
    }

    @Transient
    public String getDiaMesa() {
        return dataInicial.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " 
                + dataFinal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /*
    SETTERS
     */
    public void setId(int value) {
        this.id.set(value);
    }

    public void setDataInicial(LocalDate value) {
        this.dataInicial = value;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public void setRelatorioDiarioViaturas(Set<RelatorioDiarioViaturas> value) {
        this.relatorioDiarioViaturas = value;
    }

    public void setServidores(Set<ServidorFuncao> value) {
        this.servidores = value;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void setHoraInicial(LocalTime horaInicial) {
        this.horaInicial = horaInicial;
    }

    public void setHoraFinal(LocalTime horaFinal) {
        this.horaFinal = horaFinal;
    }

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RelatorioDiarioMesas{" + "id=" + id + ", dataInicial=" + dataInicial + ", dataFinal=" + dataFinal + ", horaInicial=" + horaInicial + ", horaFinal=" + horaFinal + ", mesa=" + mesa + ", QTD_relatorioDiarioViaturas=" + relatorioDiarioViaturas.size() + ", QTD_servidores=" + servidores.size() + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.dataInicial);
        hash = 59 * hash + Objects.hashCode(this.dataFinal);
        hash = 59 * hash + Objects.hashCode(this.horaInicial);
        hash = 59 * hash + Objects.hashCode(this.horaFinal);
        hash = 59 * hash + Objects.hashCode(this.mesa);
        hash = 59 * hash + Objects.hashCode(this.relatorioDiarioViaturas);
        hash = 59 * hash + Objects.hashCode(this.servidores);
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
        final RelatorioDiarioMesas other = (RelatorioDiarioMesas) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dataInicial, other.dataInicial)) {
            return false;
        }
        if (!Objects.equals(this.dataFinal, other.dataFinal)) {
            return false;
        }
        if (!Objects.equals(this.horaInicial, other.horaInicial)) {
            return false;
        }
        if (!Objects.equals(this.horaFinal, other.horaFinal)) {
            return false;
        }
        if (!Objects.equals(this.mesa, other.mesa)) {
            return false;
        }
        if (!Objects.equals(this.relatorioDiarioViaturas, other.relatorioDiarioViaturas)) {
            return false;
        }
        if (!Objects.equals(this.servidores, other.servidores)) {
            return false;
        }
        return true;
    }

}
