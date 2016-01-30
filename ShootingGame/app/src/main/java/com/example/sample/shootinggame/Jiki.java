package com.example.sample.shootinggame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

//自機クラス
public class Jiki extends Object{

    public Jiki(){}
    public Jiki(float dw,float dh){
        super(dw,dh);
    }
    //初期設定
    public void Oint(Bitmap imgb,float x,float y, float sx,float sy,int w,int h,int tj){
        img = new BitmapDrawable(imgb);
        cx = ms.setSizeX(disp_w, x);
        cy = ms.setSizeY(disp_h, y);
        spx = sx;
        spy = sy;
        imgw = w;
        imgh = h;
        dead = false;
        //弾の初期状態を受け取ります
        tamajoutai = tj;
        atarir = new Rect((int)cx-30,(int)cy-30,(int)cx+30,(int)cy+30);
        obsyurui = 0;
    }

    public void ODraw(Canvas c){
		//画像表示
        if(dead == false){
            img.setBounds((int)(cx-imgw/2),(int)(cy-imgh/2),
                    (int)(cx+imgw/2),(int)(cy+imgh/2));
            //自機表示
            img.draw(c);
            //当たり判定表示
            OdrawRect(c);
        }
    }

    public void OMove(int x, int y) {
        float cxx = cx;
        float cyy = cy;
        cx = x;
        cy = y;
        //当たり判定更新
        atarir = new Rect((int)cx-30,(int)cy-30,(int)cx+30,(int)cy+30);

        if(OsotoX(imgw/2)==true) cx = cxx;
        if(OsotoY(imgh/2)==true) cy = cyy;
    }
    public void OMove() {}


    //タップ範囲して移動
    public Rect OgetTapRect(){
        Rect taprect = new Rect(
                img.getBounds().left-50,img.getBounds().top-50,
                img.getBounds().right+50,img.getBounds().bottom+50);
                //タップ時のみオブジェクトの大きさを多少大きくしてついてこれるように
        return taprect;
    }
    @Override
    public void Oint(Bitmap imgb, float x, float y, float sx, float sy, int w,int h) {}
}

