
package com.sharonomokwale.cryptopal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharonomokwale.cryptopal.Data.Market;
import com.sharonomokwale.cryptopal.Data.Portfolio;
import com.sharonomokwale.cryptopal.adapter.MarketDetail;
import com.sharonomokwale.cryptopal.adapter.PortfolioAdapter;
import com.sharonomokwale.cryptopal.api.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class portfolioFragment extends Fragment implements View.OnClickListener {

    private String TAG = "res", url;
    private RecyclerView recyclerViewDetail;
    FloatingActionButton fab;


    private LinearLayoutManager linearLayoutManager;
    Map<String, String> params;
    Map<String, String> params_;


    private PortfolioAdapter padapterDetail;

    private List<Portfolio> pdataDetail;

    JSONparser parser;


    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.portfolio_recyclerview, container, false);

        super.onCreateView(inflater, container,savedInstanceState);
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String id = pref.getString("id", "empty");
        url = "https://www.cryptoghana.net/cryptopal/pindex.php";
        params = new HashMap<String, String>();
        params_ = new HashMap<String, String>();
        recyclerViewDetail = view.findViewById(R.id.recycler_view_detail);
        fab = view.findViewById(R.id.fab);
        pdataDetail = new ArrayList<>();

        params.put("action", "0");
        params.put("Userid",id);
        mLoadDetailPage();

        linearLayoutManager = new LinearLayoutManager(portfolioFragment.this.getActivity());
        recyclerViewDetail.setLayoutManager(linearLayoutManager);

        padapterDetail=new PortfolioAdapter(getActivity(), pdataDetail);
        recyclerViewDetail.setAdapter(padapterDetail);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                params_.put("action", "2");
                params_.put("coinname",pdataDetail.get(position).getCoinname());
                params_.put("Userid",id);
                parser = new JSONparser(Request.Method.POST,
                        url,params_,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v("response", String.valueOf(response));
                                JSONObject jObject = null;
                                Toast.makeText(portfolioFragment.this.getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

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

                MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(parser);



            }
        }).attachToRecyclerView(recyclerViewDetail);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                Fragment selectedFragment = new MarketFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(selectedFragment, "Market")
                        .addToBackStack(null).replace(R.id.fragment_container,
                        selectedFragment).commit();

            }



            //portfolioFragment.myDatabase.portfolioDao().insertItem(myDataLists)
        });

        return view;

    }
    private void mLoadDetailPage() {

         parser = new JSONparser(Request.Method.POST,
                url,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject jObject = null;
                        try {
                            JSONArray jsonarray = response.getJSONArray("prediction");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONArray jsonArr = jsonarray.getJSONArray(i);
                                String coinname = jsonArr.getString(0);
                                String amount = jsonArr.getString(1);
                                String imageurl = jsonArr.getString(4);
                                String quantity = jsonArr.getString(3);
                                Portfolio data = new Portfolio(coinname,amount,imageurl,quantity);
                                pdataDetail.add(data);
                                padapterDetail.notifyDataSetChanged();}
                            //progressBar.setVisibility(View.GONE);
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

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(parser);
    }


    @Override
    public void onClick(View view) {

    }
}