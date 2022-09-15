package com.ekenya.rnd.walletbaseapp.tollo.di.helpers.features;

import com.ekenya.rnd.walletbaseapp.tollo.di.helpers.activities.AddressableActivity;
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
        modules = Arrays.asList(new FeatureModule[]{(FeatureModule) FeatureOnboarding.INSTANCE,
                (FeatureModule) FeatureDashboard.INSTANCE}.clone()
        );
    }

    public static final class FeatureOnboarding implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        private static final String className;

        public static final FeatureOnboarding INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureOnboarding() {
        }

        static {
            FeatureOnboarding var0 = new FeatureOnboarding();
            INSTANCE = var0;
            name = "onboarding";
            injectorName = Constants.BASE_PACKAGE_NAME+"."+name+".di.TourismInjector";
            className = Constants.BASE_PACKAGE_NAME +"."+name+".MainActivity";
        }

        @Override
        public String getClassName() {
            return className;
        }
    }

    public static final class FeatureDashboard implements FeatureModule,AddressableActivity {

        private static final String name;
        private static final String className;


        private static final String injectorName;

        public static final FeatureDashboard INSTANCE;

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureDashboard() {
        }

        static {
            FeatureDashboard var0 = new FeatureDashboard();
            INSTANCE = var0;
            name = "dashboard";
            injectorName = Constants.BASE_PACKAGE_NAME+".dashboard.di.SupportInjector";
            className = Constants.BASE_PACKAGE_NAME +"."+name+".MainActivity";

        }

        @Override
        public String getClassName() {
            return className;
        }
    }

}
