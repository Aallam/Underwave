package dependency.plugin

import dependency.Dependency

object Bintray : Dependency {
    override val group = "com.jfrog.bintray.gradle"
    override val artifact = "gradle-bintray-plugin"
    override val version = "1.8.4"
}