package com.application.nagercoilbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomWindow  implements GoogleMap.InfoWindowAdapter {
    final  private View window;
    final Context context;


    public CustomWindow(Context context) {
        window = LayoutInflater.from(context).inflate(R.layout.custonwindow,null);
        this.context = context;
    }
private void renderWindow(Marker marker,View view){
String title=marker.getTitle().toUpperCase();
    TextView textView=view.findViewById(R.id.title);
    if(!title.equals("")){
        textView.setText(title);
    }

    String snippet =marker.getSnippet();
    TextView textView1=view.findViewById(R.id.snippet);

    if(!snippet.equals("")){
        textView1.setText(snippet);
    }
    ImageView image=view.findViewById(R.id.imageView2);
    Picasso.get().load(R.drawable.am).into(image);
    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker,window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker,window);

        return window;
    }
}
