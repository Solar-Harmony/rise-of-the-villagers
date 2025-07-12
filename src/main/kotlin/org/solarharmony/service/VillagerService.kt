package org.solarharmony.service

interface VillagerService {
    fun getVillagerCount(): Int
    fun addVillager(name: String)
    fun getVillagerNames(): List<String>
}