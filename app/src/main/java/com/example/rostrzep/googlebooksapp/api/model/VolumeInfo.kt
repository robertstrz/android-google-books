package com.example.rostrzep.googlebooksapp.api.model

import java.io.Serializable

class VolumeInfo(val title: String, val subtitle: String, val authors: List<String>, val description: String, val imageLinks: BookThumbnail) : Serializable
