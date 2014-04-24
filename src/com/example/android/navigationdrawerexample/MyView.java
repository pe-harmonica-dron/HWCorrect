package com.example.android.navigationdrawerexample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.ImageView;
import android.widget.Toast;

public class MyView extends ImageView {  
   int curPageNo;
   
   DrawActivity mainactivity;
   
   /** ��Ϸ���� **/  
   Paint mPaint = new Paint();
   String comment = "";
   long curtime = 0, prevtime = 0;
   
   
   ArrayList<Bitmap> undobitmap = new ArrayList<Bitmap>();
   ArrayList<Bitmap> redobitmap = new ArrayList<Bitmap>();
   
   
   boolean hasCorrect = false;
   
   int color = 2;
   int size = 5;
   int bgcolor = Color.WHITE;
 
   
   /** ��Ϸ���� **/  
   public Canvas mCanvas = null;
   
   public Bitmap background = null;

   /**������Ϸѭ��**/  
   boolean mIsRunning = false;  
     
   /**��ͨ�ֱ�**/
   usualPen myUsualPen = new usualPen();
   
   
   static private double prex, prey, prev,curv;  
   
   
   int contr = 0, pointThreeFlag = 0;
   static int ini_v = 0, ini_width = 3, pointcnt = 0, prepointcnt = 0;
     
   public MyView(Context context, AttributeSet attrs) {  
       super(context, attrs);  
       /** ���õ�ǰViewӵ�п��ƽ��� **/
       this.setKeepScreenOn(true);
       this.setFocusable(true);  
       /** ���õ�ǰViewӵ�д����¼� **/  
       this.setFocusableInTouchMode(true);  
      /** �õ�SurfaceHolder���� **/  
       //mSurfaceHolder = this.getHolder();  
       /** ��mSurfaceHolder��ӵ�Callback�ص������� **/  
       //mSurfaceHolder.addCallback(this);  
       /** �������� **/
       mCanvas = new Canvas();
       /** �������߻��� **/
       mPaint = new Paint();    
       /**���û��ʿ����**/  
       mPaint.setAntiAlias(true);  
       /**���ʵ�����**/  
       mPaint.setFilterBitmap(true);
       mPaint.setStyle(Paint.Style.FILL);  

       mPaint.setColor(Color.RED);
       
   } 

   @Override  
   public boolean onTouchEvent(MotionEvent event) {
	   hasCorrect = true;
       /** �õ�������״̬ **/  
       int action = event.getAction();  
       double x = event.getX();  
       double y = event.getY();
       curtime = event.getEventTime();
       switch (action) {  
       // �������µ��¼�  
       case MotionEvent.ACTION_DOWN:  
       /**�������߹켣��� X Y����**/  
       	saveundolist();
    	prex = x;
    	prey = y;
    	prev = 0;
       	break;  
       // �����ƶ����¼�  
       case MotionEvent.ACTION_MOVE:  
       /**��������**/  
    	curv = Math.sqrt((x-prex)*(x-prex) + (y-prey)*(y-prey));
    	curv = curv / ((double)(curtime - prevtime)/1000);
    	if (prev == 0) prev = 1.5 * curv;
    	mCanvas.save();
    	myUsualPen.paintOncanvas(prex, prey, x, y, prev, curv, mPaint, mCanvas);
    	mCanvas.restore();
    	prex = x;
    	prey = y;
    	prev = curv;
    	setImageBitmap(background);
       	break;  
       // ����̧����¼�  
       case MotionEvent.ACTION_UP:  
       /**����̧������·���켣**/ 
       //mPath.reset();
    	curv = Math.sqrt((x-prex)*(x-prex) + (y-prey)*(y-prey));
    	curv = curv / ((double)(curtime - prevtime)/1000);
       	if (prev == 0) prev = 1.5 * curv;
       	curv = curv*40;
       	mCanvas.save();
       	myUsualPen.paintOncanvas(prex, prey, x, y, prev, curv, mPaint, mCanvas);
       	mCanvas.restore();
       	setImageBitmap(background);
       break;  
       }  
       prevtime = curtime;
       return true;  
   }  
   
     

   
   public void undo() {
	   if (undobitmap.isEmpty()) return;
	   redobitmap.add(background);
	   Bitmap tmp = undobitmap.get(undobitmap.size()-1);
	   undobitmap.remove(undobitmap.size()-1);
	   background = tmp;
   }
   
   public void redo() {
	   if (redobitmap.isEmpty()) return;
	   undobitmap.add(background);
	   Bitmap tmp = redobitmap.get(redobitmap.size()-1);
	   redobitmap.remove(redobitmap.size()-1);
	   background = tmp;
   }
   
   public void clear() {
	   saveundolist();
	   hasCorrect = false;
   	   background = null;
   }
   
   public void saveundolist() {
	   undobitmap.add(background);
	   if (undobitmap.size() > 12) undobitmap.remove(0);
       redobitmap.clear();
   }
   
   public void changecolor(int newcolor) {
   	   mPaint.setColor(newcolor);
   	//PathList.add(new Path());
   	//PaintList.add(mPaint[color]);
   	//mPath = PathList.get(PathList.size()-1);
   	//mTextPaint.setColor(mPaint[color].getColor());
   }
   
   public void changesize(int newsize) {
	   size = newsize;
   }
   
   public void changebg(int newcolor) {
	   saveundolist();
   	if(newcolor == 0)bgcolor = Color.BLACK;
   	if(newcolor == 1)bgcolor = Color.WHITE;
   	if(newcolor == 2)bgcolor = Color.RED;
   	if(newcolor == 3)bgcolor = Color.BLUE;
   	if(newcolor == 4)bgcolor = Color.GREEN;
   	if(newcolor == 5)bgcolor = Color.argb(255, 255, 0, 255);
   	if(newcolor == 6)bgcolor = Color.argb(255, 255, 255, 0);
   	if(newcolor == 7)bgcolor = Color.GRAY;
   	background = null;
   }
   


   
   public void save() {           
		Bitmap mbitmap = Bitmap.createBitmap(mainactivity.getWindowManager().getDefaultDisplay().getWidth(), mainactivity.getWindowManager().getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        if (background != null) mbitmap = background;
        else {
        	Canvas tmpcv = new Canvas(mbitmap);
        	tmpcv.drawColor(bgcolor);
        }
		
       File dir = new File("/mnt/sdcard/Drawer");
       if (!dir.exists()) {
       	dir.mkdir();
       }       
       String picname = ((Long)System.currentTimeMillis()).toString()+".png";
       try {
			FileOutputStream fos = new FileOutputStream(new File(dir.getPath() + "/" + picname));
			mbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			Toast toast=Toast.makeText(mainactivity.getApplicationContext(), "����Ϊ"+picname, Toast.LENGTH_SHORT);
			toast.show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
   }
   
   public Bitmap getCurPageBitmap() {
	   Bitmap mbitmap = Bitmap.createBitmap(mainactivity.getWindowManager().getDefaultDisplay().getWidth(), mainactivity.getWindowManager().getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
       if (background != null) mbitmap = background;
       else {
       	Canvas tmpcv = new Canvas(mbitmap);
       	tmpcv.drawColor(bgcolor);
       }
       return mbitmap;
	
   }

 }  
