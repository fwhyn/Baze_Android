package com.fwhyn.lib.baze.common.helper

class RealClock : Clock {
    override fun currentMillis() = System.currentTimeMillis()
}