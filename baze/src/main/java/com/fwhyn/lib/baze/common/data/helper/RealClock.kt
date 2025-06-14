package com.fwhyn.lib.baze.common.data.helper

class RealClock : Clock {
    override fun currentMillis() = System.currentTimeMillis()
}