package day5;

import java.util.LinkedList;
import java.util.Queue;

/***
 * 猫狗队列
 *
 * 实现一种猫狗队列 用现有的用户结构 要求
 *
 * 1. 用户可以调用 add 方法将 cat 或者 dog 类放入队列中
 * 2. 用户可以调用 pollAll 方法，将队列中所有的实例按照队列的先后顺序依次弹出（每次出队一个）
 * 3. 用户可以调用 pollDog 方法，将队列的中 Dog 类的实例按照进队列的先后顺序依次弹出
 * 4. 用户可以调用 pollCat 方法，将队列中的 Cat 类的实例按照进队列的先后顺序依次弹出
 * 5. 用户可以调用 isEmpty 方法，判断队列中是否还有 Dog 或 cat 实例
 * 6. 用户可以调用 isDogEmpty 方法，判断队列中是否还有 Dog 实例
 * 7. 用户可以调用 isCatEmpty 方法，判断队列中是否还有 Cat 实例
 */
public class zcy4 {


    private static class DogCatQueue {
        private Queue<PetEnterQueue> catQueue;
        private Queue<PetEnterQueue> dogQueue;
        private long count;

        public DogCatQueue() {
            catQueue = new LinkedList<>();
            dogQueue = new LinkedList<>();
            count = 0;
        }

        public void add(Pet pet) {
            if (pet.getType().equals("Dog")) {
                dogQueue.add(new PetEnterQueue(count++, pet));
            } else if (pet.getType().equals("Cat")) {
                catQueue.add(new PetEnterQueue(count++, pet));
            } else {
                throw new RuntimeException("err! not a dog or a cat");
            }
        }

        public Pet pollAll() {
            if (!dogQueue.isEmpty() && !catQueue.isEmpty()) {
                if (dogQueue.peek().time < catQueue.peek().time) {
                    return dogQueue.poll().getPet();
                } else {
                    return catQueue.poll().getPet();
                }
            } else if (!dogQueue.isEmpty()) {
                return dogQueue.poll().getPet();
            } else if (!catQueue.isEmpty()) {
                return catQueue.poll().getPet();
            } else {
                throw new RuntimeException("err! you Queue is Empty");
            }
        }

        public Pet pollDog() {
            if (!dogQueue.isEmpty()) {
                return dogQueue.poll().getPet();
            } else {
                throw new RuntimeException("err! There is not dog in Queue");
            }
        }

        public Pet pollCat() {
            if (!catQueue.isEmpty()) {
                return catQueue.poll().getPet();
            } else {
                throw new RuntimeException("err! There is not cat in Queue");
            }
        }

        public boolean isEmpty() {
            return catQueue.isEmpty() && dogQueue.isEmpty();
        }

        public boolean isDogEmpty() {
            return dogQueue.isEmpty();
        }

        public boolean isCatEmpty() {
            return catQueue.isEmpty();
        }
    }

    /***
     * 构建用户输入的对象 time 为输入的时间（序号）
     */
    private static class PetEnterQueue {
        private long time;
        private Pet pet;

        public PetEnterQueue(long time, Pet pet) {
            this.time = time;
            this.pet = pet;
        }

        public long getTime() {
            return time;
        }

        public Pet getPet() {
            return pet;
        }

        public String getEnterPetType() {
            return this.pet.type;
        }
    }

    /*** 题目给出的实体结构 （不能改变）*/
    private static class Pet {
        private String type;

        public Pet(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    private static class Cat extends Pet {
        public Cat() {
            super("Cat");
        }
    }

    private static class Dog extends Pet {
        public Dog() {
            super("Dog");
        }
    }
}
