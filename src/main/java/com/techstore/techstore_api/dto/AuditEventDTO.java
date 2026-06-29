package com.techstore.techstore_api.dto;

public class AuditEventDTO {

    private String accion;
    private Long productoId;
    private String nombre;
    private String usuario;
    private String fecha;

    public AuditEventDTO() {
    }

    public AuditEventDTO(
            String accion,
            Long productoId,
            String nombre,
            String usuario,
            String fecha) {

        this.accion = accion;
        this.productoId = productoId;
        this.nombre = nombre;
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
