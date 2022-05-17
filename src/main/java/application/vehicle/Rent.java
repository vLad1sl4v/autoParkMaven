package application.vehicle;

import java.util.Date;

public class Rent {
    private Date rentDate;
    private Double rentCost;
    private int id;

    public Rent() {

    }

    public Rent(int id, Date rentDate, Double rentCost) {
        this.id = id;
        this.rentDate = rentDate;
        this.rentCost = rentCost;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Double getRentCost() {
        return rentCost;
    }

    public void setRentCost(Double rentCost) {
        this.rentCost = rentCost;
    }
}
