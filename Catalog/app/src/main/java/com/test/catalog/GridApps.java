package com.test.catalog;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

public class GridApps extends Fragment {

    ListView listView;
    mAdapter adapter;
    GridView gridView;
    mAdapter2 adapter2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.apps_list, container, false);
        String extra = getActivity().getIntent().getExtras().getString("cat");

        JSONArray second = new JSONArray();
        try {
            JSONArray array = new JSONArray(Helper.getJsonString(getActivity()));
            for (int a = 0; a < array.length(); a++) {
                String term = array.getJSONObject(a).getJSONObject("category").getJSONObject("attributes").getString("term");
                if (term.equals(extra)) {
                    second.put(array.getJSONObject(a));
                }
            }


            if (Helper.isTablet(getActivity())) {
                gridView = (GridView) v.findViewById(R.id.appsGrid);
                adapter2 = new mAdapter2(second, (AppCompatActivity) getActivity());
                gridView.setAdapter(adapter2);
            } else {
                listView = (ListView) v.findViewById(R.id.appsList);
                adapter = new mAdapter(second, (AppCompatActivity) getActivity());
                listView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return v;
    }

    class mAdapter extends BaseAdapter {

        private JSONArray array;
        private AppCompatActivity context;

        public mAdapter(JSONArray array, AppCompatActivity context) {
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
                array.get(i);
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v = context.getLayoutInflater().inflate(R.layout.apps_list_item, viewGroup, false);
            TextView title = (TextView) v.findViewById(R.id.appTitle);
            ImageView imageView = (ImageView) v.findViewById(R.id.appImg);
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.app_list_item_layout);
            try {
                String name = array.getJSONObject(i).getJSONObject("im:name").getString("label");
                String urlImg = array.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label").replace("\\", "");
                Glide.with(context).load(urlImg)
                        .centerCrop().placeholder(android.R.mipmap.sym_def_app_icon)
                        .crossFade().into(imageView);
                title.setText(name);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new Details();
                        Bundle args = new Bundle();
                        try {
                            args.putString("json", array.getJSONObject(i).toString());
                            fragment.setArguments(args);
                            context.getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.in_left, R.anim.out_left)
                                    .replace(R.id.container, fragment).commitAllowingStateLoss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return v;
        }
    }

    class mAdapter2 extends BaseAdapter {

        private JSONArray array;
        private AppCompatActivity context;

        public mAdapter2(JSONArray array, AppCompatActivity context) {
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
                array.get(i);
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v = context.getLayoutInflater().inflate(R.layout.apps_grid_item, viewGroup, false);
            TextView title = (TextView) v.findViewById(R.id.appTitle);
            ImageView imageView = (ImageView) v.findViewById(R.id.appImg);
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.app_grid_item_layout);
            try {
                String name = array.getJSONObject(i).getJSONObject("im:name").getString("label");
                String urlImg = array.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label").replace("\\", "");
                Glide.with(context).load(urlImg)
                        .centerCrop().placeholder(android.R.mipmap.sym_def_app_icon)
                        .crossFade().into(imageView);
                title.setText(name);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new Details();
                        Bundle args = new Bundle();
                        try {
                            args.putString("json", array.getJSONObject(i).toString());
                            fragment.setArguments(args);
                            context.getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.in_left, R.anim.out_left)
                                    .replace(R.id.container, fragment).commitAllowingStateLoss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return v;
        }
    }
}
