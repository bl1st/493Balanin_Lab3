package com.example.a493balanin_lab3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Activity_Graphs extends AppCompatActivity {

    DB db;
    ListView lv;
    List<Graph> gList;
    ArrayAdapter<Graph> adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        db = new DB(this,"Graphs.db",null, 1);

        gList= db.getGraphsList();
        adp = new ArrayAdapter<Graph>(this,
                android.R.layout.simple_list_item_1, gList);

        lv = findViewById(R.id.lv_graphs);
        lv.setAdapter(adp);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {
                        int graphID = adp.getItem(position).id; //adrp get item returns graph which contains id field
                        Intent i = new Intent();
                        i.putExtra("graphID",graphID);
                        setResult(RESULT_OK,i);
                        finish();

                    }
                }
        );

    }


    public void onButtonCreate_Click(View v){
        //MessageBox - tv with name (ok,cancel)
        final String[] name = new String[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Title");
        alert.setMessage("Insert new graph name");
        //Create TextView
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                name[0] = input.getText().toString();
                // Do something with value!

                Graph g = new Graph();
                g.Name = name[0];
                g.ts = (int)(System.currentTimeMillis() /1000);
                int id =db.addGraph(g);
                g.id = id;
                gList.add(g);
                adp.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();

    }
}