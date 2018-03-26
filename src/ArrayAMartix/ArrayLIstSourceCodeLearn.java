package ArrayAMartix;

import org.omg.CORBA.TCKind;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArrayLIstSourceCodeLearn {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        //第一次插入元素集合扩容到 element[10]的数组大小
        list.add("1");
        //之后再添加 ensureCapacityInternal 则不会扩容除非集合大小已经大于10
        list.add("2");

        ArrayList<TClass> tClasses = new ArrayList<>();

        TClass t1 = new TClass();
        t1.setAge(10);
        InnerTClass innerTClass1 = new InnerTClass();
        innerTClass1.setAge(100);
        t1.setInnerTClass(innerTClass1);

        TClass t2 = new TClass();
        t2.setAge(11);
        InnerTClass innerTClass2 = new InnerTClass();
        innerTClass2.setAge(101);
        t2.setInnerTClass(innerTClass2);

        tClasses.add(t1);
        tClasses.add(t2);

        System.out.println(tClasses.toString());
        System.out.println();





        /* toArray() 方法验证 */
        System.out.println(" -----  toArray() 方法验证  ------");


        Object[] arr = tClasses.toArray();
        System.out.println(Arrays.toString(arr));
        System.out.println();

        try {
            System.out.println(arr instanceof TClass[]);//false
            TClass[] arrTClass1 = (TClass[]) tClasses.toArray();
        } catch (Exception e) {
//            e.printStackTrace();// java.lang.ArrayStoreException

        }

        System.out.println();

        System.out.println(arr[0] instanceof TClass);//true

        System.out.println();


        ArrayList<SubClass> tLists = new ArrayList<>();
        tLists.add(new SubClass(1));
        tLists.add(new SubClass(2));

        //当 List.toArray(T[] a) 中 a.length < list.size 的时候使用 Array.copyOf 产生不同于 a 的新数组返回
        SubClass[] source = new SubClass[]{};
        //slist to Array 之前 source []source.length:: 0
        System.out.println("list to Array 之前 source " + Arrays.toString(source) + "source.length:: " + source.length);
        SubClass[] desSource = tLists.toArray(source);
        //list to Array 之后 source []   source.length:: 0
        System.out.println("list to Array 之后 source " + Arrays.toString(source) + "   source.length:: " + source.length);
        //source ==  desSource false
        System.out.println("source ==  desSource " + (source == desSource));


        //当 List.toArray(T[] a) 中 a.length == list.size 的时候使用 Array.copyOf 会将 list 中的内容赋值给 sourceEqual 并将其返回
        SubClass[] sourceEqual = new SubClass[2];
        //list to Array 之前 sourceEqual [null, null]   sourceEqual.length:: 2
        System.out.println("list to Array 之前 sourceEqual " + Arrays.toString(sourceEqual) + "   sourceEqual.length:: " + sourceEqual.length);
        SubClass[] desSourceEqual = tLists.toArray(sourceEqual);
        //list to Array 之后 desSourceEqual [SubClass{test=1}, SubClass{test=2}]
        System.out.println("list to Array 之后 desSourceEqual " + Arrays.toString(desSourceEqual));
        //list to Array 之后 sourceEqual [SubClass{test=1}, SubClass{test=2}]sourceEqual.length:: 2
        System.out.println("list to Array 之后 sourceEqual " + Arrays.toString(sourceEqual) + "sourceEqual.length:: " + sourceEqual.length);
        //sourceEqual ==  desSourceEqual true
        System.out.println("sourceEqual ==  desSourceEqual " + (sourceEqual == desSourceEqual));


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


        //初始化list集合
        List<String> listStream = new ArrayList<String>();
        listStream.add("测试数据1");
        listStream.add("测试数据2");
        listStream.add("测试数据3");
        listStream.add("测试数据12");

        //使用λ表达式遍历集合
        listStream.forEach(s -> System.out.println(s));
        System.out.println("---------------- \r\n");

        //结合Predicate使用和过滤条件筛选元素
        Predicate<String> contain1 = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("1");
            }
        };
        Predicate<String> contain2 = n -> n.contains("2");
        Predicate<String> andPredicate = contain1.and(contain2);

        //根据条件遍历集合
        listStream.stream().filter(contain1).forEach((String n) -> {
            System.out.println(n);
        });
        System.out.println("---------------- \r\n");

        list.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return contain1.test(s);
            }
        }).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });


        System.out.println("---------------- \r\n");

        list.stream().filter(contain1.and(contain2)).forEach(n -> System.out.println(n));

        System.out.println("---------------- \r\n");

        list.stream().filter(contain1.or(contain2)).forEach(n -> System.out.println(n));
        System.out.println("---------------- \r\n");


        //将过滤后的元素重新放到一个集合中
        List<String> newList = list.stream().filter(contain1.and(contain2)).collect(Collectors.toList());

        newList.forEach(s -> System.out.println(s));

        System.out.println(" -----------------");


        List<String> subClasses = Arrays.asList("abc", "def");

        Object[] objects = subClasses.toArray();

        //这里返回的是 String[]
        System.out.println(objects.getClass().getSimpleName());

        System.out.println("-------------------");

        String[] arrString = {"abc", "def"};

        Object[] copyOf = Arrays.copyOf(arrString, 5, Object[].class);

        System.out.println(Arrays.toString(copyOf));


        System.out.println("---------");

        Collection<String> testRemove = new ArrayList<>(10);

        testRemove.add("1");
        testRemove.add("2");

//        String remove = testRemove.remove(2);

        System.out.println(testRemove.contains(new SubClass(1)));


        List<SubClass> lists = new ArrayList<>();

        lists.add(new SubClass(1));
        lists.add(new SubClass(2));
        lists.add(new SubClass(3));
        lists.add(new SubClass(3));
        lists.add(new SubClass(3));
        lists.add(new SubClass(3));
        lists.add(new SubClass(3));

        List<SubClass> tempList = new ArrayList<>(lists.subList(2, lists.size()));

        tempList.get(1).test = 10;
        System.out.println(tempList); // 1

        System.out.println(lists); // 2

        System.out.println("---------\r\n");

        Object[] toArr = lists.toArray();
        System.out.println("" + Arrays.toString(toArr));

        toArr[1] = new SubClass(10);


        System.out.println("lists = " + lists.toString());

        ((SubClass) toArr[1]).test = 100;

        System.out.println("lists = " + lists.toString());


        lists.forEach(new Consumer<SubClass>() {
            @Override
            public void accept(SubClass subClass) {
                System.out.println(subClass.toString());
            }
        });

        System.out.println("---------------__!!!!!__");

        //每次取元素之前重新拿一次 lists.size 这样并不会出现异常
//        int size = lists.size();
//        for (int i = 0; i < size; i++) {
//            if (lists.get(i).test == 3)
//                list.remove(i);
//            else
//                System.out.println(lists.get(i).toString());
//        }

        List<SubClass> list2 = new ArrayList<>();

        list2.add(new SubClass(1));
        list2.add(new SubClass(2));
        list2.add(new SubClass(3));
        list2.add(new SubClass(3));

//        for (int i = 0; i < list2.size(); i++) {
//            if (list2.get(i).test == 3) {
//                list2.remove(i);//remove 以后 list 内部将 size 重新改变了 for 循环下次调用的时候可能就不进去了
//            }
//        }
//        System.out.println(list2);
//
//        for (SubClass sub : list2) {
//            if (sub.test == 3) {
//                list2.remove(sub);
//            }
//        }

//
//        for (int i = 0; i < list2.size(); i++) {
//            if (list2.get(i).test == 3) {
//                list2.remove(i);//remove 以后 list 内部将 size 重新改变了 for 循环下次调用的时候可能就不进去了
//            }
//        }
//        System.out.println(list2);
//        int size = list2.size();
//        for (int i = 0; i < size; i++) {
//            if (list2.get(i).test == 3) {
//                list2.remove(i);//remove 以后 list 内部将 size 重新改变了 for 循环下次调用的时候可能就不进去了
//            }
//        }
//        System.out.println(list2);


//
        Iterator<SubClass> iterator = list2.iterator();

        while (iterator.hasNext()) {
            SubClass next = iterator.next();
            int index = next.test;
            if (index == 3) {
                iterator.remove();
            }
        }

        System.out.println(list2);


//        List<Integer> test = new ArrayList<>(100000);
//        for (int i = 0; i < 100000; i++) {
//            test.add(i);
//        }
//
//
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < test.size(); i++) {
//            System.out.print(i);
//        }
//        System.out.println();
//        System.out.println("foi time  = [" + (System.currentTimeMillis() - start) + "]");
//
//        long start1 = System.currentTimeMillis();
//
//        test.forEach(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) {
//                System.out.print(integer);
//            }
//        });
//        System.out.println();
//        System.out.println("forEach time  = [" + (System.currentTimeMillis() - start1) + "]");
//
//
//        Iterator<Integer> iterator = test.iterator();
//
//        long start2 = System.currentTimeMillis();
//
//        while (iterator.hasNext()){
//            System.out.print(iterator.next());
//        }
//
//        System.out.println();
//        System.out.println("iterator time  = [" + (System.currentTimeMillis() - start2) + "]");
//
//        ArrayList<String> list3 = new ArrayList<String>();
//        for (int i = 0; i < 1000; i++) {
//            list3.add("sh" + i);
//        }
//
//        for (int i = 0; list3.iterator().hasNext(); i++) {
//            list3.remove(i);
//            System.out.println("秘密" + list3.get(i));
//        }

        User[] users = new User[]{
                new User(1, "seven", "seven@qq.com"),
                new User(2, "six", "six@qq.com"),
                new User(3, "ben", "ben@qq.com")};// 初始化对象数组

        User[] target = new User[users.length];// 新建一个目标对象数组

        System.arraycopy(users, 0, target, 0, users.length);// 实现复制

        System.out.println("源对象与目标对象的物理地址是否一样："  + (users[0] == target[0]) + "   "+ (users[0] == target[0] ? "浅复制" : "深复制"));  //浅复制

        target[0].setEmail("admin@sina.com");

        System.out.println("修改目标对象的属性值后源对象users：");

        for (User user : users) {
            System.out.println(user);
        }
    }

    public static class User {
        private Integer id;
        private String username;
        private String email;

        // 无参构造函数
        public User() {
        }

        // 有参的构造函数
        public User(Integer id, String username, String email) {
            super();
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "User [id=" + id + ", username=" + username + ", email=" + email + "]";
        }
    }

    private static class SubClass {
        int test;

        public SubClass(int test) {
            this.test = test;
        }

        @Override
        public String toString() {
            return "SubClass{" +
                    "test=" + test +
                    '}';
        }
    }

    private static class TClass {
        private int age;
        private InnerTClass innerTClass;

        private TClass() {
            //no instance
        }

        public TClass(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public InnerTClass getInnerTClass() {
            return innerTClass;
        }

        public void setInnerTClass(InnerTClass innerTClass) {
            this.innerTClass = innerTClass;
        }

        @Override
        public String toString() {
            return "TClass{" +
                    "age=" + age +
                    ", innerTClass=" + innerTClass +
                    '}';
        }
    }

    private static class InnerTClass {
        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "InnerTClass{" +
                    "age=" + age +
                    '}';
        }
    }
}
