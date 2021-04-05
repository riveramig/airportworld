package messages;

import BESA.Kernel.Agent.Event.DataBESA;

public class WatcherMessage extends DataBESA {
    private long delay;

    public WatcherMessage(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return delay;
    }
}
