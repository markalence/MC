package com.example.woofer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.woofer.R;


public class TimelineFragment extends Fragment {

    FloatingActionButton fab;
    Button cancel;
    Button share;
    EditText text;

    public TimelineFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View RootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        fab = (FloatingActionButton)RootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final View view = (LayoutInflater.from(getContext())).inflate(R.layout.user_input,null);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());


                alertDialog.setView(view);
                final Dialog dialog = alertDialog.create();

                cancel = (Button)view.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                share = (Button)view.findViewById(R.id.post);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    text = (EditText)view.findViewById(R.id.text);
                    Editable status = text.getText();
                        System.out.println(status);
                        dialog.dismiss();

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

        return RootView;



    }
}
