package org.solarharmony

import org.koin.dsl.module
import org.solarharmony.entities.registry.EntityRegistry
import org.solarharmony.entities.registry.EntityRegistryImpl
import org.solarharmony.siege.SiegeController
import org.solarharmony.siege.SiegeControllerTest

val appModule = module {
    single<SiegeController> { SiegeControllerTest() }
    single<EntityRegistry> { EntityRegistryImpl() }
}
