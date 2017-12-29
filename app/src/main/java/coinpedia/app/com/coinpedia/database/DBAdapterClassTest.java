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

public class DBAdapterClassTest {
   /* protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DBConnectionClass mDbHelper;

    public DBAdapterClassTest(Context context) {
        this.mContext = context;
        mDbHelper = new DBConnectionClass(mContext);
    }

    public DBAdapterClassTest createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapterClassTest open() throws SQLException {
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

    // PROJECT UPDATE SECTION
    public ArrayList<ProjectUpdatePojo> getProjectUpdateData()
    {
        ArrayList<ProjectUpdatePojo> arrayListProjectUpdate = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tblUpdate";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
                String table_name = mCur.getString(mCur.getColumnIndex("table_name"));
                String last_update_date = mCur.getString(mCur.getColumnIndex("last_update_date"));

                ProjectUpdatePojo productUpdatePojo = new ProjectUpdatePojo(table_name,last_update_date);
                arrayListProjectUpdate.add(productUpdatePojo);
                mCur.moveToNext();
            }
        }catch (SQLException se)
        {
            Log.e(TAG,"Error : "+se, se);
        }
        return arrayListProjectUpdate;
    }

    // SPLASH SCREEN SECTION
    public ArrayList<AppDescMasterPojo> getSplashData() {
        ArrayList<AppDescMasterPojo> splashArrList = new ArrayList<>();

        try {

            String sql = "select msg_data from tbl_splash_screen";
            Cursor mCur = mDb.rawQuery(sql, null);

            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {

                String data = mCur.getString(mCur.getColumnIndex("msg_data"));

                AppDescMasterPojo splashMasterPojo = new AppDescMasterPojo(data);
                splashArrList.add(splashMasterPojo);
                mCur.moveToNext();
            }
        }catch (SQLException mSQLException)
        {
            Log.e(TAG," SQL Error"+mSQLException, mSQLException );
        }
        return splashArrList;
    }

    // OFFER SECTION
    public ArrayList<BannerPojo> getBannerData()
    {
        ArrayList<BannerPojo> bannerAL = new ArrayList<>();

        try {

            String sql = "select * from tblOfferBanner";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
                String bannerImgDir = mCur.getString(mCur.getColumnIndex("offer_img_dir"));
                String bannerImg = mCur.getString(mCur.getColumnIndex("offer_img"));

                BannerPojo bannerPojo = new BannerPojo(bannerImgDir, bannerImg);
                bannerAL.add(bannerPojo);
            mCur.moveToNext();
            }

        }catch (SQLException se)
        {
            Log.e(TAG, "Error"+se, se);
        }
        return bannerAL;
    }

    public void updateOfferTable(String dir, String image, String date)
    {
        try
        {
            ContentValues cv = new ContentValues();
          //  cv.put("id", 3);


          //  insert into tblOfferBanner (offer_img_dir, offer_img,last_update_date) values
            cv.put("offer_img_dir", dir);
            cv.put("offer_img", image);
         //   cv.put("last_update_date", date);
          //  cv.put("Mobile", arr.get(i).mobile);
            mDb.insert("tblOfferBanner", null, cv);

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
    }


    public void deleteOfferTable()
    {
        try
        {
            mDb.delete("tblOfferBanner",null,null);
        }catch (SQLException se)
        {
            Log.e(TAG, "Error : "+se,se);
        }
    }


    // --- CATEGORY SECTION

    public ArrayList<CategoryPojo> getCategoryData()
    {
        ArrayList<CategoryPojo> arrayListCategory = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tblCategory";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
           //     int category_id = mCur.getInt(mCur.getColumnIndex("category_id"));
                String category_name = mCur.getString(mCur.getColumnIndex("category_name"));
                String category_dir = mCur.getString(mCur.getColumnIndex("category_dir"));
                String category_img = mCur.getString(mCur.getColumnIndex("category_img"));

                CategoryPojo categoryPojo = new CategoryPojo(0, category_name, category_dir, category_img);
                arrayListCategory.add(categoryPojo);
                mCur.moveToNext();
            }
        }catch (SQLException se)
        {
            Log.e(TAG,"Error : "+se, se);
        }
        return arrayListCategory;
    }
    public ArrayList<CategoryPojo> getCategoryIdData(int id)
    {
        ArrayList<CategoryPojo> arrayListProduct = new ArrayList<>();

        try {
            String sql = "SELECT category_id FROM tblCategory where id='"+id+"'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
                int category_id = mCur.getInt(mCur.getColumnIndex("category_id"));

                CategoryPojo categoryPojo = new CategoryPojo(category_id, null, null, null);
                arrayListProduct.add(categoryPojo);
                mCur.moveToNext();
            }
        }catch (SQLException se)
        {
            Log.e(TAG,"Error : "+se, se);
        }
        return arrayListProduct;
    }

    public void updateCategoryTable(String categoryID, String categoryName,String categoryImage,String categoryDir, String date)
    {
        try
        {
            ContentValues cv = new ContentValues();
                   cv.put("category_id", categoryID);
            cv.put("category_name", categoryName);
            cv.put("category_img", categoryImage);
            cv.put("category_dir", categoryDir);
            //   cv.put("last_update_date", date);
            //  cv.put("Mobile", arr.get(i).mobile);
            mDb.insert("tblCategory", null, cv);

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
    }

    public void deleteCategoryTable()
    {
        try
        {
            mDb.delete("tblCategory",null,null);
            //    mDb.close();
           *//*String sql = "DELETE FROM tblOfferBanner";
            Cursor cursor = mDb.rawQuery(sql,null);*//*


        }catch (SQLException se)
        {
            new AlertDialog.Builder(mContext)
                    .setTitle("Category Table Refreshing")
                    .setMessage("Something went wromg!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    }


    // ---- PRODUCT SECTION

    public ArrayList<ProductPojo> getProductData(int cate_id)
    {
        ArrayList<ProductPojo> arrayListProduct = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tblProduct where category_id='"+cate_id+"'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
                String product_name = mCur.getString(mCur.getColumnIndex("product_name"));
                String product_dir = mCur.getString(mCur.getColumnIndex("product_dir"));
                String product_img = mCur.getString(mCur.getColumnIndex("product_img"));

                ProductPojo productPojo = new ProductPojo(0, product_name, product_dir, product_img,null);
                arrayListProduct.add(productPojo);
                mCur.moveToNext();
            }
        }catch (SQLException se)
        {
            Log.e(TAG,"Error : "+se, se);
        }
        return arrayListProduct;
    }


    public void updateProductTable(String categoryID, String productName,String productImage,String productUrl, String productDir, String date)
    {
        try
        {
            ContentValues cv = new ContentValues();
            cv.put("category_id", categoryID);
            cv.put("product_name", productName);
            cv.put("product_img", productImage);
             cv.put("product_url", productUrl);
            cv.put("product_dir", productDir);
            //   cv.put("last_update_date", date);
            //  cv.put("Mobile", arr.get(i).mobile);
            mDb.insert("tblProduct", null, cv);

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
    }

    public void deleteProductTable()
    {
        try
        {
            mDb.delete("tblProduct",null,null);
            //    mDb.close();
           *//*String sql = "DELETE FROM tblOfferBanner";
            Cursor cursor = mDb.rawQuery(sql,null);*//*


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
    public void projectOfferUpdate(String offer_date)
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
    }

    public void projectProductUpdate(String product_date)
    {
        try
        {
            ContentValues cv = new ContentValues();
            //  cv.put("id", 3);


            //  insert into tblOfferBanner (offer_img_dir, offer_img,last_update_date) values
            cv.put("last_update_date", product_date);


                mDb.update("tblUpdate",cv,"table_name='product_update'",null);

            *//*  insert(String table, String nullColumnHack, ContentValues values)
            Convenience method for inserting a row into the database.

            update(String table, ContentValues values, String whereClause, String[] whereArgs)
            Convenience method for updating rows in the database.*//*

        }catch (SQLException se)
        {
            new AlertDialog.Builder(mContext)
                    .setTitle("ProductTable Update")
                    .setMessage("Something went wromg!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        }
    }

    public ArrayList<ProductPojo> getProductKnowMoreData(String prodName)
    {
        ArrayList<ProductPojo> arrayListProduct = new ArrayList<>();

        try {
            String sql = "SELECT product_url FROM tblProduct where product_name='"+prodName+"'";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            while (mCur.isAfterLast() == false) {
                String product_url = mCur.getString(mCur.getColumnIndex("product_url"));

                ProductPojo productPojo = new ProductPojo(0,null,null,null,product_url);
                arrayListProduct.add(productPojo);
                mCur.moveToNext();
            }
        }catch (SQLException se)
        {
            Log.e(TAG,"Error : "+se, se);
        }
        return arrayListProduct;
    }
*/}