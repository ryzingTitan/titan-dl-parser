package com.ryzingtitan.datalogparser.domain.parsing.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties

@Generated
@ConfigurationProperties(prefix = "column-configuration")
data class ColumnConfiguration(
    val deviceTime: Int,
    val intakeAirTemperature: Int
)