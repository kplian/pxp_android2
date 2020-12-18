
package com.vouz.mobileV2.models.session;

public class UserSession {
    private String alias;
    private String autentificacion;
    private long cont_alertas;
    private String estilo_vista;
    private String id_funcionario;
    private String id_usuario;
    private String mensaje_tec;
    private String nombre_basedatos;
    private String nombre_usuario;
    private String phpsession;
    private Boolean success;
    private long timeout;
    private String user;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAutentificacion() {
        return autentificacion;
    }

    public void setAutentificacion(String autentificacion) {
        this.autentificacion = autentificacion;
    }

    public long getCont_alertas() {
        return cont_alertas;
    }

    public void setCont_alertas(long cont_alertas) {
        this.cont_alertas = cont_alertas;
    }

    public String getEstilo_vista() {
        return estilo_vista;
    }

    public void setEstilo_vista(String estilo_vista) {
        this.estilo_vista = estilo_vista;
    }

    public String getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(String id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getMensaje_tec() {
        return mensaje_tec;
    }

    public void setMensaje_tec(String mensaje_tec) {
        this.mensaje_tec = mensaje_tec;
    }

    public String getNombre_basedatos() {
        return nombre_basedatos;
    }

    public void setNombre_basedatos(String nombre_basedatos) {
        this.nombre_basedatos = nombre_basedatos;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getPhpsession() {
        return phpsession;
    }

    public void setPhpsession(String phpsession) {
        this.phpsession = phpsession;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
