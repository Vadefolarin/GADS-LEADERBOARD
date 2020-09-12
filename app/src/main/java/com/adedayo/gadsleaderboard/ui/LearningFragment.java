package com.adedayo.gadsleaderboard.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adedayo.gadsleaderboard.R;
import com.adedayo.gadsleaderboard.model.Skills;
import com.adedayo.gadsleaderboard.util.ApiUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;



public class LearningFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ProgressBar mProgressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomAdapter adapter;
    private final Handler handler = new Handler();

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mProgressBar = view.findViewById(R.id.pb_loading);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view);


       loadData();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadData();
//                Toast.makeText(getActivity(), "refreshing..", Toast.LENGTH_SHORT).show();
            handler.postDelayed(() -> {
                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        });

        return view;
    }

    private void loadData() {
        try {
            URL url = ApiUtil.buildLearningUrl();
            new SkillQueryTask().execute(url);
        }catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }


    public class SkillQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(url);
            }
            catch(IOException e){
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null){
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_LONG).show();
            }
            ArrayList<Skills> skillsArr = ApiUtil.parseJson(result);
            adapter = new CustomAdapter(skillsArr);
            recyclerView.setAdapter(adapter);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder>{

        ArrayList<Skills> mSkill;

        public CustomAdapter(ArrayList<Skills> skills) {
            this.mSkill = skills;
        }

        @Override
        public  viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_list, viewGroup, false);
            return new viewHolder(view);
        }
        @Override
        public  void onBindViewHolder(viewHolder viewHolder, int position) {
            Skills skills = mSkill.get(position);
            viewHolder.bind(skills);
        }

        @Override
        public int getItemCount() {
            return mSkill.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView image;
            TextView summary;

            public viewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                image = itemView.findViewById(R.id.image);
                summary = itemView.findViewById(R.id.summary);
            }

            public void bind(Skills skills){
                name.setText(skills.name);
                String buildSummary = skills.hours + " learning hours, " + skills.country;
                summary.setText(buildSummary);
                if(!skills.badgeUrl.equals("")){
                    Picasso.get().load(skills.badgeUrl)
                            .into(image);
                }
            }


        }
    }
}
