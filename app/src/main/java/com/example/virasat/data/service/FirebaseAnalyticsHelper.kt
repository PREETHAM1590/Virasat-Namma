package com.example.virasat.data.service

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseAnalyticsHelper {

    private var analytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        if (analytics == null) {
            analytics = FirebaseAnalytics.getInstance(context.applicationContext)
        }
    }

    fun logScreenView(screenName: String) {
        analytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        })
    }

    fun logSiteView(siteId: String, siteName: String) {
        analytics?.logEvent("view_heritage_site", Bundle().apply {
            putString("site_id", siteId)
            putString("site_name", siteName)
        })
    }

    fun logSearch(query: String) {
        analytics?.logEvent(FirebaseAnalytics.Event.SEARCH, Bundle().apply {
            putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        })
    }

    fun logQuizStart(quizTopic: String) {
        analytics?.logEvent("quiz_start", Bundle().apply {
            putString("quiz_topic", quizTopic)
        })
    }

    fun logQuizComplete(score: Int, total: Int) {
        analytics?.logEvent("quiz_complete", Bundle().apply {
            putInt("score", score)
            putInt("total_questions", total)
        })
    }

    fun logTalkingTourStart(siteId: String) {
        analytics?.logEvent("talking_tour_start", Bundle().apply {
            putString("site_id", siteId)
        })
    }

    fun logCheckIn(siteId: String) {
        analytics?.logEvent("heritage_checkin", Bundle().apply {
            putString("site_id", siteId)
        })
    }
}
