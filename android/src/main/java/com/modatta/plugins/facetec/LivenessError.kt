package com.modatta.plugins.facetec
enum class LivenessErrorCode {
    NoActivity,
    InvalidConfiguration,
    SetupFailed,
    CreateSessionFailed,
    InvalidSession,
    FaceScanCancelled,
    FaceScanFailed,
    LivenessCheckFailed
}

sealed class LivenessError(
        val code: LivenessErrorCode,
        message: String? = code.toString(),
        cause: Exception? = null,
) :
        Exception(message, cause) {
    class NoActivity(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.NoActivity, message, cause)

    class InvalidConfiguration(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.InvalidConfiguration, message, cause)

    class SetupFailed(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.SetupFailed, message, cause)

    class CreateSessionFailed(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.CreateSessionFailed, message, cause)

    class InvalidSession(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.InvalidSession, message, cause)

    class FaceScanCancelled :
            LivenessError(LivenessErrorCode.FaceScanCancelled)

    class FaceScanFailed(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.FaceScanFailed, message, cause)

    class LivenessCheckFailed(message: String? = null, cause: Exception? = null) :
            LivenessError(LivenessErrorCode.LivenessCheckFailed, message, cause)
}
