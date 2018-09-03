package com.example.aranatwal.courseworkv3;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.VisitFragment.OnListFragmentInteractionListener;
import java.util.List;

public class MyVisitRecyclerViewAdapter extends RecyclerView.Adapter<MyVisitRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceVisited> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyVisitRecyclerViewAdapter(List<PlaceVisited> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_visit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getTitle());

        //checks for images to use as thumbnail
        if (holder.mItem.getPhotos().getPhotoStrings().size()>0) {
            holder.mImageContent.setImageBitmap(BitmapFactory.decodeFile(holder.mItem.getPhotos().getPhotoStrings().get(0)));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageView mImageContent;
        public PlaceVisited mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageContent = (ImageView) view.findViewById(R.id.image_content);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
