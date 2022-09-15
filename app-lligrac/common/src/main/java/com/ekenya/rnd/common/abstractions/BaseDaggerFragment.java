package com.ekenya.rnd.common.abstractions;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public abstract class BaseDaggerFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
