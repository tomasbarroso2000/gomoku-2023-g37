package com.example.pdm2324i_gomoku_g37.service

import android.util.Log
import com.example.pdm2324i_gomoku_g37.domain.Author
import com.example.pdm2324i_gomoku_g37.domain.Game
import com.example.pdm2324i_gomoku_g37.domain.GameInfo
import com.example.pdm2324i_gomoku_g37.domain.LobbyId
import com.example.pdm2324i_gomoku_g37.domain.ReadyLobby
import com.example.pdm2324i_gomoku_g37.domain.Rules
import com.example.pdm2324i_gomoku_g37.domain.User
import com.example.pdm2324i_gomoku_g37.domain.UserInfo
import com.example.pdm2324i_gomoku_g37.domain.WaitingLobby
import com.example.pdm2324i_gomoku_g37.domain.dtos.AuthorsDto
import com.example.pdm2324i_gomoku_g37.domain.dtos.AuthorsDtoType
import com.example.pdm2324i_gomoku_g37.domain.dtos.LobbiesDto
import com.example.pdm2324i_gomoku_g37.domain.dtos.LobbiesDtoType
import com.example.pdm2324i_gomoku_g37.service.utils.PathTemplate
import com.example.pdm2324i_gomoku_g37.service.utils.ProblemJson
import com.example.pdm2324i_gomoku_g37.service.utils.SirenMediaType
import com.example.pdm2324i_gomoku_g37.service.utils.SirenModel
import com.example.pdm2324i_gomoku_g37.service.utils.plus
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.net.URI
import java.net.URL
import kotlin.coroutines.resumeWithException

private const val GOMOKU_API_URL = "http://10.0.2.2:8080"

class RealGomokuService(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val baseRequestUrl: URL = URL(GOMOKU_API_URL),
) : GomokuService {

    private val authorsRequest by lazy {
        buildRequest(url = baseRequestUrl + URI(PathTemplate.AUTHORS))
    }

    private val lobbiesRequest by lazy {
        buildRequest(url = baseRequestUrl + URI(PathTemplate.LOBBIES))
    }

    override suspend fun fetchAuthors(): List<Author> = authorsRequest.send { body ->
        gson.fromJson<AuthorsDto>(body.string(), AuthorsDtoType.type)
    }.properties.authors

    override suspend fun fetchLobbies(): List<WaitingLobby> = lobbiesRequest.send { body ->
        gson.fromJson<LobbiesDto>(body.string(), LobbiesDtoType.type)
    }.properties.lobbyList

    override suspend fun fetchApiInfo(): String {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(username: String, password: String): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun login(username: String, password: String): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun fetchRankings(): GomokuRankings.Rankings {
        TODO("Not yet implemented")
    }

    override suspend fun createLobby(token: String, rules: Rules): Flow<WaitingLobby> {
        TODO("Not yet implemented")
    }

    override suspend fun lobbyInfo(token: String, lobbyId: String): WaitingLobby {
        TODO("Not yet implemented")
    }

    override suspend fun enterLobby(token: String, lobby: WaitingLobby): Flow<ReadyLobby> {
        TODO("Not yet implemented")
    }


    override suspend fun leaveLobby(token: String, lobbyId: String): LobbyId {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUser(token: String, userId: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun isGameCreated(token: String, lobbyId: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getGameById(token: String, gameId: String): GameInfo {
        TODO("Not yet implemented")
    }

    private suspend fun <T> Request.send(handler: (ResponseBody) -> T): T = suspendCancellableCoroutine { continuation ->
        val call = client.newCall(request = this)
        call.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(FetchGomokuError("Error", e))
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body
                    if (!response.isSuccessful || body == null) {
                        handleUnsuccessfulResponse(body, response.code, continuation)
                    } else {
                        continuation.resumeWith(Result.success(handler(body)))
                    }
                } catch (t: Throwable) {
                    continuation.resumeWithException(FetchGomokuError("Error. Remote service returned ${response.code}", t))
                }
            }
        })

        continuation.invokeOnCancellation { call.cancel() }
    }

    private fun <T> handleUnsuccessfulResponse(body: ResponseBody?, code: Int, continuation: CancellableContinuation<T>) {
        val problemJson = gson.fromJson(body?.string(), ProblemJson::class.java)
        if (problemJson == null && code == 401) continuation.resumeWithException(ApiUnauthorizedException())
        else continuation.resumeWithException(ApiErrorException(problemJson = problemJson))
    }

    private fun buildRequest(
        url: URL,
        method: String = "GET",
        body: RequestBody? = null,
        token: String? = null
    ): Request = Request
        .Builder()
        .url(url)
        .also {  requestBuilder ->
            if (token != null) requestBuilder.addHeader("Authorization", "Bearer $token")
            if (method.uppercase() != "GET") requestBuilder.method(method, body)
        }
        .build()

}