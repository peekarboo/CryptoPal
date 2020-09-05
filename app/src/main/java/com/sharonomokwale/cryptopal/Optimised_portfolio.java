package com.sharonomokwale.cryptopal;

//connect to mysql
//edit the values
//for the layout
//
// pop up button asking how much do you want to invest/*

// pie chart
// then card view recycler view
// the onclick for the card will link to coin detail fragment*/
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Optimised_portfolio extends AppCompatActivity {

    private static final String url = "jdbc:mysql://108.167.137.46:3306/etoilero_cryptopal";
    private static final String user = "etoilero_root";
    private static final String pass = "new123";
    TextView coin1,coin2,coin3,coin4,coin5,coin1_,coin2_,coin3_,coin4_,coin5_,coin_1,coin_2,coin_3,coin_4,coin_5,returns,sharperatio,volatility;
    PieChart pieChart;
    List<Integer> weights = new ArrayList<>();
    List<String> names = new ArrayList<>();
    ProgressBar progressBar;
    RelativeLayout rel_lay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheetlayout);
        coin1 = findViewById(R.id.pcoin1);
        coin2 = findViewById(R.id.pcoin2);
        coin3 = findViewById(R.id.pcoin3);
        coin4 = findViewById(R.id.pcoin4);
        coin5 = findViewById(R.id.pcoin5);
        coin1_ = findViewById(R.id.coinname1);
        coin2_ = findViewById(R.id.coinname2);
        coin3_ = findViewById(R.id.coinname3);
        coin4_ = findViewById(R.id.coinname4);
        coin5_ = findViewById(R.id.coinname5);
        coin_1 = findViewById(R.id.coin1);
        coin_2 = findViewById(R.id.coin2);
        coin_3 = findViewById(R.id.coin3);
        coin_4 = findViewById(R.id.coin4);
        coin_5 = findViewById(R.id.coin5);
        returns = findViewById(R.id.returns_);
        sharperatio= findViewById(R.id.sharpe_ratio);
        volatility = findViewById(R.id.volatilty_);

        pieChart = findViewById(R.id.piechart);

        rel_lay= (RelativeLayout) findViewById(R.id.rel_lay);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);


        ConnectMySql connectMySql = new ConnectMySql();
        connectMySql.execute("");
    }

    private void setData(List<Integer>weights, List<String> names)
    {
        for(int i =0; i<weights.size(); i++){

            // Set the percentage of language used
            coin1.setText(String.valueOf(weights.get(0)).concat("%"));
            coin2.setText(String.valueOf(weights.get(1)).concat("%"));
            coin3.setText(String.valueOf(weights.get(3)).concat("%"));
            coin4.setText(String.valueOf(weights.get(4)).concat("%"));
            coin5.setText(String.valueOf(weights.get(5)).concat("%"));
            returns.setText(String.valueOf(weights.get(2)/100).concat("%"));
            sharperatio.setText(String.valueOf(weights.get(6)/100).concat("%"));
            volatility.setText(String.valueOf(weights.get(7)/100).concat("%"));


        }


        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "coin1",
                        Integer.parseInt(coin1.getText().toString().replaceAll("%","")),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "coin2",
                        Integer.parseInt(coin2.getText().toString().replaceAll("%","")),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "coin3",
                        Integer.parseInt(coin3.getText().toString().replaceAll("%","")),
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "coin4",
                        Integer.parseInt(coin4.getText().toString().replaceAll("%","")),
                        Color.parseColor("#29B6F6")));



        // To animate the pie chart


    }






    private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Optimised_portfolio.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);


                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select *  from Prediction");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    weights.add((int) ((Double.valueOf(rs.getString(1).toString()))*100.0));
                    names.add(rs.getString(2).toString());
                    result += rs.getString(2).toString() + "\n";
                }
                res = result;

                setData(weights,names);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coin1_.setText(names.get(0).replaceAll("-%",""));
                        coin2_.setText(names.get(1).replaceAll("-%",""));
                        coin3_.setText(names.get(3).replaceAll("-%",""));
                        coin4_.setText(names.get(4).replaceAll("-%",""));
                        coin5_.setText(names.get(5).replaceAll("-%",""));
                        coin_1.setText(names.get(0).replaceAll("-%",""));
                        coin_2.setText(names.get(1).replaceAll("-%",""));
                        coin_3.setText(names.get(3).replaceAll("-%",""));
                        coin_4.setText(names.get(4).replaceAll("-%",""));
                        coin_5.setText(names.get(5).replaceAll("-%",""));

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            pieChart.startAnimation();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    rel_lay.setVisibility(View.VISIBLE);

                }
            });


        }
    }


}