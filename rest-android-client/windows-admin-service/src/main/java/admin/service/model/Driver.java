package admin.service.model;

public class Driver {
    private int id;
    private Name name;
    private String license_number;
    private String email;
    private String password;
    private String phone;
    private Boolean active;
    private double average_rating;
    private double payslip;

    public Driver(int id, Name name, String license_number, String email, String password, String phone, Boolean active,
                  double average_rating, double payslip) {
        this.id = id;
        this.name = name;
        this.license_number = license_number;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.active = active;
        this.average_rating = average_rating;
        this.payslip = payslip;
    }

    public Driver(Name name, String license_number, String email, String password, String phone, Boolean active) {
        this.name = name;
        this.license_number = license_number;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.active = active;
    }

    public Driver() { }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(double average_rating) {
        this.average_rating = average_rating;
    }

    public double getPayslip() {
        return payslip;
    }

    public void setPayslip(double payslip) {
        this.payslip = payslip;
    }

}