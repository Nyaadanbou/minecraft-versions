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
 * An enumeration of CraftBukkit package versions.
 */
@DefaultQualifier(NonNull.class)
public enum PackageVersion {
    NONE {
        @Override protected String getPackageComponent() {
            return ".";
        }
    },
    v1_17_R1(
            MinecraftVersions.v1_17,
            MinecraftVersions.v1_17_1
    ),
    v1_18_R2(
            MinecraftVersions.v1_18_2
    ),
    v1_19_R3(
            MinecraftVersions.v1_19_4
    ),
    v1_20_R3(
            MinecraftVersions.v1_20_3,
            MinecraftVersions.v1_20_4
    ),
    // No more entries after v1_20_R3 since there's no OBC package relocation anymore, see the post:
    // https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/
    NO_RELOCATION(
            // We will add more Minecraft versions here in the future
            // as we expect that new versions are no longer relocated.
            MinecraftVersions.v1_20_5,
            MinecraftVersions.v1_20_6
    ) {
        @Override protected String getPackageComponent() {
            return ".";
        }
    };

    /**
     * The {@link MinecraftVersion}s that used this {@link PackageVersion}.
     */
    protected final Set<MinecraftVersion> minecraftVersions;

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

    PackageVersion(MinecraftVersion... minecraftVersions) {
        this.minecraftVersions = ImmutableSet.copyOf(minecraftVersions);
        this.nmsPrefix = NMS;
        this.obcPrefix = OBC + getPackageComponent();
    }

    protected String getPackageComponent() {
        return "." + name() + ".";
    }

    /**
     * Gets the {@link MinecraftVersion}s that used this {@link PackageVersion}.
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

    private void checkComparable(PackageVersion other) {
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
    public boolean isBefore(PackageVersion other) {
        checkComparable(other);
        return this.ordinal() < other.ordinal();
    }

    /**
     * Gets if this version comes after the {@code other} version.
     *
     * @param other the other version
     * @return if it comes after
     */
    public boolean isAfter(PackageVersion other) {
        checkComparable(other);
        return this.ordinal() > other.ordinal();
    }

    /**
     * Gets if this version is the same as or comes before the {@code other} version.
     *
     * @param other the other version
     * @return if it comes before or is the same
     */
    public boolean isBeforeOrEq(PackageVersion other) {
        checkComparable(other);
        return this.ordinal() <= other.ordinal();
    }

    /**
     * Gets if this version is the same as or comes after the {@code other} version.
     *
     * @param other the other version
     * @return if it comes after or is the same
     */
    public boolean isAfterOrEq(PackageVersion other) {
        checkComparable(other);
        return this.ordinal() >= other.ordinal();
    }

    private static final Map<MinecraftVersion, PackageVersion> MC_TO_PACKAGE = Indexing.buildMultiple(values(), PackageVersion::getMinecraftVersions);

    /**
     * Gets the {@link PackageVersion} for the given {@link MinecraftVersion}.
     *
     * @param minecraftVersion the minecraft version
     * @return the obc package version
     */
    public static PackageVersion forMinecraftVersion(MinecraftVersion minecraftVersion) {
        PackageVersion packageVersion = MC_TO_PACKAGE.get(minecraftVersion);
        if (packageVersion == null) {
            return NONE;
        }
        return packageVersion;
    }

    private static final PackageVersion RUNTIME_VERSION;

    static {
        // We are following the best practice from the Paper announcement:
        // https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/
        // In a nutshell, we decide the runtime OBC package version by the
        // runtime Minecraft version, not by parsing the OBC package name.
        MinecraftVersion runtimeVersion = MinecraftVersion.getRuntimeVersion();
        RUNTIME_VERSION = PackageVersion.forMinecraftVersion(runtimeVersion);
    }

    /**
     * Gets the package version for the current runtime server instance.
     *
     * @return the package version of the current runtime
     */
    public static PackageVersion runtimeVersion() {
        return RUNTIME_VERSION;
    }
}