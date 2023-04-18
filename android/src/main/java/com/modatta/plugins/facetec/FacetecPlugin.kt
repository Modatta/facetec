package com.modatta.plugins.facetec

import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import java.util.*

@CapacitorPlugin(name = "Facetec")
class FacetecPlugin : Plugin() {
    private val implementation = Facetec()


    @PluginMethod
    fun setup(call: PluginCall) {
        if (implementation.getSession() == null) {// Método criado para verificar se a sessão foi criada
            implementation.setup(call, bridge) //Adicionei o Bridge aqui como parâmetro
            implementation.createSession("LIVENESS_ID-" + UUID.randomUUID(), call)
        }else{
            implementation.checkLiveness(call)
        }
    }

    @PluginMethod
    fun checkLiveness(call: PluginCall) {
        implementation.checkLiveness(call)
    }
}

//Exemplo
/*@PluginMethod
fun echo(call: PluginCall) {
    val value = call.getString("value")
    val ret = JSObject()
    ret.put("value", implementation.echo(value!!))
    call.resolve(ret)
}*/