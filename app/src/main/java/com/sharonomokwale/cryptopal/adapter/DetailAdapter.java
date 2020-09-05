package com.sharonomokwale.cryptopal.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sharonomokwale.cryptopal.Data.News;
import com.sharonomokwale.cryptopal.R;

import java.util.List;




public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private Context context;
    private List<News> mDetailData;
    private onItemClickListener mListener;
    public interface onItemClickListener{
        void onItemClick(int position);

    }

    public void setOnItemClickListener(onItemClickListener listner)
    {
        mListener = listner;
    }

    public DetailAdapter(Context context, List<News> mDetailData) {
        this.context = context;
        this.mDetailData = mDetailData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(context).load(mDetailData.get(position).getImage_link()).apply(new RequestOptions()
                .error(R.mipmap.ic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.detail_img);


        holder.publish_at.setText(mDetailData.get(position).getPublish_at());

        holder.author.setText(mDetailData.get(position).getAuthor().equals("null") ? "" : mDetailData.get(position).getAuthor());
        holder.short_desc.setText(mDetailData.get(position).getDescription());
        holder.title.setText(mDetailData.get(position).getTitle());
        holder.url = mDetailData.get(position).getUrl();



        //Glide.with(context).load(mDetailData.get(position).getLogo()).apply(new RequestOptions()
               // .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView detail_img;
        public TextView author, title;
        public TextView desc, publish_at, short_desc;
        public ImageView img;
        public String url;

        public ViewHolder(View itemView) {
            super(itemView);

            //img = itemView.findViewById(R.id.mHeaderView);
            detail_img = itemView.findViewById(R.id.detail_img);
            short_desc = itemView.findViewById(R.id.short_desc);
            author = itemView.findViewById(R.id.author);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            publish_at = itemView.findViewById(R.id.publish_at);

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
