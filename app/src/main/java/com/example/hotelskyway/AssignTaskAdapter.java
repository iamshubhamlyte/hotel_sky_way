package com.example.hotelskyway;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssignTaskAdapter extends RecyclerView.Adapter<AssignTaskAdapter.AssignTaskHolder> {
    private  Context mContext;
    private Cursor mCursor;
    public AssignTaskAdapter(Context context, Cursor cursor) {
        mContext=context;
        mCursor=cursor;
    }
    public class AssignTaskHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView ID;
        public TextView positionText;
        public AssignTaskHolder(@NonNull View itemView) {
            super ( itemView );
            nameText=itemView.findViewById ( R.id.card_name );
            ID=itemView.findViewById ( R.id.card_id );
            positionText=itemView.findViewById ( R.id.card_position );
        }
    }

    @NonNull
    @Override
    public AssignTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from ( mContext );
        View view =inflater.inflate ( R.layout.assigntask_cardview,parent,false );
        return new AssignTaskHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull AssignTaskHolder holder, int position) {
        if (!mCursor.moveToPosition (position)){
            return;
        }
        String name =mCursor.getString ( 2 )+" "+mCursor.getString ( 3 );
        String id =String.valueOf ( 999+mCursor.getInt ( 10 ) );
        String pos=mCursor.getString ( 7 );


        holder.nameText.setText ( name );
        holder.ID.setText ( id );
        holder.positionText.setText ( pos );
        holder.itemView.setTag ( mCursor.getString ( 10 ) );
    }



    @Override
    public int getItemCount() {
        return mCursor.getCount ();
    }
    public void swapCursor(Cursor newCursor){
        if (mCursor!=null){
            mCursor.close ();
        }
        mCursor =newCursor;

        if (newCursor!=null){
            notifyDataSetChanged ();
        }
    }


}

