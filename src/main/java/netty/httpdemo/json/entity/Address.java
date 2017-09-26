package netty.httpdemo.json.entity;

public class Address {

    private String province;
    private String city;
    private String avenue;
    private String country;

    public Address(String province, String city, String avenue, String country) {
        this.province = province;
        this.city = city;
        this.avenue = avenue;
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvenue() {
        return avenue;
    }

    public void setAvenue(String avenue) {
        this.avenue = avenue;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", avenue='" + avenue + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
