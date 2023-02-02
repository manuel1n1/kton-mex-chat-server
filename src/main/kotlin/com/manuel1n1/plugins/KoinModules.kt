package com.manuel1n1.plugins

import com.manuel1n1.dao.UserDao
import com.manuel1n1.db.DatabaseFactory
import com.manuel1n1.db.DatabaseFactoryInterface
import com.manuel1n1.db.table.UserRepository
import org.koin.dsl.module

val koinModules = module {
    single { DatabaseFactory as DatabaseFactoryInterface }
    single { UserRepository as UserDao }
}