package com.lucasjosino.on_audio_query

import android.app.Activity
import android.content.Context
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.lang.ref.WeakReference

/**
 * A singleton used to define all variables/methods that will be used on all plugin.
 *
 * The singleton will provider the ability to 'request' required variables/methods on any moment.
 *
 * All variables/methods should be defined after plugin initialization (activity/context) and
 * dart request (call/result).
 */
object PluginProvider {
    private const val ERROR_MESSAGE =
        "Tried to get one of the methods but the 'PluginProvider' has not initialized"

    /**
    * Define if 'warn' level will show more detailed logging.
    *
    * Will be used when a query produce some error.
    */
    var showDetailedLog: Boolean = false

    private lateinit var context: WeakReference<Context>

    private lateinit var activity: WeakReference<Activity>

    private lateinit var call: WeakReference<MethodCall>

    private lateinit var result: WeakReference<MethodChannel.Result>

    /**
     * Used to define the current [Activity] and [Context].
     *
     * Should be defined once.
     */
    fun set(activity: Activity) {
        this.context = WeakReference(activity.applicationContext)
        this.activity = WeakReference(activity)
    }

    /**
     * Used to define only the application [Context].
     *
     * This is useful when the plugin is attached to the engine but there's no Activity
     * (for example: background execution / Android Auto). It sets the application
     * context so methods that only need a Context (not an Activity) can still run.
     */
    fun setContext(context: Context) {
        this.context = WeakReference(context)
    }

    /**
     * Used to define the current dart request.
     *
     * Should be defined/redefined on every [MethodChannel.MethodCallHandler.onMethodCall] request.
     */
    fun setCurrentMethod(call: MethodCall, result: MethodChannel.Result) {
        this.call = WeakReference(call)
        this.result = WeakReference(result)
    }

    /**
     * The current plugin 'context'. Defined once.
     *
     * @throws UninitializedPluginProviderException
     * @return [Context]
     */
    fun context(): Context {
        return this.context.get() ?: throw UninitializedPluginProviderException(ERROR_MESSAGE)
    }

    /**
     * The current plugin 'activity'. Defined once.
     * Returns null if no activity is available (app in background).
     * 
     * @return [Activity]? The current activity or null if not available
     */
    fun activityOrNull(): Activity? {
        return this.activity.get()
    }

    /**
     * The current plugin 'activity'. Defined once.
     *
     * @throws UninitializedPluginProviderException
     * @return [Activity]
     */
    fun activity(): Activity {
        return activityOrNull() ?: throw UninitializedPluginProviderException(ERROR_MESSAGE)
    }

    /**
     * The current plugin 'call'. Will be replace with newest dart request.    /**
     * Nullable activity accessor. Returns the current Activity or null if none is set.
     * Use this when the caller wants to handle the absence of an Activity without
     * throwing an exception (for example: background execution / Android Auto).
     */
    fun activityOrNull(): Activity? {
        return this.activity.get()
    }

    /**
     * The current plugin 'call'. Will be replace with newest dart request.
     *
     * @throws UninitializedPluginProviderException
     * @return [MethodCall]
     */
    fun call(): MethodCall {
        return this.call.get() ?: throw UninitializedPluginProviderException(ERROR_MESSAGE)
    }

    /**
     * Nullable call accessor. Returns the current MethodCall or null if none is set.
     */
    fun callOrNull(): MethodCall? {
        return this.call.get()
    }

    /**
     * The current plugin 'result'. Will be replace with newest dart request.
     *
     * @throws UninitializedPluginProviderException
     * @return [MethodChannel.Result]
     */
    fun result(): MethodChannel.Result {
        return this.result.get() ?: throw UninitializedPluginProviderException(ERROR_MESSAGE)
    }

    /**
     * Nullable result accessor. Returns the current MethodChannel.Result or null if none is set.
     */
    fun resultOrNull(): MethodChannel.Result? {
        return this.result.get()
    }

    class UninitializedPluginProviderException(msg: String) : Exception(msg)
}