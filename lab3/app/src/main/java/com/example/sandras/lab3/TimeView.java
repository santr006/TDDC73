package com.example.sandras.lab3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

public class TimeView extends TextView {
    private String titleText;
    private boolean color;

    public TimeView(Context context) {
        super(context);

        Toast.makeText(getContext(), "constructor 1", Toast.LENGTH_SHORT).show();
        setTimeView();
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // retrieved values correspond to the positions of the attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.TimeView);
        int count = typedArray.getIndexCount();

        Toast.makeText(getContext(), "contructor 2", Toast.LENGTH_SHORT).show();
        try{

            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.TimeView_timeTitle) {
                    // set the text from the layout
                    titleText = typedArray.getString(attr);
                    Toast.makeText(getContext(), titleText, Toast.LENGTH_SHORT).show();
                    setTimeView();
                } else if(attr == R.styleable.TimeView_setColor) {
                    // set the color of the attr "setColor"
                    color = typedArray.getBoolean(attr, false);
                    Toast.makeText(getContext(), "color", Toast.LENGTH_SHORT).show();
                    decorateText();
                }
            }
        }

        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }
    }

    public TimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTimeView();
    }

    private void setTimeView() {
        // has the format hour.minuits am/pm
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String time = dateFormat.format(Calendar.getInstance().getTime());

        Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();

        if(this.titleText != null )
            setText(this.titleText+" "+time);
        else
            setText(time);
    }

    private void decorateText() {
        // when we set setColor attribute to true in the XML layout
        if(this.color == true){
            // set the characteristics and the color of the shadow
            setShadowLayer(4, 2, 2, Color.rgb(250, 00, 250));
            setBackgroundColor(Color.CYAN);
        } else {
            setBackgroundColor(Color.RED);
        }
    }
}
