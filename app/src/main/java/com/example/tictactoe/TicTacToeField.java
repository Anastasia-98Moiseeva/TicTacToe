package com.example.tictactoe;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class TicTacToeField extends GridLayout {

    private final long mDuration = 700;

    private final int mSize = 3;

    private Paint mInnerBorderPaint = new Paint();
    private Paint mOuterBorderPaint = new Paint();

    private ArrayList<ArrayList<TicTacToeItem>> mItemViews = new ArrayList<>();

    private float mRowAnimation = 0;
    private float mColumnAnimation = 0;

    private CheckWinner mCheckWinner = new CheckWinner();

    private MainActivity mWinListener;

    public TicTacToeField(Context context) {
        this(context, null);
    }

    public TicTacToeField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TicTacToeField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setSaveEnabled(true);
        mInnerBorderPaint.setStyle(Style.STROKE);
        mInnerBorderPaint.setColor(getResources().getColor(R.color.colorGreen));
        mInnerBorderPaint.setStrokeWidth(mSize);
        mOuterBorderPaint.setStyle(Style.STROKE);
        mOuterBorderPaint.setColor(getResources().getColor(R.color.colorGreen));
        mOuterBorderPaint.setStrokeWidth(mSize*2);
        for (int i = 0; i < mSize; i++) {
            ArrayList<TicTacToeItem> itemViewRow = new ArrayList<>();
            for (int j = 0; j < mSize; j++) {
                TicTacToeItem itemView = new TicTacToeItem(getContext());
                MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                int margin = getResources().getDimensionPixelSize(R.dimen.default_margin);
                params.setMargins(margin, margin, margin, margin);
                itemView.setLayoutParams(params);
                itemViewRow.add(itemView);
                addView(itemView);
            }
            mItemViews.add(itemViewRow);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ValueAnimator animator = ValueAnimator.ofFloat(0, getWidth());
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRowAnimation = (float) animation.getAnimatedValue();
                mColumnAnimation = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();

        final View m = findViewById(R.id.tic_tac_toe_field);
        ValueAnimator a = ValueAnimator.ofFloat(0f, 1f);
        a.setDuration(mDuration);
        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                m.setAlpha((float) animation.getAnimatedValue());
            }
        });
        a.start();
        m.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && !mCheckWinner.getIsWinner()) {
            checkWinner();
            int countSymbols = 0;
            for (int i = 0; i < mSize; i++) {
                for (int j = 0; j < mSize; j++) {
                    if (mCheckWinner.table[i][j] != Symbol.EMPTY){
                        countSymbols++;
                    }
                }
            }
            if (countSymbols == 9 && mCheckWinner.getWinner() == null){
                mWinListener.win(Symbol.EMPTY);
            }
        }
        return super.onInterceptTouchEvent(event);
    }


    private void checkWinner(){
        int num = getChildCount();
        for (int i = 0; i < num; i++) {
            mCheckWinner.table[i/getColumnCount()][i%getColumnCount()] =
                    ((TicTacToeItem) getChildAt(i)).getSymbol();
        }
        mCheckWinner.setWinner(mCheckWinner.tryToFindWinner());
        if(mCheckWinner.getWinner() != null){
            mCheckWinner.setExistWinner(true);
            mWinListener.win(mCheckWinner.getWinner());
        }
    }

    public void restartField(){
        for (int i = 0; i < getChildCount(); i++) {
            TicTacToeItem child = ((TicTacToeItem) getChildAt(i));
            child.restartItem();
        }
        mCheckWinner = new CheckWinner();
    }

    public void setWinListener(Winner listener){
        mWinListener = (MainActivity) listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setWinnerAnimation(canvas);
        int indentHorizontal = 0;
        int indentVertical = 0;
        for (int i = 0; i < mSize + 1; i++) {
            Paint currentPaint = mOuterBorderPaint;
            if(i == 1 || i == 2){
                currentPaint = mInnerBorderPaint;
            }
            canvas.drawLine(indentHorizontal, 0, indentHorizontal, mColumnAnimation, currentPaint);
            canvas.drawLine(0, indentVertical, mRowAnimation, indentVertical, currentPaint);
            View child = getChildAt(i);
            final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            indentHorizontal += child.getMeasuredWidth() +
                    layoutParams.leftMargin + layoutParams.rightMargin;
            indentVertical += child.getMeasuredWidth() +
                    layoutParams.topMargin + layoutParams.bottomMargin;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setWinnerAnimation(Canvas canvas){
        if(mCheckWinner.getIsWinner()){
            setWinnerFlags();
            for (Pair<Integer, Integer> pair : mCheckWinner.winnerPairs){
                TicTacToeItem item = mItemViews.get(pair.first).get(pair.second);
                View child = getChildAt(0);
                final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();

                int indentx = pair.second*(child.getMeasuredWidth() + layoutParams.leftMargin
                        + layoutParams.rightMargin) + layoutParams.leftMargin;

                int indenty = pair.first*(child.getMeasuredWidth() + layoutParams.topMargin
                        + layoutParams.bottomMargin) + layoutParams.topMargin;
                Drawable mWinnerDrawable = null;

                if (item.getSymbol() == Symbol.CROSS) {
                    mWinnerDrawable = getContext().getDrawable(R.drawable.winner_animated_cross);
                }
                if (item.getSymbol() == Symbol.CIRCLE) {
                    mWinnerDrawable = getContext().getDrawable(R.drawable.winner_animated_circle);
                }
                System.out.println(item.getSymbol());
                mWinnerDrawable.setBounds(indentx - mCheckWinner.getWinIndent(),
                        indenty - mCheckWinner.getWinIndent(),
                        indentx + item.getItemSize() + mCheckWinner.getWinIndent(),
                        indenty + item.getItemSize() + mCheckWinner.getWinIndent());
                ((Animatable) mWinnerDrawable).start();
                mWinnerDrawable.draw(canvas);
            }
        }
    }

    private void setWinnerFlags(){
        for (ArrayList<TicTacToeItem> items : mItemViews) {
            for (TicTacToeItem item : items) {
                item.setExistWinner(true);
            }
        }
    }
}
