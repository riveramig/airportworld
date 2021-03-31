package avion;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import ltc.LaneTrafficControlGuard;
import ltc.LaneTrafficControlMessageType;
import messages.LaneTrafficControlMessage;
import messages.PlaneMessage;

public class PlaneGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        PlaneMessage message = (PlaneMessage) eventBESA.getData();
        PlaneState planeState = (PlaneState) this.agent.getState();
        AgHandlerBESA ah = null;
        switch (message.getType()) {
            case WHERE_AM_I:
                ReportBESA.info("["+planeState.getAlias()+"]"+"Estoy volando? " + (planeState.isAmIFlying() ? "SI" : "NO"));
                ReportBESA.info("["+planeState.getAlias()+"]"+"Estoy lleno? " + (planeState.isAmIFilled() ? "SI" : "NO"));
                if(!planeState.isAmIFlying() && !planeState.isAmIFilled()){
                    try {
                        ah =this.agent.getAdmLocal().getHandlerByAlias(planeState.getAlias());
                        PlaneMessage messageToSend = new PlaneMessage(PlaneMessageType.FILL_PLANE);
                        EventBESA msj = new EventBESA(PlaneGuard.class.getName(), messageToSend);
                        ah.sendEvent(msj);
                    } catch (ExceptionBESA exceptionBESA) {
                        exceptionBESA.printStackTrace();
                    }
                }
                break;
            case FILL_PLANE:
                ReportBESA.info("["+planeState.getAlias()+"]"+"Llenando avion lol " + planeState.getTimeToFillInGate() + " milisegundos");
                try {
                    this.doWait(planeState.getTimeToFillInGate());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ReportBESA.info("["+planeState.getAlias()+"]"+"Avion lleno");
                ReportBESA.info("["+planeState.getAlias()+"]"+"Enviar LTC peticion de despegue....");
                try {
                    ah =this.agent.getAdmLocal().getHandlerByAlias(planeState.getCurrentAirport().getLtcAgent().getAlias());
                    LaneTrafficControlMessage messageToLTC = new LaneTrafficControlMessage(LaneTrafficControlMessageType.PLANE_RUNWAY_TAKEOFF_REQUEST,planeState.getAlias());
                    EventBESA msj = new EventBESA(LaneTrafficControlGuard.class.getName(),messageToLTC);
                    ah.sendEvent(msj);
                } catch (ExceptionBESA exceptionBESA) {
                    exceptionBESA.printStackTrace();
                }
                break;
            default:
                ReportBESA.info("No entendi");
        }

    }

    private synchronized void doWait(long number) throws InterruptedException {
        this.wait(number);
    }
}
