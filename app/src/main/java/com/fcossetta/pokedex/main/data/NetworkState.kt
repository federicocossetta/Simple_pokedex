package com.fcossetta.pokedex.main.data

import io.uniflow.core.flow.data.UIState

open class NetworkState : UIState() {

    data class NetworkChanged(val online: Boolean) :
        NetworkState()

}