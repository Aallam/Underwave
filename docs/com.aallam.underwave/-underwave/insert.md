[underwave](../../index.md) / [com.aallam.underwave](../index.md) / [Underwave](index.md) / [insert](./insert.md)

# insert

(android) `suspend fun insert(imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, imageView: ImageView): `[`Request`](../../com.aallam.underwave.load/-request/index.md)

A suspendable method to load an image to an [ImageView](https://developer.android.com/reference/android/widget/ImageView.html) using the caller's coroutine scope.

### Parameters

`imageUrl` - image url to be loaded.

`imageView` - image view to load the image into.

**Return**
object representing the requested operation.

