package com.business.benasa.content

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.business.benasa.R
import com.business.benasa.databinding.FragmentStartBinding
import com.business.benasa.entities.Apod
import com.business.benasa.nasapi.ApiInstance
import io.getstream.photoview.PhotoView
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
        configOnClicks()

        return frag.root
    }

    private fun configOnClicks()
    {
        binding.apodImage.setOnClickListener {
            val drawable = binding.apodImage.drawable
            showImageFullScreen(drawable)
        }

        binding.btnNext.setOnClickListener {
            showActions()
        }
    }

    private fun showActions()
    {
        val actionsFragment = ContentFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fvContent, actionsFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showImageFullScreen(drawable: Drawable?)
    {
        drawable?.let {
            val photoView = PhotoView(requireContext())
            photoView.setImageDrawable(it)
            photoView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            photoView.adjustViewBounds = true
            photoView.scaleType = ImageView.ScaleType.FIT_CENTER

            val popupWindow = PopupWindow(
                photoView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
            )

            popupWindow.setBackgroundDrawable(resources.getDrawable(R.color.black))

            photoView.setOnClickListener {
                popupWindow.dismiss()
            }

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        }
    }

    private fun requestApod() {
        ApiInstance.api.getApod("planetary/apod?api_key=${getString(R.string.api_key)}").enqueue(object :
            Callback<Apod> {
            @SuppressLint("SetTextI18n")
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

                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Apod>, t: Throwable)
            {

            }
        })
    }
}