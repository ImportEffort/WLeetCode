package DesignMode;

public class BasicSingleTon {

    //创建唯一实例
    private static BasicSingleTon instance = new BasicSingleTon();

    //第二部暴露静态方法返回唯一实例
    public static BasicSingleTon getInstance() {
        return instance;
    }

    //第一步构造方法私有
    private BasicSingleTon() {
    }
}
