package org.solarharmony

import org.koin.dsl.module
import org.solarharmony.raids.CustomRaidController
import org.solarharmony.raids.CustomRaidControllerTest

val appModule = module {
    single<CustomRaidController> { CustomRaidControllerTest() }
}
