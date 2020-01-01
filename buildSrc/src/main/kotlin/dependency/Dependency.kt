package dependency

interface Dependency {
    val group: String
    val artifact: String
    val version: String

    /**
     * Builds the dependency notation.
     */
    operator fun invoke(): String = "$group:$artifact:$version"

    /**
     * Builds the dependency notation with a given [module].
     *
     * @param module simple name of the Kotlin module, for example "reflect"
     */
    operator fun invoke(module: String): String = "$group:$artifact-$module:$version"
}