package Reflection;

public class Student {
    public Student() {
    }

    public Student(Integer code, String name, double scores) {
        this.code = code;
        this.name = name;
        this.scores = scores;
    }

    public final int id  = 30;
    public static final int ID = 1000;

    public Integer code  = 90;
    private String name;
    private double scores;



    public String getName() {
        System.out.println("我是 Student 的  public 方法");
        return name;
    }

    private void testPrivate(){
        System.out.println("我是 Student 的 private 方法");
    }
}
