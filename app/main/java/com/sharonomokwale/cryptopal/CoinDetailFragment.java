package com.sharonomokwale.cryptopal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sharonomokwale.cryptopal.api.CoinDetailUtil;
import com.sharonomokwale.cryptopal.api.MarketUtil;
import com.sharonomokwale.cryptopal.api.MySingleton;
import com.sharonomokwale.cryptopal.api.TableMarker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class   CoinDetailFragment extends Fragment {


    CoinDetailFragment fab;

    ChipGroup tableChipGroup;
    Chip detail_daily_chip;
    Chip detail_monthly_chip;

    LineChart chart;
    ProgressBar table_progressbar;
    float price = 0.00f;

    String predurl;
    Map<String, String> params;

    String checkedFilter = "DAILY";

    List<String> dateMonthly;
    List<Float> pricesMonthly;

    List<String> dateDaily;
    List<Float> pricesDaily;

    List<String> dateThreeMonths;
    List<Float> pricesThreeMonths;

    JSONparser parser;


    private String url,url1,url2,ohlc_url;
    private String name,name_,sym,coin_price,imgurl,weiss;

    String high,low,open,close;

    TextView changepct,marketcap,coinname;
    TextView High,Low,Close,Open;
    Button predbuton;

    public String TAG = "CoinDetailFragment";


    public CoinDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dateDaily = new ArrayList<>();
        pricesDaily = new ArrayList<>();

        dateMonthly = new ArrayList<>();
        pricesMonthly = new ArrayList<>();

        dateThreeMonths = new ArrayList<>();
        pricesThreeMonths = new ArrayList<>();

        params = new HashMap<String, String>();


        name = (getArguments().getString("fullname")).toLowerCase();
        name_= getArguments().getString("name");
        weiss = getArguments().getString("weiss");
        coin_price = (getArguments().getString("price"));
        //this is because of the api
        name = name.replaceAll("\\s+","-");
        sym =getArguments().getString("name");
        imgurl = (getArguments().getString("image"));
        predurl ="https://www.cryptoghana.net/cryptopal/prediction.php";

        View view=  inflater.inflate(R.layout.fragment_coin_detail, container, false);

        //FAB
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new Form();
                Bundle args = new Bundle();
                args.putString("image",imgurl);
                args.putString("price",coin_price);
                args.putString("fullname", name);
                args.putString("weiss", weiss);
                selectedFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().add(selectedFragment, "form")
                        .addToBackStack(null).replace(R.id.fragment_container,
                        selectedFragment).commit();


            }
        });
        predbuton = (Button)view.findViewById(R.id.predbutton);
        predbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = new PredictionFragment();
                Bundle args = new Bundle();
                args.putString("name",name_);
                selectedFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().add(selectedFragment, "predictionfragment")
                        .addToBackStack(null).replace(R.id.fragment_container,
                        selectedFragment).commit();

            }
        });


        High=view.findViewById(R.id.detail_price_up);
        Low=view.findViewById(R.id.detail_price_down);
        Open = view.findViewById(R.id.detail_open);
        Close=view.findViewById(R.id.detail_close);


        coinname= (TextView) view.findViewById(R.id.coin_name);
        changepct = (TextView) view.findViewById(R.id.changepct);
        marketcap= (TextView) view.findViewById(R.id.marketcap) ;


        changepct.setText(getArguments().getString("change_percentage"));
        marketcap.setText(getArguments().getString("marketcap"));
        coinname.setText(getArguments().getString("fullname"));

        ///Negative % change
        if(getArguments().getString("change_percentage").contains("-")){
            changepct.setTextColor(Color.RED);
        }

        //Positive % change
        else{
            changepct.setTextColor(Color.RED);
        }
        super.onCreateView(inflater, container,savedInstanceState);

        //one day
        url = "https://api.coingecko.com/api/v3/coins/"+name+"/market_chart?vs_currency=usd&days=1";

        //one month
        url1 = "https://api.coingecko.com/api/v3/coins/"+name+"/market_chart?vs_currency=usd&days=30";

        //three months
        url2= "https://api.coingecko.com/api/v3/coins/"+name+"/market_chart?vs_currency=usd&days=90";

        //open high low closed based on crypto clicked on
        ohlc_url="https://min-api.cryptocompare.com/data/v2/histoday?fsym="+sym+"&tsym=USD&limit=10&api_key="+CoinDetailUtil.APIKEY;


        Log.e("**********", "" + url);

        //If it connects
        if (MarketUtil.isConnected(CoinDetailFragment.this.getActivity())) {
            tableChipGroup = view.findViewById(R.id.detail_chip_group);
            detail_daily_chip = view.findViewById(R.id.detail_daily_chip);
            detail_monthly_chip = view.findViewById(R.id.detail_monthly_chip);
            tableChipGroup.setSingleSelection(true);
            tableChipGroup.setSelectionRequired(true);
            chart = view.findViewById(R.id.chart);
            table_progressbar = view.findViewById(R.id.table_progressbar);

            chart.animateX(400);

            chart.setDrawBorders(false);
            chart.setPinchZoom(true);
            chart.setDrawGridBackground(false);
            chart.setScaleEnabled(true);
            chart.getXAxis().setTextColor(getResources().getColor(R.color.contentTextColor));
            chart.getAxisLeft().setTextColor(getResources().getColor(R.color.contentTextColor));
            chart.getAxisLeft().setEnabled(true);
            chart.getAxisRight().setEnabled(false);
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getXAxis().setDrawGridLines(false);
            chart.setExtraRightOffset(getResources().getDimension(R.dimen.table_margin));
            chart.getLegend().setEnabled(false);
            getpred();
            ohlc();
            drawChart();
            drawChart_oneMonth();
            drawChart_threeMonth();
        } else {
            Toast.makeText(CoinDetailFragment.this.getActivity(), "check your internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
    private void setDateChart(List<String> date) {

        XAxis xAxis = chart.getXAxis();
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(date));
        IMarker marker = new TableMarker(getActivity().getBaseContext(), R.layout.markerview_table, date);
        chart.setMarker(marker);
    }

    private void setDataChart(List<Float> price) {
        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < price.size(); i++) {
            if (price.get(i) != 0)
                values1.add(new Entry(i, price.get(i)));
        }
        boolean circleStatus = price.size() <= 90;

        LineDataSet lineDataSet;
        lineDataSet = new LineDataSet(values1, "luinn");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet.setLineWidth(3f);
            lineDataSet.setDrawCircles(circleStatus);
            lineDataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
            lineDataSet.setColor(getResources().getColor(R.color.colorAccent));
            lineDataSet.setCircleRadius(6f);
            lineDataSet.setCircleHoleColor(Color.WHITE);
            lineDataSet.setCircleHoleRadius(4f);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);
            lineDataSet.setDrawVerticalHighlightIndicator(false);
            LineData data = new LineData(lineDataSet);
            data.setDrawValues(false);
            chart.setData(data);
            chart.invalidate();
        }
    }
    void loadChips() {

        switch (checkedFilter) {
            case "DAILY":
                tableChipGroup.check(R.id.detail_daily_chip);
                setDateChart(dateDaily);
                setDataChart(pricesDaily);
                break;
           case "MONTHLY":
                tableChipGroup.check(R.id.detail_monthly_chip);
                setDateChart(dateMonthly);
                setDataChart(pricesMonthly);
                break;
           case "3MONTHS":
                tableChipGroup.check(R.id.detail_three_months_chip);
                setDateChart(dateThreeMonths);
                setDataChart(pricesThreeMonths);
                break;

        }

        tableChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.detail_daily_chip:
                    checkedFilter = "DAILY";
                    chart.clearValues();
                    setDateChart(dateDaily);
                    setDataChart(pricesDaily);
                    break;
                case R.id.detail_monthly_chip:
                    chart.clearValues();
                    checkedFilter = "MONTHLY";
                    setDateChart(dateMonthly);
                    setDataChart(pricesMonthly);
                    break;
                 case R.id.detail_three_months_chip:
                    chart.clearValues();
                    checkedFilter = "3MONTHS";
                    setDateChart(dateThreeMonths);
                    setDataChart(pricesThreeMonths);
                    break;

            }
        });
    }

    public void drawChart() {
        hideChart();
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        //ohlc();
                        //Log.v(TAG, (String.valueOf(dataDetail.get(0).getHigh())));
                        Log.d(TAG, "Response: " + response);
                        JSONObject jObject = null;

                        try {
                            //Daily{time and close}
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("prices");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONArray jsonArr = jsonarray.getJSONArray(i);
                                Date d = new Date(jsonArr.getLong(0));
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String time = sdf.format(d);

                                price= Float.valueOf(jsonArr.getString(1));
                                dateDaily.add(time);
                                pricesDaily.add(price);
                            }
                            loadChips();
                            showChart();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d(TAG, error.toString());
                }
            }
        }

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq);
    }
    void hideChart() {
        chart.setVisibility(View.GONE);
        tableChipGroup.setVisibility(View.GONE);
        table_progressbar.setVisibility(View.VISIBLE);
    }
    void showChart() {
        chart.setVisibility(View.VISIBLE);
        tableChipGroup.setVisibility(View.VISIBLE);
        table_progressbar.setVisibility(View.GONE);
    }
    void ohlc(){
        StringRequest strReq = new StringRequest(Request.Method.GET, ohlc_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        showChart();
                        Log.d(TAG, "Response: " + response);
                        JSONObject jObject = null;

                        try {
                            //Daily{time and close}
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonobj = jsonObject.getJSONObject("Data");
                            JSONArray jsonarray = jsonobj.getJSONArray("Data");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jobj = jsonarray.getJSONObject(i);

                                       if(i < (jsonarray.length()-1)){
                                           continue;
                                        }
                                        else{
                                           NumberFormat nf = NumberFormat.getCurrencyInstance();
                                             high = nf.format(new BigDecimal(jobj.getString("high")));
                                             low = nf.format(new BigDecimal(jobj.getString("low")));
                                             close = nf.format(new BigDecimal(jobj.getString("close")));
                                             open = nf.format(new BigDecimal(jobj.getString("open")));
                                        }
                                        High.setText(high);
                                        Low.setText(low);
                                        Close.setText(close);
                                        Open.setText(open);

                                    }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d(TAG, error.toString());
                }
            }
        }

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq);

    }
    public void drawChart_oneMonth() {
        StringRequest strReq = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        JSONObject jObject = null;

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("prices");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONArray jsonArr = jsonarray.getJSONArray(i);
                                Date d = new Date(jsonArr.getLong(0));
                                SimpleDateFormat sdf =  new SimpleDateFormat(getString(R.string.Monthformat));
                                String time = sdf.format(d);

                                price= Float.valueOf(jsonArr.getString(1));
                                dateMonthly.add(time);
                                pricesMonthly.add(price);
                            }
                            loadChips();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d(TAG, error.toString());
                }
            }
        }

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq);
    }
    public void drawChart_threeMonth() {
        StringRequest strReq = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        JSONObject jObject = null;

                        try {
                            //Daily{time and close}
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("prices");
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONArray jsonArr = jsonarray.getJSONArray(i);
                                Date d = new Date(jsonArr.getLong(0));
                                SimpleDateFormat sdf =  new SimpleDateFormat(getString(R.string.Monthformat));
                                String time = sdf.format(d);

                                price= Float.valueOf(jsonArr.getString(1));
                                dateThreeMonths.add(time);
                                pricesThreeMonths.add(price);
                            }
                            loadChips();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.d(TAG, error.toString());
                }
            }
        }

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq);
    }
    public void getpred(){
        params.put("coinname",name_);
        params.put("action","0");
        parser = new JSONparser(Request.Method.POST,
                predurl,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("response", String.valueOf(response));
                        JSONObject jObject = null;
                        try {

                            if(response.getString("success").equals("1")){
                                predbuton.setVisibility(View.VISIBLE);

                            }
                            else{
                                predbuton.setVisibility(View.GONE);}
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
}