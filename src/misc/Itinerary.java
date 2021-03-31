package misc;

public class Itinerary {

    private String gate;
    private String runway;
    private Airport airportTo;
    private long flightDuration;

    public Itinerary(String gate, String runway, Airport airportTo, long flightDuration) {
        this.gate = gate;
        this.runway = runway;
        this.airportTo = airportTo;
        this.flightDuration = flightDuration;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    public String getRunaWay() {
        return runway;
    }

    public void setRunaWay(String runaWay) {
        this.runway = runaWay;
    }

    public Airport getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(Airport airportTo) {
        this.airportTo = airportTo;
    }

    public long getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(long flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "gate='" + gate + '\'' +
                ", runway='" + runway + '\'' +
                ", airportTo=" + airportTo +
                ", flightDuration=" + flightDuration +
                '}';
    }
}
