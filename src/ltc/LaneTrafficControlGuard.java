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
import misc.Gate;

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
                    messageToSend.setGateId(message.getGate());
                    EventBESA msj = new EventBESA(AirTrafficControlGuard.class.getName(),messageToSend);
                    ah.sendEvent(msj);
                } catch (ExceptionBESA exceptionBESA) {
                    exceptionBESA.printStackTrace();
                }
                break;
            case ATC_RUNAWAY_REQUEST_RESPONSE_APPROVE:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" despegue aprobado en pista "+message.getRunaway());
                PlaneMessage messageToPlane1 = new PlaneMessage(PlaneMessageType.TAKE_OF);
                messageToPlane1.setRunwayId(message.getRunaway());
                toggleGate(message.getGate(),ltcState);
                sendPlaneMessage(message.getPlaneId(),messageToPlane1);
                break;
            case ATC_RUNAWAY_REQUEST_RESPONSE_REQUEST_WAIT:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" no hay pistas disponibles, espera de: "+message.getMetaInfo());
                PlaneMessage messageToPlane2 = new PlaneMessage(PlaneMessageType.WAIT_TAKE_OF);
                messageToPlane2.setMetaContent(message.getMetaInfo());
                sendPlaneMessage(message.getPlaneId(), messageToPlane2);
                break;
            case PLANE_GATEWAY_REQUEST:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" pidiendo puerta de embarque: "+message.getGate());
                // Revisa si la puerta de embarque esta disponible
                if(isGateAvailable(message.getGate(),ltcState)) {
                    ReportBESA.info("["+ltcState.getAlias()+"] Puerta de embarque: "+message.getGate()+" disponible");
                    PlaneMessage planeMessage = new PlaneMessage();
                    planeMessage.setType(PlaneMessageType.LTC_GATE_REQUEST_APPROVAL);
                    sendPlaneMessage(message.getPlaneId(),planeMessage);
                    toggleGate(message.getGate(),ltcState);
                }else {
                    String anotherGateAvailable = findAnotherGate(ltcState);
                    if(anotherGateAvailable!=null){
                        ReportBESA.info("["+ltcState.getAlias()+"] Puerta de embarque: "+message.getGate()+" no disponible, cambiando a: "+anotherGateAvailable);
                        PlaneMessage planeMessage = new PlaneMessage();
                        planeMessage.setType(PlaneMessageType.LTC_GATE_REQUEST_CHANGE);
                        planeMessage.setGateId(anotherGateAvailable);
                        sendPlaneMessage(message.getPlaneId(), planeMessage);
                        toggleGate(anotherGateAvailable,ltcState);
                    }else {
                        String suggestedWaitTime = "2000";
                        ReportBESA.info("["+ltcState.getAlias()+"] Sin puertas de embarque, solicitando espera de "+suggestedWaitTime+"....");
                        PlaneMessage planeMessage = new PlaneMessage();
                        planeMessage.setType(PlaneMessageType.LTC_GATE_REJECTED);
                        planeMessage.setMetaContent(suggestedWaitTime);
                        sendPlaneMessage(message.getPlaneId(), planeMessage);
                    }
                }
                break;
            case PLANE_UNLOAD_PASSENGERS_END_ITINERARY:
                ReportBESA.info("["+ltcState.getAlias()+"] Avion -> "+message.getPlaneId()+" termino de descargar pasajeros en puerta de embarque: "+message.getGate());
                toggleGate(message.getGate(),ltcState);
                break;
        }
    }

    private synchronized void toggleGate(String gateId, LaneTrafficControlState ltcState) {
        for (Gate gateObj : ltcState.getGates()){
            if (gateObj.getGateId().equals(gateId)){
                gateObj.setOccupied(!gateObj.isOccupied());
            }
        }
    }

    private synchronized boolean isGateAvailable(String idGate, LaneTrafficControlState ltcState) {
        return ltcState.getGates().stream().anyMatch(
                runway -> runway.getGateId().equals(idGate) && !runway.isOccupied() && runway.isUsable()
        );
    }

    private synchronized String findAnotherGate(LaneTrafficControlState ltcState){
        return ltcState.getGates().stream().filter(gate -> gate.isUsable() && !gate.isOccupied()).map(Gate::getGateId).findFirst().orElse(null);
    }

    private synchronized void sendPlaneMessage(String planeId, PlaneMessage planeMessage) {
        try {
            AgHandlerBESA ah1 =agent.getAdmLocal().getHandlerByAlias(planeId);
            EventBESA msj = new EventBESA(PlaneGuard.class.getName(),planeMessage);
            ah1.sendEvent(msj);
        }catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }
}
