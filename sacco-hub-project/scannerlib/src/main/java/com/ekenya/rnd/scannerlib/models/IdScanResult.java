package com.ekenya.rnd.scannerlib.models;

import android.graphics.Bitmap;

public class IdScanResult {
    private CardDetails details;
    private Bitmap face;
    /**getters n setters*/

    public CardDetails getDetails() {
        return details;
    }

    public void setDetails(CardDetails details) {

        this.details = details;
    }

    public Bitmap getFace() {
        return face;
    }

    public void setFace(Bitmap face) {

        this.face = face;
    }
}
