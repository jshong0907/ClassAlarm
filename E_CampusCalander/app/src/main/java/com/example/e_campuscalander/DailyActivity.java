package com.example.e_campuscalander;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DailyActivity extends AppCompatActivity {
    private Context context;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_sub);
        mTextView = findViewById(R.id.textView2);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        String user_pw = intent.getStringExtra("user_pw");
        context = this;

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.0.2:5000";
        JSONObject data = new JSONObject();

        try {
            data.put("user_id", user_id);
            data.put("user_pw", user_pw);
            mTextView.setText("great!!!!!!!");
        } catch (JSONException e) {
            mTextView.setText(e.toString());
        }
        mTextView.append(user_id);
        mTextView.append(user_pw);


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
