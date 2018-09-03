package com.example.aranatwal.courseworkv3;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    Context context;
    private final ArrayList<String> imagePaths;
    View view;
    LayoutInflater layoutInflater;

    public ImageAdapter(Context context, ArrayList<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;

    }

    @Override
    public int getCount() {
        if (imagePaths!= null && imagePaths.size()>0)  {
            return imagePaths.size();

        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return imagePaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            //sets image to pathname image from list
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(position)));

        }
        return view;

    }
}