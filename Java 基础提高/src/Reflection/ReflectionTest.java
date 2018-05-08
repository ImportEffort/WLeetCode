package Reflection;

import java.lang.reflect.*;

public class ReflectionTest {


    private class Teacher {

    }

    public static void main(String[] args) {
        ReflectionTest reflectionTest = new ReflectionTest();

//        reflectionTest.testGetClass();

//        reflectionTest.testGetField();

//        reflectionTest.testGetMethod();

//        reflectionTest.testInvokeMethod();

        reflectionTest.testGetConstructor();
    }

    private void testGetConstructor() {
        Class<TopStudent> topStudentClass = TopStudent.class;
        Constructor<?>[] constructors = topStudentClass.getConstructors();
        for (Constructor constructor: constructors) {
            System.out.println("constructor = " + constructor);
        }

        Constructor<?>[] declaredConstructors = topStudentClass.getDeclaredConstructors();
        for (Constructor constructor: declaredConstructors) {
            System.out.println("constructor = " + constructor);
        }

        try {
            Constructor<TopStudent> isRealConstructor = topStudentClass.getConstructor(boolean.class);
            System.out.println("isRealConstructor = " + isRealConstructor);

            Constructor<TopStudent> gradeConstructor = topStudentClass.getDeclaredConstructor(int.class);
            System.out.println("gradeConstructor = " + gradeConstructor);
            // 如果没有空构造函数，将抛出 InstantiationException 异常
//            TopStudent topStudent = topStudentClass.newInstance();

            TopStudent topStudent = isRealConstructor.newInstance(false);
            System.out.println("topStudent.isReal = " + topStudent.isReal());

            //调用私有构造函数的时候必须把对应的 Constructor 设置为  setAccessible(true)
            gradeConstructor.setAccessible(true);
            TopStudent topStudent1 = gradeConstructor.newInstance(1000);
            System.out.println("topStudent.grade = " + topStudent1.grade);
            //传入错误的参数的个数的时候将会抛出 java.lang.IllegalArgumentException
            TopStudent errorInstance = isRealConstructor.newInstance(false, 100);


        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void testInvokeMethod() {
        try {
            Class<TopStudent> topStudentClass = TopStudent.class;
            TopStudent topStudent = topStudentClass.newInstance();

            //调用 public 方法
            Method isRealDeclareMethod = topStudentClass.getDeclaredMethod("isReal");
            isRealDeclareMethod.invoke(topStudent);

            //调用私有方法必须绕过权限检查 即需要设置对应的 Method 对象的 setAccessible 属性为 true
            Method testSelfPrivateDeclareMethod = topStudentClass.getDeclaredMethod("testSelfPrivate");
            testSelfPrivateDeclareMethod.setAccessible(true);
            testSelfPrivateDeclareMethod.invoke(topStudent);

            Method testParams1 = topStudentClass.getMethod("testParams", double.class);

            //传入错误的参数类型将会抛出 IllegalArgumentException 异常
//            testParams1.invoke(topStudent,"200");
            testParams1.invoke(topStudent, 200);

            Method testParams2 = topStudentClass.getMethod("testParams", String.class, int.class);
            testParams2.invoke(topStudent, "测试", 200);

            //这里为
            Method testParams3 = topStudentClass.getMethod("testParams", int.class);
            //returnType = class java.lang.String
            Class<?> returnType = testParams3.getReturnType();
            System.out.println("returnType = " + returnType);
            String result = (String) testParams3.invoke(topStudent, 200);
            System.out.println("result = " + result);//result = 20000

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void testGetMethod() {
        Class<TopStudent> topStudentClass = TopStudent.class;
        Method[] declaredMethods = topStudentClass.getDeclaredMethods();


        System.out.println();
        System.out.println("---------");
        System.out.println();

        for (Method method : declaredMethods) {
            System.out.println(method);
        }

        try {
            //可以正常获取自己以及父类的公有方法
            Method isRealMethod = topStudentClass.getMethod("isReal");
            Method getNameMethod = topStudentClass.getMethod("getName");

            // 尝试获取私有方法将抛出 java.lang.NoSuchMethodException 异常
            Method testSelfPrivateMethod = topStudentClass.getMethod("testSelfPrivate");
            Method testPrivateMethod = topStudentClass.getMethod("testPrivate");


            //尝试获取父类的方法 将抛出 NoSuchMethodException 异常
            Method getNameDeclareMethod = topStudentClass.getDeclaredMethod("getName");

            // 可以获取私有方法将抛出 以及公有方法
            Method isRealDeclareMethod = topStudentClass.getDeclaredMethod("isReal");
            Method testSelfPrivateDeclareMethod = topStudentClass.getDeclaredMethod("testSelfPrivate");


            //  getMethod(String name, Class<?>... parameterTypes)
            Method testParams1 = topStudentClass.getMethod("testParams", double.class);
            Method testParams2 = topStudentClass.getMethod("testParams", String.class, int.class);
            //获取并不存在的重载方法 将抛出 java.lang.NoSuchMethodException
            Method testParams3 = topStudentClass.getMethod("testParams");

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void testGetField() {
        try {
            Class<TopStudent> topStudentClass = TopStudent.class;
//
            //id public 属性定义在父类 Student 中
//            Field id = topStudentClass.getField("id");
//            //grade public 属性定义在 TopStudent 中
//            Field grade = topStudentClass.getField("grade");
//            //isReal private 属性定义在 TopStudent 中 无法获得私有属性将抛出NoSuchFieldException: isReal
//            Field isReal = topStudentClass.getField("isReal");
//
//            //id public 属性定义在父类 Student 中 无法获得 public 父类属性 java.lang.NoSuchFieldException: name
//            Field  declaredId = topStudentClass.getDeclaredField("id");
//            //grade public 属性定义在 TopStudent 中
//            Field declaredGrade = topStudentClass.getDeclaredField("grade");
//            //isReal private 属性定义在 TopStudent 中
//            Field declaredIsReal = topStudentClass.getDeclaredField("isReal");
//
//            Field[] fields = topStudentClass.getFields();
//            Field[] declaredFields = topStudentClass.getDeclaredFields();
//
//            //fields = [public int Reflection.TopStudent.grade, public int Reflection.Student.id]
//            System.out.println("fields = " + Arrays.toString(fields));
//
//            // grade  id
//            for (Field field : fields) {
//                System.out.println("" + field.getName());
//            }
//
//            //declaredFields = [private boolean Reflection.TopStudent.isReal, public int Reflection.TopStudent.grade]
//            System.out.println("declaredFields = " + Arrays.toString(declaredFields));
//            //isReal grade
//            for (Field field : declaredFields) {
//                System.out.println("" + field.getName());
//            }


            TopStudent topStudent = topStudentClass.newInstance();

 /*           Field grade = topStudentClass.getDeclaredField("grade");

            grade.set(topStudent, 4);
            //Can not set int field Reflection.TopStudent.grade to java.lang.Float
//            grade.set(topStudent,4.0f);
            System.out.println("grade = " + grade.get(topStudent));

            Class<?> type = grade.getType();
            Type genericType = grade.getGenericType();
            System.out.println("type = " + type);
            System.out.println("genericType = " + genericType);
            //如果我们知道对应的变量为基本类型变量的某个类型可以使用 setXXX 的等价方法
             grade.setInt(topStudent,4);*/
            //Can not set int field Reflection.TopStudent.grade to (float)4.0
            //grade.setFloat(topStudent,4);

            // 测试 自动拆箱装箱
//            Field code = topStudentClass.getField("code");
//            //装箱成功
//            code.set(topStudent,100);
//            //无法自动装箱 Can not set java.lang.Integer field Reflection.Student.code to (int)200
//            code.setInt(topStudent,200);
//            int codeVal = (int) code.get(topStudent);
//            System.out.println("codeVal = " + codeVal);

            //测试 set final
            Field id = topStudentClass.getField("id");
            // 如果不设置 setAccessible(true) 将抛出 IllegalAccessException
            // 设置 settAccessible 将绕过检查
            id.setAccessible(true);
            id.set(topStudent, 100);
            int idVal = (int) id.get(topStudent);
            System.out.println("idVal = " + idVal);
            System.out.println("idVal = " + topStudent.id);

            Field code = topStudentClass.getField("code");
            code.setAccessible(true);
            code.set(topStudent, 100);
            int codeVal = (int) code.get(topStudent);
            System.out.println("codeVal = " + codeVal);
            System.out.println("codeVal = " + topStudent.code);

            Field ID = topStudentClass.getField("ID");

            //即使设置了setAccessible(true) 也会抛出 IllegalAccessException
//            ID.setAccessible(true);

            //通过反射将对应成员变量的 final 修饰去掉
            Field modifiersField = Field.class.getDeclaredField("modifiers"); //①
            modifiersField.setAccessible(true);
            modifiersField.setInt(ID, ID.getModifiers() & ~Modifier.FINAL); //②

            ID.set(topStudent, 100);
            int IDVal = (int) id.get(topStudent);
            System.out.println("IDVal = " + IDVal);
            System.out.println("IDVal = " + TopStudent.ID);

           /* Field isReal = topStudentClass.getDeclaredField("isReal");
            isReal.setAccessible(true);
            // 如果不设置isReal.setAccessible(true);
            // 将会抛出 can not access a member of class Reflection.TopStudent with modifiers "private"异常
            isReal.set(topStudent, true);
            boolean isRealValue = (boolean) isReal.get(topStudent);
            System.out.println("isRealValue = " + isRealValue);

            int gradeValue = grade.getInt(topStudent);
            System.out.println("gradeValue  " + gradeValue);*/


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


    }

    private void testGetClass() {
        try {
            Class<Student> studentClass = Student.class;
            //java 中内部类的全路径命名是 OuterClassName$InnerClassName
            Class<?> tearcherClass = Class.forName("Reflection.ReflectionTest$Teacher");

            Teacher teacher = new Teacher();
            //tearcherClass.isInstance(teacher) true
            System.out.println("tearcherClass.isInstance(teacher)" + tearcherClass.isInstance(teacher));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
