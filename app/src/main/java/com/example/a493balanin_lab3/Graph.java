package com.example.a493balanin_lab3;

import com.example.a493balanin_lab3.model.Link;
import com.example.a493balanin_lab3.model.Node;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Graph {
    public String Name;
    public int id;
    public long ts;
    ArrayList<Node> node = new ArrayList<Node>();
    ArrayList<Link> link = new ArrayList<Link>();
    public void add_node(float x, float y)
    {
        node.add(new Node(x,y));
    }
    public void remove_node(int index)
    {
        if (index <0) return;
        node.remove(index);
    }

    public void add_link(int x, int y){
        if (x < 0 || y < 0) return;
        for (int i =0; i < link.size(); i++){
            Link l = link.get(i);
            if (x == l.a && y == l.b) return;
        }
        link.add(new Link(x,y));
    }

    @Override
    public String toString()
    {
        Timestamp stamp = new Timestamp(this.ts);
        Date date = new Date(stamp.getTime());
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        return this.Name;
       // return this.Name + "\n" + df.format(date);
    }
    public Graph copyGraphNoReference(){
        Graph g = new Graph();
        g.Name = this.Name + " copy";
        g.ts = new java.util.Date().getTime();
        g.node = new ArrayList<Node>();
        for (int i =0; i < this.node.size();i++)
        {
            Node n1 = this.node.get(i);
            Node n2 = new Node(n1.x,n1.y);
            n2.text = n1.text;
            g.node.add(n2);
        }
        g.link = new ArrayList<Link>();
        for (int i =0; i < this.link.size();i++)
        {
            Link l1 = this.link.get(i);
            Link l2 = new Link(l1.a,l1.b);
            l2.value = l1.value;
            g.link.add(l2);
        }
        return g;
    }





}
