# Overview

Utilities for working with various versions of Minecraft: Java Edition at runtime.

This project only works for Minecraft 1.17.1+ in the [Paper software](https://papermc.io/software/paper).

# Internals

To get the Minecraft version/OBC package version at the server runtime, we follow the best practice from this
[Paper announcement](https://forums.papermc.io/threads/important-dev-psa-future-removal-of-cb-package-relocation.1106/). That is, the code fetching the game version depends on the [Paper method](https://github.com/PaperMC/Paper/blob/ver/1.17.1/patches/api/0203-Expose-game-version.patch) that was
added in 2020 (and not depends on parsing the OBC package name), which is why this project only works for Minecraft
1.17.1+ in the Paper software.

# Class overview

- `Indexing`
    - Utility class to builds an index for given values
- `SnapshotVersion`
    - Encapsulates a snapshot version of Minecraft.
- `MinecraftVersion`
    - Encapsulates a release version of Minecraft
- `MinecraftVersions`
    - Utility class which holds some common versions of Minecraft
- `PackageVersion`
    - Enumeration of CraftBukkit package versions

# Basic usage

You will be mostly using the `MinecraftVersions` class to check the Minecraft version at runtime, and the
`PackageVersion` class to check the OBC package version at runtime.
