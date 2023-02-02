package com.manuel1n1.plugins

import com.manuel1n1.db.DatabaseFactory
import com.manuel1n1.db.DatabaseFactoryInterface
import org.koin.dsl.module

val koinModules = module {
    single { DatabaseFactory as DatabaseFactoryInterface }
}