package tiger.spike.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tiger.spike.maps.R

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var noteFilter: EditText
    private lateinit var userNameFilter: EditText
    private lateinit var noteButton: Button
    private lateinit var userNameButton: Button
    private var mListener: ItemClickListener? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteFilter = view.findViewById(R.id.note_ed)
        userNameFilter = view.findViewById(R.id.user_ed)
        noteButton = view.findViewById(R.id.note_button)
        userNameButton = view.findViewById(R.id.username_button)
        setUpViews()
    }

    private fun setUpViews() {
        noteButton.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("note", noteFilter.text.toString())
        }

        userNameButton.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("userName", userNameFilter.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context
        } else {
            throw RuntimeException(
                    "$context must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface ItemClickListener {
        fun onItemClick(item: String, filter: String)
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): OptionsBottomSheetFragment {
            val fragment = OptionsBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}