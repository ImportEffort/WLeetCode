package NodePractice;

import TestUItls.Node;

import java.util.Random;

public class NodePractice {

    public static void main(String[] args) {

        NodePractice llm = new NodePractice();

        Node head = llm.buildList(10,10);

        llm.printList("原链表为", head);

        System.out.println("单链表长度为 " + llm.getLength(head));

        try {
            System.out.println("单链表中第9个节点为 " + llm.getValueOfIndex(head, 6));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("获取指定值的 " + llm.getVNodeIndex(head, 2));

        Node newHead = llm.addAtHead(head, 11);
        llm.printList("添加头节点后的链表为", newHead);

        llm.addAtTail(head, 11);
        llm.printList("添加尾节点后的链表为", head);

        try {
            Node node = llm.insertElement(head, 18, 6);
            llm.printList("在 index 添加节点后的链表为", node);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Node node = llm.deleteHead(head);
            llm.printList("删除头节点后的链表为", node);

            llm.deleteTail(head);
            llm.printList("删除尾节点后的链表为", head);

            Node deleteElement = llm.deleteElement(head, 10);
            llm.printList("删除指定索引节点后的链表为", deleteElement);

        } catch (Exception e) {
            e.printStackTrace();
        }


        Node mid = llm.getMid(head);
        llm.printList("链表的中间节点为 ", mid);


        Node loopList = llm.buildLoopList(6,10);
        boolean isLoopList1 = llm.isLoopList(loopList);
        System.out.println("链表是否为循环链表 " + isLoopList1);

        Node loopList2 = llm.buildList(6,10);
        boolean isLoopList2 = llm.isLoopList(loopList2);
        System.out.println("链表是否为循环链表 " + isLoopList2);
        System.out.println();

        Node node = llm.getLastIndexNode(head, 11);
        llm.printList("链表的倒数第 n 个节点为 ", node);

        Node deleteLastNNode = llm.deleteLastNNode(head, 11);

        llm.printList("删除链表的倒数第 n 个节点后为 ", deleteLastNNode);


        Node head5 = llm.buildList(10, 9);
        llm.printList("旋转链表前 为", head5);
        Node rotateList = llm.rotateList(head5, 4);
        llm.printList("旋转链表后 为", rotateList);

        Node head6 = llm.buildList(5, 100);
        llm.printList("翻转部分链表前 为", head6);
        Node reversePartList = llm.reversePartList(head6, 2, 4);
        llm.printList("翻转部分链表后 为", reversePartList);

        Node partition = llm.buildList(10,100);
        llm.printList("划分链表前 为", partition);
        Node partitionResult = llm.partition(partition, 20);
        llm.printList("划分链表后 为", partitionResult);


        Node addList1 = llm.buildList(3,9);
        llm.printList("待合并的链表 addList1 为", addList1);
        Node addList2 = llm.buildList(4,9);
        llm.printList("待合并的链表 addList2 为", addList1);
        Node addLists = llm.addLists(addList1, addList2);
        llm.printList("合并后的链表 addLists 为", addLists);

        //进行排序并输出
        Node mergeSortHead = llm.bulidSortList(10, 100);
        llm.printList("待排序链表前为", mergeSortHead);
        Node sortedHead1 = llm.mergeSort(mergeSortHead);
        llm.printList("归并排序后链表为", sortedHead1);

        Node sortedHead = llm.bulidSortList(10, 100);
        llm.printList("待排序链表前为", mergeSortHead);

        Node sortedHead2 = llm.BubbleUpSortList(sortedHead);
        llm.printList("冒泡排序后链表为", sortedHead2);

        Node sortedHead3 = llm.insertionSortList(sortedHead);
        llm.printList("插入排序后链表为", sortedHead3);

        Node head3 = llm.bulidSortList(10, 100);
        llm.printList("删除重复元素前为", head3);

        llm.delSortSame(head3);
        llm.printList("删除重复元素后为", head3);

        Node head4 = llm.buildList(10, 9);
        llm.printList("删除重复元素前为", head4);
        llm.delSame(head4);
        llm.printList("删除重复元素后为", head4);

        Node relocate1 = llm.buildList(6, 9);

        llm.printList("重排前链表为", relocate1);
        llm.relocate1(head6);
        llm.printList("重排后的链表为 ", relocate1);

        Node relocate2 = llm.buildList(new int[]{1, 92, 8, 86, 9, 43, 20});
        llm.printList("重排前链表为  ", relocate2);

        Node relocate = llm.relocate2(relocate2);

        llm.printList("重排后的链表为 ", relocate);
    }



    private Node partition(Node head , int x){
        if(head == null){
            return null;
        }

        Node left = new Node(0);
        Node right = new Node(0);

        Node dummyLeft = left;
        Node dummyRight = right;

        while(head != null){
            if(head.value < x){
                dummyLeft.next = head;
                dummyLeft = dummyLeft.next;
            }else{
                dummyRight.next = head;
                dummyRight = dummyRight.next;
            }
            head = head.next;
        }

        dummyLeft.next = right.next;
        right.next = null;

        return left.next;
    }


    public int getLength(Node head) {

        if (head == null) {
            return 0;
        }

        Node pointer = head;

        int len = 0;

        while (pointer != null) {
            len++;
            pointer = pointer.next;
        }

        return len;
    }


    public int getValueOfIndex(Node head, int index) throws Exception {

        if (index < 0 || index >= getLength(head)) {
            throw new Exception("角标越界！");
        }

        if (head == null) {
            throw new Exception("当前链表为空！");
        }

        Node dummyHead = head;

        while (dummyHead.next != null && index > 0) {
            dummyHead = dummyHead.next;
            index--;
        }

        return dummyHead.value;
    }


    public int getVNodeIndex(Node head, int value) {

        int index = -1;
        Node dummyHead = head;

        while (dummyHead != null) {
            index++;
            if (dummyHead.value == value) {
                return index;
            }
            dummyHead = dummyHead.next;
        }

        return -1;
    }

    public Node addAtHead(Node head, int value) {
        Node newHead = new Node(value);
        newHead.next = head;
        return newHead;
    }

    public void addAtTail(Node head, int value) {
        Node node = new Node(value);
        Node dummyHead = head;

        //找到未节点 注意这里是当元素的下一个元素为空的时候这个节点即为未节点
        while (dummyHead.next != null) {
            dummyHead = dummyHead.next;
        }

        dummyHead.next = node;
    }


    public Node insertElement(Node head, int value, int index) throws Exception {
        //为了方便这里我们假设知道链表的长度
        int length = getLength(head);
        if (index < 0 || index >= length) {
            throw new Exception("角标越界！");
        }

        if (index == 0) {
            return addAtHead(head, value);
        } else if (index == length - 1) {
            addAtTail(head, value);
        } else {

            Node pre = head;
            Node cur = head.next;
            //
            while (pre != null && index > 1) {
                pre = pre.next;
                cur = cur.next;
                index--;
            }

            //循环结束后 pre 保存的是索引的上一个节点 而 cur 保存的是索引值当前的节点
            Node node = new Node(value);
            pre.next = node;
            node.next = cur;
        }
        return head;
    }

    public Node deleteHead(Node head) throws Exception {
        if (head == null) {
            throw new Exception("当前链表为空！");
        }
        return head.next;
    }


    public void deleteTail(Node head) throws Exception {

        if (head == null) {
            throw new Exception("当前链表为空！");
        }

        Node dummyHead = head;
        while (dummyHead.next != null && dummyHead.next.next != null) {
            dummyHead = dummyHead.next;
        }
        dummyHead.next = null;
    }

    public Node deleteElement(Node head, int index) throws Exception {
        //为了方便这里我们假设知道链表的长度
        int size = getLength(head);

        if (index < 0 || index >= size) {
            throw new Exception("角标越界！");
        }

        if (index == 0) {
            return deleteHead(head);
        } else if (index == size - 1) {
            deleteTail(head);
        } else {
            Node pre = head;

            while (pre.next != null && index > 1) {
                pre = pre.next;
                index--;
            }

            //循环结束后 pre 保存的是索引的上一个节点 将其指向索引的下一个元素
            if (pre.next != null) {
                pre.next = pre.next.next;
            }
        }

        return head;
    }


    public Node getMid(Node head) {
        if (head == null) {
            return null;
        }

        Node slow = head;
        Node fast = head;

        // fast.next = null 表示 fast 是链表的尾节点
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }


    /**
     * 判断一个链表是否为有环
     *
     * @param head
     * @return
     */
    private boolean isLoopList(Node head) {

        if (head == null) {
            return false;
        }


        Node slow = head;
        Node fast = head.next;

        //如果不是循环链表那么一定有尾部节点 此节点 node.next = null
        while (slow != null && fast != null && fast.next != null) {
            if (fast == slow || fast.next == slow) {
                return true;
            }

            fast = fast.next.next;
            slow = slow.next;
        }

        return false;
    }

    private Node intersect(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }

        Node cur1 = head1;
        Node cur2 = head2;

        int n = 0;

        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }

        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }

        if (cur1 != cur2) {
            return null;
        }

        //令 cur1 指向 较长的链表，cur2 指向较短的链表
        if (n > 0) {
            cur1 = head1;
            cur2 = head2;
        } else {
            cur1 = head2;
            cur2 = head1;
        }

        n = Math.abs(n);

        //较长的链表先走 n 步
        while (n != 0) {
            cur1 = cur1.next;
        }

        //两个链表一起走 第一次相等节点即为相交的第一个节点
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }

        return cur1;
    }

    private void relocate1(Node head) {
        //如果链表长度小于2 则不需要重新操作
        if (head == null || head.next == null) {
            return;
        }

        //使用快慢指针 遍历链表找到链表的中点
        Node mid = head;
        Node right = head.next;

        while (right.next != null && right.next.next != null) {
            mid = mid.next;
            right = right.next.next;
        }

        //拆分左右半区链表
        right = mid.next;
        mid.next = null;

        //按要求合并
        mergeLR(head, right);

    }

    //head  1 -> 92 -> 8 -> 86 -> 9 -> 43 -> 20
    private Node relocate2(Node head) {

        //新建一个左右连个链表的头指针
        Node left = new Node();
        Node right = new Node();


        Node dummyLeft = left;
        Node dummyRight = right;

        int i = 0;
        while (head != null) {
            //因为 i 从0 开始 链表的头节点算是奇数位所以 i 先自增 再比较
            i++;
            if (i % 2 == 0) {
                dummyRight.next = head;
                dummyRight = dummyRight.next;
            } else {
                dummyLeft.next = head;
                dummyLeft = dummyLeft.next;
            }
            //每次赋值后记得将下一个节点置位 null
            Node next = head.next;
            head.next = null;
            head = next;
        }

        right = reverseList(right.next);
        dummyLeft.next = right;

        return left.next;
    }

    private void mergeLR(Node left, Node right) {
        Node temp = null;
        while (left.next != null) {
            temp = right.next;

            right.next = left.next;
            left.next = right;

            //这里每次向后移动两个位置 也就是原来的 left.next
            left = right.next;
            right = temp;
        }
        left.next = right;
    }

    private void delSame(Node head) {

        if (head == null || head.next == null) {
            return;
        }
        Node pre = null;
        Node next = null;
        Node cur = head;

        while (cur != null) {
            //当前考察的元素的前一个节点
            pre = cur;
            //当前考察元素
            next = cur.next;
            //从遍历剩余链表删除重复元素
            while (next != null) {
                if (cur.value == next.value) {
                    //删除相同元素
                    pre.next = next.next;
                } else {
                    //移动指针
                    pre = next;
                }
                //移动指针
                next = next.next;
            }
            //考察下一个元素
            cur = cur.next;
        }
    }


    private Node rotateList(Node head, int n) {

        int start = 1;

        Node fast = head;

        //先让快指针走 n 给个位置
        while (start < n && fast.next != null) {
            fast = fast.next;
            start++;
        }


        //循环结束后如果 start < n 表示 n 整个链表还要长 旋转后还是原链表
        //如果 fast.next = null 表示 n 正好等于原链表的长度此时也不需要旋转
        if (fast.next == null || start < n) {
            return head;
        }

        //倒数第 n + 1个节点
        Node pre = fast;
        //旋转后的头节点
        Node newHead = fast.next;

        while (fast.next != null) {
            fast = fast.next;
        }
        //原链表的最后一个节点指向原来的头节点
        fast.next = head;
        //将旋转的节点的上一个节点变为尾节点
        pre.next = null;

        return newHead;
    }


    /**
     * 删除有序链表重复元素
     *
     * @param head
     * @return
     */
    private void delSortSame(Node head) {

        if (head == null || head.next == null) {
            return;
        }

        Node dummy = head;
        while (dummy.next != null) {
            if (dummy.value == dummy.next.value) {
                dummy.next = dummy.next.next;
            } else {
                dummy = dummy.next;
            }
        }
    }

    private Node addLists(Node head1, Node head2) {
        head1 = reverseList(head1);
        head2 = reverseList(head2);
        //进位标识
        int ca = 0;
        int n1 = 0;
        int n2 = 0;
        int sum = 0;

        Node addHead = new Node(0);
        Node dummyHead = addHead;

        Node cur1 = head1;
        Node cur2 = head2;

        while (cur1 != null || cur2 != null) {
            n1 = cur1 == null ? 0 : cur1.value;
            n2 = cur2 == null ? 0 : cur2.value;

            sum = n1 + n2 + ca;

            Node node = new Node(sum % 10);
            ca = sum / 10;

            dummyHead.next = node;

            dummyHead = dummyHead.next;

            cur1 = cur1 == null ? null : cur1.next;
            cur2 = cur2 == null ? null : cur2.next;
        }

        if (ca > 0) {
            dummyHead.next = new Node(ca);
        }

        head1 = reverseList(head1);
        head2 = reverseList(head2);

        addHead = addHead.next;
        return reverseList(addHead);
    }


    private Node mergeSort2(Node head) {
        Node evenList = new Node();
        Node oddList = new Node();

        Node auxEvenList = evenList;
        Node auxOddList = oddList;
        Node next = null;
        int i = 0;
        while (head != null) {

            if (i % 2 == 0) {
                next = head.next;
                auxEvenList.next = head;
                auxEvenList = auxEvenList.next;

            } else {
                next = head.next;
                auxOddList.next = head;
                auxOddList = auxOddList.next;
            }

            head = next;
            i++;
        }

        auxEvenList.next = null;
        auxOddList.next = null;

        //翻转右侧链表
        Node reverseOdd = reverseList(oddList.next);
        return merge(evenList.next, reverseOdd);
    }


    private static Node reverseList(Node head) {
        Node cur = head;
        Node pre = null;
        Node next = null;

        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        //注意这里返回的是赋值当前比较元素
        return pre;
    }


    /**
     * @param head
     * @return
     */
    private Node mergeSort(Node head) {

        //递归退出的条件 当归并的元素为1个的时候 即 head.next 退出递归
        if (head == null || head.next == null) {
            return head;
        }

        Node slow = head;
        Node fast = head;

        //寻找 mid 值
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        Node left = head;
        Node right = slow.next;

        //拆分两个链表 如果设置链表的最后一个元素指向 null 那么 left 永远等于 head 这链表 也就无法排序
        slow.next = null;

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    /**
     * 排序归并部分 核心部分
     *
     * @param l 左半个链表
     * @param r 右半个链表
     * @return 排好序的整个链表
     */
    private Node merge(Node l, Node r) {

        //创建临时空间
        Node aux = new Node();
        Node cur = aux;

        //由于链表不能方便的拿到链表长度 所以一般使用 while l == null 表示链表遍历到尾部
        while (l != null && r != null) {
            if (l.value < r.value) {
                cur.next = l;
                cur = cur.next;
                l = l.next;
            } else {
                cur.next = r;
                cur = cur.next;
                r = r.next;
            }
        }
        //当有一半链表遍历完成后 另外一个链表一定只剩下最后一个元素（链表为基数）
        if (l != null) {
            cur.next = l;
        } else if (r != null) {
            cur.next = r;
        }

        return aux.next;
    }


    /**
     * 单链表的冒泡排序 冒泡排序：从第一个位置元素开始比较 每一次外层循环总能将最大的数排列好
     *
     * @param head
     * @return
     */
    public Node BubbleUpSortList(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
//        bubbleSort(head);
        int len = getListLength(head);
        int n = 0;
        Node q = head;
        while (q.next != null) {
            Node p = head;
            while (p.next != null && len - n > 0) {
                if (p.value > p.next.value) {
                    int temp = p.value;
                    p.value = p.next.value;
                    p.next.value = temp;
                } else {
                    p = p.next;
                }
            }
            q = q.next;
            n++;
        }
        return head;
    }

    /**
     * 获取链表长度
     *
     * @param head
     * @return
     */
    private int getListLength(Node head) {
        int len = 0;
        while (head.next != null) {
            len++;
            head = head.next;
        }

        return len;
    }


    /**
     * 单链表的插入排序
     * <p>
     * 对节点本身的操作会影响整个链表 对指针的赋值操作并不会影响链表
     *
     * @param head
     * @return
     */
    public Node insertionSortList(Node head) {
        if (head == null || head.next == null) return head;

        Node dummyHead = new Node(0), p = head;
        dummyHead.next = head;

        while (p.next != null) {
            if (p.value <= p.next.value) {  //p 的值不小于下一节点 p 指向 4
                p = p.next;
            } else {
                //p 指向 4
                Node temp = p.next;
                Node q = dummyHead;
                p.next = p.next.next;

                //从头遍历链表找到比当前 temp 值小的第一个元素插入其后边 整个位置一定在 头节点与 q 节点之间
                while (q.next.value < temp.value && q.next != q)
                    q = q.next;

                temp.next = q.next;
                //重新连接链表 注意 else 的过程并没有改变，
                q.next = temp;
            }
        }
        return dummyHead.next;
    }


    private Node reversePartList(Node head, int from, int to) {
        //首先判断是否满足翻转条件
        Node dummyHead = head;

        int len = 0;

        Node fPosPre = null;
        Node tPosNext = null;
        Node toPos = null;
        Node fromPos = null;

        while (dummyHead != null) {
            //因为 len = 0 开始的所以 len 先做自增一
            len++;

            if (len == from) {
                fromPos = dummyHead;
            } else if (len == from - 1) {
                fPosPre = dummyHead;
            } else if (len == to + 1) {
                tPosNext = dummyHead;
            } else if (len == to) {
                toPos = dummyHead;
            }

            dummyHead = dummyHead.next;
        }

        //不满足条件不翻转链表
        if (from > to || from < 0 || to > len || from > len) {
            return head;
        }


        Node cur = fromPos;
        Node pre = tPosNext;
        Node next = null;

        while (cur != null && cur != tPosNext) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        // 如果翻转的起点不是 head 则返回 head
        if (fPosPre != null) {
            fPosPre.next = pre;
            return head;
        }
        // 如果反转的链表是起点，那么翻转后 toPos 就是头结点
        return toPos;
    }


    /**
     * 删除倒是第 n 个节点 我们就要找到倒数第 n + 1 个节点， 如果 n > len 则返回原列表
     */
    private Node deleteLastNNode(Node head, int n) {

        if (head == null || n < 1) {
            return head;
        }

        Node fast = head;

        //注意 我们要构建长度为 n + 1 的窗口 所以 i 从 0 开始
        for (int i = 0; i < n; i++) {
            //fast 指针指向倒数第一个节点的时候，就是要删除头节点
            if (fast == null) {
                return head;
            } else {
                fast = fast.next;
            }
        }

        // 由于 n = len 再循环内部没有判断直接前进了一个节点，临界值 n = len 的时候 循环完成或 fast = null
        if (fast == null) {
            return head.next;
        }

        //此时 n 一定是小于 len 的 且 fast 先走了 n 步
        Node pre = head;

        while (fast.next != null) {
            fast = fast.next;
            pre = pre.next;
        }

        pre.next = pre.next.next;

        return head;
    }

    /**
     * 注意我们一般说倒数第 n 个元素 n 是从 1 开始的 构建一个长度为 n 的滑动窗口
     * <p>
     * n = 1 fast 指针不移动 fast 到达最后一个节点 即 fast.next 的时候 slow 也到达尾部节点满足条件
     * n = len fast 指针移动 n-1（len -1 ） 次 fast 到达最后一个节点  slow 位于头节点不变 满足条件 两个临界值
     * <p>
     * n > len 的时候 由上边的分析， 移动第 len -1 次的时候 fast 已经位于链表尾部 循环内部判断是 fast.next = null 的时候如果还可以循环
     * 则直接 return null；
     */
    private Node getLastIndexNode(Node head, int n) {

        // 输入的链表不能为空，并且 n 大于0
        if (n < 1 || head == null) {
            return null;
        }

        n = 10;
        // 指向头结点
        Node fast = head;
        // 倒数第k个结点与倒数第一个结点相隔 n-1 个位置
        // fast 先走 n-1 个位置
        for (int i = 1; i < n; i++) {
            // 说明还有结点
            if (fast.next != null) {
                fast = fast.next;
            }
            // 已经没有节点了，但是i还没有到达k-1说明k太大，链表中没有那么多的元素
            else {
                // 返回结果
                return null;
            }
        }

        Node slow = head;
        // fast 还没有走到链表的末尾，那么 fast 和 slow 一起走，
        // 当 fast 走到最后一个结点即，fast.next=null 时，slow 就是倒数第 n 个结点
        while (fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }
        // 返回结果
        return slow;
    }



    //建立链表
    private Node buildLoopList(int number, int randMark) {
        Random r = new Random();
        Node head = new Node(r.nextInt(randMark));
        Node pointer = head;
        for (int i = 1; i < number; i++) {
            Node tem = new Node(r.nextInt(randMark));//生成一个随机节点
            pointer.next = tem;
            pointer = tem;
        }
        pointer.next = head;
        return head;
    }


    private Node buildList(int[] arr) {
        if (arr == null) {
            return null;
        }
        Node head = new Node(arr[0]);
        Node pointer = head;
        for (int i = 1; i < arr.length; i++) {
            Node tem = new Node(arr[i]);//生成一个随机节点
            pointer.next = tem;
            pointer = tem;
        }

        return head;
    }

    private Node buildList(int number, int randMark) {
        if (number <= 0)
            return null;
        Random r = new Random();
        Node head = new Node(r.nextInt(randMark));
        Node pointer = head;
        for (int i = 1; i < number; i++) {
            Node tem = new Node(r.nextInt(randMark));//生成一个随机节点
            pointer.next = tem;
            pointer = tem;
        }
        return head;
    }

    private Node bulidSortList(int number, int randMark) {
        if (number <= 0)
            return null;
        Random r = new Random();
        Node head = new Node(r.nextInt(randMark));
        Node pointer = head;
        for (int i = 1; i < number; i++) {
            int value = r.nextInt(randMark);
            while (value < pointer.value){
                value = r.nextInt(randMark);
            }
            Node tem = new Node(value);//生成一个随机节点
            pointer.next = tem;
            pointer = tem;
        }
        return head;
    }


    private void printList(String prefix, Node head) {
        if (head == null) {
            System.out.println("null");
            return;
        }
        Node dummy = head;

        StringBuilder sb = new StringBuilder();

        while (dummy != null) {
            sb.append(dummy.value).append(" -> ");
            dummy = dummy.next;
        }

        String shortest = sb.substring(0, sb.length() - 4);
        if (prefix == null) {
            prefix = "链表为: ";
        }
        System.out.println(prefix + ": " + shortest);
        sb = null;
        System.out.println();
    }
}
