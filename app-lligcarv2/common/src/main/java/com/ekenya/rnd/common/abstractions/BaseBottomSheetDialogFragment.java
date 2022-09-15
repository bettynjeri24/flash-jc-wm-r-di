package com.ekenya.rnd.common.abstractions;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ekenya.rnd.common.dialogs.base.adapter_detail.model.DialogDetailCommon;
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks;
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCommon;
import com.ekenya.rnd.common.dialogs.dialog_progress.ProgressDialogCommon;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements HasSupportFragmentInjector {

    AlertDialog progressAlertDialog;

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }


    public void showConfirmationDialog(String title,
                                       String subtitle,
                                       List<DialogDetailCommon> dialogDetailCommons,
                                       ConfirmDialogCallBacks confirmDialogCallBacks){

        ConfirmDialogCommon confirmDialog = new ConfirmDialogCommon(getContext());
        confirmDialog.setDialogTitle(title);
        confirmDialog.setDialogSubtitle(subtitle);
        confirmDialog.setUpRecyclerAdapter(dialogDetailCommons);
        confirmDialog.setCallbacks(confirmDialogCallBacks);
        confirmDialog.create().show();
    }

    public void showConfirmationDialog(String title,
                                       String subtitle,
                                       HashMap<String, String> details,
                                       ConfirmDialogCallBacks confirmDialogCallBacks){

        List<DialogDetailCommon> dialogDetailCommons = new ArrayList<>();
        for (String key : details.keySet()) {
            dialogDetailCommons.add(new DialogDetailCommon(key,details.get(key)));
        }
        ConfirmDialogCommon confirmDialog = new ConfirmDialogCommon(getContext());
        confirmDialog.setDialogTitle(title);
        confirmDialog.setDialogSubtitle(subtitle);
        confirmDialog.setUpRecyclerAdapter(dialogDetailCommons);
        confirmDialog.setCallbacks(confirmDialogCallBacks);
        confirmDialog.create().show();
    }

    public void showHideProgress(String message){
        // init dialog if empty
        if (progressAlertDialog == null){
            ProgressDialogCommon progressDialog = new ProgressDialogCommon(getContext());
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