package com.bipo.javier.bipo.InsecureArea;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bipo.javier.bipo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsecureAreaFragment extends Fragment {

    private WebView wvInsecureArea;
    private static final String BIPO_URL = "http://www.bipoapp.com/page/zones";

    public InsecureAreaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insecure_area, container, false);
        wvInsecureArea = (WebView)view.findViewById(R.id.WvInsecureArea);
        wvInsecureArea.getSettings().setJavaScriptEnabled(true);
        wvInsecureArea.setWebViewClient(new WebViewClient());
        wvInsecureArea.loadUrl(BIPO_URL);
        return view;
    }



}
