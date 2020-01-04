[underwave](../../index.md) / [com.aallam.underwave](../index.md) / [Underwave](./index.md)

# Underwave

(android) `class Underwave`

A singleton to present a simple static interface for building requests.

### Functions

| Name | Summary |
|---|---|
| (android) [clear](clear.md) | Clears as much memory and as possible and disk cache.`fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (android) [insert](insert.md) | A suspendable method to load an image to an [ImageView](https://developer.android.com/reference/android/widget/ImageView.html) using the caller's coroutine scope.`suspend fun insert(imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, imageView: ImageView): `[`Request`](../../com.aallam.underwave.load/-request/index.md) |
| (android) [load](load.md) | Load [imageUrl](load.md#com.aallam.underwave.Underwave$load(kotlin.String, android.widget.ImageView)/imageUrl) into [imageView](load.md#com.aallam.underwave.Underwave$load(kotlin.String, android.widget.ImageView)/imageView).`fun load(imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, imageView: ImageView): `[`Request`](../../com.aallam.underwave.load/-request/index.md) |

### Companion Object Functions

| Name | Summary |
|---|---|
| (android) [debugMode](debug-mode.md) | Enable or disable Underwave's debug mode.`fun debugMode(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`, logger: `[`Logger`](../../com.aallam.underwave.log/-logger/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| (android) [with](with.md) | Begin a load with Underwave by passing in a context.`fun with(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Underwave`](./index.md) |
