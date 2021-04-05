package watcher;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Log.ReportBESA;

public class WatcherGuardPeriodic extends PeriodicGuardBESA {
    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        WatcherState watcherState = (WatcherState) this.agent.getState();
        ReportBESA.info("[Watcher]"+"Tiempo delays: "+watcherState.getCounterDelays()+"ms");
    }
}
