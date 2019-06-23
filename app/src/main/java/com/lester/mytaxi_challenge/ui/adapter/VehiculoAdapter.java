package com.lester.mytaxi_challenge.ui.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lester.mytaxi_challenge.R;
import com.lester.mytaxi_challenge.repository.model.VehicleE;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.ViewHolder> {
    private Context ctx;
    private Callback callback;
    private ArrayList<VehicleE> data;

    public VehiculoAdapter(Context _ctx, ArrayList<VehicleE> data, Callback callback) {
        ctx = _ctx;
        this.data = data;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_vehicle, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VehicleE obj = data.get(position);
        holder.txt_id.setText("#"+obj.getId());
        holder.txt_fleet_type.setText(obj.getFleetType());
        if (obj.isPooling()){
            holder.iv_car.setImageDrawable(ContextCompat.getDrawable(ctx, R.mipmap.ic_car_pooling));
        }else{
            holder.iv_car.setImageDrawable(ContextCompat.getDrawable(ctx, R.mipmap.ic_car_taxi));
        }

        double lat = obj.getCoordinate().getLatitude();
        double lng = obj.getCoordinate().getLongitude();
        String YOUR_API_KEY = ctx.getString(R.string.maps_apikey);
        String url = "https://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=200x200&sensor=false&key="+YOUR_API_KEY;
//        Glide.with(ctx).load(url).into(holder.iv_map);

        holder.itemView.setOnClickListener(v -> callback.onItemSelected(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txt_fleet_type) TextView txt_fleet_type;
        @BindView(R.id.txt_id) TextView txt_id;
        @BindView(R.id.iv_car) ImageView iv_car;
        @BindView(R.id.iv_map) ImageView iv_map;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Callback{
        void onItemSelected(int position);
    }
}