package com.brainmedia.masterdownloader.Dialogues;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.brainmedia.masterdownloader.R;

public class AboutusDialoge extends Dialog {
    public AboutusDialoge(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_us);

        TextView contact_txt = findViewById(R.id.email);
        ImageButton share_btn = findViewById(R.id.share_btn);
        Button close_btn = findViewById(R.id.close_btn);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();

            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



    }



    // Initialize Share Ap Function
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String link = "https://play.google.com/store/apps/";
        String desc = "Download the App";
        shareIntent.putExtra(Intent.EXTRA_TEXT,desc);
        shareIntent.putExtra(Intent.EXTRA_TEXT,link);
        getContext().startActivity(Intent.createChooser(shareIntent,"Share Via"));

    }

}
