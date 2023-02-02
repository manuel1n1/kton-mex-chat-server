package com.manuel1n1.models.request

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class SignUpRequest(
    @NotNull
    val email: String,
    @NotNull
    val password: String,
    @NotNull
    val confirmPassword: String
)