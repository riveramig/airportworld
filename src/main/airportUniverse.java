package main;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.PeriodicGuardBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import BESA.Util.PeriodicDataBESA;
import atc.AirTrafficControlAgent;
import atc.AirTrafficControlGuard;
import atc.AirTrafficControlState;
import avion.*;
import ltc.LaneTrafficControlAgent;
import ltc.LaneTrafficControlGuard;
import ltc.LaneTrafficControlState;
import messages.PlaneMessage;
import misc.Airport;
import misc.Gate;
import misc.Itinerary;
import misc.Runway;
import utils.CabinEnum;
import watcher.WatcherAgent;
import watcher.WatcherGuard;
import watcher.WatcherGuardPeriodic;
import watcher.WatcherState;


public class airportUniverse {

    private static final double PSSWD = 0.91;
    private static long PERIODIC_TIME = 1000;

    public static void main(String[] args) {

        try {
            AdmBESA adm = AdmBESA.getInstance();
            createWatcher(adm);
            Airport elDorado = buildElDorado();
            Airport madridAirport = buildMadrid();

            //------------------------------------ SET-UP Avion 1 ----------------------------------------------

            PlaneAgent avianca01 = createPlaneAgent("avianca01",elDorado, CabinEnum.LARGE, elDorado.getGates()[0], elDorado.getRunaways()[0]);
            // Se adicionan los itinerarios
            // Itinerario avion 1 Este indica puertas de embarque y pistas a aterrizar en el aeropuerto de destino
            avianca01.addItinerary(new Itinerary("dorado-g-0","dorado-r-0",elDorado,16000));
            avianca01.addItinerary(new Itinerary("madrid-g-0","madrid-r-0",madridAirport,15000));
            // Se hace set del gate occupied solo para set up
            elDorado.getGates()[0].setOccupied(true);
            initOpsPlane(avianca01);

            //-------------end set-up

            //------------------------------------ SET-UP Avion 2 ----------------------------------------------
            PlaneAgent delta02 = createPlaneAgent("delta02",elDorado, CabinEnum.SMALL, elDorado.getGates()[1], elDorado.getRunaways()[0]);
            // Se adicionan los itinerarios
            // Itinerario avion. Este indica puertas de embarque y pistas a aterrizar en el aeropuerto de destino
            delta02.addItinerary(new Itinerary("dorado-g-0","dorado-r-0",elDorado,16000));
            delta02.addItinerary(new Itinerary("madrid-g-0","madrid-r-0",madridAirport,15000));
            // Se hace set del gate occupied solo para set up
            elDorado.getGates()[1].setOccupied(true);
            initOpsPlane(delta02);


        } catch (Exception ex) {
            ReportBESA.error(ex);
        }
    }

    public static void initOpsPlane(PlaneAgent plane) {
        AdmBESA adm = AdmBESA.getInstance();
        plane.start();
        AgHandlerBESA ah = null;
        try {
            ah = adm.getHandlerByAid(plane.getAid());
            // mensaje inicial radar
            PeriodicDataBESA periodicData2 = new PeriodicDataBESA(PERIODIC_TIME, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA eventPeriodic2 = new EventBESA(PlaneGuardPeriodic.class.getName(), periodicData2);
            ah.sendEvent(eventPeriodic2);
            // mensaje inicial
            AgHandlerBESA ah2 = adm.getHandlerByAid(plane.getAid());
            PlaneMessage planeMessage1 = new PlaneMessage(PlaneMessageType.WHERE_AM_I);
            EventBESA msj2 = new EventBESA(PlaneGuard.class.getName(), planeMessage1);
            ah2.sendEvent(msj2);
        } catch (ExceptionBESA exceptionBESA) {
            exceptionBESA.printStackTrace();
        }
    }


    public static Airport buildElDorado() {
        Gate[] gates = createGates("dorado-g",5);
        Runway[] runways = createRunways("dorado-r",1);
        AirTrafficControlAgent atcDorado = createAgentATC("dorado-atc", runways);
        LaneTrafficControlAgent ltcDorado = createAgentLTC("dorado-ltc",gates);
        if(atcDorado == null || ltcDorado == null){
            throw new RuntimeException("atc o ltc del dorado son null!!");
        }
        atcDorado.setLtcAgent(ltcDorado);
        ltcDorado.setAtcAgent(atcDorado);
        atcDorado.start();
        ltcDorado.start();
        System.out.println("Dorado has been built!!!!");
        return new Airport("El dorado",atcDorado,ltcDorado,runways,gates);
    }

    public static Airport buildMadrid() {
        Gate[] gates = createGates("madrid-g",5);
        Runway[] runways = createRunways("madrid-r",1);
        AirTrafficControlAgent atcMadrid = createAgentATC("madrid-atc", runways);
        LaneTrafficControlAgent ltcMadrid = createAgentLTC("madrid-ltc",gates);
        if(atcMadrid == null || ltcMadrid == null){
            throw new RuntimeException("atc o ltc de madrid son null!!");
        }
        atcMadrid.setLtcAgent(ltcMadrid);
        ltcMadrid.setAtcAgent(atcMadrid);
        atcMadrid.start();
        ltcMadrid.start();
        System.out.println("Madrid has been built!!!!");
        return new Airport("Madrid barajas",atcMadrid,ltcMadrid,runways,gates);
    }

    private static  synchronized PlaneAgent createPlaneAgent(String alias,Airport airportInit, CabinEnum cabinType, Gate initGate, Runway initRunway) {
        try {
            PlaneState planeState = new PlaneState(cabinType,airportInit,initGate, initRunway);
            StructBESA structPlane = new StructBESA();
            structPlane.bindGuard(PlaneGuard.class);
            structPlane.bindGuard(PlaneGuardPeriodic.class);
            return new PlaneAgent(alias,planeState,structPlane,PSSWD);
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
            return null;
        }
    }

    private static synchronized AirTrafficControlAgent createAgentATC(String alias, Runway... runways) {
        try {
            AirTrafficControlState atcState = new AirTrafficControlState(runways);
            StructBESA structATC = new StructBESA();
            structATC.bindGuard(AirTrafficControlGuard.class);
            return new AirTrafficControlAgent(alias,atcState,structATC,PSSWD);
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
            return null;
        }
    }

    private static synchronized LaneTrafficControlAgent createAgentLTC(String alias, Gate... gates) {
        try {
            LaneTrafficControlState ltcState = new LaneTrafficControlState(gates);
            StructBESA structLTC = new StructBESA();
            structLTC.bindGuard(LaneTrafficControlGuard.class);
            return new LaneTrafficControlAgent(alias,ltcState,structLTC,PSSWD);
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
            return null;
        }
    }

    private static synchronized Gate[] createGates(String prefix, int quantity) {
        Gate[] gates = new Gate[quantity];
        for (int i = 0; i < quantity; i++) {
            gates[i]=new Gate(prefix+"-"+i);
            gates[i].setOccupied(false);
            gates[i].setUsable(true);
        }
        return gates;
    }

    private static synchronized Runway[] createRunways(String prefix, int quantity) {
        Runway[] runways = new Runway[quantity];
        for (int i = 0; i < quantity; i++) {
            runways[i]=new Runway(prefix+"-"+i);
            runways[i].setUsable(true);
            runways[i].setOccupied(false);
        }
        return runways;
    }

    public static synchronized void createWatcher(AdmBESA adm){
        WatcherState watcherState = new WatcherState();
        StructBESA structWatcher = new StructBESA();
        structWatcher.bindGuard(WatcherGuard.class);
        structWatcher.bindGuard(WatcherGuardPeriodic.class);
        try {
            WatcherAgent watcherAgent = new WatcherAgent("watcher",watcherState,structWatcher,PSSWD);
            watcherAgent.start();
            AgHandlerBESA ah = adm.getHandlerByAlias("watcher");
            PeriodicDataBESA periodicData2 = new PeriodicDataBESA(2000, PeriodicGuardBESA.START_PERIODIC_CALL);
            EventBESA eventPeriodic2 = new EventBESA(WatcherGuardPeriodic.class.getName(), periodicData2);
            ah.sendEvent(eventPeriodic2);
            System.out.println("watcher createddd");
        } catch (ExceptionBESA ex) {
            ex.printStackTrace();
        }
    }
}
