package main;

import BESA.ExceptionBESA;
import BESA.Kernel.Agent.Event.EventBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Kernel.System.AdmBESA;
import BESA.Kernel.System.Directory.AgHandlerBESA;
import BESA.Log.ReportBESA;
import atc.AirTrafficControlAgent;
import atc.AirTrafficControlGuard;
import atc.AirTrafficControlState;
import avion.PlaneAgent;
import avion.PlaneGuard;
import avion.PlaneState;
import ltc.LaneTrafficControlAgent;
import ltc.LaneTrafficControlGuard;
import ltc.LaneTrafficControlState;
import messages.MessageGeneric;
import misc.Airport;
import misc.Gate;
import misc.Itinerary;
import misc.Runway;
import utils.CabinEnum;


public class airportUniverse {

    private static final double PSSWD = 0.91;

    public static void main(String[] args) {
        try {
            AdmBESA adm = AdmBESA.getInstance();
            Airport elDorado = buildElDorado();
            // Dummy airport destination
            Airport dummyAirport = new Airport("madrid", null,null);

            // Itinerario avion 1 Este indica puertas de embarque y pistas a aterrizar en el aeropuerto de destino
            Itinerary it1 = new Itinerary("dorado-g-0","dorado-r-0",dummyAirport);

            PlaneAgent avianca01 = createPlaneAgent("avianca01",elDorado, CabinEnum.LARGE,elDorado.getGates()[0]);
            // Se adicionan los itinerarios
            avianca01.addItinerary(it1);
            // Se hace set del gate occupied solo para set up
            elDorado.getGates()[0].setOccupied(true);
            avianca01.start();


            // mensaje inicial
            AgHandlerBESA ah = adm.getHandlerByAid(avianca01.getAid());
            MessageGeneric message = new MessageGeneric("Hola");
            EventBESA msj = new EventBESA(PlaneGuard.class.getName(), message);
            ah.sendEvent(msj);


        } catch (Exception ex) {
            ReportBESA.error(ex);
        }
    }


    public static Airport buildElDorado() {
        Gate[] gates = createGates("dorado-g",5);
        Runway[] runways = createRunways("dorado-r",2);
        AirTrafficControlAgent atcDorado = createAgentATC(runways);
        LaneTrafficControlAgent ltcDorado = createAgentLTC(gates);
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

    private static  synchronized PlaneAgent createPlaneAgent(String alias,Airport airportInit, CabinEnum cabinType, Gate initGate) {
        try {
            PlaneState planeState = new PlaneState(cabinType,airportInit,initGate);
            StructBESA structPlane = new StructBESA();
            structPlane.bindGuard(PlaneGuard.class);
            return new PlaneAgent(alias,planeState,structPlane,PSSWD);
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
            return null;
        }
    }

    private static synchronized AirTrafficControlAgent createAgentATC(Runway... runways) {
        try {
            AirTrafficControlState atcState = new AirTrafficControlState(runways);
            StructBESA structATC = new StructBESA();
            structATC.bindGuard(AirTrafficControlGuard.class);
            return new AirTrafficControlAgent("",atcState,structATC,PSSWD);
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
            return null;
        }
    }

    private static synchronized LaneTrafficControlAgent createAgentLTC(Gate... gates) {
        try {
            LaneTrafficControlState ltcState = new LaneTrafficControlState(gates);
            StructBESA structLTC = new StructBESA();
            structLTC.bindGuard(LaneTrafficControlGuard.class);
            return new LaneTrafficControlAgent("",ltcState,structLTC,PSSWD);
        }catch (ExceptionBESA ex) {
            ReportBESA.error(ex);
            return null;
        }
    }

    private static synchronized Gate[] createGates(String prefix, int quantity) {
        Gate[] gates = new Gate[quantity];
        for (int i = 0; i < quantity; i++) {
            gates[i]=new Gate(prefix+"-"+i);
        }
        return gates;
    }

    private static synchronized Runway[] createRunways(String prefix, int quantity) {
        Runway[] runways = new Runway[quantity];
        for (int i = 0; i < quantity; i++) {
            runways[i]=new Runway(prefix+"-"+i);
        }
        return runways;
    }
}
