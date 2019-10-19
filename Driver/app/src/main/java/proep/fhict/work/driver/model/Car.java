package proep.fhict.work.driver.model;

public class Car {
    private String license_plate;
    private String driver_license;
    private String car_type;
    private String car_model;
    private double price_km;
    private String trunk_size;
    private String seats_number;
    private boolean available;

    public Car(String license_plate, String driver_license,double price_km, String car_type, String car_model, String seats_number, String trunk_size) {
        this.license_plate = license_plate;
        this.driver_license = driver_license;
        this.price_km = price_km;
        this.car_type = car_type;
        this.car_model = car_model;
        this.seats_number = seats_number;
        this.trunk_size = trunk_size;
        available = true;
    }

    public Car() {
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public double getPrice_km() {
        return price_km;
    }

    public void setPrice_km(double price_km) {
        this.price_km = price_km;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getSeats_number() {
        return seats_number;
    }

    public void setSeats_number(String seats_number) {
        this.seats_number = seats_number;
    }

    public String getTrunk_size() {
        return trunk_size;
    }

    public void setTrunk_size(String trunk_size) {
        this.trunk_size = trunk_size;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDriver_license() {
        return driver_license;
    }

    public void setDriver_license(String driver_license) {
        this.driver_license = driver_license;
    }
}