# 搞懂 Java equals 和 hashCode 方法

分析完 Java List 容器的源码后，本来想直接进入 Set 和 Map 容器的源码分析，但是对于这两种容器，内部存储元素的方式的都是以键值对相关的，而元素如何存放，便与 `equals` 和  `hashCode` 这两个方法密切相关。所以在分析 Map 家族之前，需要深入了解下这两个方法，而且这两个方法在面试的时候也属于极有可能考察的问题。

跟往常一样，本文也尽可能结合面试题来重点讲解下 equals 和 hashCode 的使用以及意义。


## 概述

首先 `equals` 和 `hashCode` 两个方法属于 Object 基类的方法：


```
public boolean equals(Object obj) {
   return (this == obj);
}

public native int hashCode();

```

可以看出 `equals` 方法默认比较的是两个对象的引用是否指向同一个内存地址。而 `hashCode` 这是一个 native 本地方法，其实默认的 `hashCode` 方法返回的就是对象对应的内存地址。

> hasCode 方法的注释这样说的： This is typically implemented by converting the internal address of the object into an integer,

这一点我们通过 `toString` 方法也可以间接了解，我们都知道 toString 返回的是「类名@十六进制内存地址」，由源码可以看出内存地址与 `hashCode()` 返回值相同。

```
public String toString() {
   return getClass().getName() + "@" + Integer.toHexString(hashCode());
}
```
> 面试题目： `hashCode` 方法返回的是对象的内存地址么？
> 答： Object 基类的 `hashCode` 方法默认返回对象的内存地址，但是在一些场景下我们需要覆写 `hashCode` 函数，比如需要使用 `Map` 来存放对象的时候，覆写后 `hashCode` 就不是对象的内存地址了。

## equals 详解

`equals` 方法既然是基类 `Object` 的方法，我们创建的所有的对象都拥有这个方法，并有权利去重写这个方法。该方法返回一个 boolean 值，代表比较的两个对象是否相同，这里的相同的条件由重写 `equals` 方法的类来解决。比如我们都知道 :

```
String str1 = "abc";
String str2 = "abc";
str1.equals(str2);//true
```
显然 `String` 类一定重写了 `equals` 方法否则两个 `String` 对象内存地址肯定不同。我们简单看下 `String` 类的 `equals` 方法：


```
 public boolean equals(Object anObject) {
   //首先判断两个对象的内存地址是否相同
   if (this == anObject) {
       return true;
   }
   // 判断连个对象是否属于同一类型。
   if (anObject instanceof String) {
       String anotherString = (String)anObject;
       int n = value.length;
       //长度相同的情况下逐一比较 char 数组中的每个元素是否相同
       if (n == anotherString.value.length) {
           char v1[] = value;
           char v2[] = anotherString.value;
           int i = 0;
           while (n-- != 0) {
               if (v1[i] != v2[i])
                   return false;
               i++;
           }
           return true;
       }
   }
   return false;
}
```

从源码我们也可以看出， `equals` 方法已经不单单是调用 `this==obj`来判断对象是否相同了。事实上所有 Java 定义好的一些现有的引用数据类型都重写了该方法。当我们自己定义引用数据类型的时候我们应该依照什么原则去判定两个对象是否相同，这就需要我们自己来根据业务需求来把握。但是我们都需要遵循以下规则：

- **自反性（reflexive）**。对于任意不为 null 的引用值 x，`x.equals(x)` 一定是 true。

- **对称性（symmetric）**。对于任意不为 null 的引用值 x 和 y ，当且仅当`x.equals(y)`是 true 时，`y.equals(x)`也是true。

- **传递性（transitive）**。对于任意不为 null 的引用值x、y和z，如果 `x.equals(y)` 是 true，同时 `y.equals(z)` 是 true，那么`x.equals(z)`一定是 true。

- **一致性（consistent）**。对于任意不为null的引用值x和y，如果用于equals比较的对象信息没有被修改的话，多次调用时 `x.equals(y)` 要么一致地返回 true 要么一致地返回 false。

- 对于任意不为 null 的引用值 x，`x.equals(null)` 返回 false。

### equals vs ==
说到 `equals` 怎么能不说  `==` ，其实两个在初学 Java 的时候给新手还是带来了蛮多困惑的。对于这两个的区别需要看比较的对象是什么样的类型。

我们都知道 Java 数据类型可分为 基本数据类型 和 引用数据类型。基本数据类型包括 `byte, short, int , long , float , double , boolen ，char ` 八种。对于基本数据类型 == 操作符判断的是左右两边变量的值：

```
int a = 10;
int b = 10;
float c = 10.0f;
//以下输出结果均为 true
System.out.println("(a == b) = " + (a == b));
System.out.println("(b == c) = " + (b == c));
```
而对于引用数据类型 == 操作符判断就是等号两边的指向的对象的内存地址是否相同。也就是说**通过 == 判断的两个引用数据类型变量，如果相同，则他们指向的肯定是同一个对象。**

```
EntryClass entryClass1 = new EntryClass(1);
EntryClass entryClass2 = new EntryClass(1);
EntryClass entryClass3 = entryClass1;
 
 // (entryClass1 == entryClass2) = false   
System.out.println(" (entryClass1 == entryClass2) = " + (entryClass1 == entryClass2));
// (entryClass1 == entryClass3) = true
System.out.println(" (entryClass1 == entryClass3) = " + (entryClass1 == entryClass3));
```

**equals 与 == 操作符的区别总结如下：**

1. **若 == 两侧都是基本数据类型，则判断的是左右两边操作数据的值是否相等**

2. **若 == 两侧都是引用数据类型，则判断的是左右两边操作数的内存地址是否相同。若此时返回 true , 则该操作符作用的一定是同一个对象。**

3. **Object 基类的 equals 默认比较两个对象的内存地址，在构建的对象没有重写 equals 方法的时候，与 == 操作符比较的结果相同。**

3. **equals 用于比较引用数据类型是否相等。在满足equals 判断规则的前体系，两个对象只要规定的属性相同我们就认为两个对象是相同的。**

## hashCode 方法

`hashCode` 方法并没有 `equals` 方法使用的那么频繁，说道 hashCode 方法就不得不结合 Java 的 Map 容器，类似于 `HashMap` 这种使用了哈希算法容器会根据对象的`hashCode`返回值来初步确定对象在容器中的位置,然后内部再根据一定的 hash 算法来实现元素的存取。


### hash 法简介
hash 算法，又被成为散列算法，基本上，哈希算法就是将对象本身的键值，通过特定的数学函数运算或者使用其他方法，转化成相应的数据存储地址的。而哈希法所使用的数学函数就被称为 『哈希函数』又可以称之为散列函数。

说了这么多定义的东西，那这个 hash 算法究竟是干什么用的呢 ？我们可以通过一个例子来说明：

如果我们要在存放了的元素` {0，4，6，9，28}` 的数组中找到数值等于 6 的值的索引我们会怎么做？我们是不是需要遍历一遍数组才能拿到对应的索引。在数组较大的时候这往往是低效率的。

如果我们能在数组存放的时候就按一定的规则放入元素，在我们想找某个元素的时候在根据之前定好的规则，就可以很快的得到我们想要的结果了。换句话说之前我们在数组中存放元素的顺序可能是依照添加顺序进行的，但是如果我们是按照一种既定的数学函数运算得到要放入元素的值，和数组角标的映射关系的话。那么我们在想取某个值的元素的时候就使用映射关系就可以找到对应的角标了。

在常见的 hash 函数中有一种最简单的方法交「除留余数法」，操作方法就是将要存入数据除以某个常数后，使用余数作为索引值。 下面看个例子：

将 323 ，458 ，25 ，340 ，28 ，969， 77 使用「除留余数法」存储在长度为11的数组中。我们假设上边说的某个常数即为数组长度11。 每个数除以11以后存放的位置如下图所示：

![](https://ws3.sinaimg.cn/large/006tNc79ly1fq0vjp6i75j31ac09w74c.jpg)

试想一下我们现在想要拿到 77 在数组中的位置，是不是只需要 `arr[77%11] = 77` 就可以了。

但是上述简单的 hash 算法，缺点也是很明显的，比如 77 和 88 对 11 取余数得到的值都是 0，但是角标为 0 位置已经存放了 77 这个数据，那88就不知道该去哪里了。上述现象在哈希法中有个名词叫碰撞：

> 碰撞：若两个不同的数据经过相同哈希函数运算后，得到相同的结果，那么这种现象就做碰撞。

于是在设计 hash 函数的时候我们就要尽可能做到：

1. 降低碰撞的可能性
2. 尽量将要存入的元素经过 hash 函数运算后的结果，尽量能够均匀的分布在指定的容器（我们在称之为桶）。


### hashCode 方法 与 hash 算法的关系

其实 Java 中的有所的对象又拥有 hashCode 方法其实就是一种 hash 算法，只是有的类覆写好提供给我们了，有些就需要我们手动去覆写。比如我们可以看一下 String 提供给我们的 hashCode 算法：

```
public int hashCode() {
   int h = hash;//默认是0
   if (h == 0 && value.length > 0) {
       char val[] = value;
        // 字符串转化的 char 数组中每一个元素都参与运算
       for (int i = 0; i < value.length; i++) {
           h = 31 * h + val[i];
       }
       hash = h;
   }
   return h;
}
```

前文说了 hashCode 方法与 java 中使用散列表的集合类息息相关，我们拿 Set 来举例，我们都知道 Set 中是不允许存放重复的元素的。那么我们凭借什么来判断已有的 Set 集合中是否有何要存入的元素重复的元素呢？有人可能会说我们可以通过 equals 来判断两个元素是否相同。那么问题又来，如果 Set 中已经有 10000个元素了，那么之后在存入一个元素岂不是要调用 10000 次 equals 方法。显然这不合理，性能低到令人发指。那要怎么办才能保证即高效又不重复呢？答案就在于 hashCode 这个函数。

经过之前的分析我们知道 hash 算法是使用特定的运算来得到数据的存储位置的，那么 hashCode 方法就充当了这个特定的函数运算。这里我们可以简单认为调用 hashCode 方法后得到数值就是元素的存储位置（其实集合内部还做了进一步的运算，以保证尽可能的均匀分布在桶内）。

当 Set 需要存放一个元素的时候，首先会调用 hashCode 方法去查看对应的地址上有没有存放元素，如果没有则表示 Set 中肯定没有相同的元素，直接存放在对应位置就好，但是如果 hashCode 的结果相同，即发生了碰撞，那么我们在进一步调用该位置元素的 equals 方法与要存放的元素进行比较，如果相同就不存了，如果不相同就需要进一步散列其它的地址。这样我们就可以尽可能高效的保证了无重复元素的方法。

> 面试题： hashCode 方法的作用和意义
> 答： 在 Java 中 hashCode 的存在主要是用于提高容器查找和存储的快捷性，如 HashSet， Hashtable，HashMap 等，hashCode是用来在散列存储结构中确定对象的存储地址的，


### hashCode 和 equals 方法的关系

翻看Object 类对于 equals 方法的注释上有这这么一条：

> 请注意，当这个方法被重写时，通常需要覆盖{@code hashCode}方法，以便维护{@code hashCode}方法的一般契约，该方法声明相等对象必须具有相等的哈希码.

可以看到如果我们出于某种原因复写了 equals 方法我们需要按照约定去覆写 hashCode 方法，并且使用 equals 比较相同的对象，必须拥有相等的哈希码。

Object 对于 hashCode 方法也有几条要求：

> 1. 在 Java 应用程序执行期间，在对同一对象多次调用 hashCode 方法时，必须一致地返回相同的整数，前提是将对象进行 equals 比较时所用的信息没有被修改。从某一应用程序的一次执行到同一应用程序的另一次执行，该整数无需保持一致。
> 2. 如果根据 equals(Object) 方法，两个对象是相等的，那么对这两个对象中的每个对象调用 hashCode 方法都必须生成相同的整数结果。

> 3. 如果根据 equals(java.lang.Object) 方法，两个对象不相等，那么对这两个对象中的任一对象上调用 hashCode 方法 不要求 一定生成不同的整数结果。但是，程序员应该意识到，为不相等的对象生成不同整数结果可以提高哈希表的性能。 
　 

结合 equals 方法的，我们可以做出如下总结：

1. **调用 equals 返回 true 的两个对象必须具有相等的哈希码。**

2. **如果两个对象的 hashCode 返回值相同，调用它们 equals 方法不一返回 true 。**

我们先来看下第一个结论：调用 equals 返回 true 的两个对象必须具有相等的哈希码。为什么这么要求呢？比如我们还拿 Set 集合举例，Set 首先会调用对象的 hashCode 方法寻找对象的存储位置，如果两个相同的对象调用 hashCode 方法得到的结果不同，那么造成的后果就是 Set 中存储了相同的元素，而这样的结果肯定是不对的。所以就要求 **调用 equals 返回 true 的两个对象必须具有相等的哈希码**。

那么第二条为什么 `hashCode` 返回值相同，两个对象却不一定相同呢？这是因为，目前没有完美的 hash 算法能够完全的避免 「哈希碰撞」，既然碰撞是无法完全避免的所以两个不相同的对象总有可能得到相同的哈希值。所以我们只能尽可能的保证不同的对象的 `hashCode` 不相同。事实上，对于 `HashMap` 在存储键值对的时候，就会发生这样的情况，在 JDK 1.7 之前，`HashMap` 对键的哈希值碰撞的处理方式，就是使用所谓的‘**拉链法**’。 具体实现会在之后分析 `HashMap` 的时候说到。


## 总结

本文总结了 equals 方法和 hashCode 方法的作用和意义。并学习了在覆写这两个方法的时候需要注意的要求。需要注意的是，关于这两个方法在面试的时候还是很有可能被问及的所以，我们至少要明白：

1. `hashCode` 返回值不一定对象的存储地址，比如发生哈希碰撞的时候。
2. 调用 `equals` 返回 true 的两个对象必须具有相等的哈希码。
3. 如果两个对象的 `hashCode` 返回值相同，调用它们 `equals` 方法不一返回 true 。






