package com.example.car_crash;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int COL = 3, ROW = 5, LIFE = 3, START_BABY_POSITION = 1, RANDOM_HOLE_DELAY = 1200, SHIFT_DELAY = 600;
    public static ImageView[][] bees;
    public static int current_score, current_baby_position, current_random_bee = -1, current_life = LIFE;
    private final Handler handler = new Handler();
    private ImageView[] hearts, baby;
    private MaterialButton btn_left, btn_right, start_new_game, quite_game;
    private TextView score_LBL, menu_score_LBL;
    private CardView menu_layout;
    private LinearLayoutCompat game_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        find_views();
        clickBTN();
//        start_new_game();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start_new_game();
    }


    //timers run/stop game
    private void startTimer() {
        handler.post(run_random_bee);
        handler.postDelayed(run_shift, SHIFT_DELAY);
    }
    private void stopTimer() {
        handler.removeCallbacks(run_shift);
        handler.removeCallbacks(run_random_bee);
        ViewManager.disable_btn_ui(btn_left, btn_right, false);
    }


    //views
    private void find_views() {
        find_bees();
        find_hearts();
        find_btn();
        find_baby();
        find_lbl();
        find_layouts();
    }

    private void find_layouts() {
        menu_layout = findViewById(R.id.menu_layout);
        game_layout = findViewById(R.id.game_layout);
    }

    private void find_lbl() {
        score_LBL = findViewById(R.id.score_LBL);
        menu_score_LBL = findViewById(R.id.menu_score_LBL);
    }

    private void find_baby() {
        baby = new ImageView[COL];
        for (int i = 0; i < COL; i++) {
            String icID = "ic_baby_" + (i + 1);
            int resID = getResources().getIdentifier(icID, "id", getPackageName());
            baby[i] = findViewById(resID);
        }
    }

    private void find_btn() {
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        start_new_game = findViewById(R.id.start_new_game);
        quite_game = findViewById(R.id.quite_game);
    }

    private void find_bees() {
        bees = new ImageView[COL][ROW];
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                int index = i * ROW + j + 1;
                String icID = "ic_bee_" + index;
                int resID = getResources().getIdentifier(icID, "id", getPackageName());
                bees[i][j] = findViewById(resID);
            }
        }
        ViewManager.init_bee_animation_ui(MainActivity.this, bees, COL, ROW);
    }

    private void find_hearts() {
        hearts = new ImageView[LIFE];
        for (int i = 0; i < LIFE; i++) {
            String icID = "ic_heart_" + (i + 1);
            int resID = getResources().getIdentifier(icID, "id", getPackageName());
            hearts[i] = findViewById(resID);
        }
    }


    //move baby with buttons
    private void clickBTN() {
        btn_left.setOnClickListener(v -> move(-1));
        btn_right.setOnClickListener(v -> move(1));
        start_new_game.setOnClickListener(v -> start_new_game());
        quite_game.setOnClickListener(v -> finishAffinity());
    }

    private void move(int direction) {
        current_baby_position += direction;
        if (current_baby_position < 0) current_baby_position = 0;
        if (current_baby_position > COL - 1) current_baby_position = COL - 1;
        ViewManager.move_baby_ui(baby, current_baby_position, COL);
        if (bees[current_baby_position][ROW - 1].getVisibility() == View.VISIBLE) loss_point();
    }



    //generate randoms bee
    private void random_bee() {
        int rv;
        do {
            rv = new Random().nextInt(COL);
        } while (current_random_bee == rv);
        current_random_bee = rv;
        bees[current_random_bee][0].setVisibility(View.VISIBLE);
    }


    //hit by a bee
    public void loss_point() {
        current_score -= 3;
        current_life--;
        ViewManager.boom_ui(this, getSystemService(VIBRATOR_SERVICE), baby, COL, ROW, bees, current_baby_position);

        new Handler().postDelayed(() -> {
            ViewManager.baby_injured_ui(current_life, baby, COL, getResources(), getPackageName());
            ViewManager.loss_one_life_ui(hearts, current_life, LIFE);
        }, 1000);

        if (current_life == 0) game_over();

    }

    //game over
    private void game_over() {
        ViewManager.open_menu_ui(game_layout, menu_layout, 4);
        ViewManager.set_lbl_ui(menu_score_LBL, current_score + "");
        EffectsManager.playGameOverSound(this);
        stopTimer();
    }

    //new game
    private void start_new_game() {
        current_score = 0;
        current_life = LIFE;
        current_baby_position = START_BABY_POSITION;
        ViewManager.reset_game_ui(baby, bees, hearts, COL, ROW, current_score, score_LBL, current_baby_position,
                game_layout, menu_layout, current_life, btn_left, btn_right, getResources(), getPackageName());
        startTimer();
    }

    //runnable
    private final Runnable run_random_bee = new Runnable() {
        public void run() {
            handler.postDelayed(run_random_bee, RANDOM_HOLE_DELAY);
            random_bee();
        }
    };
    private final Runnable run_shift = new Runnable() {
        public void run() {
            handler.postDelayed(run_shift, SHIFT_DELAY);
            if (ViewManager.shift_down_ui(COL, ROW, bees, current_baby_position)) loss_point();
            ViewManager.set_lbl_ui(score_LBL, "" + current_score++);

        }
    };
}





