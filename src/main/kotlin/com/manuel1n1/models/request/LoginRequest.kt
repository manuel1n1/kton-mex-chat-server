package com.manuel1n1.models.request

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class LoginRequest(
    @NotNull
    val userName: String,
    @NotNull
    val password: String
)