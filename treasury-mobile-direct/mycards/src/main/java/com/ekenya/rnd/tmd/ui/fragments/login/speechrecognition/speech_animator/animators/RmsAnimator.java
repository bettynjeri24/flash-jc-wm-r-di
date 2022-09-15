package com.ekenya.rnd.tmd.ui.fragments.login.speechrecognition.speech_animator.animators;


import com.ekenya.rnd.tmd.ui.fragments.login.speechrecognition.speech_animator.RecognitionBar;

import java.util.ArrayList;
import java.util.List;

public class RmsAnimator implements BarParamsAnimator {
    final private List<BarRmsAnimator> barAnimators;


    public RmsAnimator(List<RecognitionBar> recognitionBars) {
        this.barAnimators = new ArrayList<>();
        for (RecognitionBar bar : recognitionBars) {
            barAnimators.add(new BarRmsAnimator(bar));
        }
    }

    @Override
    public void start() {
        for (BarRmsAnimator barAnimator : barAnimators) {
            barAnimator.start();
        }
    }

    @Override
    public void stop() {
        for (BarRmsAnimator barAnimator : barAnimators) {
            barAnimator.stop();
        }
    }

    @Override
    public void animate() {
        for (BarRmsAnimator barAnimator : barAnimators) {
            barAnimator.animate();
        }
    }

    public void onRmsChanged(float rmsDB) {
        for (BarRmsAnimator barAnimator : barAnimators) {
            barAnimator.onRmsChanged(rmsDB);
        }
    }
}