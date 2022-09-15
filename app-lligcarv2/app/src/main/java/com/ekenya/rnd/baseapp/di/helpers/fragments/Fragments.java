package com.ekenya.rnd.baseapp.di.helpers.fragments;


import androidx.annotation.NonNull;

import com.ekenya.rnd.common.Constants;

public final class Fragments {

    public static final Fragments INSTANCE;

    private Fragments() {
    }

    static {
        Fragments var0 = new Fragments();
        INSTANCE = var0;
    }

    public static final class FeatureWalletWithdraw implements AddressableFragment {

        private static final String className;

        public static final FeatureWalletWithdraw INSTANCE;

        @NonNull
        public String getClassName() {
            return className;
        }

        private FeatureWalletWithdraw() {
        }

        static {
            FeatureWalletWithdraw var0 = new FeatureWalletWithdraw();
            INSTANCE = var0;
            className = Constants.BASE_PACKAGE_NAME + ".identity.ui.LoginDialogFragment";
        }

    }
}

