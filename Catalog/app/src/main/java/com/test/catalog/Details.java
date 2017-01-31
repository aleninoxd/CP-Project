package com.test.catalog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class Details extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details, container, false);
        TextView name = (TextView) v.findViewById(R.id.appname),
                summary = (TextView) v.findViewById(R.id.appDescription),
                developer = (TextView) v.findViewById(R.id.rights);
        ImageView img = (ImageView) v.findViewById(R.id.appimage);
        String json = getArguments().getString("json");
        try {
            JSONObject object = new JSONObject(json);
            name.setText(object.getJSONObject("im:name").getString("label"));
            summary.setText(object.getJSONObject("summary").getString("label"));
            developer.setText(object.getJSONObject("rights").getString("label"));
            Glide.with(getActivity())
                    .load(object.getJSONArray("im:image").getJSONObject(2)
                            .getString("label")
                            .replace("\\", ""))
                    .centerCrop()
                    .placeholder(android.R.mipmap.sym_def_app_icon)
                    .crossFade()
                    .into(img);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
