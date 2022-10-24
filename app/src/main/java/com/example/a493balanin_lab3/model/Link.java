package com.example.a493balanin_lab3.model;

public class Link {
    public int a,b;
    public float value;
    public float x0,x1,y0,y1; //квадрат для нажатия
//493balanin
   public Link(int x, int y){
       a = x;
       b = y;

   }

   public void setSquare(float x0,float x1, float y0,float y1)
   {
       this.x0 = x0;
       this.x1 = x1;
       this.y0 = y0;
       this.y1 = y1;
   }



   public boolean squareIsTouched(float x,float y)
   {
       return x > x0 && x < x1 && y > y0 && y < y1;
   }

   public void setValue(float value){
       this.value = value;
   }
}
