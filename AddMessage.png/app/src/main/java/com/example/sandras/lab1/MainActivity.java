package com.example.sandras.lab1;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //part 1
        //linear layout
        /*LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main);
        //rl.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //knapp
        Button b = new Button(this);
        b.setText("KNAPP");
        b.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(b);

        //textfält
        EditText et = new EditText(this);
        et.setText("Textfält");
        et.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        et.setSingleLine();
        ll.addView(et);

        //rating
        RatingBar r = new RatingBar(this);
        r.setNumStars(5);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        r.setLayoutParams(lp);
        //r.style
        ll.addView(r);

        //multiline
        EditText multi = new EditText(this);
        multi.setText("hej\nny rad\nyay");
        multi.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        multi.setSingleLine(false);
        multi.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        multi.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ll.addView(multi);
        //*/

        //part 2
        LinearLayout rl = (LinearLayout) findViewById(R.id.activity_main);
        TableLayout t = new TableLayout(this);
        t.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        TableRow tr1 = new TableRow(this);
        tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView name = new TextView(this);
        name.setText("Namn");
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr1.addView(name);

        EditText wname = new EditText(this);
        wname.setText("Anders");
        wname.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        wname.setSingleLine();
        tr1.addView(wname);
        t.addView(tr1);

        TableRow tr2 = new TableRow(this);
        tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView password = new TextView(this);
        password.setText("Lösenord");
        password.setPadding(0,0,10,0);
        password.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr2.addView(password);

        EditText wpassword = new EditText(this);
        wpassword.setText("lalalalalal");
        wpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        wpassword.setTransformationMethod(new PasswordTransformationMethod());
        wpassword.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        tr2.addView(wpassword);
        t.addView(tr2);

        TableRow tr3 = new TableRow(this);
        tr3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView email = new TextView(this);
        email.setText("Epost");
        email.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr3.addView(email);

        EditText wemail = new EditText(this);
        wemail.setText("mail@mail.com");
        wemail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        wemail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        tr3.addView(wemail);
        t.addView(tr3);

        TableRow tr4 = new TableRow(this);
        tr3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView age = new TextView(this);
        age.setText("Ålder");
        age.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr4.addView(age);

        SeekBar wage = new SeekBar(this);
        wage.setProgress(50);
        wage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        tr4.addView(wage);
        t.addView(tr4);

        rl.addView(t);
        //*/

        //part 3
        /*RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_main);
        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        l.setOrientation(LinearLayout.VERTICAL);

        TextView t1 = new TextView(this);
        t1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        t1.setText("Hur trivs du på LiU?");
        t1.setGravity(Gravity.CENTER_HORIZONTAL);
        l.addView(t1);

        LinearLayout l1 = new LinearLayout(this);
        l1.setOrientation(LinearLayout.HORIZONTAL);
        l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        CheckBox cb1 = new CheckBox(this);
        cb1.setText("Bra");
        l1.addView(cb1);
        CheckBox cb2 = new CheckBox(this);
        cb2.setText("Mycket Bra");
        l1.addView(cb2);
        CheckBox cb3 = new CheckBox(this);
        cb3.setText("Jättebra");
        l1.addView(cb3);

        l.addView(l1);

        TextView t2 = new TextView(this);
        t2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        t2.setText("Läser du på LiTH?");
        t2.setGravity(Gravity.CENTER_HORIZONTAL);
        l.addView(t2);

        LinearLayout l2 = new LinearLayout(this);
        l2.setOrientation(LinearLayout.HORIZONTAL);
        l2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        CheckBox cb4 = new CheckBox(this);
        cb4.setText("Ja");
        l2.addView(cb4);
        CheckBox cb5 = new CheckBox(this);
        cb5.setText("Nej");
        l2.addView(cb5);

        l.addView(l2);

        ImageView im = new ImageView(this);
        im.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
        im.setImageResource(R.mipmap.ic_launcher);
        l.addView(im);

        TextView t3 = new TextView(this);
        t3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        t3.setText("Är detta LiUs logotyp?");
        t3.setGravity(Gravity.CENTER_HORIZONTAL);
        l.addView(t3);

        LinearLayout l3 = new LinearLayout(this);
        l3.setOrientation(LinearLayout.HORIZONTAL);
        l3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        CheckBox cb6 = new CheckBox(this);
        cb6.setText("Ja");
        l3.addView(cb6);
        CheckBox cb7 = new CheckBox(this);
        cb7.setText("Nej");
        l3.addView(cb7);

        l.addView(l3);

        Button b = new Button(this);
        b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        b.setText("SKICKA IN");
        l.addView(b);

        rl.addView(l);
        //*/
    }
}
