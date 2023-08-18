package afzal.mugrai.vo.avalanche;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class SettingActivity extends Activity {

    Button back;
    RadioGroup musicGroup;
    int musicGroupOption;
    RadioButton musicBttn;

    Handler handler = new Handler();

    MediaPlayer mediaPlayer;
    static boolean music = false;

    //This is the music intent control
    Intent svc;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setting);

        svc= new Intent(this, BackgroundSoundService.class);


        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        mediaPlayer.setVolume(100, 100);

        back = (Button) findViewById(R.id.backBttn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


        musicGroup = (RadioGroup) findViewById(R.id.musicControls);
        musicGroupOption = musicGroup.getCheckedRadioButtonId();
        musicBttn = (RadioButton) findViewById(musicGroupOption);
        //for two option radioButton
        //isChecked will look at the TRUE radioButton
        //WHICH IS INITALLY OFF
        musicBttn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                { //Play Music
                    // music = true;
                    if (music == false)
                        stopService(svc);
                    music = true;
                }
                else
                { //Stop Playing Music
                    //  music = false;
                    startService(svc);
                    music = false;
                }
            }
        });


    }



}
