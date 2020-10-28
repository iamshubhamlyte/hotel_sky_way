package com.example.hotelskyway;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import static android.os.Build.ID;



public class Database extends SQLiteOpenHelper{
    public static final  String DATABASE_NAME="HotelData.db";
    public static final  String TABLE_NAME="AuthDetail";
    public static final  String COL_1="Email";
    public static final  String COL_2="Password";
    public static final  String COL_3="First_Name";
    public static final  String COL_4="Last_Name";
    public static final  String COL_5="Designation";
    public static final  String COL_6="Status";
    public static  final String COL_7="Mobile";
    public static  final String COL_8="Subdesignation";
    public static  final String COL_9="Doj";
    public static  final String COL_10="TIMESTAMP";

    public static final  String TABLE_NAME2="RoomDetail";
    public static final  String COL_1_2="RoomNo";
    public static final  String COL_2_2="RoomStatus";
    public static final  String COL_3_2="RoomType";

    public static final  String TABLE_NAME3="RoomHistory";
    public static final  String COL_1_3="TIMESTAMP";
    public static final  String COL_2_3="Name";
    public static final  String COL_3_3="RoomNo";
    public static final  String COL_4_3="DOB";
    public static final  String COL_5_3="DOCI";
    public static final  String COL_6_3="DOCO";
    public static final  String COL_7_3="CurrentRoomStatus";

    public static final  String TABLE_NAME4="TaskAssigned";
    public static final  String COL_1_4="TIMESTAMP";
    public static final  String COL_2_4="ID";
    public static final  String COL_3_4="Name";
    public static final  String COL_4_4="Subdegisnation";
    public static final  String COL_5_4="TASK";
    public static final  String COL_6_4="TASKSTATUS";
    public static final  String COL_7_4="TaskID";





    public Database( Context context) {
        super ( context, DATABASE_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL ( "CREATE TABLE "+TABLE_NAME+"( Email String NOT NULL, Password TEXT,First_Name TEXT, Last_Name TEXT,Mobile TEXT,Designation TEXT,Status TEXT,Subdesignation TEXT,Doj TEXT,TIMESTAMP DEFAULT CURRENT_TIMESTAMP,ID INTEGER PRIMARY KEY AUTOINCREMENT )" );
        db.execSQL ( "CREATE TABLE "+TABLE_NAME2+"("+COL_1_2+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2_2+" TEXT,"+COL_3_2+" TEXT)" );
        db.execSQL("create table "+TABLE_NAME3+" (TIMESTAMP DEFAULT CURRENT_TIMESTAMP,Name TEXT,RoomNo INTEGER,DOB TEXT,DOCI TEXT,DOCO TEXT,CurrentRoomStatus TEXT)");
        db.execSQL("create table "+TABLE_NAME4+" (TIMESTAMP DEFAULT CURRENT_TIMESTAMP,ID INTEGER,Name TEXT,Subdegisnation TEXT,TASK TEXT,TASKSTATUS INTEGER,TaskID INTEGER PRIMARY KEY AUTOINCREMENT)");
    }
//shubham
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ( "DROP TABLE IF EXISTS " + TABLE_NAME );
        //db.execSQL ( "CREATE TABLE IF NOT EXISTS " + TABLE_NAME );

        db.execSQL ( "DROP TABLE IF EXISTS " + TABLE_NAME2);
        //db.execSQL ( "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 );
        db.execSQL ( "DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL ( "DROP TABLE IF EXISTS " + TABLE_NAME4);
        onCreate ( db );

    }

    public long  addUser(String email, String password,String firstName, String lastName,String mobile, String designation,String status,String subdesignation, String doj){
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues (  );
        contentValues.put ( COL_1,email );
        contentValues.put ( COL_2,password );
        contentValues.put ( COL_3,firstName );
        contentValues.put ( COL_4,lastName );
        contentValues.put ( COL_5,designation );
        contentValues.put ( COL_6,status );
        contentValues.put ( COL_7,mobile);
        contentValues.put ( COL_8,subdesignation );
        contentValues.put ( COL_9,doj);
        long res =database.insert ( TABLE_NAME,null,contentValues );
        database.close ();
        return res;

    }


    public boolean checkUserForLogin(String email, String password,String status){
        SQLiteDatabase database = getReadableDatabase ();
        String query = "Select * FROM AuthDetail Where Email='"+email+"'and Password='"+password+"';";
        Log.d("searchUser_query",query);
        Cursor cursor =database.rawQuery ( query,null );
        int count =cursor.getCount ();
        cursor.close();
        if(cursor!=null && count>0){
            ContentValues contentValues = new ContentValues (  );
            contentValues.put ( COL_6,status );
            long res =database.update ( TABLE_NAME, contentValues,"Email = ?",new String[] {email} );
            database.close ();
            return true;
        }
        else {
            return false;
        }
    }
    public boolean checkUserForPasswordChange(String email, String mobile){
        SQLiteDatabase database = getReadableDatabase ();
        String query = "Select * FROM "+TABLE_NAME+" Where Email='"+email+"'and Mobile='"+mobile+"';";
        Log.d("searchUser_query",query);
        Cursor cursor =database.rawQuery ( query,null );
        int count =cursor.getCount ();
        cursor.close();
        database.close();
        if(cursor!=null && count>0){
            return true;
        }
        else {
            return false;
        }
    }
    public long updatepassword(String email, String password){
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues (  );
        contentValues.put ( COL_2,password );
        long res =database.update ( TABLE_NAME, contentValues,"Email = ?",new String[] {email} );
        database.close ();
        return res;
    }

    public long updateprofile(String email, String first,String last,String mobile){
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues (  );
        contentValues.put ( COL_3,first );
        contentValues.put ( COL_4,last );
        contentValues.put ( COL_7,mobile );
        long res =database.update ( TABLE_NAME, contentValues,"Email = ?",new String[] {email} );
        database.close ();
        return res;
    }
    public long LoginStatus (String status){
        int ActiveUser=0;
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT COUNT(*) FROM "+TABLE_NAME+" WHERE Status='"+status+"'; ";
        Cursor cursor = database.rawQuery ( query,null );
        if (cursor.getCount ()>0){
            cursor.moveToFirst ();
            ActiveUser= cursor.getInt ( 0);
        }
         cursor.close ();
        return ActiveUser;
    }

    public Cursor FetchLoggedInUserData(String status){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+TABLE_NAME+" WHERE Status='"+status+"'; ";
        Cursor cursor = database.rawQuery ( query,null );
        //cursor.moveToFirst ();
        return cursor;
    }
    public long LogOutUser(String email, String status){
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues (  );
        contentValues.put ( COL_6,status );
        long res =database.update ( TABLE_NAME, contentValues,"Email = ?",new String[] {email} );
        database.close ();
        return res;
    }
    public Cursor AllStaff(String designation){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+TABLE_NAME+" WHERE Designation='"+designation+"'; ORDER BY "+COL_10+"DESC";
        Cursor cursor =database.rawQuery ( query,null );
        return cursor;
    }
    public Cursor OneStaff(String designation,int id){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+ TABLE_NAME+" WHERE ID ='"+id+"' AND "+COL_5+"='"+designation+"';";
        Cursor cursor =database.rawQuery ( query,null );
        cursor.moveToFirst ();
        return cursor;
    }

    public long  AddRooms(String RoomStatus, String RoomType) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ( COL_2_2, RoomStatus );
        contentValues.put ( COL_3_2, RoomType );
        long res = database.insert ( TABLE_NAME2, null, contentValues );
        Log.d ("query",String.valueOf ( res ) );
        database.close ();
        return res;
    }

    public Cursor AllRooms(){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+TABLE_NAME2+" ORDER BY "+COL_1_2+" DESC";
        Cursor cursor =database.rawQuery ( query,null );
        Log.d ( "Query: ",query );
        return cursor;
    }
    public Cursor OneRoom(int roomno){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+ TABLE_NAME2+" WHERE "+COL_1_2+"='"+roomno+"';";
        Cursor  cursor =database.rawQuery ( query,null );
        cursor.moveToFirst ();
        Log.d ( "Query: ",query );
        return cursor;
    }
    public long UpdateRoomDetail(int roomno,String roomstatus){
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues (  );
        contentValues.put ( COL_2_2,roomstatus );
        long res =database.update ( TABLE_NAME2, contentValues,COL_1_2 +" = " + roomno,null );
        database.close ();
        return res;
    }

    public Cursor BoookingHistory1(int roomno,String currentbookingstatus){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+ TABLE_NAME3+" WHERE "+COL_3_3+"='"+roomno+"' AND "+COL_7_3+"='"+currentbookingstatus+"';";
        Cursor cursor =database.rawQuery ( query,null );
        cursor.moveToFirst ();
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
        }
        Log.d ( "Query: ",query );
        return cursor;
    }
    public Cursor BoookingHistoryall(){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+TABLE_NAME3+" ORDER BY "+COL_1_3+" ASC";
        Cursor cursor =database.rawQuery ( query,null );
        Log.d ( "Query: ",query );
        return cursor;
    }

    public long  AddHistory(String name, int roomno, String dob,String roomstatus) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ( COL_2_3, name );
        contentValues.put ( COL_3_3, roomno );
        contentValues.put ( COL_4_3, dob );
        contentValues.put ( COL_7_3, "BOOKED" );
        long res = database.insert ( TABLE_NAME3, null, contentValues );
        Log.d ("query",String.valueOf ( res ) );
        database.close ();
        return res;
    }
    public long  UpdateHistoryB2O( int roomno,String doci,String roomstatus,String previousroomstatus) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ( COL_5_3, doci );
        contentValues.put ( COL_7_3, roomstatus );
        long res= database.update(TABLE_NAME3, contentValues, ""+COL_3_3+"= '"+ roomno+"' AND "+COL_7_3+"='"+previousroomstatus+"'" , null);
        Log.d ("query",String.valueOf ( res ) );
        database.close ();
        return res;
    }
    public long  UpdateHistoryO2F( int roomno,String doco,String roomstatus, String previousroomstatus) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ( COL_6_3, doco );
        contentValues.put ( COL_7_3, "FREE" );
        long res= database.update(TABLE_NAME3, contentValues, ""+COL_3_3+"= '"+ roomno+"' AND "+COL_7_3+"='"+previousroomstatus+"'" , null);
        Log.d ("query",String.valueOf ( res ) );
        database.close ();
        return res;
    }

//assigning and viewing Task database part
    public long  AssignTask(String name,int id, String subdesination, String Task) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ( COL_3_4,name );
        contentValues.put ( COL_2_4, id );
        contentValues.put ( COL_4_4, subdesination );
        contentValues.put ( COL_5_4, Task );
        contentValues.put ( COL_6_4,0 );
        long res = database.insert ( TABLE_NAME4, null, contentValues );
        Log.d ("query",String.valueOf ( res ) );
        database.close ();
        return res;
    }
    public Cursor  MyDuty( int id) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        String query = " SELECT * FROM "+TABLE_NAME4+" WHERE "+COL_2_4+"='"+id+"'; ORDER BY "+COL_10+"ASC";
        Cursor cursor =database.rawQuery ( query,null );
        Log.d ( "Query: ",query );
        return cursor;
    }
    public Cursor ViewAllTaskAssigned() {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        String query = " SELECT * FROM "+TABLE_NAME4+" ORDER BY "+COL_1_4+" ASC";
        Cursor cursor =database.rawQuery ( query,null );
        Log.d ( "Query: ",query );
        return cursor;
    }
    public String gettaskstatus(int taskid) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        String query = " SELECT "+COL_6_4+" FROM "+ TABLE_NAME4+" WHERE "+COL_7_4+"='"+taskid+"';";
        //String query = " SELECT "+COL_6_4+" FROM "+TABLE_NAME4+" ORDER BY "+COL_1_4+" ASC";
        Cursor cursor =database.rawQuery ( query,null );
        cursor.moveToFirst ();
        String tasksts=cursor.getString ( 0 );
        Log.d ( "Query: ",query );
        return tasksts;
    }
    public long  UpdateTaskStatus( int taskid) {
        SQLiteDatabase database = getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ( COL_6_4, 1 );
        long res= database.update(TABLE_NAME4, contentValues, ""+COL_7_4+"= '"+ taskid+"'" , null);
        Log.d ("query",String.valueOf ( res ) );
        database.close ();
        return res;
    }


    public Cursor currentuser(String status){
        SQLiteDatabase database = getWritableDatabase ();
        String query = " SELECT * FROM "+TABLE_NAME+" WHERE Status='"+status+"'; ";
        Cursor cursor = database.rawQuery ( query,null );
        cursor.moveToFirst ();
        return cursor;
    }


}
