package proxy;

/**
 * 被代理的类，必须实现代理接口中的方法
 */
public  class HouseOwner implements RentHouse {

    @Override
    public void rent() {
        System.out.println("我要出租房子");
    }

    @Override
    public void charge(String str) {
        System.out.println("你需要支付  " + str + "  RMB 费用");
    }

    public void abstractMethod(){
        System.out.println("自己的方法");

    }

}
