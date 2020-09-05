
package com.sharonomokwale.cryptopal;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sharonomokwale.cryptopal.Data.Exchange;
import com.sharonomokwale.cryptopal.adapter.ExchangeAdapter;
import com.sharonomokwale.cryptopal.api.MySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ExchangeFragment extends Fragment implements View.OnClickListener {

    private String TAG = "res", url, logoview;
    private RecyclerView recyclerViewDetail;
    ProgressBar table_progressbar;
    TextView text;
    private LinearLayoutManager linearLayoutManager;


    private ExchangeAdapter adapterDetail;
    private ArrayList<ExchangeFragment> marketData;
    private List<Exchange> dataDetail;
    private ProgressBar progressBar;
    private TextView vol;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.activity_exchange_fragment, container, false);

        super.onCreateView(inflater, container,savedInstanceState);
        url = "https://api.coingecko.com/api/v3/exchanges?per_page=100";//url to coin exchanges
        Log.e("**********", "" + url);
        recyclerViewDetail = view.findViewById(R.id.recycler_view_detail);
        table_progressbar = view.findViewById(R.id.table_progressbar);
        dataDetail = new ArrayList<>();



        linearLayoutManager = new LinearLayoutManager(ExchangeFragment.this.getActivity());
        recyclerViewDetail.setLayoutManager(linearLayoutManager);

        adapterDetail = new ExchangeAdapter(ExchangeFragment.this.getActivity(), dataDetail);
        recyclerViewDetail.setAdapter(adapterDetail);
        adapterDetail.setOnItemClickListener(new ExchangeAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Uri uriUrl = Uri.parse(dataDetail.get(position).getUrl());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        loadjson lj =new loadjson();
        lj.execute();
        //mLoadDetailPage();

        return view;

    }

    private class loadjson extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            // Log.d(TAG, response);
                            JSONObject jObject = null;
                            try {
                                JSONArray jsonarray = new JSONArray(response);

                                for (int i = 0; i < jsonarray.length(); i++) {

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                    String name = jsonobject.getString("name");
                                    String imageurl = jsonobject.getString("image");
                                    String url = jsonobject.getString("url");
                                    String trustscore = jsonobject.getString("trust_score");
                                    String rank = jsonobject.getString("trust_score_rank");
                                    Exchange data = new Exchange(name, imageurl, url, trustscore, rank);


                                    dataDetail.add(data);
                                    adapterDetail.notifyDataSetChanged();
                                    // progressBar.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {
                                Log.d(TAG, error.toString());
                            }
                        }
                    }

            );

            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            table_progressbar.setVisibility(View.VISIBLE);
            recyclerViewDetail.setVisibility(View.GONE);
            Toast.makeText(ExchangeFragment.this.getActivity(), "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    table_progressbar.setVisibility(View.GONE);
                    recyclerViewDetail.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(ExchangeFragment.this.getActivity(), url, Toast.LENGTH_LONG).show();
    }

}