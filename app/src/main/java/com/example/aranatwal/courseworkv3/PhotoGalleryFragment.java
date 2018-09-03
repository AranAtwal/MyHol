package com.example.aranatwal.courseworkv3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.MyPhotos;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.model.PlaceVisitedData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoGalleryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private int int_id;
    private Holiday existing_hol;
    public HolidayData holidayData = new HolidayData().getInstance();
    private Menu mOptionsMenu;
    private boolean isHoliday = false;

    private PlaceVisited existing_place;
    public PlaceVisitedData placeVisitedData = new PlaceVisitedData().getInstance();
    private int int_id_pl;
    private boolean isPlaceVisited = false;
    private boolean isTravelGallery = false;
    static boolean chosenTGPhotos, chosenHolPhotos, chosenPVPhotos;

    private TextView allPhotos, allHolidays, allPlaces;

    MyPhotos myPhotos = MyPhotos.getInstance();
    private ArrayList<String> global;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoGalleryFragment newInstance(String param1, String param2) {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
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

        if (getArguments() != null) {
            //different uses for the page all determined by route taken to get to the page

            if (bundle.containsKey("holID")) {
                isHoliday = true;
                int_id = bundle.getInt("holID");

                existing_hol = holidayData.getHoliday(int_id);
            } else if (bundle.containsKey("plID")) {
                isPlaceVisited = true;
                int_id_pl = bundle.getInt("plID");

                existing_place = placeVisitedData.getPlaceVisited(int_id_pl);
            } else if (bundle.containsKey("TravelGallery")) {
                isTravelGallery = true;

                if (bundle.containsKey("chosenTGPhotos")) {
                    chosenTGPhotos = bundle.getBoolean("chosenTGPhotos");
                    chosenHolPhotos = bundle.getBoolean("chosenHolPhotos");
                    chosenPVPhotos = bundle.getBoolean("chosenPVPhotos");
                } else {
                    chosenTGPhotos = true;
                    chosenHolPhotos = true;
                    chosenPVPhotos = true;
                }

            }

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_photo_gallery, menu);
        mOptionsMenu = menu;

        //sets filter icon and reports tick icon
        if (isTravelGallery) {
            mOptionsMenu.getItem(2).setVisible(false);
            mOptionsMenu.getItem(0).setVisible(true);
        }

        super.onCreateOptionsMenu(mOptionsMenu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_done:
                if (isHoliday) {
                    mListener.onDoneFragmentInteraction(existing_hol);
                } else if (isPlaceVisited) {
                    mListener.onListFragmentInteraction(existing_place);
                }
                return true;
            case R.id.filter_photos:
                setFilters();
                return true;
            case R.id.action_add:
                addPhoto();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        allPhotos = (TextView) view.findViewById(R.id.all_photos);
        allHolidays = (TextView) view.findViewById(R.id.all_photos_holiday);
        allPlaces = (TextView) view.findViewById(R.id.all_photos_places);

        global = myPhotos.getGlobalPhotoStrings();

//sets different grid views to populate
    if (isHoliday) {
        GridView gridView = (GridView) view.findViewById(R.id.photo_grid);
        gridView.setVisibility(View.VISIBLE);
        ArrayList<String> pathArrayLists = existing_hol.getPhotos().getPhotoStrings();
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), pathArrayLists);
        gridView.setAdapter(imageAdapter);
    } else if (isPlaceVisited) {
        GridView gridView = (GridView) view.findViewById(R.id.photo_grid);
        gridView.setVisibility(View.VISIBLE);
        ArrayList<String> pathArrayLists = existing_place.getPhotos().getPhotoStrings();
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), pathArrayLists);
        gridView.setAdapter(imageAdapter);
    } else if (isTravelGallery) {

        //checks chosen filters and displays appropriate sections
        if (chosenTGPhotos) {
            GridView gridView = (GridView) view.findViewById(R.id.photo_grid);
            if (!global.isEmpty()) {
                if (global != null) {
                    allPhotos.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<String> pathArrayLists = global;
            ImageAdapter imageAdapter = new ImageAdapter(getContext(), pathArrayLists);
            gridView.setAdapter(imageAdapter);
        }

        if (chosenHolPhotos) {
            GridView gridView2 = (GridView) view.findViewById(R.id.photo_grid_2);
            ArrayList<String> holPhotos = new ArrayList<String>();
            List<Holiday> hols = holidayData.getHolidays();
            for (Holiday hol :
                    hols) {
                if (hol.getPhotos().getPhotoStrings() != null && hol.getPhotos().getPhotoStrings().size() > 0) {
                    holPhotos.addAll(hol.getPhotos().getPhotoStrings());
                    allHolidays.setVisibility(View.VISIBLE);
                    gridView2.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<String> pathArrayLists2 = holPhotos;
            ImageAdapter imageAdapter2 = new ImageAdapter(getContext(), pathArrayLists2);
            gridView2.setAdapter(imageAdapter2);
        }

        if (chosenPVPhotos) {
            GridView gridView3 = (GridView) view.findViewById(R.id.photo_grid_3);
            ArrayList<String> placePhotos = new ArrayList<String>();
            List<PlaceVisited> places = placeVisitedData.getPlacesVisited();
            for (PlaceVisited place :
                    places) {
                if (place.getPhotos().getPhotoStrings() != null && place.getPhotos().getPhotoStrings().size() > 0) {
                    placePhotos.addAll(place.getPhotos().getPhotoStrings());
                    allPlaces.setVisibility(View.VISIBLE);
                    gridView3.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<String> pathArrayLists3 = placePhotos;
            ImageAdapter imageAdapter3 = new ImageAdapter(getContext(), pathArrayLists3);
            gridView3.setAdapter(imageAdapter3);
        }
    }


        return view;
    }

    public void addPhoto() {

        //adds photos but adds them to different collections
        if (isHoliday) {

            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 66);
                return;
            }

            getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 55);


        } else if (isPlaceVisited) {

            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 66);
                return;
            }

            getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 44);


        } else if (isTravelGallery) {

            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 66);
                return;
            }

            getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 33);

        }



    }


    private void setFilters() {
        FilterPhotosClass filterPhotosClass = new FilterPhotosClass();
        filterPhotosClass.show(getChildFragmentManager(), "My_Dialog");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isTravelGallery) {
            ((MainActivity) getActivity()).setActionBarTitle("Travel Gallery");

        } else {
            ((MainActivity) getActivity()).setActionBarTitle("Add Media");
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
        void onDoneFragmentInteraction(Holiday holiday);
        void onListFragmentInteraction(PlaceVisited placeVisited);
    }

    public static class FilterPhotosClass extends DialogFragment {
        //three different filters for each type of photo
        final CharSequence[] filters = {"Travel Gallery", "Holidays", "Places Visited"};


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Set Filters").setMultiChoiceItems(filters, new boolean[]{chosenTGPhotos, chosenHolPhotos, chosenPVPhotos}, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                    switch (i) {
                        case 0:
                            if (chosenTGPhotos) {
                                chosenTGPhotos = false;
                            } else {
                                chosenTGPhotos = true;
                            }
                            break;
                        case 1:
                            if (chosenHolPhotos) {
                                chosenHolPhotos = false;
                            } else {
                                chosenHolPhotos = true;
                            }

                            break;
                        case 2:
                            if (chosenPVPhotos) {
                                chosenPVPhotos = false;
                            } else {
                                chosenPVPhotos = true;
                            }
                            break;
                        default:
                            chosenTGPhotos = true;
                            chosenHolPhotos = true;
                            chosenPVPhotos = true;
                            break;
                    }

                }
            }).setPositiveButton("Select Filters", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), "Filters applied", Toast.LENGTH_LONG).show();

                    Bundle bundle = new Bundle();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    bundle.putBoolean("TravelGallery", true);
                    //passes through booleans of selected filters
                    bundle.putBoolean("chosenTGPhotos", chosenTGPhotos);
                    bundle.putBoolean("chosenHolPhotos", chosenHolPhotos);
                    bundle.putBoolean("chosenPVPhotos", chosenPVPhotos);

                    PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
                    photoGalleryFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.home, photoGalleryFragment);
                    fragmentTransaction.addToBackStack("back");
                    fragmentTransaction.commit();
                }
            });

            return builder.create();
        }

    }

}