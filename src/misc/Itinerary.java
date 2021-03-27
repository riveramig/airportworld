package misc;

public class Itinerary {

    private String gate;
    private String runway;
    private Airport airportTo;

    public Itinerary(String gate, String runway, Airport airportTo) {
        this.gate = gate;
        this.runway = runway;
        this.airportTo = airportTo;
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

    @Override
    public String toString() {
        return "Itinerary{" +
                "gate='" + gate + '\'' +
                ", runaWay='" + runway + '\'' +
                ", airportTo=" + airportTo +
                '}';
    }
}
