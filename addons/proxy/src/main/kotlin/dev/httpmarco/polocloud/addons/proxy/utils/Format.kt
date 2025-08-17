package dev.httpmarco.polocloud.addons.proxy.utils

import java.time.Duration

open class Format {

    companion object {
        fun formatDuration(millis: Long): String {
            var duration = Duration.ofMillis(millis)

            val days = duration.toDays()
            duration = duration.minusDays(days)

            val hours = duration.toHours()
            duration = duration.minusHours(hours)

            val minutes = duration.toMinutes()
            duration = duration.minusMinutes(minutes)

            val seconds = duration.seconds
            val sb = StringBuilder()

            if (days > 0) sb.append(days).append("d ")
            if (hours > 0 || days > 0) sb.append(hours).append("h ")
            if (minutes > 0 || hours > 0 || days > 0) sb.append(minutes).append("m ")
            if (seconds > 0 || minutes > 0 || hours > 0 || days > 0) sb.append(seconds).append("s ")

            return sb.toString()
        }
    }

}