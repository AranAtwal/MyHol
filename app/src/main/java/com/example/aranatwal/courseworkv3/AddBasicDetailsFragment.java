package com.example.aranatwal.courseworkv3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.model.PlaceVisitedData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddBasicDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddBasicDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBasicDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Button mDateFrom, mDateTo;
    private EditText mTitle, mDescription;
    public Holiday holiday;
    public HolidayData holidayData = new HolidayData().getInstance();

    public String editing_title;
    public String editing_description;
    public int editing_id;
    //add dates after
    private String editing_date_from;
    private String editing_date_to;
    public boolean editing = false;

    private String holDescription;
    private String holTitle;
    private String dateFromString;
    private String dateToString;

    private OnFragmentInteractionListener mListener;
    private int int_id;
    private int new_int_id;

    //calender
    int day, month, year;
    private Calendar mCurrentDate;
    private Date dateFrom;
    private Date dateTo;
    private boolean dateFromSet = false;
    private static Menu mOptionsMenu;

    //places visited fragment
    private boolean isPlaceVisited = false;
    public PlaceVisited placeVisited;
    public PlaceVisitedData placeVisitedData = new PlaceVisitedData().getInstance();
    private int new_int_id_pl;
    private boolean editingPL = false;


    public AddBasicDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBasicDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBasicDetailsFragment newInstance(String param1, String param2) {
        AddBasicDetailsFragment fragment = new AddBasicDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
            Bundle bundle = getArguments();

        if (getArguments() != null) {

            //checks for an id
            //if one exists then it is an edit and not a new holiday or place visited
            if (bundle.containsKey("id")) {

                int_id = bundle.getInt("id");
                Holiday editing_hol = holidayData.getHoliday(int_id);

                if (editing_hol != null) {
                    editing_title = editing_hol.getTitle();
                    editing_description = editing_hol.getDescription();
                    editing_id = editing_hol.getId();

                    if (editing_hol.getDateFrom() != null) {
                        editing_date_from = editing_hol.getDateFromString();
                        dateFrom = editing_hol.getDateFrom();
                    }
                    if (editing_hol.getDateTo() != null) {
                        editing_date_to = editing_hol.getDateToString();
                        dateTo = editing_hol.getDateTo();
                    }

                    editing = true;
                }
            }
            //this key chnages a boolean to show that it is a place visited and not a holiday
            else if (bundle.containsKey("isPlaceVisited")) {

                isPlaceVisited = true;

            } else if (bundle.containsKey("placeIDEDIT")) {
            //this is a place visted that is being edited
                new_int_id_pl = bundle.getInt("placeIDEDIT");

                PlaceVisited editing_pl = placeVisitedData.getPlaceVisited(new_int_id_pl);

                if (editing_pl != null) {
                    editing_title = editing_pl.getTitle();
                    editing_description = editing_pl.getDescription();
                    editing_id = editing_pl.getId();

                    if (editing_pl.getDateFrom() != null) {
                        editing_date_from = editing_pl.getDateFromString();
                        dateFrom = editing_pl.getDateFrom();
                    }

                    editingPL = true;
                }


            }


        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_basic_details, menu);
        mOptionsMenu = menu;

        if(mTitle.getText().toString().length() > 0) {
            mOptionsMenu.findItem(R.id.next_to_location).setVisible(true);
        }
        else {
            mOptionsMenu.findItem(R.id.next_to_location).setVisible(false);
        }

        super.onCreateOptionsMenu(mOptionsMenu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.next_to_location:
                nextToLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_basic_details, container, false);

        mTitle = (EditText) v.findViewById(R.id.holiday_name);
        mDescription = (EditText) v.findViewById(R.id.holiday_description);
        mDateFrom = (Button) v.findViewById(R.id.date_from);
        mDateTo = (Button) v.findViewById(R.id.date_to);

        mTitle.addTextChangedListener(titleWatcher);
        mDescription.addTextChangedListener(descriptionWatcher);

        if(editing) {
        holiday = new Holiday();


            if (mTitle != null) {
                mTitle.setText(editing_title);
            }
            if (mDescription != null) {
                mDescription.setText(editing_description);
            }
            if (editing_date_from != null)  {
                mDateFrom.setText(editing_date_from);
                mDateTo.setVisibility(View.VISIBLE);
            }
            if (editing_date_to != null)  {
                mDateTo.setText(editing_date_to);
                mDateTo.setVisibility(View.VISIBLE);
            }
        } else if (editingPL) {

            PlaceVisited placeVisited = new PlaceVisited();

            if (mTitle != null) {
                mTitle.setText(editing_title);
            }
            if (mDescription != null) {
                mDescription.setText(editing_description);
            }
            if (editing_date_from != null)  {
                mDateFrom.setText(editing_date_from);
            }

        }

        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        mDateFrom.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;

                                dateFromString = dayOfMonth +"/"+monthOfYear+"/"+year;

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date date = format.parse(dateFromString);

                                    dateFrom = date;

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                mDateFrom.setText(dateFromString);

                                //sets date to button to appropiate visibililty depending on holiday or place visited and if date from is picked
                                if (dateFrom!=null && isPlaceVisited) {
                                    dateFromSet = true;
                                    mDateTo.setClickable(true);
                                    mDateTo.setVisibility(View.INVISIBLE);

                                } else if (dateFrom!=null && !editingPL) {
                                    dateFromSet = true;
                                    mDateTo.setClickable(true);
                                    mDateTo.setVisibility(View.VISIBLE);
                                }

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });



        mDateTo.setOnClickListener(new View.OnClickListener() {



            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear += 1;

                                dateToString = dayOfMonth +"/"+monthOfYear+"/"+year;

                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date date = format.parse(dateToString);

                                    dateTo = date;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(dateFrom.before(dateTo)) {
                                    mDateTo.setText(dateToString);
                                } else {
                                    dateToString = null;
                                    dateTo = null;
                                    //check to make sure date to is after date from
                                    Toast.makeText(getContext(), "Date to must be after date from", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        return v;
    }

    public void nextToLocation() {

        //closes keyboard off screen
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        //passes different ids depending on what the page is being used for
        if (editing) {
            holidayData.updateHoliday(holTitle, holDescription, dateFrom, dateTo, editing_id);

            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            new_int_id = holidayData.getNextId();
            bundle.putInt("holID", new_int_id);

            LocationFragment locationFragment = new LocationFragment();
            locationFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, locationFragment);

            fragmentTransaction.commit();


        } else  if (isPlaceVisited) {
            PlaceVisited placeVisited1 = new PlaceVisited(holTitle, holDescription);
            placeVisited1.setDateFrom(dateFrom);
            placeVisitedData.createPlaceVisited(placeVisited1);

            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            new_int_id_pl = placeVisitedData.getNextId();
            bundle.putInt("plID", new_int_id_pl);

            LocationFragment locationFragment = new LocationFragment();
            locationFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, locationFragment);

            fragmentTransaction.commit();


        } else if (editingPL) {

            placeVisitedData.updatePlaceVisited(holTitle, holDescription, dateFrom, editing_id);

            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            bundle.putInt("plID", editing_id);

            LocationFragment locationFragment = new LocationFragment();
            locationFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, locationFragment);

            fragmentTransaction.commit();
        }

        else {
            Holiday holiday1 = new Holiday(holTitle, holDescription);
            holiday1.setDateFrom(dateFrom);
            holiday1.setDateTo(dateTo);
            holidayData.createHoliday(holiday1);

            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            new_int_id = holidayData.getNextId();
            bundle.putInt("holID", new_int_id);

            LocationFragment locationFragment = new LocationFragment();
            locationFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, locationFragment);

            fragmentTransaction.commit();

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private final TextWatcher titleWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {

                mOptionsMenu.getItem(0).setVisible(false);

            } else{

            holTitle = mTitle.getText().toString();

//                mOptionsMenu.findItem(R.id.next_to_location).setVisible(true);

                if(!mTitle.getText().toString().isEmpty()) {

                    //sets menu item to visible so that the user can advance only once a title has been entered
                    mOptionsMenu.getItem(0).setVisible(true);
                }
            }
        }
    };



    private final TextWatcher descriptionWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
                holDescription = mDescription.getText().toString();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //different title on same page depending on use
        if (editing) {
            ((MainActivity) getActivity()).setActionBarTitle("Edit Holiday Details");
        } else if (isPlaceVisited) {
            ((MainActivity) getActivity()).setActionBarTitle("Add Place Visited Details");
        } else {
            ((MainActivity) getActivity()).setActionBarTitle("Add Holiday Details");
        }
    }

}
