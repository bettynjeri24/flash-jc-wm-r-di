package com.ekenya.rnd.common.abstractions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCommon;
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks;
import com.ekenya.rnd.common.dialogs.base.adapter_detail.model.DialogDetailCommon;
import com.ekenya.rnd.common.dialogs.dialog_progress.ProgressDialogCommon;
import com.google.android.play.core.splitcompat.SplitCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Bourne Koloh on 16 February,2021.
 * Eclectics International, Products and R&D
 * PROJECT: Dynamic App Demo
 */
public abstract class BaseActivity extends DaggerAppCompatActivity {
    public static final int WRITE_PERMISSION = 0;

    AlertDialog progressAlertDialog;

    @Inject
    protected ViewModelProvider.Factory mViewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onPostCreate(@Nullable  Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        //
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // If the permission is not authorized in the first time. A new permission access
//            // request will be created.
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        WRITE_PERMISSION);
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        WRITE_PERMISSION);
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // Emulates installation of on demand modules using SplitCompat.
        SplitCompat.installActivity(this);
    }


    public void showHideProgress(String message){
        // init dialog if empty
        if (progressAlertDialog == null){
            ProgressDialogCommon progressDialog = new ProgressDialogCommon(getApplicationContext());
            progressDialog.setloadingMessage(message);
            progressAlertDialog = progressDialog.create();
        }

        if (!progressAlertDialog.isShowing()){
            progressAlertDialog.show();
        }else {
            progressAlertDialog.dismiss();
        }
    }



}