package com.sharonomokwale.cryptopal;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharonomokwale.cryptopal.api.MySingleton;
import com.sharonomokwale.cryptopal.api.TableMarker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Form extends Fragment {
    private static String investment_Amount;
    private static EditText editamount, editreturn;
    private static int iamount;
    Float pricePercentage;
    static String url;
    String usdprice;
    static String TAG="Form.java";
    String name, coin_price,sym,imgurl,weiss;


    TextView total_invested;
    TextView weiss_rating;
    TextView riskreward;
    TextView DCAreturn;



    LineChart chart;
    ProgressBar table_progressbar;


    static List<String> allDates;
    static List<String> reverseDates;
    static List<Float> allPrices;
    static List<String> allPrices_;
    static List<Float> cryptoprices;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.activity_form, container, false);
        super.onCreate(savedInstanceState);
        name = (getArguments().getString("fullname")).toLowerCase();
        coin_price = (getArguments().getString("price"));
        name = name.replaceAll("\\s+","-");
        sym =getArguments().getString("name");
        imgurl = (getArguments().getString("image"));
        weiss  = getArguments().getString("weiss");
        DCAreturn = view.findViewById(R.id.DCAreturn);
        riskreward = view.findViewById(R.id.riskreward);
        total_invested =view.findViewById(R.id.textView);
        weiss_rating= view.findViewById(R.id.textView_PV);

        allDates = new ArrayList<>();
        reverseDates = new ArrayList<>();
        cryptoprices = new ArrayList<>();
        allPrices = new ArrayList<>();
        allPrices_ = new ArrayList<>();
        editamount = view.findViewById(R.id.txtName);
        editreturn = view.findViewById(R.id.expectedreturn);
        table_progressbar = view.findViewById(R.id.table_progressbar);
        Toast.makeText(Form.this.getActivity(), weiss, Toast.LENGTH_SHORT).show();


        chart = view.findViewById(R.id.chart);
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


        Button enters = view.findViewById(R.id.btnSend);
        enters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editreturn.setVisibility(View.GONE);
                editamount.setVisibility(View.GONE);
                enters.setVisibility(View.GONE);
                loadjson lj =new loadjson();
                lj.execute();

            }
        });
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                //Portfolio myDataList=new Portfolio();

                AlertDialog.Builder builder = new AlertDialog.Builder(Form.this.getActivity());
                builder.setTitle("Confirmation").
                        setMessage("WOULD YOU LIKE TO LOG A TRANSACTION?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Fragment selectedFragment = new transaction_form_Fragment();
                                Bundle args = new Bundle();
                                args.putString("image",imgurl);
                                args.putString("price",coin_price);
                                args.putString("fullname", name);
                                selectedFragment.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().add(selectedFragment, "form")
                                        .addToBackStack(null).replace(R.id.fragment_container,
                                        selectedFragment).commit();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            }



                //portfolioFragment.myDatabase.portfolioDao().insertItem(myDataLists)
        });
        return view;
    }
    private class loadjson extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            table_progressbar.setVisibility(View.VISIBLE);
            try {
                getprices(allPrices_,allDates);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            table_progressbar.setVisibility(View.VISIBLE);
            chart.setVisibility(View.GONE);
            Toast.makeText(Form.this.getActivity(), "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    table_progressbar.setVisibility(View.GONE);
                    chart.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    public  void getprices(List<String> allPrices_, List<String> allDates) throws ParseException {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        LocalDate now = LocalDate.now();
        String test = now.format(dateFormatter);

        for (int i = 0; i < 12; i++) {
            //gets 12 periods
            String[] usdprice = {"test"};
            String months = String.valueOf(now.minusMonths(i).format(dateFormatter));
            allDates.add(months);
            url = "https://api.coingecko.com/api/v3/coins/bitcoin/history?date=" + allDates.get(i) + "&localization=false";
            // getprice(url);
            StringRequest strReq = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            //ohlc();
                            //Log.v(TAG, (String.valueOf(dataDetail.get(0).getHigh())));
                            Log.d(TAG, "Response: " + response);
                            JSONObject jObject = null;

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonobject = jsonObject.getJSONObject("market_data");
                                JSONObject jsonobj_ = jsonobject.getJSONObject("current_price");
                                usdprice[0] = jsonobj_.getString("usd");
                                gottatestthis(usdprice[0]);

                            }

                            catch (JSONException ex) {
                                ex.printStackTrace();
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




    }

    void gottatestthis(String usd){
        Float totalval= 0f;
        allPrices_.add(usd);
        if(allPrices_.size()==12){
            Float latest =Float.valueOf(allPrices_.get(11));
            String amount = String.valueOf(editamount.getText());
            String rexpecte = String.valueOf(editreturn.getText());
            int amount_ = Integer.valueOf(amount);
            int rexpected = Integer.valueOf(rexpecte);
            String ratio = ratio(amount_,rexpected);

           // Calculate with one year historical data
            Float twelve = Float.valueOf(amount)/12f;

            //get the amount bought in cryptocurrency per month
            for(int i = 0; i<allPrices_.size();i++){
                cryptoprices.add(twelve/Float.valueOf(allPrices_.get(i)));
            }
            for(int i = 0; i<cryptoprices.size();i++){
               totalval += cryptoprices.get(i);
               allPrices.add((totalval)*Float.valueOf(allPrices_.get(i)));
            }
            for(int i = allDates.size()-1; i>1;i--){
                reverseDates.add(allDates.get(i));

            }

           totalval = totalval*latest;
            Log.d(TAG,String.valueOf(allPrices.size()));
            total_invested.setText(String.valueOf(editamount.getText()));
            riskreward.setText(ratio);
            weiss_rating.setText(weiss);
            DCAreturn.setText(String.valueOf(totalval));
           /**/

            setDataChart(allPrices);
            setDateChart(reverseDates);

        }


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


    int getratio(int amount, int expected){
            if (expected == 0)
                return amount;
            else
                return getratio(expected, amount % expected);
        }
        String ratio(int amount, int expected) {
            final int gcd = getratio(amount,expected);
           return (amount/gcd + ":" + expected/gcd);
        }



}


