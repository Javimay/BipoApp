# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Javier\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#### -- Picasso --
 -dontwarn com.squareup.picasso.**

 #### -- OkHttp --

 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --

 -dontwarn org.apache.commons.logging.**

 -ignorewarnings
#### -- New changes --
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -verbose
 -keepclasseswithmembernames class * {
     native <methods>;
 }
 -keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
 }
 -dontwarn javax.xml.stream.events.**

 # Okio
 -keep class sun.misc.Unsafe { *; }
 -dontwarn java.nio.file.*
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 -dontwarn okio.**

 # Retrofit
 -dontwarn retrofit2.Platform$Java8
 -dontwarn retrofit.Platform$Java8

 # Retrofit
 -dontwarn retrofit2.**
 -dontwarn org.codehaus.mojo.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions
 -keepattributes *Annotation*

 -keepattributes RuntimeVisibleAnnotations
 -keepattributes RuntimeInvisibleAnnotations
 -keepattributes RuntimeVisibleParameterAnnotations
 -keepattributes RuntimeInvisibleParameterAnnotations

 -keepattributes EnclosingMethod

 -keepclasseswithmembers class * {
     @retrofit2.* <methods>;
 }

 -keepclasseswithmembers interface * {
     @retrofit2.* <methods>;
 }