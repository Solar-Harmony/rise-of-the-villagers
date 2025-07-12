package org.solarharmony.service

class VillagerServiceImpl : VillagerService {
    private val villagers = mutableListOf<String>()

    override fun getVillagerCount(): Int = villagers.size

    override fun addVillager(name: String) {
        villagers.add(name)
    }

    override fun getVillagerNames(): List<String> = villagers.toList()
}
