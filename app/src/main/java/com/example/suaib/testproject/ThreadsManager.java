package com.example.suaib.testproject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by suaib on 5/22/2018.
 */

public class ThreadsManager {

    //Pool, Responsible for Managing Add Row Threads
    private final ScheduledThreadPoolExecutor mAddRowThreadPool;

    //Pool, Responsible for Managing Remove Row Threads
    private final ScheduledThreadPoolExecutor mRemoveRowThreadPool;

    //Managed Pool of Add Row Threads
    private static AddRowRunnable mAddRowRunnable;

    //Managed Pool of Remove Row Threads
    private static RemoveRowRunnable mRemoveRowRunnable;

    //Time Unit seconds thread will wait
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT ;

    //Get Available System Cores
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // instance of PhotoManager-implement the singleton pattern
    private static ThreadsManager sInstance = null;



    //Manages Messages in a Threads
    private Handler mHandler;

    //Signal To add Row
    private static final int SIGN_ADD_ROW = 1;

    //Signal To Remove Row
    private static final int SIGN_REMOVE_ROW = 2;


    //ListView Reference from ui Threads
    private static WeakReference<ListView> mListView;

    //Array Adapter for Table
    private ArrayAdapter<String> TableAdapter;

    //List for Table
    private List<String> TableListArray;

    //Count row that are on Table
    private int countRow = 0;


    // Sets class fields
    static {

        // keep alive Time is in seconds
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

        // instance of ThreadsManager
        sInstance = new ThreadsManager();
    }


    /**
     * Constructs thread pools for AddRow and RemoveRow.
     */
    private ThreadsManager(){

        /*
         *  * Pool of Thread AddRow
         */
        mAddRowThreadPool = new ScheduledThreadPoolExecutor (
                NUMBER_OF_CORES);

        /*
         *  * Pool of Thread RemoveRow
         */
        mRemoveRowThreadPool = new ScheduledThreadPoolExecutor (
                NUMBER_OF_CORES);


        // Handler to take signals from threads and to set value to UI. This Handler makes and Sets Rows.
        mHandler = new Handler(Looper.getMainLooper()) {
            //Take Message to Learn which Threads send
            @Override
            public void handleMessage(Message inputMessage) {

                switch (inputMessage.what) {
                    case SIGN_ADD_ROW:

                        //Add Row to Array and tell to Adapter
                        TableListArray.add("Row " + countRow);
                        TableAdapter.notifyDataSetChanged();
                        //Sum Row Added
                        countRow++;
                        break;

                    case SIGN_REMOVE_ROW:

                        //Remove only if there is any row
                        if(TableListArray.size()>=1)
                        {   //Remove Row From Table
                            TableListArray.remove(TableListArray.size() - 1);
                            TableAdapter.notifyDataSetChanged();
                            countRow--;
                        }
                        break;
                }
            }

        };

    }



    // Add Producer From Producer Button
    public static synchronized void  addRow(ListView listView){
        mListView =  new WeakReference<ListView>(listView);
        mAddRowRunnable = new AddRowRunnable();
        //Put Producer to mAddRowThreadPool Pool
        sInstance.mAddRowThreadPool.scheduleAtFixedRate(mAddRowRunnable, 0, 3, KEEP_ALIVE_TIME_UNIT);
    }


    // Add Consumer From Consumer Button
    public static synchronized void  RemoveRow(ListView listView){
        mListView =  new WeakReference<ListView>(listView);
        mRemoveRowRunnable = new RemoveRowRunnable();
        //Put Consumer to mRemoveRowThreadPool Pool
        sInstance.mRemoveRowThreadPool.scheduleAtFixedRate(mRemoveRowRunnable, 0, 4, KEEP_ALIVE_TIME_UNIT);
    }


    //Getter ThreadsManager Object
    public static ThreadsManager getInstance() {

        return sInstance;
    }


    //Take Signals of Consumer and Producer
    @SuppressLint("HandlerLeak")
    public void handleState(Thread Thread, int sign) {
        switch (sign) {

            // The Producer Signal Made Row
            case SIGN_ADD_ROW:
                // Get object Sent to Handler
                Message completeMessage = mHandler.obtainMessage(sign, Thread);
                completeMessage.sendToTarget();
                break;


            // The Consumer tell that Time to remove Row
            case SIGN_REMOVE_ROW:
                // Get object Sent to Handler
                Message RemoveTime = mHandler.obtainMessage(sign, Thread);
                RemoveTime.sendToTarget();
                break;

            default:
                mHandler.obtainMessage(sign, Thread).sendToTarget();
                break;
        }

    }


    //Getter
    public List<String> getTableListArray() {
        return TableListArray;
    }
    //Setter
    public void setTableListArray(List<String> tableListArray) {
        TableListArray = tableListArray;
    }
    //Getter
    public ArrayAdapter<String> getTableAdapter() {
        return TableAdapter;
    }
    //Setter
    public void setTableAdapter(ArrayAdapter<String> tableAdapter) {
        TableAdapter = tableAdapter;
    }
}
