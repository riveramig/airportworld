package watcher;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import messages.WatcherMessage;

public class WatcherGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        WatcherState watcherState = (WatcherState) this.agent.getState();
        WatcherMessage message = (WatcherMessage) eventBESA.getData();
        watcherState.addDelay(message.getDelay());
    }
}
