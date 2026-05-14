package com.example.virasat.data.di

import android.content.Context
import com.example.virasat.data.repository.FirebaseHeritageRepository
import com.example.virasat.data.repository.HeritageRepository
import com.example.virasat.data.repository.RoomHeritageRepository

object RepositoryProvider {
    @Volatile private var _useFirebase: Boolean = true
    var useFirebase: Boolean
        get() = _useFirebase
        set(value) {
            if (_useFirebase != value) {
                _useFirebase = value
                reset() // invalidate cached instance when mode changes
            }
        }

    private var repository: HeritageRepository? = null

    fun getRepository(context: Context): HeritageRepository {
        return repository ?: synchronized(this) {
            repository ?: if (_useFirebase) {
                FirebaseHeritageRepository(context)
            } else {
                RoomHeritageRepository(context)
            }.also { repository = it }
        }
    }

    internal fun reset() {
        synchronized(this) { repository = null }
    }
}
