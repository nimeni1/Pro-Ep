package admin.service.model;

public class Car {
    private int id;
    private String driver_id;
    private int license_plate;
    private double price_km;
    private String car_type;
    private String car_model;
    private int nr_of_seats;
    private int trunk_size;

    public Car(String driver_id, int license_plate, double price_km, String car_type, String car_model, int nr_of_seats, int trunk_size) {
        this.driver_id = driver_id;
        this.license_plate = license_plate;
        this.price_km = price_km;
        this.car_type = car_type;
        this.car_model = car_model;
        this.nr_of_seats = nr_of_seats;
        this.trunk_size = trunk_size;
    }

    public Car(int id, String driver_id, int license_plate, double price_km, String car_type, String car_model, int nr_of_seats, int trunk_size) {
        this.id = id;
        this.driver_id = driver_id;
        this.license_plate = license_plate;
        this.price_km = price_km;
        this.car_type = car_type;
        this.car_model = car_model;
        this.nr_of_seats = nr_of_seats;
        this.trunk_size = trunk_size;
    }

    public Car() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public int getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(int license_plate) {
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

    public int getNr_of_seats() {
        return nr_of_seats;
    }

    public void setNr_of_seats(int nr_of_seats) {
        this.nr_of_seats = nr_of_seats;
    }

    public int getTrunk_size() {
        return trunk_size;
    }

    public void setTrunk_size(int trunk_size) {
        this.trunk_size = trunk_size;
    }
    
}