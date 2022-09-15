package com.ekenya.rnd.baseapp.di.helpers.features;

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
        INSTANCE = new Modules();
        modules = Arrays.asList(new FeatureModule[]{
                FeatureMyCards.INSTANCE,
                }.clone()
        );
    }

    public static final class FeatureMyCards implements FeatureModule, AddressableActivity {

        private static final String name;

        private static final String injectorName;

        public static final FeatureMyCards INSTANCE;

        private static final String className;

        public String getClassName() {
            return className;
        }

        public String getName() {
            return name;
        }

        public String getInjectorName() {
            return injectorName;
        }

        private FeatureMyCards() {
        }

        static {
            INSTANCE = new FeatureMyCards();
            name = "mycards";
            injectorName = "com.ekenya.rnd.tmd.di.MyCardsInjector";
            className = "com.ekenya.rnd.tmd.ui.ActivityMainMyTmd";
        }
    }

}
