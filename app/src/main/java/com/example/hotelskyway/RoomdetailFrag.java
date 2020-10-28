package com.example.hotelskyway;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoomdetailFrag extends Fragment {
    Snackbar mSnackbar;
    Database database;
    TextView count;
    Cursor cursor,cursor1,cursor2;
    RecyclerView recyclerView;
    private RoomDetailAdapter roomDetailAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate ( R.layout.fragment_roomdetail, container, false );
        // Inflate the layout for this fragment
        database= new Database ( getContext () );
        recyclerView =(RecyclerView) view.findViewById ( R.id.showdata );
        LinearLayoutManager layoutManager = new LinearLayoutManager ( getContext () );
        layoutManager.setStackFromEnd ( true );
        layoutManager.setReverseLayout( true );

        recyclerView.setLayoutManager ( layoutManager );
        //for Updating Recycler View On change;
        UpdateRecyclerview ();
        cursor =database.AllRooms();
        if (cursor.getCount ()==0){
            TextView hint= view.findViewById ( R.id.hint );
            hint.setVisibility ( View.VISIBLE );
        }else{
            TextView Totalrooms=view.findViewById ( R.id.totalrooms );
            Totalrooms.setText ( String.valueOf ( cursor.getCount () ) );
        }



        new ItemTouchHelper ( new ItemTouchHelper.SimpleCallback (0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ChangeRoomStatus((String) viewHolder.itemView.getTag ());
            }
        } ).attachToRecyclerView ( recyclerView );


        return view;

    }

    void UpdateRecyclerview() {
        Cursor cursor;
        cursor =database.AllRooms();
        roomDetailAdapter= new RoomDetailAdapter ( getContext (), cursor );
        recyclerView.setAdapter ( roomDetailAdapter );
        roomDetailAdapter.swapCursor ( database.AllRooms () );
    }

    private void ChangeRoomStatus(final String tag) {
        AlertDialog.Builder myDialog = new  AlertDialog.Builder(getContext ());
        LayoutInflater inflater = LayoutInflater.from(getContext ());
        View view = inflater.inflate(R.layout.changeroomstatus,null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(view);
        dialog.show();
        final TextInputEditText CustomerName = view.findViewById ( R.id.customername );
        TextInputEditText RoomNo = view.findViewById ( R.id.roomnumber );
        TextInputEditText RoomType = view.findViewById ( R.id.roomtype );
        final TextInputEditText DOB = view.findViewById ( R.id.bookingdate );
        final TextInputEditText DOCI = view.findViewById ( R.id.occupingdate );
        final TextInputEditText DOCO = view.findViewById ( R.id.leavingdate );
        final TextView Tasktext = view.findViewById ( R.id.tasktext );
        final ImageView TaskIcon = view.findViewById ( R.id.taskicon );

        RoomNo.setText ( String.valueOf ( Integer.parseInt ( tag )+99 ) );
        final String date = DateFormat.getDateInstance().format(new Date ());
        cursor1=database.OneRoom ( Integer.parseInt ( tag ) );
        final String roomstatus=cursor1.getString ( 1 );
        String roomtype =cursor1.getString ( 2 );

        cursor2=database.BoookingHistory1 (Integer.parseInt ( tag ) ,roomstatus );
        if(roomstatus.equals ( "BOOKED" )){
            TaskIcon.setImageResource ( R.drawable.checkin );
            Tasktext.setText ( "Check In" );
            String dname=cursor2.getString ( 1 );
            String dob=cursor2.getString ( 3 );
            CustomerName.setEnabled ( false );
            CustomerName.setText ( dname );
            RoomType.setText ( roomtype );
            DOB.setText ( dob );
            DOCI.setText ( date );
            DOCO.setVisibility ( View.GONE );
        }
        if(roomstatus.equals ( "FREE" )){
            TaskIcon.setImageResource ( R.drawable.booking );
            Tasktext.setText ( "Book It" );
            RoomType.setText ( roomtype );
            DOB.setText ( date );
            DOCI.setVisibility ( View.GONE );
            DOCO.setVisibility ( View.GONE );
        }
        if(roomstatus.equals ( "OCCUPIED" )){
            TaskIcon.setImageResource ( R.drawable.checkout );
            Tasktext.setText ( "Check Out" );
            String dname=cursor2.getString ( 1 );
            String dob=cursor2.getString ( 3 );
            String doci=cursor2.getString ( 4 );
            CustomerName.setEnabled ( false );
            CustomerName.setText ( dname );
            RoomType.setText ( roomtype );
            DOB.setText ( dob );
            DOCI.setText ( doci );
            DOCO.setText ( date );
        }
        TaskIcon.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String gotcustomername=CustomerName.getText ().toString ().trim ();
                long res = 0,res1=0;
                if(roomstatus.equals ( "BOOKED" )){
                    res=database.UpdateHistoryB2O ( Integer.parseInt ( tag ),date,"OCCUPIED","BOOKED" );
                    res1=database.UpdateRoomDetail ( Integer.parseInt ( tag ),"OCCUPIED" );
                }
                if(roomstatus.equals ( "FREE" )){
                    if (gotcustomername.length ()!=0){
                        res=database.AddHistory ( gotcustomername,Integer.parseInt ( tag ),date,"BOOKED" );
                        res1=database.UpdateRoomDetail ( Integer.parseInt ( tag ),"BOOKED" );
                    }
                    else {
                        CustomerName.setError ( "Enter Customer Name" );
                        return;
                    }

                }
                if(roomstatus.equals ( "OCCUPIED" )){
                    res=database.UpdateHistoryO2F ( Integer.parseInt ( tag ),date,"FREE","OCCUPIED" );
                    res1=database.UpdateRoomDetail ( Integer.parseInt ( tag ),"FREE" );
                }
                if(res>0 && res1>0){
                    Toast.makeText (getContext (), "Successful "+ roomstatus, Toast.LENGTH_SHORT ).show ();
                    //update data
                    UpdateRecyclerview ();
                }
                dialog.dismiss ();
            }
        } );

    }

}
