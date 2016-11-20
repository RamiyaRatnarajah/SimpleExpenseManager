package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by User on 11/20/2016.
 */
public class PersistantAccountDAO implements AccountDAO {

    private SQLiteDatabase database;
    private DataBase DB1;
    private String[] allColumns = {DataBase.Account_number, DataBase.Bank_name,
              DataBase.Account_holder_name,DataBase.Balance};

    public PersistantAccountDAO(Context context) {
            DB1 = new DataBase(context);
    }

    public void open() throws SQLException {
         database = DB1.getWritableDatabase();
    }

    public void close() {
            DB1.close();
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<String>();
        String args[] = {String.valueOf(DataBase.Account_number)};
        Cursor cursor = database.query(DataBase.Table_Account,args,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            accountNumberList.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return accountNumberList;

    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<Account>();
        Cursor cursor = database.query(DataBase.Table_Account,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Account account = cursorToAccount(cursor);
            accounts.add(account);
            cursor.moveToNext();
        }
        cursor.close();
        return accounts;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String args[] = {String.valueOf(accountNo)};
        Cursor cursor = database.rawQuery("SELECT * FROM projects WHERE project_id=?", args);
        cursor.moveToFirst();
        Account account = cursorToAccount(cursor);
        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBase.Account_number,account.getAccountNo());
        contentValues.put(DataBase.Account_holder_name,account.getAccountHolderName());
        contentValues.put(DataBase.Bank_name,account.getBankName());
        contentValues.put(DataBase.Balance, account.getBalance());
        database.insert(DataBase.Table_Account, null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        database.delete(DataBase.Table_Account, DataBase.Account_number +
                         " = " + accountNo, null);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        Double balance = account.getBalance();
        if(expenseType == ExpenseType.EXPENSE){
                  balance = balance - amount;
        }
        else{
            balance = balance + amount;
        }
        account.setBalance(balance);
        removeAccount(accountNo);
        addAccount(account);
        }
        private Account cursorToAccount(Cursor cursor){
        String accountName = cursor.getString(0);
        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        Double balance = cursor.getDouble(3);
        return new Account(accountName,bankName,accountHolderName,balance);
        }
    }

