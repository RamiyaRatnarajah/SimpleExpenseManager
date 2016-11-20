package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

import static java.security.AccessController.getContext;

/**
 * Created by User on 11/20/2016.
 */
public class PersistentDemoExpensemanager extends ExpenseManager {
    Context context;
    public PersistentDemoExpensemanager(Context context) {
        this.context = context;
        setup();
        }
    @Override
    public void setup()  {

        TransactionDAO persistentTransactionDAO = new PersistantTransactionDAO(context.getApplicationContext());
        setTransactionsDAO(persistentTransactionDAO);
        AccountDAO persistentAccountDAO = new InMemoryAccountDAO();
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        Account Acct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account Acct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(Acct1);
        getAccountsDAO().addAccount(Acct2);
    }


}
