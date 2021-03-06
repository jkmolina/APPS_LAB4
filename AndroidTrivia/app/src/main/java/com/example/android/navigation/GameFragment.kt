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
 * See the License for the  specific language governing permissions and
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
import com.example.android.navigation.MyApplication.Companion.globalVar
import com.example.android.navigation.databinding.FragmentGameBinding
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment() {

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)

    //we prepare the list of guests to the party
    private val guestList: MutableList<Guest> = globalVar

//CODIGO BASADO EN CODELAB #4


    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 10
    //the following two variables are meant to keep count of the invitees
    var list: String="";
    var confirmed:Int=0;
    var guestIndex=0;
    private var guest = Guest()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        binding.guest=guest

        binding.apply{
            binding.invalidateAll();
            questionText.text=guestList[guestIndex].name
            phoneText.text=guestList[guestIndex].phone
            emailText.text=guestList[guestIndex].email
        }


            setHasOptionsMenu(true)

            return binding.root
        }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, guestIndex + 1, globalVar.size)
        questionText.text=guestList[guestIndex].name
        phoneText.text=guestList[guestIndex].phone
        emailText.text=guestList[guestIndex].email

        when (item!!.itemId) {
            R.id.button_cancel -> {
                list= list+ "|"+(guestList[guestIndex].name)+": NO"
                if(guestIndex== globalVar.size-1)
                {
                    view!!.findNavController().navigate(GameFragmentDirections
                            .actionGameFragmentToGameWonFragment(list, confirmed))
                }

            }
            R.id.button_yes -> {
                confirmed++;
                list= list+ "|"+(guestList[guestIndex].name)+": SI"
                if(guestIndex==globalVar.size-1)
                {
                    view!!.findNavController().navigate(GameFragmentDirections
                            .actionGameFragmentToGameWonFragment(list, confirmed))
                }

            }

        }
        guestIndex++
        return super.onOptionsItemSelected(item)
    }


}
