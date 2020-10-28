package com.example.hotelskyway;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Viewssigntask extends Fragment {
    Database database;
    TextView count;
    Cursor cursor,cursor1,cursor2;
    private ViewTaskAdapter viewTaskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate ( R.layout.fragment_viewssigntask, container, false );

        database= new Database ( getContext () );
        RecyclerView recyclerView =(RecyclerView) view.findViewById ( R.id.showdata );
        LinearLayoutManager layoutManager = new LinearLayoutManager ( getContext () );
        layoutManager.setStackFromEnd ( true );
        layoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager ( layoutManager );
        cursor = database.currentuser ("ACTIVE");
        final int id= cursor.getInt ( 10 );
        final String designation=cursor.getString (  7);
        if (designation.equals ( "MANAGER" )){
            cursor1 =database.ViewAllTaskAssigned ();
            if (cursor1.getCount ()==0){
                TextView hint= view.findViewById ( R.id.hint );
                hint.setVisibility ( View.VISIBLE );
            }
            viewTaskAdapter= new ViewTaskAdapter ( getContext (),cursor1 );
            TextView tv=view.findViewById ( R.id.hideformanager );
            tv.setVisibility ( View.GONE );
        }
        else {
            cursor1 =database.MyDuty ( id );
            if (cursor1.getCount ()==0){
                TextView hint= view.findViewById ( R.id.hint );
                hint.setVisibility ( View.VISIBLE );
            }
            viewTaskAdapter= new ViewTaskAdapter ( getContext (),cursor1 );

        }
        //ViewTaskAdapter.swapCursor ( database.AllRooms () );
        recyclerView.setAdapter ( viewTaskAdapter );
        new ItemTouchHelper ( new ItemTouchHelper.SimpleCallback (0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (!designation.equals ( "MANAGER" )){
                    String data= (String) viewHolder.itemView.getTag ();
                    String taskstatus=database.gettaskstatus ( Integer.parseInt ( data ) );
                    removeItem ( Integer.parseInt ( data ), Integer.parseInt ( taskstatus ));
                }
                else{

                }
            }
        } ).attachToRecyclerView ( recyclerView );

        return view;
    }

    private void removeItem(final int taskid, int taskstatus) {

        if(String.valueOf ( taskstatus ).equals ( "0" )){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(getContext ());
            builder.setMessage("Did You Completed This Task?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            long res = database.UpdateTaskStatus ( taskid );
                            if (res>0) {
                                Toast.makeText ( getContext (), "Task Completed", Toast.LENGTH_SHORT ).show ();
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
        else{
            Toast.makeText ( getContext (), "Task is Already Compleated", Toast.LENGTH_SHORT ).show ();
        }

    }

}
