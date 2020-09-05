package com.sharonomokwale.cryptopal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sharonomokwale.cryptopal.api.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class transaction_form_Fragment extends Fragment {

    //PARAMETERS
    String name, coin_price, sym, imgurl, quantity;
    TextView coinname, Price, Quantity;
    Button log_transaction;
    JSONparser parser;
    Map<String, String> params;
    String url;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_form_, container, false);
        params = new HashMap<String, String>();

        url = "https://www.cryptoghana.net/cryptopal/pindex.php";

        //GET PARAMETERS PASSED BY "FORM"
        name = (getArguments().getString("fullname")).toLowerCase();
        coin_price = (getArguments().getString("price"));
        name = name.replaceAll("\\s+", "-");
        sym = getArguments().getString("name");
        imgurl = (getArguments().getString("image"));


        coinname = view.findViewById(R.id.coinName);
        Price = view.findViewById(R.id.coinPrice);
        Quantity = view.findViewById(R.id.quantity);

        // get user id from shared prefereneces
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String id = pref.getString("id", "empty");

        //set coinname and price
        coinname.setText(name);
        Price.setText(coin_price);


        //Add to portfolio database on button click
        log_transaction = view.findViewById(R.id.button);
        log_transaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                quantity = Quantity.getText().toString().trim();
                //IF THE USER DOES NOT PROVIDE AN INPUT FOR QUANTITY PRODUCE AN ERROR ELSE SEND POST HTTP REQUEST
                if (quantity==""){
                    Toast.makeText(transaction_form_Fragment.this.getActivity(), "Please enter coin quantity", Toast.LENGTH_SHORT).show();

                }
                else{
                    //parameters to POST
                    params.put("amount", coin_price);
                    params.put("coinname", name);
                    params.put("coinquantity", quantity);
                    params.put("Userid", id);
                    params.put("imgurl",imgurl);
                    params.put("action","1");

                    parser = new JSONparser(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jObject = null;
                            Toast.makeText(transaction_form_Fragment.this.getActivity(), "Added to Portfolio", Toast.LENGTH_SHORT).show();


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError response) {
                            Log.d("Response: ", response.toString());
                        }
                    });
                    MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(parser);

                }
            }
        });


        return view;

    }
}