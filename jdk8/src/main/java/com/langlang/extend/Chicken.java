package com.langlang.extend;

public class Chicken extends Animal {

    public void method(Animal a) {
        System.out.println("子类方法执行---" );
        if (a instanceof Chicken) { // 子类对象赋值给父类, 这里可以判断是不是子类类型
            Chicken d = (Chicken) a;    //向下转型
            System.out.println("向下转换执行成功");
        }
    }

    public void fly() {
        System.out.println("fly...");
    }


    public static void main(String[] args) {
        Animal animal = new Animal();
        Animal chickenParam = new Chicken();
        Chicken chicken = new Chicken();
        chicken.eat();
        chicken.fly();
        chicken.method(animal);
        chicken.method(chickenParam);

        animal.eat();
        animal.fly();
//        Chicken animal1 = (Chicken) animal; // 父类强制转换成子类 报错: ClassCastException




    }
}
