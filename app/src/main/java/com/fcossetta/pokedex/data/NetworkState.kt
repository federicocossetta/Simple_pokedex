package com.fcossetta.pokedex.data

import io.uniflow.core.flow.data.UIState

open class NetworkState : UIState() {

    data class NetworkChanged(val online: Boolean) :
        NetworkState()

}