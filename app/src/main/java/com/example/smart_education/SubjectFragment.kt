package com.example.smart_education

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smart_education.Retrofit.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_subject.*
import kotlinx.android.synthetic.main.fragment_todo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SubjectFragment : Fragment(R.layout.fragment_subject) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = activity!!.intent.extras!!.getString("Email")
        var subject = subject_spinner.selectedItem.toString()


       // val BASE_URL = "http://192.168.43.208:3000"
        val BASE_URL = "https://smarteducationv1.herokuapp.com/"

        val progress = ProgressDialog(context)
        progress.setMessage("Fetching:) ")
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.isIndeterminate = true
        progress.show()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface: RetrofitInterface =
            retrofit.create(RetrofitInterface::class.java)

        /* val map: HashMap<String?, String?> = HashMap()
         map["email"] = email
         map["date"] = currentTime*/

        val call: Call<Array<Topics>?>? = retrofitInterface.executeTopics(subject,email)
        call!!.enqueue(object : Callback<Array<Topics>?> {
            override fun onResponse(
                call: Call<Array<Topics>?>?,
                response: Response<Array<Topics>?>
            ) {
                if (response.code() == 200) {
                    val result: Array<Topics>? = response.body()
                    Toast.makeText(
                        context, "Tasks",
                        Toast.LENGTH_SHORT
                    ).show()


                    val adapt = result?.toList()?.let { TopicsAdapter(it) }
                    rv_todo.adapter = adapt
                    rv_todo.layoutManager = LinearLayoutManager(context)
                    progress.dismiss()


                } else if (response.code() == 404) {
                    Toast.makeText(
                        context, "Try Again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            }

            override fun onFailure(call: Call<Array<Topics>?>, t: Throwable) {
                Toast.makeText(
                    context, "Try Again",
                    Toast.LENGTH_SHORT
                ).show()
                progress.dismiss()
            }
        })

        add_topic.setOnClickListener {
            subject = subject_spinner.selectedItem.toString()
            val topic= textInputLayout2.editText?.text.toString()

            val map1: HashMap<String?, String?> = HashMap()
            map1["email"] = email
            map1["subject"] = subject
            map1["topic"] = topic

            val call1: Call<Void?>? = retrofitInterface.executeAddTopics(map1)
            call1!!.enqueue(object : Callback<Void?> {
                override fun onResponse(
                    call1: Call<Void?>?,
                    response: Response<Void?>
                ) {
                    if (response.code() == 200) {
                        //var result = response.body()
                        Toast.makeText(
                            context, "Topic Added",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()

                        new_todo.text?.clear()

                    } else if (response.code() == 404) {
                        Toast.makeText(
                            context, "Try Again!",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress.dismiss()
                    }
                }

                override fun onFailure(call1: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        context, "Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    progress.dismiss()
                }
            })

        }
    }

}