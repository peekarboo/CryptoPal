package com.sharonomokwale.cryptopal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sharonomokwale.cryptopal.Data.Exchange;
import com.sharonomokwale.cryptopal.Data.News;
import com.sharonomokwale.cryptopal.R;

import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {

    private Context context;
    private List<Exchange> mExchangeData;
    private ExchangeAdapter.onItemClickListener mListener;
    public interface onItemClickListener{
        void onItemClick(int position);

    }

    public void setOnItemClickListener(ExchangeAdapter.onItemClickListener listner)
    {
        mListener = listner;
    }

    public ExchangeAdapter(Context context, List<Exchange> mExchangeData) {
        this.context = context;
        this.mExchangeData = mExchangeData;
    }

    @Override
    public ExchangeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_layout, parent, false);

        return new ExchangeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(mExchangeData.get(position).getImageUrl()).apply(new RequestOptions()
                .error(R.mipmap.ic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.coin_image);
        holder.url = mExchangeData.get(position).getUrl();
        holder.rank = mExchangeData.get(position).getRank();
        holder.trustscore = mExchangeData.get(position).getTrustscore();
        holder.name.setText(mExchangeData.get(position).getName());
    }



    @Override
    public int getItemCount() {
        return mExchangeData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView coin_image;
        public TextView name;
        public String url,rank,trustscore;

        public ViewHolder(View itemView) {
            super(itemView);

            coin_image= itemView.findViewById(R.id.item_image_icon);
            name = itemView.findViewById(R.id.exchangename);

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
