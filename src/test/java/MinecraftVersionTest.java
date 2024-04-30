import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import cc.mewcraft.version.MinecraftVersion;
import cc.mewcraft.version.NmsVersion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MinecraftVersionTest {
    private ServerMock server;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testMinecraftVersion1() {
        assertDoesNotThrow(MinecraftVersion::getRuntimeVersion);
    }

    @Test
    public void testPackageVersion1() {
        assertDoesNotThrow(NmsVersion::runtimeVersion);
    }

    @Test
    public void testPackageVersion2() {
        assertNotEquals(NmsVersion.runtimeVersion(), NmsVersion.NONE);
    }
}
