package com.lester.mytaxi_challenge.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.lester.mytaxi_challenge.R;
import com.lester.mytaxi_challenge.repository.VehicleRepository;
import com.lester.mytaxi_challenge.repository.datasource.remote.VehicleRest;
import com.lester.mytaxi_challenge.repository.datasource.remote.retrofit.ApiService;
import com.lester.mytaxi_challenge.repository.model.VehicleE;
import com.lester.mytaxi_challenge.ui.adapter.VehicleAdapter;
import com.lester.mytaxi_challenge.viewmodel.VehicleViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rv_list) RecyclerView rvDataList;
    @BindView(R.id.pullToRefreshItems) SwipeRefreshLayout pullToRefreshItems;
    @BindView(R.id.llBottomSheet) RelativeLayout llBottomSheet;
    @BindView(R.id.btn_num_vehicles) Button btn_num_vehicles;

    private VehicleViewModel vehicleViewModel;
    private VehicleAdapter vehicleAdapter;
    private ArrayList<VehicleE> dataList = new ArrayList<>();

    GoogleMap mMap;
    boolean listenCameraUpdates = false;
    BottomSheetBehavior bottomSheetBehavior;

    HashMap<Long, Marker> markerCars = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupUI();
        init();
    }

    private void setupUI() {
        toolbar.setTitle("My Taxi");
        setSupportActionBar(toolbar);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setHideable(false);

        pullToRefreshItems.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_red_light),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_blue_bright));
        pullToRefreshItems.setEnabled(false);

        vehicleAdapter = new VehicleAdapter(this, dataList, position -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            VehicleE vehicle = dataList.get(position);
            LatLng ll = new LatLng(vehicle.getCoordinate().getLatitude(), vehicle.getCoordinate().getLongitude());
            Marker marker = markerCars.get(vehicle.getId());
            if(marker != null) marker.showInfoWindow();
            pointToMarker(ll);
        });
        rvDataList.setLayoutManager(new LinearLayoutManager(this));
        rvDataList.setHasFixedSize(true);
        rvDataList.setAdapter(vehicleAdapter);

        SupportMapFragment map = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.main_map);
        map.getMapAsync(this);
    }

    private void init() {
        vehicleViewModel = ViewModelProviders.of(this).get(VehicleViewModel.class);
        vehicleViewModel.dataListLive.observe(this, vehicleList -> updateDataList(vehicleList));
        vehicleViewModel.showLoadingLive.observe(this, b -> {
            if(b){
                btn_num_vehicles.setText("Loading...");
                dataList.addAll(new ArrayList<>());
                vehicleAdapter.notifyDataSetChanged();
                pullToRefreshItems.setRefreshing(true);
            }else{
                pullToRefreshItems.setRefreshing(false);
            }
        });

        vehicleViewModel.provide(new VehicleRepository(new VehicleRest(new ApiService())));
    }

    @OnClick(R.id.btn_num_vehicles)
    public void ShowBottomSheet(){
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void updateDataList(ArrayList<VehicleE> vehicleList) {
        this.dataList.clear();
        this.dataList.addAll(vehicleList);
        vehicleAdapter.notifyDataSetChanged();

        if(vehicleList.size() == 0){
            btn_num_vehicles.setText("No cars available");
        }else{
            String total = vehicleList.size() + " cars near you";
            btn_num_vehicles.setText(total);
        }

        showMarkers();
    }

    private void showMarkers() {
        mMap.clear();
        markerCars = new HashMap<>();

        for (VehicleE v: dataList){
            LatLng vMarker = new LatLng(v.getCoordinate().getLatitude(), v.getCoordinate().getLongitude());
            String car_id = String.valueOf(v.getId());
            MarkerOptions mkrOpt = new MarkerOptions().position(vMarker).zIndex(v.getId());
            mkrOpt.title(v.getFleetType());
            mkrOpt.snippet("#"+car_id);
            mkrOpt.icon(getMarker(v.isPooling(), v.getHeading()));
            Marker marker = mMap.addMarker(mkrOpt);
            ObjectAnimator.ofFloat(marker, "alpha", 0f, 1f).setDuration(1000).start();
            markerCars.put(v.getId(), marker);
        }
    }

    private BitmapDescriptor getMarker(boolean isPooling, double heading) {
        int marker_id;
        if (heading <= 60) {
            marker_id = isPooling ? R.mipmap.ic_marker_pool_up: R.mipmap.ic_marker_taxi_up;
        } else if (heading > 60 && heading <= 120) {
            marker_id = isPooling ? R.mipmap.ic_marker_pool_right: R.mipmap.ic_marker_taxi_right;
        } else if (heading > 120 && heading <= 240) {
            marker_id = isPooling ? R.mipmap.ic_marker_pool_down: R.mipmap.ic_marker_taxi_down;
        } else if (heading > 240 && heading <= 300) {
            marker_id = isPooling ? R.mipmap.ic_marker_pool_left: R.mipmap.ic_marker_taxi_left;
        }else{
            marker_id = isPooling ? R.mipmap.ic_marker_pool_up: R.mipmap.ic_marker_taxi_up;
        }
        return BitmapDescriptorFactory.fromResource(marker_id);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapLoadedCallback(this::showDefaultMap);
        mMap.setOnCameraIdleListener(this::loadData);
        mMap.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            pointToMarker(marker.getPosition());
            return true;
        });
    }

    private void loadData() {
        if(!listenCameraUpdates){
            listenCameraUpdates = true;
            return;
        }

        LatLngBounds curMap = mMap.getProjection().getVisibleRegion().latLngBounds;

        double p1Lat = curMap.northeast.latitude;
        double p1Lon = curMap.northeast.longitude;
        double p2Lat = curMap.southwest.latitude;
        double p2Lon = curMap.southwest.longitude;
        vehicleViewModel.loadVehicles(p1Lat, p1Lon, p2Lat, p2Lon);
    }

    public void showDefaultMap(){
        // Hamburg (53.694865, 9.757589 & 53.394655, 10.099891)
        double p1Lat = 53.694865;
        double p1Lon = 9.757589;
        double p2Lat = 53.394655;
        double p2Lon = 10.099891;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(p1Lat, p1Lon));
        builder.include(new LatLng(p2Lat, p2Lon));
        LatLngBounds bounds = builder.build();
        int padding = 0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private void pointToMarker(LatLng position) {
        listenCameraUpdates = false;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
    }
}