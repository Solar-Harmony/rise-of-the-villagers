package org.solarharmony

import org.koin.dsl.module
import org.solarharmony.service.VillagerService
import org.solarharmony.service.VillagerServiceImpl

val appModule = module {
    // Register VillagerService as a singleton
    single<VillagerService> { VillagerServiceImpl() }

    // Add more dependencies here as needed
    // Example patterns:
    // single<SomeService> { SomeServiceImpl() }  // Singleton
    // factory<SomeRepository> { SomeRepositoryImpl() }  // New instance each time
}
