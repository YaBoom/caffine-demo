import java.util.List;
import java.util.Optional;

public class Test {
    public static void main(String[] args) {
        List<MyObject> myObjects = List.of(
            new MyObject(1, "Object1","1"),
            new MyObject(2, "Object2","2"),
            new MyObject(3, "Object3","3")
        );

        // 假设我们要根据id过滤出对象
        int targetId = 2;
        Optional<MyObject> result = myObjects.stream()
            .filter(obj -> obj.getId() == targetId)
            .findFirst();

        result.ifPresentOrElse(
            obj -> System.out.println("Found object: " + obj.getName()),
            () -> System.out.println("Object not found")
        );
       int id_op =  result.ifPresent(
            obj -> Integer.valueOf(obj.getStrId()).intValue());
        );

        System.out.println(id_op);
        
    }
}

class MyObject {
    private int id;
    private String name;
    private String strId;

    public MyObject(int id, String name,String strId) {
        this.id = id;
        this.name = name;
        this.strId = strId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStrId() {
        return strId;
    }
}