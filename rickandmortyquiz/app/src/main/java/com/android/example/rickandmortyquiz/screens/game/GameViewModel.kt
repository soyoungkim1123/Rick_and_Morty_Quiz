package com.android.example.rickandmortyquiz.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.rickandmortyquiz.R

data class Question(
        val questionID: Int,
        val answer: Boolean,
        var attempted: Boolean = false,
        var answered: Boolean = false
)
class GameViewModel : ViewModel() {
    private var questionIndex = 0
    private lateinit var questionBank: MutableList<Question>

    private val _scoreString = MutableLiveData<String>()
    val scoreString: LiveData<String>
        get() = _scoreString

    private val _question = MutableLiveData<Int>()
    val question: LiveData<Int>
        get() = _question

    private val _attempted = MutableLiveData<Boolean>()
    val attempted: LiveData<Boolean>
        get() = _attempted

    private val _checkTrue = MutableLiveData<Boolean>()
    val checkTrue: LiveData<Boolean>
        get() = _checkTrue

    private val _checkFalse = MutableLiveData<Boolean>()
    val checkFalse: LiveData<Boolean>
        get() = _checkFalse

    private val _isCorrect = MutableLiveData<Boolean>()
    val isCorrect: LiveData<Boolean>
        get() = _isCorrect

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun questionsAttempted() = questionBank.count {it.attempted}

    fun newGame() {
        resetQuestionBank()
        questionIndex = 0
        _eventGameFinish.value = false
        updateQuestion()
    }

    private fun updateQuestion() {
        _question.value = questionBank[questionIndex].questionID
        _attempted.value = questionBank[questionIndex].attempted
        _isCorrect.value = questionBank[questionIndex].answer == questionBank[questionIndex].answered

        _checkFalse.value = questionBank[questionIndex].attempted and !questionBank[questionIndex].answered
        _checkTrue.value = questionBank[questionIndex].attempted and questionBank[questionIndex].answered

        _scoreString.value = "Your Score: ${questionBank.count{it.answered and it.answer}}/${questionBank.size}"

        if(questionsAttempted() == questionBank.size){
            onGameFinish()
        }

    }

    private fun resetQuestionBank() {
        questionBank = mutableListOf(
                Question(R.string.question_1, false),
                Question(R.string.question_2, true),
                Question(R.string.question_3, true),
//                Question(R.string.question_4, false),
//                Question(R.string.question_5, false),
//                Question(R.string.question_6, true),
//                Question(R.string.question_7, false),
//                Question(R.string.question_8, true),
//                Question(R.string.question_9, false),
//                Question(R.string.question_10, false),
//                Question(R.string.question_11, false),
//                Question(R.string.question_12, true),
//                Question(R.string.question_13, false),
//                Question(R.string.question_14, true),
//                Question(R.string.question_15, false),
//                Question(R.string.question_16, false),
//                Question(R.string.question_17, true),
//                Question(R.string.question_18, false),
//                Question(R.string.question_19, false),
//                Question(R.string.question_20, true)
        )

        questionBank.shuffle()
    }

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        newGame()
    }

    fun prevQuestion() {
        if(questionIndex == 0){
            questionIndex = questionBank.size - 1
        } else {
            questionIndex--
        }
        updateQuestion()
    }

    fun nextQuestion() {
        if(questionIndex == questionBank.size - 1){
            questionIndex = 0
        } else {
            questionIndex++
        }
        updateQuestion()
    }

    fun onSelected(rdBtnType: Boolean){
        questionBank[questionIndex].attempted = true

        if(rdBtnType){
            questionBank[questionIndex].answered = true
        }

        updateQuestion()
    }
}