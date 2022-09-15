# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.ekenya.rnd.cargillbuyer.data.** { *; }
-keep class com.ekenya.rnd.cargillbuyer.data.responses.** { *; }

-keep class androidx.datastore.*.** {*;}
-keep class res.drawable.**{*;}

#-keep public class com.google.gson.** {
#  public protected *;
#}

-keep class retrofit.** { *; }

#-keep class com.eclectics.faulumfb.exodus.models** { *; }

##---------------Begin: proguard configuration for Gson  ----------

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}


##---------------End: proguard configuration for Gson  ----------

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

 -keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    public <init>(...);
 }

  -keep class cn.pedant.SweetAlert.Rotate3dAnimation {
     public <init>(...);
  }
  -keep class cn.pedant.** { *; }

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#-keep assets/io/michaelrocks/libphonenumber/android/**

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.airbnb.lottie.samples.** { *; }