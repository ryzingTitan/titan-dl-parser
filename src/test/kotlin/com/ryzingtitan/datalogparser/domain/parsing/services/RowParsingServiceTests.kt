package com.ryzingtitan.datalogparser.domain.parsing.services

import com.ryzingtitan.datalogparser.domain.parsing.configuration.ColumnConfiguration
import com.ryzingtitan.datalogparser.domain.parsing.configuration.TrackInfoConfiguration
import com.ryzingtitan.datalogparser.domain.parsing.configuration.UserConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.*

class RowParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `parses the row correctly when it contains valid session data`() {
            val row = "$firstLineDeviceTime," +
                "$firstLineLongitude," +
                "$firstLineLatitude," +
                "$firstLineAltitude," +
                "${firstLineCoolantTemperature.toFloat()}," +
                "${firstLineEngineRpm.toFloat()}," +
                "${firstLineIntakeAirTemperature.toFloat()}," +
                "${firstLineSpeed.toFloat()}," +
                "$firstLineThrottlePosition," +
                "$firstLineBoostPressure," +
                firstLineAirFuelRatio

            val datalog = rowParsingService.parse(row, sessionId)

            assertEquals(sessionId, datalog.sessionId)
            assertEquals(firstLineEpochMilliseconds, datalog.epochMilliseconds)
            assertEquals(firstLineLongitude, datalog.data.longitude)
            assertEquals(firstLineLatitude, datalog.data.latitude)
            assertEquals(firstLineAltitude, datalog.data.altitude)
            assertEquals(firstLineIntakeAirTemperature, datalog.data.intakeAirTemperature)
            assertEquals(firstLineBoostPressure, datalog.data.boostPressure)
            assertEquals(firstLineCoolantTemperature, datalog.data.coolantTemperature)
            assertEquals(firstLineEngineRpm, datalog.data.engineRpm)
            assertEquals(firstLineSpeed, datalog.data.speed)
            assertEquals(firstLineThrottlePosition, datalog.data.throttlePosition)
            assertEquals(firstLineAirFuelRatio, datalog.data.airFuelRatio)
            assertEquals("Test Track", datalog.trackInfo.name)
            assertEquals(42.4086, datalog.trackInfo.latitude)
            assertEquals(-86.1374, datalog.trackInfo.longitude)
            assertEquals("test@test.com", datalog.user.email)
            assertEquals("test", datalog.user.firstName)
            assertEquals("tester", datalog.user.lastName)
        }

        @Test
        fun `parses the row correctly when it contains invalid session data`() {
            val row = "$secondLineDeviceTime," +
                "$secondLineLongitude," +
                "$secondLineLatitude," +
                "$secondLineAltitude," +
                "$secondLineCoolantTemperature," +
                "$secondLineEngineRpm," +
                "$secondLineIntakeAirTemperature," +
                "$secondLineSpeed," +
                "$secondLineThrottlePosition," +
                "$secondLineBoostPressure," +
                secondLineAirFuelRatio

            val datalog = rowParsingService.parse(row, sessionId)

            assertEquals(sessionId, datalog.sessionId)
            assertEquals(secondLineEpochMilliseconds, datalog.epochMilliseconds)
            assertEquals(secondLineLongitude, datalog.data.longitude)
            assertEquals(secondLineLatitude, datalog.data.latitude)
            assertEquals(secondLineAltitude, datalog.data.altitude)
            assertNull(datalog.data.intakeAirTemperature)
            assertNull(datalog.data.boostPressure)
            assertNull(datalog.data.coolantTemperature)
            assertNull(datalog.data.engineRpm)
            assertNull(datalog.data.speed)
            assertNull(datalog.data.throttlePosition)
            assertNull(datalog.data.airFuelRatio)
            assertEquals("Test Track", datalog.trackInfo.name)
            assertEquals(42.4086, datalog.trackInfo.latitude)
            assertEquals(-86.1374, datalog.trackInfo.longitude)
            assertEquals("test@test.com", datalog.user.email)
            assertEquals("test", datalog.user.firstName)
            assertEquals("tester", datalog.user.lastName)
        }
    }

    @BeforeEach
    fun setup() {
        rowParsingService = RowParsingService(
            mockColumnConfiguration,
            mockUserConfiguration,
            mockTrackInfoConfiguration,
        )

        whenever(mockColumnConfiguration.deviceTime).thenReturn(0)
        whenever(mockColumnConfiguration.longitude).thenReturn(1)
        whenever(mockColumnConfiguration.latitude).thenReturn(2)
        whenever(mockColumnConfiguration.altitude).thenReturn(3)
        whenever(mockColumnConfiguration.coolantTemperature).thenReturn(4)
        whenever(mockColumnConfiguration.engineRpm).thenReturn(5)
        whenever(mockColumnConfiguration.intakeAirTemperature).thenReturn(6)
        whenever(mockColumnConfiguration.speed).thenReturn(7)
        whenever(mockColumnConfiguration.throttlePosition).thenReturn(8)
        whenever(mockColumnConfiguration.boostPressure).thenReturn(9)
        whenever(mockColumnConfiguration.airFuelRatio).thenReturn(10)

        whenever(mockUserConfiguration.email).thenReturn("test@test.com")
        whenever(mockUserConfiguration.firstName).thenReturn("test")
        whenever(mockUserConfiguration.lastName).thenReturn("tester")

        whenever(mockTrackInfoConfiguration.name).thenReturn("Test Track")
        whenever(mockTrackInfoConfiguration.latitude).thenReturn(42.4086)
        whenever(mockTrackInfoConfiguration.longitude).thenReturn(-86.1374)
    }

    private lateinit var rowParsingService: RowParsingService

    private val mockColumnConfiguration = mock<ColumnConfiguration>()
    private val mockTrackInfoConfiguration = mock<TrackInfoConfiguration>()
    private val mockUserConfiguration = mock<UserConfiguration>()

    companion object RowParsingServiceTestConstants {
        val sessionId: UUID = UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")

        const val firstLineDeviceTime = "18-Sep-2022 14:15:47.963"
        const val firstLineLongitude = -86.14162999999999
        const val firstLineLatitude = 42.406800000000004
        const val firstLineAltitude = 188.4f
        const val firstLineIntakeAirTemperature = 123
        const val firstLineBoostPressure = 16.5f
        const val firstLineCoolantTemperature = 155
        const val firstLineEngineRpm = 5500
        const val firstLineSpeed = 86
        const val firstLineThrottlePosition = 95.5f
        const val firstLineAirFuelRatio = 14.7f
        val firstLineEpochMilliseconds = Instant.parse("2022-09-18T18:15:47.963Z").toEpochMilli()

        const val secondLineDeviceTime = "18-Sep-2022 14:18:47.968"
        const val secondLineLongitude = 86.14162999999999
        const val secondLineLatitude = -42.406800000000004
        const val secondLineAltitude = 188.0f
        const val secondLineIntakeAirTemperature = "-"
        const val secondLineBoostPressure = "-"
        const val secondLineCoolantTemperature = "-"
        const val secondLineEngineRpm = "-"
        const val secondLineSpeed = "-"
        const val secondLineThrottlePosition = "-"
        const val secondLineAirFuelRatio = "-"
        val secondLineEpochMilliseconds = Instant.parse("2022-09-18T18:18:47.968Z").toEpochMilli()
    }
}
