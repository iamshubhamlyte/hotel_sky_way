package com.example.hotelskyway;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RoomHistoryAdapter extends RecyclerView.Adapter<RoomHistoryAdapter.RoomHistoryViewHolder> {
    private static Context mContext;
    private Cursor mCursor;

    public RoomHistoryAdapter(Context context, Cursor cursor) {
        mContext=context;
        mCursor=cursor;
    }
    public class RoomHistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView RoomNo;
        public TextView CustomerName;
        public TextView DOB;
        public TextView DOCI;
        public TextView DOCO;
        public RoomHistoryViewHolder(View view) {
            super (view);
            RoomNo=itemView.findViewById ( R.id.card_roomNo );
            CustomerName=itemView.findViewById ( R.id.card_customername );
            DOB=itemView.findViewById ( R.id.card_dob );
            DOCI=itemView.findViewById ( R.id.card_doci );
            DOCO=itemView.findViewById ( R.id.card_doco );

        }
    }


    @Override
    public RoomHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from ( mContext );
        View view =inflater.inflate ( R.layout.roomhistorydata,parent,false );
        return new RoomHistoryViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHistoryAdapter.RoomHistoryViewHolder holder, int position) {
        if (!mCursor.moveToPosition (position)){
            return;
        }
        long roomno =99+mCursor.getLong ( 2 );
        String customername =mCursor.getString ( 1 );
        String dob =mCursor.getString ( 3 );
        String doci =mCursor.getString ( 4 );
        String doco =mCursor.getString ( 5 );


        holder.RoomNo.setText ( String.valueOf ( roomno ) );
        holder.CustomerName.setText ( customername );
        holder.DOB.setText ( dob );
        if (doci==null){
            doci="Not Yet";
        }
        if (doco==null){
            doco="Not Yet";
        }
        holder.DOCI.setText ( doci );
        holder.DOCO.setText ( doco );
        holder.itemView.setTag ( mCursor.getString ( 6 ) );
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
//shubham keshri
        if (newCursor!=null){
            notifyDataSetChanged ();
        }
    }



}
