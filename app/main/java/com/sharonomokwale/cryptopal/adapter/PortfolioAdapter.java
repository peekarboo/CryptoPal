package com.sharonomokwale.cryptopal.adapter;

import android.content.Context;
import android.util.Log;
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
import com.sharonomokwale.cryptopal.Data.Portfolio;
import com.sharonomokwale.cryptopal.R;

import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {
    private Context context;
    private List<Portfolio> mPortfolioLists;
    private DetailAdapter.onItemClickListener mListener;
    private Double amount_ , quantity;

    public PortfolioAdapter(Context context, List<Portfolio> mPortfolioLists) {
        this.context = context;
        this.mPortfolioLists = mPortfolioLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.portfoliocoinlayout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String img =mPortfolioLists.get(i).getImageurl().replaceAll("\\\\/", "/");
        Log.v("img",mPortfolioLists.get(i).getImageurl());

            Glide.with(context).load(img).apply(new RequestOptions()
               .error(R.mipmap.ic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(viewHolder.Coinimage);
            viewHolder.coinname.setText(mPortfolioLists.get(i).getCoinname());
            viewHolder.coinprice.setText(mPortfolioLists.get(i).getAmount());
            viewHolder.coinamount.setText(mPortfolioLists.get(i).getQuantity());
            Integer.valueOf( mPortfolioLists.get(i).getQuantity() );
       String amount = mPortfolioLists.get(i).getAmount().replaceAll("[$] ","") ;
       amount_=Double.valueOf(amount.replaceAll(",",""));
       quantity =Double.valueOf( mPortfolioLists.get(i).getQuantity() );

            viewHolder.amount.setText("$"+String.valueOf(amount_*quantity));
        }

    @Override
    public int getItemCount() {
        if (mPortfolioLists != null){
            Log.v("TAF", String.valueOf(mPortfolioLists.size()));
            return mPortfolioLists.size();}
        else return 0;
    }
    public void setWords(List<Portfolio> words){
        mPortfolioLists = words;
        notifyDataSetChanged();
    }
    public Portfolio getportfolio(int position) {
        return mPortfolioLists.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView coinname,amount,coinamount,pricechange, coinprice;
            ImageView Coinimage;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Coinimage = itemView.findViewById(R.id.item_image_icon);
                coinname=itemView.findViewById(R.id.coinname);
                coinamount = itemView.findViewById(R.id.volume_owned);
                coinprice=itemView.findViewById(R.id.item_price);
                amount = itemView.findViewById(R.id.volume_owned_dollar);
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
