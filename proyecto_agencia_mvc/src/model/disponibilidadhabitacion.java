package model;

public class disponibilidadhabitacion {

    private Long id_disponibilidad;
    private Long id_hotel;
    private Long id_tipo_hab;
    private Integer plazas_disponibles;

    private String nombreHotel;
    private String tipoHabitacion;

    public disponibilidadhabitacion() {}

    public Long getid_disponibilidad() {
        return id_disponibilidad;
    }

    public void setid_disponibilidad(Long id_disponibilidad) {
        this.id_disponibilidad = id_disponibilidad;
    }

    public Long getid_hotel() {
        return id_hotel;
    }

    public void setid_hotel(Long id_hotel) {
        this.id_hotel = id_hotel;
    }

    public Long getid_tipo_hab() {
        return id_tipo_hab;
    }

    public void setid_tipo_hab(Long id_tipo_hab) {
        this.id_tipo_hab = id_tipo_hab;
    }

    public Integer getplazas_disponibles() {
        return plazas_disponibles;
    }

    public void setplazas_disponibles(Integer plazas_disponibles) {
        this.plazas_disponibles = plazas_disponibles;
    }

    public String getNombreHotel() {
        return nombreHotel;
    }

    public void setNombreHotel(String nombreHotel) {
        this.nombreHotel = nombreHotel;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    @Override
    public String toString() {
        return nombreHotel + " - " + tipoHabitacion + " (" + plazas_disponibles + ")";
    }
}
