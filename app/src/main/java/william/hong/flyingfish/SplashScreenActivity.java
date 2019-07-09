//we want our app to start on the splash activity instead of main activity whenever user opens our app
//this screen should hold for 5 seconds at the start, and then automatically transitions to main activity.
/*In AndroidManifest.xml, the code block <intent-filter></intent-filter> is moved from mainActivity
* to inside the splashScreenActivity, so that the app would start on the splash screen first instead of main. */

/*Inside manifest folder, under app, on line 7, modifying the folder
* and file path will modify what picture is used as App Icon in phone's menu
* Every class/activity you add, you include its name in manifest;
* e.g. Lines 12, 13, 20; 13-19 is a longer block, that's where app starts (splash screen)*/

package william.hong.flyingfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import william.hong.flyingfish.MainActivity;
import william.hong.flyingfish.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread() {
            public void run() {
                try {
                    //stay on splash screen for 5000 milliseconds
                    sleep(5000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    //transition to the main activity page
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }
        };
        thread.start();

        }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}


/*I don't know how to comment XML files so here are the comments for activity_splash_screen.xml
* Line 7: Set background of that page to splash, which is one of the .png under drawable.
* Lines 10-15: Give the image an id; set width and height of image; centralize the image on screen, add splash_icon
* Lines 17-25: Various design informations about "The Flying Fish" red text above the fish. Match parent and wrap content
* gives size and position of text. Line 14 says, put the fish icon below the text with id textview1, which is "The Flying Fish"
* Lines 33-44: Made another block of text, "By William :3" using similar methods as above.
* */