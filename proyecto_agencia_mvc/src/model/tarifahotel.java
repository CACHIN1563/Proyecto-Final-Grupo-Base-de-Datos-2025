package model;

public class tarifahotel {

    private Long id_tarifa;
    private Long id_hotel;
    private Long id_tipo_hab;
    private String regimen;       
    private Double precio_noche;
    private String nombreHotel;
    private String tipoHabitacion;

    public tarifahotel() {}

    public Long getid_tarifa() {
        return id_tarifa;
    }

    public void setid_tarifa(Long id_tarifa) {
        this.id_tarifa = id_tarifa;
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

    public String getregimen() {
        return regimen;
    }

    public void setregimen(String regimen) {
        this.regimen = regimen;
    }

    public Double getprecio_noche() {
        return precio_noche;
    }

    public void setprecio_noche(Double precio_noche) {
        this.precio_noche = precio_noche;
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
}
