import java.lang.annotation.*;
import java.lang.reflect.*;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class Annotation {
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {

//        enum Color {BULE, RED, GREEN}

        String value() default "";

        String[] values();

        int id() default -1;

        int[] ids();

//        Color testEnum() default Color.BULE;

//        Color[] testEnums();

        Class className();


    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface FruitName {
        String value();//static final  不可以用
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface FruitColor {//可以使用所有的权限修饰符

        enum Color {BULE, RED, GREEN}

        Color fruitColor();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FruitProvider {
        public int id();

        String name();//注解的变量只可以使用 public 或 default 修饰？

        String address();
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InheritedAnnotation {
    }


    @InheritedAnnotation
    public class Apple {

        @FruitName(value = "Apple")
        public String name;

        @FruitColor(fruitColor = FruitColor.Color.RED)
        private String appColor;

        @FruitProvider(id = 1, name = "山东", address = "山东富士")
        private String appleProvider;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAppColor() {
            return appColor;
        }

        public void setAppColor(String appColor) {
            this.appColor = appColor;
        }

        public String getAppleProvider() {
            return appleProvider;
        }

        public void setAppleProvider(String appleProvider) {
            this.appleProvider = appleProvider;
        }
    }

    public static void getFruitInfo(Class<?> clazz) {
        try {
            String fruitName = "水果名称：";

            Field field = clazz.getDeclaredField("name");
            java.lang.annotation.Annotation[] annotations = field.getAnnotations();
            System.out.println("annotations = " + Arrays.toString(annotations));
            if (field.isAnnotationPresent(FruitName.class)) {
                FruitName fruitNameAnno = field.getAnnotation(FruitName.class);
                fruitName = fruitName + fruitNameAnno.value();
                System.out.println(fruitName);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static class FruitInfoProccessor {

        public static void getFruitInfo(Class<?> clazz) {
            String fruitName = "水果名称：";
            String fruitColor = "水果颜色:";
            String fruitProvider = "供应商信息：";

            Field[] fields = clazz.getDeclaredFields();

//            System.out.println(Arrays.toString(fields));//[Ljava.lang.reflect.Field;@511d50c0 不同于普通数组的打印

            for (Field field : fields) {
                if (field.isAnnotationPresent(FruitName.class)) {
                    java.lang.annotation.Annotation[] annotations = field.getAnnotations();
                    System.out.println("annotations = " + Arrays.toString(annotations));

                    FruitName fruitNameAnno = field.getAnnotation(FruitName.class);
                    fruitName = fruitName + fruitNameAnno.value();
                    System.out.println(fruitName);
                } else if (field.isAnnotationPresent(FruitProvider.class)) {
                    FruitProvider fruitProviderAnno = field.getAnnotation(FruitProvider.class);
                    fruitProvider = fruitProvider + fruitProviderAnno.name() + fruitProviderAnno.id() + fruitProviderAnno.address();
                    System.out.println(fruitProvider);
                } else if (field.isAnnotationPresent(FruitColor.class)) {
                    FruitColor fruitColorAnno = field.getAnnotation(FruitColor.class);
                    fruitColor = fruitColor + fruitColorAnno.fruitColor();
                    System.out.println(fruitColor);
                }
            }
        }

        public static void getFruitReflection(Class<?> clazz) {



//            Type[] genericInterfaces = clazz.getGenericInterfaces();
//            Type genericSuperclass = clazz.getGenericSuperclass();
//
//            System.out.println("genericSuperclass = " + genericSuperclass);
//            System.out.println("genericInterfaces = " + Arrays.toString(genericInterfaces));

            java.lang.annotation.Annotation[] annotations = clazz.getAnnotations();
            System.out.println("annotations = " + Arrays.toString(annotations));

            java.lang.annotation.Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
            System.out.println("declaredAnnotations = " + Arrays.toString(declaredAnnotations));

            FruitColor[] annotationsByType = clazz.getAnnotationsByType(FruitColor.class);
            System.out.println("annotationsByType = " + Arrays.toString(annotationsByType));

            FruitColor annotation = clazz.getAnnotation(FruitColor.class);
            System.out.println("annotation = " + annotation);

//            String canonicalName = clazz.getCanonicalName();
//            System.out.println("canonicalName = " + canonicalName);
//            Class<?> componentType = clazz.getComponentType();
//            System.out.println("componentType = " + componentType);

//            ProtectionDomain protectionDomain = clazz.getProtectionDomain();
//            System.out.println("protectionDomain = " + protectionDomain);
//


        }
    }

    public static void main(String[] args) {
//        FruitInfoProccessor.getFruitInfo(Apple.class);
        getFruitInfo(Apple.class);

    }
}
