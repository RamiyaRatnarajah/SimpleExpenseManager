package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by User on 11/20/2016.
 */
public class PersistantTransactionDAO implements TransactionDAO {

    private SQLiteDatabase database;
    private DataBase dbHelper;
    private String[] allColumns = {DataBase.Date, DataBase.Account_number,
            DataBase.Amount, DataBase.Expense_type};

    public PersistantTransactionDAO(Context context) {
        dbHelper = new DataBase(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATEFORMAT,Locale.ENGLISH);
        String dateString = simpleDateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.Date,dateString);
        contentValues.put(DataBase.Account_number,accountNo);
        String expenseTypeString = Constants.INCOME;
        if(expenseType == ExpenseType.EXPENSE){
            expenseTypeString = Constants.EXPENSE;
        }
        contentValues.put(DataBase.Expense_type,expenseTypeString);
        contentValues.put(DataBase.Amount,amount);

        database.insert(DataBase.Table_Transaction, null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<Transaction>();

        Cursor cursor = database.query(DataBase.Table_Transaction, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Transaction transaction = cursorToTransaction(cursor);
            transactions.add(transaction);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transactions;
    }


    private Transaction cursorToTransaction(Cursor cursor){
           String date1= cursor.getString(0);
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATEFORMAT, Locale.ENGLISH);
            Date date=new Date();
        try{
            date=dateFormat.parse(date1);
        }catch(ParseException e){
            e.printStackTrace();
        }
           String accountNo = cursor.getString(1);
           String expenseType1 = cursor.getString(2);
        ExpenseType expenseType=ExpenseType.EXPENSE;
        if(expenseType1.equals(Constants.INCOME)){
            expenseType=ExpenseType.INCOME;
        }
           Double amount = cursor.getDouble(3);
           return new Transaction(date,accountNo,expenseType,amount);
    }
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }



}
