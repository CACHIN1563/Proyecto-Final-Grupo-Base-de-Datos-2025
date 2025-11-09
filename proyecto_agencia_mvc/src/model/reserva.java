package model;
import java.time.LocalDateTime;

public class reserva {

    private Long idReserva;
    private LocalDateTime fechaReserva;
    private Long idSucursal;
    private Long idTurista;
    private String regimenHospedaje;
    private String observaciones;

    public reserva(){}

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Long getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Long idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Long getIdTurista() {
        return idTurista;
    }

    public void setIdTurista(Long idTurista) {
        this.idTurista = idTurista;
    }

    public String getRegimenHospedaje() {
        return regimenHospedaje;
    }

    public void setRegimenHospedaje(String regimenHospedaje) {
        this.regimenHospedaje = regimenHospedaje;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
