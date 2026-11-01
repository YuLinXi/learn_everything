package m00_2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class ValidationDemoTest {
    // 导师提供的示例：断言期望结果与实际结果一致。
    @Test
    void trimsLeadingAndTrailingSpaces() {
        assertEquals("基本信息", ValidationDemo.requireGroupName("  基本信息  "));
    }

    @Test
    void preservesInternalSpaces() {
        assertEquals("基本 信息", ValidationDemo.requireGroupName("  基本 信息  "));
    }

    @Test
    void rejectsNullName() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> ValidationDemo.requireGroupName(null)
        );
        assertEquals("group name must not be blank", error.getMessage());
    }

    @Test
    void rejectsEmptyName() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> ValidationDemo.requireGroupName("")
        );
        assertEquals("group name must not be blank", error.getMessage());
    }

    @Test
    void rejectsSpacesOnlyName() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> ValidationDemo.requireGroupName("   ")
        );
        assertEquals("group name must not be blank", error.getMessage());
    }
}
