package com.example.messivsronaldo.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messivsronaldo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private GoogleMap googleMap;
    private Runnable onMapReadyCallback;


    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.goolge_map);
        mapFragment.getMapAsync(googleMap ->{
            this.googleMap = googleMap;
            if(onMapReadyCallback != null){
                onMapReadyCallback.run();
                onMapReadyCallback = null;
            }
        });
        return view;
    }

    public void setOnMapReadyCallback(Runnable callback) {
        onMapReadyCallback = callback;
    }

    public void zoom (double latitude, double longitude, String playerName){
        if (googleMap == null) {
            setOnMapReadyCallback(() -> zoom(latitude, longitude, playerName));
            return;
        }

        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude)).title(playerName));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(15)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}