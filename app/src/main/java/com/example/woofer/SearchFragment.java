package com.example.woofer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woofer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    SearchView sv;
    ArrayList<String> users = new ArrayList<>();
    ListView lv;
    ContentValues params = new ContentValues();
    ContentValues friendCv = new ContentValues();
    ImageButton addFriend;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View RootView = inflater.inflate(R.layout.fragment_search, container, false);
        lv = (ListView) RootView.findViewById(R.id.listView);
        sv = (SearchView) RootView.findViewById(R.id.searchView);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                users.clear();
                params.put("search", s);
                params.put("username", MainActivity.sUsername);

                @SuppressLint("StaticFieldLeak") RegisterRequest rr = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/search.php", params) {

                    @Override
                    protected void onPostExecute(String output) {
                        JSONArray ja = null;

                        try {

                            ja = new JSONArray(output);
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jo = (JSONObject) ja.get(i);
                                ArrayList<String> user = new ArrayList<>();
                                String username = (jo.get("username").toString());
                                String name = (jo.get("name").toString());
                                String surname = (jo.get("surname").toString());

                                users.add(username + "  (" + name + " " + surname + ")");


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        myListAdapter adapter = new myListAdapter(getContext(),R.layout.search_tab,users);
                        lv.setAdapter(adapter);
                    }
                };

                rr.execute();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                params.clear();
                params.put("search", s);
                params.put("username", MainActivity.sUsername);
                users.clear();
                //myListAdapter arrayAdapter = new myListAdapter(getContext(), R.layout.search_tab, users);
                //lv.setAdapter(arrayAdapter);
                //lv.invalidateViews();


                @SuppressLint("StaticFieldLeak") RegisterRequest rr = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/search.php", params) {

                    @Override
                    protected void onPostExecute(String output) {
                        JSONArray ja = null;

                        if(!output.equals("<br />")) {

                            try {

                                ja = new JSONArray(output);
                                System.out.println("COME ON");
                                ;
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = (JSONObject) ja.get(i);
                                    ArrayList<String> user = new ArrayList<>();
                                    String username = (jo.get("username").toString());
                                    String name = (jo.get("name").toString());
                                    String surname = (jo.get("surname").toString());
                                    users.add(username + "  (" + name + " " + surname + ")");
                                    System.out.println(users);
                                    myListAdapter arrayAdapter = new myListAdapter(getActivity(), R.layout.search_tab, users);
                                    lv.setAdapter(arrayAdapter);
                                    //lv.invalidateViews();

                                    arrayAdapter.notifyDataSetChanged();

                               /* lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        String tokens[] = users.get(i).split(" ");
                                        System.out.println(tokens[0]);
                                        friendCv.put("friend", tokens[0]);
                                        friendCv.put("username", MainActivity.sUsername);

                                        addFriend = (ImageButton) view.findViewById(R.id.addFriend);

                                    }
                                });*/


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }

                };

                rr.execute();
                return false;
            }
        });


        return RootView;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    private class myListAdapter extends ArrayAdapter<String> {

        private int layout;

        private myListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource,  objects);

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
                        friendCv.put("friend",users.get(position).toString().split(" ")[0]);

                        RegisterRequest add = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addfriend.php", friendCv) {
                        };

                        System.out.println(friendCv);

                        add.execute();
                        Toast.makeText(getContext(), "FRIEND ADDED", Toast.LENGTH_SHORT).show();

                    }
                });
                convertView.setTag(viewHolder);

            } else {

                System.out.println(position);
                mainViewHolder = (ViewHolder) convertView.getTag();
                mainViewHolder.title = (TextView) convertView.findViewById(R.id.heading);
                mainViewHolder.button = (ImageButton) convertView.findViewById(R.id.addFriend);

                mainViewHolder.title.setText(getItem(position).toString());


                mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        friendCv.put("username",MainActivity.sUsername);
                        friendCv.put("friend",users.get(position).toString().split(" ")[0]);
                        RegisterRequest add = new RegisterRequest("http://lamp.ms.wits.ac.za/~s1676701/addfriend.php", friendCv) {
                        };

                        System.out.println(friendCv + "    OI");
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
