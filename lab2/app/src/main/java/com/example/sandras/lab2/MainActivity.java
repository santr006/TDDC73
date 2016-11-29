package com.example.sandras.lab2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    EditText path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        path = (EditText) findViewById(R.id.path);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.listView);

        // preparing list data
        prepareListData();

        listAdapter = new MyAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                path.setText("/" + listDataHeader.get(groupPosition) + "/" + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition));

                setHighlight(listAdapter, groupPosition, childPosition);

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if(!path.getText().toString().equals(("/" + listDataHeader.get(groupPosition)).toString())) {
                    path.setText("/" + listDataHeader.get(groupPosition));
                }

                setHighlight(listAdapter, groupPosition, -1);
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                if(groupPosition == listAdapter.mGroupNumber) {
                    path.setText("/");
                    setHighlight(listAdapter, -1, -1);
                }
            }
        });

        path.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                //if text is longer than /
                if(cs.length() > 1)
                {
                    //check if the text matches any of the headers
                    boolean goodHeader = false;
                    for (int i = 0; i < listDataHeader.size(); i++)
                    {
                        CharSequence header = listDataHeader.get(i);
                        if (cs.length() > header.length())
                        {
                            String textPart = cs.subSequence(1, header.length() + 1).toString();
                            String headerPart = header.toString();
                            //if equal to header
                            if (textPart.equals(headerPart)) {
                                //mark header
                                unMark(path);
                                setHighlight(listAdapter, i, -2);

                                goodHeader = true;
                                checkForItemMatch(header, i, cs);

                                //no need to check the other headers
                                break;
                            }
                        } else {
                            //if the text could be right
                            String textPart = cs.subSequence(1, cs.length()).toString();
                            String headerPart = header.subSequence(0, cs.length()-1).toString();
                            if (textPart.equals(headerPart)) {
                                goodHeader = true;
                                unMark(path);
                            }
                        }
                    }
                    if (!goodHeader) {
                        markRed(path);
                        setHighlight(listAdapter, -1, -1);
                    }
                }
                else if(cs.length() == 1 && !cs.toString().equals("/")){
                    markRed(path);
                }
                else{
                    unMark(path);
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
    }

    private boolean checkForItemMatch(CharSequence header, int headerNum, CharSequence cs){
        List<String> items = listDataChild.get(header.toString());
        boolean goodItem = false;

        if(cs.length() > header.length() + 2) {
            //loop through the items under the header
            String text = cs.subSequence(header.length() + 2, cs.length()).toString(); //2 / should not be counted
            for (int j = 0; j < items.size(); j++) {
                String item = items.get(j);

                //if the rest of the text matches the item
                if (item.equals(text)) {
                    //mark
                    unMark(path);
                    setHighlight(listAdapter, headerNum, j);
                    goodItem = true;
                    break;
                }
                //if the rest of the text could be the start of an item
                else if (text.length() < item.length()) {
                    String itemPart = items.get(j).subSequence(0, text.length()).toString();

                    if (itemPart.equals(text)) {
                        //user could be writing something right
                        goodItem = true;
                        unMark(path);
                        break;
                    }
                }
            }
            if (!goodItem) {
                //user is writing something that doesn't exist
                //mark red
                markRed(path);
                setHighlight(listAdapter, -1, -1);
            }
        }
        return false;
    }

    void markRed(EditText t){
        t.setBackgroundColor(Color.RED);
    }
    void unMark(EditText t){
        t.setBackgroundColor(Color.WHITE);
    }

    void setHighlight(MyAdapter ad, int gp, int cp){
        if(cp != -2)
            ad.mChildNumber = cp;
        ad.mGroupNumber = gp;

        if(gp != -1 && !expListView.isGroupExpanded(gp)){
            expListView.expandGroup(gp);
        }

        ad.notifyDataSetChanged();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
