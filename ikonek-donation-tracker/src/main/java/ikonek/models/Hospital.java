package ikonek.models;

public class Hospital {
    private int hospitalId;
    private String name;
    private String city;
    private String province;
    private String contactNumber;

    // Constructor
    public Hospital(String name, String city, String province, String contactNumber) {
        this.name = name;
        this.city = city;
        this.province = province;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalId=" + hospitalId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}