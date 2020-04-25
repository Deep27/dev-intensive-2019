package ru.skillbranch.devintensive.models

class UserView(
    val id: String, val fullName: String, val nickname: String,
    var avatar: String?, var status: String? = "offline", val initials: String?
) {

}