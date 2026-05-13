package com.example.virasat.data.di

import android.content.Context
import com.example.virasat.data.repository.FirebaseHeritageRepository
import com.example.virasat.data.repository.HeritageRepository
import com.example.virasat.data.repository.RoomHeritageRepository

object RepositoryProvider {
    @Volatile var useFirebase: Boolean = true

    private var repository: HeritageRepository? = null

    fun getRepository(context: Context): HeritageRepository {
        return repository ?: synchronized(this) {
            repository ?: if (useFirebase) {
                FirebaseHeritageRepository(context)
            } else {
                RoomHeritageRepository(context)
            }.also { repository = it }
        }
    }

    fun reset() {
        repository = null
    }
}
