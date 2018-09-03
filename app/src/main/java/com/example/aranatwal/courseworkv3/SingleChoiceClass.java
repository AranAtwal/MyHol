package com.example.aranatwal.courseworkv3;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.model.PlaceVisitedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AranAtwal on 04/04/2018.
 */

public class SingleChoiceClass extends DialogFragment {

    HolidayData holidayData = HolidayData.getInstance();
    private List<Holiday> items;
    private List<String> itemTitles;
    Holiday selection;

    PlaceVisitedData placeVisitedData = PlaceVisitedData.getInstance();
    boolean added = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final int pVID = getArguments().getInt("PVID");

        items = holidayData.getHolidays();
        itemTitles = new ArrayList<>();

        for (Holiday item:
             items) {
            itemTitles.add(item.getTitle());
        }

        final CharSequence[] charSequenceItems = itemTitles.toArray(new CharSequence[itemTitles.size()]);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add to Holiday").setSingleChoiceItems(charSequenceItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                selection = items.get(i);
                if (!selection.getPlaceVisiteds().contains(placeVisitedData.getPlaceVisited(pVID))) {
                    selection.addPlaceVisited(placeVisitedData.getPlaceVisited(pVID));
                    added = true;
                }

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(added) {
                    Toast.makeText(getContext(), "Place Visited added to " + selection.getTitle(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Place Visited already added", Toast.LENGTH_LONG).show();

                }
            }
        });
        return builder.create();
    }




}
