/*
 * This file is part of shadow-bukkit, licensed under the MIT License.
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

import com.google.common.collect.ImmutableSet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An enumeration of NMS versions.
 * <p>
 * A note on what this enum exactly represents:
 * <p>
 * <ul>
 *     <li>From {@link #v1_17_R1} to {@link #v1_20_R3}, this enum is a 1:1 representation of the CB package version.</li>
 *     <li>From {@link #v1_20_R4} and onwards, this enum represents a <i>significant</i> version of the server internals.</li>
 * </ul>
 * By <i>significant</i>, we mean that the version is so different from the previous that it's necessary to
 * introduce a new entry of {@link NmsVersion} to allow our usage of server internals to continue working
 * as expected.
 */
@DefaultQualifier(NonNull.class)
public enum NmsVersion {
    NONE(false),
    v1_17_R1(true, MinecraftVersions.v1_17, MinecraftVersions.v1_17_1),
    v1_18_R2(true, MinecraftVersions.v1_18_2),
    v1_19_R3(true, MinecraftVersions.v1_19_4),
    v1_20_R3(true, MinecraftVersions.v1_20_3, MinecraftVersions.v1_20_4),

    // Starting from Paper 1.20.5, there's no OBC package relocation anymore,
    // and the server mappings are changed from spigot one to mojang one. See the post for details:
    // https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/
    //
    // Also, the meaning of the enum name has changed: it has become the version of server internals,
    // not 1:1 representation of the OBC package version, and has nothing to do with the NMS version
    // introduced by Spigot (i.e., v1_20_R1, v1_20_R2, etc.)
    //
    // Why? Because there have been many cases where mojang have changed a bunch of the internals
    // which caused a few methods to be renamed and Spigot didn't change the number, causing plugins
    // to break in unexpected manners.
    //
    // New NmsVersion entries will be introduced if it's necessary for our usage of server internals to
    // continue working correctly. Otherwise, we don't have to introduce a new entry for every single release
    // of Minecraft version.

    v1_20_R4(false, MinecraftVersions.v1_20_5, MinecraftVersions.v1_20_6),
    v1_21_R1(false, MinecraftVersions.v1_21, MinecraftVersions.v1_21_1)
    ;

    /**
     * The {@link MinecraftVersion}s that used this {@link NmsVersion}.
     */
    private final Set<MinecraftVersion> minecraftVersions;
    /**
     * A mark that indicates whether the OBC package is relocated or not.
     */
    private final boolean isObcRelocated;

    /**
     * The nms prefix for 1.17+ (excludes version component)
     */
    private static final String NMS = "net.minecraft.";

    /**
     * The obc prefix (without the version component)
     */
    private static final String OBC = "org.bukkit.craftbukkit";

    private final String nmsPrefix;
    private final String obcPrefix;

    NmsVersion(boolean isObcRelocated, MinecraftVersion... minecraftVersions) {
        this.isObcRelocated = isObcRelocated;
        this.minecraftVersions = ImmutableSet.copyOf(minecraftVersions);
        this.nmsPrefix = NMS;
        this.obcPrefix = OBC + getPackageComponent();
    }

    private String getPackageComponent() {
        return this.isObcRelocated ? "." + name() + "." : ".";
    }

    /**
     * Gets the {@link MinecraftVersion}s that used this {@link NmsVersion}.
     *
     * @return the minecraft versions for this obc package version
     */
    public Set<MinecraftVersion> getMinecraftVersions() {
        return this.minecraftVersions;
    }

    /**
     * Prepends the versioned NMS prefix to the given class name
     *
     * @param className the name of the class
     * @return the full class name
     */
    public String nms(String className) {
        Objects.requireNonNull(className, "className");
        return this.nmsPrefix + className;
    }

    /**
     * Prepends the versioned NMS prefix to the given class name
     *
     * @param className the name of the class
     * @return the class represented by the full class name
     */
    public Class<?> nmsClass(String className) throws ClassNotFoundException {
        return Class.forName(nms(className));
    }

    /**
     * Prepends the versioned OBC prefix to the given class name
     *
     * @param className the name of the class
     * @return the full class name
     */
    public String obc(String className) {
        Objects.requireNonNull(className, "className");
        return this.obcPrefix + className;
    }

    /**
     * Prepends the versioned OBC prefix to the given class name
     *
     * @param className the name of the class
     * @return the class represented by the full class name
     */
    public Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(obc(className));
    }

    private void checkComparable(NmsVersion other) {
        Objects.requireNonNull(other, "other");
        if (this == NONE) {
            throw new IllegalArgumentException("this cannot be NONE");
        }
        if (other == NONE) {
            throw new IllegalArgumentException("other cannot be NONE");
        }
    }

    /**
     * Gets if this version comes before the {@code other} version.
     *
     * @param other the other version
     * @return if it comes before
     */
    public boolean isBefore(NmsVersion other) {
        checkComparable(other);
        return this.ordinal() < other.ordinal();
    }

    /**
     * Gets if this version comes after the {@code other} version.
     *
     * @param other the other version
     * @return if it comes after
     */
    public boolean isAfter(NmsVersion other) {
        checkComparable(other);
        return this.ordinal() > other.ordinal();
    }

    /**
     * Gets if this version is the same as or comes before the {@code other} version.
     *
     * @param other the other version
     * @return if it comes before or is the same
     */
    public boolean isBeforeOrEq(NmsVersion other) {
        checkComparable(other);
        return this.ordinal() <= other.ordinal();
    }

    /**
     * Gets if this version is the same as or comes after the {@code other} version.
     *
     * @param other the other version
     * @return if it comes after or is the same
     */
    public boolean isAfterOrEq(NmsVersion other) {
        checkComparable(other);
        return this.ordinal() >= other.ordinal();
    }

    private static final Map<MinecraftVersion, NmsVersion> MC_TO_PACKAGE = Indexing.buildMultiple(values(), NmsVersion::getMinecraftVersions);

    /**
     * Gets the {@link NmsVersion} for the given {@link MinecraftVersion}.
     *
     * @param minecraftVersion the minecraft version
     * @return the obc package version
     */
    public static NmsVersion forMinecraftVersion(MinecraftVersion minecraftVersion) {
        NmsVersion nmsVersion = MC_TO_PACKAGE.get(minecraftVersion);
        if (nmsVersion == null) {
            return NONE;
        }
        return nmsVersion;
    }

    private static final NmsVersion RUNTIME_VERSION;

    static {
        // We are following the best practice from the Paper announcement:
        // https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/
        // In a nutshell, we decide the runtime OBC package version by the
        // runtime Minecraft version, not by parsing the OBC package name.
        MinecraftVersion runtimeVersion = MinecraftVersion.getRuntimeVersion();
        RUNTIME_VERSION = NmsVersion.forMinecraftVersion(runtimeVersion);
    }

    /**
     * Gets the package version for the current runtime server instance.
     *
     * @return the package version of the current runtime
     */
    public static NmsVersion runtimeVersion() {
        return RUNTIME_VERSION;
    }
}