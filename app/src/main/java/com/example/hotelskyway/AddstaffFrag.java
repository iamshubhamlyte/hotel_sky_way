package com.example.hotelskyway;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddstaffFrag extends Fragment {
    Database database;
    TextView count;
    Cursor cursor;
    RelativeLayout relativeLayout;
    private UserDetailAdapter userDetailAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate ( R.layout.fragment_addstaff, container, false );
        // Inflate the layout for this fragment
        FloatingActionButton floatingActionButton =(FloatingActionButton) view.findViewById ( R.id.addStaff );
        floatingActionButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        } );
        database= new Database ( getContext () );
        RecyclerView recyclerView =(RecyclerView) view.findViewById ( R.id.showdata );
        LinearLayoutManager layoutManager = new LinearLayoutManager ( getContext () );
        layoutManager.setStackFromEnd ( true );
        layoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager ( layoutManager );
        cursor =database.AllStaff ( "STAFF" );
        if (cursor.getCount ()==0){
            TextView hint= view.findViewById ( R.id.hint );
            hint.setVisibility ( View.VISIBLE );
        }
        userDetailAdapter= new UserDetailAdapter ( getContext (),cursor );
        recyclerView.setAdapter ( userDetailAdapter );
        new ItemTouchHelper ( new ItemTouchHelper.SimpleCallback (0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((String) viewHolder.itemView.getTag ());
            }
        } ).attachToRecyclerView ( recyclerView );
        count=(TextView) view.findViewById ( R.id.totalstaff );
        count.setText ( String.valueOf ( cursor.getCount () ) );


        return view;

    }


    private void removeItem(String tag) {
        Toast.makeText ( getContext (), tag.toString (), Toast.LENGTH_SHORT ).show ();
    }


    private void customDialog() {

        AlertDialog.Builder myDialog = new  AlertDialog.Builder(getContext ());
        LayoutInflater inflater = LayoutInflater.from(getContext ());
        View view = inflater.inflate(R.layout.input_data_layout,null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(view);
        String[] Designation = { "Select Designation", "Front Desk Clerks", "Porters",
                "Concierges", "HouseKeeping", "Room Service","Kitchen Staff","Chef","Accounting",
                "Assistant Manager","Purchasing" };
        final TextInputEditText FirstName = view.findViewById(R.id.firstName);
        final TextInputEditText LastName = view.findViewById(R.id.lastName);
        final TextInputEditText Email = view.findViewById(R.id.email);
        final TextInputEditText Mobile=view.findViewById ( R.id.Mobile );

        final Spinner spinner =view.findViewById ( R.id.desig_spin );

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String> ( getContext (),
                android.R.layout.simple_spinner_item, Designation );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter ( dataAdapter );


        Button save = view.findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = FirstName.getText().toString().trim();
                String lName = LastName.getText().toString().trim();
                String memail = Email.getText().toString().trim();
                String mmobile= Mobile.getText ().toString ().trim ();
                String subdeg =spinner.getSelectedItem ().toString ();


                if (TextUtils.isEmpty(fName)){
                    FirstName.setError("Required field.");
                    return;
                }
                if (TextUtils.isEmpty(lName)) {
                    LastName.setError("Required field.");
                    return;
                }
                if (TextUtils.isEmpty(memail)){
                    Email.setError("Required field.");
                    return;
                }
                if (TextUtils.isEmpty ( mmobile ) && mmobile.length ()!=10){
                    Mobile.setError ( "Enter Valid Number" );
                    return;
                }
                if(subdeg.equals ( "Select Designation" )){
                    spinner.performClick ();
                    return;
                }

                String date = DateFormat.getDateInstance().format(new Date ());
                long val = database.addUser ( memail,fName+"_123",fName,lName,mmobile,"STAFF","NOTActive",subdeg,date );
                if (val >0)
                {
                    relativeLayout=(RelativeLayout) view.findViewById ( R.id.addStaff_main );
                    userDetailAdapter.swapCursor ( database.AllStaff ( "STAFF" ) );
                    count.setText ( String.valueOf ( cursor.getCount () ) );

                }
                else
                {
                    Toast.makeText ( getContext (), "user not added try once more", Toast.LENGTH_SHORT ).show ();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
