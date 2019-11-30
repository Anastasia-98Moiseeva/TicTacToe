package com.example.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class TicTacToeItem extends View {

    private int mItemSize;
    private Symbol mSymbol;
    private static boolean isFirstPlayer;
    private boolean existWinner;

    public TicTacToeItem(Context context) {
        this(context, null);
    }

    public TicTacToeItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TicTacToeItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSaveEnabled(true);
        isFirstPlayer = true;
        existWinner = false;
        mSymbol = Symbol.EMPTY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mItemSize = countSize();
        final int w = resolveSize(mItemSize, widthMeasureSpec);
        final int h = resolveSize(mItemSize, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSymbol == Symbol.CROSS) {
            final Drawable drawable = getContext().getDrawable(R.drawable.animated_cross);
            drawable.setBounds(0, 0, mItemSize, mItemSize);
            ((Animatable) drawable).start();
            drawable.draw(canvas);
        }
        if (mSymbol == Symbol.CIRCLE) {
            final Drawable drawable = getContext().getDrawable(R.drawable.animated_circle);
            drawable.setBounds(0, 0, mItemSize, mItemSize);
            ((Animatable) drawable).start();
            drawable.draw(canvas);
        }
        ((TicTacToeField) getParent()).invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (!existWinner && mSymbol == Symbol.EMPTY && action == MotionEvent.ACTION_DOWN) {
            mSymbol = (isFirstPlayer) ? Symbol.CROSS : Symbol.CIRCLE;
            isFirstPlayer = !isFirstPlayer;
            invalidate();
            return false;
        }
        return false;
    }

    private int countSize() {
        int displayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        return Math.min(displayWidth, displayHeight)/5;
    }

    public void restartItem(){
        mSymbol = Symbol.EMPTY;
        isFirstPlayer = true;
        existWinner = false;
        invalidate();
    }

    public Symbol getSymbol(){
        return mSymbol;
    }

    public int getItemSize(){
        return mItemSize;
    }

    public void setExistWinner(boolean existWinner) {
        this.existWinner = existWinner;
    }
}
