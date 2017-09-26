package netty.httpdemo.json.entity;

public class Order {

    private Shipping shipping;
    private User user;
    private Address address;
    private int num;
    private float total;
    private long orderId;

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public static Order createOrder(long orderId) {
        Order order = new Order();
        order.orderId = orderId;
        order.address = new Address("GuangDong", "MaoMing", "NanSheng Avenue", "China");
        order.user = new User("123", "xihao");
        order.shipping = Shipping.NORMAL;
        order.num = 10;
        order.total = 100;
        return order;
    }

    @Override
    public String toString() {
        return "Order{" +
                "shipping=" + shipping +
                ", user=" + user +
                ", address=" + address +
                ", num=" + num +
                ", total=" + total +
                ", orderId=" + orderId +
                '}';
    }
}
