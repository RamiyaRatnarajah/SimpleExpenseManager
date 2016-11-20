package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 11/20/2016.
 */
public class DataBase extends SQLiteOpenHelper {
    public static final String Table_Account = "accounts";
    public static final String Account_number= "account_number";
    public static final String Bank_name = "bank_name";
    public static final String Account_holder_name = "account_holder_name";
    public static final String Balance = "balance";
    public static final String Table_Transaction = "transaction";
    public static final String Date = "date";
    public static final String Amount = "amount";
    public static final String Expense_type = "expense_type";

    private static final String DATABASE_NAME = "140494D.db";
    private static final int DATABASE_VERSION = 1;

    private static final String Create_Account = "create table IF NOT EXISTS " + Table_Account + "(" + Account_number
                   + " TEXT primary key , " + Bank_name
                   + " TEXT not null, " + Account_holder_name
                   + " TEXT not null, " + Balance
                   + " REAL not null);";
    private static final String Create_Transaction = "create table IF NOT EXISTS "
                + Table_Transaction + "(" + Date
                + " TEXT not null , " + Account_number
                + " TEXT not null, " + Amount
                + " REAL not null, " + Expense_type
                + " TEXT not null);";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Account);
        db.execSQL(Create_Transaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBase.class.getName(),
                                "Upgrading database from version " + oldVersion + " to " + newVersion);
    }
}
