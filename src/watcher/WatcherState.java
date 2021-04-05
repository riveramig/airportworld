package watcher;

import BESA.Kernel.Agent.StateBESA;

public class WatcherState extends StateBESA {

    private long counterDelays = 0;

    public WatcherState() {
    }

    public long getCounterDelays() {
        return counterDelays;
    }

    public void addDelay(long delay){
        this.counterDelays=this.counterDelays+delay;
    }
}
