package info.logbat.common.event

interface EventConsumer<T> {
    fun consume():List<T>
}