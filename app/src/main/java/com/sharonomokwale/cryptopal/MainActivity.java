package com.sharonomokwale.cryptopal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {


    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewRegister;
    JSONparser parser;
    Map<String, String> params;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url ="https://www.cryptoghana.net/cryptopal/index.php";
        params = new HashMap<String, String>();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String loginval = "one";
                params.put("username",username);
                params.put("email",loginval);
                params.put("password",password);

                if (username != null) {
                    parser  = new JSONparser(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject jObject = null;
                            try {

                                if(response.getString("message").equals("Successfully logged in")) {
                                    String id =  response.getString("id");
                                    Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                    Intent moveToLogin = new Intent(MainActivity.this, HomeActivity.class);


                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edt = pref.edit();
                                    edt.putString("id", id);
                                    edt.commit();


                                    moveToLogin.putExtra("id",id );
                                    startActivity(moveToLogin);



                                }
                                else
                                    Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError response) {
                            Log.d("Response: ", response.toString());
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(parser);


                }else{
                    Toast.makeText(MainActivity.this, "Unregistered user, or incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
