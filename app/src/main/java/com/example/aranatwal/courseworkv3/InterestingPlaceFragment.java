package com.example.aranatwal.courseworkv3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InterestingPlaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InterestingPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterestingPlaceFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Place placePicked;
    boolean isPicked = false;
    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private LatLng userLatLng;
    private GoogleApiClient mGoogleApiClient;
    private static String filter = "";
    private static int filterNum = -1;
    private Menu mOptionsMenu;
    private FloatingActionButton fab0, fab1, fab2;
    Animation fabOpen, fabClose, fabRClockwise, fabRAnticlockwise;
    private boolean isOpen = false;
    private TextView mfab_search_nearby, mfab_filtered_search;

    public InterestingPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterestingPlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterestingPlaceFragment newInstance(String param1, String param2) {
        InterestingPlaceFragment fragment = new InterestingPlaceFragment();
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //for when a place has been picked in the search nearby, routes them to the google maps page
            if (getArguments().containsKey("interestingPlace")) {
                placePicked = (Place) getArguments().getParcelable("interestingPlace");
                isPicked = true;
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_interesting_places, menu);
        mOptionsMenu = menu;
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_search:
                setFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interesting_place, container, false);

        //fabs
        fab0 = (FloatingActionButton) view.findViewById(R.id.interest_fab_0);
        fab1 = (FloatingActionButton) view.findViewById(R.id.interest_fab_1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.interest_fab_2);

        //fab text views
        mfab_search_nearby = (TextView) view.findViewById(R.id.fab_search_nearby);
        mfab_filtered_search = (TextView) view.findViewById(R.id.fab_filtered_search);

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
                    fab2.setClickable(false);
                    fab1.setClickable(false);
                    isOpen = false;

                    fab2.startAnimation(fabClose);
                    fab1.startAnimation(fabClose);

                    mfab_search_nearby.startAnimation(fabClose);
                    mfab_filtered_search.startAnimation(fabClose);


                }
                else {

                    fab0.startAnimation(fabRClockwise);
                    fab2.setClickable(true);
                    fab1.setClickable(true);
                    isOpen = true;

                    fab2.startAnimation(fabOpen);
                    fab1.startAnimation(fabOpen);

                    mfab_search_nearby.startAnimation(fabOpen);
                    mfab_filtered_search.startAnimation(fabOpen);

                }
            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFilteredNearby();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchNearby();
            }
        });


        if (isPicked) {
            Toast.makeText(getContext(), placePicked.getAddress().toString(), Toast.LENGTH_SHORT).show();
            openGoogleMap(placePicked);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_interests);
        mapFragment.getMapAsync(this);

        return view;
    }

    public void searchNearby() {
        int PLACE_PICKER_REQUEST = 101;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            getActivity().startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void setFilter() {
        //opens dialog box
        FilterChoiceClass filterChoiceClass = new FilterChoiceClass();
        Bundle bundle = new Bundle();
        filterChoiceClass.setArguments(bundle);

        filterChoiceClass.show(getChildFragmentManager(), "My_Dialog");

    }

    public void getFilteredNearby() {
        //adds filter string to google maps search
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+filter);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    public void openGoogleMap(Place place) {
        String dest = Uri.encode(place.getAddress().toString());
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + dest);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //refreshes location every 3 seconds
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //sets camera each time the location updates (3 seconds)
        mLastLocation = location;

        userLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());


        if (mLastLocation != null) {

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14));
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Places of Interest");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //get permissions
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static class FilterChoiceClass extends DialogFragment {
        //array of types supported by google maps search, only ones which apply to holidays
        final CharSequence[] filters = {"No Filter", "Amusement Park", "Aquarium", "Art Gallery", "Department Store", "Movie Theater", "Museum", "Night Club", "Restaurant", "Shopping Mall", "Spa", "Supermarket", "Zoo"};

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //single choice filter as google maps doesn't do concatenated searches well
            //resets filter
            //static variables as the the page starts two intents for google maps and the filters would have to be reapplied each time the page is loaded
            filter = "";
            filterNum=0;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Set Filter").setSingleChoiceItems(filters, filterNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(i==0) {
                        filter = "";
                        filterNum = i;
                    } else {
                        filter = filters[i].toString();
                        filterNum = i;
                    }

                }


            }).setPositiveButton("Select Filter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!filter.equals("")) {
                        Toast.makeText(getContext(), "Filter added: " + filter, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "No filter selected", Toast.LENGTH_LONG).show();
                    }
                }
            });

            return builder.create();
        }

    }

}
