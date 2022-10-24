package com.example.a493balanin_lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GraphView gv;
    DB db;//493 balanin
    Intent j;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id)
        {
            case R.id.item_graphs:
                Intent i = new Intent(this, Activity_Graphs.class);
                startActivityForResult(i,1);
                break;
            case R.id.item_changeName:
                final String[] name = new String[1];
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Title");
                alert.setMessage("Insert new graph name\n(this operation will save this in db)");
                final EditText input = new EditText(this);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        name[0] = input.getText().toString();
                        gv.g.Name = name[0];
                        db.saveGraph(gv.g);
                        gv.invalidate();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { } });
                alert.show();
                break;

            case R.id.item_deleteGraph:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Graph delete");
                alertDialog.setMessage("Are you sure you want to delete this graph?\nThis operation will delete graph from database");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.deleteGraph(gv.g.id);
                        gv.g.node.clear();
                        gv.g.link.clear();
                        gv.invalidate();
                        startActivityForResult(j,1);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { }  });
                alertDialog.show();
                break;

            case R.id.item_copyGraph:  //saves copied graph to DB
                db.saveGraph(gv.g);
                Graph g = gv.g.copyGraphNoReference();
                g.id = db.addGraph(g);
                gv.g = null;
                gv.g = g;
                Toast toast = Toast.makeText(this, "created copy of this graph\nswitched to copy", Toast.LENGTH_SHORT);
                toast.show();
                gv.invalidate();
                db.saveGraph(gv.g);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        j = new Intent(this, Activity_Graphs.class);
        db = new DB(this,"Graphs.db",null, 1);
        gv = findViewById(R.id.gv);
        startActivityForResult(j,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    { //493 balanin
        try
        {
            if (requestCode == 1  && resultCode  == RESULT_OK)
            {
                int graphID = data.getIntExtra("graphID",-1);
                Graph g = db.getGraphById(graphID);
                gv.g = g;
                gv.invalidate();
            }
        }
        catch (Exception ex)
        {
           Log.e("TEST","SOMETHING WENT WRONG ON ONACTIVITYRESULT");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onButtonClick(View v)
    {
        gv.add_node();
    }

    public void onRemoveClick(View v)
    {
        if (gv.linkSelected){
            gv.remove_link();
        }

        gv.remove_node();
    }

    public void onAddLinkButton_Click(View v)
    {
        gv.link_selected_node();
    }
    //493 balanin
    public void onButtonSaveGraph_Click(View v)
    {
        gv.g.id = db.saveGraph(gv.g);
        Log.e("TEST","Graph saved");
    }

    public void onButtonClear_Click(View v)
    {
        gv.g.node.clear();
        gv.g.link.clear();
        gv.invalidate();
    }
    //493 balanin
    public void onButtonRenameNode_Click(View v)
    {
        final String[] name = new String[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change node text");
        alert.setMessage("Insert new node text");
        //Create TextView
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                name[0] = input.getText().toString();
                gv.changeNodeText(name[0]);
            }
        });//493 balanin

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { } });
        alert.show();
    }

    public void onButtonEditLink_Click(View v)
    {
        final float[] value = new float[1];
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Link value");
        alert.setMessage("Insert new link value");
        //Create TextView
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setView(input);
        //493 balanin
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                value[0] =  Float.parseFloat(input.getText().toString());
                gv.changeLinkValue(value[0]);
                gv.invalidate();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { } });
        alert.show();

    }

}