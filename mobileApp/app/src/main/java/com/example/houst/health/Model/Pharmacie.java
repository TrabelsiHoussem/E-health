package com.example.houst.health.Model;

public class Pharmacie {
    public String nom;
    public String adresse;
    public String tel;
    public String nuitjour;
    public  String lat,lng;



    public void setLng(String lng) {
        this.lng = lng;
    }

    public Pharmacie() {
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNuitjour() {
        return nuitjour;
    }

    public void setNuitjour(String nuitjour) {
        this.nuitjour = nuitjour;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

}
