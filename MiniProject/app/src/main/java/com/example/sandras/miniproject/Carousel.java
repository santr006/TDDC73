package com.example.sandras.miniproject;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Carousel extends ViewGroup {

    public interface OnSelectListener {
        void onSelect(int i);
    }

    private OnSelectListener mOnSelectListener;
    private Context cont;
    private int deviceWidth;
    private RelativeLayout List;

    private int imageWidth = 300;
    private int imageHeight = 300;
    private int paddingBetweenImages = 20;

    private int fullListWidth;
    private int selected;
    private int displacement;

    private float x1,x2;
    private static final int MIN_DISTANCE = 150;
    private int xDelta;
    private int timeAtDown;
    private static final int MAX_TIME_MILLIS = 500;

    public Carousel(Context context) {
        super(context);
        init(context);
    }

    public Carousel(Context context, AttributeSet attrs) {
        super(context, attrs);
        // retrieved values correspond to the positions of the attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Progress);
        int count = typedArray.getIndexCount();

        try{
            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                if(attr == R.styleable.Carousel_heightOfItem) {
                    imageHeight = typedArray.getInt(attr, imageHeight);
                }
                else if(attr == R.styleable.Carousel_paddingBetweenItems) {
                    paddingBetweenImages = typedArray.getInt(attr, paddingBetweenImages);
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

    public void setOnSelectListener(OnSelectListener listener){
        mOnSelectListener = listener;
    }

    private void select(int i) {
        selected = i;
        if(mOnSelectListener != null) {
            mOnSelectListener.onSelect(i);
        }
    }

    private void init(Context context) {
        cont = context;
        inflate(context, R.layout.carousel, this);

        fullListWidth = 0;
        selected = -1;
        displacement = 0;

        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        //set touch listener to the list
        List = (RelativeLayout) findViewById(R.id.List);
        List.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int X = (int) event.getRawX();
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        ImageView im = (ImageView) List.getChildAt(0);
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) im.getLayoutParams();
                        xDelta = X - lParams.leftMargin;
                        timeAtDown = (int) System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        //detect swipe or tap
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        displacement += deltaX;
                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                //Toast.makeText(cont, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                            }
                            // Right to left swipe action
                            else {
                                //Toast.makeText(cont, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                            }
                        }
                        else {
                            // consider as something else - a screen tap for example
                            if((int) System.currentTimeMillis() - timeAtDown < MAX_TIME_MILLIS){
                                //select this item
                                select(findSelected(x2));
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(fullListWidth > deviceWidth)
                            moveList(X - xDelta);
                        break;
                }
                return true;
            }
        });
    }

    private void moveList(int moveX){
        //for every image
        for(int i = 0; i < List.getChildCount(); i++)
        {
            //change margin to move the image
            ImageView im = (ImageView) List.getChildAt(i);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) im.getLayoutParams();
            layoutParams.leftMargin = moveX + (imageWidth + paddingBetweenImages) * i;

            //border conditions
            if(layoutParams.leftMargin >= fullListWidth - 2 * imageWidth)//2 because the list starts a width early and it's the first corner we're interested in
                layoutParams.leftMargin = layoutParams.leftMargin - fullListWidth;
            if(layoutParams.leftMargin < -imageWidth)
                layoutParams.leftMargin = layoutParams.leftMargin + fullListWidth;

            im.setLayoutParams(layoutParams);
        }
    }

    private int findSelected(float xPos){
        int pos = (-displacement + (int) xPos) % fullListWidth;
        if(pos <= 0)
            return List.getChildCount() - 1 + pos / (imageWidth + paddingBetweenImages);
        else
            return pos / (imageWidth + paddingBetweenImages);
    }

    public void addToList(int drawable)
    {
        ImageView im = new ImageView(cont);
        im.setImageResource(drawable);
        im.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
        layoutParams.leftMargin = fullListWidth;
        layoutParams.rightMargin = -imageWidth;
        im.setLayoutParams(layoutParams);
        List.addView(im);
        fullListWidth += imageWidth + paddingBetweenImages;
    }

    public void removeFromList(int position){
        for(int i = position; i < List.getChildCount(); i++){
            ImageView im = (ImageView) List.getChildAt(i);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) im.getLayoutParams();
            layoutParams.leftMargin = (imageWidth + paddingBetweenImages) * (i - 1);
        }

        List.removeViewAt(position);

        fullListWidth = (imageWidth + paddingBetweenImages) * List.getChildCount();
    }

    //returns the currently selected image's position in the list
    public int getSelected(){
        return selected;
    }

    public int getNumOfItems() { return List.getChildCount(); }

    public int getImageHeight() { return imageHeight; }
    public void setImageHeight(int newHeight){
        imageWidth = newHeight;
        imageHeight = newHeight;

        for(int i = 0; i < List.getChildCount(); i++){
            ImageView im = (ImageView) List.getChildAt(i);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) im.getLayoutParams();
            layoutParams.height = newHeight;
            layoutParams.width = newHeight;
            layoutParams.leftMargin = (imageWidth + paddingBetweenImages) * i;
        }
        fullListWidth = (imageWidth + paddingBetweenImages) * List.getChildCount();
    }

    public int getImagePadding() { return paddingBetweenImages; }
    public void setImagePadding(int newPadding){
        paddingBetweenImages = newPadding;
        for(int i = 0; i < List.getChildCount(); i++){
            ImageView im = (ImageView) List.getChildAt(i);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) im.getLayoutParams();
            layoutParams.leftMargin = (imageWidth + paddingBetweenImages) * i;
        }
        fullListWidth = (imageWidth + paddingBetweenImages) * List.getChildCount();
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

            child.getLayoutParams();
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


