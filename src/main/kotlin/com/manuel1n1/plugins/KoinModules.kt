package com.manuel1n1.plugins

import com.manuel1n1.controller.auth.AuthController
import com.manuel1n1.controller.auth.DefaultAuthController
import com.manuel1n1.controller.auth.JWTController
import com.manuel1n1.controller.auth.TokenProvider
import com.manuel1n1.dao.UserDao
import com.manuel1n1.db.DatabaseFactory
import com.manuel1n1.db.DatabaseFactoryInterface
import com.manuel1n1.db.table.UserRepository
import org.koin.dsl.module

val koinModules = module {
    single { DatabaseFactory as DatabaseFactoryInterface }
    single {  DefaultAuthController() as AuthController }
    single { UserRepository as UserDao }
    //single { JWTController as TokenProvider  }
}