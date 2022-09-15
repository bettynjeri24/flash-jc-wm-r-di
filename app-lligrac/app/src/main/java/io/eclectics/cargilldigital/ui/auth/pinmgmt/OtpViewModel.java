package io.eclectics.cargilldigital.ui.auth.pinmgmt;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OtpViewModel extends ViewModel {

    private MutableLiveData<Boolean> navigated;

    public LiveData<Boolean> getNavigated() {
        if (navigated == null) {
            navigated = new MutableLiveData<>();
        }
        return navigated;
    }

    private MutableLiveData<String> pin;

    public LiveData<String> getPin() {
        if (pin == null) {
            pin = new MutableLiveData<>();
        }
        return pin;
    }

    public OtpViewModel() {
        navigated = new MutableLiveData<>();
        navigated.setValue(false);
        pin = new MutableLiveData<>();
        pin.setValue("");
    }

    public void navigate() {
        navigated.setValue(true);
    }

    /**
     * The method maps the key stoke to input fields, determines if input is correct and displays appropriate actions
     *
     * @param code :pressed key
     */
    public void mapToCode(String code) {
        pin.setValue(pin.getValue() + code);
    }

    /**
     * The method deletes the key stoke to input fields,
     *
     * @param :pressed key
     */
    public void deleteKeyStoke() {
        if (pin.getValue().length() == 0){
            return;
        }

        pin.setValue(removeLastChars(pin.getValue(), 1));
        Log.d("otpviewmodel", "pin values is "+ pin.getValue());

    }

    public static String removeLastChars(String str, int chars) {
        return (str != null) ? str.substring(0, str.length() - chars) : "";
    }

    /**
     * Show that the navigation has occurred
     */
    public void hasNavigated() {
        navigated.setValue(false);
        pin.setValue("");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        navigated.setValue(false);
        pin.setValue("");
    }



}
