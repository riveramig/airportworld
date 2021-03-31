package ltc;

import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import messages.LaneTrafficControlMessage;

public class LaneTrafficControlGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        LaneTrafficControlMessage message = (LaneTrafficControlMessage) eventBESA.getData();
        LaneTrafficControlState ltcState = (LaneTrafficControlState) this.agent.getState();
        AgHandlerBESA ah = null;
        switch (message.getType()) {
            case PLANE_RUNWAY_TAKEOFF_REQUEST:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getContent()+" Esta haciendo peticion de despegue ");
                break;
        }
    }
}
