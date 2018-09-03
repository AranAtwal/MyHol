package com.example.aranatwal.courseworkv3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.MyPlace;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.model.PlaceVisitedData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
//    public static final int IMAGE_GALLERY_REQUEST = 88;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int GET_FROM_GALLERY = 888;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    public HolidayData holidayData = new HolidayData().getInstance();
    private int int_id;
    private Holiday existing_hol;
    private boolean isHoliday = false;
    private Menu mOptionsMenu;

    public PlaceVisitedData placeVisitedData = new PlaceVisitedData().getInstance();
    private PlaceVisited existing_place;
    private boolean isPlaceVisited = false;
    private int int_id_pl;


    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        //finds the context in which the page is being use
        if (getArguments() != null) {

            if (bundle.containsKey("holID")) {

                isHoliday = true;

                int_id = bundle.getInt("holID");

                existing_hol = holidayData.getHoliday(int_id);

                autocompleteFragment = new SupportPlaceAutocompleteFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.search_container, autocompleteFragment);
                fragmentTransaction.commit();

                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        MyPlace myplace = new MyPlace(place.getLatLng(), place.getAddress().toString());
                        existing_hol.addPlace(myplace);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.d("CS3040", "An error occurred: " + status);
                    }
                });

            } else if (bundle.containsKey("plID")) {

                isPlaceVisited = true;

                int_id_pl = bundle.getInt("plID");
                existing_place = placeVisitedData.getPlaceVisited(int_id_pl);

                autocompleteFragment = new SupportPlaceAutocompleteFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.search_container, autocompleteFragment);
                fragmentTransaction.commit();

                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        MyPlace myplace = new MyPlace(place.getLatLng(), place.getAddress().toString());
                        existing_place.addPlace(myplace);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.d("CS3040", "An error occurred: " + status);
                    }
                });
            }

        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_location, menu);
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.next_to_gallery:
                nextToGallery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        return view;
    }

    public void nextToGallery() {
        //move to next page and pass appropriate data
        if (isHoliday) {
            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            bundle.putInt("holID", int_id);

            PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
            photoGalleryFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, photoGalleryFragment);
            fragmentTransaction.commit();
        } else if (isPlaceVisited) {
            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            bundle.putInt("plID", int_id_pl);

            PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
            photoGalleryFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, photoGalleryFragment);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onResume() {
        super.onResume();
        Log.i("CS3040", "On Resume");
        ((MainActivity) getActivity()).setActionBarTitle("Add Location");

    }

}
