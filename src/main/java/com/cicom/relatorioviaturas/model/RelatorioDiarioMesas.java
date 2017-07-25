package com.cicom.relatorioviaturas.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalDate dia;
    private StringProperty turno = new SimpleStringProperty();
    private Mesa mesa;
    private Set<RelatorioDiarioViaturas> relatorioDiarioViaturas = new HashSet<>();
    private List<ServidorFuncao> servidores = new ArrayList<>();

    public RelatorioDiarioMesas() {
    }

    public RelatorioDiarioMesas(LocalDate dia, Mesa mesa, List<ServidorFuncao> servidores) {
        this.dia = dia;
        this.mesa = mesa;
        this.servidores = servidores;
    }

    public RelatorioDiarioMesas(LocalDate dia, Mesa mesa, Set<RelatorioDiarioViaturas> relatorioDiarioViaturases, List<ServidorFuncao> servidores) {
        this.dia = dia;
        this.mesa = mesa;
        this.relatorioDiarioViaturas = relatorioDiarioViaturases;
        this.servidores = servidores;
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
    @Column(name = "DIA")
    public LocalDate getDia() {
        return this.dia;
    }

    @Basic
    @NotNull
    @Column(name = "TURNO")
    public String getTurno() {
        return this.turno.get();
    }

    @OneToOne(targetEntity = Mesa.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "MESA")
    public Mesa getMesa() {
        return this.mesa;
    }

    @OneToMany(targetEntity = RelatorioDiarioViaturas.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "TBL_RELATORIO_MESAS_VIATURAS", joinColumns = {
        @JoinColumn(name = "RELATORIO_MESAS_ID_FK")}, inverseJoinColumns = {
        @JoinColumn(name = "RELATORIO_VIATURAS_ID_FK")})
    public Set<RelatorioDiarioViaturas> getRelatorioDiarioViaturas() {
        return this.relatorioDiarioViaturas;
    }

    @OneToMany(targetEntity = ServidorFuncao.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "TBL_SERVIDORES_RELATORIO_MESAS", joinColumns = {
        @JoinColumn(name = "RELATORIO_MESAS_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SERVIDOR_ID")})
    public List<ServidorFuncao> getServidores() {
        return this.servidores;
    }

    @Transient
    public ServidorFuncao getSupervisor() {
        if (this.servidores.get(0) != null && !servidores.isEmpty()) {
            return this.servidores.get(0);
        } else {
            return null;
        }
    }

    @Transient
    public ServidorFuncao getOperador() {
        if (this.servidores.get(1) != null && !servidores.isEmpty()) {
            return this.servidores.get(1);
        } else {
            return null;
        }
    }

    /*
    SETTERS
     */
    public void setId(int value) {
        this.id.set(value);
    }

    public void setDia(LocalDate value) {
        this.dia = value;
    }

    public void setTurno(String value) {
        this.turno.set(value);
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public void setRelatorioDiarioViaturas(Set<RelatorioDiarioViaturas> value) {
        this.relatorioDiarioViaturas = value;
    }

    public void setServidores(List<ServidorFuncao> value) {
        this.servidores = value;
    }

    public void setSupervisor(ServidorFuncao value) {
        this.servidores.add(0, value);
    }

    public void setOperador(ServidorFuncao value) {
        this.servidores.add(1, value);
    }

    /*
    PROPERTY
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    public StringProperty turnoProperty() {
        return this.turno;
    }

    @Override
    public String toString() {
        return "RelatorioDiarioMesas{" + "id=" + id + ", dia=" + dia + ", turno=" + turno + ", mesa=" + mesa + ", relatorioDiarioViaturases=" + relatorioDiarioViaturas + ", servidores=" + servidores + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.dia);
        hash = 47 * hash + Objects.hashCode(this.turno);
        hash = 47 * hash + Objects.hashCode(this.mesa);
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
        if (!Objects.equals(this.dia, other.dia)) {
            return false;
        }
        if (!Objects.equals(this.turno, other.turno)) {
            return false;
        }
        if (!Objects.equals(this.mesa, other.mesa)) {
            return false;
        }
        return true;
    }

}
