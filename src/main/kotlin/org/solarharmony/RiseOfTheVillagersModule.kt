package org.solarharmony

import org.koin.dsl.module
import org.solarharmony.raids.CustomRaidController
import org.solarharmony.raids.CustomRaidControllerTest
import org.solarharmony.raids.EntityRegistry
import org.solarharmony.raids.EntityRegistryImpl

val appModule = module {
    single<CustomRaidController> { CustomRaidControllerTest() }
    single<EntityRegistry> { EntityRegistryImpl() }
}
