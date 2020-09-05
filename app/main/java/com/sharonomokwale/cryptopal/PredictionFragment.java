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
import android.widget.EditText;
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


public class PredictionFragment extends Fragment {

    String name;
    EditText coinname, Price, accuracy;
    JSONparser parser;
    Map<String, String> params;
    String url;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prediction, container, false);
        params = new HashMap<String, String>();
        url = "https://www.cryptoghana.net/cryptopal/prediction.php";
        name = (getArguments().getString("name"));
        coinname = view.findViewById(R.id.coinname);
        Price = view.findViewById(R.id.Futureprice);
        accuracy = view.findViewById(R.id.accuracy);

        coinname.setText(name);

        params.put("coinname", name);
        params.put("action","1");

        parser = new JSONparser(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jObject = null;
                try {
                    String accuracy_ = "Acurracy "+ Double.valueOf(response.getString("accuracy")) * 100;
                    String futureprice_ ="Future 50 day price prediction: $"+ response.getString("futureprice");
                    Price.setText(futureprice_);
                    accuracy.setText(accuracy_);
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(parser);

        return view;

    }
}
