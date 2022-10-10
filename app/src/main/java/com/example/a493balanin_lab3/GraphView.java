package com.example.a493balanin_lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a493balanin_lab3.model.Link;
import com.example.a493balanin_lab3.model.Node;

public class GraphView extends SurfaceView {

    Graph g = new Graph();

    Paint p;
    //Если выбрано 2 узла то двигаем тот над которым мышка! опять запуск метода get_node_at_xy
    //На ребре нужно нарисовать квадрат. Если нажать на КВАДРАТ, то ребро выбирается
    int selected1 = -1;
    int selected2 = -1;
    int lastSelected = -1;
    boolean linkSelected = false;
    float rad = 40.0f;
    float squareRadius = 10.0f;

    float last_x;
    float last_y;
    TextView tv_info;

    public void link_selected_node(){ //создать связь
        if (linkSelected)  return;
        if (selected1 < 0) return;
        if (selected2 < 0) return;

        for(int i = 0; i < g.link.size();i++){
            Link l = g.link.get(i);
            if (selected1 == l.a && selected2 == l.b || selected2 == l.a && selected1 == l.b)
                return;
        }
        g.add_link(selected1,selected2);
        invalidate();
    }

    public int get_link_at_xy(float x,float y){

        for (int i = 0; i < g.link.size();i++){
            //ОТРИСОВКА КВАДРАТИКА НА ВЫБРАНОЙ СВЯЗИ
            Link l = g.link.get(i);
            boolean isTouched = l.squareIsTouched(x,y);
            if (isTouched)
                return i;
        }
        return -1;
    }


    public int get_node_at_xy(float x,float y){
        for (int i = g.node.size() - 1; i >= 0; i--)
        {
            Node n = g.node.get(i);
            float dx = x - n.x;
            float dy = y - n.y;

            if (dx * dx + dy * dy <= rad * rad) {
                return i;
            }
        }
        return -1;
    }


    public GraphView(Context context, AttributeSet attrs) {

        super(context, attrs);
        p = new Paint();
        p.setAntiAlias(true);

        //В последнюю очередь
        setWillNotDraw(false);
    }


    public void add_node(){
        g.add_node(100.0f,100.0f);
        //selected = g.node.size()-1;
        invalidate();
    }

    public void remove_node(){
        if (selected1 < 0) return;
        if (linkSelected) return;
        for (int i =0; i < g.link.size();i++)
        {
            Link l = g.link.get(i);
            if (l.a == selected1 || l.b == selected1)
            {
                g.link.remove(i);
            }
        }
        g.remove_node(selected1);
        selected1 = -1;
        invalidate();
    }
    public void changeNodeText(String text)
    {
        if (selected1 ==-1){
           Toast t = Toast.makeText(this.getContext(),"No nodes selected", Toast.LENGTH_SHORT);
           t.show();
           return;
        }
        Node n = g.node.get(selected1);
        n.text = text;
        invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.rgb(255,255,255));

        p.setColor(Color.argb(127,50,0,0));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(40.0f);
        for (int i=0; i < g.link.size(); i++)
        {
            Link l = g.link.get(i);
            Node na = g.node.get(l.a);
            Node nb = g.node.get(l.b);
            canvas.drawLine(na.x,na.y,nb.x,nb.y,p);

            float bx = (na.x + nb.x) * 0.5f;
            float by = (na.y + nb.y) * 0.5f;
            float x0 = bx - squareRadius;
            float x1 = bx + squareRadius;
            float y0 = by - squareRadius;
            float y1 = by + squareRadius;

            float xmid = (x0 +x1) /2;
            float ymid = (y0+y1) /2;

            l.setSquare(x0,x1,y0,y1);

            canvas.drawRect(l.x0,l.y0,l.x1,l.y1,p);

            double theta = Math.atan2(na.x - nb.x, na.y - nb.y);
            double offset = (50 - 2) * Math.cos(90); //50 - arrowSize

            canvas.drawLine(na.x, na.y, (int)(na.x - offset * Math.cos(theta)), (int)(na.y - offset * Math.sin(theta)),p);

        }

        for (int i = 0; i < g.node.size(); i++)
        {
            Node n = g.node.get(i);

            p.setStyle(Paint.Style.FILL);

            if (i == selected1) p.setColor(Color.argb(50,127,0,255));
            else if (i == selected2) p.setColor(Color.argb(50,255,0,50));
            else  p.setColor(Color.argb(50,0,127,255));

            canvas.drawCircle(n.x,n.y, rad,p);
            if (n.text != null)
                canvas.drawText(n.text,n.x, n.y + (2 * rad),p);

            p.setStyle(Paint.Style.STROKE);

            if (i == selected1) p.setColor(Color.rgb(127,0,255));
            else p.setColor(Color.rgb(0,127,255));
        }
        //super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                linkSelected = false;
                int i = get_link_at_xy(x,y);

                if (i > 0){ //link is touched
                    linkSelected = true;
                    return true;
                }

                i = get_node_at_xy(x,y);

                int s1 = -1;
                int s2 = -1;

                if (i < 0)
                {
                    selected1 = s1;
                    selected2 = s2;
                    lastSelected = -1;
                    invalidate();
                    return true;
                }

                if (i == selected1)
                {
                    s1= i;
                }
                if (selected1 >=0  && i != selected1)
                {
                    s2 = i;
                    selected2 = s2;
                    s1 = selected1;
                }
                if (selected2 < 0)
                {
                    s1 = i;
                }
                lastSelected = i;
                selected1 = s1;
                selected2 = s2;
                last_x = x;
                last_y = y;
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:

                if (lastSelected >= 0) {
                    Node n = g.node.get(lastSelected);
                    n.x += x - last_x;
                    n.y += y - last_y;
                    invalidate();
                }
                last_x = x;
                last_y = y;
                return true;

        }
        return super.onTouchEvent(event);

    }

}
