package com.applepulse.activeaura.ui.auth.signUpScreen

import com.applepulse.activeaura.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class SignUpRepository {

    suspend fun registerUser(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
            Logger.debugLog("AuthResult: $authResult")
            authResult.user
        } catch (e: FirebaseAuthUserCollisionException) {
            val auth = FirebaseAuth.getInstance()
            val projectId = auth.app.options.projectId
            Logger.debugLog("Firebase", "Project ID: $projectId")
            Logger.debugLog("User with email $email already exists.")
            Logger.debugLog("FirebaseAuthUserCollisionException: ${e.message}")
            null // Return null if the user email already exists
        } catch (e: Exception) {
            Logger.debugLog("Second")
            null
        }
    }

}