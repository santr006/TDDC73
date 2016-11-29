package com.example.sandras.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InteractiveSearcher extends ViewGroup {

    int numAlternatives = 10;
    int deviceWidth;
    int numRequest = -1;
    private int currentRequest = -1;
    Context cont;
    EditText theText;
    MyList theList;
    private Object lock = new Object();
    PopupWindow popupWindow;

    public InteractiveSearcher(Context context) {
        super(context);
        init(context);
    }

    public InteractiveSearcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        // retrieved values correspond to the positions of the attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.InteractiveSearcher);
        int count = typedArray.getIndexCount();

        try{
            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.InteractiveSearcher_numAlternatives) {
                    // set the text from the layout
                    numAlternatives = typedArray.getInt(attr, 10);
                }
            }
            init(context);
        }

        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }
    }

    public InteractiveSearcher(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context){
        //Called from layout when this view should assign a size and position to each of its children.
        //Derived classes with children should override this method and call layout on each of their children.
        cont = context;

        inflate(getContext(), R.layout.interactive_searcher, this);
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        theText = (EditText) findViewById(R.id.editText);
        //theList = (MyList) findViewById(R.id.myList);
        theList = new MyList(cont);
        theList.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        theList.numAlternatives = numAlternatives;

        EditText e = (EditText) findViewById(R.id.editText);
        e.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                if(cs.length() == 0){
                    theList.clearList();
                    popupWindow.dismiss();
                }
                else {
                    //send request
                    //update adapter
                    numRequest++;

                    ConnectivityManager connMgr = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // fetch data
                        Log.d("main", "starting");
                        new DownloadWebpageTask().execute("http://flask-afteach.rhcloud.com/getnames/"
                                + numRequest + "/" + cs.toString());
                    } else {
                        // display error
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        theList.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    Log.d("InteractiveSearcher","Touch coordinates : " + event.getX() + " " + event.getY());
                    for(int i = 0; i < numAlternatives; i++)
                    {
                        if(event.getY() < theList.mTextHeight * (i + 1))
                        {
                            Log.d("InteractiveSearcher", "word clicked " + theList.words.get(i));
                            theText.setText(theList.words.get(i));
                            break;
                        }
                    }
                }
                return true;
            }
        });
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

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                String myurl = urls[0];

                InputStream is = null;
                // Only display the first 500 characters of the retrieved
                // web page content.
                StringBuilder builder = new StringBuilder();

                try {
                    URL url = new URL(myurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d("DownloadWebpageTask", "The response is: " + response);
                    is = conn.getInputStream();

                    // Convert the InputStream into a string
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));;

                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }

                    return new String(builder);

                    // Makes sure that the InputStream is closed after the app is
                    // finished using it.
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject reader = new JSONObject(result);
                int id = reader.getInt("id");
                if(id > currentRequest ) {
                    synchronized (lock) {
                        currentRequest = id;
                    }
                    JSONArray results = reader.getJSONArray("result");
                    theList.updateList(results);

                    if(popupWindow == null)
                        popupWindow = new PopupWindow(cont);
                    popupWindow.setWidth(250);
                    popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    popupWindow.setContentView(theList);
                    popupWindow.showAsDropDown(theText, 0, 0);
                }
            }
            catch (final JSONException e)
            {
                Log.d("DownloadWebpageTask", "Json parsing error InteractiveSearcher: " + e.getMessage());
                ((Activity)cont).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(cont,
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
