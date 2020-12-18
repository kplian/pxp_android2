package com.vouz.mobileV2.models;

@SuppressWarnings("unused")
public class SendSocket {

    private com.vouz.mobileV2.models.Data data;
    private String tipo;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
