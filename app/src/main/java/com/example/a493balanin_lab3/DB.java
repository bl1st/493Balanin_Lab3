package com.example.a493balanin_lab3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.a493balanin_lab3.model.Link;
import com.example.a493balanin_lab3.model.Node;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DB extends SQLiteOpenHelper
{
    //493 balanin
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE Graphs (id integer PRIMARY KEY AUTOINCREMENT,Name TEXT NOT NULL,date REAL NOT NULL);";
        db.execSQL(sql);
        sql="CREATE TABLE Nodes (id INTEGER PRIMARY KEY AUTOINCREMENT, graphID INTEGER NOT NULL,LocationX REAL NOT NULL, LocationY REAL NOT NULL,Text TEXT NOT NULL, FOREIGN KEY (graphID) REFERENCES Graphs(id));" ;
        db.execSQL(sql);
        sql="CREATE TABLE Links (id integer PRIMARY KEY AUTOINCREMENT, graphID INTEGER NOT NULL,NodeA INTEGER not null, NodeB INTEGER NOT NULL,value REAL NOT NULL, FOREIGN KEY (graphID) REFERENCES Graphs(id),FOREIGN KEY (NodeA) REFERENCES Nodes(id));";
        db.execSQL(sql);
    }

    public List<Graph> getGraphsList(){
        List<Graph> g = new ArrayList<Graph>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * From Graphs;";
        Cursor cur = db.rawQuery(sql,null);
        //493 balanin
        if (cur.moveToFirst() == true) {
            do {
                Graph gr = new Graph();
                gr.id = cur.getInt(0);
                gr.Name = cur.getString(1);
                gr.ts = cur.getInt(2);
                g.add(gr);
            }while (cur.moveToNext() == true);
        }
        return g;
    }
    public void deleteGraph(int id)
    {    //493 balanin
        String sql ="";
        //Удаляем старые сведения о графе
        SQLiteDatabase db = getWritableDatabase();
        sql = "DELETE FROM Links WHERE GraphId = "+ id +";";
        db.execSQL(sql);
        sql = "DELETE FROM Nodes WHERE GraphId = "+ id +";";
        db.execSQL(sql);
        sql = "DELETE FROM Graphs WHERE id = "+ id +";";
        db.execSQL(sql);
        Log.e("TEST","Graph " + id + " deleted from DB");
    }

    public Graph getGraphById(int id)
    {
        Graph g = new Graph();
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM Graphs WHERE id = "+ id +";";
        Cursor cur = db.rawQuery(sql,null);
        if (cur.moveToFirst()){
            g.id = cur.getInt(0);
            g.Name = cur.getString(1);
            g.ts = cur.getLong(2);
        }
        //493 balanin
        sql = "SELECT * FROM Nodes WHERE GraphId = "+ id +";";
        cur = db.rawQuery(sql,null);

        if (cur.moveToFirst() == true) {
            do {
                Node n = new Node(cur.getFloat(2),cur.getFloat(3));
                String text = cur.getString(4);
                if (text.equals("null")) text = null;
                n.setText(text);
                g.node.add(n);

            }while (cur.moveToNext() == true);
        }
        //493 balanin
        sql = "SELECT * FROM Links WHERE GraphId = '"+ id +"';";
        cur = db.rawQuery(sql,null);

        if (cur.moveToFirst() == true) {
            do {
                Link l = new Link(cur.getInt(2),cur.getInt(3));
                float value = cur.getFloat(4);
                l.setValue(value);
                g.link.add(l);

            }while (cur.moveToNext() == true);
        }
        return g;
    }
    //493 balanin
    public int addGraph(Graph g)  //returns Graph ID in Table
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO Graphs (Name,date) VALUES ('" + g.Name+ "',"+ g.ts+");";
        db.execSQL(sql);
        Log.e("TES","Inserted new graph");
        return getGraphID(g);
    }

    //493 balanin
    private int getGraphID(Graph g){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id from Graphs WHERE date = " +g.ts + ";";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()){
            Log.e("TEST","ID RETRIEVED SUCCESSFULL");
            return cur.getInt(0);
        }
        return -1;
    }
    //493 balanin
    public int saveGraph(Graph g)
    {
        deleteGraph(g.id); //удаляем граф
        g.ts = new Date().getTime();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO Graphs(id,Name,date) VALUES ("+ g.id + ",'"+ g.Name + "',"+ g.ts + ");";
        db.execSQL(sql);
        for (int i=0;i<g.node.size();i++)
        {
            Node n = g.node.get(i);
            sql = "INSERT INTO Nodes(graphID,LocationX,LocationY,Text) VALUES("+ g.id +","+ n.x +","+ n.y +",'"+ n.text +"');";
            db.execSQL(sql);
        }
        for (int i=0;i<g.link.size();i++)
        {
            Link l = g.link.get(i);
            sql = "INSERT INTO Links(graphID,NodeA,NodeB,value) VALUES("+ g.id +","+ l.a +","+ l.b +","+ l.value +");";
            db.execSQL(sql);
        }
        return getGraphID(g);
    }

    //493 balanin
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}