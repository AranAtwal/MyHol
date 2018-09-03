package com.example.aranatwal.courseworkv3;

import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.MyPhotos;
import com.example.aranatwal.courseworkv3.model.MyPlace;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.vision.text.Line;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.aranatwal.courseworkv3.R.layout.fragment_visit_list;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HolidayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HolidayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HolidayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static String list_title;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    private String[] myStrings;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Item";
    private static final String ARG_PARAM2 = "Place";

    // TODO: Rename and change types of parameters
    private Holiday item;
    public TextView notesField, locationField, dateField;
    public Holiday holiday;
    private boolean isHoliday = false;
    private HolidayData holidayData = HolidayData.getInstance();

    private PlaceVisited placeItem;
    private boolean isPlace = false;
    private Menu mOptionsMenu;

    private ListView placeVisitedList;
    private TextView placeVisitedHeader;
    private ArrayList<PlaceVisited> visits;


    public HolidayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HolidayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HolidayFragment newInstance(String param1, String param2) {
        HolidayFragment fragment = new HolidayFragment();
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

        //checking what the page will be used for
        if (getArguments() != null) {
            if (getArguments().containsKey("Item")) {
                item = (Holiday) getArguments().getSerializable(ARG_PARAM1);
                isHoliday = true;
            } else if (getArguments().containsKey("Place")) {
                placeItem = (PlaceVisited) getArguments().getSerializable(ARG_PARAM2);
                isPlace = true;
            }
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_holiday, menu);
        mOptionsMenu = menu;
        //allows add to holiday option if holidays exist
        if (isPlace && holidayData.getSize()>0) {
            mOptionsMenu.getItem(2).setVisible(true);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_holiday:
                editHoliday();
                return true;
            case R.id.share_holiday:
                shareItem();
                return true;
            case R.id.add_to_holiday:
                addToHoliday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_holiday, container, false);

        notesField = view.findViewById(R.id.holiday_description_view);
        dateField = view.findViewById(R.id.holiday_date_view);


        locationField = view.findViewById(R.id.holiday_location_view);
        placeVisitedHeader = view.findViewById(R.id.pv_title);
        placeVisitedList = (ListView) view.findViewById(R.id.pv_list_view);

        //sets page according to its use
        if (isHoliday) {

            if (item != null) {

                StringBuilder stringBuilder = new StringBuilder();

                if (item.getDateFrom() != null) {
                    stringBuilder.append(convertDateToString(item.getDateFrom()));
                }
                if (item.getDateTo() != null) {
                    stringBuilder.append(" - ").append(convertDateToString(item.getDateTo()));
                }

                dateField.setText(stringBuilder);
                if (item.getDescription() != null) {
                    notesField.setText(item.getDescription());
                } else {
                    notesField.setVisibility(View.GONE);
                }

                if (item.getPlace() != null) {
                    locationField.setText(item.getPlace().getName());
                } else {
                    locationField.setVisibility(View.GONE);
                }

                SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_holiday);

                if (item.getPlace() != null) {

                    ArrayList<MyPlace> myPlaces = new ArrayList<MyPlace>();

                    if (item.getPlaceVisiteds()!=null) {
                        ArrayList<PlaceVisited> myPlacesVisited = item.getPlaceVisiteds();

                        for (PlaceVisited pl:
                             myPlacesVisited) {

                            if (pl.getPlace()!= null) {
                                myPlaces.add(pl.getPlace());
                            }

                        }
                    }
                    MyPlace myPl = new MyPlace(item.getPlace().getLatLng(), item.getPlace().getName());

                    mapFragment.getMapAsync(new MyMapCallback(myPl, myPlaces));

                } else {
                    mapFragment.getView().setVisibility(View.GONE);
                }


                GridView gridView = (GridView) view.findViewById(R.id.hol_images);

                ArrayList<String> pathArrayLists = item.getPhotos().getPhotoStrings();

                if (item.getPlaceVisiteds() != null) {
                    visits = item.getPlaceVisiteds();

                    for (PlaceVisited visit:
                         visits) {

                        if (visit.getPhotos().getPhotoStrings()!= null) {
                            ArrayList<String> photos = visit.getPhotos().getPhotoStrings();

                            for (String photo:
                                 photos) {
                                if (!pathArrayLists.contains(photo)) {
                                    pathArrayLists.add(photo);
                                }
                            }
                        }
                    }


                }

                ImageAdapter imageAdapter = new ImageAdapter(getContext(), pathArrayLists);

                gridView.setAdapter(imageAdapter);


                if(!item.getPlaceVisiteds().isEmpty()) {
                    placeVisitedHeader.setVisibility(View.VISIBLE);

                    PVList pvList = new PVList();
                    placeVisitedList.setAdapter(pvList);
                }

            }

        } else if (isPlace) {

            if (placeItem != null) {

                StringBuilder stringBuilder = new StringBuilder();

                if (placeItem.getDateFrom() != null) {
                    stringBuilder.append(convertDateToString(placeItem.getDateFrom()));
                }

                dateField.setText(stringBuilder);

                if (placeItem.getDescription() != null) {
                    notesField.setText(placeItem.getDescription());
                } else {
                    notesField.setVisibility(View.GONE);
                }

                if (placeItem.getPlace() != null) {
                    locationField.setText(placeItem.getPlace().getName());
                } else {
                    locationField.setVisibility(View.GONE);
                }

                SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_holiday);

                if (placeItem.getPlace() != null) {
                    mapFragment.getMapAsync(new MyMapCallback(new MyPlace(placeItem.getPlace().getLatLng(), placeItem.getPlace().getName())));
                } else {
                    mapFragment.getView().setVisibility(View.GONE);
                }


                GridView gridView = (GridView) view.findViewById(R.id.hol_images);

                ArrayList<String> pathArrayLists = placeItem.getPhotos().getPhotoStrings();

                ImageAdapter imageAdapter = new ImageAdapter(getContext(), pathArrayLists);

                gridView.setAdapter(imageAdapter);

            }
        }


        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void editHoliday() {

        if (isHoliday) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            AddBasicDetailsFragment addBasicDetailsFragment = new AddBasicDetailsFragment();
            addBasicDetailsFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, addBasicDetailsFragment);
            fragmentTransaction.addToBackStack("back");
            fragmentTransaction.commit();

        } else if (isPlace) {

            Bundle bundle = new Bundle();
            bundle.putInt("placeIDEDIT", placeItem.getId());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            AddBasicDetailsFragment addBasicDetailsFragment = new AddBasicDetailsFragment();
            addBasicDetailsFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.home, addBasicDetailsFragment);
            fragmentTransaction.addToBackStack("back");
            fragmentTransaction.commit();
        }
    }

    public void shareItem() {
        String sharedString = "";

        if (isHoliday) {
            sharedString = item.toString();

        } else if (isPlace) {
            sharedString = placeItem.toString();
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharedString);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Choose an Application"));
    }

    private void addToHoliday() {

        SingleChoiceClass singleChoiceClass = new SingleChoiceClass();
        Bundle bundle = new Bundle();
        bundle.putInt("PVID", placeItem.getId());
        singleChoiceClass.setArguments(bundle);

        singleChoiceClass.show(getChildFragmentManager(), "My_Dialog");
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
        void onListFragmentInteraction(PlaceVisited item);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (isHoliday) {
            if(item!=null) {
                ((MainActivity) getActivity()).setActionBarTitle(item.getTitle());
            }
        } else if (isPlace) {
            if(placeItem!=null) {
                ((MainActivity) getActivity()).setActionBarTitle(placeItem.getTitle());
            }
        }

    }

    public void setList_title(String str) {
        list_title = str;
    }


    public String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = dateFormat.format(date);

        return newDate;
    }



    class PVList extends BaseAdapter {

        //places place visited items into a list on the page
        //also has on long click listener so that the user can view more details

        @Override
        public int getCount() {
            return visits.size();
        }

        @Override
        public Object getItem(int i) {
            return visits.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.places_visited_item, null);

            LinearLayout pvTitleLayout = (LinearLayout) view.findViewById(R.id.pv_list_title);
            TextView pvTitleValue = (TextView) view.findViewById(R.id.pv_list_title_value);

            LinearLayout pvLocationLayout = (LinearLayout) view.findViewById(R.id.pv_list_location);
            TextView pvLocationValue = (TextView) view.findViewById(R.id.pv_list_location_value);

            if (visits != null) {

                if (visits.get(i).getTitle()!= null) {
                    pvTitleLayout.setVisibility(View.VISIBLE);
                    pvTitleValue.setText(visits.get(i).getTitle());
                }

                if (visits.get(i).getPlace()!= null) {
                    pvLocationLayout.setVisibility(View.VISIBLE);
                    pvLocationValue.setText(visits.get(i).getPlace().getName());
                }

            }

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onListFragmentInteraction(visits.get(i));

                    return true;
                }
            });

            return view;
        }
    }



}
