package avion;

import BESA.Kernel.Agent.StateBESA;
import misc.Airport;
import misc.Gate;
import misc.Itinerary;
import misc.Runway;
import utils.CabinEnum;
import utils.Functions;

import java.util.Stack;

public class PlaneState extends StateBESA {

    private Stack<Itinerary> itinerary = new Stack<>();
    private CabinEnum cabin;
    private long timeToFillInGate;
    private Airport currentAirport;
    private Gate currentGate;
    private Runway runway;
    private boolean amIFilled;
    private boolean amIFlying;
    private String alias;

    public PlaneState(CabinEnum cabin, Airport initAirport, Gate initGate, Runway initRunway) {
        this.cabin = cabin;
        this.timeToFillInGate = Functions.getRandomCabin(cabin);
        this.currentAirport = initAirport;
        this.currentGate = initGate;
        this.runway = initRunway;
        this.amIFlying = false;
        this.amIFilled = false;
    }


    public void addAirportItinerary(Itinerary itinerary) {
        this.itinerary.push(itinerary);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void discardItinerary() {
        this.itinerary.pop();
    }

    public Runway getRunway() {
        return runway;
    }

    public void setRunway(Runway runway) {
        this.runway = runway;
    }

    public Itinerary getCurrentItinerary() {
        return this.itinerary.peek();
    }

    public Airport getCurrentAirport() {
        return currentAirport;
    }

    public long getTimeToFillInGate() {
        return timeToFillInGate;
    }

    public void setCurrentAirport(Airport currentAirport) {
        this.currentAirport = currentAirport;
    }

    public Gate getCurrentGate() {
        return currentGate;
    }

    public void setCurrentGate(Gate currentGate) {
        this.currentGate = currentGate;
    }

    public boolean isAmIFlying() {
        return amIFlying;
    }

    public void setAmIFlying(boolean amIFlying) {
        this.amIFlying = amIFlying;
    }

    public void setItinerary(Stack<Itinerary> itinerary) {
        this.itinerary = itinerary;
    }

    public boolean isAmIFilled() {
        return amIFilled;
    }

    public boolean haveIAnotherItinerary(){return !this.itinerary.empty();}

    public void setAmIFilled(boolean amIFilled) {
        this.amIFilled = amIFilled;
    }

    @Override
    public String toString() {
        return "PlaneState{" +
                "itinerary=" + itinerary +
                ", cabin=" + cabin +
                ", timeToFillInGate=" + timeToFillInGate +
                ", currentAirport=" + currentAirport +
                ", currentGate=" + currentGate +
                ", amIFilled=" + amIFilled +
                ", amIFlying=" + amIFlying +
                ", alias='" + alias + '\'' +
                '}';
    }
}
