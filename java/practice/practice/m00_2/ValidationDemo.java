package m00_2;

public class ValidationDemo {
    public static String requireGroupName(String name) {
        // TODO：按 Q02 要求补齐名称校验与前后空格处理。
        String result = "";
        if (name == null || name.isEmpty() || (result = name.trim()).isEmpty()) {
            throw new IllegalArgumentException("group name must not be blank");
        }

        return result;
    }

    public static void main(String[] args) {
        String name = args.length > 0 ? args[0] : null;
        System.out.println(requireGroupName(name));
    }
}
