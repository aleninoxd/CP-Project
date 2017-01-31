package com.test.catalog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading content");
        if (!Helper.getJsonString(Categories.this).isEmpty()) {
            try {
               checkArray(new JSONArray(Helper.getJsonString(Categories.this)));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("err",e.toString());
            }

        } else {
            dialog.show();
            Volley.newRequestQueue(this, new HurlStack(null, sslSocket())).add(
                    new JsonObjectRequest(Request.Method.GET, Helper.uri, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            try {
                                JSONArray arr = response.getJSONObject("feed").getJSONArray("entry");
                                Helper.setJsonString(Categories.this,arr.toString());
                               checkArray(arr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Log.e("VolleyError", error.toString());
                            Toast.makeText(Categories.this, "Failed to load content, please try again later.", Toast.LENGTH_LONG).show();
                            Categories.this.finish();
                        }
                    })
            );
        }

    }

    private void checkArray(JSONArray jsonArray) throws JSONException {
//        Toast.makeText(Categories.this, "start with "+jsonArray.length(), Toast.LENGTH_SHORT).show();
        JSONArray arrayClean = new JSONArray();
        ArrayList<String> strings = new ArrayList<>();
        JSONObject obj = new JSONObject();
        obj.put("e","empt");
        for (int a = 0;a<jsonArray.length();a++){
            String comp = jsonArray.getJSONObject(a).getJSONObject("category").getJSONObject("attributes").getString("term");
            for (int b = 0 ;b< strings.size();b++){
                if (comp.equals(strings.get(b))){
                    jsonArray.put(a,obj);
                }
            }
            strings.add(comp);
        }
        for (int a=0;a<jsonArray.length();a++){
            if (!jsonArray.getJSONObject(a).equals(obj)){
                arrayClean.put(jsonArray.getJSONObject(a));
            }
        }
//        Toast.makeText(Categories.this, "ends with "+arrayClean.length(), Toast.LENGTH_SHORT).show();
        executeContent(arrayClean);
    }

    private void executeContent(JSONArray array) {

        if (Helper.isTablet(Categories.this)){
            GridView grid = (GridView) findViewById(R.id.categories_);
            mAadapter2 adapter2 = new mAadapter2(Categories.this,array);
            grid.setAdapter(adapter2);
        } else {
            ListView list = (ListView) findViewById(R.id.categories);
            mAadapter adapter = new mAadapter(Categories.this,array);
            list.setAdapter(adapter);
        }
    }

    private static SSLSocketFactory sslSocket() {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        SSLContext sslContext = null;
        SSLSocketFactory sslSocketFactory = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("Categories", e.toString());
        } catch (KeyManagementException e) {
            Log.e("Categories", e.toString());
        }

        return sslSocketFactory;
    }

    class mAadapter extends BaseAdapter {

        JSONArray array;
        Activity context;

        public mAadapter(Activity context, JSONArray array) {
            this.array = array;
            this.context = context;
        }

        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int i) {
            try {
                return array.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = context.getLayoutInflater().inflate(R.layout.categ_item, viewGroup, false);
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.clickLay);
            try {
                JSONObject obj = array.getJSONObject(i).getJSONObject("category").getJSONObject("attributes");
                final TextView tv = (TextView) v.findViewById(R.id.categoryTitle);
                tv.setText(obj.getString("term"));
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, Apps.class).putExtra("cat",tv.getText().toString()));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }

    class mAadapter2 extends BaseAdapter {

        JSONArray array;
        Activity context;

        public mAadapter2(Activity context, JSONArray array) {
            this.array = array;
            this.context = context;
        }

        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int i) {
            try {
                return array.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = context.getLayoutInflater().inflate(R.layout.categ_grid_item, viewGroup, false);
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.clickLay);
            try {
                JSONObject obj = array.getJSONObject(i).getJSONObject("category").getJSONObject("attributes");
                final TextView tv = (TextView) v.findViewById(R.id.categoryTitle);
                tv.setText(obj.getString("term"));
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, Apps.class).putExtra("cat",tv.getText().toString()));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }
}
