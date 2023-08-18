package afzal.mugrai.vo.avalanche;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

    Button play, setting, help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.playBttn);
        setting = (Button) findViewById(R.id.settingBttn);
        help = (Button) findViewById(R.id.helpBttn);

        //initially
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent("afzal.mugrai.vo.avalanche.GameActivity"));
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent("afzal.mugrai.vo.avalanche.SettingActivity"));
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent("afzal.mugrai.vo.avalanche.HelpActivity"));
            }
        });


    }

}
