package com.project.bonggong.presenter

import android.util.Log
import com.project.bonggong.ChatContract
import com.project.bonggong.R
import com.project.bonggong.api.MessageResponse
import com.project.bonggong.api.RetrofitInstance
import com.project.bonggong.api.RunResponse
import com.project.bonggong.model.Message

class ChatPresenter(
    private var view: ChatContract.View,
    private var model: ChatContract.Model
): ChatContract.Presenter {

    private val lengthLimit = 200
    private var threadId: String? = null  // thread 생성 여부를 확인

    override fun onUserInput(input: String) {

        // 입력 길이 검증
        if (input.length > lengthLimit) {
            view.showError("입력은 " + lengthLimit + "자 이하로 제한됩니다.")
            return
        }

        Log.d("ChatPresenter.onUserInput", "사용자 입력 : $input")

        if (threadId == null) {
            // 1. 첫 번째 사용자 입력이 들어 왔을 때
            // thread 생성 및 message 추가, run 생성
            model.createThreadAndRun(input, { response ->
                threadId = response.thread_id // 첫 메시지의 thread ID 저장
                processMessages(threadId!!)
            }, { error ->
                view.showError(error.message ?: "알 수 없는 에러가 발생했습니다. 다시 시도해 주세요")
            })
        } else {
            // 2. 첫 번째가 아닌 사용자 입력이 들어 왔을 때
            // run 생성 (message 추가)
                model.createRun(input, threadId!!, { response ->
                    processMessages(threadId!!)
            }, { error ->
                view.showError(error.message ?: "알 수 없는 에러가 발생했습니다. 다시 시도해 주세요")
            })
        }
    }

    private fun processMessages(threadId: String) {
        // 비동기 방식으로 listMessages 호출
        model.listMessages(threadId, { messages ->
            // assistant 역할의 마지막 메시지의 text.content.value를 가져옴
            val assistantMessage = messages
                .filter { it.role == "assistant" }
                .maxByOrNull { it.created_at }
                ?.content?.firstOrNull { it.type == "text" }
                ?.text?.value

            Log.d("ChatPresenter.onUserInput", "봉공이봇 답변 : ${assistantMessage.toString()}")

            if (assistantMessage != null) {
                view.displayGPTResponse(
                    Message(assistantMessage, R.drawable.bonggong_profile, false)
                )
            } else {
                view.showError("No assistant response found.")
            }
        }, { error ->
            // 오류가 발생했을 때 처리
            view.showError(error.message ?: "An unknown error occurred.")
        })
    }
}