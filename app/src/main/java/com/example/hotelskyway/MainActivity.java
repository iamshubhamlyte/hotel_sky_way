package com.example.hotelskyway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    DrawerLayout drawer;
    Database database;
    Cursor cursor;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String email,firstname,lastname,mobile,subdesignation,doj;
    int Uniqueid;
    TextView navUsername,navPosition,navUniqueId;
    Snackbar mSnackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        database= new Database ( this );
        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);



        cursor = database.FetchLoggedInUserData ("ACTIVE");
        if (cursor.getCount ()==0){
            Toast.makeText ( MainActivity.this, "No Data", Toast.LENGTH_SHORT ).show ();
        }
        else{
            while (cursor.moveToNext ()){
                email=cursor.getString ( 0 );
                firstname=cursor.getString ( 2 );
                lastname=cursor.getString ( 3 );
                mobile=cursor.getString ( 4 );
                subdesignation=cursor.getString (  7);
                doj=cursor.getString ( 8 );
                Uniqueid=cursor.getInt ( 10 )+999;//shubham keshri
            }
        }


        NavigationView navigationView= (NavigationView) findViewById ( R.id.nav_view );
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener ( this );
        navUsername = (TextView) headerView.findViewById ( R.id.header_username );
        navPosition =(TextView) headerView.findViewById ( R.id.header_designation );
        navUniqueId =(TextView) headerView.findViewById ( R.id.header_uniqueid );
        navUsername.setText ( firstname+" "+lastname );
        navPosition.setText ( "( "+subdesignation+" )" );
        navUniqueId.setText ( String.valueOf ( "UID- "+Uniqueid ));


        //setting menu for navigation drawer according to user type
        if (subdesignation.equals ( "MANAGER" )) {
            navigationView.inflateMenu ( R.menu.drawer_menu );
        }
        else{
            navigationView.inflateMenu ( R.menu.drawer_menu_forstaff );
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle ( MainActivity.this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener ( toggle );
        toggle.setDrawerIndicatorEnabled ( true );
        toggle.syncState ();

        // loading default fragment according user
        if (subdesignation.equals ( "MANAGER" )) {
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.add(R.id.fragment_container,new RoomdetailFrag ());
            fragmentTransaction.commit ();
            toolbar.setTitle ( "Room Status" );
        }
        else{
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.add(R.id.fragment_container,new Viewssigntask ());
            fragmentTransaction.commit ();
            toolbar.setTitle ( "Room Status" );
        }
        mSnackbar = Snackbar.make(drawer,"Press again to exit", Snackbar.LENGTH_LONG);

    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer ( GravityCompat.START );
        if (item.getItemId ()==R.id.nav_AddStaff){
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.replace (R.id.fragment_container,new AddstaffFrag ()).addToBackStack ( null);
            fragmentTransaction.commit ();
            toolbar.setTitle ( "ADD STAFF" );
        }
        if (item.getItemId ()==R.id.nav_AssignTask){
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.replace (R.id.fragment_container,new AssignTaskfrag ()).addToBackStack ( null);
            fragmentTransaction.commit ();
            toolbar.setTitle ( "ASSIGN TASK" );
        }
        if (item.getItemId ()==R.id.nav_ViewTask){
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.replace (R.id.fragment_container,new Viewssigntask ()).addToBackStack ( null);
            fragmentTransaction.commit ();
            if (subdesignation.equals ( "MANAGER" )){
                toolbar.setTitle ( "VIEW ASSIGNED TASK" );
            }
            else {
                toolbar.setTitle ( "MY DUTY" );
            }
        }
        if (item.getItemId ()==R.id.nav_RoomDetail){
            toolbar.setTitle ( "ROOM DETAIL" );
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.replace (R.id.fragment_container,new RoomdetailFrag ()).addToBackStack ( null);
            fragmentTransaction.commit ();
        }
        if (item.getItemId ()==R.id.nav_RoomBookingHistory){
            toolbar.setTitle ( "ROOM BOOKING HISTORY" );
            fragmentManager = getSupportFragmentManager ();
            fragmentTransaction = fragmentManager.beginTransaction ();
            fragmentTransaction.replace (R.id.fragment_container,new RoomHistoryFrag ()).addToBackStack ( null);
            fragmentTransaction.commit ();
        }
        if (item.getItemId ()==R.id.nav_Logout){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            long res = database.LogOutUser (email,"NOTActive");
                            if (res>0) {
                                Toast.makeText ( getApplicationContext (), "Logout Successful",
                                        Toast.LENGTH_SHORT ).show ();
                                Intent i =new Intent ( MainActivity.this,splashscreen.class );
                                startActivity ( i );
                                finish ();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        if(item.getItemId ()==R.id.nav_aboutappcreator){
            AlertDialog.Builder myDialog = new  AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.aboutme,null);
            final AlertDialog dialog = myDialog.create();
            dialog.setView(view);
            dialog.show();

            Button save = view.findViewById(R.id.btn_save);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                }

            });
        }
        if (item.getItemId ()==R.id.Logout){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            long res = database.LogOutUser (email,"NOTActive");
                            if (res>0) {
                                Toast.makeText ( getApplicationContext (), "Logout Successful",
                                        Toast.LENGTH_SHORT ).show ();
                                Intent i =new Intent ( MainActivity.this,splashscreen.class );
                                startActivity ( i );
                                finish ();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        if(item.getItemId ()==R.id.nav_Resources){
            AlertDialog.Builder myDialog = new  AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.resource,null);
            final AlertDialog dialog = myDialog.create();
            dialog.setView(view);
            dialog.show();

            final TextInputEditText Single = view.findViewById(R.id.Single);
            final TextInputEditText Double = view.findViewById(R.id.Double);
            final TextInputEditText Triple = view.findViewById(R.id.Triple);
            final TextInputEditText Guest=view.findViewById ( R.id.Guest );
            final TextInputEditText Meeting=view.findViewById ( R.id.Meeting );
            //cursor =database.AllRooms ();
            //roomDetailAdapter= new RoomDetailAdapter ( MainActivity.this,cursor );
            Button save = view.findViewById(R.id.btn_save);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int single,doubles,triple,guest,meeting;
                    if ( Single.getText ().toString ().trim ().length ()!=0){
                         single = Integer.parseInt ( Single.getText ().toString ().trim () );
                        if (single<0){
                            Single.setError("Enter valid data");
                            return;
                        }

                    }
                    else{
                        Single.setError("Required field.");
                        return;
                    }
                    if ( Double.getText ().toString ().trim ().length ()!=0){
                        doubles = Integer.parseInt ( Double.getText ().toString ().trim () );
                        if (doubles<0){
                            Double.setError("Enter valid data");
                            return;
                        }

                    }
                    else{
                        Double.setError("Required field.");
                        return;
                    }
                    if ( Triple.getText ().toString ().trim ().length ()!=0){
                        triple = Integer.parseInt ( Triple.getText ().toString ().trim () );
                        if (triple<0){
                            Triple.setError("Enter valid data");
                            return;
                        }

                    }
                    else{
                        Triple.setError("Required field.");
                        return;
                    }
                    if ( Guest.getText ().toString ().trim ().length ()!=0){
                        guest = Integer.parseInt ( Guest.getText ().toString ().trim () );
                        if (guest<0){
                            Guest.setError("Enter valid data");
                            return;
                        }
                    }
                    else{
                        Guest.setError("Required field.");
                        return;
                    }
                    if ( Meeting.getText ().toString ().trim ().length ()!=0){
                        meeting = Integer.parseInt ( Meeting.getText ().toString ().trim () );
                        if (meeting<0){
                            Meeting.setError("Enter valid data");
                            return;
                        }

                    }
                    else{
                        Meeting.setError("Required field.");
                        return;
                    }


                    for(int i=0;i<single;i++) {
                        long val = database.AddRooms ( "FREE","SINGLE BED" );

                    }
                    for(int i=0;i<doubles;i++) {
                        long val = database.AddRooms ( "FREE","DOUBLE BED" );

                    }
                    for(int i=0;i<triple;i++) {
                        long val = database.AddRooms ( "FREE","TRIPLE BED" );
                    }
                    for(int i=0;i<guest;i++) {
                        long val = database.AddRooms ( "FREE","GUEST ROOM" );

                    }
                    long aa = 0;
                    for(int i=0;i<meeting;i++) {
                        long val = database.AddRooms ( "FREE","MEETING ROOM" );
                        aa=val;
                    }
                    if (aa>0)
                    {
                        //updating content of recycler view
                        //RoomdetailFrag roomdetailFrag =new RoomdetailFrag ();
                        //roomdetailFrag.UpdateRecyclerview();
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        Toast.makeText ( MainActivity.this, "Resources Updated SuccessFully", Toast.LENGTH_SHORT ).show ();
                    }
                    else
                    {
                        Toast.makeText ( MainActivity.this, "Error Updating data\n uninstall and Update again", Toast.LENGTH_SHORT ).show ();
                    }
                    dialog.dismiss();
                }

            });


        }
        if(item.getItemId ()==R.id.nav_updateProfile){
            AlertDialog.Builder myDialog = new  AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.updateprofile,null);
            final AlertDialog dialog = myDialog.create();
            dialog.setView(view);
            dialog.show();
            final TextView Email = view.findViewById(R.id.email);
            final TextView Position = view.findViewById(R.id.position);
            final TextInputEditText FirstName = view.findViewById(R.id.firstName);
            final TextInputEditText LastName=view.findViewById ( R.id.lastName );
            final TextInputEditText Mobile=view.findViewById ( R.id.Mobile );
            Email.setText ( email );
            Position.setText ( "( "+subdesignation+" )" );
            FirstName.setText ( firstname );
            LastName.setText ( lastname );
            Mobile.setText (mobile);
            Button save = view.findViewById(R.id.btn_save);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String mfirstname=FirstName.getText ().toString ().trim ();
                    String mlastname =LastName.getText ().toString ().trim ();
                    String mmobile= Mobile.getText ().toString ().trim ();


                    if ( mfirstname.length ()<=0){
                        FirstName.setError("Enter First Name");
                        return;
                    }
                    if ( mlastname.length ()<=0){
                        LastName.setError("Enter Last Name");
                        return;
                    }
                    if ( Mobile.length ()!=10){
                        Mobile.setError("Enter valid Number");
                        return;
                    }

                    long val = database.updateprofile ( email,mfirstname,mlastname,mmobile );
                    if (val >0)
                    {
                        Toast.makeText ( MainActivity.this, "Successfully Updated", Toast.LENGTH_SHORT ).show ();
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                    else
                    {
                        Toast.makeText ( MainActivity.this, "Not Updated", Toast.LENGTH_SHORT ).show ();
                    }

                    dialog.dismiss();
                }
            });

        }
        if (item.getItemId ()==R.id.nav_myProfile){
            AlertDialog.Builder myDialog = new  AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.myprofile,null);
            final AlertDialog dialog = myDialog.create();
            dialog.setView(view);
            dialog.show();
            final TextInputEditText Email = view.findViewById(R.id.email);
            final TextInputEditText Position = view.findViewById(R.id.position);
            final TextInputEditText FirstName = view.findViewById(R.id.firstName);
            final TextInputEditText LastName=view.findViewById ( R.id.lastName );
            final TextInputEditText Mobile=view.findViewById ( R.id.Mobile );
            final TextInputEditText Doj=view.findViewById ( R.id.doj );

            Email.setText ( email );
            Position.setText ( subdesignation );
            FirstName.setText ( firstname );
            LastName.setText ( lastname );
            Mobile.setText (mobile);
            Doj.setText ( doj );
            Button save = view.findViewById(R.id.btn_save);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        return true;
    }

    int i=0;
    @Override
    public void onBackPressed() {

        if (mSnackbar.isShown()) {
            super.onBackPressed();
        } else {
            mSnackbar.show();
        }
    }

}
