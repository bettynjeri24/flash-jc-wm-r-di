package com.ekenya.rnd.baseapp.di.helpers.features;

import com.ekenya.rnd.baseapp.di.helpers.activities.AddressableActivity;
import com.ekenya.rnd.common.Constants;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class Modules {


    private static final List<FeatureModule> modules;

    public static final Modules INSTANCE;

    public FeatureModule getModuleFromName(String moduleName) {

        Iterator var4 = modules.iterator();

        FeatureModule it;
        do {
            if (!var4.hasNext()) {
                throw new IllegalArgumentException(moduleName + " is not found");
            }
            Object element$iv = var4.next();
            it = (FeatureModule) element$iv;
        } while (!it.getName().equalsIgnoreCase(moduleName));

        return it;
    }

    private Modules() {
    }

    static {
        Modules var0 = new Modules();
        INSTANCE = var0;
        modules = Arrays.asList(new FeatureModule[]{
                        (FeatureModule) FeatureCargillAuth.INSTANCE,
                        (FeatureModule) FeatureCargillFarmer.INSTANCE,
                        (FeatureModule) FeatureCargillBuyer.INSTANCE,
                        (FeatureModule) FeatureCargillCooperative.INSTANCE,
                }.clone()
        );
    }

    /**
     * Cargill Auth
     * com.ekenya.rnd.cargillauth
     */
    public static final class FeatureCargillAuth implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        private static final String className;

        public static final FeatureCargillAuth INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureCargillAuth() {
        }

        static {
            FeatureCargillAuth var0 = new FeatureCargillAuth();
            INSTANCE = var0;
            name = "authcargill";
            injectorName = Constants.BASE_PACKAGE_NAME + "." + name + ".di.AuthCargillInjector";
            className = Constants.BASE_PACKAGE_NAME + "." + name + ".AuthCargillMainActivity";

        }

        @Override
        public String getClassName() {
            return className;
        }
    }

    /**
     * Cargill Farmer
     * com.ekenya.rnd.cargillfarmer
     */
    public static final class FeatureCargillFarmer implements FeatureModule, AddressableActivity {

        private static final String name;

        public enum WalletAction {
            ACTION_FARMER,
            ACTION_BUYER,
            ACTION_GENERAL_USER,
            ACTION_COOPARATIVE,
        }

        private static final String injectorName;

        private static final String className;

        public static final FeatureCargillFarmer INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureCargillFarmer() {
        }

        static {
            FeatureCargillFarmer var0 = new FeatureCargillFarmer();
            INSTANCE = var0;
            name = "cargillfarmer";
            injectorName = Constants.BASE_PACKAGE_NAME + "." + name + ".di.CargillInjector";
            className = Constants.BASE_PACKAGE_NAME + "." + name + ".CargillMainActivity";

        }

        @Override
        public String getClassName() {
            return className;
        }
    }

    /**
     * Cargill Buyer
     * com.ekenya.rnd.cargillbuyer
     */
    public static final class FeatureCargillBuyer implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        private static final String className;

        public static final FeatureCargillBuyer INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureCargillBuyer() {
        }
        static {
            FeatureCargillBuyer var0 = new FeatureCargillBuyer();
            INSTANCE = var0;
            name = "cargillbuyer";
            injectorName = Constants.BASE_PACKAGE_NAME + "." + name + ".di.CargillBuyerInjector";
            className = Constants.BASE_PACKAGE_NAME + "." + name + ".CargillBuyerMainActivity";

        }
        @Override
        public String getClassName() {
            return className;
        }
    }

    /**
     * Cargill Buyer
     * com.ekenya.rnd.cargillcoop.
     */
    public static final class FeatureCargillCooperative implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        private static final String className;

        public static final FeatureCargillCooperative INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureCargillCooperative() {
        }

        static {
            FeatureCargillCooperative var0 = new FeatureCargillCooperative();
            INSTANCE = var0;
            name = "cargillcoop";
            injectorName = Constants.BASE_PACKAGE_NAME + "." + name + ".di.CargillCoopInjector";
            className = Constants.BASE_PACKAGE_NAME + "." + name + ".ui.CoopMainActivity";
        }

        @Override
        public String getClassName() {
            return className;
        }
    }

}
