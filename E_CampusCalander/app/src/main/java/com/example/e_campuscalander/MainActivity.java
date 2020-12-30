package com.example.e_campuscalander;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private TextView mTextView2;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Context context;
    private EditText userId;
    private EditText userPw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mTextView = (TextView) findViewById(R.id.text);
        //mTextView2 = (TextView) findViewById(R.id.text2);
        userId = (EditText) findViewById(R.id.userId);
        userPw = (EditText) findViewById(R.id.userPw);
        context = this;
    }


    public void mOnClick(View v) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.0.2:5000";
        JSONObject data = new JSONObject();

        try {
            data.put("user_id", userId.getText());
            data.put("user_pw", userPw.getText());
            //mTextView2.setText("great!!!!!!!");
        } catch (JSONException e) {
            //mTextView2.setText(e.toString());
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response)  {

                        String str_response = response.toString();
                        Intent intent = new Intent(context, SubActivity.class);
                        intent.putExtra("response", str_response);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //mTextView.setText(error.toString());

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);



    }
}