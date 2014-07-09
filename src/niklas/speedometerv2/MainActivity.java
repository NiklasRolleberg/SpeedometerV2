package niklas.speedometerv2;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class MainActivity extends Activity {		
		
		//Nytt
		public static int height;
		public static int width;
		DrawView drawView;
		
		
		//saker som behövs till denna activity
		public double startlon=0;
		public double startlat=0;
		public double stoplon=0;
		public double stoplat=0;
		public static long hastighet;
		
		public double latskillnad;
		public double lonskillnad;
		public float timeskillnad;
		public float tid1;
		public float tid2;
		public float tid3;
		
		public long startTime;
		public long stopTime;
		public float tidsedanstart;
		public float uppstartsTime;
		public static float upptid;
		public float extratid;
		
		public static String kmM;
		public static String minS;
		
		public float varde1;
		public float varde2;
		public float varde3;
		public float lagom;
		
		public float rlat;
		public float rlon;
		public double radie=6371;
		public float rstartlat;
		public float rstoplat;
		
		public float a;
		public float c;
		public float d;
		public float m;
		public float m2;
		
		public float tids;
		public float speed;
		
		//public String hastigheten;
		//public String tiden;
		//public String distancen;
		
		/** Till distansräknaren*/
		public boolean OnOff= false; 
		public static float distance;
		
		public int StartDelay=0;
		public boolean Getstarttime=true;
		
		
		private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
	    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	    
	    protected LocationManager locationManager;
	    
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        
	        super.onCreate(savedInstanceState);
	        
	        DisplayMetrics metrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        height = metrics.heightPixels;
	        width = metrics.widthPixels;
	        
	      
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	        locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, 
	                MINIMUM_TIME_BETWEEN_UPDATES, 
	                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
	                new MyLocationListener()
	        );
	        
	        drawView = new DrawView(this);
	        setContentView(drawView);

	        
	    }

	    protected void showCurrentLocation() {

	        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);        
	        
	        if (location != null) 
	        {
	            String message = String.format(
	                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
	                    location.getLongitude(), location.getLatitude()
	            );
	            Toast.makeText(MainActivity.this, message,
	                    Toast.LENGTH_LONG).show();
	        }
	        
	        else
	        {
	        	Toast.makeText(MainActivity.this, "Location= Null",Toast.LENGTH_SHORT).show();
	        	
	        	if(locationManager.isProviderEnabled(LOCATION_SERVICE)==true)
	        	{
	        		Toast.makeText(MainActivity.this, "Servicen funkar",Toast.LENGTH_SHORT).show();
	        	}
	        	
	        	else
	        	{
	        		Toast.makeText(MainActivity.this, "Nej",Toast.LENGTH_SHORT).show();
	        	}
	        }

	    }   

	    public class MyLocationListener implements LocationListener 
	    {

	        public void onLocationChanged(Location location) 
	        {
	        	
	               	
	        	if (Getstarttime==true)
	            {
	         	   if (StartDelay==10)
	         	   {
	         		   Getstarttime= false;
	         		   uppstartsTime=System.currentTimeMillis();
	         	   }
	           }
	                       
	           stoplon=location.getLongitude();
	           stoplat=location.getLatitude();
	           stopTime = System.currentTimeMillis(); 	            	
	           hastighet= hastighetsraknare(startlon, startlat, stoplat, stoplon, startTime, stopTime);
	           
	                      
	           if (StartDelay>10)
	           {
	        	   distance= distanceraknare(startlon, startlat, stoplat, stoplon, startTime, stopTime);
	        	   upptid= tidsraknare(startTime, stopTime, uppstartsTime);
	           }
	           
	           
	           //Prefix fixande
	           if(distance>1000)
	           {
	        	   distance= ((int)(distance/100))/10f;
	        	   kmM=" km";  	   
	           }
	           
	           else
	           {
	        	   distance= (int)(distance*100);
	        	   distance = distance/100;
	        	   kmM=" m";
	           }
	           //*****
	           if(upptid<60)
	           {
	        	   minS=" s";
	           }
	           else
	           {
	        	   minS=" Min";
	        	   upptid= (int)upptid/60;
	           }
	           
	                 	  
	                     
	           //hastigheten= hastighet +" Km/h";
	           //distancen = distance+kmM;
	           //tiden =upptid+minS;
	           
	           
	           drawView.invalidate(); //<<>
	           
	           
	           startTime= stopTime; 
	           startlon=stoplon;
	       	   startlat=stoplat;
	       	   
	       	   StartDelay=StartDelay+1;
	        }
	        
	        /**Funktion som ska räkna ut distancen*/
	        public long hastighetsraknare(double startlon, double startlat, double stoplat, double stoplon, double startTime, double stopTime)
	        {
	        	
	        	
	        	/**Räknar ut massa saker!*/
	        	lonskillnad= (stoplon - startlon);
	        	latskillnad= (stoplat - startlat);
	        	timeskillnad= (float) (stopTime - startTime);
	        	
	        	
	        	rlat = (float) Math.toRadians(latskillnad);
	        	rlon = (float) Math.toRadians(lonskillnad);
	        	
	        	rstartlat = (float) Math.toRadians(startlat);
	        	rstoplat = (float) Math.toRadians(stoplat);
	        	       	
	        	a = (float) (Math.sin(rlat/2)*Math.sin(rlat/2)	+Math.cos(rstartlat)*Math.cos(rstoplat)* 
	        			   Math.sin(rlon/2)*Math.sin(rlon/2));
	        	
	        	c =(float) (2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
	        	
	        	d= (float) (radie*c); //Sträcka i km
	        	m = d*1000; //sträcka i meter
	        	
	        			
	        	tids= (timeskillnad/1000); //tid i sec
	        	
	        	speed =(float) ((m/tids)*3.6);
	        	
	        	/**Någonting som kanske kan förhindra att hastigheten "hoppar" för mycket */
	        	varde3=varde2;					
	        	varde2=varde1;					
	        	varde1=speed;
	        	
	        	lagom= (varde1+varde2+varde3)/3;    
	        	return (long) lagom;   	
	        }
	        
	        public Float distanceraknare(double startlon, double startlat, double stoplat, double stoplon, double startTime, double stopTime)
	        {
	        	/**Räknar ut massa saker!*/
	        	lonskillnad= (stoplon - startlon);
	        	latskillnad= (stoplat - startlat);
	        	timeskillnad= (float) (stopTime - startTime);

	        	rlat = (float) Math.toRadians(latskillnad);
	        	rlon = (float) Math.toRadians(lonskillnad);
	        	
	        	rstartlat = (float) Math.toRadians(startlat);
	        	rstoplat = (float) Math.toRadians(stoplat);
	        	       	
	        	a = (float) (Math.sin(rlat/2)*Math.sin(rlat/2)	+Math.cos(rstartlat)*Math.cos(rstoplat)* 
	        			   Math.sin(rlon/2)*Math.sin(rlon/2));
	        	c =(float) (2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
	        	
	        	d= (float) (radie*c); //Sträcka i km
	        	m = d*1000; //sträcka i meter
	        	
	        	Float stracksedanstart= m+m2;
	        	
	        	m2=stracksedanstart;

	        	return stracksedanstart;   	  	 
	        }
	        
	        public long tidsraknare(double startTime, double stopTime, double uppstartsTime)
	        {
	        	
	        	tid1= (float) (stopTime - startTime);
	        	tid2= tid1+tid3;
	        	tid3=tid2;
	        	lagom= tid2/1000;      	
	        	return (long) lagom;   	  	 
	        }
	  

	        public void onStatusChanged(String s, int i, Bundle b) {}

	        public void onProviderDisabled(String s) {
	            Toast.makeText(MainActivity.this,
	                    "Du stängde av gpen, det var inte bra!",
	                    Toast.LENGTH_LONG).show();
	        }

	        public void onProviderEnabled(String s) {
	            Toast.makeText(MainActivity.this,
	                    "Nu är gpsen på!",
	                    Toast.LENGTH_LONG).show();
	        }

	    }
	    
	    //@Override
	    //protected void onPause()
		//{
			// TODO Auto-generated method stub
	    	//System.exit(0);			//det gjorde så att gpsen stängdes av tror jag, eller så är den överflödig
	    	//finish();
	    	//System.out.println("Pausad");
		//}
	    
	    
	    //protected void onStop()
		//{
			// TODO Auto-generated method stub
	    	//System.exit(0);		//det gjorde så att gpsen stängdes av när activityn stoppades
	    	//finish();
		//}
	    
	    
	    protected void onDestroy()
		{
	    	System.exit(0);
			// TODO Auto-generated method stub
		}

	    
	    
	    
	    
	}

