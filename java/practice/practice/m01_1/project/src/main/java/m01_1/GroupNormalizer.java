package m01_1;

public class GroupNormalizer {
    public static GroupSettings normalize(GroupInput input) {
        // TODO：按 Q01 约定补齐输入校验和默认值处理。

        if (input == null) {
            throw new IllegalArgumentException("input must not be null");
        }

        if (input.entityId == null || input.entityId <= 0) {
            throw new IllegalArgumentException("entityId must be positive");
        }

        GroupSettings result = new GroupSettings();
        result.entityId = input.entityId;
        result.enabled = input.enabled == null ? true : input.enabled;
        return result;
    }
}
