package com.example.woofer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TimelineFragment extends Fragment {

    FloatingActionButton fab;
    Button cancel;
    Button share;
    EditText wow;
    String username = MainActivity.sUsername;
    ListView lv;
    List<String> posts = new ArrayList<>();
    RegisterRequest postStatus;
    ContentValues params = new ContentValues();
    ContentValues usernameValue = new ContentValues();



    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View RootView = inflater.inflate(R.layout.fragment_timeline, container, false);
        lv = (ListView)RootView.findViewById(R.id.postView);
        fab = (FloatingActionButton) RootView.findViewById(R.id.fab);
        fab.setFocusable(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View view = (LayoutInflater.from(getContext())).inflate(R.layout.user_input, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setView(view);
                final Dialog dialog = alertDialog.create();

                cancel = (Button) view.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                share = (Button) view.findViewById(R.id.post);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        EditText thePost = (EditText)view.findViewById(R.id.please);
                        String status = thePost.getText().toString();
                        params.put("status",status);
                        params.put("username",username);
                        RegisterRequest rr = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addpost.php",params) {

                            @Override
                            protected void onPostExecute(String output) {
                                dialog.dismiss();
                            }
                        };


                        rr.execute();
                      //  System.out.println(params.get("status"));
                       // System.out.println(params.get("username"));

                    }

                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                Rect displayRectangle = new Rect();
                Window window = getActivity().getWindow();

                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

                // dialog.getWindow().setLayout((int)(displayRectangle.width()), (int)(displayRectangle.height() ));


            }
        });

        usernameValue.put("username",username);
        @SuppressLint("StaticFieldLeak") RegisterRequest getPosts = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/test.php",usernameValue) {
            @Override
            protected void onPostExecute(String output) {
                //super.onPostExecute(output);
                posts.clear();

                if (!output.equals("<br />")){
                    try {
                        JSONArray ja = new JSONArray(output);
                        for (int i = 0; i < ja.length(); ++i) {

                            JSONObject j;
                            j = (JSONObject) ja.get(i);
                            posts.add(j.get("POST").toString() + ";" + j.get("POSTTIME").toString() + ";" + j.get("FRIENDUSERNAME"));

                        }

                        final myListAdapter arrayAdapter = new myListAdapter(getContext(), R.layout.timeline_item, R.id.post, posts);
                        lv.setAdapter(arrayAdapter);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
            }

            else {

                }

            }

        };

        getPosts.execute();


        return RootView;


    }



    private class myListAdapter extends ArrayAdapter<String> {

        private int layout;

        private myListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
            super(context, resource, textViewResourceId, objects);

            layout = resource;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder mainViewHolder = null;
           // System.out.println(getItem(position));
            String[] tokens = getItem(position).split(";");
            String [] ndate = tokens[1].split("-");
            String month = ndate[1];
            String day = ndate[2].split(" ")[0];


            if(convertView == null){


                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                TimelineFragment.ViewHolder viewHolder = new TimelineFragment.ViewHolder();
                viewHolder.post = (TextView)convertView.findViewById(R.id.post);
                viewHolder.post.setText(tokens[0]);
                viewHolder.posttime = (TextView) convertView.findViewById(R.id.posttime);
                viewHolder.posttime.setText(month+ "/" + day);
                viewHolder.friend = (TextView)convertView.findViewById(R.id.friendName);
                viewHolder.friend.setText(tokens[2]);
                convertView.setTag(viewHolder);

            }

            else{

                mainViewHolder = (ViewHolder) convertView.getTag();

                mainViewHolder.post.setText(tokens[0]);
                mainViewHolder.posttime.setText(month+ "/" + day);
                mainViewHolder.friend.setText(tokens[2]);


            }

            return convertView;
        }
    }

    public class ViewHolder{

        TextView post;
        TextView posttime;
        TextView friend;

    }



}
