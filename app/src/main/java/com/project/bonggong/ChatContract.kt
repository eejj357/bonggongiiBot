package com.project.bonggong

import com.project.bonggong.api.MessageResponse
import com.project.bonggong.api.RunResponse
import com.project.bonggong.model.Message

interface ChatContract {

    // UI 관련된 동작 처리
    interface View {
        // 메세지 입력 후, 답변 기다리는 로딩 보여주기 -> 없을 수 있음
        fun showLoading()
        fun hideLoading()

        // gpt 응답 표시
        fun displayGPTResponse(response : Message)

        // 에러 메세지 표시
        fun showError(errorMessage: String)
    }

    //
    interface Presenter {
        // 사용자 메세지 처리
        fun onUserInput(input: String)
    }

    // ChatGPT api 통신 처리
    interface Model {
        // thread 생성 및 message 추가, run 생성
        fun createThreadAndRun(input: String, callback: (RunResponse) -> Unit, errorCallback: (Throwable) -> Unit)

        // run 생성
        fun createRun(input: String, threadId: String, callback: (RunResponse) -> Unit, errorCallback: (Throwable) -> Unit)

        // run 검색 (retrieve)
        fun retrieveRun(threadId: String, runId: String, output: (List<MessageResponse>) -> Unit, errorCallback: (Throwable) -> Unit)
        // 일정 시간 대기 후 run 검색 재시도
        fun delayRetry(threadId: String, runId: String, output: (List<MessageResponse>) -> Unit, errorCallback: (Throwable) -> Unit)

        // 해당 thread의 message list 가져오기
        fun listMessages(threadId: String, output: (List<MessageResponse>) -> Unit, errorCallback: (Throwable) -> Unit)
    }
}