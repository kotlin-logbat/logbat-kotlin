package info.logbat.dev.util

import java.lang.ref.WeakReference
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

class ThreadLocalLongAdder {
    private val localCounter =  ThreadLocal<Counter>()
    private val activeCounters: Queue<WeakReference<Counter>> = ConcurrentLinkedQueue()

    private var value = 0L
    fun increment() {
        val counter = localCounter.get() ?: Counter().apply{
            localCounter.set(this)
            activeCounters.add(WeakReference<Counter>(this))
        }
        counter.increaseValue()
    }

    fun get(): Long = flush().run{
        value
    }

    fun reset() = flush().apply{
        value=0L
    }

    private fun flush() {
        synchronized(activeCounters) {
            activeCounters.removeIf { weakCounter ->
                weakCounter.get()?.let { counter ->
                    value += counter.getValue()
                    counter.resetValue()
                    false
                } ?: true // null일 경우 true를 반환하여 요소 제거
            }
        }
    }

    class Counter{
        private var value: Long = 0
        fun increaseValue() = value++
        fun getValue() = value
        fun resetValue() {
            value = 0
        }
    }
}