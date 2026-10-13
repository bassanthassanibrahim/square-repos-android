# Retrofit, OkHttp, Room, Hilt and kotlinx.serialization ship their own consumer rules.
# Keep the generic signatures Retrofit needs to read suspend function return types.
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
