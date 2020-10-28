package com.example.hotelskyway;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewTaskAdapter extends RecyclerView.Adapter<ViewTaskAdapter.ViewTaskHolder> {
    private Context mContext;
    private static Cursor mCursor;

    public ViewTaskAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ViewTaskHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView ID;
        public TextView positionText;
        public TextView Status;
        public TextView Taskdata;

        public ViewTaskHolder(@NonNull View itemView) {
            super ( itemView );
            nameText = itemView.findViewById ( R.id.card_name );
            ID = itemView.findViewById ( R.id.card_id );
            positionText = itemView.findViewById ( R.id.card_position );
            Status = itemView.findViewById ( R.id.card_taskstatus );
            Taskdata = itemView.findViewById ( R.id.Taskdata );
        }
    }

    @NonNull
    @Override
    public ViewTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from ( mContext );
        View view = inflater.inflate ( R.layout.viewtask_cardview, parent, false );
        return new ViewTaskHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTaskHolder holder, int position) {
        if (!mCursor.moveToPosition ( position )) {
            return;
        }
        String name = mCursor.getString ( 2 );
        String userid =String.valueOf ( 999 + mCursor.getInt ( 1 ));
        String pos = mCursor.getString ( 0 );
        int status = mCursor.getInt ( 5 );
        String task = mCursor.getString ( 4 );
        String statustext;
        if (status == 1) {
            statustext = "COMPLETED";
        } else {
            statustext = "PENDING";
        }//shubham
        holder.nameText.setText ( name );
        holder.ID.setText ( userid );
        holder.positionText.setText ( pos );
        holder.Taskdata.setText ( task );
        holder.Status.setText ( statustext );
        String taskid=mCursor.getString ( 6 );
        holder.itemView.setTag(taskid);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount ();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close ();
        }
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged ();
        }
    }


}
