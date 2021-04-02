package ltc;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import atc.AirTrafficControlGuard;
import atc.AirTrafficControlType;
import avion.PlaneGuard;
import avion.PlaneMessageType;
import messages.AirTrafficControlMessage;
import messages.LaneTrafficControlMessage;
import messages.PlaneMessage;

public class LaneTrafficControlGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        LaneTrafficControlMessage message = (LaneTrafficControlMessage) eventBESA.getData();
        LaneTrafficControlState ltcState = (LaneTrafficControlState) this.agent.getState();
        AgHandlerBESA ah = null;
        switch (message.getType()) {
            case PLANE_RUNWAY_TAKEOFF_REQUEST:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" Esta haciendo peticion de despegue en pista: "+message.getRunaway());
                try {
                    ah = this.agent.getAdmLocal().getHandlerByAlias(ltcState.getAtc().getAlias());
                    AirTrafficControlMessage messageToSend = new AirTrafficControlMessage(AirTrafficControlType.LTC_REQUEST_RUNAWAY_TAKE_OF,message.getPlaneId(),message.getRunaway());
                    EventBESA msj = new EventBESA(AirTrafficControlGuard.class.getName(),messageToSend);
                    ah.sendEvent(msj);
                } catch (ExceptionBESA exceptionBESA) {
                    exceptionBESA.printStackTrace();
                }
                break;
            case ATC_RUNAWAY_REQUEST_RESPONSE_APPROVE:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" despegue aprobado en pista "+message.getRunaway());
                try {
                    ah=this.agent.getAdmLocal().getHandlerByAlias(message.getPlaneId());
                    PlaneMessage messageToPlane = new PlaneMessage(PlaneMessageType.TAKE_OF);
                    messageToPlane.setRunwayId(message.getRunaway());
                    EventBESA msj = new EventBESA(PlaneGuard.class.getName(),messageToPlane);
                    ah.sendEvent(msj);
                } catch (ExceptionBESA exceptionBESA) {
                    exceptionBESA.printStackTrace();
                }
                break;
            case ATC_RUNAWAY_REQUEST_RESPONSE_REQUEST_WAIT:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" no hay pistas disponibles, espera de: "+message.getMetaInfo());
                try {
                    ah=this.agent.getAdmLocal().getHandlerByAlias(message.getPlaneId());
                    PlaneMessage messageToPlane = new PlaneMessage(PlaneMessageType.WAIT_TAKE_OF);
                    messageToPlane.setMetaContent(message.getMetaInfo());
                    EventBESA msj = new EventBESA(PlaneGuard.class.getName(),messageToPlane);
                    ah.sendEvent(msj);
                }catch (ExceptionBESA exceptionBESA) {
                    exceptionBESA.printStackTrace();
                }
                break;
        }
    }
}
