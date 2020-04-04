@file:Suppress("unused")
package me.li2.android.common.arch

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * A wrapper for data that is exposed via a LiveData that represents an event.
 * https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * An Observer for [Event]s, simplifying the pattern of checking if the Event's content has already been handled.
 *
 * @param onEventUnhandledContent is *ONLY* called if the [Event]'s contents has not been handled.
 * @see <a href="https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150">LiveData with SnackBar, Navigation and other events</a>
 * @see <a href="https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9">EventObserver.kt</a>
 */
fun <T : Any, L : LiveData<Event<T>>> Fragment.observeEventOnView(
        liveData: L,
        onEventUnhandledContent: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { event ->
        event?.getContentIfNotHandled()?.let { content ->
            onEventUnhandledContent(content)
        }
    })
}
