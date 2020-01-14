package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.web.DataReciver;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.AlbumDetailActivity.EXTRA_ALBUM_ID;

public class AlbumDetailFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<LinkedTreeMap> mUrls = new ArrayList<>();

    public Fragment newInstance() {
        return new AlbumDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getActivity().getIntent().getSerializableExtra(EXTRA_ALBUM_ID).toString();
        setRetainInstance(true);
        new DataReciverTask(id).execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_detail_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.detail_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3)); // todo можно реализовать динамическое кол-во столбцов при помощи ViewTreeObserver.OnGlobalLayoutListener

        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new DetailAdapter(mUrls));
        }
    }

    private class DataReciverTask extends AsyncTask<Void, Void, List<LinkedTreeMap>> {
        private String id;

        public DataReciverTask(String id) {
            this.id = id;
        }

        @Override
        protected List<LinkedTreeMap> doInBackground(Void... voids) {
            return new DataReciver().getUrls(id);
        }

        @Override
        protected void onPostExecute(List<LinkedTreeMap> urls) {
            mUrls = urls;
            setupAdapter();
        }
    }

    private class DetailAdapter extends RecyclerView.Adapter<DetailHolder> {

        private List<LinkedTreeMap> mUrls;

        public DetailAdapter(List<LinkedTreeMap> mUrls) {
            this.mUrls = mUrls;
        }

        @NonNull
        @Override
        public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.album_detail_item, parent, false);

            return new DetailHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailHolder holder, int position) {
            holder.bindDetail(mUrls.get(position));

        }

        @Override
        public int getItemCount() {
            return mUrls.size();
        }
    }

    private class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinkedTreeMap mDetail;
        private TextView mDetailTextView;

        public DetailHolder(@NonNull View itemView) {
            super(itemView);
            mDetailTextView = itemView.findViewById(R.id.tumblUrl_text_view);
            itemView.setOnClickListener(this);
        }

        public void bindDetail(LinkedTreeMap url) {
            this.mDetail = url;
            mDetailTextView.setText((String) mDetail.get("thumbnailUrl"));
        }

        @Override
        public void onClick(View view) {

        }
    }
}
