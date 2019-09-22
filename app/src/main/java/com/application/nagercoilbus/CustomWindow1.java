package com.application.nagercoilbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class CustomWindow1  implements GoogleMap.InfoWindowAdapter {
    final  private View window;
    final Context context;
    final String uri,name,department;

    public CustomWindow1( Context context, String uri, String name,String department) {

        this.context = context;
        this.uri = uri;
        this.name = name;
        this.department=department;
        window = LayoutInflater.from(context).inflate(R.layout.custonwindow,null);

    }



    private void renderWindow(Marker marker,View view){
        String title=marker.getTitle();
        TextView textView=view.findViewById(R.id.title);
        if(!title.equals("")){
            textView.setText(title);
        }
        else{
            textView.setText(name);
        }
        String snippet =marker.getSnippet();
        TextView textView1=view.findViewById(R.id.snippet);

        if(!snippet.equals("")){
            textView1.setText(snippet);
        }
        else{
            textView1.setText(department);
        }
        if (title.equals("")) {
            ImageView image = view.findViewById(R.id.imageView2);
            Picasso.get().load(uri).into(image);
        }
        else {
            ImageView image = view.findViewById(R.id.imageView2);
            Picasso.get().load(R.drawable.am).into(image);
        }

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
