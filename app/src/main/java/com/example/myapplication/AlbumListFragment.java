package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.web.DataReciver;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myapplication.AbstactFragmentActivity.invoke;
import static com.example.myapplication.AlbumDetailActivity.EXTRA_ALBUM_ID;

public class AlbumListFragment extends Fragment {
	private static final String TAG = "AlbumListFragment";
	private RecyclerView mRecyclerView;
	private List<LinkedTreeMap> mAlbums = new ArrayList<>();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true); // для уничтожения таска
		new DataReciverTask().execute();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.albums_fragment, container, false);
		mRecyclerView = view.findViewById(R.id.root_recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		setupAdapter();

		return view;
	}

	public static Fragment newInstance() {
		return new AlbumListFragment();
	}

	private void setupAdapter() {
		if (isAdded()) {
			mRecyclerView.setAdapter(new AlbumAdapter(mAlbums));
		}
	}

	private class DataReciverTask extends AsyncTask<Void, Void, List<LinkedTreeMap>> {
		@Override
		protected List<LinkedTreeMap> doInBackground(Void... voids) {
			return new DataReciver().getAlbumItems();
		}

		@Override
		protected void onPostExecute(List<LinkedTreeMap> albums) {
			mAlbums = albums;
			setupAdapter();
		}
	}

	private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

		private List<LinkedTreeMap> mAlbums;

		public AlbumAdapter(List<LinkedTreeMap> albums) {
			this.mAlbums = albums;
		}

		@NonNull
		@Override
		public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
			View view = layoutInflater.inflate(R.layout.albums_list_item, parent, false);

			return new AlbumHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
			holder.bindAlbum(mAlbums.get(position));
		}

		@Override
		public int getItemCount() {
			return mAlbums.size();
		}
	}

	private class AlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private TextView mAlbumNameTextView;
		private LinkedTreeMap mAlbum;

		public AlbumHolder(@NonNull View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			mAlbumNameTextView = itemView.findViewById(R.id.album_name);
		}

		@Override
		public void onClick(View view) {
			Map<String, Serializable> params = new HashMap<>();
			params.put(EXTRA_ALBUM_ID, (Serializable) mAlbum.get("id"));
			Intent intent = invoke(getActivity(), AlbumDetailActivity.class, params);
			startActivity(intent);
		}

		public void bindAlbum(LinkedTreeMap album) {
			this.mAlbum = album;
			mAlbumNameTextView.setText((String) mAlbum.get("title"));
		}
	}
}
