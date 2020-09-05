
package com.sharonomokwale.cryptopal;

import android.content.Intent;
import android.net.Uri;
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
import com.sharonomokwale.cryptopal.adapter.DetailAdapter;
import com.sharonomokwale.cryptopal.api.MySingleton;
import com.sharonomokwale.cryptopal.api.News_Util;
import com.sharonomokwale.cryptopal.Data.News;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment implements View.OnClickListener {

    private String TAG = "TESTFRAGMENT", url, logoview;
    private RecyclerView recyclerViewDetail;
    private LinearLayoutManager linearLayoutManager;

    private DetailAdapter adapterDetail;
    private List<News> dataDetail;
    private ProgressBar progressBar;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.activity_test_fragment, container, false);
        super.onCreateView(inflater, container,savedInstanceState);

        //API URL ENDPOINT
        url = News_Util.DETAIL + "&api_key=" + News_Util.APIKEY;

        logoview = getActivity().getIntent().getStringExtra(News_Util.LOGO);
        title = view.findViewById(R.id.title);
        recyclerViewDetail = view.findViewById(R.id.recycler_view_detail);
        progressBar = view.findViewById(R.id.progressBar2);
        dataDetail = new ArrayList<>();


        //IF NEWS CONNECTS SUCCESSFULLY LOAD PAGE ELSE RETURN A CONNETCION ERROR
        if (News_Util.isConnected(TestFragment.this.getActivity())) {
            mLoadDetailPage();
        } else {
            Toast.makeText(TestFragment.this.getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
        }

        linearLayoutManager = new LinearLayoutManager(TestFragment.this.getActivity());
        recyclerViewDetail.setLayoutManager(linearLayoutManager);
        adapterDetail = new DetailAdapter(TestFragment.this.getActivity(), dataDetail);
        recyclerViewDetail.setAdapter(adapterDetail);


        //WHEN AN INTEM IN THE RECYCLER VIEW IS CLICKED, OPEN THE URL TO THE NEWS
        adapterDetail.setOnItemClickListener(new DetailAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //url to open in new tab
                Uri uriUrl = Uri.parse(dataDetail.get(position).getUrl());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);

            }
        });

        return view;

    }

    private void mLoadDetailPage() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Log.d(TAG, response);
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);
                            //JSONArray jsonarray = new JSONArray(jObject.getString("articles"));
                            JSONArray jsonarray = new JSONArray(jObject.getString("Data"));
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                News data = new News(jsonobject.getString("title")
                                        , jsonobject.getString("source"),
                                        jsonobject.getString("imageurl"),
                                        logoview, jsonobject.getString("tags"),
                                        StringEscapeUtils.unescapeHtml4( jsonobject.getString("body")),
                                jsonobject.getString("url"));

                                dataDetail.add(data);
                                adapterDetail.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
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

        @Override
        public void onClick(View v) {

            Toast.makeText(TestFragment.this.getActivity(), url, Toast.LENGTH_LONG).show();
        }

}