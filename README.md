Underwave: Background Image-Loading Library
============================================

[![Build Status](https://travis-ci.com/Aallam/Underwave.svg?token=nP2QNDvP3CyhgqiTDFz2&branch=master)](https://travis-ci.com/Aallam/Underwave) 
[![Documentation](https://img.shields.io/static/v1?label=docs&message=up&color=green)](https://mouaad.aallam.com/Underwave) 
[![Download](https://api.bintray.com/packages/aallam/maven/com.aallam.underwave%3Aunderwave-android/images/download.svg)](https://bintray.com/aallam/maven/com.aallam.underwave%3Aunderwave-android/_latestVersion) 


**Underwave** is a simple library to asynchronously download and load images to views, with memory 
and disk caching, resource pooling, and request cancellation support.

**Underwave** can be used to load images to simple views or lists of views with efficiency.

<p align="center">
  <img width="200" height="200" src="static/the_wave.png">
</p>

Usage
-----
To add **Underwave** to your project, add the following line to your `build.gradle`:
```groovy
implementation "com.aallam.underwave:underwave-android:0.1.0"
```

Quick Start
-----------
**Underwave** API is simple and straightforward:

### Simple
Use `load` function to load an image to an [ImageView][1], :
````kotlin
Underwave.with(context).load("https://www.example.com/image.jpg", imageView)
````
````kotlin
imageView.load("https://www.example.com/image.jpg")
````

### Scoped
Use `insert` function to take advantage of a custom coroutine scope (UI scope for example):
````kotlin
uiScope.launch {
    Underwave.with(context).insert("https://www.example.com/image.jpg", imageView)
}
````
````kotlin
uiScope.launch {
    imageView.insert("https://www.example.com/image.jpg")
}
````

### Cancellation
Call `cancel()` on `Request` object to cancel the loading operation:
````kotlin
val request = Underwave.with(context).load("https://www.example.com/image.jpg", imageView)
//...
request.cancel()
````
````kotlin
val request = imageView.insert("https://www.example.com/image.jpg")
//...
request.cancel()
````
Requirements
------------
* Min SDK 21
* Compile SDK: 29+
* AndroidX
* Java 8+

R8 / Proguard
-------------
If you use Proguard, you may need to add rules for [coroutines][2].

License
-------

Underwave is distributed under the terms of the Apache License (Version 2.0). 
See [LICENSE](LICENSE) for details.

[1]: https://developer.android.com/reference/android/widget/ImageView
[2]: https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/resources/META-INF/proguard/coroutines.pro
