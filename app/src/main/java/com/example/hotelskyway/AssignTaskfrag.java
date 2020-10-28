package com.example.hotelskyway;


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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignTaskfrag extends Fragment {
    Database database;
    TextView count;
    Cursor cursor,cursor1,cursor2;
    private AssignTaskAdapter assignTaskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_assign_taskfrag, container, false );

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
        assignTaskAdapter= new AssignTaskAdapter ( getContext (),cursor );
        recyclerView.setAdapter ( assignTaskAdapter );
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


        return view;

    }

    private void removeItem(final String tag) {
        AlertDialog.Builder myDialog = new  AlertDialog.Builder(getContext ());
        LayoutInflater inflater = LayoutInflater.from(getContext ());
        View view = inflater.inflate(R.layout.taskdetail_cardview,null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(view);
        dialog.show();
        final TextInputEditText GetTask= view.findViewById(R.id.Taskdata);



        Button save = view.findViewById(R.id.btn_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String gettask=GetTask.getText ().toString ().trim ();
                if (gettask.length ()<=0)
                {
                    GetTask.setError ( "Assign Any Task" );
                    return;
                }
                cursor1= database.OneStaff ( "STAFF",Integer.parseInt ( tag ) );
                long res=database.AssignTask ( cursor1.getString ( 2 )+" "+cursor1.getString ( 3 ),cursor1.getInt ( 10 ),
                        cursor1.getString ( 8 ),gettask);
                if(res>0){
                    Toast.makeText ( getContext (), "Task Assigned Successfully", Toast.LENGTH_SHORT ).show ();
                }
                dialog.dismiss();
            }
        });
    }

}
