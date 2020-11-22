package com.example.houst.health.Model;

public class Hopital {
    public String nom;
    public String adresse;
    public String tel;
    public String type;
    public String specialite;
    public String lat;
    public  String  lng;

     public Hopital() {
    }

    public Hopital(String nom, String adresse, String tel, String type, String specialite, String lat, String lng) {
        this.nom = nom;
        this.adresse = adresse;
        this.tel = tel;
        this.type = type;
        this.specialite = specialite;
        this.lat = lat;
        this.lng = lng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String  getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }
}
