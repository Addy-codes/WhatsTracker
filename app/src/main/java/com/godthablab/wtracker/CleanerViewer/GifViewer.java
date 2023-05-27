package com.godthablab.wtracker.CleanerViewer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.FileOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GifViewer extends AppCompatActivity implements View.OnClickListener {

        @BindView(R.id.gifView)
        ImageView gifView;

        @BindView(R.id.menu_fab)
        FloatingActionMenu menuFab;

        @BindView(R.id.delete)
        FloatingActionButton delete;

        @BindView(R.id.download)
        FloatingActionButton download;

        @BindView(R.id.share)
        FloatingActionButton share;

        @BindView(R.id.toolbar)
        Toolbar toolbar;

        @BindView(R.id.toolbar_title)
        TextView toolbarTitle;

        private boolean readOnly,menuDelete,menuDownload;
        String filePath;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState)  {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.gif_view_activity);
                ButterKnife.bind(this);
                setSupportActionBar(toolbar);

                if(getSupportActionBar()!=null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        toolbarTitle.setText("Images");
                }


                filePath = getIntent().getStringExtra("file");
                readOnly = getIntent().getBooleanExtra("readOnly",true);
                menuDelete = getIntent().getBooleanExtra("menuDelete",false);
                menuDownload = getIntent().getBooleanExtra("menuDownload",false);

                Glide.with(this)
                        .asGif()
                        .load(filePath)
                        .into(gifView);

                if(readOnly) {
                        menuFab.setVisibility(View.GONE);
                } else {
                        if (menuDelete) {
                                delete.setVisibility(View.VISIBLE);
                                delete.setOnClickListener(this);
                        } else {
                                delete.setVisibility(View.GONE);
                        }

                        // Not using it anymore
                        menuDownload = false;
                        if (menuDownload) {
                                download.setVisibility(View.VISIBLE);
                                download.setOnClickListener(this);
                        } else {
                                download.setVisibility(View.GONE);
                        }
                        share.setOnClickListener(this);
                }

                delete.setOnClickListener(v -> {
                        FileOperation.delete(new File(filePath));
                });
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

                if(item.getItemId() == android.R.id.home) {
                        finish();
                }

                return super.onOptionsItemSelected(item);
        }


        @Override
        public void onClick(View v) {
                if(v.equals(download)) {
                        // ignore not part of requirement
                        // Toast.makeText(this,"Download",Toast.LENGTH_SHORT).show();
                }
                if(v.equals(delete)) {
                        FileOperation.delete(new File(filePath));
                }
                if(v.equals(share)) {
                        List<File> list=  new ArrayList<>();
                        File f = new File(filePath);
                        list.add(f);
                        FileOperation.shareMultiple(list,this);
                }
        }

}
