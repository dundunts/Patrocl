package org.turter.patrocl.domain.model

sealed class DataVersionsActualizerStatus {

    data object Initial : DataVersionsActualizerStatus()

    data object Checking : DataVersionsActualizerStatus()

    data object Updating : DataVersionsActualizerStatus()

    data object Completed : DataVersionsActualizerStatus()

    data class Error(val cause: Throwable): DataVersionsActualizerStatus()

}