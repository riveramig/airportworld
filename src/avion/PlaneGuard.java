package avion;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.GuardBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import atc.AirTrafficControlGuard;
import atc.AirTrafficControlType;
import ltc.LaneTrafficControlGuard;
import ltc.LaneTrafficControlMessageType;
import messages.AirTrafficControlMessage;
import messages.LaneTrafficControlMessage;
import messages.PlaneMessage;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlaneGuard extends GuardBESA {
    @Override
    public void funcExecGuard(EventBESA eventBESA) {
        PlaneMessage message = (PlaneMessage) eventBESA.getData();
        PlaneState planeState = (PlaneState) this.agent.getState();
        ScheduledThreadPoolExecutor exec;
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
                ReportBESA.info("["+planeState.getAlias()+"]"+"Llenando avion " + planeState.getTimeToFillInGate() + " milisegundos");
                exec = new ScheduledThreadPoolExecutor(1);
                exec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        ReportBESA.info("["+planeState.getAlias()+"]"+"Avion lleno");
                        ReportBESA.info("["+planeState.getAlias()+"]"+"Enviar LTC peticion de despegue....");
                        sendLTCTakeOfMessage(planeState);
                    }
                },planeState.getTimeToFillInGate(), TimeUnit.MILLISECONDS);
                exec.shutdown();
                break;
            case TAKE_OF:
                ReportBESA.info("["+planeState.getAlias()+"]"+"Despegando en pista " + message.getRunwayId());
                exec = new ScheduledThreadPoolExecutor(1);
                AgentBESA agent = this.agent;
                exec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ReportBESA.info("["+planeState.getAlias()+"]"+"Notificando ATC despegue exitoso...");
                            AgHandlerBESA ah1 =agent.getAdmLocal().getHandlerByAlias(planeState.getCurrentAirport().getAtcAgent().getAlias());
                            AirTrafficControlMessage messageToATC = new AirTrafficControlMessage(AirTrafficControlType.PLANE_TAKE_OF_SUCCESSFUL);
                            messageToATC.setPlaneId(planeState.getAlias());
                            messageToATC.setRunwayId(message.getRunwayId());
                            EventBESA msj = new EventBESA(AirTrafficControlGuard.class.getName(),messageToATC);
                            ah1.sendEvent(msj);
                            planeState.setAmIFlying(true);
                            // Se hace set de la pista y puerta de embarque en aeropuerto de destino
                            String nextRunway = planeState.getCurrentItinerary().getRunaWay();
                            String nextGate = planeState.getCurrentItinerary().getGate();
                            planeState.setRunway(planeState.getCurrentItinerary().getAirportTo().findRunwayById(nextRunway));
                            planeState.setCurrentGate(planeState.getCurrentItinerary().getAirportTo().findGateByName(nextGate));
                            planeState.setCurrentAirport(planeState.getCurrentItinerary().getAirportTo());
                            // mensaje a guarda que estoy volando que contiene el tiempo total de vuelo
                            AgHandlerBESA ah2 =agent.getAdmLocal().getHandlerByAlias(planeState.getAlias());
                            PlaneMessage planeMessage = new PlaneMessage(PlaneMessageType.IM_FLYING);
                            planeMessage.setMetaContent(planeState.getCurrentItinerary().getFlightDuration()+"");
                            EventBESA msj2 = new EventBESA(PlaneGuard.class.getName(),planeMessage);
                            ah2.sendEvent(msj2);
                            // Se descarta el itinerario luego que se hace set de la pista
                            planeState.discardItinerary();
                        }catch (ExceptionBESA exceptionBESA) {
                            exceptionBESA.printStackTrace();
                        }
                    }
                },2000, TimeUnit.MILLISECONDS);
                exec.shutdown();
                break;
            case WAIT_TAKE_OF:
                ReportBESA.info("["+planeState.getAlias()+"]"+"Esperando " + message.getMetaContent());
                exec = new ScheduledThreadPoolExecutor(1);
                exec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        ReportBESA.info("["+planeState.getAlias()+"]"+"Re-intentar LTC peticion de despegue....");
                        sendLTCTakeOfMessage(planeState);
                    }
                },Long.parseLong(message.getMetaContent()), TimeUnit.MILLISECONDS);
                exec.shutdown();
                break;
            case IM_FLYING:
                ReportBESA.info("["+planeState.getAlias()+"]"+"estoy volando hacia ------->" + planeState.getCurrentAirport().getName());
                exec = new ScheduledThreadPoolExecutor(1);
                AgentBESA agent2 = this.agent;
                exec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        ReportBESA.info("["+planeState.getAlias()+"]"+"Enviar solicitud aterrizaje a: "+planeState.getCurrentAirport().getAtcAgent().getAlias());
                        AirTrafficControlMessage airTrafficControlMessage = new AirTrafficControlMessage(AirTrafficControlType.PLANE_LAND_REQUEST);
                        airTrafficControlMessage.setPlaneId(planeState.getAlias());
                        airTrafficControlMessage.setRunwayId(planeState.getRunway().getRunawayId());
                        try {
                            AgHandlerBESA ah1 =agent2.getAdmLocal().getHandlerByAlias(planeState.getCurrentAirport().getAtcAgent().getAlias());
                            EventBESA msj = new EventBESA(AirTrafficControlGuard.class.getName(),airTrafficControlMessage);
                            ah1.sendEvent(msj);
                        }catch (ExceptionBESA exceptionBESA) {
                        exceptionBESA.printStackTrace();
                        }
                    }
                },Long.parseLong(message.getMetaContent()), TimeUnit.MILLISECONDS );
                exec.shutdown();
                break;
            case ATC_LAND_RESPONSE_APPROVAL:
                ReportBESA.info("["+planeState.getAlias()+"]"+"aterrizando en pista: " + message.getRunwayId());
                exec = new ScheduledThreadPoolExecutor(1);
                exec.schedule(new Runnable() {
                    @Override
                    public void run() {
                        planeState.setAmIFlying(false);
                        ReportBESA.info("["+planeState.getAlias()+"]"+"aterrizaje exitoso");
                    }
                },3000, TimeUnit.MILLISECONDS );
                exec.shutdown();
                break;
            default:
                ReportBESA.info("No entendi");
        }

    }

    private void sendLTCTakeOfMessage(PlaneState planeState) {
        AgHandlerBESA ah;
        try {
            ah =this.agent.getAdmLocal().getHandlerByAlias(planeState.getCurrentAirport().getLtcAgent().getAlias());
            LaneTrafficControlMessage messageToLTC = new LaneTrafficControlMessage(LaneTrafficControlMessageType.PLANE_RUNWAY_TAKEOFF_REQUEST,planeState.getAlias());
            messageToLTC.setRunaway(planeState.getRunway().getRunawayId());
            EventBESA msj = new EventBESA(LaneTrafficControlGuard.class.getName(),messageToLTC);
            ah.sendEvent(msj);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }
}
