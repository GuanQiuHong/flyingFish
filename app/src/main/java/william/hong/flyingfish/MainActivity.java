package william.hong.flyingfish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //declaration of new object
    private FlyingFishView gameView;
    private android.os.Handler handler = new android.os.Handler();
    private final static long Interval = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); replaced with FlyingFishView

        gameView = new FlyingFishView(this);
        setContentView(gameView);

        //this piece of code enables actual moving up and down of the fish; the motions; without this block, fish is stuck in initial position,
        //no movement, just stays there.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        }, 0, Interval);
    }
}
