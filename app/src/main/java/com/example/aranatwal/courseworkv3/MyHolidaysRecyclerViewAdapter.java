package com.example.aranatwal.courseworkv3;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aranatwal.courseworkv3.MyHolidaysFragment.OnListFragmentInteractionListener;
import com.example.aranatwal.courseworkv3.model.Holiday;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Holiday} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHolidaysRecyclerViewAdapter extends RecyclerView.Adapter<MyHolidaysRecyclerViewAdapter.ViewHolder> {

    private final List<Holiday> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyHolidaysRecyclerViewAdapter(List<Holiday> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_myholidays, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getTitle());

        //check if an image has been uploaded to the holiday and uses it as the thumbnail image otherwise has a deafult
        if (holder.mItem.getPhotos().getPhotoStrings().size()>0) {
            holder.mImageContent.setImageBitmap(BitmapFactory.decodeFile(holder.mItem.getPhotos().getPhotoStrings().get(0)));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Holiday item = holder.mItem;
                    mListener.onListFragmentInteraction(item);
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
        public Holiday mItem;

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
