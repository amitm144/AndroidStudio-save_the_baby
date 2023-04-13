package com.example.car_crash;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class ViewManager {


    public static void init_bee_animation_ui(MainActivity activity, ImageView[][] bees, int COL, int ROW) {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                Glide.with(activity)
                        .load(R.drawable.beegif)
                        .into(bees[i][j]);
            }
        }
    }


    public static void move_baby_ui(ImageView[] baby, int current_baby_position, int COL) {
        for (int i = 0; i < COL; i++)
            baby[i].setVisibility(View.INVISIBLE);
        baby[current_baby_position].setVisibility(View.VISIBLE);
    }

    public static boolean shift_down_ui(int COL, int ROW, ImageView[][] bees, int current_baby_position) {
        for (int i = 0; i < COL; i++) {
            for (int j = ROW; j > 0; j--) {
                if (bees[i][j - 1].getVisibility() == View.VISIBLE) {
                    bees[i][j - 1].setVisibility(View.INVISIBLE);
                    if (j <= ROW - 1) bees[i][j].setVisibility(View.VISIBLE);
                    if (j == ROW - 1 && current_baby_position == i) return true;
                }
            }
        }
        return false;
    }


    public static void baby_injured_ui(int current_life, ImageView[] baby, int COL, Resources resources, String packageName) {
        String icID = "baby" + current_life;
        int resID = resources.getIdentifier(icID, "drawable", packageName);
        for (int i = 0; i < COL; i++)
            baby[i].setImageResource(resID);

    }

    public static void open_menu_ui(LinearLayoutCompat game_layout, CardView menu_layout, int open) {

        game_layout.setAlpha(new Float(1 - open / 7));
        menu_layout.setVisibility(View.INVISIBLE - open);


    }

    public static void set_lbl_ui(TextView lbl, String s) {
        lbl.setText(s);
    }

    public static void boom_ui(MainActivity mainActivity, Object systemService, ImageView[] baby, int COL , int ROW , ImageView[][] bees, int current_baby_position ) {
        bees[current_baby_position][ROW - 1].setVisibility(View.INVISIBLE);
        EffectsManager.playFailureSound(mainActivity);
        EffectsManager.vibrate(systemService);
        for (int i = 0; i < COL; i++)
            Glide.with(mainActivity)
                    .load(R.drawable.boom)
                    .into(baby[i]);
    }

    public static void disable_btn_ui(MaterialButton btn_left, MaterialButton btn_right, boolean allow) {
        btn_left.setClickable(allow);
        btn_right.setClickable(allow);
    }

    public static void reset_game_ui(ImageView[] baby, ImageView[][] bees, ImageView[] hearts, int COL, int ROW, int current_score,
                                     TextView score_LBL, int current_baby_position, LinearLayoutCompat game_layout, CardView menu_layout,
                                     int current_life, MaterialButton btn_left, MaterialButton btn_right, Resources resources, String packageName) {
        reset_bees_ui(bees, COL, ROW);
        reset_score_ui(score_LBL, current_score);
        reset_life_ui(hearts);
        move_baby_ui(baby, current_baby_position, COL);
        open_menu_ui( game_layout,  menu_layout, 0) ;
        disable_btn_ui(btn_left, btn_right, true);
        baby_injured_ui(current_life, baby, COL,resources ,packageName);


    }

    private static void reset_bees_ui(ImageView[][] bees, int COL, int ROW) {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                bees[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private static void reset_score_ui(TextView score_LBL, int current_score) {
        set_lbl_ui(score_LBL, current_score + "");
    }


    private static void reset_life_ui(ImageView[] hearts) {
        for (ImageView i : hearts)
            i.setVisibility(View.VISIBLE);
    }


    public static void loss_one_life_ui(ImageView[] hearts, int current_life , int LIFE) {
        for (int i = 0; i < current_life; i++)
            hearts[i].setVisibility(View.VISIBLE);
        for (int i = current_life; i < LIFE; i++)
            hearts[i].setVisibility(View.INVISIBLE);
    }
}
