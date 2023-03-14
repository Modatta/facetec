package com.modatta.plugins.facetec
import java.net.URI

data class Config(
        val checkUrl: URI,
        val sessionUrl: URI,
        val deviceKeyIdentifier: String,
        val publicFaceScanEncryptionKey: String,
        val productionKeyText: String? = null,
) {
    companion object {
        fun create(
                checkUrl: String,
                sessionUrl: String,
                deviceKeyIdentifier: String,
                publicFaceScanEncryptionKey: String,
                productionKeyText: String? = null,
        ): Config {
            try {
                if (deviceKeyIdentifier.isEmpty()) {
                    throw Exception("Device key identifier is empty")
                }

                if (publicFaceScanEncryptionKey.isEmpty()) {
                    throw Exception("Public face scan encryption key is empty")
                }

                if (productionKeyText != null && productionKeyText.isEmpty()) {
                    throw Exception("Production key text is empty")
                }

                return Config(
                        checkUrl = URI(checkUrl),
                        sessionUrl = URI(sessionUrl),
                        deviceKeyIdentifier = deviceKeyIdentifier,
                        publicFaceScanEncryptionKey = publicFaceScanEncryptionKey,
                        productionKeyText = productionKeyText
                )
            } catch (exception: Exception) {
                throw Exception("Unable to create configuration: ${exception.message}", exception)
            }
        }
    }
}
