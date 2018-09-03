package com.example.aranatwal.courseworkv3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.MyPlace;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.model.PlaceVisitedData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    HolidayData holidayData = new HolidayData().getInstance();
    private MyHolidaysFragment.OnListFragmentInteractionListener mListener;

    PlaceVisitedData placeVisitedData = new PlaceVisitedData().getInstance();
    private VisitFragment.OnListFragmentInteractionListener mListenerPlace;

    TextView mNum_hols, mLatest_hol, mNum_places, mLatest_place;
    LinearLayout mLatest_hol_layout, mLatest_place_layout;

    FloatingActionButton fab0, fab1, fab2, fab3;
    Animation fabOpen, fabClose, fabRClockwise, fabRAnticlockwise;
    boolean isOpen = false;
    TextView mFab_add_holiday, mFab_add_place_visited, mFab_add_photo;

    static HashMap<Marker, Holiday> markers = new HashMap<Marker, Holiday>();
    static HashMap<Marker, PlaceVisited> markersPlace = new HashMap<Marker, PlaceVisited>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);

        //stats
        mNum_hols = (TextView) view.findViewById(R.id.num_hols);
        mLatest_hol = (TextView) view.findViewById(R.id.latest_hol);
        mLatest_hol_layout = (LinearLayout) view.findViewById(R.id.last_hol_layout);

        mNum_places = (TextView) view.findViewById(R.id.num_places);
        mLatest_place = (TextView) view.findViewById(R.id.latest_place);
        mLatest_place_layout = (LinearLayout) view.findViewById(R.id.last_place_layout);


        mNum_hols.setText(holidayData.getStringSize());
        if(holidayData.getSize()>0) {
            mLatest_hol_layout.setVisibility(View.VISIBLE);
            mLatest_hol.setText(holidayData.getHoliday(holidayData.getNextId()).getTitle());

        }

        mNum_places.setText(placeVisitedData.getStringSize());
        if(placeVisitedData.getSize()>0) {
            mLatest_place_layout.setVisibility(View.VISIBLE);
            mLatest_place.setText(placeVisitedData.getPlaceVisited(placeVisitedData.getNextId()).getTitle());
        }

        //fabs
        fab0 = (FloatingActionButton) view.findViewById(R.id.home_fab_0);
        fab1 = (FloatingActionButton) view.findViewById(R.id.home_fab_1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.home_fab_2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.home_fab_3);

        //fab text views
        mFab_add_holiday = (TextView) view.findViewById(R.id.fab_add_holiday);
        mFab_add_place_visited = (TextView) view.findViewById(R.id.fab_add_place_visitied);
        mFab_add_photo = (TextView) view.findViewById(R.id.fab_add_photo);

        //animations
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabRClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        fabRAnticlockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anticlockwise);

        fab0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){

                    fab0.startAnimation(fabRAnticlockwise);
                    fab3.setClickable(false);
                    fab2.setClickable(false);
                    fab1.setClickable(false);
                    isOpen = false;

                    fab3.startAnimation(fabClose);
                    fab2.startAnimation(fabClose);
                    fab1.startAnimation(fabClose);

                    mFab_add_holiday.startAnimation(fabClose);
                    mFab_add_place_visited.startAnimation(fabClose);
                    mFab_add_photo.startAnimation(fabClose);


                }
                else {

                    fab0.startAnimation(fabRClockwise);
                    fab3.setClickable(true);
                    fab2.setClickable(true);
                    fab1.setClickable(true);
                    isOpen = true;

                    fab3.startAnimation(fabOpen);
                    fab2.startAnimation(fabOpen);
                    fab1.startAnimation(fabOpen);

                    mFab_add_holiday.startAnimation(fabOpen);
                    mFab_add_place_visited.startAnimation(fabOpen);
                    mFab_add_photo.startAnimation(fabOpen);



                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                AddBasicDetailsFragment addBasicDetailsFragment = new AddBasicDetailsFragment();

                fragmentTransaction.replace(R.id.home, addBasicDetailsFragment);
                fragmentTransaction.addToBackStack("back");
                fragmentTransaction.commit();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isPlaceVisited", true);

                AddBasicDetailsFragment addBasicDetailsFragment = new AddBasicDetailsFragment();
                addBasicDetailsFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.home, addBasicDetailsFragment);
                fragmentTransaction.addToBackStack("back");
                fragmentTransaction.commit();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putBoolean("TravelGallery", true);

                PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
                photoGalleryFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.home, photoGalleryFragment);
                fragmentTransaction.addToBackStack("back");
                fragmentTransaction.commit();
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (MyHolidaysFragment.OnListFragmentInteractionListener) context;
            mListenerPlace = (VisitFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.menu_home));

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onListFragmentInteraction(Holiday holiday);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //sets up the map

        final List<PlaceVisited> places = placeVisitedData.getPlacesVisited();

        //loops the places visited for locations and put them on the map
        //colours changed to blue to make them different
        for (PlaceVisited placeVisited:
                places) {
            MyPlace place = placeVisited.getPlace();

            if(place != null) {
                Marker m = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(placeVisited.getTitle()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                markersPlace.put(m, placeVisited);
            }
        }

        //increased zIndex from default of 0 to 1 so that the holiday markers appears on top for times when markers are close on map, holidyas should have priority
        final List<Holiday> hols = holidayData.getHolidays();

        for (Holiday hol:
                hols) {
            MyPlace place = hol.getPlace();

            if(place != null) {
                Marker m = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(hol.getTitle()).zIndex(1.0f));
                markers.put(m, hol);

        }

        //sets on click to take the user to the details page for that marker

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (markers.containsKey(marker)) {
                    Holiday mrkrHol = markers.get(marker);
                    mListener.onListFragmentInteraction(mrkrHol);
                } else if (markersPlace.containsKey(marker)) {
                    PlaceVisited mrkrPl = markersPlace.get(marker);
                    mListenerPlace.onListFragmentInteraction(mrkrPl);
                }

                return true;
            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 77);
            return;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
            mMap.setMyLocationEnabled(true);
        }

    }
}
