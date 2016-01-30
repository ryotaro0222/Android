package com.example.sample.shootinggame;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//メインループ
public class MainLoop extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    //ホルダー
    private SurfaceHolder holder;
    //スレッド
    private Thread thread;

    //どのActivityを使用しているかのための変数
    private MainActivity ma;
    private Mesod ms;
    private float disp_w,disp_h;
    private Bitmap jikibit,tamabit;

    //弾用、連続で重ならないようにの変数
    private boolean tamaflg;
    private int tamatime;
    //弾変化ボタン用
    private Rect tamabtn;

    //敵機画像用
    private Drawable tekiimg;
    private Bitmap tekibit;

    //自機,弾用のArrayList
    private ArrayList<Object> object = new ArrayList();

    //コンストラクタ
    public MainLoop(Context context) {
        super(context);
        init(context);
    }
    //コンストラクタ,xmlで呼ぶために使う
    public MainLoop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context){

        holder = getHolder();
        holder.addCallback(this);
        holder.setFixedSize(getWidth(), getHeight());

        ma = (MainActivity)context;
        ms = new Mesod();

        disp_w = ma.disp_w;
        disp_h = ma.disp_h;

        //画像登録用
        Resources resources = context.getResources();

        //ビットマップ方式で画像取り込み
        Bitmap img= BitmapFactory.decodeResource(resources,R.drawable.jiki);

        //画像分割
        //自機
        jikibit = Bitmap.createBitmap(img,0,0,img.getWidth()/4,img.getHeight());
        //敵機
        tekibit = Bitmap.createBitmap(img,img.getWidth()/4*2,0,img.getWidth()/4,img.getHeight());
        //弾
        tamabit = Bitmap.createBitmap(img,img.getWidth()/4,0,img.getWidth()/4,img.getHeight());
		/*
		 * Onjectクラスではインスタンス（実装）できないので
		 * ObjectクラスをextendsさせたJikiクラスを実装
		 * ArrayListを使用しているため、addでインスタンスしています
		 * メソッドなどを使用する場合はget(インデックス).でメソッドなど
		 * 色々呼び出したりします。
		 * 今回はArrayListの０番目の要素に自機が入っています
		 */
        object.add(new Jiki(disp_w, disp_h));
        object.get(0).Oint(jikibit, 240, 800, 0, 0, jikibit.getWidth(), jikibit.getHeight(),0);

        //弾ボタン用座標
        tamabtn = new Rect(0,0,50,50);

        //ランダムで敵機１０機作成
        Random r = new Random(new Date().getTime());
        for(int i=0;i<10;i++){
            int x = r.nextInt((int) (disp_w-50));
            int y = r.nextInt((int) (disp_h/2));
            object.add(new Tekiki(disp_w,disp_h));
            object.get(object.size()-1).
                    Oint(tekibit, x, y, 0, 0, tekibit.getWidth(), tekibit.getHeight(),0);
        }

    }

    //メインループ
    public void run() {

        Canvas c;
        Paint p = new Paint();
        p.setAntiAlias(true);

        while(thread != null){
            c = holder.lockCanvas();

            c.drawColor(Color.BLACK);

            //弾変化ボタン
            p.setColor(Color.BLUE);
            c.drawRect(tamabtn, p);
            p.setTextSize(30);
            c.drawText("tama:"+object.get(0).tamajoutai, 50, 50, p);

			/*
			 * 自機も弾も同じObjectの要素を持っているので
			 * インスタンス（実装）時に作成したいクラスを
			 * 指定しておけば、同じObjectクラスとして使用
			 * することができます。わざわざ各オブジェクトの
			 * メソッドを呼ぶのではなく、共通しているObjectの
			 * メソッドを呼ぶことで解決しています
			 */

            for(int i=0;i<object.size();i++){
                object.get(i).ODraw(c);
                object.get(i).OMove();
                //当たり判定
                Atarihantei(i);

                //弾が画面外に出たら要素を消す
                if(object.get(i).Ogetdead()==true) object.remove(i);
            }

            holder.unlockCanvasAndPost(c);//お決まり

            try {
                //スレッド実行時間
                Thread.sleep(20);
            } catch (Exception e){}
        }
    }

    //タッチイベント
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch(action){
            case MotionEvent.ACTION_DOWN:

                if(ms.RectTap(x, y, object.get(0).OgetTapRect()) == true){
                    //弾の状態
                    Tamajoutai();
                }
			    //弾の状態を変化させるボタン
                if(ms.RectTap(x, y, tamabtn)==true){++object.get(0).tamajoutai;
                    object.get(0).tamajoutai = (object.get(0).tamajoutai+3)%3;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                if(ms.RectTap(x, y, object.get(0).OgetTapRect()) == true) object.get(0).OMove(x, y);
                break;
        }
        return true;
    }

    /*
     * メインループで表示移動メソッドが呼び出されているオブジェクト
     * をナンバー(i)で受け取り、その他と総当りで判定
     * 同じオブジェクト以外と同じ種類のオブジェクト以外なら判定
     * この場合自機と自機弾は同じ種類としています
     * Mesodクラスの短形同士の当たり判定がtrueなら比べたオブジェクト
     * 両方共に消す
     */
    //当たり判定
    public void Atarihantei(int i){
        for(int j=0;j<object.size()-1;j++){
            if(i!=j && object.get(i).obsyurui != object.get(j).obsyurui){
                if(ms.RectRect(object.get(i).atarir, object.get(j).atarir)==true){
                    object.get(i).dead=true;
                    object.get(j).dead=true;
                }
            }

        }
    }

    //弾の状態
    public void Tamajoutai(){
        //通常の１発
        if(object.get(0).tamajoutai == 0){
            object.add(new JiTama(disp_w,disp_h));
            object.get(object.size()-1).Oint(
                    tamabit, object.get(0).cx, object.get(0).cy-jikibit.getHeight(),
                    0, 30, tamabit.getWidth(), tamabit.getHeight(),0);
        }
        //２発並ぶ
        if(object.get(0).tamajoutai == 1){

            object.add(new JiTama(disp_w,disp_h));
            object.get(object.size()-1).Oint(
                    tamabit, object.get(0).cx-20, object.get(0).cy-jikibit.getHeight(),
                    0, 30, tamabit.getWidth(), tamabit.getHeight(),0);
            object.add(new JiTama(disp_w,disp_h));
            object.get(object.size()-1).Oint(
                    tamabit, object.get(0).cx+20, object.get(0).cy-jikibit.getHeight(),
                    0, 30, tamabit.getWidth(), tamabit.getHeight(),0);
        }
		/*
		 * 36度づつ自機の周りに１０発一気に出ます
		 *
		 * ちょっと改造しました。自機を中心にちょっと離れた場所
		 * から弾が発射されるようにするために、弾の初期位置の角度
		 * （360/10）からその角度にsinでx座標cosでy座標に進む座標
		 * の最小値が出るので、一定の数値を掛ける（55）、そして
		 * 自機の中心座標にそれぞれ足したりすることで自機の中心から
		 * 少し離れた場所から発射されるようになりました。
		 */
        //自機の周りに10発
        if(object.get(0).tamajoutai == 2){
            for(int i=0;i<10;i++){
                int r = i*(360/10);
                float rx = (float) (Math.sin(ms.toRadian(r))*55);
                float ry = (float) (Math.cos(ms.toRadian(r))*55);
                object.add(new JiTama(disp_w,disp_h));
                object.get(object.size()-1).Oint(
                        tamabit, object.get(0).cx+rx, object.get(0).cy-ry,
                        30, 30, tamabit.getWidth(), tamabit.getHeight(),r);
            }

        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}//お決まり

    public void surfaceCreated(SurfaceHolder arg0) {thread = new Thread(this);thread.start();}//お決まり

    public void surfaceDestroyed(SurfaceHolder arg0) {thread = null;}//お決まり

}


