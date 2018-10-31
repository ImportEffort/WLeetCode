package DesignMode;

public class StaticInnerSingleTon {

    private static class InnerStaticClass{
        private static StaticInnerSingleTon singleTon  = new StaticInnerSingleTon();
    }

    public StaticInnerSingleTon getInstance(){
        return InnerStaticClass.singleTon; //引用一个类的静态成员，将会触发该类的初始化
    }

    private StaticInnerSingleTon() {
    }
}
