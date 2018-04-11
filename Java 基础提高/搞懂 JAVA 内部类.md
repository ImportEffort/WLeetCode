# 搞懂 JAVA 内部类

前些天写了一篇关于 2018 年奋斗计划的文章，其实做 Android 开发也有一段时间了，文章中所写的内容，也都是在日常开发中遇到各种问题后总结下来需要巩固的基础或者进阶知识。那么本文就从内部类开刀。

本文将会从以下几部分来总结：

1. 为什么要存在内部类
2. 内部类与外部类的关系
2. 内部的分类及几种分类的详细使用注意事项
3. 实际开发中会遇到内部类的问题

## 内部类为什么存在 

> 内部类 ( inner class ) : 定义在另一个类中的类

我们为什么需要内部类？或者说内部类为啥要存在？其主要原因有如下几点：

- 内部类方法可以访问该类定义所在作用域中的数据，包括被 private 修饰的私有数据
- 内部类可以对同一包中的其他类隐藏起来
- 内部类可以实现 java 单继承的缺陷
- 当我们想要定义一个回调函数却不想写大量代码的时候我们可以选择使用匿名内部类来实现

### 内部类方法可以访问该类定义所在作用域中的数据 
做 Android 的我们有时候会将各种 Adapter 直接写在 Activity 中，如：

```
class MainActivity extends AppCompatActivity{
    ....
    private List<Fragment> fragments = new ArrayList();
    
    private class BottomPagerAdapter extends FragmentPagerAdapter{
        ....
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        ...
    }
    ...
}
```
上文中 BottomPagerAdapter 便是 MainActivity 的一个内部类。也可以看出 BottomPagerAdapter 可以直接访问 MainActivity 中定义的 fragments 私有变量。如果将 BottomPagerAdapter 不定义为内部类访问 fragments 私有变量 没有 getXXX 方法是做不到的。 这就是内部类的第一点好处。

可是为什么内部类就可以随意访问外部类的成员呢？是如何做到的呢？

> 当外部类的对象创建了一个内部类的对象时，内部类对象必定会秘密捕获一个指向外部类对象的引用，然后访问外部类的成员时，就是用那个引用来选择外围类的成员的。当然这些编辑器已经帮我们处理了。
> 
> 另外注意内部类只是一种编译器现象，与虚拟机无关。编译器会将内部类编译成 外部类名$内部类名 的常规文件，虚拟机对此一无所知。

### 内部类可以对同一包中的其他类隐藏起来
关于内部类的第二个好处其实很显而易见，我们都知道外部类即普通的类不能使用 private protected 访问权限符来修饰的，而内部类则可以使用 private 和 protected 来修饰。当我们使用 private 来修饰内部类的时候这个类就对外隐藏了。这看起来没什么作用，但是当内部类实现某个接口的时候，在进行向上转型，对外部来说，就完全隐藏了接口的实现了。 如：



```
public interface Incrementable{
  void increment();
}
//具体类
public class Example {

    private class InsideClass implements InterfaceTest{
         public void test(){
             System.out.println("这是一个测试");
         }
    }
    public InterfaceTest getIn(){
        return new InsideClass();
    }
}

public class TestExample {

 public static void main(String args[]){
    Example a=new Example();
    InterfaceTest a1=a.getIn();
    a1.test();
 }
}
```

从这段代码里面我只知道Example的getIn()方法能返回一个InterfaceTest实例但我并不知道这个实例是这么实现的。而且由于InsideClass是private的，所以我们如果不看代码的话根本看不到这个具体类的名字，所以说它可以很好的实现隐藏。

### 内部类可以实现 java 单继承的缺陷

我们知道 java 是不允许使用 extends 去继承多个类的。内部类的引入可以很好的解决这个事情。
以下引用 《Thinking In Java》中的一段话：

> 每个内部类都可以队里的继承自一个（接口的）实现，所以无论外围类是否已经继承了某个（接口的）实现，对于内部类没有影响
>如果没有内部类提供的、可以继承多个具体的或抽象的类的能力，一些设计与编程问题就难以解决。
>接口解决了部分问题，一个类可以实现多个接口，内部类允许继承多个非接口类型（类或抽象类）。

我的理解 Java只能继承一个类这个学过基本语法的人都知道，而在有内部类之前它的多重继承方式是用接口来实现的。但使用接口有时候有很多不方便的地方。比如我们实现一个接口就必须实现它里面的所有方法。而有了内部类就不一样了。它可以使我们的类继承多个具体类或抽象类。如下面这个例子：


```
//类一
public class ClassA {
   public String name(){
       return "liutao";
   }
   public String doSomeThing(){
    // doSomeThing
   }
}
//类二
public class ClassB {
    public int age(){
        return 25;
    }
}

//类三
public class MainExample{
   private class Test1 extends ClassA{
        public String name(){
          return super.name();
        }
    }
    private class Test2 extends ClassB{
       public int age(){
         return super.age();
       }
    }
   public String name(){
    return new Test1().name();
   }
   public int age(){
       return new Test2().age();
   }
   public static void main(String args[]){
       MainExample mi=new MainExample();
       System.out.println("姓名:"+mi.name());
       System.out.println("年龄:"+mi.age());
   }
}
```
上边这个例子可以看出来，MainExample 类通过内部类拥有了 ClassA 和 ClassB 的两个类的继承关系。 而无需关注 ClassA 中的 doSomeThing 方法的实现。这就是比接口实现更有戏的地方。

### 通过匿名内部类来"优化"简单的接口实现

关于匿名内部类相信大家都不陌生，我们常见的点击事件的写法就是这样的：

```
    ...
    view.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(){
            // ... do XXX...
        }
    })
    ...
```
为什么标题优化带了"",其实在 Java8 引入 lambda 表达式之前我个人是比较讨厌这种写法的，因为 onClick 方法中的内容可能很复杂，可能会有很多判断逻辑的存在，这就导致代码显得很累赘，所以个人更喜欢使用匿名内部类来完成一些简便的操作，配合lambda 表达式，代码会更便于阅读 如


```
    view.setOnClickListener(v -> gotoVipOpenWeb());
```

## 内部类与外部类的关系

- 对于非静态内部类，内部类的创建依赖外部类的实例对象，在没有外部类实例之前是无法创建内部类的
- 内部类是一个相对独立的实体，与外部类不是is-a关系
- 创建内部类的时刻并不依赖于外部类的创建

### 创建内部类的时刻并不依赖于外部类的创建

这句话是《Thinking In Java》中的一句话，大部分人看到这里会断章取义的认为 内部类的创建不依赖于外部类的创建，这种理解是错误的，去掉时刻二字这句话就会变了一个味道。

事实上静态内部类「嵌套类」的确不依赖与外部类的创建，因为 static 并不依赖于实例，而依赖与类 Class 本身。

但是对于普通的内部类，其必须依赖于外部类实例创建正如第一条关系所说：对于非静态内部类，内部类的创建依赖外部类的实例对象，在没有外部类实例之前是无法创建内部类的。

对于普通内部类创建方法有两种：


```
public class ClassOuter {
    
    public void fun(){
        System.out.println("外部类方法");
    }
    
    public class InnerClass{
        
    }
}

public class TestInnerClass {
    public static void main(String[] args) {
        //创建方式1
        ClassOuter.InnerClass innerClass = new ClassOuter().new InnerClass();
        //创建方式2
        ClassOuter outer = new ClassOuter();
        ClassOuter.InnerClass inner = outer.new InnerClass();
    }
}

```

值得注意的是：**正式由于这种依赖关系，所以普通内部类中不允许有 static 成员，包括嵌套类（内部类的静态内部类）** ，道理显然而知：static 本身是针对类本身来说的。又由于非static内部类总是由一个外部的对象生成，既然与对象相关，就没有静态的字段和方法。当然静态内部类不依赖于外部类，所以其内允许有 static 成员。

现在返回头来看标题，其实英文版中这句话是这样描述的：

> The point of creation of the inner-class objects not tied to the creation of the outer-class object.

个人认为这句话理解为:**创建一个外部类的时候不一定要创建这个内部类。**

拿文章开头的 Adapter 的例子来说，我们不能说创建了 Activity 就一定会创建 Adapter （假设 Adapter 创建依赖于某个条件的成立）。只有当满足条件的时候才会被创建。

### 内部类是一个相对独立的实体，与外部类不是is-a关系

首先理解什么是「is-a关系」： is-a关系是指继承关系。知道什么是is-a关系后相信，内部类个外部类不是is-a关系就很容易理解了。

而对于内部类是一个相对独立的实体，我们可以从两个方面来理解这句话：

1. 一个外部类可以拥有多个内部类对象，而他们之间没有任何关系，是独立的个体。
2. 从编译结果来看，内部类被表现为 「外部类$内部类.class 」，所以对于虚拟机来说他个一个单独的类来说没什么区别。但是我们知道他们是有关系的，因为内部类默认持有一个外部类的引用。

## 内部类的分类

内部类可以分为：静态内部类（嵌套类）和非静态内部类。非静态内部类又可以分为：成员内部类、方法内部类、匿名内部类。对于这几种类的书写相信大家早已熟练，所以本节主要说明的是这几种类之间的区别：

### 静态内部类和非静态内部类的区别

1. 静态内部类可以有静态成员，而非静态内部类则不能有静态成员。
2. 静态内部类可以访问外部类的静态变量，而不可访问外部类的非静态变量；
3. 非静态内部类的非静态成员可以访问外部类的非静态变量。
4. 静态内部类的创建不依赖于外部类，而非静态内部类必须依赖于外部类的创建而创建。

我们通过一个例子就可以很好的理解这几点区别：


```
public class ClassOuter {
    private int noStaticInt = 1;
    private static int STATIC_INT = 2;

    public void fun() {
        System.out.println("外部类方法");
    }

    public class InnerClass {
        //static int num = 1; 此时编辑器会报错 非静态内部类则不能有静态成员
        public void fun(){
            //非静态内部类的非静态成员可以访问外部类的非静态变量。
            System.out.println(STATIC_INT);
            System.out.println(noStaticInt);
        }
    }

    public static class StaticInnerClass {
        static int NUM = 1;//静态内部类可以有静态成员
        public void fun(){
            System.out.println(STATIC_INT);
            //System.out.println(noStaticInt); 此时编辑器会报 不可访问外部类的非静态变量错
        }
    }
}

public class TestInnerClass {
    public static void main(String[] args) {
        //非静态内部类 创建方式1
        ClassOuter.InnerClass innerClass = new ClassOuter().new InnerClass();
        //非静态内部类 创建方式2
        ClassOuter outer = new ClassOuter();
        ClassOuter.InnerClass inner = outer.new InnerClass();
        //静态内部类的创建方式
        ClassOuter.StaticInnerClass staticInnerClass = new ClassOuter.StaticInnerClass();
    }
}

```

### 局部内部类
> 如果一个内部类只在一个方法中使用到了，那么我们可以将这个类定义在方法内部，这种内部类被称为局部内部类。其作用域仅限于该方法。

局部内部类有两点值得我们注意的地方：
1. 局部内类不允许使用访问权限修饰符 public private protected 均不允许
2. 局部内部类对外完全隐藏，除了创建这个类的方法可以访问它其他的地方是不允许访问的。
3. 局部内部类与成员内部类不同之处是他可以引用成员变量，但该成员必须声明为 final，并内部不允许修改该变量的值。（这句话并不准确，因为如果不是基本数据类型的时候，只是不允许修改引用指向的对象，而对象本身是可以被就修改的）

```
public class ClassOuter {
    private int noStaticInt = 1;
    private static int STATIC_INT = 2;

    public void fun() {
        System.out.println("外部类方法");
    }
    
    public void testFunctionClass(){
        class FunctionClass{
            private void fun(){
                System.out.println("局部内部类的输出");
                System.out.println(STATIC_INT);
                System.out.println(noStaticInt);
                System.out.println(params);
                //params ++ ; // params 不可变所以这句话编译错误
            }
        }
        FunctionClass functionClass = new FunctionClass();
        functionClass.fun();
    }
}

```

### 匿名内部类

1. 匿名内部类是没有访问修饰符的。
2. 匿名内部类必须继承一个抽象类或者实现一个接口
3. 匿名内部类中不能存在任何静态成员或方法
4. 匿名内部类是没有构造方法的，因为它没有类名。
5. 与局部内部相同匿名内部类也可以引用局部变量。此变量也必须声明为 final 

```
public class Button {
    public void click(final int params){
        //匿名内部类，实现的是ActionListener接口
        new ActionListener(){
            public void onAction(){
                System.out.println("click action..." + params);
            }
        }.onAction();
    }
    //匿名内部类必须继承或实现一个已有的接口
    public interface ActionListener{
        public void onAction();
    }

    public static void main(String[] args) {
        Button button=new Button();
        button.click();
    }
}
```
### 为什么局部变量需要final修饰呢

原因是：因为局部变量和匿名内部类的生命周期不同。

匿名内部类是创建后是存储在堆中的，而方法中的局部变量是存储在Java栈中，当方法执行完毕后，就进行退栈，同时局部变量也会消失。那么此时匿名内部类还有可能在堆中存储着，那么匿名内部类要到哪里去找这个局部变量呢？

为了解决这个问题编译器为自动地帮我们在匿名内部类中创建了一个局部变量的备份，也就是说即使方法执结束，匿名内部类中还有一个备份，自然就不怕找不到了。

但是问题又来了。如果局部变量中的a不停的在变化。那么岂不是也要让备份的a变量无时无刻的变化。为了保持局部变量与匿名内部类中备份域保持一致。编译器不得不规定死这些局部域必须是常量，一旦赋值不能再发生变化了。所以为什么匿名内部类应用外部方法的域必须是常量域的原因所在了。

特别注意：**在Java8中已经去掉要对final的修饰限制，但其实只要在匿名内部类使用了，该变量还是会自动变为final类型（只能使用，不能赋值）。**
## 实际开发中内部类有可能会引起的问题

### 内部类会造成程序的内存泄漏

相信做 Android 的朋友看到这个例子一定不会陌生，我们经常使用的 Handler 就无时无刻不给我们提示着这样的警告。我们先来看下内部类为什么会造成内存泄漏。

要想了解为啥内部类为什么会造成内存泄漏我们就必须了解 java 虚拟机的回收机制，但是我们这里不会详尽的介绍 java 的内存回收机制，我们只需要了解 java 的内存回收机制通过「可达性分析」来实现的。即 java 虚拟机会通过内存回收机制来判定引用是否可达，如果不可达就会在某些时刻去回收这些引用。

那么内部类在什么情况下会造成内存泄漏的可能呢？
> 1. 如果一个匿名内部类没有被任何引用持有，那么匿名内部类对象用完就有机会被回收。
> 
> 2. 如果内部类仅仅只是在外部类中被引用，当外部类的不再被引用时，外部类和内部类就可以都被GC回收。
> 
> 3. 如果当内部类的引用被外部类以外的其他类引用时，就会造成内部类和外部类无法被GC回收的情况，即使外部类没有被引用，因为内部类持有指向外部类的引用）。


```
public class ClassOuter {

    Object object = new Object() {
        public void finalize() {
            System.out.println("inner Free the occupied memory...");
        }
    };

    public void finalize() {
        System.out.println("Outer Free the occupied memory...");
    }
}

public class TestInnerClass {
    public static void main(String[] args) {
        try {
            Test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void Test() throws InterruptedException {
        System.out.println("Start of program.");

        ClassOuter outer = new ClassOuter();
        Object object = outer.object;
        outer = null;

        System.out.println("Execute GC");
        System.gc();

        Thread.sleep(3000);
        System.out.println("End of program.");
    }
}

```
运行程序发现 执行内存回收并没回收 object 对象，这是因为即使外部类没有被任何变量引用，只要其内部类被外部类以外的变量持有，外部类就不会被GC回收。我们要尤其注意内部类被外面其他类引用的情况，这点导致外部类无法被释放，极容易导致内存泄漏。

在Android 中 Hanlder 作为内部类使用的时候其对象被系统主线程的 Looper 持有（当然这里也可是子线程手动创建的 Looper）掌管的消息队列 MessageQueue 中的 Hanlder 发送的 Message 持有，当消息队列中有大量消息处理的需要处理，或者延迟消息需要执行的时候，创建该 Handler 的 Activity 已经退出了，Activity 对象也无法被释放，这就造成了内存泄漏。

那么 Hanlder 何时会被释放，当消息队列处理完 Hanlder 携带的 message 的时候就会调用 msg.recycleUnchecked()释放Message所持有的Handler引用。

在 Android 中要想处理 Hanlder 内存泄漏可以从两个方面着手：

-  在关闭Activity/Fragment 的 onDestry，取消还在排队的Message:


```
mHandler.removeCallbacksAndMessages(null);
```

- 将 Hanlder 创建为静态内部类并采用软引用方式


```
   private static class MyHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity == null || activity.isFinishing()) {
               return;
            }
            // ...
        }
    }

```

## 总结

本文从内部类的存在理由，内部类与外部类的关系，内部类分类以及开发中内部类可能造成的内存泄漏的问题上，总结了与内部类相关的问题，原谅本人才疏学浅，本文之前想要使用「彻底搞懂 java 内部类」但是当我写完整片文章，我才发现，通过 java 内部类可能会延伸出各种各样的知识，所以最终去掉了彻底二字，总结可能有很多不到位的地方。还望能够及时帮我指出。

其中内部类分类，静态内部类和非静态内部类，以及局部内部类和匿名内部的共同点和区别点很可能被面试问到，如果能因此延伸到内部类造成的内存泄漏问题上，想必也是个加分项。

> 本文参考 《Thinking in java》，《Java 核心技术 卷1》
> http://blog.csdn.net/mcryeasy/article/details/54848452
> http://blog.csdn.net/mcryeasy/article/details/53149594
> https://www.zhihu.com/question/21373020
> http://daiguahub.com/2016/09/08/java%E5%86%85%E9%83%A8%E7%B1%BB%E7%9A%84%E6%84%8F%E4%B9%89%E5%92%8C%E4%BD%9C%E7%94%A8/
> https://www.zhihu.com/question/20969764

