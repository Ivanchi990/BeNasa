package com.business.benasa.content

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.business.benasa.R
import com.business.benasa.databinding.FragmentStartBinding
import com.business.benasa.entities.Apod
import com.business.benasa.nasapi.ApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartFragment : Fragment()
{
    private lateinit var binding: FragmentStartBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val frag = FragmentStartBinding.inflate(layoutInflater)
        binding = frag

        requestApod()

        return frag.root
    }

    private fun requestApod() {
        ApiInstance.api.getApod("planetary/apod?api_key=${getString(R.string.api_key)}").enqueue(object :
            Callback<Apod> {
            override fun onResponse(call: Call<Apod>, response: Response<Apod>)
            {
                if (response.body() != null)
                {
                    binding.apodTitle.text = response.body()!!.title
                    val copyright = response.body()!!.copyright?.replace("\n", "")
                    binding.apodAuthor.text = "${binding.apodAuthor.text} $copyright"
                    binding.apodDate.text = "${binding.apodDate.text} ${response.body()!!.date}"

                    Glide.with(binding.root)
                        .load(Uri.parse(response.body()!!.hdurl))
                        .error(R.drawable.nasa_logo_svg)
                        .fitCenter().into(binding.apodImage)

                    binding.apodDescription.text = response.body()!!.explanation
                }
            }

            override fun onFailure(call: Call<Apod>, t: Throwable)
            {

            }
        })
    }
}