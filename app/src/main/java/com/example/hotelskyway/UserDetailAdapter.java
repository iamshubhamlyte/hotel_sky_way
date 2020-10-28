package com.example.hotelskyway;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.UserViewHolder> {
    private  Context mContext;
    private Cursor mCursor;

    public UserDetailAdapter(Context context, Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView emailText;
        public TextView positionText;
        public TextView dojText;
        public UserViewHolder(@NonNull View itemView) {
            super ( itemView );

            nameText=itemView.findViewById ( R.id.card_name );
            emailText=itemView.findViewById ( R.id.card_email );
            positionText=itemView.findViewById ( R.id.card_position );
            dojText=itemView.findViewById ( R.id.card_doj );
        }
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from ( mContext );
        View view =inflater.inflate ( R.layout.userdata_layout,parent,false );
        return new UserViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (!mCursor.moveToPosition (position)){
            return;
        }
        String name =mCursor.getString ( 2 )+" "+mCursor.getString ( 3 );
        String email =mCursor.getString ( 0 );
        String doj =mCursor.getString ( 8 );
        String pos=mCursor.getString ( 7 );

        holder.nameText.setText ( name );
        holder.emailText.setText ( email );
        holder.positionText.setText ( pos );
        holder.dojText.setText ( doj );
        holder.itemView.setTag ( mCursor.getString ( 0 ) );
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
