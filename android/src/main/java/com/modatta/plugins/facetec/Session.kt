import com.modatta.plugins.facetec.*
import okhttp3.*
import org.json.JSONObject
import java.util.*

class Session(
        configuration: Config,
        val id: LivenessId,
        private val onSuccess: () -> Unit,
        private val onFailure: (error: LivenessError) -> Unit,
) {
    var token: SessionToken? = null
        private set

    init {
        val payload = JSONObject(mapOf("livenessId" to id.value))

        HttpClient.enqueueGetRequest(
                url = configuration.sessionUrl,
                deviceKeyIdentifier = configuration.deviceKeyIdentifier,
                onFailure = {
                    onFailure(LivenessError.CreateSessionFailed("Unable to create session", it))
                },
                onResponse = {
                    token = SessionToken(it.getString("sessionToken"))
                    onSuccess()
                }
        )
    }
}

data class LivenessId(val value: String) {}

data class SessionToken(val value: String) {}
