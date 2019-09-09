package com.example.kotlin_everywhere_to_do_list

data class NoteData(val key: String, var title: String, var content: String, var date: String, var isSelected: Boolean = false)
