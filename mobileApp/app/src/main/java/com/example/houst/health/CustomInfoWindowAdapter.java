package com.example.houst.health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.houst.health.Model.Hopital;
import com.example.houst.health.Model.Pharmacie;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    //private  final  View mWindow;
    private  final  View mWindow,win;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext =context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.hopitalcustomwindow,null);
        win=LayoutInflater.from(context).inflate(R.layout.pharmaciecustomwindow,null);
    }

    private void rendowWindowText(Marker marker,View view){

        TextView pharmanom = (TextView) view.findViewById(R.id.pharmanom);
        TextView pharmatel = (TextView) view.findViewById(R.id.pharmatel);
        TextView pharmaadresse = (TextView) view.findViewById(R.id.pharmaadresse);
        TextView pharmatype = (TextView) view.findViewById(R.id.pharmatype);




        TextView hopnom = (TextView) view.findViewById(R.id.hopnom);
        TextView hoptel = (TextView) view.findViewById(R.id.hoptel);
        TextView hopadresse = (TextView) view.findViewById(R.id.hopadresse);
        TextView hoptype = (TextView) view.findViewById(R.id.hoptype);
        TextView hopspecialite = (TextView) view.findViewById(R.id.hopspecialite);

//        txtPickupSnippet.setText(marker.getSnippet());

        if(marker.getSnippet()!=null && marker.getTitle().equals("hopital")) {
            Gson gson = new Gson();
            Hopital hopital = gson.fromJson(marker.getSnippet(), Hopital.class);


            hopnom.setText( hopital.getNom());
            hoptel.setText(   hopital.getTel());
            hopadresse.setText(hopital.getAdresse());
            hoptype.setText(hopital.getType());
            hopspecialite.setText(hopital.getSpecialite());
        }
        if (marker.getSnippet()!=null && marker.getTitle().equals("pharmacie")){
            Gson gson = new Gson();
            Pharmacie pharmacie = gson.fromJson(marker.getSnippet(), Pharmacie.class);


            pharmanom.setText( pharmacie.getNom());
            pharmatel.setText( pharmacie.getTel());
            pharmaadresse.setText(pharmacie.getAdresse());
            pharmatype.setText(pharmacie.getNuitjour());
//            pharmatype.setText("sfgdfgdfgdfg");

        }

    }


    @Override
    public View getInfoWindow(Marker marker) {


        if(marker.getSnippet().equals("hopital") ||marker.getTitle().equals("hopital") ){
            rendowWindowText(marker,mWindow);
            return mWindow;
        }else if (marker.getTitle().equals("pharmacie")){
            rendowWindowText(marker,win);
            return  win;
        }else {
            return null;
        }

        //TextView txtPickupSnippet =((TextView)myView.findViewById(R.id.txtPickupSnippet));
        //txtPickupSnippet.setText(marker.getSnippet());

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
