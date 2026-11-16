package com.zc.vndb.feature.profile.domain.usecase

import com.zc.vndb.common.domain.repository.SessionRepository

class LogOutUseCase(private val repository: SessionRepository) {

    suspend operator fun invoke() = repository.logOut()
}
