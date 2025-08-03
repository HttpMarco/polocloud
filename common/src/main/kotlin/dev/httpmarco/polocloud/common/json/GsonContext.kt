package dev.httpmarco.polocloud.common.json

import com.google.gson.GsonBuilder

var GSON = GsonBuilder().create()
var PRETTY_GSON = GsonBuilder().setPrettyPrinting().create()