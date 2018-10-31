package proxy;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 如果使用静态代理，首先被代理这要实现接口，然后代理这要实现这个接口，这个代理接口是需求者需要执行的操作，
 * 最后想需求者，通过调用已经实现了这些操作接口的代理类的方法，来实现需求。
 * <p>
 * 而动态代理：
 * <p>
 * 首先有一个委托类，同样实现了已经规定好的与外界沟通的操作方法的接口。而代理类则不用实现"之前所谓的代理接口"，
 * 而是转而实现 JDK 的 InvocationHandler，最后需求者通过调用 Proxy.newProxyInstance() 生成的"代理接口的实现类的方法来实现需求。
 * <p>
 * 可以看出差异在与没有手动创建一个代理类，而是通过 JDK 的 Proxy.newProxyInstance() 来进行创建的。 这又是 JDK 为我做的事情。实际上可以看出
 * 真正的方法调用是通过 Invoke 方法去调用的，也就是在 InvocationHandler 的 invoke 里边我们可以区分代理接口的方法来做一些前置和后置操作。
 * <p>
 * 那现在还有一点不明白的是为什么 Proxy.newProxyInstance() 生成的对象就是对应的代理接口实现类。
 * <p>
 * newProxyInstance(ClassLoader loader, Class<?>[] interfaces,InvocationHandler h）
 * 第一传入 classLoader，第二个传入对应的代理接口，第三个传入 InvocationHandler 想想一下流程
 * <p>
 * 通过这三个参数，去生成对应的代理接口的实现类，然后调用改接口内的方法的时候将会调用 InvocationHandler 的 invoke 方法
 * <p>
 * 想要跟被代理类打交道的对象，只能通过代理的类去打交道
 */
public class Client {


    public static void main(String[] args) {
        //被代理的类不能为抽象类
        HouseOwner houseOwner = new HouseOwner();

        //这里要想强转就得实现知道 HouseOwer 实现的是哪个代理接口
        RentHouse rentHouse = (RentHouse) new DynamicProxy().bind(houseOwner);

        System.out.println(rentHouse.getClass().getName());

        rentHouse.rent();

        rentHouse.charge("10000");
        Solution solution = new Solution();
        int[] pushA = new int[]{1, 2, 3, 4, 5};
        int[] popA1 = new int[]{4, 3, 5, 1, 2};
        int[] popA2 = new int[]{4, 5, 3, 2, 1};

        System.out.println("popA1 是否是出栈队列 " + solution.IsPopOrder(pushA, popA1));
        System.out.println("popA2 是否是出栈队列 " + solution.IsPopOrder(pushA, popA2));
//        TwoQueueStack<Integer> queueStack = new TwoQueueStack<>();
//        queueStack.push(1);
//        queueStack.push(2);
//        queueStack.push(3);
//        queueStack.push(4);
//        queueStack.pop();
//        queueStack.pop();
//        queueStack.push(5);
//        queueStack.pop();

        System.out.println("log2  = " + log2(3));
        System.out.println("isodd  = " + isOdd2(1));
        System.out.println("11111  = " + count1(-1));
        int[] arr = {2,1,3,3,2,1,4,5};
        printOddTimesNum(arr);

    }

    public static int count1(int n){
        int res = 0;
        while(n != 0){
            n &= (n - 1);
            res++;
        }
        return res;
    }


    public static boolean log2(int num) {
        return (num & (num - 1)) == 0;
    }

    public static boolean isOdd2(int num) {
        return (num & 1) != 0;
    }

    public static int OddTimesNum(int[] arr) {
        int eO = 0;
        for (int cur : arr) {
            eO = eO ^ cur;
        }

        return eO;
    }


    public static void printOddTimesNum(int[] arr) {

        int eO = 0;
        int eOhasOne = 0;

        for (int cur : arr) {
            eO = eO ^ cur;
        }

        int rightOne = eO & (~eO + 1);
        for (int cur : arr) {
            if ((rightOne & cur) != 0) {
                eOhasOne = eOhasOne ^ cur;
            }
        }

        System.out.println("eOhasOne = " + eOhasOne + "  " + (eOhasOne ^ eO));
    }

    public static class TwoStackQueue<E> {
        private Stack<E> stackA;
        private Stack<E> stackB;

        public TwoStackQueue() {
            stackA = new Stack<>();
            stackB = new Stack<>();
        }

        /**
         * 添加元素逻辑
         *
         * @param e 要添加的元素
         * @return 这里只是遵循 Queue 的习惯，这里简单处理返回 true 即可
         */
        public boolean add(E e) {
            stackA.push(e);
            return true;
        }

        /**
         * 去除元素的时候需要判断两个地方，StackA & StackB 是否都为空
         * StackB 为空的时候讲StackA中的元素全部依次压入 StackB
         *
         * @return 返回队列中的元素 如果队列为空返回 null
         */
        public E poll() {
            //如果队列中没有元素则直接返回空，也可以选择抛出异常
            if (stackB.isEmpty() && stackA.isEmpty()) {
                return null;
            }

            if (stackB.isEmpty()) {
                while (!stackA.isEmpty()) {
                    stackB.add(stackA.pop());
                }
            }

            return stackB.pop();
        }

        /**
         * peek 操作不取出元素，只返回队列头部的元素值
         *
         * @return 队列头部的元素值
         */
        public E peek() {
            //如果队列中没有元素则直接返回空，也可以选择抛出异常
            if (stackB.isEmpty() && stackA.isEmpty()) {
                return null;
            }

            if (stackB.isEmpty()) {
                while (!stackA.isEmpty()) {
                    stackB.add(stackA.pop());
                }
            }

            return stackB.peek();
        }
    }

    public static class Solution {

        public boolean IsPopOrder(int[] pushA, int[] popA) {
            int len = pushA.length;

            Stack<Integer> stack = new Stack<>();
            for (int pushIndex = 0, popIndex = 0; pushIndex < len; pushIndex++) {
                stack.push(pushA[pushIndex]);
                while (popIndex < len && popA[popIndex] == stack.peek()) {
                    stack.pop();
                    popIndex++;
                }
            }
            return stack.isEmpty();
        }
    }


    public static class TwoQueueStack<E> {
        private Queue<E> queueA;
        private Queue<E> queueB;

        public TwoQueueStack() {
            queueA = new LinkedList<>();
            queueB = new LinkedList<>();
        }

        /**
         * 选一个非空的队列入队
         *
         * @param e
         * @return
         */
        public E push(E e) {
            if (queueA.size() != 0) {
                System.out.println("从 queueA 入队 " + e);
                queueA.add(e);
            } else if (queueB.size() != 0) {
                System.out.println("从 queueB 入队 " + e);
                queueB.add(e);
            } else {
                System.out.println("从 queueA 入队 " + e);
                queueA.add(e);
            }
            return e;
        }

        public E pop() {
            if (queueA.size() == 0 && queueB.size() == 0) {
                return null;
            }

            E result = null;
            if (queueA.size() != 0) {
                while (queueA.size() > 0) {
                    result = queueA.poll();
                    if (queueA.size() != 0) {
                        System.out.println("从 queueA 出队 并 queueB 入队 " + result);
                        queueB.add(result);
                    }
                }
                System.out.println("从 queueA 出队 " + result);

            } else {
                while (queueB.size() > 0) {
                    result = queueB.poll();
                    if (queueB.size() != 0) {
                        System.out.println("从 queueB 出队 并 queueA 入队 " + result);
                        queueA.add(result);
                    }
                }
                System.out.println("从 queueB 出队" + result);
            }
            return result;
        }
    }
}



