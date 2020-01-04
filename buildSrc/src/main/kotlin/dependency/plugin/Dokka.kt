package dependency.plugin

import dependency.Dependency

object Dokka: Dependency {
    override val group = "org.jetbrains.dokka"
    override val artifact = "dokka-gradle-plugin"
    override val version = "0.10.0"
}
