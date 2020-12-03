package com.example.unidirectional.data

import kotlinx.coroutines.delay

interface GreetingRepository {
    suspend fun getMessage(nickname: String): String
}

object GreetingRepositoryImpl : GreetingRepository {
    override suspend fun getMessage(nickname: String): String {
        delay(2500)
        return "Hello $nickname! "
    }
}
