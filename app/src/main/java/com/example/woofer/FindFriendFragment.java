package com.example.woofer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woofer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FindFriendFragment extends Fragment {

    ContentValues params = new ContentValues();
    ArrayList<ArrayList<String>> al = new ArrayList<>();
    ArrayList<String> foundFriends = new ArrayList<>();
    ArrayList<String> commonFriends = new ArrayList<>();
    ListView lv;
    ImageButton addFriend;
    ContentValues friendCv = new ContentValues();
    ArrayList<String> commons = new ArrayList<>();

    public FindFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        foundFriends.clear();
        params.put("username", MainActivity.sUsername);
        final View RootView = inflater.inflate(R.layout.fragment_find_friend, container, false);
        lv = (ListView) RootView.findViewById(R.id.listViewFof);

        @SuppressLint("StaticFieldLeak") RegisterRequest rr = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/fof.php", params) {
            @Override
            protected void onPostExecute(String output) {

                super.onPostExecute(output);

                if (!output.equals("<br />")) {
                    al = parseOutput(output);

                    for (int i = 0; i < al.size(); ++i) {

                        foundFriends.add(al.get(i).get(0));
                        commonFriends.add(al.get(i).get(1));


                    }

                    System.out.println(commonFriends);

                    myListAdapter arrayAdapter = new myListAdapter(getContext(), R.layout.search_tab, foundFriends);
                    lv.setAdapter(arrayAdapter);
                    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                            ListView common = (ListView) RootView.findViewById(R.id.listViewFof);
                            String[] temp = commonFriends.get(i).split(" ");


                            //ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,commons);
                            // common.setAdapter(adapter);
                            // System.out.println(commons);
                   /*     final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        View dialogView = (LayoutInflater.from(getContext())).inflate(android.R.layout.simple_list_item_1, null);
                        alertDialog.setView(dialogView);
                        alertDialog.setItems(temp, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
*/
                            String msg = "";

                            for (int j = 0; j < temp.length; j++) {

                                if (j != temp.length - 1) {
                                    msg += temp[j] + "\n";
                                } else {
                                    msg += temp[j];
                                }

                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            //builder.setTitle("Friends you share with " + foundFriends.get(i).toString());
                            builder.setMessage(msg);
                            builder.setNegativeButton("OK", null);
                            AlertDialog dialog = builder.show();

// Must call show() prior to fetching text view
                            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                            messageView.setGravity(Gravity.CENTER);


                            // dialog.setTitle("Friends you share with " + foundFriends.get(i).toString());
                            //dialog.show();

                            return false;
                        }
                    });

                }
            }
        };


        rr.execute();
        return RootView;


    }


    public ArrayList<ArrayList<String>> parseOutput(String output) {

        JSONArray ja = null;
        ContentValues cv = new ContentValues();
        ArrayList<ArrayList<String>> al = new ArrayList<>();
        //System.out.println(output);

        try {

            ja = new JSONArray(output);
            //System.out.println("LENGTH " + ja.length());


            for (int i = 0; i < ja.length(); i++) {

                JSONObject jo = new JSONObject((String) ja.get(i));
                String fof = jo.get("FRIENDUSERNAME").toString();
                String commonFriend = jo.getString("COMMONFRIENDNAME");
                ArrayList<String> pair = new ArrayList<>();
                pair.add(fof);
                pair.add(commonFriend);
                al.add(pair);


            }


            for (int i = 0; i < al.size(); ++i) {

                String current = al.get(i).get(0);
                String fof = al.get(i).get(1);

                for (int j = i + 1; j < al.size(); ++j) {

                    if (al.get(j).get(0).equals(current)) {


                        String commonFriend = al.get(j).get(1);

                        String newS = al.get(i).get(1);
                        al.get(i).set(1, newS + " " + commonFriend);
                        al.remove(j);
                        System.out.println(al);

                    }

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return al;
    }


    private class myListAdapter extends ArrayAdapter<String> {

        private int layout;

        private myListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);

            layout = resource;

        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder mainViewHolder = null;
            if (convertView == null) {

                LayoutInflater inflater = LayoutInflater.from(getContext());

                convertView = inflater.inflate(layout, parent, false);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.heading);
                viewHolder.button = (ImageButton) convertView.findViewById(R.id.addFriend);
                viewHolder.title.setText(getItem(position).toString());



                viewHolder.button.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {


                        friendCv.put("username",MainActivity.sUsername);
                        friendCv.put("friend",foundFriends.get(position).toString().split(" ")[0]);

                        RegisterRequest add = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addfriend.php", friendCv) {
                        };

                        System.out.println(friendCv);

                        add.execute();
                        Toast.makeText(getContext(), "FRIEND ADDED", Toast.LENGTH_SHORT).show();

                    }
                });

                convertView.setTag(viewHolder);

            } else {

                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.title.setText(getItem(position).toString());
                mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        friendCv.put("username",MainActivity.sUsername);
                        friendCv.put("friend",foundFriends.get(position).toString().split(" ")[0]);
                        System.out.println(friendCv);

                        RegisterRequest add = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addfriend.php", friendCv) {
                        };

                        add.execute();
                        Toast.makeText(getContext(), "FRIEND ADDED", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return convertView;
        }
    }

    public class ViewHolder {

        TextView title;
        ImageButton button;

    }


}

