package com.example.gonulluproje;

import java.io.Serializable;

public class Foto implements Serializable {
    private String resim;
    private String konum;
    private String kullanici;
    private String key;

    public Foto() {
    }

    public Foto(String resim, String konum,String kullanici,String key) {
        this.resim = resim;
        this.konum = konum;
        this.kullanici = kullanici;
        this.key = key;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getKullanici() {
        return kullanici;
    }

    public void setKullanici(String kullanici) {
        this.kullanici = kullanici;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
