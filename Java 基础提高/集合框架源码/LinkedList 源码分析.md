# LinkedList 源码分析

![](https://ws3.sinaimg.cn/large/006tNbRwly1fpwlldho9yj30k00fd76r.jpg)


由于最近工作有点忙，进行了 APP 的部分优化，期间也学习了很多有关于布局优化和其他性能优化的知识，但是仍然觉得不太成体系，期待能有更多的优质的性能优化实战文章能够涌现出来，以便于大家一起交流学习。

周末有时间把手头的工作放一放，来继续进行 Java 集合源码的学习。今天来学习下 「LinkedList」的源码。

1. LinkedList 的概述
2. LinkedList 的构造方法
3. LinkedList 的增删改查。
4. LinkedList 作为队列（Queue）使用的时候增删改查。
5. LinkedList 的遍历方法


## LinkedList 的概述

先来看下 LinkedList 的继承体系图，这里悄悄告诉大家一个方法在学习源码的时候如何查看一个类的继承体系的方法，第一步打开 IntelliJ IDEA 找到你要查看的类 ，第二步点击右键，选择 Diagrams 选择二级菜单的任意一项，就可以得到下面这样一个体系图，还有好多方便的操作，大家可以通过这篇文章来了解下 [使用IntelliJ IDEA查看类的继承关系图形](http://www.cnblogs.com/deng-cc/p/6927447.html) 。


![](https://ws3.sinaimg.cn/large/006tNbRwly1fpwa6r4nnhj30yk0q6jru.jpg)

> 图中蓝色实线箭头是指继承关系 ，绿色虚线箭头是指接口实现关系。

1. `LinkedList` 继承自 `AbstrackSequentialList` 并实现了 `List` 接口以及 `Deque` 双向队列接口，因此 LinkedList 不但拥有 List 相关的操作方法，也有队列的相关操作方法。

2. `LinkedList` 和 `ArrayList` 一样实现了序列化接口 `Serializable` 和 `Cloneable` 接口使其拥有了序列化和克隆的特性。

`LinkedList` 一些主要特性：

1. **`LinkedList` 集合底层实现的数据结构为双向链表**
2. **`LinkedList` 集合中元素允许为 null**
3. **`LinkedList` 允许存入重复的数据**
4. **`LinkedList` 中元素存放顺序为存入顺序。**
5. **`LinkedList` 是非线程安全的，如果想保证线程安全的前提下操作 `LinkedList`，可以使用 `List list = Collections.synchronizedList(new LinkedList(...));` 来生成一个线程安全的 `LinkedList`**


链表是一种不同于数组的数据结构，双向链表是链表的一种子数据结构，它具有以下的特点：

**每个节点上有三个字段：当前节点的数据字段（data）,指向上一个节点的字段（prev），和指向下一个节点的字段（next）。**

| LLink | Data | RLink  |
| --- | --- | --- |

## LinkedList 双向链表实现及成员变量

概述上说了双向链表的特点，而 `LinkedList` 又继承自 `Deque` 这个双链表接口，在介绍 `LinkedList` 的具体方法前我们先了解下双向链表的实现。

```
private static class Node<E> {
   // 当前节点的元素值
   E item;
   // 下一个节点的索引
   Node<E> next;
   // 上一个节点的索引
   Node<E> prev;

   Node(Node<E> prev, E element, Node<E> next) {
       this.item = element;
       this.next = next;
       this.prev = prev;
   }
}
```

正如我们所说，`LinkedList` 的节点实现完全符合双向链表的数据结构要求，而构造方法第一个参数为上一个节点的索引，当前节点的元素，下一个节点索引。

LinkedList 主要成员变量有下边三个：

```
//LinkedList 中的节点个数
transient int size = 0;

//LinkedList 链表的第一个节点
transient Node<E> first;

//LinkedList 链表的最后一个节点
transient Node<E> last;

```

之所以 LinkedList 要保存链表的第一个节点和最后一个节点是因为，我们都知道，链表数据结构相对于数组结构，有点在于增删，缺点在于查找。如果我们保存了LinkedList 的头尾两端，当我们需要以索引来查找节点的时候，我们可以根据 `index` 和 `size/2` 的大小,来决定从头查找还是从尾部查找，这也算是一定程度上弥补了单链表数据结构的缺点。

## LinkedList 的构造函数

LinkedList 有两个构造函数：


```
/**
 * 空参数的构造由于生成一个空链表 first = last = null
 */
 public LinkedList() {
 }

/**
 * 传入一个集合类，来构造一个具有一定元素的 LinkedList 集合
 * @param  c  其内部的元素将按顺序作为 LinkedList 节点
 * @throws NullPointerException 如果 参数 collection 为空将抛出空指针异常
 */
public LinkedList(Collection<? extends E> c) {
   this();
   addAll(c);
}
```

带参数的构造方法，调用 `addAll(c)` 这个方法，实际上这方法调用了 `addAll(size, c)` 方法，在外部单独调用时，将指定集合的元素作为节点，添加到 `LinkedList` 链表尾部：
而 `addAll(size, c)` 可以将集合元素插入到指定索引节点。

```
public boolean addAll(Collection<? extends E> c) {
    return addAll(size, c);
}
```

```
/**
 * 在 index 节点前插入包含所有 c 集合元素的节点。
 * 返回值表示是否成功添加了对应的元素.
 */
public boolean addAll(int index, Collection<? extends E> c) {
   // 查看索引是否满足 0 =< index =< size 的要求
   checkPositionIndex(index);
    // 调用对应 Collection 实现类的 toArray 方法将集合转为数组
   Object[] a = c.toArray();
   //检查数组长度，如果为 0 则直接返回 false 表示没有添加任何元素
   int numNew = a.length;
   if (numNew == 0)
       return false;
   // 保存 index 当前的节点为 succ，当前节点的上一个节点为 pred
   Node<E> pred, succ;
   // 如果 index = size 表示在链表尾部插入
   if (index == size) {
       succ = null;
       pred = last;
   } else {
       succ = node(index);
       pred = succ.prev;
   }
    
    // 遍历数组将对应的元素包装成节点添加到链表中
   for (Object o : a) {
       @SuppressWarnings("unchecked") E e = (E) o;
       Node<E> newNode = new Node<>(pred, e, null);
       //如果 pred 为空表示 LinkedList 集合中还没有元素
       //生成的第一个节点将作为头节点 赋值给 first 成员变量
       if (pred == null)
           first = newNode;
       else
           pred.next = newNode;
       pred = newNode;
   }
   // 如果 index 位置的元素为 null 则遍历数组后 pred 所指向的节点即为新链表的末节点，赋值给 last 成员变量
   if (succ == null) {
       last = pred;
   } else {
       // 否则将 pred 的 next 索引指向 succ ，succ 的 prev 索引指向 pred
       pred.next = succ;
       succ.prev = pred;
   }
   // 更新当前链表的长度 size 并返回 true 表示添加成功
   size += numNew;
   modCount++;
   return true;
}
```

经过上边的代码注释可以了解到，LinkedList 批量添加节点的方法实现了。大体分下面几个步骤：

1. 检查索引值是否合法，不合法将抛出角标越界异常
2. 保存 index 位置的节点，和 index-1 位置的节点，对于单链表熟悉的同学一定清楚对于链表的增删操作都需要两个指针变量完成，可参考：[搞懂单链表面试题](https://juejin.im/post/5aa299c1518825557b4c5806) 来深入理解下。
3. 将参数集合转化为数组，循环将数组中的元素封装为节点添加到链表中。
4. 更新链表长度并返回添加 true 表示添加成功。


对于 `checkPositionIndex`方法这里想顺带分析了，`LinkedList` 中有两个方法用于检查角标越界，内部实现一样，都是通过判断 ` index >= 0 && index < size ` 是否满足条件。

```
private String outOfBoundsMsg(int index) {
   return "Index: "+index+", Size: "+size;
}

private void checkElementIndex(int index) {
   if (!isElementIndex(index))
       throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

private void checkPositionIndex(int index) {
   if (!isPositionIndex(index))
       throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

/**
* Tells if the argument is the index of an existing element.
*/
private boolean isElementIndex(int index) {
   return index >= 0 && index < size;
}

/**
* Tells if the argument is the index of a valid position for an
* iterator or an add operation.
*/
private boolean isPositionIndex(int index) {
   return index >= 0 && index <= size;
}
```

## LinkedList 的增删改查

### LinkedList 添加节点的方法

LinkedList 作为链表数据结构的实现，不同于数组，它可以方便的在头尾插入一个节点，而 add 方法默认在链表尾部添加节点：


```
/**
 * Inserts the specified element at the beginning of this list.
 *
 * @param e the element to add
 */
 public void addFirst(E e) {
    linkFirst(e);
 }

/**
 * Appends the specified element to the end of this list.
 *
 * <p>This method is equivalent to {@link #add}.
 *
 * @param e the element to add
 */
 public void addLast(E e) {
    linkLast(e);
 }
    
/**
 * Appends the specified element to the end of this list.
 *
 * <p>This method is equivalent to {@link #addLast}.
 *
 * @param e element to be appended to this list
 * @return {@code true} (as specified by {@link Collection#add})
 */
 public boolean add(E e) {
    linkLast(e);
    return true;
 }
```
上述英文太过简单不翻译了，我们可以看到 add 方法是有返回值的，这个可以注意下。看来这一系方法都调用用了 `linkXXX` 方法，


```
 /**
  * 添加一个元素在链表的头节点位置
  */
private void linkFirst(E e) {
   // 添加元素之前的头节点
   final Node<E> f = first;
   //以添加的元素为节点值构建新的头节点 并将 next 指针指向 之前的头节点
   final Node<E> newNode = new Node<>(null, e, f);
   // first 索引指向将新的节点
   first = newNode;
   // 如果添加之前链表空则新的节点也作为未节点
   if (f == null)
       last = newNode;
   else
       f.prev = newNode;//否则之前头节点的 prev 指针指向新节点
   size++;
   modCount++;//操作数++
}

/**
 * 在链表末尾添加一个节点
 */
 void linkLast(E e) {
   final Node<E> l = last;//保存之前的未节点
   //构建新的未节点，并将新节点 prev 指针指向 之前的未节点
   final Node<E> newNode = new Node<>(l, e, null);
   //last 索引指向末节点
   last = newNode;
   if (l == null)//如果之前链表为空则新节点也作为头节点
       first = newNode;
   else//否则将之前的未节点的 next 指针指向新节点
       l.next = newNode;
   size++;
   modCount++;//操作数++
}
```

除了上述几种添加元素的方法，以及之前在将构造的时候说明的 addAll 方法，LinkedList 还提供了 `add(int index, E element);` 方法，下面我们来看在这个方法：


```
/**
 * 在指定 index 位置插入节点
 */
public void add(int index, E element) {
   // 检查角标是否越界
   checkPositionIndex(index);
   // 如果 index = size 代表是在尾部插入节点
   if (index == size)
       linkLast(element);
   else
       linkBefore(element, node(index));
}
```

可以先看到当 `0 =< index <size` 的时候调用了 `linkBefore(element, node(index))`方法，我们先来看下 node(index) 方法的实现：


```

/**
 * 返回一个非空节点，这个非空节点位于 index 位置
 */
 Node<E> node(int index) {
   // assert isElementIndex(index);
    // 如果 index < size/2 则从0开始寻找指定角标的节点
    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
    } else {
         // 如果 index >= size/2 则从 size-1 开始寻找指定角标的节点
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
    }
 }
```
 
大家可能会疑惑为什么这里注释为返回一个非空节点？其实仔细想下就明白了，这里的节点一定不为 null，如果一开始链表为空的时候，index 为 0 的位置肯定为 null，为什么不会产生异常情况呢？其实如果一开始链表中没有元素 size = 0，如果我们向 `index = 0` 的位置添加元素是不会走到 else 中的，而是会调用 `linkLast(element);` 方法去添加元素。 因此 node 方法可以用于根据指定 index 去以 size/2 为界限搜索index 位置的 Node;

我们再看回 linkBefore 方法，为什么要叫做 linkBefore 呢，因为在链表的中间位置添加节点，其实就是讲 index 原来的节点前添加一个节点，添加节点我们需要知道该节点的前一个节点和当前节点，

1. 将构造的新节点前指针 prev 指向 index 的前一个元素，
2. 新节点前指针 next 指针指向 index 位置的节点，
3. index 位置节点 prev 指针指向新节点
4. index 位置前节点（pred）的 next 指针指向新节点。

linkBefore 也是做了上述四件事：

```
void linkBefore(E e, Node<E> succ) {
   // assert succ != null;
   // 由于 succ 一定不为空，所以可以直接获取 prev 节点
   final Node<E> pred = succ.prev;
   // 新节点 prev 节点为 pred，next 节点为 succ
   final Node<E> newNode = new Node<>(pred, e, succ);
   // 原节点的 prev 指向新节点
   succ.prev = newNode;
   // 如果 pred 为空即头节点出插入了一个节点，则将新的节点赋值给 first 索引
   if (pred == null)
       first = newNode;
   else
       pred.next = newNode;//否则 pred 的下一个节点改为新节点
   size++;
   modCount++;
}
```

### LinkedList 删除节点的方法

与添加节点方法对应的就是删除节点方法：

```
/**
 *  删除头节点
 * @return 删除的节点的值 即 节点的 element
 * @throws NoSuchElementException 如果链表为空则抛出异常
 */
 public E removeFirst() {
    final Node<E> f = first;
    if (f == null)
        throw new NoSuchElementException();
    return unlinkFirst(f);
 }

/**
 *  删除尾节点
 *
 * @return  删除的节点的值 即 节点的 element
 * @throws NoSuchElementException  如果链表为空则抛出异常
 */
 public E removeLast() {
    final Node<E> l = last;
    if (l == null)
        throw new NoSuchElementException();
    return unlinkLast(l);
 }
 
```

可以看出最终调用的方法为 `unlinkFirst`，`unlinkLast` 方法：


```
/**
 * 移除头节点
 */
 private E unlinkFirst(Node<E> f) {
    // assert f == first && f != null;
    // 头节点的 element 这里作为返回值使用
    final E element = f.item;
    // 头节点下个节点
    final Node<E> next = f.next;
    // 释放头节点的 next 指针，和 element 下次 gc 的时候回收这个内部类
    f.item = null;
    f.next = null; // help GC
    // 将 first 索引指向新的节点
    first = next;
    // 如果 next 节点为空，即链表只有一个节点的时候，last 指向 null
    if (next == null)
        last = null;
    else
        next.prev = null; //否则 next 的 prev 指针指向 null
    size--;//改变链表长度
    modCount++;//修改操作数
    return element;//返回删除节点的值
 }

/**
 * 移除未节点
 */
 private E unlinkLast(Node<E> l) {
    // assert l == last && l != null;
    final E element = l.item;
    //未节点的前一个节点，
    final Node<E> prev = l.prev;
    //释放未节点的内容
    l.item = null;
    l.prev = null; // help GC
    //将 last 索引指向新的未节点
    last = prev;
    // 链表只有一个节点的时候，first 指向 null
    if (prev == null)
       first = null;
    else
        prev.next = null;
    size--;
    modCount++;
    return element;
 }
```
上边我们说过在指定位置添加的节点时候的是4个步骤，移除头尾节点是两个特殊的节点，但是总体来说还是一样的。下面看到 `unlink(node(index))`就是这样的：

![](https://ws3.sinaimg.cn/large/006tNbRwly1fpwg67573gj31cm0giweg.jpg)

```
/**
 * Unlinks non-null node x.
 */
E unlink(Node<E> x) {
   // assert x != null;
   final E element = x.item;
   //保存 index 节点的前后两个节点
   final Node<E> next = x.next;
   final Node<E> prev = x.prev;
    // 如果节点为头节点，则做 unlinkFirst 相同操作
   if (prev == null) {
       first = next;
   } else {//否则将上一个节点的 next 指针指向下个节点
       prev.next = next;
       // 释放 index 位置 prev 指针
       x.prev = null;
   }
    // 如果节点为尾节点，则将 last 索引指向上个节点
   if (next == null) {
       last = prev;
   } else {//否则下个节点 prev 指针指向上个节点
       next.prev = prev;
       x.next = null;
   }

   x.item = null;
   size--;
   modCount++;
   return element;
}
```

看完 `unlink` 操作结合之前说的 `node(index) `，下边两种删除节点的操作，就很好理解了

```
/**
 * 删除指定索引位置的节点
 */
public E remove(int index) {
   checkElementIndex(index);
   return unlink(node(index));
}

/**
 *删除从头节点其第一个与 o 相同的节点
 */
public boolean remove(Object o) {
    // 区别对待 null 元素，比较元素时候使用 == 而不是 equals
   if (o == null) {
       for (Node<E> x = first; x != null; x = x.next) {
           if (x.item == null) {
               unlink(x);
               return true;
           }
       }
   } else {
       for (Node<E> x = first; x != null; x = x.next) {
           if (o.equals(x.item)) {
               unlink(x);
               return true;
           }
       }
   }
   return false;
}
```

看完单个删除节点的方法 LinkedList 实现了 List 接口的 clear 操作，用于删除链表所有的节点：


```
/**
* Removes all of the elements from this list.
* The list will be empty after this call returns.
*/
public void clear() {
   // 依次清除节点，帮助释放内存空间
   for (Node<E> x = first; x != null; ) {
       Node<E> next = x.next;
       x.item = null;
       x.next = null;
       x.prev = null;
       x = next;
   }
   first = last = null;
   size = 0;
   modCount++;
}
```

### LinkedList 查询节点的方法

LinkedList 查询节点的方法，可分为根据指定的索引查询，获取头节点，获取未节点三种。值得注意的是，根据索引去获取节点内容的效率并不高，所以如果查询操作多余增删操作的时候建议用 `ArrayList` 去替代。

```
/**
* 根据索引查询
*
public E get(int index) {
   checkElementIndex(index);
   return node(index).item;
}

/**
* 返回 first 索引指向的节点的内容
*
* @return the first element in this list
* @throws NoSuchElementException 如果链表为空则抛出异常
*/
public E getFirst() {
   final Node<E> f = first;
   if (f == null)
       throw new NoSuchElementException();
   return f.item;
}

/**
* 返回 last 索引指向的节点的内容
*
* @return the last element in this list
* @throws NoSuchElementException 如果链表为空则抛出异常
*/
public E getLast() {
   final Node<E> l = last;
   if (l == null)
       throw new NoSuchElementException();
   return l.item;
}

```

### LinkedList 的修改节点方法

修改节点也分为修改指定索引的节点内容和修改头节点内容，未节点内容的方法？ 哈哈，理所因当了，其实` LinkedList` 只提供了 `set(int index, E element) ` 一个方法。


```
public E set(int index, E element) {
   // 判断角标是否越界
   checkElementIndex(index);
   // 采用 node 方法查找对应索引的节点
   Node<E> x = node(index);
   //保存节点原有的内容值
   E oldVal = x.item;
   // 设置新值
   x.item = element;
   // 返回旧的值
   return oldVal;
}

```

### LinkedList 的元素查询方法

上边的我们知道 `LinkedList`提供根据角标查询节点的方法，`LinkedList` 还提供了一系列判断元素在链表中的位置的方法。


```
/* 
* 返回参数元素在链表的节点索引，如果有重复元素，那么返回值为从**头节点**起的第一相同的元素节点索引，
* 如果没有值为该元素的节点，则返回 -1；
* 
* @param o element to search for
* @return 
*/
public int indexOf(Object o) {
   int index = 0;
  // 区别对待 null 元素，用 == 判断，非空元素用 equels 方法判断 
   if (o == null) {
       for (Node<E> x = first; x != null; x = x.next) {
           if (x.item == null)
               return index;
           index++;
       }
   } else {
       for (Node<E> x = first; x != null; x = x.next) {
           if (o.equals(x.item))
               return index;
           index++;
       }
   }
   return -1;
}

/**
**返回参数元素在链表的节点索引，如果有重复元素，那么返回值为从**尾节点起**的第一相同的元素节点索引，
* 如果没有值为该元素的节点，则返回 -1；
*
* @param o element to search for
* @return the index of the last occurrence of the specified element in
*         this list, or -1 if this list does not contain the element
*/
public int lastIndexOf(Object o) {
   int index = size;
   if (o == null) {
       for (Node<E> x = last; x != null; x = x.prev) {
           index--;
           if (x.item == null)
               return index;
       }
   } else {
       for (Node<E> x = last; x != null; x = x.prev) {
           index--;
           if (o.equals(x.item))
               return index;
       }
   }
   return -1;
}
```

两个方法分别返回从**头节点起**第一个与参数元素相同的节点索引，和从**尾节点起**第一个与参数元素相同的节点索引。

除了上述两个方法我们开可以调用 `contains(Object o)` 来判断链表中是否有该元素存在。调用 indexOf 从头结点开始查询元素位置遍历完成后若 返回值 !=-1 则表示存在，反之不存在

```
public boolean contains(Object o) {
    return indexOf(o) != -1;
}
```

## LinkedList 作为双向队列的增删改查

分析完 LinkedList 作为 List 集合的增删改查操作，我们看下 LinkedList 是如何实现 `Deque` 接口的方法的：

### Deque 双端队列

我们先来认识一下 Java 中的 双端队列，我们都知道 `Queue` 是一个队列，遵循 FIFO 准则，我们也知道 `Stack` 是一个栈结构，遵循 FILO 准则。 而` Deque` 这个双端队列就厉害了,它既可以实现栈的操作，也可以实现队列的操作，换句话说，实现了这个接口的类，既可以作为栈使用也可以作为队列使用。

我们来看下 Queue 给我们提供了的方法：

|  | 头部 |  头部 | 尾部 | 尾部  |
| :-- | :-: | :-: | :-: | :-: |
| 插入 | addFirst(e) | offerFirst(e) | addLast(e) | offerLast(e) |
| 移除 | removeFirst() | pollFirst() | remveLast() | pollLast |
| 获取 | getFirst() | peekFirst() | getLast() | peekLast |	addLast(e)	offerLast(e)

由于 `Deque` 接口继承 `Queue` 接口，当 `Deque` 当做队列使用时（FIFO），只需要在头部删除，尾部添加即可。我们现在复习下 `Queue` 中的方法及区别：

1. `Queue` 的 `offer` 和 `add` 都是在队列中插入一个元素，具体区别在于，对于一些 Queue 的实现的队列是有大小限制的，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。此时调用 `add() `方法会抛出异常，而 `offer()` 只是返回的 false。

2. `remove()` 和 `poll()` 方法都是从队列中删除第一个元素。remove()也将抛出异常，而 `poll()` 则会返回 `null`

3. `element()` 和 `peek()` 用于在队列的头部查询元素。在队列为空时， `element()` 抛出一个异常，而 `peek()` 返回 `null`。

上述方法的区别对于 Queue 对应的实现类的对应方法，是一种规定，自己在实现 Queue 队列的时候也要遵循此规则。

我们通过下边的表格来对照下双端队列是如何实现队列操作的，值得注意的是 `Deque` 实现了 `Queue`，所以 `Queue` 所有的方法 `Deque` 都有，下面比较的是`Deque`区别 `Queue` 的方法：

| Queue | Deque |
| --- | --- |
| add(e)  | addLast() |
| offer(e) | offerLast() |
| remove() | removeFirst() |
| poll() | pollFirst() |
| element() | getFirst() |
| peek() | peekFirst() |


由上表我们可以看到 `Deque` 对应的 `Queue` 的方法，那么对于他们的实现类 `LinkedList` 是怎么实现的呢？

### Deque 和 Queue 添加元素的方法

我们对比下对应的实现方法：

```
// queue 的添加方法实现，
public boolean add(E e) {
   linkLast(e);
   return true;
}
// Deque 的添加方法实现，
public void addLast(E e) {
   linkLast(e);
} 
  
// queue 的添加方法实现，
public boolean offer(E e) {
   return add(e);
}

// Deque 的添加方法实现，
public boolean offerLast(E e) {
        addLast(e);
        return true;
}
    
```

上面提及到 `Queue`的 `offer` 和 `add` 的区别针对容量有限制的实现，很明显 `LinkedList` 的大小并没有限制，所以在 `LinkedList` 中他们的实现并没有实质性不同。

### Deque 和 Queue 删除元素的方法

```
// Queue 删除元素的实现 removeFirst 会抛出 NoSuchElement 异常
public E remove() {
   return removeFirst();
}

// Deque 的删除方法实现
public E removeFirst() {
   final Node<E> f = first;
   if (f == null)
       throw new NoSuchElementException();
   return unlinkFirst(f);
}
    
// Queue 删除元素的实现 不会抛出异常 如果链表为空则返回 null 
public E poll() {
   final Node<E> f = first;
   return (f == null) ? null : unlinkFirst(f);
}

// Deque 删除元素的实现 不会抛出异常 如果链表为空则返回 null 
public E pollFirst() {
   final Node<E> f = first;
   return (f == null) ? null : unlinkFirst(f);
}
```

### Deque 和 Queue 获取队列头部元素的实现

```
 // Queue 获取队列头部的实现 队列为空的时候回抛出异常
 public E element() {
    return getFirst();
 }
// Deque 获取队列头部的实现 队列为空的时候回抛出异常
public E getFirst() {
   final Node<E> f = first;
   if (f == null)
       throw new NoSuchElementException();
   return f.item;
}

// Queue 获取队列头部的实现 队列为空的时候返回 null
public E peek() {
   final Node<E> f = first;
   return (f == null) ? null : f.item;
}

// Deque 获取队列头部的实现 队列为空的时候返回 null
public E peekFirst() {
   final Node<E> f = first;
   return (f == null) ? null : f.item;
}
```

上述我们分析了，双端队列作为队列使用的时候的各个方法的区别，也可是看出 `LinkedList` 对对应方法的实现，遵循了队列设计原则。

下面我们来看看下双端队列作为栈 `Stack`使用的时候方法对应关系，与 `Queue` 不同，`Stack` 本身就是实现类，他拥有 FILO 的原则， `Stack` 的入栈操作通过 `push` 方法进行，出栈操作通过 `pop` 方法进行，查询操作通过 `peek` 操作进行。 `Deque` 作为栈使用的时候，也遵循 FILO 准则，入栈和出栈是通过添加和移除头节点实现的。


| Stack | Deque |
| --- | --- |
| push(e) | addFist(e) |
| pop() | removeFirst() |
| peek() | peekFirst() |

由于分析队列的时候已经分析了`addFist` 和 `removeFirst`，`peekFirst`操作的方法了，下边我们来显 push 和 pop 的实现：


```
public void push(E e) {
   addFirst(e);
}

public E pop() {
   return removeFirst();
}
```
哇，毫无遮掩的直接调用了 `addFirst` 和 `removeFirst` 方法。这样看来没啥好分析的了。



## LinkedList 的遍历

在 `ArrayList` 分析的时候，我们就知道 `List` 的实现类，有4中遍历方式：for 循环，高级 for 循环，`Iterator` 迭代器方法， `ListIterator` 迭代方法。由于 `ArrayList` 源码分析的时候比较详细看了源码，对于不同数据结构的 `LinkedList` 我们只看下他们的不同之处.

`LinkedList` 没有单独 `Iterator` 实现类，它的 `iterator` 和 `listIterator` 方法均返回 `ListItr `的一个对象。 LinkedList 作为双向链表数据结构，过去上个元素和下个元素很方便。

下边我们来看下 `ListItr` 的源码：

```
private class ListItr implements ListIterator<E> {
   // 上一个遍历的节点
   private Node<E> lastReturned;
   // 下一次遍历返回的节点
   private Node<E> next;
   // cursor 指针下一次遍历返回的节点
   private int nextIndex;
   // 期望的操作数
   private int expectedModCount = modCount;
    
   // 根据参数 index 确定生成的迭代器 cursor 的位置
   ListItr(int index) {
       // assert isPositionIndex(index);
       // 如果 index == size 则 next 为 null 否则寻找 index 位置的节点
       next = (index == size) ? null : node(index);
       nextIndex = index;
   }

   // 判断指针是否还可以移动
   public boolean hasNext() {
       return nextIndex < size;
   }
    
  // 返回下一个带遍历的元素
  public E next() {
       // 检查操作数是否合法
       checkForComodification();
       // 如果 hasNext 返回 false 抛出异常，所以我们在调用 next 前应先调用 hasNext 检查
       if (!hasNext())
           throw new NoSuchElementException();
        // 移动 lastReturned 指针
       lastReturned = next;
        // 移动 next 指针
       next = next.next;
       // 移动 nextIndex cursor
       nextIndex++;
       // 返回移动后 lastReturned
       return lastReturned.item;
   }

  // 当前游标位置是否还有前一个元素
   public boolean hasPrevious() {
       return nextIndex > 0;
   }
  
  // 当前游标位置的前一个元素
   public E previous() {
       checkForComodification();
       if (!hasPrevious())
           throw new NoSuchElementException();
        // 等同于 lastReturned = next；next = (next == null) ? last : next.prev;
        // 发生在 index = size 时
       lastReturned = next = (next == null) ? last : next.prev;
       nextIndex--;
       return lastReturned.item;
   }
    
   public int nextIndex() {
       return nextIndex;
   }

   public int previousIndex() {
       return nextIndex - 1;
   }
    
    // 删除链表当前节点也就是调用 next/previous 返回的这节点，也就 lastReturned
   public void remove() {
       checkForComodification();
       if (lastReturned == null)
           throw new IllegalStateException();

       Node<E> lastNext = lastReturned.next;
       //调用LinkedList 的删除节点的方法
       unlink(lastReturned);
       if (next == lastReturned)
           next = lastNext;
       else
           nextIndex--;
       //上一次所操作的 节点置位空    
       lastReturned = null;
       expectedModCount++;
   }

    // 设置当前遍历的节点的值
   public void set(E e) {
       if (lastReturned == null)
           throw new IllegalStateException();
       checkForComodification();
       lastReturned.item = e;
   }
    // 在 next 节点位置插入及节点
   public void add(E e) {
       checkForComodification();
       lastReturned = null;
       if (next == null)
           linkLast(e);
       else
           linkBefore(e, next);
       nextIndex++;
       expectedModCount++;
   }
    //简单哈操作数是否合法
   final void checkForComodification() {
       if (modCount != expectedModCount)
           throw new ConcurrentModificationException();
   }
}
```

![](https://ws3.sinaimg.cn/large/006tNbRwly1fpwkq0tm98j31040dqglq.jpg)

![](https://ws4.sinaimg.cn/large/006tNbRwly1fpwks1yjmyj31580huwep.jpg)

![](https://ws1.sinaimg.cn/large/006tNbRwly1fpwkqkvcofj316k0hoglw.jpg)



## 总结

本文从 `LinkedList` 的源码出发，分析了LinkedList 集合常见的操作，以及它作为队列或者栈的时候增删改查方法。是继上一篇 `ArrayList`源码分析后的第二篇集合框架源码分析。

你见过凌晨3点北京的太阳吗？没有！三点太阳还没升起呢~



