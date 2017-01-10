package com.example.sandras.miniproject;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ProgressBar extends View {

    private Context cont;
    private int deviceWidth;
    private Paint mPaint;
    public int steps;
    public int stepSize;
    public int current;
    public int barHeight;

    public ProgressBar(Context context) {
        super(context);
        init(context);
    }

    public ProgressBar(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        cont = context;
        final Display display = ((WindowManager) cont.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    public void activate(){
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = barHeight;
        setLayoutParams(lp);

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        //draw the View
        for(int i = 0; i < steps; i++)
        {
            if(i < current)
                mPaint.setColor(Color.GREEN);
            else
                mPaint.setColor(Color.LTGRAY);
            canvas.drawRect(stepSize * i, 0, stepSize * (i + 1), barHeight, mPaint);
        }

        //since rounding errors make it so that the bar doesn't fill the whole screeb
        //fill out the last part of the screen
        if(steps * stepSize < deviceWidth)
        {
            if(steps - 1 < current)
                mPaint.setColor(Color.GREEN);
            else
                mPaint.setColor(Color.LTGRAY);
            canvas.drawRect(stepSize * (steps - 1), 0, deviceWidth, barHeight, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.

        if (getVisibility() != GONE)
        {
            // Measure the child.
            maxWidth += Math.max(maxWidth, getMeasuredWidth());
            mLeftWidth += getMeasuredWidth();

            if ((mLeftWidth / deviceWidth) > rowCount) {
                maxHeight += getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        if(steps != 0)
            stepSize = maxWidth / steps;
    }
}
