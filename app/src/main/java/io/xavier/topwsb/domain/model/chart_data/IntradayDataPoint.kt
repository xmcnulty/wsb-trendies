package io.xavier.topwsb.domain.model.chart_data

import java.time.LocalDateTime

/**
 * Data point on price chart.
 *
 * @property time time of the data (chart x-axis)
 * @property price closing price at time
 */
data class IntradayDataPoint(
    val time: LocalDateTime,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double
) {
    override fun toString(): String {
        return "$time:\t$$close"
    }
}
