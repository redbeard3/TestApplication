package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.web.DataReciver;
import com.example.myapplication.model.web.ThumbnailDownloader;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.AlbumDetailActivity.EXTRA_ALBUM_ID;

public class AlbumDetailFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<LinkedTreeMap> mUrls = new ArrayList<>();
    private ThumbnailDownloader<DetailHolder> mThumbnailDownloader;
    private static final String TAG = "AlbumDetailFragment";

    public Fragment newInstance() {
        return new AlbumDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getActivity().getIntent().getSerializableExtra(EXTRA_ALBUM_ID).toString();
        setRetainInstance(true);
        new DataReciverTask(id).execute();

        Handler responseHadler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHadler);
        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<DetailHolder>() {
            @Override
            public void onThumbnailDownloaded(DetailHolder target, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                target.bindDetail(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_detail_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.detail_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4)); // todo можно реализовать динамическое кол-во столбцов при помощи ViewTreeObserver.OnGlobalLayoutListener

        setupAdapter();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
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
            Drawable placeholder = getResources().getDrawable(R.drawable.ic_launcher_foreground); // заглушка, если нет данных
            holder.bindDetail(placeholder);
            mThumbnailDownloader.queueThumbnail(holder, (String) mUrls.get(position).get("thumbnailUrl"));
        }

        @Override
        public int getItemCount() {
            return mUrls.size();
        }
    }

    private class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mDetailView;

        public DetailHolder(@NonNull View itemView) {
            super(itemView);
            mDetailView = itemView.findViewById(R.id.tumblUrl_imageView);
            itemView.setOnClickListener(this);
        }

        public void bindDetail(Drawable drawable) {
            mDetailView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
