package com.modatta.plugins.facetec

import LivenessId
import Session
import android.content.Context
import android.os.Handler
import android.util.Log
import com.facetec.sdk.*
import com.getcapacitor.Bridge
import com.getcapacitor.PluginCall
import com.modatta.plugins.facetec.GlobalVars


class Facetec {

    private var _configuration: Config? = null

    private var _session: Session? = null

    private var _check: LivenessCheck? = null
    private var activity: Context? = null

    var globals: GlobalVars = GlobalVars()

    fun setup(promise: PluginCall, bridge: Bridge) {
        activity = bridge.context // Removi


        if (activity == null) {
            promise.rejectWith(LivenessError.NoActivity())
            return
        }

        val configuration = try {
            Config.create(
                sessionUrl = globals.BaseURL+"/session-token",
                checkUrl = globals.BaseURL+"/liveness-3d",
                deviceKeyIdentifier = globals.DeviceKeyIdentifier,
                publicFaceScanEncryptionKey = globals.PublicFaceScanEncryptionKey,
            )
        } catch (exception: Exception) {
            promise.rejectWith(LivenessError.InvalidConfiguration(cause = exception))
            return
        }

        val customization = createFaceTecCustomization()

        FaceTecSDK.setCustomization(customization)
        FaceTecSDK.setLowLightCustomization(customization)
        FaceTecSDK.setDynamicDimmingCustomization(customization)

        when (val status = FaceTecSDK.getStatus(activity)) {
            FaceTecSDKStatus.NEVER_INITIALIZED, FaceTecSDKStatus.NETWORK_ISSUES -> {
                val callback = object : FaceTecSDK.InitializeCallback() {
                    override fun onCompletion(successful: Boolean) {
                        if (successful) {
                            _configuration = configuration
                            promise.resolve()
                        } else {
                            _configuration = null
                            promise.rejectWith(
                                LivenessError.SetupFailed(
                                    FaceTecSDK.getStatus(
                                        activity
                                    ).toString()
                                )
                            )
                        }
                    }
                }

                if (configuration.productionKeyText != null) {
                    FaceTecSDK.initializeInProductionMode(
                        activity!!,
                        configuration.productionKeyText,
                        configuration.deviceKeyIdentifier,
                        configuration.publicFaceScanEncryptionKey,
                        callback
                    )
                } else {
                    FaceTecSDK.initializeInDevelopmentMode(
                        activity!!,
                        configuration.deviceKeyIdentifier,
                        configuration.publicFaceScanEncryptionKey,
                        callback
                    )
                }
            }
            FaceTecSDKStatus.INITIALIZED -> promise.rejectWith(LivenessError.SetupFailed("Already initialized"))
            else -> promise.rejectWith(LivenessError.SetupFailed(status.toString()))
        }
    }

    fun getSession(): Session? {
        return _session
    }

    fun createSession(id: String, promise: PluginCall) {

        if (activity == null) {
            promise.rejectWith(LivenessError.NoActivity())
            return
        }

        val configuration = _configuration;

        if (configuration == null) {
            promise.rejectWith(LivenessError.InvalidConfiguration())
            return
        }

        _session = Session(
            id = LivenessId(id),
            configuration = configuration,
            onSuccess = {
                promise.resolve()//true)
            }, onFailure = {
                promise.rejectWith(it)
            })
    }


    fun checkLiveness(promise: PluginCall) {

        val configuration = _configuration

        if (configuration == null) {
            promise.rejectWith(LivenessError.InvalidConfiguration())
            return
        }

        val session = _session

        if (session == null) {
            promise.rejectWith(LivenessError.InvalidSession("No session available"))
            return
        }

        val token = session.token

        if (token == null) {
            promise.rejectWith(LivenessError.InvalidSession("Session token is empty"))
            return
        }

        if (_check != null) {
            promise.rejectWith(LivenessError.LivenessCheckFailed("Liveness check already in progress"))
            return
        }

        val check = LivenessCheck(
            configuration = configuration,
            session = session
        )

        _session = null
        _check = check

        val processor = FaceTecFaceScanProcessor { sessionResult, callback ->
            if (sessionResult.status != FaceTecSessionStatus.SESSION_COMPLETED_SUCCESSFULLY) {
                callback.cancel()

                when (sessionResult.status) {
                    FaceTecSessionStatus.USER_CANCELLED, FaceTecSessionStatus.USER_CANCELLED_VIA_HARDWARE_BUTTON, FaceTecSessionStatus.USER_CANCELLED_VIA_CLICKABLE_READY_SCREEN_SUBTEXT -> {
                        _check = null
                        promise.rejectWith(LivenessError.FaceScanCancelled())
                    }
                    else -> {
                        _check = null
                        promise.rejectWith(LivenessError.FaceScanFailed(sessionResult.status.asConstant()))
                    }
                }

                return@FaceTecFaceScanProcessor
            }

            val request = try {
                sessionResult.toRequest()
            } catch (exception: Exception) {
                callback.cancel()
                _check = null;
                promise.rejectWith(
                    LivenessError.FaceScanFailed(
                        "Unable to create liveness request",
                        exception
                    )
                )
                return@FaceTecFaceScanProcessor
            }

            try {
                callback.uploadProgress(0F)

                check.start(
                    request = request,
                    onSuccess = { result ->
                        callback.uploadProgress(1F)
                        if (callback.proceedToNextStep(result.blob)) {
                            _check = null
                            promise.resolve()//result.succeeded)
                        }
                    },
                    onProgress = {
                        callback.uploadProgress(it)
                    },
                    onFailure = {
                        callback.uploadProgress(1F)
                        callback.cancel()
                        _check = null
                        promise.rejectWith(it)
                    }
                )
            } catch (exception: Exception) {
                promise.rejectWith(
                    LivenessError.LivenessCheckFailed(
                        "Unable to create liveness check",
                        exception
                    )
                )
            }
        }

        Handler(activity!!.mainLooper).post {
            try {
                FaceTecSessionActivity.createAndLaunchSession(activity, processor, token.value)
            } catch (exception: Exception) {
                _check = null
                promise.rejectWith(
                    LivenessError.LivenessCheckFailed(
                        "Unable to launch FaceTec activity",
                        exception
                    )
                )
            }
        }
    }
}

private fun FaceTecSessionResult.toRequest(): LivenessCheckRequest {
    val storedLowQualityAuditTrail = this.lowQualityAuditTrailCompressedBase64;
    val lowQualityAuditTrailCompressedBase64 = if (storedLowQualityAuditTrail.isNotEmpty()) {
        storedLowQualityAuditTrail[0]
    } else {
        null
    }

    return LivenessCheckRequest(
        token = this.sessionId!!,
        faceScanBase64 = this.faceScanBase64,
        auditTrailCompressedBase64 = this.auditTrailCompressedBase64[0],
        lowQualityAuditTrailCompressedBase64 = lowQualityAuditTrailCompressedBase64
    )
}

private fun PluginCall.rejectWith(error: LivenessError) {
    reject(error.code.name, error.message, error)
}

private fun FaceTecSessionStatus.asConstant(): String {
    return when (this) {
        FaceTecSessionStatus.NON_PRODUCTION_MODE_KEY_INVALID -> "NON_PRODUCTION_MODE_KEY_INVALID"
        FaceTecSessionStatus.NON_PRODUCTION_MODE_NETWORK_REQUIRED -> "NON_PRODUCTION_MODE_NETWORK_REQUIRED"
        FaceTecSessionStatus.USER_CANCELLED -> "USER_CANCELLED"
        FaceTecSessionStatus.USER_CANCELLED_VIA_HARDWARE_BUTTON -> "USER_CANCELLED_VIA_HARDWARE_BUTTON"
        FaceTecSessionStatus.SESSION_COMPLETED_SUCCESSFULLY -> "SESSION_COMPLETED_SUCCESSFULLY"
        FaceTecSessionStatus.SESSION_UNSUCCESSFUL -> "SESSION_UNSUCCESSFUL"
        FaceTecSessionStatus.CAMERA_PERMISSION_DENIED -> "CAMERA_PERMISSION_DENIED"
        FaceTecSessionStatus.ENCRYPTION_KEY_INVALID -> "ENCRYPTION_KEY_INVALID"
        FaceTecSessionStatus.TIMEOUT -> "TIMEOUT"
        FaceTecSessionStatus.CONTEXT_SWITCH -> "CONTEXT_SWITCH"
        FaceTecSessionStatus.CAMERA_INITIALIZATION_ISSUE -> "CAMERA_INITIALIZATION_ISSUE"
        FaceTecSessionStatus.UNKNOWN_INTERNAL_ERROR -> "UNKNOWN_INTERNAL_ERROR"
        FaceTecSessionStatus.LANDSCAPE_MODE_NOT_ALLOWED -> "LANDSCAPE_MODE_NOT_ALLOWED"
        FaceTecSessionStatus.REVERSE_PORTRAIT_NOT_ALLOWED -> "REVERSE_PORTRAIT_NOT_ALLOWED"
        FaceTecSessionStatus.LOCKED_OUT -> "LOCKED_OUT"
        FaceTecSessionStatus.MISSING_GUIDANCE_IMAGES -> "MISSING_GUIDANCE_IMAGES"
        FaceTecSessionStatus.USER_CANCELLED_VIA_CLICKABLE_READY_SCREEN_SUBTEXT -> "USER_CANCELLED_VIA_CLICKABLE_READY_SCREEN_SUBTEXT"
    }
}



fun echo(value: String): String {
    Log.i("Echo", value)
    return value
}

