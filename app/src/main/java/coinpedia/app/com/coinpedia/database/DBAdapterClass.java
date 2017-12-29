package coinpedia.app.com.coinpedia.database;

/**
 * Created by Cyber Matrix3 on 11/18/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import coinpedia.app.com.coinpedia.model.front.ApiToViewModel;


public class DBAdapterClass {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DBConnectionClass mDbHelper;

    public DBAdapterClass(Context context) {
        this.mContext = context;
        mDbHelper = new DBConnectionClass(mContext);
    }

    public DBAdapterClass createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapterClass open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            //mDb = mDbHelper.getReadableDatabase();
            mDb = mDbHelper.getWritableDatabase();

        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    // --- QUERY BELOW
    // ---- PRODUCT SECTION
//  SELECT
    public ArrayList<ApiToViewModel> getCoinData()
    {
        ArrayList<ApiToViewModel> arrayListProduct = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tblCoinData";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
                String longs = mCur.getString(mCur.getColumnIndex("longs"));
                String shorts = mCur.getString(mCur.getColumnIndex("shorts"));
                String perc = mCur.getString(mCur.getColumnIndex("perc"));
                Double price = mCur.getDouble(mCur.getColumnIndex("price"));
                Double volume = mCur.getDouble(mCur.getColumnIndex("volume"));


                ApiToViewModel productPojo = new ApiToViewModel(longs, shorts, perc, price, volume);
                arrayListProduct.add(productPojo);
                mCur.moveToNext();
            }
        }catch (SQLException se)
        {
            Log.e(TAG,"Error : "+se, se);
        }
        return arrayListProduct;
    }

    // Insert
    public void insertCoinTable(long time, String shorts,String longs,String perc, double price, double volume)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put("time", time);
            cv.put("shorts", shorts);
            cv.put("longs", longs);
             cv.put("perc", perc);
            cv.put("price", price);
            cv.put("volume", volume);

            mDb.insert("tblCoinData", null, cv);
        }catch (SQLException se)
        {
            new AlertDialog.Builder(mContext)
                    .setTitle("Coin Data Parsing")
                    .setMessage("Something went wromg!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }

    }

    //DELETE
    public void deleteCoinTable()
    {
        try
        {
            mDb.delete("tblCoinData",null,null);
        }catch (SQLException se)
        {
            new AlertDialog.Builder(mContext)
                    .setTitle("Product Table Refreshing")
                    .setMessage("Something went wromg!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    }

// --------  project update
   /* public void projectOfferUpdate(String offer_date)
    {
        try
        {
            ContentValues cv = new ContentValues();
            //  cv.put("id", 3);
            //  insert into tblOfferBanner (offer_img_dir, offer_img,last_update_date) values
            cv.put("last_update_date", offer_date);


                mDb.update("tblUpdate", cv, "table_name='offer_update'", null);

          *//*  insert(String table, String nullColumnHack, ContentValues values)
            Convenience method for inserting a row into the database.

            update(String table, ContentValues values, String whereCl-ause, String[] whereArgs)
            Convenience method for updating rows in the database.*//*

        }catch (SQLException se)
        {
            new AlertDialog.Builder(mContext)
                    .setTitle("OfferTable Update")
                    .setMessage("Something went wromg!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        }
    }*/

}