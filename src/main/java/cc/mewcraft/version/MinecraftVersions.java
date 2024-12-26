/*
 * This file is part of helper, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package cc.mewcraft.version;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * Utility class which holds some common versions of Minecraft.
 *
 * @author Kristian (ProtocolLib)
 */
@SuppressWarnings({"UnreachableCode", "ConstantValue"})
@DefaultQualifier(NonNull.class)
public final class MinecraftVersions {
    public static final MinecraftVersion v1_21_4 = MinecraftVersion.parse("1.21.4");
    public static final MinecraftVersion v1_21_3 = MinecraftVersion.parse("1.21.3");
    public static final MinecraftVersion v1_21_2 = MinecraftVersion.parse("1.21.2");
    public static final MinecraftVersion v1_21_1 = MinecraftVersion.parse("1.21.1");
    public static final MinecraftVersion v1_21 = MinecraftVersion.parse("1.21");

    public static final MinecraftVersion v1_20_6 = MinecraftVersion.parse("1.20.6");
    public static final MinecraftVersion v1_20_5 = MinecraftVersion.parse("1.20.5");
    public static final MinecraftVersion v1_20_4 = MinecraftVersion.parse("1.20.4");
    public static final MinecraftVersion v1_20_3 = MinecraftVersion.parse("1.20.3");
    public static final MinecraftVersion v1_20 = MinecraftVersion.parse("1.20");

    public static final MinecraftVersion v1_19_4 = MinecraftVersion.parse("1.19.4");
    public static final MinecraftVersion v1_19 = MinecraftVersion.parse("1.19");

    public static final MinecraftVersion v1_18_2 = MinecraftVersion.parse("1.18.2");
    public static final MinecraftVersion v1_18 = MinecraftVersion.parse("1.18");

    public static final MinecraftVersion v1_17_1 = MinecraftVersion.parse("1.17.1");
    public static final MinecraftVersion v1_17 = MinecraftVersion.parse("1.17");

    /**
     * The Minecraft version of the runtime.
     */
    public static final MinecraftVersion RUNTIME_VERSION;

    static {
        Server server = Bukkit.getServer();

        if (server != null) { // in test environment, this could be null
            RUNTIME_VERSION = MinecraftVersion.parse(Bukkit.getMinecraftVersion());
        } else {
            // in test environment, we fall back to the latest Minecraft version we know
            RUNTIME_VERSION = MinecraftVersion.parse(MinecraftVersion.NEWEST_MINECRAFT_VERSION);
        }
    }

    private MinecraftVersions() {
        throw new UnsupportedOperationException();
    }
}
