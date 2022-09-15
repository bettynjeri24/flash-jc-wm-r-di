package com.ekenya.rnd.baseapp.di.helpers.features;

import android.util.Log;

import com.ekenya.rnd.baseapp.di.helpers.activities.AddressableActivity;
import com.ekenya.rnd.common.Constants;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class Modules {


    private static final List<FeatureModule> modules;

    public static final Modules INSTANCE;

    public final FeatureModule getModuleFromName(String moduleName) {

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
        modules = Arrays.asList(new FeatureModule[]{(FeatureModule) FeatureTijara.INSTANCE,
                (FeatureModule) FeatureSupport.INSTANCE}.clone()
        );
    }

    public static final class FeatureTijara implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        private static final String className;

        public static final FeatureTijara INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureTijara() {
        }

        static {
            FeatureTijara var0 = new FeatureTijara();
            INSTANCE = var0;
            name = "tijara";
            injectorName = Constants.BASE_PACKAGE_NAME+"."+name+".di.TijaraInjector";
            Log.d("INJECTOR NAME,",injectorName);
            className = Constants.BASE_PACKAGE_NAME +"."+name+".TijaraSplashActivity";
            Log.d("INJECTOR CLASS,",className);
        }

        @Override
        public String getClassName() {
            return className;
        }
    }
    public static final class FeatureScanning implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        private static final String className;

        public static final FeatureScanning INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureScanning() {
        }

        static {
            FeatureScanning var0 = new FeatureScanning();
            INSTANCE = var0;
            name = "scannerlib";
            injectorName = Constants.BASE_PACKAGE_NAME+"."+name+".di.TijaraInjector";
            Log.d("INJECTOR NAME,",injectorName);
            className = Constants.BASE_PACKAGE_NAME +"."+name+".ScanningActivity";
            Log.d("INJECTOR CLASS,",className);
        }

        @Override
        public String getClassName() {
            return className;
        }
    }

    public static final class FeatureSupport implements FeatureModule {

        private static final String name;

        private static final String injectorName;

        public static final FeatureSupport INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureSupport() {
        }

        static {
            FeatureSupport var0 = new FeatureSupport();
            INSTANCE = var0;
            name = "support";
            injectorName = Constants.BASE_PACKAGE_NAME+".support.di.SupportInjector";
        }
    }

}
