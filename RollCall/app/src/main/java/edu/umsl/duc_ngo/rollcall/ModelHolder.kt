package edu.umsl.duc_ngo.rollcall

import java.lang.ref.WeakReference
import kotlin.reflect.KClass

class ModelHolder private constructor(){
    private val hashMap = HashMap<String, WeakReference<Any?>>()

    companion object {
        @JvmStatic
        val instance = ModelHolder()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any>get(class_type: KClass<T>): T?{
        val weakReference = hashMap[class_type.java.toString()]
        return weakReference?.get() as? T
    }

    fun <T: Any>set(class_instance: T?) {
        hashMap[class_instance?.javaClass.toString()] = WeakReference(class_instance as? Any)
    }
}