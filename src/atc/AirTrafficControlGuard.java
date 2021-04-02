package atc;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import avion.PlaneGuard;
import avion.PlaneMessageType;
import ltc.LaneTrafficControlGuard;
import ltc.LaneTrafficControlMessageType;
import messages.AirTrafficControlMessage;
import messages.LaneTrafficControlMessage;
import messages.PlaneMessage;
import misc.Runway;

public class AirTrafficControlGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        AirTrafficControlMessage message = (AirTrafficControlMessage) eventBESA.getData();
        AirTrafficControlState atcState = (AirTrafficControlState) this.agent.getState();
        AgHandlerBESA ah = null;
        switch (message.getType()) {
            case LTC_REQUEST_RUNAWAY_TAKE_OF:
                ReportBESA.info("["+atcState.getAlias()+"]"+"LTC tiene Avion: " + message.getPlaneId()+ " y requiere pista: "+message.getRunwayId());
                // Revisa si la pista solicitada esta disponible
                if (isRunWayAvailable(message.getRunwayId(),atcState)) {
                    ReportBESA.info("["+atcState.getAlias()+"]"+"Pista: " + message.getRunwayId()+ " esta disponible");
                    sendLTCMessage(message, atcState, LaneTrafficControlMessageType.ATC_RUNAWAY_REQUEST_RESPONSE_APPROVE, null);
                    toggleRunway(message.getRunwayId(), atcState);
                } else {
                    // Si la pista solicitada no esta disponible, revisa si otra esta libre
                    String anotherRunawayAvailableId = findAnotherRunway(atcState);
                    if(anotherRunawayAvailableId != null ) {
                        ReportBESA.info("["+atcState.getAlias()+"]"+"Pista solicitada no disponible pero" + anotherRunawayAvailableId+ " esta disponible");
                        sendLTCMessage(message, atcState, LaneTrafficControlMessageType.ATC_RUNAWAY_REQUEST_RESPONSE_APPROVE, null);
                        toggleRunway(anotherRunawayAvailableId, atcState);
                    } else {
                        // Si no hay ninguna pista libre hace esperar el avion
                        String suggestedWaitTime = "3000";
                        ReportBESA.info("["+atcState.getAlias()+"]"+"No hay pistas disponibles, solicitar espera: " + suggestedWaitTime+ "ms");
                        sendLTCMessage(message, atcState, LaneTrafficControlMessageType.ATC_RUNAWAY_REQUEST_RESPONSE_REQUEST_WAIT, suggestedWaitTime);
                    }
                }
                break;
            case PLANE_TAKE_OF_SUCCESSFUL:
                ReportBESA.info("["+atcState.getAlias()+"]"+"Avion: " + message.getPlaneId()+ " despego exitosamente");
                toggleRunway(message.getRunwayId(), atcState);
                break;
            case PLANE_LAND_REQUEST:
                ReportBESA.info("["+atcState.getAlias()+"]"+"Avion: " + message.getPlaneId()+ " solicita pista de aterrizaje: "+message.getRunwayId());
                PlaneMessage planeMessage = new PlaneMessage();
                if(isRunWayAvailable(message.getRunwayId(),atcState)){
                    ReportBESA.info("["+atcState.getAlias()+"]"+"pista de aterrizaje: "+message.getRunwayId() + "disponible");
                    planeMessage = new PlaneMessage(PlaneMessageType.ATC_LAND_RESPONSE_APPROVAL);
                    planeMessage.setRunwayId(message.getRunwayId());
                    toggleRunway(message.getRunwayId(),atcState);
                } else {
                    String anotherRunwayAvailable = findAnotherRunway(atcState);
                    if(anotherRunwayAvailable != null) {
                        ReportBESA.info("["+atcState.getAlias()+"]"+"pista de aterrizaje: "+message.getRunwayId() + "no disponible, cambiando pista: "+anotherRunwayAvailable);
                        planeMessage = new PlaneMessage(PlaneMessageType.ATC_LAND_RESPONSE_APPROVAL);
                        planeMessage.setRunwayId(anotherRunwayAvailable);
                        toggleRunway(anotherRunwayAvailable,atcState);
                    } else {
                        ReportBESA.info("["+atcState.getAlias()+"]"+"sin pistas de aterrizaje, solicitando espera"+message.getRunwayId());
                        planeMessage = new PlaneMessage(PlaneMessageType.ATC_LAND_RESPONSE_REJECT);
                        // Tiempo sugerido de espera
                        planeMessage.setMetaContent("3000");
                    }
                }
                try {
                    ah = this.agent.getAdmLocal().getHandlerByAlias(message.getPlaneId());
                    EventBESA msj = new EventBESA(PlaneGuard.class.getName(),planeMessage);
                    ah.sendEvent(msj);
                }catch (ExceptionBESA e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void sendLTCMessage(AirTrafficControlMessage message, AirTrafficControlState atcState, LaneTrafficControlMessageType type, String metaInfo) {
        AgHandlerBESA ah;
        LaneTrafficControlMessage messageToSend = new LaneTrafficControlMessage(type);
        messageToSend.setPlaneId(message.getPlaneId());
        messageToSend.setRunaway(message.getRunwayId());
        messageToSend.setMetaInfo(metaInfo);
        try {
            ah = this.agent.getAdmLocal().getHandlerByAlias(atcState.getLtc().getAlias());
            EventBESA msj = new EventBESA(LaneTrafficControlGuard.class.getName(),messageToSend);
            ah.sendEvent(msj);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }

    private synchronized void toggleRunway(String runwayId, AirTrafficControlState atcState) {
        for (Runway runwayObj : atcState.getRunaways()){
            if (runwayObj.getRunawayId().equals(runwayId)){
                runwayObj.setOccupied(!runwayObj.isOccupied());
            }
        }
    }

    private synchronized boolean isRunWayAvailable(String idRunaway, AirTrafficControlState atcState) {
        return atcState.getRunaways().stream().anyMatch(
                runway -> runway.getRunawayId().equals(idRunaway) && !runway.isOccupied() && runway.isUsable()
        );
    }

    private synchronized String findAnotherRunway(AirTrafficControlState atcState){
        return atcState.getRunaways().stream().filter(runway -> runway.isUsable() && !runway.isOccupied()).map(Runway::getRunawayId).findFirst().orElse(null);
    }
}
