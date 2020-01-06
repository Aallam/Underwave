[underwave](../../index.md) / [com.aallam.underwave.log](../index.md) / [Logger](./index.md)

# Logger

(android) `interface Logger`

May be used to create a custom logging solution to override the [Default](-default/index.md) behaviour.

### Types

| Name | Summary |
|---|---|
| (android) [Default](-default/index.md) | Default implementation of [Logger](./index.md) which uses [Log.println](https://developer.android.com/reference/android/util/Log.html#println(int, java.lang.String, java.lang.String)) to log the messages.`object Default : `[`Logger`](./index.md) |

### Functions

| Name | Summary |
|---|---|
| (android) [log](log.md) | Pass the log details off to the [Logger](./index.md) implementation.`abstract fun log(priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
