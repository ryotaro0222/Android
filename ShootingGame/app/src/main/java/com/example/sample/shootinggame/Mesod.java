package com.example.sample.shootinggame;

import android.graphics.Rect;
import android.media.MediaPlayer;

//メソッドクラス
public class Mesod {
	/*
	 * わたくしの環境がXPERIAでその画面幅でアプリを作成
	 * しているのでそれから各アプリの画面幅に合うように調整
	 * させるための変数
	 */
    static public final float XPERIA_W = 1200f;
    static public final float XPERIA_H = 1824f;
    //せっかくなので０も固定値に
    static public final float ZERO = 0f;
    private static final double PIE = 3.1415926;


    //渡された短形と短形の当たり判定。重なっていればtrue
    public boolean RectRect(Rect oa,Rect ob){
        return oa.left < ob.right && ob.left < oa.right && oa.top < ob.bottom && ob.top < oa.bottom;
    }

    /*
     * sin,cosなどを使用するときに入れ込む数値は
     * 3.14を半周とした数値を180で割ったラジアン値
     * というものを使用しなければなりません。
     * （１周3.14×2=6.28を360で割った数値）
     * 角度設定などは度数で出したほうが簡単なので設定は
     * 度数でして、使用するときにここのメソッドでラジアン値
     * に変換しています
     */
    public double toRadian(double deg){return (deg * PIE / 180);}
    /*
     * 受け取ったxy座標と調べたい短形範囲が重なっているかいないか
     */
    public boolean RectTap(int x,int y,Rect gazou){
        return gazou.left < x && gazou.top < y && gazou.right > x && gazou.bottom > y;
    }
    /*
     * この２行で各座標を実装機種の画面比に合わせます
     */
    public int setSizeX(float disp_w,float zahyou){return (int) (zahyou * (disp_w / XPERIA_W));}
    public int setSizeY(float disp_h,float zahyou){return (int) (zahyou * (disp_h / XPERIA_H));}
}

