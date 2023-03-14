import com.modatta.plugins.facetec.Config
import com.modatta.plugins.facetec.HttpClient
import com.modatta.plugins.facetec.LivenessError
import com.modatta.plugins.facetec.enqueuePostRequest
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

        HttpClient.enqueuePostRequest(
                url = configuration.sessionUrl,
                payload = payload,
                deviceKeyIdentifier = configuration.deviceKeyIdentifier,
                onFailure = {
                    onFailure(LivenessError.CreateSessionFailed("Unable to create session", it))
                },
                onResponse = {
                    token = SessionToken(it.getString("token"))
                    onSuccess()
                }
        )
    }
}

data class LivenessId(val value: String) {}

data class SessionToken(val value: String) {}
