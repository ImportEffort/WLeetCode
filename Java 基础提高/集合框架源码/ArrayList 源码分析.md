# ArrayList 源码分析

不知道各位朋友，还记得开工前制定的学习目标么？ 有没有一直为了那个目标废寝忘食呢？继 [搞懂 Java 内部类](https://juejin.im/post/5a903ef96fb9a063435ef0c8) 后开始探索总结 Java 集合框架源码的知识，希望能给自己夯实基础，也希望能为自己实现目标更近一步。


## ArrayList 源码分析思路

ArrayList 是我们 App 开发中常用的 Java 集合类，从学习 Java 开始我们基本上就对它天天相见了，但是通过探索ArrayList 源码，我们将会把它从普通朋友变成知根知底的老朋友,本文将从以下几部分开始分析 `ArrayList`：

1. **ArrayList 概述**
1. **ArrayList 的构造函数，也就是我们创建一个 ArrayList 的方法**。
2. **ArrayList 的添加元素的方法， 以及 ArrayList 的扩容机制**
3. **ArrayList 的删除元素的常用方法**
4. **ArrayList 的 改查常用方法**
4. **ArrayList 的 toArray 方法**
5. **ArrayList 的遍历方法，以及常见的错误操作即产生错误操作的原因**

## ArrayList 概述

### ArrayList的基本特点

1. **ArrayList 底层是一个动态扩容的数组结构**
2. **允许存放（不止一个） null 元素**
3. **允许存放重复数据，存储顺序按照元素的添加顺序**
4. **ArrayList 并不是一个线程安全的集合。如果集合的增删操作需要保证线程的安全性，可以考虑使用 `CopyOnWriteArrayList` 或者使用 `collections.synchronizedList(List l)`函数返回一个线程安全的ArrayList类.**


### ArrayList 的继承关系

```
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

从 `ArrayList` 的继承关系来看， `ArrayList` 继承自 `AbstractList`，实现了`List<E>, RandomAccess, Cloneable, java.io.Serializable` 接口。

- 其中 `AbstractList`和 `List<E>` 是规定了 `ArrayList` 作为一个集合框架必须具备的一些属性和方法，`ArrayList` 本身覆写了基类和接口的大部分方法，这就包含我们要分析的增删改查操作。

-  `ArrayList` 实现 `RandomAccess` 接口标识着其支持随机快速访问，查看源码可以知道`RandomAccess` 其实只是一个标识，标识某个类拥有随机快速访问的能力，针对 ArrayList 而言通过 `get(index)`去访问元素可以达到 O(1) 的时间复杂度。有些集合类不拥有这种随机快速访问的能力，比如 `LinkedList` 就没有实现这个接口。
 
- `ArrayList` 实现 `Cloneable` 接口标识着他可以被克隆/复制，其内部实现了 clone 方法供使用者调用来对 ArrayList 进行克隆，但其实现只通过 `Arrays.copyOf` 完成了对 ArrayList 进行「浅复制」，也就是你改变 `ArrayList clone `后的集合中的元素，源集合中的元素也会改变，对于深浅复制我以后会单独整理一篇文章来讲述这里不再过多的说。

- 对于 `java.io.Serializable` 标识着集合可被被序列化。

我们发现了一些有趣的事情，除了` List<E> ` 以外，`ArrayList` 实现的接口都是标识接口，标识着这个类具有怎样的特点，看起来更像是一个属性。

## ArrayList 的构造方法

在说构造方法之前我们要先看下与构造参数有关的几个全局变量：


```
/**
 * ArrayList 默认的数组容量
 */
 private static final int DEFAULT_CAPACITY = 10;

/**
 * 这是一个共享的空的数组实例，当使用 ArrayList(0) 或者 ArrayList(Collection<? extends E> c) 
 * 并且 c.size() = 0 的时候讲 elementData 数组讲指向这个实例对象。
 */
 private static final Object[] EMPTY_ELEMENTDATA = {};

/**
 * 另一个共享空数组实例，再第一次 add 元素的时候将使用它来判断数组大小是否设置为 DEFAULT_CAPACITY
 */
 private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

/**
 * 真正装载集合元素的底层数组 
 * 至于 transient 关键字这里简单说一句，被它修饰的成员变量无法被 Serializable 序列化 
 * 有兴趣的可以去网上查相关资料
 */
transient Object[] elementData; // non-private to simplify nested class access
```
对于上述几个成员变量，我们只是在注释中简单的说明，对于他们具体有什么作用，在下边分析构造方法和扩容机制的时候将会更详细的讲解。

ArrayList 一共三种构造方式，我们先从无参的构造方法来开始：

### 无参构造方法

```
/**
 * 构造一个初始容量为10的空列表。
 */
public ArrayList() {
   this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```
这是我们经常使用的一个构造方法，其内部实现只是将 `elementData` 指向了我们刚才讲得 `DEFAULTCAPACITY_EMPTY_ELEMENTDATA` 这个空数组，这个空数组的容量是 0， 但是源码注释却说这是构造一个初始容量为10的空列表。这是为什么？其实在集合调用 add 方法添加元素的时候将会调用 `ensureCapacityInternal` 方法，在这个方法内部判断了：

```
if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
       minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
}
```
可见，如果采用无参数构造方法的时候第一次添加元素肯定走进 if 判断中 minCapacity 将被赋值为 10，所以「构造一个初始容量为10的空列表。」也就是这个意思。


### 指定初始容量的构造方法


```
/**
 * 构造一个具有指定初始容量的空列表。
 * @param  初始容量 
 * @throws 如果参数小于 0 将会抛出 IllegalArgumentException  参数不合法异常
 */
public ArrayList(int initialCapacity) {
   if (initialCapacity > 0) {
       this.elementData = new Object[initialCapacity];
   } else if (initialCapacity == 0) {
       this.elementData = EMPTY_ELEMENTDATA;
   } else {
       throw new IllegalArgumentException("Illegal Capacity: "+
                                          initialCapacity);
   }
}
```

如果我们预先知道一个集合元素的容纳的个数的时候推荐使用这个构造方法，比如我们有个一 `FragmentPagerAdapter` 一共需要装 15 个 `Fragment` ，那么我们就可以在构造集合的时候生成一个初始容量为 15 的一个集合。有人会认为 `ArrayList` 自身具有动态扩容的机制，无需这么麻烦，下面我们讲解扩容机制的时候我们就会发现，每次扩容是需要有一定的内存开销的，而这个开销在预先知道容量的时候是可以避免的。 

源代码中指定初始容量的构造方法实现，判断了如果 我们指定容量大于 0 ，将会直接 new 一个数组，赋值给 `elementData` 引用作为集合真正的存储数组，而指定容量等于 0 的时候讲使用成员变量 `EMPTY_ELEMENTDATA`  作为暂时的存储数组，这是 `EMPTY_ELEMENTDATA` 这个空数组的一个用处（不必太过于纠结 EMPTY_ELEMENTDATA 的作用，其实它的在源码中出现的频率并不高）。


### 使用另个一个集合 Collection 的构造方法


```

/**
 * 构造一个包含指定集合元素的列表，元素的顺序由集合的迭代器返回。
 *
 * @param 源集合，其元素将被放置到这个集合中。 
 * @如果参数为 null，将会抛出 NullPointerException 空指针异常
 */
 public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        // c.toArray 可能(错误地)不返回 Object[]类型的数组 参见 jdk 的 bug 列表(6260652)
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        // 如果集合大小为空将赋值为 EMPTY_ELEMENTDATA 等同于 new ArrayList(0);
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```

看完这个代码我最疑惑的地方是 `Collection.toArray()` 和 `Arrays.copyOf（）` 这两个方法的使用，看来想明白这个构造参数具体做了什么必须理解这两个方法了。

###  Object[]  Collection.toArray() 方法

我们都知道 Collection 是集合框架的超类，其实 `Collection.toArray` 是交给具体的集合子类去实现的，这就说明不同的集合可能有不同的实现。他用来将一个集合转化为一个 Object[] 数组，事实上的真的是这样的么？参见 jdk 的 bug 列表(6260652)又是什么意思呢 ？我们来看下下边的这个例子：

```
List<String> subClasses = Arrays.asList("abc","def");

// class java.util.Arrays$ArrayList  
System.out.println(list.getClass());  
    
Object[] objects = subClasses.toArray();

// class java.lang.String;  
Object[] objArray = list.toArray();  
//这里返回的是 String[]
System.out.println(objects.getClass().getSimpleName()); 

objArray[0] = new Object(); // cause ArrayStoreException  
```

咦？为啥这里并不是一个 Object 数组呢？其实我们注意到，`list.getClass` 得到的并不是我们使用的 `ArrayList` 而是 `Arrays` 的内部类 `Arrays$ArrayList`。


```
 ArrayList(E[] array) {
       //这里只是检查了数组是否为空，不为空直接将原数组赋值给这个 ArrayList 的存储数组。
       a = Objects.requireNonNull(array);
}

@Override
public Object[] toArray(){
  return a.clone();
}

```
而我们调用的 toArray 方法就是这个内部对于 Collection.toArray 的实现，`a.clone()` ,这里 clone 并不会改变一个数组的类型，所以当原始数组中放的 String 类型的时候就会出现上边的这种情况了。

其实我们可以认为这是 jdk 的一个 bug，早在 05年的时候被人提出来了，但是一直没修复，但是在新的 「jdk 1.9」 种这个 bug 被修复了。

有兴趣的可以追踪 [bug 6260652](http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6260652) 看下。


### Arrays.copyOf 方法 

这个方法是在集合源码中常见的一个方法，他有很多重载方式,我们来看下最根本的方法：

```
public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
   @SuppressWarnings("unchecked")
    //根据class的类型是否是 Object[] 来决定是 new 还是反射去构造一个泛型数组
   T[] copy = ((Object)newType == (Object)Object[].class)
       ? (T[]) new Object[newLength]
       : (T[]) Array.newInstance(newType.getComponentType(), newLength);
       //使用 native 方法批量赋值元素至新数组中。
   System.arraycopy(original, 0, copy, 0,
                    Math.min(original.length, newLength));
   return copy;
}
```
上边的注释也看出来了，`Arrays.copyOf` 方法复制数组的时候先判断了指定的数组类型是否为 Object[] 类型，否则使用反射去构造一个指定类型的数组。最后使用 `System.arraycopy`这个 native 方法，去实现最终的数组赋值，`newLength` 如果比 `original.length` 大的时候会将多余的空间赋值为 `null` 由下边的例子可见:

```
String[] arrString = {"abc","def"};

Object[] copyOf = Arrays.copyOf(arrString, 5, Object[].class);
//[abc, def, null, null, null]
System.out.println(Arrays.toString(copyOf));
```
当然  `ArrayList(Collection<? extends E> c)` 复制的时候传递的是 `c.size()` 所以不会出现 `null`。

> ex: 对于 `System.arraycopy` 该方法，本文不再展开讨论，有一篇对于其分析很好的文章大家可以去参考[System：System.arraycopy方法详解
](https://segmentfault.com/a/1190000009922279)

ok，绕了这么大的圈子终于明白了，`ArrayList(Collection<? extends E> c)`干了啥了，其实就是将一个集合中的元素塞到 ArrayList 底层的数组中。至此我们也将 ArrayList 的构造研究完了。


## ArrayList的添加元素 & 扩容机制

敲黑板了！这块是面试的常客了，所以必须仔细研究下了。我们先看下如何给一个 `ArrayList` 添加一个元素:

### 在集合末尾添加一个元素的方法

```
//成员变量 size 标识集合当前元素个数初始为 0
int size；
/**
 * 将指定元素添加到集合（底层数组）末尾
 * @param 将要添加的元素
 * @return 返回 true 表示添加成功
 */
 public boolean add(E e) {
    //检查当前底层数组容量，如果容量不够则进行扩容
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    //将数组添加一个元素，size 加 1
    elementData[size++] = e;
    return true;
 }
```

调用 add 方法的时候总会调用 `ensureCapacityInternal` 来判断是否需要进行数组扩容，`ensureCapacityInternal` 参数为当前集合长度 `size + 1`，这很好理解，是否需要扩充长度，需要看当前底层数组是否够放 `size + 1` 个元素的。

### 扩容机制

```
//扩容检查
private void ensureCapacityInternal(int minCapacity) {
    //如果是无参构造方法构造的的集合，第一次添加元素的时候会满足这个条件 minCapacity 将会被赋值为 10
   if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
       minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
   }
    // 将 size + 1 或 10 传入 ensureExplicitCapacity 进行扩容判断
   ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
  //操作数加 1 用于保证并发访问 
   modCount++;
   // 如果 当前数组的长度比添加元素后的长度要小则进行扩容 
   if (minCapacity - elementData.length > 0)
       grow(minCapacity);
}
```
上边的源码主要做了扩容前的判断操作，注意参数为当前集合元素个数+1，第一次添加元素的时候 `size + 1 = 1` ,而 `elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA`, 长度为 0 ，` 1 - 0 > 0`,  所以需要进行 grow 操作也就是扩容。

```
/**
 * 集合的最大长度 Integer.MAX_VALUE - 8 是为了减少出错的几率 Integer 最大值已经很大了
 */
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

/**
 * 增加容量，以确保它至少能容纳最小容量参数指定的元素个数。
 * @param 满足条件的最小容量
 */
private void grow(int minCapacity) {
  //获取当前 elementData 的大小，也就是 List 中当前的容量
   int oldCapacity = elementData.length;
   //oldCapacity >> 1 等价于 oldCapacity / 2  所以新容量为当前容量的 1.5 倍
   int newCapacity = oldCapacity + (oldCapacity >> 1);
   //如果扩大1.5倍后仍旧比 minCapacity 小那么直接等于 minCapacity
   if (newCapacity - minCapacity < 0)
       newCapacity = minCapacity;
    //如果新数组大小比  MAX_ARRAY_SIZE 就需要进一步比较 minCapacity 和 MAX_ARRAY_SIZE 的大小
   if (newCapacity - MAX_ARRAY_SIZE > 0)
       newCapacity = hugeCapacity(minCapacity);
   // minCapacity通常接近 size 大小
   //使用 Arrays.copyOf 构建一个长度为 newCapacity 新数组 并将 elementData 指向新数组
   elementData = Arrays.copyOf(elementData, newCapacity);
}

/**
 * 比较 minCapacity 与 Integer.MAX_VALUE - 8 的大小如果大则放弃-8的设定，设置为 Integer.MAX_VALUE 
 */
private static int hugeCapacity(int minCapacity) {
   if (minCapacity < 0) // overflow
       throw new OutOfMemoryError();
   return (minCapacity > MAX_ARRAY_SIZE) ?
       Integer.MAX_VALUE :
       MAX_ARRAY_SIZE;
}
```
由此看来 ArrayList 的扩容机制的知识点一共又两个

1. **每次扩容的大小为原来大小的 1.5倍 （当然这里没有包含 1.5倍后大于 MAX_ARRAY_SIZE 的情况）**
2. **扩容的过程其实是一个将原来元素拷贝到一个扩容后数组大小的长度新数组中。所以 ArrayList 的扩容其实是相对来说比较消耗性能的。**

### 在指定角标位置添加元素的方法


```
/**
* 将指定的元素插入该列表中的指定位置。将当前位置的元素(如果有)和任何后续元素移到右边(将一个元素添加到它们的索引中)。
* 
* @param 要插入的索引位置
* @param 要添加的元素
* @throws 如果 index 大于集合长度 小于 0 则抛出角标越界 IndexOutOfBoundsException 异常
*/
public void add(int index, E element) {
   // 检查角标是否越界
   rangeCheckForAdd(index);
    // 扩容检查
   ensureCapacityInternal(size + 1);      
   //调用 native 方法新型数组拷贝
   System.arraycopy(elementData, index, elementData, 
                    index + 1,size - index);
    // 添加新元素
   elementData[index] = element;
   size++;
}
```
我们知道一个数组是不能在角标位置直接插入元素的，ArrayList 通过数组拷贝的方法将指定角标位置以及其后续元素整体向后移动一个位置，空出 index 角标的位置，来赋值新的元素。

将一个数组 `src` 起始 `srcPos` 角标之后 length 长度间的元素，赋值到 `dest`  数组中 `destPos` 到 `destPos + length -1 `长度角标位置上。只是在 add 方法中 `src` 和 `destPos` 为同一个数组而已。

```
public static native void arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length);
```

### 批量添加元素 

由于批量添加和添加一个元素逻辑大概相同则这里不详细说了，代码注释可以了解整个添加流程。

在数组末尾添加

```
public boolean addAll(Collection<? extends E> c) {
        // 调用 c.toArray 将集合转化数组
        Object[] a = c.toArray();
        // 要添加的元素的个数
        int numNew = a.length;
        //扩容检查以及扩容
        ensureCapacityInternal(size + numNew);  // Increments modCount
        //将参数集合中的元素添加到原来数组 [size，size + numNew -1] 的角标位置上。
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        //与单一添加的 add 方法不同的是批量添加有返回值，如果 numNew == 0 表示没有要添加的元素则需要返回 false 
        return numNew != 0;
}
```

### 在数组指定角标位置添加

```
public boolean addAll(int index, Collection<? extends E> c) {
        //同样检查要插入的位置是否会导致角标越界
        rangeCheckForAdd(index);
        
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew); 
        //这里做了判断，如果要numMoved > 0 代表插入的位置在集合中间位置，和在 numMoved == 0最后位置 则表示要在数组末尾添加 如果 < 0  rangeCheckForAdd 就跑出了角标越界
        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }
    
private void rangeCheckForAdd(int index) {
   if (index > size || index < 0)
       throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

```
两个方法不同的地方在于如果移动角标即之后的元素，`addAll(int index, Collection<? extends E> c) `里做了判断，如果要 `numMoved > 0` 代表插入的位置在集合中间位置，和在 `numMoved == 0` 最后位置 则表示要在数组末尾添加 如果 `numMoved < 0 ` ，`rangeCheckForAdd` 就抛出了角标越界异常了。

与单一添加的 add 方法不同的是批量添加有返回值，如果 numNew == 0 表示没有要添加的元素则需要返回 false 

## ArrayList 删除元素

### 根据角标移除元素


```
/**
* 将任何后续元素移到左边(从它们的索引中减去一个)。
*/
public E remove(int index) {
   //检查 index 是否 >= size
   rangeCheck(index);

   modCount++;
   //index 位置的元素 
   E oldValue = elementData(index);
    // 需要移动的元素个数
   int numMoved = size - index - 1;
   if (numMoved > 0)
        //采用拷贝赋值的方法将 index 之后所有的元素 向前移动一个位置
       System.arraycopy(elementData, index+1, elementData, index,
                        numMoved);
   // 将 element 末尾的元素位置设为 null                 
   elementData[--size] = null; // clear to let GC do its work
    // 返回 index 位置的元素 
   return oldValue;
}

// 比较要移除的角标位置和当前 elementData 中元素的个数
private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```
根绝角标移除元素的方法源码如上所示，值得注意的地方是：

`rangeCheck` 和 `rangeCheckForAdd` 方法不同 ，`rangeCheck` 只检查了 index是否**大于等于** `size`，因为我们知道 `size ` 为`elementData` 已存储数据的个数，我们只能移除 `elementData` 数组中 `[0 , size -1]` 的元素，否则应该抛出角标越界。

但是为什么没有 和 `rangeCheckForAdd`  一样检查小于0的角标呢，是不是`remove（-1）` 不会抛异常呢？ 其实不是的，因为 rangeCheck(index); 后我们去调用 `elementData(index)` 的时候也会抛出 `IndexOutOfBoundsException` 的异常，这是数组本身抛出的，不是 ArrayList 抛出的。那为什么要检查` >= size` 呢？ 数组本身不也会检查么？ 哈哈.. 细心的同学肯定知道 `elementData.length` 并不一定等于 `size`，比如：


```
   ArrayList<String> testRemove = new ArrayList<>(10);

   testRemove.add("1");
   testRemove.add("2");
    // java.lang.IndexOutOfBoundsException: Index: 2, Size: 2
   String remove = testRemove.remove(2);
    
   System.out.println("remove = " + remove + "");
```

new ArrayList<>(10) 表示 `elementData` 初始容量为10，所以`elementData.length = 10` 而我们只给集合添加了两个元素所以 `size = 2` 这也就是为啥要 `rangeCheck` 的原因了。



### 移除指定元素


```
/**
* 删除指定元素，如果它存在则反会 true，如果不存在返回 false。
* 更准确地说是删除集合中第一出现 o 元素位置的元素 ，
* 也就是说只会删除一个，并且如果有重复的话，只会删除第一个次出现的位置。
*/
public boolean remove(Object o) {
    // 如果元素为空则只需判断 == 也就是内存地址
   if (o == null) {
       for (int index = 0; index < size; index++)
           if (elementData[index] == null) {
                //得到第一个等于 null 的元素角标并移除该元素 返回 ture
               fastRemove(index);
               return true;
           }
   } else {
        // 如果元素不为空则需要用 equals 判断。
       for (int index = 0; index < size; index++)
           if (o.equals(elementData[index])) {
                //得到第一个等于 o 的元素角标并移除该元素 返回 ture
               fastRemove(index);
               return true;
           }
   }
   return false;
}

//移除元素的逻辑和 remve(Index)一样 
private void fastRemove(int index) {
   modCount++;
   int numMoved = size - index - 1;
   if (numMoved > 0)
       System.arraycopy(elementData, index+1, elementData, index,
                        numMoved);
   elementData[--size] = null; // clear to let GC do its work
}

```

由上边代码可以看出来，移除元素和移除指定角标元素一样最终都是 通过 `System.arraycopy` 将 index 之后的元素前移一位，并释放原来位于 size 位置的元素。

还可以看出，如果数组中有指定多个与 o 相同的元素只会移除角标最小的那个，并且 null 和 非null 的时候判断方法不一样。至于 equals 和  == 的区别，还有 hashCode 方法，我会之后在总结一篇单独的文章。等不急的可以先去网上找找喽。

### 批量移除/保留 removeAll/retainAll

ArrayList 提供了 `removeAll/retainAll` 操作，这两个操作分别是 批量删除与参数集合中共同享有的元素 和 批量删除与参数集合中不共同享有的元素，保留共同享有的元素，由于两个方法只有一个参数不同

```
/** 批量删除与参数集合中共同享有的元素*/
 public boolean removeAll(Collection<?> c) {
        //判空 如果为空则抛出 NullPointerException 异常 Objects 的方法
        Objects.requireNonNull(c);
        return batchRemove(c, false);
 }
 
 /** 只保留与 c 中元素相同的元素相同的元素*/
public boolean retainAll(Collection<?> c) {
   Objects.requireNonNull(c);
   return batchRemove(c, true);
}
 
 /** 批量删除的指定方法 */
private boolean batchRemove(Collection<?> c, boolean complement) {
   
   final Object[] elementData = this.elementData;
    // r w 两个角标 r 为 elementData 中元素的索引 
    // w 为删除元素后集合的长度 
   int r = 0, w = 0;
   boolean modified = false;
   try {
       for (; r < size; r++)
            // 如果 c 当前集合中不包含当前元素，那么则保留
           if (c.contains(elementData[r]) == complement)
               elementData[w++] = elementData[r];
   } finally {
       // 如果c.contains（o）可能会抛出异常，如果抛出异常后 r!=size 则将 r 之后的元素不在比较直接放入数组
       if (r != size) {
           System.arraycopy(elementData, r,
                            elementData, w,
                            size - r);
          // w 加上剩余元素的长度
           w += size - r;
       }
        // 如果集合移除过元素，则需要将 w 之后的元素设置为 null 释放内存
       if (w != size) {
           // clear to let GC do its work
           for (int i = w; i < size; i++)
               elementData[i] = null;
           modCount += size - w;
           size = w;
           modified = true;
       }
   }
   //返回是否成功移除过元素，哪怕一个
   return modified;
}
```

可以看到移除指定集合中包含的元素的方法代码量是目前分析代码中最长的了，但是逻辑也很清晰：

1. 从 0 开始遍历 `elementData` 如果 r 位置的元素不存在于指定集合 c 中，那么我们就将他复制给数组 w 位置， 整个遍历过程中 `w <= r `。
2. 由于 `c.contains（o）`可能会抛出异常 `ClassCastException/NullPointerException `，如果因为异常而终止（这两个异常是可选操作，集合源码中并没有显示生命该方法一定会抛异常），那么我们将会产生一次错误操作，所以 finally 中执行了判断操作，如果 `r!= size` 那么肯定是发生了异常，那么则将 r 之后的元素不在比较直接放入数组。最终得到的结果并不一定正确是删除了所有与 c 中的元素。
3. 批量删除和保存中，涉及高效的保存/删除两个集合公有元素的算法，是值得我们学习的地方。


## ArraList 的改查

对于一个ArrayList 的改查方法就很简单了，set 和 get 方法。下面我们看下源码吧：


### 修改指定角标位置的元素

```
public E set(int index, E element) {
    //角标越界检查
   rangeCheck(index);
 //下标取数据注意这里不是elementData[index] 而是 elementData(index) 方法
   E oldValue = elementData(index);
   //将 index 位置设置为新的元素
   elementData[index] = element;
   // 返回之前在 index 位置的元素
   return oldValue;
}

E elementData(int index) {
    return (E) elementData[index];
}

```

### 查询指定角标的元素


```
public E get(int index) {
    //越界检查
    rangeCheck(index);
    //下标取数据注意这里不是elementData[index] 而是 elementData(index) 方法
    return elementData(index); 
}

```

### 查询指定元素的角标或者集合是否包含某个元素


```
//集合中是否包含元素 indexOf 返回 -1 表示不包含 return false 否则返回 true
public boolean contains(Object o) {
   return indexOf(o) >= 0;
}

/**
* 返回集合中第一个与 o 元素相等的元素角标，返回 -1 表示集合中不存在这个元素
* 这里还做了空元素直接判断 == 的操作
*/
public int indexOf(Object o) {
   if (o == null) {
       for (int i = 0; i < size; i++)
           if (elementData[i]==null)
               return i;
   } else {
       for (int i = 0; i < size; i++)
           if (o.equals(elementData[i]))
               return i;
   }
   return -1;
}
    
 /** 
  * 从 elementData 末尾开始遍历遍历数组，所以返回的是集合中最后一个与 o 相等的元素的角标
  */
public int lastIndexOf(Object o) {
   if (o == null) {
       for (int i = size-1; i >= 0; i--)
           if (elementData[i]==null)
               return i;
   } else {
       for (int i = size-1; i >= 0; i--)
           if (o.equals(elementData[i]))
               return i;
   }
   return -1;
}

```

## ArrayList 集合的 toArry 方法

其实 `Object[] toArray();` 方法，以及其重载函数 ` <T> T[] toArray(T[] a);` 是接口 `Collection` 的方法，ArrayList 实现了这两个方法，很少见ArrayList 源码分析的文章分析这两个方法，顾名思义这两个方法的是用来，将一个集合转为数组的方法，那么两者的不同之处是，后者可以指定数组的类型，前者返回为一个 Object[] 超类数组。那么我们具体下源码实现：


```
public Object[] toArray() {
   return Arrays.copyOf(elementData, size);
}

@SuppressWarnings("unchecked")
public <T> T[] toArray(T[] a) {
   if (a.length < size)
       // Make a new array of a's runtime type, but my contents:
       return (T[]) Arrays.copyOf(elementData, size, a.getClass());
   System.arraycopy(elementData, 0, a, 0, size);
   if (a.length > size)
       a[size] = null;
   return a;
}
```

可以看到 `Object[] toArray() ` 只是调用了一次 `Arrays.copyOf` 将集合中元素拷贝到一个新的 `Object[]` 数组并返回。这个 `Arrays.copyOf` 方法前边已经讲了。所以 toArray 方法并没有什么疑问，有疑问的地方在于` toArray(T[] a) ` 。

我们可以传入一个指定类型的标志数组作为参数，` toArray(T[] a)` 方法最终会返回这个类型的包含集合元素的新数组。但是源码判断了 ：

1. 如果 `a.length < size` 即当前集合元素的个数与参数 a 数组元素的大小的时候将和 `toArray()` 一样返回一个新的数组。

2. 如果 `a.length  == size` 将不会产生新的数组直接将集合中的元素调用 ` System.arraycopy` 方法将元素复制到参数数组中，返回 a。

3. ` a.length  > size` 也不会产生新的数组,但是值得注意的是 `a[size] = null;` 这一句改变了原数组中 index = size 位置的元素，被重新设置为 null 了。

下面我们来看下第三种情况的例子：


```
SubClass[] sourceMore = new SubClass[4];
   for (int i = 0; i < sourceMore.length; i++) {
       sourceMore[i] = new SubClass(i);
}
        
//当 List.toArray(T[] a) 中 a.length == list.size 的时候使用 Array.copyOf 会将 list 中的内容赋值给 sourceMore 并将其返回
//sourceMore[0,size-1] = list{0, size-1} 而 sourceMore[size] = null

SubClass[] sourceMore = new SubClass[4];
for (int i = 0; i < sourceMore.length; i++) {
  sourceMore[i] = new SubClass(i);
}

//list to Array 之前 sourceMore [SubClass{test=0}, SubClass{test=1}, SubClass{test=2}, SubClass{test=3}]   sourceEqual.length:: 4
System.out.println("list to Array 之前 sourceMore " + Arrays.toString(sourceMore) + "   sourceEqual.length:: " + sourceMore.length);

SubClass[] desSourceMore = tLists.toArray(sourceMore);
//list to Array 之后 desSourceMore [SubClass{test=1}, SubClass{test=2}, null, SubClass{test=3}]desSourceMore.length:: 4
System.out.println("list to Array 之后 desSourceMore " + Arrays.toString(desSourceMore) + "desSourceMore.length:: " + desSourceMore.length);

//list to Array 之后 source [SubClass{test=1}, SubClass{test=2}, null, SubClass{test=3}]sourceEqual.length:: 4
System.out.println("list to Array 之后 source " + Arrays.toString(sourceMore) + "sourceEqual.length:: " + sourceMore.length);

//source ==  desSource true
System.out.println("source ==  desSource " + (sourceMore == desSourceMore));

```


## ArrayList 的遍历

ArrayList 的遍历方式 jdk 1.8 之前有三种 ：for 循环遍历， foreach 遍历，迭代器遍历,jdk 1.8 之后又引入了forEach 操作，我们先来看看迭代器的源码实现：

### 迭代器

迭代器 `Iterator` 模式是用于遍历各种集合类的标准访问方法。它可以把访问逻辑从不同类型的集合类中抽象出来，从而避免向客户端暴露集合的内部结构。 `ArrayList` 作为集合类也不例外，迭代器本身只提供三个接口方法：

```
   public interface Iterator {
     　　boolean hasNext();//是否还有下一个元素
     　　Object next();// 返回当前元素 可以理解为他相当于 fori 中 i 索引
     　　void remove();// 移除一个当前的元素 也就是 next 元素。
    }
```

`ArrayList` 中调用   `iterator()` 将会返回一个内部类对象 `Itr`  其实现了 `Iterator` 接口。

```
public Iterator<E> iterator() {
        return new Itr();
}
```

下面让我们看下其实现的源码：

正如我们的 for 循环遍历一样，数组角标总是从 0 开始的，所以 `cursor` 初始值为 0 ， `hasNext` 表示是否遍历到数组末尾，即 i < size 。对于 modCount 变量之所以一直没有介绍是因为他集合并发访问有关系，用于标记当前集合被修改（增删）的次数，如果并发访问了集合那么将会导致这个 modCount 的变化，在遍历过程中不正确的操作集合将会抛出 `ConcurrentModificationException` ，这是 Java 「fast-fail 的机制」，对于如果正确的在遍历过程中操作集合稍后会有说明。

```
private class Itr implements Iterator<E> {
   int cursor; // 对照 hasNext 方法 cursor 应理解为下个调用 next 返回的元素 初始为 0
   int lastRet = -1; // 上一个返回的角标
   int expectedModCount = modCount;//初始化的时候将其赋值为当前集合中的操作数，
   // 是否还有下一个元素 cursor == size 表示当前集合已经遍历完了 所以只有当 cursor 不等于 size 的时候 才会有下一个元素
   public boolean hasNext() {
       return cursor != size;
   }

```

next 方法是我们获取集合中元素的方法，next 返回当前遍历位置的元素，如果在调用 next 之前集合被修改，并且迭代器中的期望操作数并没有改变，将会引发`ConcurrentModificationException`。next 方法多次调用 `checkForComodification` 来检验这个条件是否成立。

```
   @SuppressWarnings("unchecked")
   public E next() {
        // 验证期望的操作数与当前集合中的操作数是否相同 如果不同将会抛出异常
       checkForComodification();
       // 如果迭代器的索引已经大于集合中元素的个数则抛出异常，这里不抛出角标越界
       int i = cursor;
       if (i >= size)
           throw new NoSuchElementException();
           
       Object[] elementData = ArrayList.this.elementData;
       // 由于多线程的问题这里再次判断是否越界，如果有异步线程修改了List（增删）这里就可能产生异常
       if (i >= elementData.length)
           throw new ConcurrentModificationException();
       // cursor 移动
       cursor = i + 1;
       //最终返回 集合中对应位置的元素，并将 lastRet 赋值为已经访问的元素的下标
       return (E) elementData[lastRet = i];
   }

```

只有 `Iterator` 的 `remove` 方法会在调用集合的 `remove` 之后让 期望 操作数改变使`expectedModCount`与 `modCount` 再相等，所以是安全的。

```
    // 实质调用了集合的 remove 方法移除元素
   public void remove() {
        // 比如操作者没有调用 next 方法就调用了 remove 操作，lastRet 等于 -1的时候抛异常
       if (lastRet < 0)
           throw new IllegalStateException();
           
        //检查操作数
       checkForComodification();
    
       try {
            //移除上次调用 next 访问的元素
           ArrayList.this.remove(lastRet);
           // 集合中少了一个元素，所以 cursor 向前移动一个位置（调用 next 时候 cursor = lastRet + 1）
           cursor = lastRet;
           //删除元素后赋值-1，确保先前 remove 时候的判断
           lastRet = -1;
           //修改操作数期望值， modCount 在调用集合的 remove 的时候被修改过了。
           expectedModCount = modCount;
       } catch (IndexOutOfBoundsException ex) {
            // 集合的 remove 会有可能抛出 rangeCheck 异常，catch 掉统一抛出 ConcurrentModificationException 
           throw new ConcurrentModificationException();
       }
   }

```

检查期望的操作数与当前集合的操作数是否相同。Java8 发布了很多函数式编程的特性包括 `lamada` 和`Stream` 操作。迭代器也因此添加了 `forEachRemaining` 方法，这个方法可以将当前迭代器访问的元素（next 方法）后的元素传递出去还没用到过，源码就不放出来了,大家有兴趣自己了解下。

```
   @Override
   @SuppressWarnings("unchecked")
   public void forEachRemaining(Consumer<? super E> consumer) {
     ... Java8 的新特性，可以将当前迭代器访问的元素（next 方法）后的元素传递出去还没用到过，源码就不放出来了,大家有兴趣自己了解下。
   }
    // 检查期望的操作数与当前集合的操作数是否相同
   final void checkForComodification() {
       if (modCount != expectedModCount)
           throw new ConcurrentModificationException();
   }
}
```

### ListIterator 迭代器

`ArrayList` 可以通过以下两种方式获取 `ListIterator` 迭代器，区别在于初始角标的位置。不带参数的迭代器默认的`cursor = 0`。


```
public ListIterator<E> listIterator(int index) {
   if (index < 0 || index > size)
       throw new IndexOutOfBoundsException("Index: "+index);
   return new ListItr(index);
}
    
public ListIterator<E> listIterator() {
   return new ListItr(0);
}
```
`ListItr`对象继承自前边分析的 `Itr`，也就是说他拥有 Itr 的所有方法，并在此基础上进行扩展，其扩展了访问当前角标前一个元素的方法。以及在遍历过程中添加元素和修改元素的方法。

`ListItr` 的构造方法如下：

```
private class ListItr extends Itr implements ListIterator<E> {
   ListItr(int index) {
       super();
       cursor = index;
}
```

**`ListItr` 的 `previous` 方法：**

```
public boolean hasPrevious() {
 // cursor = 0 表示游标在数组第一个元素的左边，此时 `hasPrevious` 返回false
  return cursor != 0;
}

public int nextIndex() {
  return cursor;//调用返回当前角标位置
}

public int previousIndex() {
  return cursor - 1;//调用返回上一个角标
}

//返回当前角标的上一个元素，并前移移动角标
@SuppressWarnings("unchecked")
public E previous() {
  // fast-fail 检查
  checkForComodification();
  int i = cursor - 1;
  // 如果前移角标 <0 代表遍历到数组遍历完成，一般在调用 previous 要调用 hasPrevious 判断
  if (i < 0)
      throw new NoSuchElementException();
  //获取元素    
  Object[] elementData = ArrayList.this.elementData;
  if (i >= elementData.length)
      throw new ConcurrentModificationException();
  //获取成功后修改角标位置和 lastRet 位置    
  cursor = i;
  return (E) elementData[lastRet = i];
}
```

**`ListItr` 的 `add` 方法**

```
public void add(E e) {
  // fast-fail 检查
  checkForComodification();
  try {
      // 获取当前角标位置，一般的是调用 previous 后，角标改变后后去 cursor 
      int i = cursor;
      //添加元素在角标位置
      ArrayList.this.add(i, e);
      //集合修改完成后要改变当前角标位置
      cursor = i + 1;
      //重新置位 -1 如果使用迭代器修改了角标位置元素后不允许立刻使用 set 方法修改修改后角标未知的额元素 参考 set 的源代码
      lastRet = -1;
      expectedModCount = modCount;
  } catch (IndexOutOfBoundsException ex) {
      throw new ConcurrentModificationException();
  }
}
```

可能对比两个迭代器后，会对 `curor` 指向的位置有所疑惑，现在我们来看下一段示例代码对应的图：


```
private void testListItr(){
   ArrayList<Integer> list  = new ArrayList<>();
   list.add(1);
   list.add(2);
   list.add(3);
   list.add(4);

   ListIterator<Integer> listIterator = list.listIterator(list.size());

   while (listIterator.hasPrevious()){

       if (listIterator.previous() == 2){
           listIterator.add(0); 
//                listIterator.set(10); //Exception in thread "main" java.lang.IllegalStateException
       }

   }

   System.out.println("list " + list.toString());

}
```

![](https://ws4.sinaimg.cn/large/006tNc79ly1fppdkn1vu8j31f60s20t2.jpg)

由此可以看 `cursor` 于 数组角标不同，它可以处的位置总比角标多一个，因为在我们使用 `Iterator` 操作集合的时候，总是要先操作  `cursor` 移动， `listIterator.previous` 也好 `iterator.next()` 也好，都是一样的道理，如果不按照规定去进行操作，带给使用者的只有异常。


### java8 新增加的遍历方法 forEach

java8增加很多好用的 API，工作和学习中也在慢慢接触这些 API，`forEach` 操作可能是我继 lambda 后，第一个使用的 API 了（囧），jdk doc 对这个方法的解释是：

对此集合的每个条目执行给定操作，直到处理完所有条目或操作抛出异常为止。 除非实现类另有规定，否则按照条目集迭代的顺序执行操作（如果指定了迭代顺序）。操作抛出的异常需要调用者自己处理。

其实其内部实现也很简单，只是一个判断了操作数的 for 循环，所以在效率上不会有提升，但是在安全性上的确有提升，也少些很多代码不是么？

```
@Override
public void forEach(Consumer<? super E> action) {
    //检查调用者传进来的操作函数是否为空
   Objects.requireNonNull(action);
   //与迭代不同期望操作被赋值为 final 也就是 forEach 过程中不允许并发修改集合否则会抛出异常
   final int expectedModCount = modCount;
   @SuppressWarnings("unchecked")
   final E[] elementData = (E[]) this.elementData;
   final int size = this.size;
   //每次取元素之前判断操作数，确保操作正常
   for (int i=0; modCount == expectedModCount && i < size; i++) {
       action.accept(elementData[i]);
   }
   if (modCount != expectedModCount) {
       throw new ConcurrentModificationException();
   }
}

```

对于高级 for 循环以及最普通的 fori 方法这里不再赘述。下面我们看下面试会问到一个问题，也是我们在单线程操作集合的时候需要注意的一个问题，如果正确的在遍历过程中修改集合。

### 错误操作 1 在 for循环修改集合后继续遍历

第一个例子：

```
List<SubClass> list2 = new ArrayList<>();

list2.add(new SubClass(1));
list2.add(new SubClass(2));
list2.add(new SubClass(3));
list2.add(new SubClass(3));

for (int i = 0; i < list2.size(); i++) {
  if (list2.get(i).test == 3) {
      list2.remove(i);
  }
}
System.out.println(list2);
//[SubClass{test=1}, SubClass{test=2}, SubClass{test=3}]

```
这个例子我们会发现，程序并没有抛出异常，但是从运行经过上来看并不是我们想要的，因为还有 `SubClass.test = 3 `的数据在，这是因为 remove 操作改变了` list.size()`,而 fori 中每次执行都会重新调用一次` lists2.size()`，当我们删除了倒数第二个元素后，`list2.size() = 3,i = 3 < 3` 不成立则没有在进行 remove 操作，知道了为什么以后我们试着这样改变了循环方式：


```
int size = list2.size();
for (int i = 0; i < size; i++) {
  if (list2.get(i).test == 3) {
      list2.remove(i);//remove 以后 list 内部将 size 重新改变了 for 循环下次调用的时候可能就不进去了
  }
}
System.out.println(list2);

//Exception in thread "main" java.lang.IndexOutOfBoundsException: Index: 3, Size: 3
```

果真程序抛出了角标越界的异常，因为这样每次 fori 的时候我们不去拿更新后的 list 元素的 size 大小，所以当我们删除一个元素后，size = 3 当我们 for 循环去` list2.get(3)`的时候就会被 `rangeCheck`方法抛出异常。

### 错误操作导致 ConcurrentModificationException 异常

我们分析迭代器的时候，知道 `ConcurrentModificationException`是指因为迭代器调用 `checkForComodification` 方法比较 `modCount` 和 `expectedModCount` 方法大小的时候抛出异常。我们在分析 ArrayList 的时候在每次对集合进行修改， 即有 add 和 remove 操作的时候每次都会对 `modCount ++ `。

modCount 这个变量主要用来记录 `ArrayList` 被修改的次数，那么为什么要记录这个次数呢？是为了防止多线程对同一集合进行修改产生错误，记录了这个变量，在对 ArrayList 进行迭代的过程中我们能很快的发现这个变量是否被修改过，如果被修改了 `ConcurrentModificationException` 将会产生。下面我们来看下例子，这个例子并不是在多线程下的，而是因为我们在同一线程中对 list 进行了错误操作导致的：



```
Iterator<SubClass> iterator = lists.iterator();

while (iterator.hasNext()) {
  SubClass next = iterator.next();
  int index = next.test;
  if (index == 3) {
      list2.remove(index);//操作1： 注意是 list2.remove 操作
      //iterator.remove()；/操作2 注意是 iterator.remove 操作
  }
}
//操作1： Exception in thread "main" java.util.ConcurrentModificationException
//操作2：  [SubClass{test=1}, SubClass{test=2}]
System.out.println(list2);
```

我们对操作1，2分别运行程序，可以看到，操作1很快就抛出了 `java.util.ConcurrentModificationException` 异常，操作2 则顺利运行出正常结果，如果对 `modCount ` 注意了的话，我们很容易理解，`list.remove(index)` 操作会修改`List` 的 `modCount`，而 `iterator.next()` 内部每次会检验 `expectedModCount != modCount `，所以当我们使用 `list.remove` 下一次再调用 `iterator.next()` 就会报错了，而`iterator.remove`为什么是安全的呢？因为其操作内部会在调用 `list.remove` 后重新将新的 `modCount` 赋值给  `expectedModCount`。所以我们直接调用 list.remove 操作是错误的。对于多线程的影响这里不在展开这里推荐有兴趣的朋友看下这个文章 [Java ConcurrentModificationException异常原因和解决方法](http://www.cnblogs.com/dolphin0520/p/3933551.html);

经过了一轮分析我们我们知道了错误产生原因了，但是大家是否能真的分辨出什么操作是错误的呢？我们来看下边这个面试题，这是我在网上无意中看到的一道大众点评的面试题：


```
ArrayList<String> list = new ArrayList<String>();
for (int i = 0; i < 10; i++) {
  list.add("sh" + i);
}

for (int i = 0; list.iterator().hasNext(); i++) {
  list.remove(i);
  System.out.println("秘密" + list.get(i));
}
```

### 一道面试题

相信大家肯定知道这样操作是会产生错误的，但是最终会抛出角标越界还是`ConcurrentModificationException`呢？

其实这里会抛出角标越界异常，为什么呢，因为 for 循环的条件 `list.iterator().hasNext()`，我们知道 `list.iterator()` 将会new 一个新的  iterator 对象，而在 new 的过程中我们将 每次 `list.remove` 后的 `modCount` 赋值给了新的 `iterator` 的 `expectedModCount`，所以不会抛出 `ConcurrentModificationException` 异常，而 `hasNext` 内部只判断了 size 是否等于 `cursor != size ` 当我们删除了一半元素以后，size 变成了 5 而新的  `list.iterator()` 的 cursor 等于 0 ，`0!=5` for 循环继续，那么当执行到 `list.remove（5）`的时候就会抛出角标越界了。


## 总结

1. ArrayList 底层是一个动态扩容的数组结构,每次扩容需要增加1.5倍的容量
2.  ArrayList 扩容底层是通过 `Arrays.CopyOf` 和  `System.arraycopy` 来实现的。每次都会产生新的数组，和数组中内容的拷贝，所以会耗费性能，所以在多增删的操作的情况可优先考虑 LinkList 而不是 ArrayList。
3. ArrayList 的 toArray 方法重载方法的使用。
2. 允许存放（不止一个） null 元素，
3. 允许存放重复数据，存储顺序按照元素的添加顺序
4. ArrayList 并不是一个线程安全的集合。如果集合的增删操作需要保证线程的安全性，可以考虑使用 `CopyOnWriteArrayList` 或者使`collections.synchronizedList(List l)`函数返回一个线程安全的ArrayList类.
5. 不正确访问集合元素的时候 `ConcurrentModificationException`和 `java.lang.IndexOutOfBoundsException` 异常产生的时机和原理。


本文又长篇大论的分析了一波 `ArrayList` 的源码，对我个人而言这很有意义，在查看源码的过程中，注意到了平时很少有机会接触的知识点。当然这只是集合源码分析的开端，以后还会更细，其他常用集合源码的分析。如果大家感觉我写的还可以， 请留言 + 点赞 + 关注。


