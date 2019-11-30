package com.example.tictactoe;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Winner {

    int animationDuration = 500;

    private TicTacToeField mField;

    private View mWinnerField;

    private TextView mWinnerTextView;

    private TextView mCrossWinner;
    private TextView mCircleWinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mField = findViewById(R.id.tic_tac_toe_field);
        mWinnerTextView = findViewById(R.id.txt_winner);
        mWinnerField = findViewById(R.id.winner_field);
        mCircleWinner = findViewById(R.id.circle_win_number);
        mCrossWinner = findViewById(R.id.cross_win_number);
        mField.setWinListener(this);
        findViewById(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mField.restartField();
                mWinnerField.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void win(Symbol winner) {
        switch (winner){
            case CROSS:
                mWinnerTextView.setText(getString(R.string.cross_winner));
                setWinnerCount(mCrossWinner);
                break;
            case CIRCLE:
                mWinnerTextView.setText(getString(R.string.circle_winner));
                setWinnerCount(mCircleWinner);
                break;
            case EMPTY:
                mWinnerTextView.setText(getString(R.string.no_winner));
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(animationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWinnerField.setAlpha((float) animation.getAnimatedValue());
            }
        });
        animator.start();
        mWinnerField.setVisibility(View.VISIBLE);
    }

    public void setWinnerCount(TextView winner){
        String s = (String) winner.getText();
        int num = Integer.parseInt(s);
        num++;
        String str = String.valueOf(num);
        winner.setText(str);
    }
}
