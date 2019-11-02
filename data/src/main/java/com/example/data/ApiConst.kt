package com.example.data

class ApiConst private constructor() {

    init {
        throw AssertionError("Don't make entity of " + ApiConst::class.java.simpleName)
    }

    companion object {

        const val BASE_URL = "https://picsum.photos/"
        const val ENDPOINT = BASE_URL + ""

        const val LIMIT = 10

    }


}
