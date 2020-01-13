package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.myapplication.model.pojo.Album;
import com.example.myapplication.model.web.DataReciver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    List<Album> mAlbums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.root_recycler_view);
        new DataReciverTask().execute();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupAdapter() {
        mRecyclerView.setAdapter(new AlbumAdapter(mAlbums, getApplicationContext()));
    }

    private class DataReciverTask extends AsyncTask<Void, Void, List<Album>> {
        @Override
        protected List<Album> doInBackground(Void... voids) {
            return new DataReciver().getAlbumItems();
        }

        @Override
        protected void onPostExecute(List<Album> albums) {
            mAlbums = albums;
            setupAdapter();
        }
    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        private List<Album> albums;
        private Context context;

        public AlbumAdapter(List<Album> albums, Context context) {
            this.albums = albums;
            this.context = context;
        }

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.albums_list_item, parent, false);
            return new AlbumHolder(view, context);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
            Album album = albums.get(position);
            holder.bindAlbum(album);
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }
    }

    private class AlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private Album album;
        private TextView albumName;

        public AlbumHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            albumName = itemView.findViewById(R.id.album_name);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindAlbum(Album album) {
            this.album = album;
            albumName.setText(album.getTitle());
        }
    }
}
