package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.common.CustomItemDecorator;
import com.example.myapplication.model.web.DataReciver;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.example.myapplication.AbstactFragmentActivity.invoke;
import static com.example.myapplication.AlbumDetailActivity.EXTRA_ALBUM_ID;

public class AlbumListFragment extends Fragment {
	private static final String TAG = "AlbumListFragment";
	private RecyclerView mRecyclerView;
	private List<LinkedTreeMap> mAlbums = new ArrayList<>();
	private TextView mEmptyText;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true); // для уничтожения таска
        if (isNotOnline()) {
            Toast.makeText(getActivity(), "Нет соединения с интернетом.", Toast.LENGTH_LONG).show();
        } else {
            new DataReciverTask().execute();
        }
	}



	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.albums_fragment, container, false);
		mRecyclerView = view.findViewById(R.id.root_recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyText = view.findViewById(android.R.id.empty);

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
			if (null != mAlbums) {
                setupAdapter();
            }
		}
	}

	private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

		private List<LinkedTreeMap> mAlbums;

		public AlbumAdapter(List<LinkedTreeMap> albums) {
			this.mAlbums = albums;
            displayView();
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

    private boolean isNotOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() == null;
    }

    private void displayView() {
        if (null != mRecyclerView && null != mEmptyText) {
            if (mAlbums.size() != 0) { // вывод сообщения, что список пуст. можно было бы создать свой кастомный RecyclerView, в котором сделать метод setEmptyView
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyText.setVisibility(View.INVISIBLE);
            } else {
                mEmptyText.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
