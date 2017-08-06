package corksproductions.practicegamethings;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size); //This stuff gets the size of the screen, so size.x is the screen's width, size.y is height

        gameView = new GameView(this, size.x, size.y); //makes the gameView

        setContentView(gameView);//Lets us see the view


    }

    @Override
    protected void onPause() { //pausing
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() { //resuming
        super.onResume();
        gameView.resume();
    }
}
