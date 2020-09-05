
package com.sharonomokwale.cryptopal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sharonomokwale.cryptopal.Data.Market;
import com.sharonomokwale.cryptopal.adapter.DetailAdapter;
import com.sharonomokwale.cryptopal.adapter.MarketDetail;
import com.sharonomokwale.cryptopal.api.MarketUtil;
import com.sharonomokwale.cryptopal.api.MySingleton;
import com.sharonomokwale.cryptopal.api.News_Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MarketFragment extends Fragment implements View.OnClickListener {

    private String TAG = "res", url,url2, logoview;
    private RecyclerView recyclerViewDetail;
    ProgressBar table_progressbar;
    TextView text;
    private LinearLayoutManager linearLayoutManager;


    private MarketDetail adapterDetail;
    private ArrayList<MarketFragment> marketData;
    private List<Market> dataDetail;
    private ProgressBar progressBar;
    private TextView vol;

    String checkedFilter = "USD";
    ChipGroup toggleGroup;
    Chip usdbutton,gbpbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.layouttest2, container, false);

        super.onCreateView(inflater, container,savedInstanceState);
        url = MarketUtil.DETAIL + "&api_key=" + News_Util.APIKEY;
        Log.e("**********", "" + url);
        url2 = "https://min-api.cryptocompare.com/data/top/totalvolfull?limit=30&tsym=gbp&api_key=2d6f1f92684971b2967387d222f203a6d2e74d296c0caa6242c2661223783d4e";
        recyclerViewDetail = view.findViewById(R.id.recycler_view_detail);
        table_progressbar = view.findViewById(R.id.table_progressbar);
        toggleGroup = view.findViewById(R.id.togglebuttons);
        usdbutton = view.findViewById(R.id.usdbutton);
        gbpbutton = view.findViewById(R.id.gbpbutton);
        dataDetail = new ArrayList<>();

        if (MarketUtil.isConnected(MarketFragment.this.getActivity())) {
             loadjson lj =new loadjson();
             lj.execute();
            }
        else {
            Toast.makeText(MarketFragment.this.getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
        }
        linearLayoutManager = new LinearLayoutManager(MarketFragment.this.getActivity());
        recyclerViewDetail.setLayoutManager(linearLayoutManager);

        adapterDetail = new MarketDetail(MarketFragment.this.getActivity(), dataDetail);
        recyclerViewDetail.setAdapter(adapterDetail);
        adapterDetail.setOnItemClickListener(new DetailAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Fragment selectedFragment = new CoinDetailFragment();
                Bundle args = new Bundle();
                args.putString("image",dataDetail.get(position).getImageUrl());
                args.putString("name",dataDetail.get(position).getName());
                args.putString("fullname", dataDetail.get(position).getFullName());
                args.putString("price", dataDetail.get(position).getPrice());
                args.putString("marketcap",dataDetail.get(position).getMKTCAP());
                args.putString("change_percentage",dataDetail.get(position).getCHANGEPCT24HOUR());
                args.putString("weiss", dataDetail.get(position).getRating());
                selectedFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().add(selectedFragment, "coindetailfragment")
                        .addToBackStack(null).replace(R.id.fragment_container,
                        selectedFragment).commit();


            }
        });

        return view;

    }
    private class loadjson extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            table_progressbar.setVisibility(View.VISIBLE);
            recyclerViewDetail.setVisibility(View.GONE);
            loadcurrency();
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            table_progressbar.setVisibility(View.VISIBLE);
            recyclerViewDetail.setVisibility(View.GONE);
            Toast.makeText(MarketFragment.this.getActivity(), "Please wait...", Toast.LENGTH_SHORT)
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

        Toast.makeText(MarketFragment.this.getActivity(), url, Toast.LENGTH_LONG).show();
    }
    void setUsdbutton_details(){
        if(!dataDetail.isEmpty()){
            dataDetail.clear();
            adapterDetail.notifyDataSetChanged();
        }
        table_progressbar.setVisibility(View.VISIBLE);
        recyclerViewDetail.setVisibility(View.GONE);

        String stuff [] = new String[2];
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Log.d(TAG, response);
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);

                            JSONArray jsonarray = new JSONArray(jObject.getString("Data"));

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                JSONObject getSth = jsonobject.getJSONObject("CoinInfo");
                                JSONObject rating = getSth.getJSONObject("Rating");
                                JSONObject weiss = rating.getJSONObject("Weiss");
                                String weissrating = weiss.getString("Rating");
                                JSONObject getSth1 = jsonobject.getJSONObject("DISPLAY");
                                JSONObject getSth2 = getSth1.getJSONObject("USD");
                                String name = getSth.getString("Name");
                                String fullname = getSth.getString("FullName");
                                String imageurl = "https://www.cryptocompare.com"+getSth.getString("ImageUrl");
                                String Mktcap = getSth2.getString("MKTCAP");
                                String PRICE = getSth2.getString("PRICE");
                                String CHANGEPCT = getSth2.getString("CHANGEPCT24HOUR")+"%";
                                String volume = getSth2.getString("TOTALVOLUME24HTO");

                                Market data = new Market(name,fullname,imageurl,PRICE,Mktcap,CHANGEPCT,volume,weissrating);


                                dataDetail.add(data);
                                adapterDetail.notifyDataSetChanged();
                                table_progressbar.setVisibility(View.GONE);
                                recyclerViewDetail.setVisibility(View.VISIBLE);
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
    }
    void setGbpbutton_details(){
        if(!dataDetail.isEmpty()){
            dataDetail.clear();
         adapterDetail.notifyDataSetChanged();
        }
        table_progressbar.setVisibility(View.VISIBLE);
        recyclerViewDetail.setVisibility(View.GONE);

        String stuff [] = new String[2];
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Log.d(TAG, response);
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);

                            JSONArray jsonarray = new JSONArray(jObject.getString("Data"));

                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                JSONObject getSth = jsonobject.getJSONObject("CoinInfo");
                                JSONObject rating = getSth.getJSONObject("Rating");
                                JSONObject weiss = rating.getJSONObject("Weiss");
                                String weissrating = weiss.getString("Rating");
                                JSONObject getSth1 = jsonobject.getJSONObject("DISPLAY");
                                JSONObject getSth2 = getSth1.getJSONObject("GBP");
                                String name = getSth.getString("Name");
                                String fullname = getSth.getString("FullName");
                                String imageurl = "https://www.cryptocompare.com"+getSth.getString("ImageUrl");
                                String Mktcap = getSth2.getString("MKTCAP");
                                String PRICE = getSth2.getString("PRICE");
                                String CHANGEPCT = getSth2.getString("CHANGEPCT24HOUR")+"%";
                                String volume = getSth2.getString("TOTALVOLUME24HTO");

                                Market data = new Market(name,fullname,imageurl,PRICE,Mktcap,CHANGEPCT,volume,weissrating);


                                dataDetail.add(data);
                                adapterDetail.notifyDataSetChanged();
                                table_progressbar.setVisibility(View.GONE);
                                recyclerViewDetail.setVisibility(View.VISIBLE);
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

    }
    void loadcurrency(){
        switch (checkedFilter) {
            case "USD":
                toggleGroup.check(R.id.usdbutton);
                setUsdbutton_details();
                break;
            case "GBP":
                toggleGroup.check(R.id.gbpbutton);
                setGbpbutton_details();
                break;

        }

        toggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.usdbutton:
                    checkedFilter = "USD";
                    setUsdbutton_details();
                    break;
                case R.id.gbpbutton:
                    checkedFilter = "GBP";
                    setGbpbutton_details();
                    break;


            }
        });


    }

}