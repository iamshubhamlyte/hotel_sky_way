package com.example.hotelskyway;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
public class RoomHistoryFrag extends Fragment  {

    Database database;
    Cursor cursor;
    private  RoomHistoryAdapter roomHistoryAdapter;

    public RoomHistoryFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_room_history, container, false );
        // Inflate the layout for this fragment
        database= new Database ( getContext () );
        //database= new Database ( getContext () );
        RecyclerView recyclerView =(RecyclerView) view.findViewById ( R.id.showdata );
        LinearLayoutManager layoutManager = new LinearLayoutManager ( getContext () );
        layoutManager.setStackFromEnd ( true );
        layoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager ( layoutManager );
        cursor =database.AllRooms();
        if (cursor.getCount ()==0){
            TextView hint= view.findViewById ( R.id.hint );
            hint.setVisibility ( View.VISIBLE );
        }
        roomHistoryAdapter= new RoomHistoryAdapter ( getContext (), cursor );
        recyclerView.setAdapter ( roomHistoryAdapter );
        roomHistoryAdapter.swapCursor ( database.BoookingHistoryall () );
        return view;
    }


}
