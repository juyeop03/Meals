package com.project.meals.view.meals.title_fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.project.meals.R
import com.project.meals.databinding.FragmentYesterdayBinding
import com.project.meals.viewmodel.meals.title_viewmodel.YesterdayViewModel
import java.text.SimpleDateFormat
import java.util.*

class YesterDayFragment : Fragment() {

    lateinit var binding : FragmentYesterdayBinding
    lateinit var viewModel : YesterdayViewModel

    lateinit var school_id : String
    lateinit var office_id : String

    var today = Date()
    var simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yesterday, container, false)
        viewModel = ViewModelProviders.of(this@YesterDayFragment).get(YesterdayViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner

        getData() // GetSchoolData
        viewModel.getMeals(school_id, office_id) // MealsServerData

        binding.date.text = simpleDateFormat.format(today)

        observerViewModel()
        return binding.root
    }

    fun getData(){
        val sharedPreferences = context!!.getSharedPreferences("checkSearch", Context.MODE_PRIVATE)
        school_id = sharedPreferences.getString("school_id", "").toString()
        office_id = sharedPreferences.getString("office_id", "").toString()
    }

    fun observerViewModel(){
        with(viewModel){
            onBreakfastEvent.observe(this@YesterDayFragment, Observer {
                val adapter = TitleMealsAdapter(viewModel.breakfastList)
                binding.recyclerView.adapter = adapter

                Log.e("급식 [아침]", "아침 급식을 성공적으로 조회하였습니다.")
                binding.mealsTextView.text = "아침"
                viewModel.checkCount = 1
            })
            onLunchEvent.observe(this@YesterDayFragment, Observer {
                val adapter = TitleMealsAdapter(viewModel.lunchList)
                binding.recyclerView.adapter = adapter

                Log.e("급식 [점심]", "점심 급식을 성공적으로 조회하였습니다.")
                binding.mealsTextView.text = "점심"
                viewModel.checkCount = 2
            })
            onDinnerEvent.observe(this@YesterDayFragment, Observer {
                val adapter = TitleMealsAdapter(viewModel.dinnerList)
                binding.recyclerView.adapter = adapter

                Log.e("급식 [저녁]", "저녁 급식을 성공적으로 조회하였습니다.")
                binding.mealsTextView.text = "저녁"
                viewModel.checkCount = 3
            })
            onFailEvent.observe(this@YesterDayFragment, Observer {
                Toast.makeText(context, "더 이상 넘어갈 수 없습니다.", Toast.LENGTH_SHORT).show()
            })
        }
    }
}
