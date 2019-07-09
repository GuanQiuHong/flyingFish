package william.hong.flyingfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private Button StartGameAgain;
    private TextView DisplayScore;
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //initialize the button to connect it to .xml button
        StartGameAgain = (Button) findViewById(R.id.play_again_btn);
        //get the score from the mainActivity class
        score = getIntent().getExtras().get("score").toString();


        //connect .xml displayScore text to a TextView object here, so we can modify it
        DisplayScore = (TextView) findViewById(R.id.displayScore);
        //set it to be ready for clicks, callback function when Play Again is clicked:
        StartGameAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch from gameOver page to "MainActivity.class", where we play the game again
                Intent mainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        //here we set what text to display alongside score int
        DisplayScore.setText("Score: " + score);
    }
}


/* Once again there's .xml modifications but idk how to comment .xml so here we go
Lines 10-24: Describes the big red letters, Game Over
Lines 25-39: Describes the button with red background, Play Again
 */