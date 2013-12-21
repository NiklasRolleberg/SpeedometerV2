package niklas.speedometerv2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View{
    Paint paint = new Paint();
    
    
    int h=MainActivity.height;
    int b=MainActivity.width;
    float i;
    int j;
    double steg;
    String text;
    String speed;
    String time;
    String distance;
    double V;
    public static int skala=2;
    
    //double a; //För att få rätt  hastigheter runt
    
    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) { 	
        paint.setColor(Color.WHITE);
        
        //bakgrundscirkeln
        canvas.drawCircle(b/2, b/2+h/20, b/2, paint);
        
        //Extra ruta
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, h*0.6f, b, h, paint);
        
        paint.setColor(Color.RED);
        canvas.drawLine(0, h*0.6f+(h/8), b, h*0.6f+(h/8), paint);
        canvas.drawLine(0, h*0.85f, b, h*0.85f, paint);
        
        //Hastighetstext
        paint.setTextSize(h/10);
        speed=(Integer.toString((int)(MainActivity.hastighet))+ " Km/h");
        //paint.setColor(Color.RED);
        canvas.drawText(speed,b/6,h*0.6f+(h/10), paint);
        
        //Sträcka sedan start
        distance=Integer.toString((int)(MainActivity.distance))+MainActivity.kmM;
        canvas.drawText(distance,b/6,h*0.72f+(h/10), paint);
        
        //Tidstext
        time= Integer.toString((int)(MainActivity.upptid));
        canvas.drawText(time+MainActivity.minS,b/6,h*0.85f+(h/10), paint);
        
        
        //Hastigheter runt
        paint.setTextSize(b/20);
        i=(float) ((Math.PI/180)*125f);
        
        while (i<Math.PI*2.3){ 	
        	double a=(-125+(i)*180/Math.PI)/skala;
        	a=(Math.round(a/10)*10);
        	text=Integer.toString((int)(a));

        	//text=Integer.toString((-125+(int)((i)*180/Math.PI))/skala);
        	canvas.drawText(text,    (float)(b/2+(b/3)*Math.cos(i))-b/80,      (float)(b/2+(b/3)*Math.sin(i)+b/10),     paint);
        	i+=(Math.PI/180)*20*skala; //+20*	
        }
        
        //Småsträck
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        i=(float) ((Math.PI/180)*135f);
        while (i<Math.PI*2.3){
        				//   startX                                   StartY                                                 endX                                    endY                      
        	canvas.drawLine((float) (b/2+(b/2.3)*Math.cos(i)),(float)(b/2+h/20+(b/2.3)*Math.sin(i)),(float) (b/2+(b/2)*Math.cos(i)), (float)(b/2+h/20+(b/2)*Math.sin(i)), paint);
        	i+=(Math.PI/180)*10; //+10*
        }
        
        // 30,50,70,90
        paint.setStrokeWidth(4);
        paint.setColor(Color.RED);
        j=3;
        while(j<=9){
        	i=(float) ((Math.PI/180)*125f+(Math.PI/180)*10*j*skala);
        	canvas.drawLine((float) (b/2+(b/3)*Math.cos(i)),(float)(b/2+h/20+(b/3)*Math.sin(i)), (float) (b/2+(b/2)*Math.cos(i)), (float)(b/2+h/20+(b/2)*Math.sin(i)), paint);
        	j+=2;
        }
        
   
        //Visaren
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        V=(Math.PI/180)*(125+(MainActivity.hastighet*skala));
        canvas.drawLine(b/2, b/2+h/20,(float) (b/2+(b/2.2)*Math.cos(V)), (float)(b/2+h/20+(b/2.2)*Math.sin(V)), paint);
    }
}