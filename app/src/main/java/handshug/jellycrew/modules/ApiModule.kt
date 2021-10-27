package handshug.jellycrew.modules

import handshug.jellycrew.base.BaseApi
import handshug.jellycrew.home.model.HomeApi
import handshug.jellycrew.main.model.MainApi
import handshug.jellycrew.member.model.MemberApi
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single(createdAtStart = false) {
        get<Retrofit>().create(BaseApi::class.java)
    }

    single(createdAtStart = false) {
        get<Retrofit>().create(MemberApi::class.java)
    }

    single(createdAtStart = false) {
        get<Retrofit>().create(MainApi::class.java)
    }

    single(createdAtStart = false) {
        get<Retrofit>().create(HomeApi::class.java)
    }
}