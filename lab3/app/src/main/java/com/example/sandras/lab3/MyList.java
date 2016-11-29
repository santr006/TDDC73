package com.example.sandras.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyList extends View {

    int numAlternatives = 3;
    Paint mTextPaint;
    int mTextColor = Color.BLACK;
    float mTextHeight = 40;
    List<String> words = new ArrayList<String>();
    Context cont;
    int deviceWidth;

    public MyList(Context context) {
        super(context);
        cont = context;
        init();
    }

    public MyList(Context context, AttributeSet attrs){
        super(context, attrs);
        cont = context;;

        // retrieved values correspond to the positions of the attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.InteractiveSearcher);
        int count = typedArray.getIndexCount();

        try
        {
            for (int i = 0; i < count; ++i)
            {
                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.MyList_numAlt)
                {
                    // set the text from the layout
                    numAlternatives = typedArray.getInt(attr, 10);
                }
            }
            init();
        }
        // the recycle() will be executed obligatorily
        finally
        {
            // for reuse
            typedArray.recycle();
        }
    }

    private void init(){
        final Display display = ((WindowManager) cont.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the View
        Log.d("MyList", "words " + words.size());
        for(int i = 0; i < words.size(); i++)
        {
            canvas.drawText(words.get(i), 0, (i+1) * mTextHeight, mTextPaint);
            Log.d("MyList", "showing " + words.get(i));
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
    }

    public void clearList(){
        words.clear();
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = 0;
        setLayoutParams(lp);
        postInvalidate();
    }

    public void updateList(JSONArray list) {
        words.clear();
        int length = numAlternatives;
        if(list.length() < numAlternatives)
            length = list.length();

        try {
            for (int i = 0; i < list.length() && i < numAlternatives; i++) {
                words.add(list.getString(i));
                Log.d("MyList", "added " + list.getString(i));
            }
        } catch (final JSONException e) {
            Log.d("DownloadWebpageTask", "Json parsing error updateList: " + e.getMessage());
            ((Activity)cont).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(cont,
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = Math.round(mTextHeight * length);
        Log.d("DownloadWebpageTask", "lp.height = " + lp.height);
        setLayoutParams(lp);
        postInvalidate();
    }
}
