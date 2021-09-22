package com.jmm.brsap.meditell.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.FragmentAddNewAreaBinding
import com.jmm.brsap.meditell.util.BaseBottomSheetDialogFragment

class AddNewArea : BaseBottomSheetDialogFragment<FragmentAddNewAreaBinding>(FragmentAddNewAreaBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun subscribeObservers() {

    }
}