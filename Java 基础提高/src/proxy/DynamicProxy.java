package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理的类 需要给他要代理的对象
 */
public class DynamicProxy implements InvocationHandler {


    //　这个就是我们要代理的真实对象，即房东
    private Object subject;

    //  构造方法，给我们要代理的真实对象赋初值
    public DynamicProxy(Object subject) {
        this.subject = subject;
    }

    public DynamicProxy(){

    }

    public Object bind(Object subject){
        this.subject = subject;
        return Proxy.newProxyInstance(subject.getClass().getClassLoader(),subject.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //在代理真实对象前我们可以添加一些自己的操作，中介收取中介费
        System.out.println("before "+method.getName()+" house");

        System.out.println("Method:" + method.getName());


        //如果方法是 charge 则中介收取100元中介费
        if (method.getName().equals("charge")) {

            method.invoke(subject, args);
            System.out.println("I will get 100 RMB ProxyCharge.");

        } else {
            // 当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
            method.invoke(subject, args);
        }

        //　　在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("after "+method.getName()+" house");


        return null;
    }
}
