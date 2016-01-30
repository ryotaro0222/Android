package com.example.sample.shootinggame;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    public float disp_w,disp_h;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //タイトルバーを非表示にする
        requestWindowFeature(Window.FEATURE_NO_TITLE);

       //フルスクリーン表示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager manager =  (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = manager.getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);

        disp_w = size.x;
        disp_h = size.y;

        setContentView(new MainLoop(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
