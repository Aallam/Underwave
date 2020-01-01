package dependency.lib

import dependency.Dependency

object Spotless: Dependency {
    override val group = "com.diffplug.spotless"
    override val artifact = "spotless-plugin-gradle"
    override val version = "3.24.2"
}
