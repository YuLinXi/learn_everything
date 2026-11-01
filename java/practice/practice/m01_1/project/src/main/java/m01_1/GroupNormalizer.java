package m01_1;

public class GroupNormalizer {
    public static GroupSettings normalize(GroupInput input) {
        // TODO：按 Q01 约定补齐输入校验和默认值处理。
        GroupSettings result = new GroupSettings();
        result.entityId = input.entityId;
        result.enabled = input.enabled;
        return result;
    }
}
