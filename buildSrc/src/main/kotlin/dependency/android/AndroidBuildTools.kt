package dependency.android

import dependency.Dependency

object AndroidGradlePlugin : Dependency {
    override val group = "com.android.tools.build"
    override val artifact = "gradle"
    override val version = "3.5.2"
}