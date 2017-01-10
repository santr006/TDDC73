package com.example.sandras.miniproject;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class Progress extends ViewGroup {

    private Context cont;
    private int deviceWidth;
    private int steps = 10;
    private int current = 0;
    private int textSize = 15;
    private String itemName = "items";
    private boolean showText = true;
    private int barHeight = 45;

    private ProgressBar p;
    private TextView info;

    public Progress(Context context) {
        super(context);

        init(context);
    }

    public Progress(Context context, AttributeSet attrs) {
        super(context, attrs);
        // retrieved values correspond to the positions of the attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Progress);
        int count = typedArray.getIndexCount();

        try{
            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                if(attr == R.styleable.Progress_numSteps) {
                    steps = typedArray.getInt(attr, steps);
                }
                else if(attr == R.styleable.Progress_current) {
                    current = typedArray.getInt(attr, current);
                }
                else if(attr == R.styleable.Progress_textSize) {
                    textSize = typedArray.getInt(attr, textSize);
                }
                else if(attr == R.styleable.Progress_itemName) {
                    itemName = typedArray.getString(attr);
                }
                else if(attr == R.styleable.Progress_showProgressText) {
                    showText = typedArray.getBoolean(attr, showText);
                }
                else if(attr == R.styleable.Progress_barHeight) {
                    barHeight = typedArray.getInt(attr, barHeight);
                }
            }
        }

        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        cont = context;
        inflate(context, R.layout.progress, this);
        info = (TextView) findViewById(R.id.infoText);
        info.setTextSize(textSize);
        if(showText)
            info.setText("" + current + " out of " + steps + " " + itemName);
        else
            info.setVisibility(GONE);

        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        p = (ProgressBar) findViewById(R.id.progressbar);
        p.steps = steps;
        p.current = current;
        p.barHeight = barHeight;
        p.activate();
    }

    /*
     * @return The number of steps in the progress bar
     */
    public int getNumOfSteps() { return steps; }

    /*
     * Set the number of steps of the full progress
     * @param newSteps The amount of steps
     */
    public void setNumOfSteps(int newSteps) {
        steps = newSteps;
        p.steps = steps;
        p.invalidate();
        if(showText)
            info.setText("" + current + " out of " + steps + " " + itemName);
    }

    /*
     * @return The current progress
     */
    public int getCurrent() {return current;}

    /*
     * Sets the current progress done and updates the view
     * @param newCurrent The amount of progress done
     */
    public void setCurrent(int newCurrent){
        if(newCurrent <= steps)
            current = newCurrent;
        else
            current = steps;
        p.current = current;
        if (showText)
            info.setText("" + current + " out of " + steps + " " + itemName);
        p.invalidate();
    }

    /*
     * @return The size of the text
     */
    public int getTextSize() { return textSize; }

    /*
     * Sets the size of the text and update it
     * @param newTextSize The new size of the text
     */
    public void setTextSize(int newTextSize) {
        textSize = newTextSize;
        info.setTextSize(textSize);
        if(showText)
            info.setText("" + current + " out of " + steps + " " + itemName);
    }

    /*
     * @return The name of th items in the progress
     */
    public String getItemName() { return itemName; }

    /*
     * Set the name of the items in the progress
     * Should be in plural
     * @param newItemName The new name of the items
     */
    public void setItemName(String newIitemName) {
        itemName = newIitemName;
        if(showText)
            info.setText("" + current + " out of " + steps + " " + itemName);
    }

    /*
     * @return True if text is shown, otherwise false
     */
    public boolean getShowText() { return showText; }

    /*
     * Sets if the text explaining the progress should be shown or not and updates the view
     * @param newShowText True if text should be shown, else false
     */
    public void setShowText(boolean newShowText) {
        showText = newShowText;
        if(showText) {
            info.setVisibility(VISIBLE);
            info.setText("" + current + " out of " + steps + " " + itemName);
        }
        else {
            info.setVisibility(GONE);
        }
    }

    /*
     * @return The height of the progress bar
     */
    public int getBarHeight() { return barHeight; }

    /*
     * Sets the height of the progress bar and updates the view
     * @param newBarHeight The new height
     */
    public void setBarHeight(int newBarHeight) {
        barHeight = newBarHeight;
        p.barHeight = barHeight;
        p.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        //get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                //return;
                continue;

            //Get the maximum size of the child
            //child.setwidth
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            //wrap is reach to the end
            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }
            //do the layout
            child.layout(curLeft, curTop, curLeft + this.getMeasuredWidth(), curTop + curHeight);
            //store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();

            if ((mLeftWidth / deviceWidth) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }
}
