package com.example.suaib.testproject;

/*
* Created By Shuaib Zengo
* Test Project
*/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //ThreadsManager Instance
    private ThreadsManager mProducer;

    // Table List use as Array
    private List<String> TableListArray;
    //Adapter for Table
    private ArrayAdapter<String> TableAdapter;
    //List View for Layout ListView Table
    private ListView TableListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Instance of ThreadManager
        mProducer = mProducer.getInstance();

        //Prepare ListView for Table List Array and Adapter
        TableListView =  (ListView)  findViewById(R.id.TableListView);
        TableListArray = new ArrayList<String>();
        TableAdapter = new ArrayAdapter<String>(this,R.layout.table_row,TableListArray);
        TableListView.setAdapter(TableAdapter);
        TableListView.deferNotifyDataSetChanged();
    }


    //Make Producer And Put to Producer Pool
    public void Producer (View view) {
        //Check if List Array is send before and if not set it
        if(mProducer.getTableListArray()== null)
            mProducer.setTableListArray(TableListArray);

        //Check if List Adapter is send before and if not set it
        if(mProducer.getTableAdapter()== null)
            mProducer.setTableAdapter(TableAdapter);

        //Call Function Which put Producer to pool to produce Row
        mProducer.addRow(TableListView);
    }


    //Make Consumer And Put to Producer Pool
    public void Consumer (View view) {

        //Check if List Array is send before and if not set it
        if(mProducer.getTableListArray()== null)
            mProducer.setTableListArray(TableListArray);

        //Check if List Adapter is send before and if not set it
        if(mProducer.getTableAdapter()== null)
            mProducer.setTableAdapter(TableAdapter);

        //Call Function Which put consumer to pool to consume Row
        mProducer.RemoveRow(TableListView);
    }
}
