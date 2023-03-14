package com.modatta.plugins.facetec

import Session
import org.json.JSONObject

class LivenessCheck(
        private val configuration:Config,
        private val session: Session
) {
    fun start(
            request: LivenessCheckRequest,
            onProgress: (percentage: Float) -> Unit,
            onSuccess: (result: LivenessCheckResult) -> Unit,
            onFailure: (error: LivenessError) -> Unit,
    ) {
        val payload = JSONObject(
                mapOf(
                        "livenessId" to session.id.value,
                        "faceScan" to request.faceScanBase64,
                        "auditTrailImage" to request.auditTrailCompressedBase64,
                        "lowQualityAuditTrailImage" to request.lowQualityAuditTrailCompressedBase64,
                )
        )

        HttpClient.enqueuePostRequest(
                url = configuration.checkUrl,
                payload = payload,
                token = request.token,
                deviceKeyIdentifier = configuration.deviceKeyIdentifier,
                onProgress = onProgress,
                onFailure = {
                    onFailure(
                            LivenessError.LivenessCheckFailed(
                                    "Face scan upload failed",
                                    it
                            )
                    )
                },
                onResponse = {
                    onSuccess(
                            LivenessCheckResult(
                                    succeeded = it.getBoolean("succeeded"),
                                    blob = it.getString("scanResultBlob")
                            )
                    )
                }
        )
    }
}

data class LivenessCheckRequest(
        val token: String,
        val faceScanBase64: String,
        val auditTrailCompressedBase64: String,
        val lowQualityAuditTrailCompressedBase64: String?
)

data class LivenessCheckResult(val succeeded: Boolean, val blob: String)
