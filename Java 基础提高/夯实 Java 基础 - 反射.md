# 夯实 Java 基础 - 反射

>自嵌套 Fragment 懒加载文章至今已经已经一个星期过去了，说实话最近对于学习的热情有点衰减，也可能是自己有点飘了，也有可能是现实中的诱惑多了点，但是这是个不好的状态，必须调整自己向着目标继续前进。

## 前言

本文将重拾 Java 基础中的反射知识，由于笔者是做移动端 Android 开发的，日常工作中反射用的少的可以拿手指头数过来。现在我所记得的上次使用它应该是在修改 TabLayout 的下划线宽度的时候。这次重拾反射这部分知识的主要原因其实是在注解和动态代理。相比反射来说这两者在我们日常使用的框架中比较常见，比如 EventBus 原理，ButterKnife 原理，Retrofit原理，甚至 AOP（面向切面编程） 在 Android 中的应用，都有这两者的身影。

本文其实没有什么营养价值，和其他相关文章一样，本文更多的是在记录反射的 API，这么做更多可能是为了以后遗忘了方便查阅。本文将从以下几个方面来记录：

1. 反射是什么？ 反射的意义？
2. 反射的入口 -  Class 类
2. 反射的成员变量获取 - Field 类
3. 反射的方法获取 - Method 类
4. 反射的构造方法获取 - Constructor 类
5. 反射的获取注解相关信息

## 反射机制

试着解释清楚为什么需要反射是并不简单，这里涉及了一些 jvm 类加载机制，那么从先前说的修改 TabLayout 的下划线宽度说起，首先 TabLayout 是存在 SDK 中并不是我们定义的一个类，但是在使用中我们遇到了要修改其内容的需求，这时候我们通过反射在程序运行时获取了其内部私有变量 `mTabStrip` ，并修改了他的 `LayoutParams`。我们知道我们通过 tablayout.mTabStrip 是无法访问的，因为变量是私有的。

```
Field tabStrip = tablayout.getDeclaredField("mTabStrip");
tabStrip.setAccessible(true);
...
do reset padding LayoutParams operation
```

当然这只是我遇到的一个使用场景，做 JavaWeb 的朋友应该对此有更深的了解。

那么这里记录下什么是反射的官方说法:

> 通过反射，我们可以在**运行时**获得程序或程序集中每一个类型的成员和成员的信息。Java 反射机制可以动态地创建对象并调用其属性，即使这个对象的类型在编译期是未知的。

通过 Java 的反射机制我们可以：

1. 在运行时判断任意一个对象所属的类；
2. 在运行时构造任意一个类的对象；
3. 在运行时获取任意一个类所具有的成员变量和方法，即使它是私有的；
4. 在运行时调用任意一个对象的方法；

注意我们所提到的前提和重点是运行时。

## 反射的入口 -  Class 类


### 如何获取 Class 类
想要通过反射操作一个类的成员或者方法，Java 为我们提供了一套 API 而这套 API 属于 Class 类，作为一些列反射操作的入口，Java 提供了三个方法或获得某个对象或者类的 Class 类对象：

- **使用 Object 的 `getClass` 方法**，如我们已知一个类的对象我们可以使用超类的 getClass 方法获取：

```
TabLayout tabLayout = findViewById(R.id.tabLayout);
Class<?> tabLayoutClass = tabLayout.getClass();
```

- **我们也可以通过 `XXX.class` 的方式直接获取某个类的 Class 对象而无需创建该类的对象。**

```
//对于普通引用数据类型的类我们可以如下调用
Class<?> tabLayoutClass = TabLayout.class;

//而对于基本数据类型我们可以使用 XXX.TYPE 的方式调用
Class<?> classInt = Integer.TYPE;
```
- **通过  Class 的静态方法 `Class.forName(String name)` 方法传入一个类的全量限定名获得创建。**该方法会要求抛出或者捕获`ClassNotFoundException`异常。

```
Class c3 = Class.forName("android.support.design.widget.TabLayout");
```
## 通过反射获取目标的类的成员变量

Class 类可以帮助我们在只知道一个类的名字的时候，获取该类的成员变量，及时某些成员变量是私有的。我们假设只知道一个类的名字，并不知道其内部构成，也就是说内部没有 API 列表提供给我们。Java Class 类提供了4种方法，来获取这些成员变量。


|  方法名称 | 返回值类型 | 是否包含获得继承的属性 | 是否可以获得私有属性 |
| --- | --- | --: | --: |
| `getField()` | `Field` | YES | NO |
| `getDeclaredField()` | `Field` | NO | YES |
| `getFields()`  | `Field[]` | YES | NO |
| `getDeclaredFields()` | `Field[]` | NO | YES |


- **测试 `getField() / getDeclaredField()`**

```java
  Class<TopStudent> topStudentClass = TopStudent.class;
  
  //id public 属性定义在父类 Student 中
  Field id = topStudentClass.getField("id");
  //grade public 属性定义在 TopStudent 中
  Field grade = topStudentClass.getField("grade");
  //isReal private 属性定义在 TopStudent 中 无法获得私有属性将抛出NoSuchFieldException: isReal
  Field isReal = topStudentClass.getField("isReal");

  //id public 属性定义在父类 Student 中 无法获得 public 父类属性 java.lang.NoSuchFieldException: id
  Field  declaredId = topStudentClass.getDeclaredField("id");
  //grade public 属性定义在 TopStudent 中
  Field declaredGrade = topStudentClass.getDeclaredField("grade");
  //isReal private 属性定义在 TopStudent 中
  Field declaredIsReal = topStudentClass.getDeclaredField("isReal");
```

- **测试 `getFields() / getDeclaredFields()`**

```java
  Field[] fields = topStudentClass.getFields();
  Field[] declaredFields = topStudentClass.getDeclaredFields();

  //fields = [public int Reflection.TopStudent.grade, public int Reflection.Student.id]
  System.out.println("fields = " + Arrays.toString(fields));

  // grade  id
  for (Field field : fields) {
      System.out.println("" + field.getName());
  }

  //declaredFields = [private boolean Reflection.TopStudent.isReal, public int Reflection.TopStudent.grade]
  System.out.println("declaredFields = " + Arrays.toString(declaredFields));
  //isReal grade
  for (Field field : declaredFields) {
      System.out.println("" + field.getName());
  }
```

事实上我们通过反射获取到属性以后，下一步可能是要获取或者修改该属性的值，Field 类也为我们准备了相应的 set 和 get 方法。同时 set 方法作用于私有属性的时候将抛出 `IllegalAccessException`异常。此时我们需要通过。

同时如果我们预先不知道该属性的类型的时候我们也可以通过 `getType/getGenericType` 来获取该属性的类型,后者在属性为泛型表示的属性时获取泛型的通用符号如果不是则返回值与 getType 内容相同。

```java
  TopStudent topStudent = topStudentClass.newInstance();

  Field grade = topStudentClass.getDeclaredField("grade");
  
  grade.set(topStudent, 4);
  //Can not set int field Reflection.TopStudent.grade to java.lang.Float
 // grade.set(topStudent,4.0f);
  System.out.println("grade = " + grade.get(topStudent));

  Class<?> type = grade.getType();
  Type genericType = grade.getGenericType();
  System.out.println("type = " + type);
  System.out.println("genericType = " + genericType);
  //如果我们知道对应的变量为基本类型变量的某个类型可以使用 setXXX 的等价方法
   grade.setInt(topStudent,4);
  //Can not set int field Reflection.TopStudent.grade to (float)4.0
  //grade.setFloat(topStudent,4);


 //再给私有属性设置值的时候要记得设置 isAccessible 为 true
  Field isReal = topStudentClass.getDeclaredField("isReal");
  isReal.setAccessible(true);
  // 如果不设置isReal.setAccessible(true);
  // 将会抛出 can not access a member of class Reflection.TopStudent with modifiers "private"异常
  isReal.set(topStudent, true);
  boolean isRealValue = (boolean) isReal.get(topStudent);
  System.out.println("isRealValue = " + isRealValue);

  int gradeValue = grade.getInt(topStudent);
  System.out.println("gradeValue  " + gradeValue);
```

### 自动装箱导致的 IllegalArgumentException 异常

值得注意的是我们当我反射的类某个属性为基本数据类型的包装类的时候，我们无法使用 setXXX 直接设置该数值，将抛出`java.lang.IllegalArgumentException` ，使用 `set(Object obj,Object val)` 则可以直接运行，原因在于 setInt 等方法无法为我们做自动装箱的操作，而后者则可以：

```
 // 测试 自动拆箱装箱
  Field code = topStudentClass.getField("code");
  //装箱成功
  code.set(topStudent,100);
  //无法自动装箱 Can not set java.lang.Integer field Reflection.Student.code to (int)200
  code.setInt(topStudent,200);
  int codeVal = (int) code.get(topStudent);
  System.out.println("codeVal = " + codeVal);
```
### 对于 final 修饰的变量改如何修改

从代码编写角度来看，如果我们将一个成员变量定义为 final 代表我们不希望有人可以修改它的值，但是实际的需求谁又能考虑到这里多呢？好在通过反射我们也可以修改某个 final 成员变量。当然需要注意的地方比较多。
 
 > 对于 Java 基本数据类型 以及用使用 String str = "111" 赋值的成员变量，在编译期 JVM 对其做了内联优化，可以简单的理解为编译后就写死在.class 文件中了，我们并不能修改成功 final 的成员变量。
 
 > 对于非上述两种情况是可以修改成功的
 
 如
 
```
//测试 set final

public class Student {
    public final int id  = 30;
    public final Integer cod  = 90;
    public static final int ID = 1000;
    ...
}

Field id = topStudentClass.getField("id");
// 如果不设置 setAccessible(true) 将抛出 IllegalAccessException
// 设置 settAccessible 将绕过检查
id.setAccessible(true);
id.set(topStudent,100);
int idVal = (int) id.get(topStudent);
System.out.println("idVal = " + idVal);//100
System.out.println("idVal = " + topStudent.id);//30 修改失败

Field code = topStudentClass.getField("code");
code.setAccessible(true);
code.set(topStudent,100);
int codeVal = (int) code.get(topStudent);
System.out.println("codeVal = " + codeVal);//100
System.out.println("codeVal = " + topStudent.code);//100 修改成功


Field ID = topStudentClass.getField("ID");

//即使设置了setAccessible(true) 也会抛出 IllegalAccessException
//ID.setAccessible(true);

//通过反射将对应成员变量的 final 修饰去掉
Field modifiersField = Field.class.getDeclaredField("modifiers"); 
modifiersField.setAccessible(true);
modifiersField.setInt(ID, ID.getModifiers() & ~Modifier.FINAL); 

ID.set(topStudent,100);
int IDVal = (int) id.get(topStudent);
System.out.println("IDVal = " + IDVal);//100
System.out.println("IDVal = " + TopStudent.ID);//1000 修改失败
``` 

结论，没事不要乱改 final 万一给后人给自己挖了个大坑埋了呢。

## 通过反射获取目标类的成员方法

### 获取目标类的成员方法

除了通过 Class 获取成员变量，通过反射也可以获取一个类的所有成员方法。与后去成员变量一样，获取
成员方法也有 4 个方法：

|  方法名称 | 返回值类型 | 是否包含获得父类方法 | 是否可以获得私有方法 |
| --- | --- | --: | --: |
| `getMethod()` | `Method` | YES | NO |
| `getDeclaredMethod()` | `Method` | NO | YES |
| `getMethods()`  | `Method[]` | YES | NO |
| `getDeclaredMethods()` | `Method[]` | NO | YES |

假设我们设置两个类它们如下：

```
public class Student {
    .....
    private String name;
    .....
    
    public String getName() {
        System.out.println("我是 Student 的  public 方法");
        return name;
    }

    private void testPrivate(){
        System.out.println("我是 Student 的 private 方法");
    }
}

public class TopStudent extends Student {
    private boolean isReal;
    public int grade;

    public boolean isReal() {
        System.out.println("我是 TopStudent 的 public 方法");
        return isReal;
    }

    private void testSelfPrivate(){
        System.out.println("我是 TopStudent 的 private 方法");
    }
}

```

我们尝试用 `getMethods()/getDeclaredMethods()` 获取 TopStudent 类所包含的方法：

```
private void testGetMethod() {
   Class<TopStudent> topStudentClass = TopStudent.class;
   Method[] methods = topStudentClass.getMethods();
   Method[] declaredMethods = topStudentClass.getDeclaredMethods();

   for (Method method: methods) {
       System.out.println(method.getName());
   }

   System.out.println("---------");

   for (Method method: declaredMethods) {
       System.out.println(method.getName());
   }
}
```

从打印结果可以看出 `getMethods()` 方法获取的 method 包含所有父类和当前类的所有 Public 方法，而 `getDeclaredMethods()` 获取的 method 仅包含当前类的所有方法。咦好像没有办法获取父类的 private 方法的途径，什么？ 子类根本就无法继承父类的私有方法好伐。

```
/*topStudentClass.getMethods 获取的 method 包含所有父类和当前类的所有 Public 方法*/

public boolean Reflection.TopStudent.isReal()
public java.lang.String Reflection.Student.getName()
public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
public final void java.lang.Object.wait() throws java.lang.InterruptedException
public boolean java.lang.Object.equals(java.lang.Object)
public java.lang.String java.lang.Object.toString()
public native int java.lang.Object.hashCode()
public final native java.lang.Class java.lang.Object.getClass()
public final native void java.lang.Object.notify()
public final native void java.lang.Object.notifyAll()

/*topStudentClass.getDeclaredMethods 获取的 method 仅包含当前类的所有方法*/

boolean Reflection.TopStudent.isReal()
private void Reflection.TopStudent.testSelfPrivate()方法*/

```

同样我们还可以获取某个类的单个方法通过 Class 提供给我们的 getMethod 和 getDeclaredMethod 这两个方法都带有两个参数,第一个参数为方法名 "name",第二个参数为对应方法需要 **传入的参数的Class 对象** 即 "Class<?>... parameterTypes"。当我们尝试获取一个并不存在的方法时，将会抛出`NoSuchMethodException` 异常。

我们为 TopStudent 添加两个方法用于测试

```
public void testParams(String p1,int p2){}

public void testParams(double p){}
```


```java
   try {
       // getMethod 可以正常获取自己以及父类的公有方法
       Method isRealMethod = topStudentClass.getMethod("isReal");
       Method getNameMethod = topStudentClass.getMethod("getName");

       // 尝试获取私有方法将抛出 java.lang.NoSuchMethodException 异常
       Method testSelfPrivateMethod = topStudentClass.getMethod("testSelfPrivate");
       Method testPrivateMethod = topStudentClass.getMethod("testPrivate");


       //尝试获取父类的方法 将抛出 NoSuchMethodException 异常
       Method getNameDeclareMethod = topStudentClass.getDeclaredMethod("getName");

       // getDeclaredMethod 可以获取私有方法将抛出 以及公有方法
       Method isRealDeclareMethod = topStudentClass.getDeclaredMethod("isReal");
       Method testSelfPrivateDeclareMethod = topStudentClass.getDeclaredMethod("testSelfPrivate");
        
        //重载方法的测试
        Method testParams1 = topStudentClass.getMethod("testParams", double.class);
       Method testParams2 = topStudentClass.getMethod("testParams", String.class, int.class);
       //获取并不存在的重载方法 将抛出 java.lang.NoSuchMethodException
       Method testParams3 = topStudentClass.getMethod("testParams");

   } catch (NoSuchMethodException e) {
       e.printStackTrace();
   }
```

### 调用目标类的成员方法

由于我们上文说过了 getMethod 和 getDeclaredMethod 方法的区别了，为了我们正常获取对应的方法去掉用，我们需要使用对应的方法。

我们获取到了指定了 Class 的成员方法后可以通过 Method 的 

> `Object invoke(Object obj, Object... args)` 

方法来调用指定类的对象的方法。第一个参数为该类的对象，第二个可变参数为该方法的参数，而返回值即所调用的方法的返回值，通常需要我们强转为指定参数类型。而我们还可以通过 Method 的 `getReturnType` 方法来获取返回值类型。

另外还需要注意的是，私有成员方法和私有变量一样，获取可以，但是当我们需要访问修改的时候，必须要绕过权限检查即设置：`method.setAccessible(true)`


下面我们来一个例子：


```java

//为 TopStudent 添加 testParams 测重载方法
public String testParams(int p) {
   System.out.println("我是 TopStudent 的 testParams(int p) 方法 ," + " p = " + p);
   return String.valueOf(p * 100);
}

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
       //testParams1.invoke(topStudent,"200");
       testParams1.invoke(topStudent, 200);

       Method testParams2 = topStudentClass.getMethod("testParams", String.class, int.class);
       testParams2.invoke(topStudent, "测试", 200);

       Method testParams3 = topStudentClass.getMethod("testParams", int.class);
       Class<?> returnType = testParams3.getReturnType();
        //returnType = class java.lang.String
       System.out.println("returnType = " + returnType);
       String result = (String) testParams3.invoke(topStudent, 200);//result = 20000

       System.out.println("result = " + result);
       
  } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
       e.printStackTrace();
   }
```


## 通过反射获取目标类的构造函数

通过反射获取构造函数的方法同样有4个，分别为

|  方法名称 | 返回值类型  | 是否可以获得私有方法 |
| --- | --- | --: | --: |
| `getConstructor()` | `Constructor<?>` | NO |
| `getDeclaredConstructor()` | `Constructor<?>`  | YES |
| `getConstructors()`  | `Constructor<?>[]`  | NO |
| `getDeclaredConstructors()` | `Constructor<?>[]`  | YES |

对于构造方法的获取这里没有指出是否可以获得父类的构造方法，因为 java 规定，子类无法继承父类的构造方法。而对于访问修饰符的限制，这里跟上文的普通成员函数没有什么区别。

如：


```java
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

       TopStudent topStudent = isRealConstructor.newInstance(false);
       System.out.println("topStudent.isReal = " + topStudent.isReal()); 
    }catch (NoSuchMethodException) {
        e.printStackTrace();
    }
       
```

运行结果

```
constructor = public Reflection.TopStudent(boolean,int)
constructor = public Reflection.TopStudent(boolean)

constructor = public Reflection.TopStudent(boolean,int)
constructor = private Reflection.TopStudent(int)
constructor = public Reflection.TopStudent(boolean)

isRealConstructor = public Reflection.TopStudent(boolean)
gradeConstructor = private Reflection.TopStudent(int)

```

而我们之前说过通过 `Class.newInstance()` 可以创建一个类的对象，但是如果一个类并没有提供空参数的构造方法，那么这个方法将抛出 `InstantiationException` 异常。此时我们就可以通过获取其他参数构造函数的方法来获得对应的 `Constructor` 对象来调用 `Constructor.newInstance(Object... obj)` 

此方法接受对应的构造函数的参数类型的对象，如果传递的参数个数以及类型错误将抛出`IllegalArgumentException`，类似于 invoke 方法所抛出的异常。


```
try {

        // 如果没有空构造函数，将抛出 InstantiationException 异常
        //  TopStudent topStudent = topStudentClass.newInstance();
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
```

## 通过 Class 做类型判断

在不使用反射的时候，我们会用 `instanceof` 关键字来判断是否为某个类的实例。当我们通过上面的方法获取了一个对象的 Class 对象，也可以Class对象的 `isInstance()` 方法来判断是否为某个类的实例，它是一个 Native方法,使用方法如下：

```
try {
       Class<Student> studentClass = Student.class;
       //java 中内部类的全路径命名是 OuterClassName$InnerClassName
       Class<?> tearcherClass = Class.forName("ReflectionTest$Teacher");

       Teacher teacher = new Teacher();
       //tearcherClass.isInstance(teacher) true 
       System.out.println("tearcherClass.isInstance(teacher)" + tearcherClass.isInstance(teacher));
   } catch (ClassNotFoundException e) {
       e.printStackTrace();
   }
```

## 通过反射获取注解相关信息

开头提到，学习反射可能更大目的不是应用于日常开发，而是为了学习一些三方库的源码，而这些源码中，反射往往都是伴随着注解一块使用的，这篇文章我们暂时不拿注解展开说，只简单的说下`Annotation` 相关的反射 API:

首先在 `java.lang.reflect` 包下有一个跟提取注解非常相关的接口，它就是 `AnnotatedElement` 接口，那么实现该接口的对象有哪些呢？其实它包含了上述我们所说的 `Class`、`Constructor`、 `Field`、`Method`几个类。其实了解注解可以修饰哪些成员的朋友对此并不难理解，注解可以修饰一个类，一个方法，一个成员，所以当我们需要自定义注解的时候，如果拿到对应的成员或者类的注解便是关键。

`AnnotatedElement` 接口定义了一下几个方法：


| 方法名 | 参数 | 返回值 | 作用 |
| --- | --- | :-: | :-: |
| `isAnnotationPresent` | 注解修饰的元素的 Class | boolean | 检测该元素是否被参数对应注解修饰
| `getAnnotation` | 注解修饰的元素的 Class |  Annotation |  返回注解对象 |
| `getAnnotations` | 无 | Annotation[]  | 返回该程序元素上存在的所有注解（如果没有注解存在于此元素上，则返回长度为零的一个数组。） |
|  |  |  |  |
| `getDeclaredAnnotations` | 无 | Annotation[] | 返回直接存在于此元素上的所有注解。与此接口中的其他方法不同，该方法将忽略继承的注解。（如果没有注解直接存在于此元素上，则返回长度为零的一个数组。） |

下面我们通过一个简单的例子来了解下如何通过反射获取注解：

假设我们有一个这样的自定义注解：

```
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
protected @interface FruitName {
   String value();
}
```
 
且我们定义了一个 Apple：

```
public class Apple {
   @FruitName(value = "Apple")
   public String name;
}
```

并有下列方法用来查看注解信息：

```
public static void getFruitInfo(Class<?> clazz) {
   try {
       String fruitName = "水果名称：";

       Field field = clazz.getDeclaredField("name");
       java.lang.annotation.Annotation[] annotations = field.getAnnotations();
       System.out.println("annotations = " + Arrays.toString(annotations));
       if (field.isAnnotationPresent(FruitName.class)) {
           FruitName fruitNameAnno = field.getAnnotation(FruitName.class);
           fruitName = fruitName + fruitNameAnno.value();
           System.out.println(fruitName);
       }
   } catch (NoSuchFieldException e) {
       e.printStackTrace();
   }
}
```
得到打印结果为

```
annotations = [@Annotation$FruitName(value=Apple)]
水果名称：Apple
```

## 总结

本文分析了一些常见的反射 API 的使用。这些并不是全部的 API。网上也有很多其他的反射的讲解也都不错。本文出发点想要与众不同，但是写着写着就"同流合污"了。 本来想说明反射中的泛型擦除，也想加上动态代理，虽然这两个知识点和反射有着很大的联系，但是两个都可独立成文。所以仅当此篇是一份学习记录，方便以后查阅吧。最后放出我所查看过一些不错的反射文章。

> 一些不错的反射介绍文章：
> 
> [深入解析Java反射（1） - 基础](https://www.sczyh30.com/posts/Java/java-reflection-1/)
> [Java 反射由浅入深 | 进阶必备](https://juejin.im/post/598ea9116fb9a03c335a99a4#heading-7)
> [JAVA反射与注解](https://www.daidingkang.cc/2017/07/18/java-reflection-annotations/)
> [反射技术在android中的应用](https://blog.csdn.net/tiefeng0606/article/details/51700866)
> [细说反射，Java 和 Android 开发者必须跨越的坎](https://blog.csdn.net/briblue/article/details/74616922)
> [Thinking in Java]() 
> [Java 核心技术卷 I]()

