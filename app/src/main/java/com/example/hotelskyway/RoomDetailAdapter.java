package com.example.hotelskyway;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoomDetailAdapter extends RecyclerView.Adapter<RoomDetailAdapter.RoomViewHolder> {
    private static Context mContext;
    private Cursor mCursor;


    public RoomDetailAdapter(Context context, Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }
    public static class RoomViewHolder extends RecyclerView.ViewHolder{
        public TextView RoomNo;
        public TextView RoomType;
        public ImageView RoomStatus;
        public RoomViewHolder(@NonNull View itemView) {
            super ( itemView );

            RoomNo=itemView.findViewById ( R.id.card_roomNo );
            RoomType=itemView.findViewById ( R.id.card_roomType );
            RoomStatus=itemView.findViewById ( R.id.card_roomStatus );
            itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {

                }
            } );
        }


    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from ( mContext );
        View view =inflater.inflate ( R.layout.roomdata,parent,false );
        return new RoomViewHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        if (!mCursor.moveToPosition (position)){
            return;
        }
        long roomno =99+mCursor.getLong ( 0 );
        String roomtype =mCursor.getString ( 2 );
        String roomstatus =mCursor.getString ( 1 );


        holder.RoomNo.setText ( String.valueOf ( roomno ) );
        holder.RoomType.setText ( roomtype );
        if (roomstatus.equals ( "FREE" )){
            holder.RoomStatus.setBackgroundResource ( R.drawable.free );
        }
        if (roomstatus.equals ( "BOOKED" )){
            holder.RoomStatus.setBackgroundResource ( R.drawable.booked );
        }
        if (roomstatus.equals ( "OCCUPIED" )){
            holder.RoomStatus.setBackgroundResource ( R.drawable.occupied );
        }
        holder.itemView.setTag ( mCursor.getString ( 0 ) );
    }
    //created by shubham keshri
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
