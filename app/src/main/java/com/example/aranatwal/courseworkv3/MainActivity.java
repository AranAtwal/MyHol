package com.example.aranatwal.courseworkv3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.aranatwal.courseworkv3.model.Holiday;
import com.example.aranatwal.courseworkv3.model.HolidayData;
import com.example.aranatwal.courseworkv3.model.MyPhotos;
import com.example.aranatwal.courseworkv3.model.PlaceVisited;
import com.example.aranatwal.courseworkv3.model.PlaceVisitedData;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyHolidaysFragment.OnListFragmentInteractionListener,
        HolidayFragment.OnFragmentInteractionListener,
        AddBasicDetailsFragment.OnFragmentInteractionListener,
        LocationFragment.OnFragmentInteractionListener,
        PhotoGalleryFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        VisitFragment.OnListFragmentInteractionListener,
        InterestingPlaceFragment.OnFragmentInteractionListener {

    public static final int PICK_FROM_GALLERY = 55;

    //shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sets nav drawer to the activity
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create a new Fragment to be placed in the activity layout
        HomeFragment firstFragment = new HomeFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.home, firstFragment).addToBackStack("back").commit();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });

    }

    private void handleShakeEvent(int count) {

        switch (count) {
            //case for different shake counts
            //set at 2 to avoid accidental triggers
            case 2:

                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 66);
                    return;
                }

                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 33);

                break;
        }

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity!", "popping backstack");
                fm.popBackStack();
            } else {
                Log.i("MainActivity!", "nothing on backstack, calling super");
                super.onBackPressed();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //different navigation options in the side nav bar
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            setActionBarTitle(getString(R.string.menu_home));
        } else if (id == R.id.nav_my_holidays) {
            fragment = new MyHolidaysFragment();
            setActionBarTitle(getString(R.string.menu_my_holidays));
        } else if (id == R.id.nav_places_visited) {
            fragment = new VisitFragment();
            setActionBarTitle(getString(R.string.menu_places_visited));
        } else if (id == R.id.nav_travel_gallery) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("TravelGallery", true);
            fragment = new PhotoGalleryFragment();
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_places_of_interest) {
            fragment = new InterestingPlaceFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(R.id.home, fragment);
            ft.addToBackStack("back");
            setActionBarTitle(getString(R.string.menu_home));
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(Holiday holiday) {
        // Create the new fragment,
        HolidayFragment newFragment = new HolidayFragment();
        // add an argument specifying the item it should show

        // note that the Holiday class must implement Serializable
        Bundle args = new Bundle();
        args.putSerializable("Item", holiday);
        newFragment.setArguments(args);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.home, newFragment);
        transaction.addToBackStack("back");

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDoneFragmentInteraction(Holiday holiday) {
        // Create the new fragment,
        HolidayFragment newFragment = new HolidayFragment();
        // add an argument specifying the item it should show

        // note that the Holiday class must implement Serializable
        Bundle args = new Bundle();
        args.putSerializable("Item", holiday);
        newFragment.setArguments(args);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.home, newFragment);
        transaction.addToBackStack("back");

        // Commit the transaction
        transaction.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            //Detects request codes
            if (requestCode == 55 && resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                Bitmap bitmap = null;

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                HolidayData holidayData = HolidayData.getInstance();
                int id = holidayData.getNextId();

                Holiday h = holidayData.getHoliday(id);
                h.getPhotos().addPhoto(picturePath);

                Bundle bundle = new Bundle();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                bundle.putInt("holID", id);

                PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
                photoGalleryFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.home, photoGalleryFragment);
                fragmentTransaction.commit();

            }

            if (requestCode == 44 && resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                Bitmap bitmap = null;

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                PlaceVisitedData placeVisitedData = PlaceVisitedData.getInstance();
                int id_pl = placeVisitedData.getNextId();
                PlaceVisited pl = placeVisitedData.getPlaceVisited(id_pl);

                pl.getPhotos().addPhoto(picturePath);


                Bundle bundle = new Bundle();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                bundle.putInt("plID", id_pl);

                PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
                photoGalleryFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.home, photoGalleryFragment);

                fragmentTransaction.commit();


            }

            if (requestCode == 33 && resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                Bitmap bitmap = null;

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                MyPhotos myPhotos = MyPhotos.getInstance();
                myPhotos.addGlobalPhoto(picturePath);

                Bundle bundle = new Bundle();
                bundle.putBoolean("TravelGallery", true);

                PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
                photoGalleryFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.home, photoGalleryFragment);

                fragmentTransaction.commit();


            }


            if (requestCode == 101 && resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();

                Bundle bundle = new Bundle();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                bundle.putParcelable("interestingPlace", (Parcelable) place);
                InterestingPlaceFragment interestingPlaceFragment = new InterestingPlaceFragment();
                interestingPlaceFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.home, interestingPlaceFragment);
                fragmentTransaction.commit();

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            //checking different permissions
            case 66:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    Toast.makeText(getApplicationContext(), "You will not be able to add media without accepting permissions", Toast.LENGTH_LONG).show();
                }
                break;
            case 77:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSION", "Granted");
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                    Toast.makeText(getApplicationContext(), "You will not be able to view your location without accepting permissions", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(PlaceVisited item) {
        // Create the new fragment,
        HolidayFragment newFragment = new HolidayFragment();
        // add an argument specifying the item it should show

        // note that the Holiday class must implement Serializable
        Bundle args = new Bundle();
        args.putSerializable("Place", item);
        newFragment.setArguments(args);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.home, newFragment);
        transaction.addToBackStack("back");

        // Commit the transaction
        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}




