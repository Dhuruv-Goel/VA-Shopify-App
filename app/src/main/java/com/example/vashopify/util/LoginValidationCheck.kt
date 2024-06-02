package com.example.vashopify.util

import android.util.Patterns

fun LvalidateEmail(email: String): LoginValidation{
    if (email.isEmpty())
        return LoginValidation.Failed("Email cannot be empty")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return LoginValidation.Failed("Wrong email format")

    return LoginValidation.Success
}

fun LvalidatePassword(password: String): LoginValidation{
    if (password.isEmpty())
        return LoginValidation.Failed("Password cannot be empty")

    if (password.length <=7)
        return LoginValidation.Failed("Password should contains 6 char")

    return LoginValidation.Success
}