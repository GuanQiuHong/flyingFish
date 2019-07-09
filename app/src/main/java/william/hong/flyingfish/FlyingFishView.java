package william.hong.flyingfish;

//this class will _extend_ the View where we will draw the fish and the background and the wings of the fish
//allow the fish to fly up in the air.

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FlyingFishView extends View {

    //declare variables that we'll later define
    private Bitmap fish[] = new Bitmap[2];
    //slightly to the right of the left edge of screen
    private int fishX = 10;
    private int fishY;
    private int fishSpeed;

    private int canvasWidth, canvasHeight;

    //for the yellow balls that increase score
    private int yellowX, yellowY, yellowSpeed = 16;
    private Paint yellowPaint = new Paint();


    //green balls give more score than yellow balls
    private int greenX, greenY, greenSpeed = 20;
    private Paint greenPaint = new Paint();

    //red balls decrease player lifeCounter
    private int redX, redY, redSpeed = 10;
    private Paint redPaint = new Paint();

    private int score, lifeCounterOfFish;

    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    //for the red and gray heart images
    private Bitmap life[] = new Bitmap[2];

    public FlyingFishView(Context context) {
        super(context);

        //initialize the images: fish, heart, grey heart
        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1); //closed wings
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2); //open wings

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);
        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

        //initialize starting Y position to be somewhere near centre of screen
        fishY = 550;
        score = 0;
        //initialize the fish to have 3 lives
        lifeCounterOfFish = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //get width and height of phone screen
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        //actual drawing of images: draws fish, hearts, background image, text Score:
        //this is like draw_main_canvas from 297
        //on the entirety of the phone screen we put the background image.
        canvas.drawBitmap(backgroundImage, 0, 0, null);

        //define upper and lower bounds for fish location vertically
        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 3;
        //fish is "dropping" vertically due to gravity; therefore...
        fishY = fishY + fishSpeed; //Y position increments

        //don't let fish go beyond its allowed boundaries
        if (fishY < minFishY) {
            fishY = minFishY;
        }
        if (fishY > maxFishY) {
            fishY = maxFishY;
        }

        //this is to simulate acceleration due to gravity
        fishSpeed = fishSpeed + 2;

        //if user tapped screen...
        if (touch) {
            //draw the open-wing fish, flying fish
            canvas.drawBitmap(fish[1], fishX, fishY, null);
            //toggle touch to false
            touch = false;
        } else {
            //if no touch, then draw the closed-wing, typical fish
            canvas.drawBitmap(fish[0], fishX, fishY, null);
        }


        //so the balls would move towards the left
        yellowX = yellowX - yellowSpeed;
        greenX = greenX - greenSpeed;
        redX = redX - redSpeed;
        //whenever fish gets a yellow ball, score increases by 10.
        if (hitBallChecker(yellowX, yellowY)) {
            score = score + 10;
            //to simply force the ball off-screen
            yellowX = - 100;
        }

        //whenever fish gets green ball, score + 20, moved off screen to respawn
        if (hitBallChecker(greenX, greenY)) {
            score = score + 20;
            greenX = - 100;
        }

        if (hitBallChecker(redX, redY)) {
            //replace heart image with gray heart png
            lifeCounterOfFish--; //decrease fish life counter
            if (lifeCounterOfFish == 0) {
                //this displays "game over" in a box very briefly on the GameOverActivity page
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();

                Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);

                //what does this do? Screen transitions even without the .addFlag line...
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //the idea here is we want to carry the score the user obtained when they lose, into the gameOverActivity
                gameOverIntent.putExtra("score", score);
                getContext().startActivity(gameOverIntent);
            }
            redX = -100;
        }

        //how balls are generated again after they go offscreen, is that it gets moved to the RHS again, move towards left
        if (yellowX < 0) {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY) + minFishY);
        }

        if (greenX < 0) {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY) + minFishY);
        }


        if (redX < 0) {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxFishY - minFishY) + minFishY);
        }

        canvas.drawCircle(redX, redY, 40, redPaint);
        //will create ball, appear at random  positions on screen.
        //this is why there's only 1 yellow and 1 green ball on-screen at a time, because a new one is generated
        //only when the old one is either 1. moved past the screen by itself or 2. fish eats ball and ball's position is moved past screen
        //in order to force generation of new ball.
        canvas.drawCircle(yellowX, yellowY, 25, yellowPaint);
        canvas.drawCircle(greenX, greenY, 15, greenPaint);

        //.drawText(text, pixels from the left, pixels from the top)
        canvas.drawText("Score : " + score, 20, 60, scorePaint);

        for (int i = 0; i < 3; i++) {
            int x = (int) (580 + life[0].getWidth() * 1.5 * i);
            int y = 30;

            //draw red hearts for the lives fish has left;
            if (i < lifeCounterOfFish) {
                canvas.drawBitmap(life[0], x, y, null);
            }
            //display the gray heart for hearts > lifeCounter, life[1] is gray heart image
            else {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }
    }

    //whenever fish gets yellow ball, it disappears and it adds score to fish
    public boolean hitBallChecker(int x, int y) {
        if (fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < (fishY + fish[0].getHeight())) {
            return true;
        }
        return false;
    }

    //when there is a touch, there is a callback function...
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //when there is a touch, touch becomes true (to influence line 83)
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            //and fishSpeed = -22 so fish goes upward, until gravity
            //brings it down again
            fishSpeed = -22;
        }
        return true;
    }
}
