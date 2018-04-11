![](https://ws2.sinaimg.cn/large/006tKfTcly1fq3y9oyycnj30et08c0t3.jpg)

# HashMap 源码分析

前几篇分析了 `ArrayList` ， `LinkedList` ，`Vector` ，`Stack`  List 集合的源码，Java 容器除了包含 List 集合外还包含着 Set 和 Map 两个重要的集合类型。而 `HashMap` 则是最具有代表性的，也是我们最常使用到的 Map 集合。我们这篇文章就来试着分析下 `HashMap` 的源码，由于 `HashMap` 底层涉及到太多方面，一篇文章总是不能面面俱到，所以我们可以带着面试官常问的几个问题去看源码：

1. 了解底层如何存储数据的
2. HashMap 的几个主要方法
2. HashMap 是如何确定元素存储位置的以及如何处理哈希冲突的
3. HashMap 扩容机制是怎样的
4. JDK 1.8 在扩容和解决哈希冲突上对 HashMap 源码做了哪些改动？有什么好处?

本文也将从以上几个方面来展开叙述：

> 由于掘金后台审核可能会由于某些原因造成文章发布延迟或者遗漏，如果感觉我写的源码分析文章还不错，可以关注我，以后我每次更新文章就可以收到推送了。另外博主也是在努力进步中，所有文章如果有问题请尽管留言给我。我会及时改正。大家一起进步。

## 概述

为了方便下边的叙述这里需要先对几个常见的关于 `HashMap` 的知识点进行下概述：

1. `HashMap` 存储数据是根据键值对存储数据的，并且存储多个数据时，数据的键不能相同，如果相同该键之前对应的值将被覆盖。注意如果想要保证 `HashMap` 能够正确的存储数据，请确保作为键的类，已经正确覆写了 `equals()` 方法。

2. `HashMap` 存储数据的位置与添加数据的键的 `hashCode()` 返回值有关。所以在将元素使用 HashMap 存储的时候请确保你已经按照要求重写了 `hashCode(）`方法。这里说有关系代表最终的存储位置不一定就是 `hashCode` 的返回值。

3. `HashMap` 最多只允许一条存储数据的键为 null，可允许多条数据的值为 null。

4. `HashMap` 存储数据的顺序是不确定的，并且可能会因为扩容导致元素存储位置改变。因此遍历顺序是不确定的。

5. `HashMap` 是线程不安全的，如果需要再多线程的情况下使用可以用 `Collections.synchronizedMap(Map map)` 方法使 `HashMap` 具有线程安全的能力，或者使用 `ConcurrentHashMap`。


## 了解 HashMap 底层如何存储数据的

要想分析 HashMap 源码，就必须在 JDK1.8 和 JDK1.7之间划分一条线，因为在 JDK 1.8 后对于 HashMap 做了底层实现的改动。

### JDK 1.7 之前的存储结构

通过上篇文章[搞懂 Java equals 和 hashCode 方法](https://juejin.im/post/5ac4d8abf265da23a4050ae3) 我们以及对 hash 表有所了解，我们了解到及时 hashCode() 方法已经写得很完美了，终究还是有可能导致 「hash碰撞」的，`HashMap` 作为使用 hash 值来决定元素存储位置的集合也是需要处理 hash 冲突的。在1.7之前JDK采用「拉链法」来存储数据，即数组和链表结合的方式：

![](https://ws3.sinaimg.cn/large/006tKfTcly1fq22kbps76j31400z474f.jpg)

「拉链法」用专业点的名词来说叫做**链地址法**。简单来说，就是数组加链表的结合。在每个数组元素上存储的都是一个链表。

我们之前说到不同的 key 可能经过 hash 运算可能会得到相同的地址，但是一个数组单位上只能存放一个元素，采用链地址法以后，如果遇到相同的 hash 值的 key 的时候，我们可以将它放到作为数组元素的链表上。待我们去取元素的时候通过 hash 运算的结果找到这个链表，再在链表中找到与 key 相同的节点，就能找到 key 相应的值了。

JDK1.7中新添加进来的元素总是放在数组相应的角标位置，而原来处于该角标的位置的节点作为 next 节点放到新节点的后边。稍后通过源码分析我们也能看到这一点。

### JDK1.8中的存储结构。

对于 JDK1.8 之后的` HashMap`底层在解决哈希冲突的时候，就不单单是使用数组加上单链表的组合了，因为当处理如果 hash 值冲突较多的情况下，链表的长度就会越来越长，此时通过单链表来寻找对应 Key 对应的 Value 的时候就会使得时间复杂度达到 O(n),因此在 JDK1.8 之后，在链表新增节点导致链表长度超过 `TREEIFY_THRESHOLD = 8` 的时候，就会在添加元素的同时将原来的单链表转化为红黑树。

对数据结构很在行的读者应该，知道红黑树是一种易于增删改查的二叉树，他对与数据的查询的时间复杂度是 `O(logn)` 级别，所以利用红黑树的特点就可以更高效的对 `HashMap` 中的元素进行操作。

![](https://ws3.sinaimg.cn/large/006tKfTcly1fq233so2qgj312i11e3yw.jpg)

> JDK1.8 对于HashMap 底层存储结构优化在于：当链表新增节点导致链表长度超过8的时候，就会将原有的链表转为红黑树来存储数据。


## 关于 HashMap 源码中提到的几个重要概念

关于 HashMap 源码中分析的文章一般都会提及几个重要的概念：

### 重要参数

1. **哈希桶（buckets）**：在 HashMap 的注释里使用哈希桶来形象的表示数组中每个地址位置。注意这里并不是数组本身，数组是装哈希桶的，他可以被称为**哈希表**。

2. **初始容量(initial capacity)** : 这个很容易理解，就是哈希表中哈希桶初始的数量。如果我们没有通过构造方法修改这个容量值默认为` DEFAULT_INITIAL_CAPACITY = 1<<4` 即16。值得注意的是为了保证 HashMap 添加和查找的高效性，`HashMap` 的容量总是  2^n 的形式。

3. **加载因子(load factor)**：加载因子是哈希表（散列表）在其容量自动增加之前被允许获得的最大数量的度量。当哈希表中的条目数量超过负载因子和当前容量的乘积时，散列表就会被重新映射（即重建内部数据结构），重新创建的散列表容量大约是之前散列表哈系统桶数量的两倍。默认加载因子（0.75）在时间和空间成本之间提供了良好的折衷。加载因子过大会导致很容易链表过长，**加载因子很小又容易导致频繁的扩容。所以不要轻易试着去改变这个默认值**。

4. **扩容阈值（threshold）**：其实在说加载因子的时候已经提到了扩容阈值了，**扩容阈值 = 哈希表容量 * 加载因子**。哈希表的键值对总数 = 所有哈希桶中所有链表节点数的加和，扩容阈值比较的是是键值对的个数而不是哈希表的数组中有多少个位置被占了。

4. **树化阀值(TREEIFY_THRESHOLD)** ：这个参数概念是在 JDK1.8后加入的，它的含义代表一个哈希桶中的节点个数大于该值（默认为8）的时候将会被转为红黑树行存储结构。

5. **非树化阀值(UNTREEIFY_THRESHOLD)**： 与树化阈值相对应，表示当一个已经转化为数形存储结构的哈希桶中节点数量小于该值（默认为 6）的时候将再次改为单链表的格式存储。导致这种操作的原因可能有删除节点或者扩容。

6. **最小树化容量(MIN_TREEIFY_CAPACITY)**: 经过上边的介绍我们只知道，当链表的节点数超过8的时候就会转化为树化存储，其实对于转化还有一个要求就是哈希表的数量超过最小树化容量的要求（默认要求是 64）,且为了避免进行扩容、树形化选择的冲突，这个值不能小于 4 * TREEIFY_THRESHOLD);在达到该有求之前优先选择扩容。扩容因为因为容量的变化可能会使单链表的长度改变。

与这个几个概念对应的在  HashMap 中几个常亮量，由于上边的介绍比较详细了，下边仅列出几个变量的声明：

```
/*默认初始容量*/
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

/*最大存储容量*/
static final int MAXIMUM_CAPACITY = 1 << 30;

/*默认加载因子*/
static final float DEFAULT_LOAD_FACTOR = 0.75f;

/*默认树化阈值*/
static final int TREEIFY_THRESHOLD = 8;

/*默认非树化阈值*/
static final int UNTREEIFY_THRESHOLD = 6;

/*默认最小树化容量*/
static final int MIN_TREEIFY_CAPACITY = 64;
```
对应的还有几个全局变量：


```
// 扩容阈值 = 容量 x 加载因子
int threshold;

//存储哈希桶的数组，哈希桶中装的是一个单链表或一颗红黑树，长度一定是 2^n
transient Node<K,V>[] table;  
  
// HashMap中存储的键值对的数量注意这里是键值对的个数而不是数组的长度
transient int size;
  
//所有键值对的Set集合 区分于 table 可以调用 entrySet(）得到该集合
transient Set<Map.Entry<K,V>> entrySet;
  
//操作数记录 为了多线程操作时 Fast-fail 机制
transient int modCount;

```

### 基本存储单元

HashMap 在 JDK 1.7 中只有 `Entry` 一种存储单元，而在 JDK1.8 中由于有了红黑树的存在，就多了一种存储单元，而 `Entry` 也随之应景的改为名为 Node。我们先来看下单链表节点的表示方法 ：

```
/**
 * 内部类 Node 实现基类的内部接口 Map.Entry<K,V>
 * 
 */
static class Node<K,V> implements Map.Entry<K,V> {
   //此值是在数组索引位置
   final int hash;
   //节点的键
   final K key;
   //节点的值
   V value;
   //单链表中下一个节点
   Node<K,V> next;
    
   Node(int hash, K key, V value, Node<K,V> next) {
       this.hash = hash;
       this.key = key;
       this.value = value;
       this.next = next;
   }

   public final K getKey()        { return key; }
   public final V getValue()      { return value; }
   public final String toString() { return key + "=" + value; }
    //节点的 hashCode 值通过 key 的哈希值和 value 的哈希值异或得到，没发现在源码中中有用到。
   public final int hashCode() {
       return Objects.hashCode(key) ^ Objects.hashCode(value);
   }

   //更新相同 key 对应的 Value 值
   public final V setValue(V newValue) {
       V oldValue = value;
       value = newValue;
       return oldValue;
   }
 //equals 方法，键值同时相同才节点才相同
   public final boolean equals(Object o) {
       if (o == this)
           return true;
       if (o instanceof Map.Entry) {
           Map.Entry<?,?> e = (Map.Entry<?,?>)o;
           if (Objects.equals(key, e.getKey()) &&
               Objects.equals(value, e.getValue()))
               return true;
       }
       return false;
   }
}
```

对于JDK1.8 新增的红黑树节点，这里不做展开叙述，有兴趣的朋友可以查看 [HashMap 在 JDK 1.8 后新增的红黑树结构](https://blog.csdn.net/u011240877/article/details/53358305)这篇文章来了解一下 JDK1.8对于红黑树的操作。

```
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
   TreeNode<K,V> parent;  // red-black tree links
   TreeNode<K,V> left;
   TreeNode<K,V> right;
   TreeNode<K,V> prev;    // needed to unlink next upon deletion
   boolean red;
   TreeNode(int hash, K key, V val, Node<K,V> next) {
       super(hash, key, val, next);
   }
   ·········
}
```

## HashMap 构造方法

`HashMap` 构造方法一共有三个：

- 可以指定期望初始容量和加载因子的构造函数，有了这两个值，我们就可以算出上边说到的 `threshold` 加载因子。其中加载因子不可以小于0，并没有规定不可以大于 1，但是不能等于无穷.

 > 大家可能疑惑 `Float.isNaN()` 其实  NaN 就是 not a number 的缩写，我们知道在运算 1/0 的时候回抛出异常，但是如果我们的除数指定为浮点数 1/0.0f 的时候就不会抛出异常了。计算器运算出的结果可以当做一个极值也就是无穷大，无穷大不是个数所以 1/0.0f 返回结果是 Infinity 无穷,使用 Float.isNaN(）判断将会返回 true。

```
 public HashMap(int initialCapacity, float loadFactor) {
    // 指定期望初始容量小于0将会抛出非法参数异常
   if (initialCapacity < 0)
       throw new IllegalArgumentException("Illegal initial capacity: " +
                                          initialCapacity);
   // 期望初始容量不可以大于最大值 2^30  实际上我们也不会用到这么大的容量                                      
   if (initialCapacity > MAXIMUM_CAPACITY)
       initialCapacity = MAXIMUM_CAPACITY;
  // 加载因子必须大于0 不能为无穷大   
   if (loadFactor <= 0 || Float.isNaN(loadFactor))
       throw new IllegalArgumentException("Illegal load factor: " +
                                          loadFactor);
   this.loadFactor = loadFactor;//初始化全局加载因子变量
   this.threshold = tableSizeFor(initialCapacity);//根据初始容量计算计算扩容阈值
}
```

咦？不是说好扩容阈值 = 哈希表容量 * 加载因子么？为什么还要用到下边这个方法呢？我们之前说了参数 `initialCapacity` 只是期望容量，不知道大家发现没我们这个构造函数并没有初始化 `Node<K,V>[] table` ，事实上真正指定哈希表容量总是在第一次添加元素的时候，这点和 ArrayList 的机制有所不同。等我们说到扩容机制的时候我们就可以看到相关代码了。



```
//根据期望容量返回一个 >= cap 的扩容阈值，并且这个阈值一定是 2^n 
static final int tableSizeFor(int cap) {
   int n = cap - 1;
   n |= n >>> 1;
   n |= n >>> 2;
   n |= n >>> 4;
   n |= n >>> 8;
   n |= n >>> 16;
   //经过上述面的 或和位移 运算， n 最终各位都是1 
   //最终结果 +1 也就保证了返回的肯定是 2^n 
   return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

- 只指定初始容量的构造函数

这个就比较简单了，将指定的期望初容量和默认加载因子传递给两个参数构造方法。这里就不在赘述。
 
```
public HashMap(int initialCapacity) {
   this(initialCapacity, DEFAULT_LOAD_FACTOR);
}
```

- 无参数构造函数
 
 这也是我们最常用的一个构造函数，该方法初始化了加载因子为默认值，并没有调动其他的构造方法，跟我们之前说的一样，哈希表的大小以及其他参数都会在第一调用扩容函数的初始化为默认值。
 
```
public HashMap() {
   this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}
```

- 传入一个 Map 集合的构造参数

该方法解释起来就比较麻烦了，因为他在初始化的时候就涉及了添加元素，扩容这两大重要的方法。这里先把它挂起来，紧接着我们讲完了扩容机制再回来看就好了。

```
public HashMap(Map<? extends K, ? extends V> m) {
   this.loadFactor = DEFAULT_LOAD_FACTOR;
   putMapEntries(m, false);
}
```

## HashMap 如何确定添加元素的位置

在分析 `HashMap` 添加元素的方法之前，我们需要先来了解下，如何确定元素在 `HashMap` 中的位置的。我们知道 `HashMap` 底层是哈希表，哈希表依靠 hash 值去确定元素存储位置。`HashMap` 在 JDK 1.7 和 JDK1.8中采用的 hash 方法并不是完全相同。我们现在看下 

### JDK 1.7 中 hash 方法的实现：

这里提出一个概念扰动函数，我们知道Map 文中存放键值对的位置有键的 hash 值决定，但是键的 hashCode 函数返回值不一定满足，哈希表长度的要求，所以在存储元素之前需要对 key 的 hash 值进行一步扰动处理。下面我们JDK1.7 中的扰动函数：

```
//4次位运算 + 5次异或运算 
//这种算法可以防止低位不变，高位变化时，造成的 hash 冲突
static final int hash(Object k) {
   int h = 0;
   h ^= k.hashCode(); 
   h ^= (h >>> 20) ^ (h >>> 12);
   return h ^ (h >>> 7) ^ (h >>> 4);
}
```

### JDK1.8 中 hash 函数的实现

JDK1.8中再次优化了这个哈希函数，把 key 的 hashCode 方法返回值右移16位，即丢弃低16位，高16位全为0 ，然后在于 hashCode 返回值做异或运算，即高 16 位与低 16 位进行异或运算，这么做可以在数组 table 的 length 比较小的时候，也能保证考虑到高低Bit都参与到 hash 的计算中，同时不会有太大的开销，扰动处理次数也从 4次位运算 + 5次异或运算 降低到 1次位运算 + 1次异或运算

```
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
 
进过上述的扰动函数只是得到了合适的 hash 值，但是还没有确定在 Node[] 数组中的角标，在 JDK1.7中存在一个函数，JDK1.8中虽然没有但是只是把这步运算放到了 put 函数中。我们就看下这个函数实现：
 
 
```
static int indexFor(int h, int length) {
     return h & (length-1);  // 取模运算
}
```
为了让 hash 值能够对应到现有数组中的位置，我们上篇文章讲到一个方法为 取模运算，即 `hash %  length`，得到结果作为角标位置。但是 HashMap 就厉害了，连这一步取模运算的都优化了。我们需要知道一个计算机对于2进制的运算是要快于10进制的，取模算是10进制的运算了，而位与运算就要更高效一些了。

我们知道 `HashMap` 底层数组的长度总是 2^n ,转为二进制总是 1000 即1后边多个0的情况。此时一个数与 2^n 取模，等价于 一个数与 2^n - 1做位与运算。而 JDK  中就使用` h & (length-1)` 运算替代了对 length取模。我们根据图片来看一个具体的例子：

![](https://ws2.sinaimg.cn/large/006tNbRwly1fq31tmt8bmj30xa0j0755.jpg)

图片来自：https://tech.meituan.com/java-hashmap.html 侵删。



### 小结

通过上边的分析我们可以到如下结论：

- **在存储元素之前，HashMap 会对 key 的 hashCode 返回值做进一步扰动函数处理，1.7 中扰动函数使用了 4次位运算 + 5次异或运算，1.8 中降低到 1次位运算 + 1次异或运算**
- **扰动处理后的 hash 与 哈希表数组length -1 做位与运算得到最终元素储存的哈希桶角标位置。**


## HashMap 的添加元素

敲黑板了，重点来了。对于理解 HashMap 源码一方面要了解存储的数据结构，另一方面也要了解具体是如何添加元素的。下面我们就来看下 `put(K key, V value)` 函数。

```
// 可以看到具体的添加行为在 putVal 方法中进行
public V put(K key, V value) {
   return putVal(hash(key), key, value, false, true);
}
```

对于 putVal 前三个参数很好理解，第4个参数 onlyIfAbsent 表示只有当对应 key 的位置为空的时候替换元素，一般传 false，在 JDK1.8中新增方法 `public V putIfAbsent(K key, V value)` 传 true，第 5 个参数 evict 如果是 false。那么表示是在初始化时调用的:



```
 final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
              boolean evict) {
              
   Node<K,V>[] tab; Node<K,V> p; int n, i;
   //如果是第一添加元素 table = null 则需要扩容
   if ((tab = table) == null || (n = tab.length) == 0)
       n = (tab = resize()).length;// n 表示扩容后数组的长度
   //  i = (n - 1) & hash 即上边讲得元素存储在 map 中的数组角标计算
   // 如果对应数组没有元素没发生 hash 碰撞 则直接赋值给数组中 index 位置   
   if ((p = tab[i = (n - 1) & hash]) == null)
       tab[i] = newNode(hash, key, value, null);
   else {// 发生 hash 碰撞了
       Node<K,V> e; K k;
        //如果对应位置有已经有元素了 且 key 是相同的则覆盖元素
       if (p.hash == hash &&
           ((k = p.key) == key || (key != null && key.equals(k))))
           e = p;
       else if (p instanceof TreeNode)//如果添加当前节点已经为红黑树，则需要转为红黑树中的节点
           e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
       else {// hash 值计算出的数组索引相同，但 key 并不同的时候，        // 循环整个单链表
           for (int binCount = 0; ; ++binCount) {
               if ((e = p.next) == null) {//遍历到尾部
                    // 创建新的节点，拼接到链表尾部
                   p.next = newNode(hash, key, value, null);             // 如果添加后 bitCount 大于等于树化阈值后进行哈希桶树化操作
                   if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                       treeifyBin(tab, hash);
                   break;
               }
               //如果遍历过程中找到链表中有个节点的 key 与 当前要插入元素的 key 相同，此时 e 所指的节点为需要替换 Value 的节点，并结束循环
               if (e.hash == hash &&
                   ((k = e.key) == key || (key != null && key.equals(k))))
                   break;
               //移动指针    
               p = e;
           }
       }
       //如果循环完后 e!=null 代表需要替换e所指节点 Value
       if (e != null) { // existing mapping for key
           V oldValue = e.value//保存原来的 Value 作为返回值
           // onlyIfAbsent 一般为 false 所以替换原来的 Value
           if (!onlyIfAbsent || oldValue == null)
               e.value = value;
            //这个方法在 HashMap 中是空实现，在 LinkedHashMap 中有关系   
           afterNodeAccess(e);
           return oldValue;
       }
   }
   //操作数增加
   ++modCount;
   //如果 size 大于扩容阈值则表示需要扩容
   if (++size > threshold)
       resize();
   afterNodeInsertion(evict);
   return null;
}
```

由于添加元素中设计逻辑有点复杂，这里引用一张图来说明，理解

> 图片来来自：https://tech.meituan.com/java-hashmap.html

![](https://ws4.sinaimg.cn/large/006tNbRwly1fq33jca8bdj31bo11sdhx.jpg)

添加元素过程：

1. 如果 `Node[] table` 表为 null ,则表示是第一次添加元素，讲构造函数也提到了，及时构造函数指定了期望初始容量，在第一次添加元素的时候也为空。这时候需要进行首次扩容过程。
2. 计算对应的键值对在 table 表中的索引位置，通过` i = (n - 1) & hash ` 获得。
3. 判断索引位置是否有元素如果没有元素则直接插入到数组中。如果有元素且key 相同，则覆盖 value 值，这里判断是用的 equals 这就表示要正确的存储元素，就必须按照业务要求覆写 key 的 equals 方法，上篇文章我们也提及到了该方法重要性。
4. 如果索引位置的 key 不相同，则需要遍历单链表，如果遍历过如果有与 key 相同的节点，则保存索引，替换 Value；如果没有相同节点，则在但单链表尾部插入新节点。这里操作与1.7不同，1.7新来的节点总是在数组索引位置，而之前的元素作为下个节点拼接到新节点尾部。
5. 如果插入节点后链表的长度大于树化阈值，则需要将单链表转为红黑树。
6. 成功插入节点后，判断键值对个数是否大于扩容阈值，如果大于了则需要再次扩容。至此整个插入元素过程结束。


## HashMap 的扩容过程

在上边说明 HashMap 的 putVal 方法时候，多次提到了扩容函数，扩容函数也是我们理解 HashMap 源码的重中之重。所以再次敲黑板~


```
final Node<K,V>[] resize() {
   // oldTab 指向旧的 table 表
   Node<K,V>[] oldTab = table;
   // oldCap 代表扩容前 table 表的数组长度，oldTab 第一次添加元素的时候为 null 
   int oldCap = (oldTab == null) ? 0 : oldTab.length;
   // 旧的扩容阈值
   int oldThr = threshold;
   // 初始化新的阈值和容量
   int newCap, newThr = 0;
   // 如果 oldCap > 0 则会将新容量扩大到原来的2倍，扩容阈值也将扩大到原来阈值的两倍
   if (oldCap > 0) {
       // 如果旧的容量已经达到最大容量 2^30 那么就不在继续扩容直接返回，将扩容阈值设置到 Integer.MAX_VALUE，并不代表不能装新元素，只是数组长度将不会变化
       if (oldCap >= MAXIMUM_CAPACITY) {
           threshold = Integer.MAX_VALUE;
           return oldTab;
       }//新容量扩大到原来的2倍，扩容阈值也将扩大到原来阈值的两倍
       else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                oldCap >= DEFAULT_INITIAL_CAPACITY)
           newThr = oldThr << 1; // double threshold
   }
   //oldThr 不为空，代表我们使用带参数的构造方法指定了加载因子并计算了
   //初始初始阈值 会将扩容阈值 赋值给初始容量这里不再是期望容量，
   //但是 >= 指定的期望容量
   else if (oldThr > 0) // initial capacity was placed in threshold
       newCap = oldThr;
   else {
        // 空参数构造会走这里初始化容量，和扩容阈值 分别是 16 和 12
       newCap = DEFAULT_INITIAL_CAPACITY;
       newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
   }
   //如果新的扩容阈值是0，对应的是当前 table 为空，但是有阈值的情况
   if (newThr == 0) {
        //计算新的扩容阈值
       float ft = (float)newCap * loadFactor;
       // 如果新的容量不大于 2^30 且 ft 不大于 2^30 的时候赋值给 newThr 
       //否则 使用 Integer.MAX_VALUE
       newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                 (int)ft : Integer.MAX_VALUE);
   }
   //更新全局扩容阈值
   threshold = newThr;
   @SuppressWarnings({"rawtypes","unchecked"})
    //使用新的容量创建新的哈希表的数组
   Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
   table = newTab;
   //如果老的数组不为空将进行重新插入操作否则直接返回
   if (oldTab != null) {
        //遍历老数组中每个位置的链表或者红黑树重新计算节点位置，插入新数组
       for (int j = 0; j < oldCap; ++j) {
           Node<K,V> e;//用来存储对应数组位置链表头节点
           //如果当前数组位置存在元素
           if ((e = oldTab[j]) != null) {
                // 释放原来数组中的对应的空间
               oldTab[j] = null;
               // 如果链表只有一个节点，
               //则使用新的数组长度计算节点位于新数组中的角标并插入
               if (e.next == null)
                   newTab[e.hash & (newCap - 1)] = e;
               else if (e instanceof TreeNode)//如果当前节点为红黑树则需要进一步确定树中节点位于新数组中的位置。
                   ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
               else { // preserve order
                   //因为扩容是容量翻倍，
                   //原链表上的每个节点 现在可能存放在原来的下标，即low位，
                   //或者扩容后的下标，即high位
              //低位链表的头结点、尾节点
              Node<K,V> loHead = null, loTail = null;
              //高位链表的头节点、尾节点
              Node<K,V> hiHead = null, hiTail = null;
              Node<K,V> next;//用来存放原链表中的节点
              do {
                  next = e.next;
                  // 利用哈希值 & 旧的容量，可以得到哈希值去模后，
                  //是大于等于 oldCap 还是小于 oldCap，
                  //等于 0 代表小于 oldCap，应该存放在低位，
                  //否则存放在高位（稍后有图片说明）
                  if ((e.hash & oldCap) == 0) {
                      //给头尾节点指针赋值
                      if (loTail == null)
                          loHead = e;
                      else
                          loTail.next = e;
                      loTail = e;
                  }//高位也是相同的逻辑
                  else {
                      if (hiTail == null)
                          hiHead = e;
                      else
                          hiTail.next = e;
                      hiTail = e;
                  }//循环直到链表结束
              } while ((e = next) != null);
              //将低位链表存放在原index处，
              if (loTail != null) {
                  loTail.next = null;
                  newTab[j] = loHead;
              }
              //将高位链表存放在新index处
              if (hiTail != null) {
                  hiTail.next = null;
                  newTab[j + oldCap] = hiHead;
              }
           }
       }
   }
   return newTab;
}
```

相信大家看到扩容的整个函数后对扩容机制应该有所了解了，整体分为两部分：**1. 寻找扩容后数组的大小以及新的扩容阈值，2. 将原有哈希表拷贝到新的哈希表中**。

第一部分没的说，但是第二部分我看的有点懵逼了，但是踩在巨人的肩膀上总是比较容易的，美团的大佬们早就写过一些有关 HashMap 的源码分析文章，给了我很大的帮助。在文章的最后我会放出参考链接。下面说下我的理解：

JDK 1.8 不像 JDK1.7中会重新计算每个节点在新哈希表中的位置，而是通过 `(e.hash & oldCap) == 0`是否等于0 就可以得出原来链表中的节点在新哈希表的位置。为什么可以这样高效的得出新位置呢？

因为扩容是容量翻倍，所以原链表上的每个节点，可能存放新哈希表中在原来的下标位置， 或者扩容后的原位置偏移量为 oldCap 的位置上，下边举个例子 图片和叙述来自 https://tech.meituan.com/java-hashmap.html：

> 图（a）表示扩容前的key1和key2两种key确定索引位置的示例，图（b）表示扩容后key1和key2两种key确定索引位置的示例，其中hash1是key1对应的哈希与高位运算结果。

![](https://ws4.sinaimg.cn/large/006tNbRwly1fq35v8brf0j319c0ceweu.jpg)

> 元素在重新计算hash之后，因为n变为2倍，那么n-1的mask范围在高位多1bit(红色)，因此新的index就会发生这样的变化：

![](https://ws3.sinaimg.cn/large/006tNbRwly1fq35xbpzi0j30tk05mjrn.jpg)

> 所以在 JDK1.8 中扩容后，只需要看看原来的hash值新增的那个bit是1还是0就好了，是0的话索引没变，是1的话索引变成“原索引+oldCap


另外还需要注意的一点是 HashMap 在 1.7的时候扩容后，链表的节点顺序会倒置，1.8则不会出现这种情况。


## HashMap 其他添加元素的方法

上边将构造函数的时候埋了个坑即使用：

```
public HashMap(Map<? extends K, ? extends V> m) {
   this.loadFactor = DEFAULT_LOAD_FACTOR;
   putMapEntries(m, false);
}
```
构造函数构建 HashMap 的时候，在这个方法里，除了赋值了默认的加载因子，并没有调用其他构造方法，而是通过批量添加元素的方法 `putMapEntries` 来构造了 HashMap。该方法为私有方法，真正批量添加的方法为` putAll`


```
public void putAll(Map<? extends K, ? extends V> m) {
   putMapEntries(m, true);
}
```

```
//同样第二参数代表是否初次创建 table 
 final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
   int s = m.size();
   if (s > 0) {
        //如果哈希表为空则初始化参数扩容阈值
       if (table == null) { // pre-size
           float ft = ((float)s / loadFactor) + 1.0F;
           int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                    (int)ft : MAXIMUM_CAPACITY);
           if (t > threshold)
               threshold = tableSizeFor(t);
       }
       else if (s > threshold)//构造方法没有计算 threshold 默认为0 所以会走扩容函数
           resize();
        //将参数中的 map 键值对一次添加到 HashMap 中
       for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
           K key = e.getKey();
           V value = e.getValue();
           putVal(hash(key), key, value, false, evict);
       }
   }
}
```

JDK1.8 中还新增了一个添加方法，该方法调用 putVal 且第4个参数传了 true，代表只有哈希表中对应的key 的位置上元素为空的时候添加成功，否则返回原来 key 对应的 Value 值。

```
@Override
public V putIfAbsent(K key, V value) {
   return putVal(hash(key), key, value, true, true);
}
```

## HashMap 查询元素

分析了完了 put 函数后，接下来让我们看下 get 函数，当然有 put 函数计算键值对在哈希表中位置的索引方法分析的铺垫后，get 方法就显得很容容易了。

1. 根据键值对的 key 去获取对应的 Value

```
public V get(Object key) {
   Node<K,V> e;
   //通过 getNode寻找 key 对应的 Value 如果没找到，或者找到的结果为 null 就会返回null 否则会返回对应的 Value
   return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
   Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
   //现根据 key 的 hash 值去找到对应的链表或者红黑树
   if ((tab = table) != null && (n = tab.length) > 0 &&
       (first = tab[(n - 1) & hash]) != null) {
       // 如果第一个节点就是那么直接返回
       if (first.hash == hash && // always check first node
           ((k = first.key) == key || (key != null && key.equals(k))))
           return first;
        //如果 对应的位置为红黑树调用红黑树的方法去寻找节点   
       if ((e = first.next) != null) {
           if (first instanceof TreeNode)
               return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            //遍历单链表找到对应的 key 和 Value   
           do {
               if (e.hash == hash &&
                   ((k = e.key) == key || (key != null && key.equals(k))))
                   return e;
           } while ((e = e.next) != null);
       }
   }
   return null;
}
```

2. JDK 1.8新增 get 方法，在寻找 key 对应 Value 的时候如果没找大则返回指定默认值

``` 
@Override
public V getOrDefault(Object key, V defaultValue) {
   Node<K,V> e;
   return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
}
```

## HashMap 的删操作

`HashMap` 没有 `set` 方法，如果想要修改对应 key 映射的 Value ，只需要再次调用 `put` 方法就可以了。我们来看下如何移除 `HashMap` 中对应的节点的方法：


```
 public V remove(Object key) {
   Node<K,V> e;
   return (e = removeNode(hash(key), key, null, false, true)) == null ?
       null : e.value;
}
```

```
@Override
public boolean remove(Object key, Object value) {
   //这里传入了value 同时matchValue为true
   return removeNode(hash(key), key, value, true, true) != null;
}
```

这里有两个参数需要我们提起注意：

- matchValue 如果这个值为 true 则表示只有当 Value 与第三个参数 Value 相同的时候才删除对一个的节点
- movable 这个参数在红黑树中先删除节点时候使用 true 表示删除并其他数中的节点。

```
 final Node<K,V> removeNode(int hash, Object key, Object value,
                               boolean matchValue, boolean movable) {
   Node<K,V>[] tab; Node<K,V> p; int n, index;
   //判断哈希表是否为空，长度是否大于0 对应的位置上是否有元素
   if ((tab = table) != null && (n = tab.length) > 0 &&
       (p = tab[index = (n - 1) & hash]) != null) {
       
       // node 用来存放要移除的节点， e 表示下个节点 k ，v 每个节点的键值
       Node<K,V> node = null, e; K k; V v;
       //如果第一个节点就是我们要找的直接赋值给 node
       if (p.hash == hash &&
           ((k = p.key) == key || (key != null && key.equals(k))))
           node = p;
       else if ((e = p.next) != null) {
            // 遍历红黑树找到对应的节点
           if (p instanceof TreeNode)
               node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
           else {
                //遍历对应的链表找到对应的节点
               do {
                   if (e.hash == hash &&
                       ((k = e.key) == key ||
                        (key != null && key.equals(k)))) {
                       node = e;
                       break;
                   }
                   p = e;
               } while ((e = e.next) != null);
           }
       }
       // 如果找到了节点
       // !matchValue 是否不删除节点
       // (v = node.value) == value ||
                            (value != null && value.equals(v))) 节点值是否相同，
       if (node != null && (!matchValue || (v = node.value) == value ||
                            (value != null && value.equals(v)))) {
           //删除节点                 
           if (node instanceof TreeNode)
               ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
           else if (node == p)
               tab[index] = node.next;
           else
               p.next = node.next;
           ++modCount;
           --size;
           afterNodeRemoval(node);
           return node;
       }
   }
   return null;
}
```

## HashMap 的迭代器
我们都只我们知道 Map 和 Set 有多重迭代方式，对于 Map 遍历方式这里不展开说了，因为我们要分析迭代器的源码所以这里就给出一个使用迭代器遍历的方法：


```
public void test(){

    Map<String, Integer> map = new HashMap<>();
    
    ...
    
    Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
    
    //通过迭代器：先获得 key-value 对（Entry）的Iterator，再循环遍历   
    Iterator iter1 = entrySet.iterator();
    while (iter1.hasNext()) {
    // 遍历时，需先获取entry，再分别获取key、value
    Map.Entry entry = (Map.Entry) iter1.next();
    System.out.print((String) entry.getKey());
    System.out.println((Integer) entry.getValue());
    }
}
```
通过上述遍历过程我们可以使用 `map.entrySet()` 获取之前我们最初提及的 `entrySet`

```
public Set<Map.Entry<K,V>> entrySet() {
   Set<Map.Entry<K,V>> es;
   return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
}
```


```
// 我们来看下 EntrySet 是一个 set 存储的元素是 Map 的键值对
final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
   // size 放回 Map 中键值对个数
   public final int size()                 { return size; }
   //清除键值对
   public final void clear()               { HashMap.this.clear(); }
   // 获取迭代器
   public final Iterator<Map.Entry<K,V>> iterator() {
       return new EntryIterator();
   }
   
   //通过 getNode 方法获取对一个及对应 key 对应的节点 这里必须传入
   // Map.Entry 键值对类型的对象 否则直接返回 false
   public final boolean contains(Object o) {
       if (!(o instanceof Map.Entry))
           return false;
       Map.Entry<?,?> e = (Map.Entry<?,?>) o;
       Object key = e.getKey();
       Node<K,V> candidate = getNode(hash(key), key);
       return candidate != null && candidate.equals(e);
   }
   // 滴啊用之前讲得 removeNode 方法 删除节点
   public final boolean remove(Object o) {
       if (o instanceof Map.Entry) {
           Map.Entry<?,?> e = (Map.Entry<?,?>) o;
           Object key = e.getKey();
           Object value = e.getValue();
           return removeNode(hash(key), key, value, true, true) != null;
       }
       return false;
   }
   ...
}
```


```
//EntryIterator 继承自 HashIterator
final class EntryIterator extends HashIterator
   implements Iterator<Map.Entry<K,V>> {
   // 这里可能是因为大家使用适配器的习惯添加了这个 next 方法
   public final Map.Entry<K,V> next() { return nextNode(); }
}

   
abstract class HashIterator {
        Node<K,V> next;        // next entry to return
        Node<K,V> current;     // current entry
        int expectedModCount;  // for fast-fail
        int index;             // current slot

        HashIterator() {
            //初始化操作数 Fast-fail 
            expectedModCount = modCount;
            // 将 Map 中的哈希表赋值给 t
            Node<K,V>[] t = table;
            current = next = null;
            index = 0;
            //从table 第一个不为空的 index 开始获取 entry
            if (t != null && size > 0) { // advance to first entry
                do {} while (index < t.length && (next = t[index++]) == null);
            }
        }
        
        public final boolean hasNext() {
            return next != null;
        }

        final Node<K,V> nextNode() {
            Node<K,V>[] t;
            Node<K,V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
             //如果当前链表节点遍历完了，则取哈希桶下一个不为null的链表头   
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {} while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }
        //这里还是调用 removeNode 函数不在赘述
        public final void remove() {
            Node<K,V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
            expectedModCount = modCount;
        }
    }
```

除了 `EntryIterator` 以外还有 `KeyIterator` 和 `ValueIterator` 也都继承了`HashIterator` 也代表了 HashMap 的三种不同的迭代器遍历方式。


```

final class KeyIterator extends HashIterator
   implements Iterator<K> {
   public final K next() { return nextNode().key; }
}

final class ValueIterator extends HashIterator
   implements Iterator<V> {
   public final V next() { return nextNode().value; }
}
```

可以看出无论哪种迭代器都是通过，遍历 table 表来获取下个节点，来遍历的，遍历过程可以理解为一种深度优先遍历，即优先遍历链表节点（或者红黑树），然后在遍历其他数组位置。


## HashTable 的区别
面试的时候面试官总是问完 HashMap 后会问 HashTable 其实 HashTable 也算是比较古老的类了。翻看 HashTable 的源码可以发现有如下区别：

1. `HashMap` 是线程不安全的，HashTable是线程安全的。
2. `HashMap` 允许 key 和 Vale 是 null，但是只允许一个 key 为 null,且这个元素存放在哈希表 0 角标位置。 `HashTable` 不允许key、value 是 null
3. `HashMap` 内部使用`hash(Object key)`扰动函数对 key 的 `hashCode` 进行扰动后作为 `hash` 值。`HashTable` 是直接使用 key 的 `hashCode()` 返回值作为 hash 值。

4. `HashMap`默认容量为 2^4 且容量一定是 2^n ; `HashTable` 默认容量是11,不一定是 2^n

5. `HashTable` 取哈希桶下标是直接用模运算,扩容时新容量是原来的2倍+1。`HashMap` 在扩容的时候是原来的两倍，且哈希桶的下标使用 &运算代替了取模。


## 参考
- JDK 1.7 & 1.8 HashMap & HashTable 源码
- 美团技术团队博客 ：[Java 8系列之重新认识HashMap](https://tech.meituan.com/java-hashmap.html)
- 美团大佬张旭童 ：[面试必备：HashMap源码解析（JDK8）](https://juejin.im/post/599652796fb9a0249975a318#heading-13)
- 张拭心 CSDN 博客 [Java 集合深入理解（16）：HashMap 主要特点和关键方法源码解读](https://blog.csdn.net/u011240877/article/details/53351188)
-  Carson_Ho CSDN 博客 [Java源码分析：关于 HashMap 1.8 的重大更新](https://blog.csdn.net/carson_ho/article/details/79373134)
-  [HashMap 源码详细分析(JDK1.8)](https://segmentfault.com/a/1190000012926722)
-  [集合番@HashMap一文通（1.7版）](https://www.zybuluo.com/kiraSally/note/819843)

## 最后

写 HashMap 源码分析的过程，可以说比 `ArrayList` 或者`LinkedList`源码简直不是一个级别的。个人能力有限，所以在学习的过程中，参考了很多前辈们的分析，也学到了很多东西。这很有用，经过这一波分析我觉得我对面试中的的 HashMap 面试题回答要比以前强很多。对于 HashMap的相关面试题[集合番@HashMap一文通（1.7版）](https://www.zybuluo.com/kiraSally/note/819843) 这篇文章末尾较全面的总结。另外 HashMap 的多线程会导致循环链表的情况，大家可以参考[Java 8系列之重新认识HashMap](https://tech.meituan.com/java-hashmap.html) 写的非常好。大家可以原博客去查看。



