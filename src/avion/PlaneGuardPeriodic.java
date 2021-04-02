package avion;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;

public class PlaneGuardPeriodic extends PeriodicGuardBESA {
    @Override
    public void funcPeriodicExecGuard(EventBESA eventBESA) {
        PlaneState planeState = (PlaneState) this.agent.getState();
        AgHandlerBESA ah = null;
        if (planeState.isAmIFlying()) {
            ReportBESA.info("["+planeState.getAlias()+"]"+"Estoy volando, escaneo de radar...");
        }
    }
}
