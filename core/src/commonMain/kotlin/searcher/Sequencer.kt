package searcher

import kotlinx.coroutines.Job


public class Sequencer(val maxOperations: Int = 5) {

    init {
        if (maxOperations <= 0)
            throw IllegalArgumentException("Sequencer maxOperations should be higher than 0.")
    }

    private val operations = mutableListOf<Job>()

    public val currentOperation get() = operations.last()

    /**
     * When an operation is added, and the maxOperations count is reached, the oldest job in the queue is canceled
     * and removed from the queue.
     */
    fun addOperation(operation: Job) {
        operations.add(operation)
        if (operations.size > maxOperations) {
            operations.removeAt(0).cancel()
        }
        operation.invokeOnCompletion {
            operationCompleted(operation)
        }
    }

    /**
     * When an operation completes, cancel and remove operations from the queue that are older.
     */
    private fun operationCompleted(operation: Job) {
        val index = operations.indexOf(operation)

        if (index > 0) {
            val jobs = operations.take(index)

            jobs.forEach { it.cancel() }
            operations.removeAll(jobs)
        }
        operations.remove(operation)
    }

    /**
     * Cancel and clear all operations from the queue.
     */
    fun cancelAll() {
        operations.forEach { it.cancel() }
        operations.clear()
    }
}
