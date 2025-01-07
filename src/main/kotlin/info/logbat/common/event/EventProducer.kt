package info.logbat.common.event

interface EventProducer<T> {
    fun produce(data:List<T>):Unit
}