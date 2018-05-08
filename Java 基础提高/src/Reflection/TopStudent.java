package Reflection;

public class TopStudent extends Student {
    private boolean isReal;
    public int grade;

//    public TopStudent() {
//
//    }

    public TopStudent(boolean isReal) {

        this.isReal = isReal;
    }

    private TopStudent(int grade){
        this.grade = grade;
    }

    public TopStudent(boolean isReal, int grade) {
        this.isReal = isReal;
        this.grade = grade;
    }

    public boolean isReal() {
        System.out.println("我是 TopStudent 的 public 方法");
        return isReal;
    }

    private void testSelfPrivate() {
        System.out.println("我是 TopStudent 的 private 方法");
    }

    public void testParams(String p1, int p2) {
        System.out.println("我是 TopStudent 的 testParams(String p1,int p2) 方法," + " p1 = " + p1 + " p2 = " + p2);
    }

    public void testParams(double p) {
        System.out.println("我是 TopStudent 的 testParams(double p) 方法 ," + " p = " + p);
    }

    public String testParams(int p) {
        System.out.println("我是 TopStudent 的 testParams(int p) 方法 ," + " p = " + p);
        return String.valueOf(p * 100);
    }
}
