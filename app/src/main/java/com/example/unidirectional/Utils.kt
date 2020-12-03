package com.example.unidirectional

import timber.log.Timber

fun infoWorkingIn(msg: String) {
    val thread = "[🧵 ${Thread.currentThread().name}]"
    Timber.i("${msg.padEnd(54)} $thread")
}