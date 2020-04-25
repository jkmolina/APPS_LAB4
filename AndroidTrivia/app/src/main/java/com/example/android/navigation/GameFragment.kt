/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Berlin",
                    answers = listOf("All of these", "Tools", "Documentation", "Libraries")),
            Question(text = "Moscu",
                    answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")),
            Question(text = "Denver",
                    answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")),
            Question(text = "Tokio",
                    answers = listOf("Data binding", "Data pushing", "Set text", "An OnClick method")),
            Question(text = "Helsinki",
                    answers = listOf("onCreateView()", "onActivityCreated()", "onCreateLayout()", "onInflateLayout()")),
            Question(text = "Oslo",
                    answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")),
            Question(text = "Nairobi",
                    answers = listOf("VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector"))

    )

//CODIGO BASADO EN CODELAB #4

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)
    //the following two variables are meant to keep count of the invitees
    var list: String="";
    var confirmed:Int=0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton.
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            confirmed=confirmed+1;
            list= list+ "|"+(questions[questionIndex].text)+": SI"
            //we add one to the question index to iterate over our list
            questionIndex++;
            currentQuestion = questions[questionIndex]
            setQuestion()
            binding.invalidateAll()
            //if we are done checking people, then we go to the game won fragment (ending fragment).
            if (questionIndex == 6) {
                view.findNavController().navigate(GameFragmentDirections
                        .actionGameFragmentToGameWonFragment(list, confirmed))
            }
        }

        //same thing as the submitButton, but this one is for people who dont go
            binding.button.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
            { view: View ->
                list= list+ " |"+(questions[questionIndex].text)+": NO"
                questionIndex++;
                currentQuestion = questions[questionIndex]

                setQuestion()
                binding.invalidateAll()
                if (questionIndex == 6) {
                    //context!!.toast(list);
                    view.findNavController().navigate(GameFragmentDirections
                            .actionGameFragmentToGameWonFragment(list,confirmed))
                }
            }

            setHasOptionsMenu(true)

            return binding.root
        }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.button_cancel -> context!!.toast("Por favor usar botones de abajo")
            R.id.button_yes -> context!!.toast("Por favor usar botones de abajo")
        }
        return super.onOptionsItemSelected(item)
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, 6)
    }
}
