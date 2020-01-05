import dependency.Dependency

object Library : Dependency {
    override val group = "com.aallam.underwave"
    override val artifact = "underwave"
    override val version = "0.0.1"

    // Maven
    val packageName = "$group:$artifact-android"
}
