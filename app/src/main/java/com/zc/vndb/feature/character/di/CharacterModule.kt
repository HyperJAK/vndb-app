package com.zc.vndb.feature.character.di

import com.zc.vndb.feature.character.data.remote.CharacterService
import com.zc.vndb.feature.character.data.repository.CharacterRepositoryImpl
import com.zc.vndb.feature.character.domain.repository.CharacterRepository
import com.zc.vndb.feature.character.domain.usecase.GetCharacterUseCase
import com.zc.vndb.feature.character.presentation.CharacterViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val characterModule = module {
    // data
    singleOf(::CharacterService)
    singleOf(::CharacterRepositoryImpl) { bind<CharacterRepository>() }

    // domain
    factoryOf(::GetCharacterUseCase)

    // presentation
    viewModelOf(::CharacterViewModel) // characterId comes from parametersOf
}
