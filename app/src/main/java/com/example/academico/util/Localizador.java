package com.example.academico.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Giovany on 18/08/2015.
 */
public class Localizador {

    private Geocoder geo;

    // Obtem o Geocoder padrão
    public Localizador(Context ctx){
        geo = new Geocoder(ctx, Locale.getDefault());
    }

    // Dado um endereço gera um objeto com suas coordenadas
    public LatLng getCoordenada(String endereco){
        try{
            List<Address> listaEnderecos;
            listaEnderecos = geo.getFromLocationName(endereco, 1);
            if(!listaEnderecos.isEmpty()){
                // Vamos usar o primeiro endereço
                Address address = listaEnderecos.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            } else {
                return null;
            }
        } catch (IOException e){
            return null;
        }
    }
}
