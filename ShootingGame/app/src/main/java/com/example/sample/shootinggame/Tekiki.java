package com.example.sample.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

//敵機クラス
public class Tekiki extends Object{

    public Tekiki(){}
    public Tekiki(float dw,float dh){
        super(dw,dh);
    }

    @Override
    public void ODraw(Canvas c) {
		//画像表示
        if(dead == false){
            img.setBounds((int)(cx-imgw/2),(int)(cy-imgh/2),
                    (int)(cx+imgw/2),(int)(cy+imgh/2));
            //敵機表示
            img.draw(c);
            //当たり判定表示
            OdrawRect(c);
        }
    }



    @Override
    public void Oint(Bitmap imgb, float x, float y, float sx, float sy, int w,
                     int h, int tj) {
        img = new BitmapDrawable(imgb);
        cx = ms.setSizeX(disp_w, x);
        cy = ms.setSizeY(disp_h, y);
        spx = sx;
        spy = sy;
        imgw = w;
        imgh = h;
        dead = false;
        tamajoutai = tj;
        atarir = new Rect((int)cx-30,(int)cy-30,(int)cx+30,(int)cy+30);
        obsyurui = 1;
    }

    @Override
    public void OMove() {}

    @Override
    public void OMove(int x, int y) {}

    @Override
    public Rect OgetTapRect() {return null;}
    @Override
    public void Oint(Bitmap imgb, float x, float y, float sx, float sy, int w,
                     int h) {}

}

