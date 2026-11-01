package m01_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// 导师提供的业务约定测试；学生本次只修改 normalize 的实现。
class GroupNormalizerTest {
    @Test
    void rejectsMissingInput() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> GroupNormalizer.normalize(null)
        );
        assertEquals("input must not be null", error.getMessage());
    }

    @Test
    void rejectsMissingEntityId() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> GroupNormalizer.normalize(input(null, true))
        );
        assertEquals("entityId must be positive", error.getMessage());
    }

    @Test
    void rejectsNonPositiveEntityIds() {
        for (long id : new long[] {0L, -1L}) {
            IllegalArgumentException error = assertThrows(
                    IllegalArgumentException.class,
                    () -> GroupNormalizer.normalize(input(id, true))
            );
            assertEquals("entityId must be positive", error.getMessage());
        }
    }

    @Test
    void defaultsMissingEnabledToTrueWithoutChangingInput() {
        GroupInput request = input(42L, null);
        GroupSettings actual = GroupNormalizer.normalize(request);
        assertEquals(42L, actual.entityId);
        assertTrue(actual.enabled);
        assertNull(request.enabled);
        assertEquals(Long.valueOf(42L), request.entityId);
    }

    @Test
    void preservesExplicitFalse() {
        GroupSettings actual = GroupNormalizer.normalize(input(1L, false));
        assertEquals(1L, actual.entityId);
        assertFalse(actual.enabled);
    }

    @Test
    void preservesTrueAndLargeEntityId() {
        GroupSettings actual = GroupNormalizer.normalize(input(Long.MAX_VALUE, true));
        assertEquals(Long.MAX_VALUE, actual.entityId);
        assertTrue(actual.enabled);
    }

    private static GroupInput input(Long entityId, Boolean enabled) {
        GroupInput result = new GroupInput();
        result.entityId = entityId;
        result.enabled = enabled;
        return result;
    }
}
