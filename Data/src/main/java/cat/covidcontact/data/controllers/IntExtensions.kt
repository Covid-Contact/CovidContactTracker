package cat.covidcontact.data.controllers

fun Int.isServerError() = this in 500..599
