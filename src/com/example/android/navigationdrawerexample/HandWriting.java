package com.example.android.navigationdrawerexample;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

abstract class Handwriting
{
	protected double speedrate;//�ٶȱ��˲���
	protected double POINTDISTRATE = 40;
	////����SAI���߷�����ġ�����ֱ��/Բ�ļ�ࡱ
	public abstract double getSizeFromSpeed(double v);
	abstract void paintOncanvas(double x1, double y1, double x2, double y2, double v1, double v2, Paint paint, Canvas canvas);
};

class usualPen extends Handwriting
{
	private double useSize1,useSize2,dist, deltaSize, deltaPixel, deltaX, deltaY, useSize, pointx, pointy;
	private double ovalWidth, ovalHeight, ovalX, ovalY;
	private Random random = new Random();
	private double size;
	RectF oval = new RectF();
	public usualPen()
	{
		speedrate = 10;
	}
	public double getSizeFromSpeed(double v)
	{
		size = Math.exp(-1*v/2000);
		size = size * speedrate;
		if (size > 10) size = 10;
		return size;
	}
	public void paintOncanvas(double x1, double y1, double x2, double y2, double v1, double v2, Paint paint, Canvas canvas)
	{
		useSize1 = getSizeFromSpeed(v1);
		useSize2 = getSizeFromSpeed(v2);
		useSize = useSize1;
		pointx = x1;
		pointy = y1;
		dist = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		//�����ƶ�����
		deltaSize = (useSize2 - useSize1) / POINTDISTRATE;
		deltaPixel = (useSize1 / POINTDISTRATE > 0.5? 
			useSize1/POINTDISTRATE: 0.5);
		deltaX = ((double)(x2-x1) * deltaPixel) / dist;
		deltaY = ((double)(y2-y1) * deltaPixel) / dist;
		//��֤�����ĵ������ �յ�֮��
		while(((x2 - pointx) * deltaX) >= 0 &&
			((y2 - pointy) * deltaY) >= 0)
		{
			pointx += deltaX;
			pointy += deltaY;
			ovalX = pointx - (useSize/2) - ((double)(random.nextInt() % 32767)/32767.0-0.5)*(useSize / 20);
			ovalY = pointy - (useSize/8) - ((double)(random.nextInt() % 32767)/32767.0-0.5)*(useSize / 20);
			ovalWidth = useSize - ((double)(random.nextInt() % 32767)/32767.0-0.5)*(useSize / 20);
			if (ovalWidth < 0) ovalWidth = -ovalWidth;
			ovalHeight = useSize/4 - ((double)(random.nextInt() % 32767)/32767.0-0.5)*(useSize / 20);
			if (ovalHeight < 0) ovalHeight = -ovalHeight;
			oval.set((int)(ovalX - ovalWidth/2), (int)(ovalY + ovalHeight/2), (int)(ovalX + ovalWidth/2), 
						(int)(ovalY - ovalHeight/2));
			canvas.drawOval(oval, paint);
			useSize += deltaSize;
		}
	}
}