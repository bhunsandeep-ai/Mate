package com.example

import com.example.data.MockDataProvider
import com.example.model.*
import org.junit.Assert.*
import org.junit.Test

class NexChatStage1UnitTest {

    @Test
    fun test24HourMessageExpiration() {
        val now = System.currentTimeMillis()
        val activeMsg = Message(
            id = "test_msg_1",
            senderId = "me",
            conversationId = "conv_1",
            text = "Hello world",
            createdAtEpochMs = now,
            expiresAtEpochMs = now + (24 * 60 * 60 * 1000L),
            status = MessageStatus.SENT
        )

        assertFalse("Active message within 24h should not be expired", activeMsg.isExpired)
        assertTrue("Remaining hours should be greater than 0", activeMsg.remainingHours in 23..24)

        val expiredMsg = Message(
            id = "test_msg_2",
            senderId = "me",
            conversationId = "conv_1",
            text = "Old message",
            createdAtEpochMs = now - (25 * 60 * 60 * 1000L),
            expiresAtEpochMs = now - (1 * 60 * 60 * 1000L),
            status = MessageStatus.EXPIRED
        )

        assertTrue("Message beyond 24h must be marked expired", expiredMsg.isExpired)
        assertEquals(0, expiredMsg.remainingHours)
    }

    @Test
    fun testIndianPhoneNumberFormats() {
        val priya = MockDataProvider.userPriya
        assertTrue("Phone number must have +91 prefix", priya.phoneNumber.startsWith("+91"))
        val digitsOnly = priya.phoneNumber.replace("\\s".toRegex(), "").removePrefix("+91")
        assertEquals(10, digitsOnly.length) // Standard Indian 10-digit mobile number
    }

    @Test
    fun testAdvertisementModelAndBudget() {
        val ad = MockDataProvider.sampleSponsoredAd
        assertEquals(499, ad.budgetInr)
        assertEquals(AdStatus.ACTIVE, ad.status)
        assertEquals(AdFormat.SPONSORED_CHAT_CARD, ad.format)
        assertTrue("Ad impressions must be tracked", ad.impressions > 0)
    }

    @Test
    fun testStoriesSeenAndSponsoredState() {
        val stories = MockDataProvider.getInitialStories()
        val sponsoredStory = stories.firstOrNull { it.isSponsored }
        assertNotNull("Sponsored story must exist in initial stories", sponsoredStory)
        assertEquals("Maa Kali Bastralaya", sponsoredStory?.sponsorBusinessName)
    }
}
