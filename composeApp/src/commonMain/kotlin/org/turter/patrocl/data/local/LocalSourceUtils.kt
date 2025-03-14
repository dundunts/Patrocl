package org.turter.patrocl.data.local

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Message
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import io.realm.kotlin.types.BaseRealmObject
import org.turter.patrocl.domain.exception.EmptyLocalDataException

fun <T : BaseRealmObject> handleSingleResult(result: SingleQueryChange<T>): Result<T> {
    return try {
        result.obj?.let { Result.success(it) }
            ?: Result.failure(EmptyLocalDataException())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

fun <T : BaseRealmObject> handleMultiResult(result: ResultsChange<T>): Result<List<T>> {
    return try {
        val entities = result.list
        if (entities.isNotEmpty()) Result.success(entities)
        else Result.failure(EmptyLocalDataException())
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}

fun <T> Result<T>.logFetchingEntityWith(logger: Logger): Result<T> {
    return this.onSuccess { logger.d { "Fetched Entity from realm: $it" } }
        .onFailure { logger.e { "Catch exception while fetching Entity from realm. Exception: $it" } }
}

fun <T> Result<List<T>>.logFetchingEntityListWith(logger: Logger): Result<List<T>> {
    return this.onSuccess { logger.d { "Fetched entities from realm, count: ${it.count()}" } }
        .onFailure { logger.e { "Catch exception while fetching entities from realm. Exception: $it" } }
}

fun <T> T?.handleNullableResult(): Result<T> {
    return if (this != null) Result.success(this) else Result.failure(EmptyLocalDataException())
}