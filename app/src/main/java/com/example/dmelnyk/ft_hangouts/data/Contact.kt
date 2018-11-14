package com.example.dmelnyk.ft_hangouts.data

data class Contact(var id: Int, var first_name: String, var last_name: String, var phone_number: String, var email: String, var photoSrc: String, var lastMessage: SmsEntity?) {

    constructor() : this(0, "", "", "", "", "default", null)
}