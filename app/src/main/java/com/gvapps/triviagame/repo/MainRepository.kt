package com.gvapps.triviagame.repo

import com.gvapps.triviagame.service.Api


class MainRepository constructor(private val api: Api) {

      fun getQuestion() = api.getQuestion()
}