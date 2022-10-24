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
//493 balanin
    Graph g = new Graph();
    Paint p;
    int selected1 = -1;
    int selected2 = -1;
    int lastSelected = -1;
    boolean linkSelected = false;
    int linkID = -1;
    float rad = 40.0f;
    float squareRadius = 10.0f;
    float last_x;
    float last_y;

    public void link_selected_node(){ //создать связь
        if (linkSelected)  return;
        if (selected1 < 0) return;
        if (selected2 < 0) return;

        for(int i = 0; i < g.link.size();i++)
        {
            Link l = g.link.get(i);
            if (selected1 == l.a && selected2 == l.b)
                return;
        }
        g.add_link(selected1,selected2);
        invalidate();
    }

    public int get_link_at_xy(float x,float y){

        for (int i = g.link.size()-1; i >=0 ;i--){
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
    public void remove_link(){
       g.link.remove(linkID);
        invalidate();
    }
    public void changeNodeText(String text)  //493 balanin
    {
        if (selected1 ==-1 || linkSelected == true){
           Toast t = Toast.makeText(this.getContext(),"No nodes selected\nor link selected", Toast.LENGTH_SHORT);
           t.show();
           return;
        }
        Node n = g.node.get(lastSelected);
        n.text = text;
        invalidate();
    }
    public void changeLinkValue(float value)
    {
        if (linkSelected)
        {
            Link l = g.link.get(linkID);
            l.setValue(value);
            invalidate();
        }
        else{
            Toast t = Toast.makeText(this.getContext(),"No nodes selected\nor link selected", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    { //493 balanin
        canvas.drawColor(Color.rgb(255,255,255));

        p.setColor(Color.argb(127,50,0,0));
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(40.0f);
        for (int i=0; i < g.link.size(); i++)
        {
            Link l = g.link.get(i);
            Node na = g.node.get(l.a);
            Node nb = g.node.get(l.b);

            float lowCat = Math.abs(nb.x - na.x);
            float highCat = Math.abs(nb.y -na.y);

            float gip = (float) Math.sqrt(lowCat * lowCat + highCat * highCat);
            float cos = lowCat/gip;
            float sin = highCat/gip;

            float xa = cos;
            float ya = sin;
            float xb = -xa;
            float yb = -ya;

            float angle = (float) Math.acos(cos);

            float deviationUp = (float) (angle + Math.PI / 7);
            float deviationDown = (float) (angle - Math.PI / 7);

            float arrow1_cos = (float) Math.cos(deviationUp);
            float arrow1_sin = (float) Math.sin(deviationUp);
            float arrow2_cos = (float) Math.cos(deviationDown);
            float arrow2_sin = (float) Math.sin(deviationDown);

            if (na.x > nb.x){
                xa *=-1;
                xb *=-1;
                arrow1_cos *= -1;
                arrow2_cos *=-1;
            }
            if (na.y > nb.y)
            {
                ya *=-1;
                yb*=-1;
                arrow1_sin *=-1;
                arrow2_sin *=-1;
            }

            float na_x = na.x + rad * xa;
            float na_y = na.y + rad * ya;
            float nb_x = nb.x + rad * xb;
            float nb_y = nb.y + rad * yb;

            float arrow1_x = nb_x + (rad * 1.5f) * (-1 * arrow1_cos);
            float arrow1_y = nb_y + (rad * 1.5f) * (-1 * arrow1_sin);
            float arrow2_x = nb_x + (rad * 1.5f) * (-1 * arrow2_cos);
            float arrow2_y = nb_y + (rad * 1.5f) * (-1 * arrow2_sin);

            canvas.drawLine(nb_x,nb_y,arrow1_x,arrow1_y,p);
            canvas.drawLine(nb_x,nb_y,arrow2_x,arrow2_y,p);
            //connecting line
            canvas.drawLine(na_x,na_y, nb_x,nb_y,p);

            float bx = (na.x + nb.x) * 0.5f;
            float by = (na.y + nb.y) * 0.5f;
            float x0 = bx - squareRadius;
            float x1 = bx + squareRadius;
            float y0 = by - squareRadius;
            float y1 = by + squareRadius;

            l.setSquare(x0,x1,y0,y1);
            p.setStyle(Paint.Style.FILL);
            if (linkID == i){ //493 balanin
                p.setColor(Color.argb(255,128,128,0));
            }
            else
            {
                p.setColor(Color.argb(50,50,0,0));
            }
            canvas.drawRect(l.x0,l.y0,l.x1,l.y1,p);
            float x_value = (l.x0 + l.x1) /2;
            float y_value = (l.y0 + l.y1) /2;
            p.setColor(Color.argb(64,50,0,0));
            if (l.value != 0.0f){
                canvas.drawText(String.valueOf(l.value),x_value, (float) (y_value + (rad*1.5)),p);
            }
            p.setColor(Color.argb(127,50,0,0));



        }

        for (int i = 0; i < g.node.size(); i++)
        {
            Node n = g.node.get(i);
            p.setStyle(Paint.Style.FILL);

            if (i == selected1) p.setColor(Color.argb(50,127,0,255));
            else if (i == selected2) p.setColor(Color.argb(50,255,0,50));
            else  p.setColor(Color.argb(50,0,127,255));

            canvas.drawCircle(n.x,n.y, rad,p);
            if (n.text != null){
                canvas.drawText(n.text,n.x, n.y + (2 * rad),p);
            }

            p.setStyle(Paint.Style.STROKE);

            if (i == selected1) p.setColor(Color.rgb(127,0,255));
            else p.setColor(Color.rgb(0,127,255));

            canvas.drawCircle(n.x,n.y, rad,p);
        }
        //super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    { //493 balanin
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                linkSelected = false;
                int i = get_link_at_xy(x,y);

                if (i > -1){ //link is touched
                    linkSelected = true;
                    linkID = i;
                    invalidate();
                    return true;
                }
                i = get_node_at_xy(x,y);
                linkID = -1;
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
