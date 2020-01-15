package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class PhotoFragment extends Fragment {
    private static final String URI_ARG = "url_arg";
    private Uri mUri;
    private WebView mWebView;

    public static PhotoFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(URI_ARG, uri);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(URI_ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        mWebView.loadUrl(mUri.toString());

        return view;
    }
}
