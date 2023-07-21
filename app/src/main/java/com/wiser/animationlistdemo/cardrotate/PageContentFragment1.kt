package com.wiser.animationlistdemo.cardrotate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wiser.animationlistdemo.R
import kotlinx.android.synthetic.main.item_card_content1.*

class PageContentFragment1: Fragment() {

    companion object {

        private const val DETAILS_RES_ID = "detailsResId"
        fun newInstance(detailsResId: Int): PageContentFragment1 {
            val pageContentFragment1 = PageContentFragment1()
            val bundle = Bundle()
            bundle.putInt(DETAILS_RES_ID, detailsResId)
            pageContentFragment1.arguments = bundle
            return pageContentFragment1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_card_content1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_details.setImageResource(arguments?.getInt(DETAILS_RES_ID,0)?:0)
    }
}