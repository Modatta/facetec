package com.modatta.plugins.facetec

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "Facetec")
class FacetecPlugin : Plugin() {
    private val implementation = Facetec()


    @PluginMethod
    fun setup(call: PluginCall) {

        implementation.setup(call)
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