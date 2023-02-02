package com.manuel1n1.models.request

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class UpdatePasswordRequest(
    @NotNull
    val currentPassword: String,
    @NotNull
    val newPassword: String,
    @NotNull
    val confirmNewPassword: String
)