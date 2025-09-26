package com.example.todoapp.helper

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale


object CommonHelper {
    fun getCurrentDate(): String {
        val currentDate: LocalDate = LocalDate.now()
        val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
        return currentDate.format(formater)
    }

    fun dateDifferent(date1: String, date2: String): Period {
        val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
        val startDate = LocalDate.parse(date1, formater)
        val endDate = LocalDate.parse(date2, formater)
        return Period.between(startDate, endDate)
    }

    fun dateDifferentDetail(date1: String, date2: String): String {
        val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
        val startDate = LocalDate.parse(date1, formater)
        val endDate = LocalDate.parse(date2, formater)
        val period = Period.between(startDate, endDate)

        if (period.isNegative) {
            val overduePeriod = period.negated()
            val parts = mutableListOf<String>()
            if (overduePeriod.years > 0) {
                parts.add("${overduePeriod.years} năm")
            }
            if (overduePeriod.months > 0) {
                parts.add("${overduePeriod.months} tháng")
            }
            if (overduePeriod.days > 0) {
                parts.add("${overduePeriod.days} ngày")
            }
            val overdueText = if (parts.isNotEmpty()) {
                parts.joinToString(" ")
            } else {
                "Hôm nay"
            }
            return "Đã quá hạn $overdueText"
        }


        val parts = mutableListOf<String>()
        if (period.years > 0) {
            parts.add("${period.years} năm")
        }
        if (period.months > 0) {
            parts.add("${period.months} tháng")
        }
        if (period.days > 0) {
            parts.add("${period.days} ngày")
        }

        return if (parts.isNotEmpty()) {
            parts.joinToString(" ")
        } else {
            "Hôm nay"
        }
    }
}