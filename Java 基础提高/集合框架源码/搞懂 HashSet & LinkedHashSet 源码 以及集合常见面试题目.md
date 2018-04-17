![](https://ws4.sinaimg.cn/large/006tKfTcly1fqg6s4mdkbj30hs0a040x.jpg)

# 搞懂 HashSet & LinkedHashSet 源码 以及集合常见面试题目

经过上两篇的 [HashMap](https://juejin.im/post/5ac83fa35188255c5668afd0) 和 [LinkedHashMap](https://juejin.im/post/5ace2bde6fb9a028e25deca8) 源码分析以后，本文将继续分析 JDK 集合之 Set 源码，由于有了之前的 Map 源码分析的铺垫，Set 源码就简单很多了，本文的篇幅也将比之前短很多。查看 Set 源码的构造参数就可以知道，Set 内部其实维护的就是一个 Map，只是单单使用了 Entry 中的 key 。那么本文将不再赘述内部数据结构，而是通过部分的源码，来说明两个 Set 集合与 Map 之间的关系。本文将从以下几部分叙述：

1. **Set 集合概述**
2. **HashSet 源码简单分析**
3. **LinkedHashSet 源码简单分析**
4. **关于面试中的集合问题总结**


## Set 集合概述

![](https://ws3.sinaimg.cn/large/006tKfTcly1fqg1wuwccuj317s0gaabq.jpg)

> 图片来自互联网侵删

由于本篇文章主要叙述 Set 容器以及和 Map 容器之间关系，我们只需要关注上述集合图谱中 Set 部分。可以看出 Set 主要的实现类有 `HashSet` 和 `TreeSet` 以及没有画出的 `LinkedHashSet`。其中 `HashSet` 的实现依赖于 `HashMap`， `TreeSet` 的实现依赖于 `TreeMap`，`LinkedHashSet` 的实现依赖于 `LinkedHashMap`。


从各个实现类的声明也可以看出其继承关系

```
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
    
    
public class LinkedHashSet<E>
       extends HashSet<E>
       implements Set<E>, Cloneable, java.io.Serializable 
    
public class TreeSet<E> extends AbstractSet<E>
    implements NavigableSet<E>, Cloneable, java.io.Serializable  
       
```

在看 Set 的之前，我们先概括的说下 Set 集合的特点

1. HashSet 底层是数组 + 单链表 + 红黑树的数据结构
2. LinkedHashSet 底层是 数组 + 单链表 + 红黑树 + 双向链表的数据结构
3. Set 不允许存储重复元素，允许存储 null
4. HashSet 存储元素是无序且不等于访问顺序
5. LinkedHashSet 存储元素是无序的，但是由于双向链表的存在，迭代时获取元素的顺序等于元素的添加顺序，注意这里不是访问顺序


## HashSet 的源码分析

HashSet 源码只有短短的 300 行，上文也阐述了实现依赖于 HashMap，这一点充分体现在其构造方法和成员变量上。我们来看下 HashSet 的构造方法和成员变量：


```
 // HashSet 真实的存储元素结构
 private transient HashMap<E,Object> map;

 // 作为各个存储在 HashMap 元素的键值对中的 Value
 private static final Object PRESENT = new Object();
    
 //空参数构造方法 调用 HashMap 的空构造参数  
 //初始化了 HashMap 中的加载因子 loadFactor = 0.75f
 public HashSet() {
        map = new HashMap<>();
 }
 
 //指定期望容量的构造方法
 public HashSet(int initialCapacity) {
    map = new HashMap<>(initialCapacity);
 }
 //指定期望容量和加载因子
 public HashSet(int initialCapacity, float loadFactor) {
    map = new HashMap<>(initialCapacity, loadFactor);
 }
 //使用指定的集合填充Set
 public HashSet(Collection<? extends E> c) {
        //调用  new HashMap<>(initialCapacity) 其中初始期望容量为 16 和 c 容量 / 默认 load factor 后 + 1的较大值
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
 }

 // 该方法为 default 访问权限，不允许使用者直接调用，目的是为了初始化 LinkedHashSet 时使用
 HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
 }
```

通过 HashSet 的构造参数我们可以看出每个构造方法，都调用了对应的 HashMap 的构造方法用来初始化成员变量 map ，因此我们可以知道，HashSet 的初始容量也为 `1<<4` 即16，加载因子默认也是 0.75f。

我们都知道 Set 不允许存储重复元素，又由构造参数得出结论底层存储结构为 HashMap,那么这个不可重复的属性必然是有 HashMap 中存储键值对的 Key 来实现了。在分析 HashMap 的时候，提到过 HashMap 通过存储键值对的 Key 的 hash 值（经过扰动函数hash()处理后）来决定键值对在哈希表中的位置，当 Key 的 hash 值相同时，再通过 equals 方法判读是否是替换原来对应 key 的 Value 还是存储新的键值对。那么我们在使用 Set 方法的时候也必须保证，存储元素的 HashCode 方法以及 equals 方法被正确覆写。

HashSet 中的添加元素的方法也很简单，我们来看下实现：

```
public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}
```

可以看出 add 方法调用了 `HashMap` 的 put 方法，构造的键值对的 key 为待添加的元素，而 Value 这时有全局变量 `PRESENT` 来充当，这个`PRESENT`只是一个 Object 对象。

除了 add 方法外 `HashSet` 实现了 Set 接口中的其他方法这些方法有：


```
public int size() {
        return map.size();
}

public boolean isEmpty() {
   return map.isEmpty();
}

public boolean contains(Object o) {
   return map.containsKey(o);
}

//调用 remove(Object key)  方法去移除对应的键值对
public boolean remove(Object o) {
   return map.remove(o)==PRESENT;
}

public void clear() {
   map.clear();
}

// 返回一个 map.keySet 的 HashIterator 来作为 Set 的迭代器
public Iterator<E> iterator() {
   return map.keySet().iterator();
}
```

关于迭代器我们在讲解 `HashMap` 中的时候没有详细列举，其实 `HashMap` 提供了多种迭代方法，每个方法对应了一种迭代器，这些迭代器包括下述几种，而 `HashSet` 由于只关注 Key 的内容，所以使用 `HashMap` 的内部类 `KeySet` 返回了一个 `KeyIterator` ，这样在调用 next 方法的时候就可以直接获取下个节点的 key 了。


```
//HashMap 中的迭代器

final class KeyIterator extends HashIterator
   implements Iterator<K> {
   public final K next() { return nextNode().key; }
}

final class ValueIterator extends HashIterator
   implements Iterator<V> {
   public final V next() { return nextNode().value; }
}

final class EntryIterator extends HashIterator
   implements Iterator<Map.Entry<K,V>> {
   public final Map.Entry<K,V> next() { return nextNode(); }
}

```

关于 HashSet 中的源码分析就这些，其实除了一些序列化和克隆的方法以外，我们已经列举了所有的 HashSet 的源码，有没有感觉巨简单，其实下面的 LinkedHashSet 由于继承自 HashSet 使得其代码更加简单只有短短100多行不信继续往下看。

![](https://ws4.sinaimg.cn/large/006tKfTcly1fqg4aup0oyj30ar09taa1.jpg)


## LinkedHashSet 源码分析

在上述分析 `HashSet` 构造方法的时候，有一个 default 权限的构造方法没有讲，只说了其跟 LinkedHashSet 构造有关系，该构造方法内部调用的是 LinkedHashMap 的构造方法。

LinkedHashMap 较之 HashMap 内部多维护了一个双向链表用来维护元素的添加顺序：


```
// dummy 参数没有作用这里可以忽略
HashSet(int initialCapacity, float loadFactor, boolean dummy) {
   map = new LinkedHashMap<>(initialCapacity, loadFactor);
}

//调用 LinkedHashMap 的构造方法，该方法初始化了初始起始容量，以及加载因子，
//accessOrder = false 即迭代顺序不等于访问顺序
public LinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
}

```
LinkedHashSet的构造方法一共有四个，统一调用了父类的 `HashSet(int initialCapacity, float loadFactor, boolean dummy) `构造方法。

```
//初始化 LinkedHashMap 的初始容量为诶 16 加载因子为 0.75f
public LinkedHashSet() {
   super(16, .75f, true);
}

//初始化 LinkedHashMap 的初始容量为 Math.max(2*c.size(), 11) 加载因子为 0.75f 
public LinkedHashSet(Collection<? extends E> c) {
   super(Math.max(2*c.size(), 11), .75f, true);
   addAll(c);
}

//初始化 LinkedHashMap 的初始容量为参数指定值 加载因子为 0.75f 
public LinkedHashSet(int initialCapacity) {
   super(initialCapacity, .75f, true);
}
 
 //初始化 LinkedHashMap 的初始容量,加载因子为参数指定值 
 public LinkedHashSet(int initialCapacity, float loadFactor) {
   super(initialCapacity, loadFactor, true);
}
```

完了..没错，LinkedHashSet 源码就这几行，所以可以看出其实现依赖于 LinkedHashMap 内部的数据存储结构。


## 关于面试中的集合问题总结

之前分析了多篇关于 JDK 集合源码的文章，而这些集合源码中的知识点都是面试的时候常客，因此在本篇结尾作为 "充数" 的一节，我们来以面试题的形式总结一下之前所分过的源码中的知识点，这些知识点在之前的文章中都有详细的分析，如果有疑问可以回顾一下之前的源码分析文章。

-  **ArrayList 与 LinkedList 区别 ?**

 1. 存储结构上 ArrayList 底层使用数组进行元素的存储，LinkedList 使用双向链表作为存储结构。
 
 2. 两者均与允许存储 null 也允许存储重复元素。
 
 3. 在性能上 ArrayList 在存储大量元素时候的增删效率 平均低于 LinkedList，因为 ArrayList 在增删的是需要拷贝元素到新的数组，而 LinkedList 只需要将节点前后指针指向改变。
 
 4. 在根据角标获取元素的时间效率上ArrayList优于 LinkedList，因为数组本身有存储连续，有 index 角标，而 LinkedList 存储元素离散，需要遍历链表。
 
 6. 不要使用 for 循环去遍历 LinkedList 因为效率很低。

 5. 两者都是线程不安全的，都可以使用 `Collections.synchronizedList(List<E> list)` 方法生成一个线程安全的 List。

- **ArrayList 与 Vector 区别（为什么要用Arraylist取代Vector呢？）**

 1. ArrayList 的扩容机制由于 Vector ， ArrayList 每次 resize 增加 1.5 倍的容量，Vector 每次增加 2倍的容量，在存储大量元素后扩容的时候就能有很大的空间节省。
 2. Vector 添加删除方法以及迭代器遍历的方法都是 synchronized 修饰的方法，在线程安全的情况下使用效率低于 ArrayList
 3. ArrayList 和 LinkedList 通过`Collections.synchronizedList(List<E> list)` 的线程同步的集合，迭代器并不同步，需要使用者去加锁。

- **简述 HashMap 的工作原理 JDK 1.8后做了哪些优化**
  
  1. JDK 1.7 HashMap 底层采用单链表 + 数组的存储结构存储元素（键值对）。JDK1.8之后 HashMap 在同一哈希桶中节点数量（单链表长度）超过 8之后会使用 红黑树替换单链表来提高效率
  2. HashMap 通过键值对的 key 的 hashCode 值经过扰动函数处理后确定存储的数组角标位置，1.7 中扰动函数使用了 4次位运算 + 5次异或运算，1.8 中降低到 1次位运算 + 1次异或运运算
  3. HashMap 扩容的时候会增加原来数组长度两倍，并对所存储的元素节点hash 值的重新计算，1.7中 HashMap 会重新调用 hash 函数计算新的位置，而 1.8中对此进行了优化通过 `(e.hash & oldCap) == 0` 来确定节点新位置是位于扩容前的角标还是之前的 2倍角标位置。
  4. HashMap 在多线程使用前提下，扩容的时候可能会导致循环链表的情况，当然我们不应在线程不安全的情况下使用 HashMap
  

- **HashMap 和 HashTable 的区别**

    1. `HashMap` 是线程不安全的，HashTable是线程安全的。
    2. `HashMap` 允许 key 和 Vale 是 null，但是只允许一个 key 为 null,且这个元素存放在哈希表 0 角标位置。 `HashTable` 不允许key、value 是 null
    3. `HashMap` 内部使用`hash(Object key)`扰动函数对 key 的 `hashCode` 进行扰动后作为 `hash` 值。`HashTable` 是直接使用 key 的 `hashCode()` 返回值作为 hash 值。
    
    4. `HashMap`默认容量为 2^4 且容量一定是 2^n ; `HashTable` 默认容量是11,不一定是 2^n
    
    5. `HashTable` 取哈希桶下标是直接用模运算,扩容时新容量是原来的2倍+1。`HashMap` 在扩容的时候是原来的两倍，且哈希桶的下标使用 &运算代替了取模。
    
- **HashMap 和 LinkedHashMap 的区别**


 1. LinkedHashMap 拥有与 HashMap 相同的底层哈希表结构，即数组 + 单链表 + 红黑树，也拥有相同的扩容机制。

 1. LinkedHashMap 相比 HashMap 的拉链式存储结构，内部额外通过 Entry 维护了一个双向链表。

 2. HashMap 元素的遍历顺序不一定与元素的插入顺序相同，而 LinkedHashMap 则通过遍历双向链表来获取元素，所以遍历顺序在一定条件下等于插入顺序。

 3. LinkedHashMap 可以通过构造参数 accessOrder 来指定双向链表是否在元素被访问后改变其在双向链表中的位置。

- **HashSet 如何检查重复,与 HashMap 的关系？**

 1. HashSet 内部使用 HashMap 存储元素，对应的键值对的键为 Set 的存储元素，值为一个默认的 Object 对象。
 2. HashSet 通过存储元素的 hashCode 方法和 equals 方法来确定元素是否重复。

-  是否了解 `fast-fail` 规则 简单说明一下

  1. 快速失败（fail—fast）在用迭代器遍历一个集合对象时，如果遍历过程中集合对象中的内容发生了修改（增加、删除、修改），则会抛出`ConcurrentModificationException`。

 2. 迭代器在遍历时直接访问集合中的内容，并且在遍历过程中使用一个 `modCount` 变量。集合在被遍历期间如果内容发生变化，就会改变 `modCount` 的值。每当迭代器使用`hasNext()/next()` 遍历下一个元素之前，都会检测 `modCount` 变量是否为`expectedmodCount` 值，是的话就返回遍历值；否则抛出异常，终止遍历。

 3. 场景：java.util包下的集合类都是快速失败的，不能在多线程下发生并发修改（迭代过程中被修改）。
 
- **集合在遍历过程中是否可以删除元素，为什么迭代器就可以安全删除元素**

 1. 集合在使用 for 循环或者高级 for 循环迭代的过程中不允许使用，集合本身的 remove 方法删除元素，如果进行错误操作将会导致 `ConcurrentModificationException`异常的发生

 1. Iterator 可以删除访问的当前元素(current)，一旦删除的元素是Iterator 对象中 next 所正在引用的，在 Iterator 删除元素通过 修改 modCount 与 expectedModCount 的值，可以使下次在调用 remove 的方法时候两者仍然相同因此不会有异常产生。


## 总结

本文分析了 JDK 中 `HashSet` 和 `LinkedHashSet` 的源码实现，阐述了Set 与 Map 的关系，也通过最后一节的面试题总结复习了一下之前几篇源码分析文章的知识点。之后可能会继续分析一下 Android 中特有的 `ArrayMap` 和 `SparseArray` 源码分析。

集合源码分析文章目录,欢迎大家查看。


1. [搞懂 Java HashMap 源码](https://juejin.im/post/5ac83fa35188255c5668afd0) 
2. [搞懂 Java LinkedHashMap 源码](https://juejin.im/post/5ace2bde6fb9a028e25deca8)
3. [搞懂 Java ArrayList 源码](https://juejin.im/post/5ab548f75188257ddb0f8fa2)
4. [搞懂 Java LinkedList 源码](https://juejin.im/post/5abfde3f6fb9a028e46ec51c)
5. [搞懂 Java equals 和 hashCode 方法](https://juejin.im/post/5ac4d8abf265da23a4050ae3)
6. [Java List 容器源码分析的补充](https://juejin.im/post/5ac3a5f7f265da239f0792f8)


