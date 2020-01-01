package dependency.lib

import dependency.Dependency

object DiskLruCache: Dependency {
    override val group = "com.jakewharton"
    override val artifact = "disklrucache"
    override val version = "2.0.2"
}