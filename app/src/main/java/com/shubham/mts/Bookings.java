package com.shubham.mts;

public class Bookings {
    private String ID;
    private String fare;
    private String num;
    private String route;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Bookings() {
        //required for Firebase
    }

    public Bookings(String ID, String fare, String num, String route) {
        this.ID = ID;
        this.fare = fare;
        this.num = num;
        this.route = route;
    }
}
