package com.nantianba.study.flow;

public class SlowSubscriber extends FlowSubscriber {
    private final int sleepTIme;

    public SlowSubscriber(String name, int sleepTIme) {
        super(name);
        this.sleepTIme = sleepTIme;
    }

    @Override
    public void onNext(String item) {
        try {
            Thread.sleep(sleepTIme);
        }
        catch (Exception e){

        }
        super.onNext(item);
    }
}