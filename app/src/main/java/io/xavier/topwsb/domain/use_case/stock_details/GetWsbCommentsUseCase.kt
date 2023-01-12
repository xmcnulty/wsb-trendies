package io.xavier.topwsb.domain.use_case.stock_details

import io.xavier.topwsb.common.Resource
import io.xavier.topwsb.common.startOfDayMilliseconds
import io.xavier.topwsb.domain.model.WsbComment
import io.xavier.topwsb.domain.repository.WsbCommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Use case class that performs an API call get a list of comments on /r/wallstreetbets
 * returned as a list of [WsbComment] wrapped in a [Resource].
 */
class GetWsbCommentsUseCase @Inject constructor(
    private val repository: WsbCommentsRepository
) {
    /**
     * Invokes asynchronous call to fetch list of comments for the current day
     * on /r/wallstreetbets that contain [ticker] in the comment body
     *
     * @param ticker ticker of the stock to query
     * @return flow with a [Resource] object. If successful, the resource will wrap a list of
     *  [WsbComment]
     */
    operator fun invoke(
        ticker: String
    ): Flow<Resource<List<WsbComment>>> = flow {
        try {
            emit(Resource.Loading())

            val comments = repository.getComments(
                ticker, startOfDayMilliseconds()
            )
            emit(Resource.Success(comments))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}