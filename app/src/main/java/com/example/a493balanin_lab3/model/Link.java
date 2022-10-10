package com.example.a493balanin_lab3.model;

public class Link {
    public int a,b;
    public String text;
    public float x0,x1,y0,y1; //квадрат для нажатия

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

   public void setText(String text){

       this.text = text;
   }

   public boolean squareIsTouched(float x,float y)
   {
       return x > x0 && x < x1 && y > y0 && y < y1;
   }
}
