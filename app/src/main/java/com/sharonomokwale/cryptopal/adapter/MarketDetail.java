package com.sharonomokwale.cryptopal.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sharonomokwale.cryptopal.Data.Market;
import com.sharonomokwale.cryptopal.R;

import java.util.List;

public class MarketDetail extends RecyclerView.Adapter<MarketDetail.ViewHolder> {

    private Context context;
    private List<Market> mMarketData;
    private DetailAdapter.onItemClickListener mListener;
    public interface onItemClickListener{
        void onItemClick(int position);

    }
    public void setOnItemClickListener(DetailAdapter.onItemClickListener listner)
    {
        mListener = listner;
    }
    public MarketDetail(){

    }
    public MarketDetail(Context context, List<Market> mMarketData) {
        this.context = context;
        this.mMarketData = mMarketData;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_market_fragment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mMarketData.get(position).getCHANGEPCT24HOUR().contains("-")){
            holder.changepct.setText(mMarketData.get(position).getCHANGEPCT24HOUR());
            holder.changepct.setTextColor(Color.RED);
        }
        else{
            holder.changepct.setText(mMarketData.get(position).getCHANGEPCT24HOUR());
            holder.changepct.setTextColor(Color.GREEN);
        }
        Glide.with(context).load(mMarketData.get(position).getImageUrl()).apply(new RequestOptions()
                .error(R.mipmap.ic_default)
              .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.coin_image);


        holder.name.setText(mMarketData.get(position).getName());
        holder.weiss = mMarketData.get(position).getRating();


        holder.fullname.setText(mMarketData.get(position).getFullName().equals("null") ? "" : mMarketData.get(position).getFullName());
        holder.price.setText(mMarketData.get(position).getPrice());
        holder.marketcap.setText(mMarketData.get(position).getMKTCAP());
        holder.volume.setText(mMarketData.get(position).getVolume());



        //Glide.with(context).load(mMarketData.get(position).getLogo()).apply(new RequestOptions()
               // .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return mMarketData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView coin_image;
        public String weiss;
        public TextView name, fullname,volume;
        public TextView price, marketcap, changepct;


        public ViewHolder(View itemView) {
            super(itemView);

            coin_image = itemView.findViewById(R.id.item_image_icon);
            name = itemView.findViewById(R.id.name);
            fullname = itemView.findViewById(R.id.fullname);
            price = itemView.findViewById(R.id.item_price_fiat);
            changepct= itemView.findViewById(R.id.price_percent_change);
            marketcap= itemView.findViewById(R.id.marketcap);
            volume = itemView.findViewById(R.id.volume);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            mListener.onItemClick(position);
                        }
                    }

                }
            });


        }
    }
}
