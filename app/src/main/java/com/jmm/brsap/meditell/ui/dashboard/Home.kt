package com.jmm.brsap.meditell.ui.dashboard

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.adapters.DashboardWidgetAdapter
import com.jmm.brsap.meditell.databinding.FragmentHomeBinding
import com.jmm.brsap.meditell.model.WidgetModel
import com.jmm.brsap.meditell.ui.currentdayactivity.CurrentActiveDayActivity
import com.jmm.brsap.meditell.ui.doctorpharmacy.AddDoctorOrPharmacy
import com.jmm.brsap.meditell.ui.doctorpharmacy.SearchDoctorPharmacy
import com.jmm.brsap.meditell.ui.schedule.ManageSchedule
import com.jmm.brsap.meditell.util.*

import com.jmm.brsap.meditell.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jmm.brsap.meditell.model.ReportModel
import java.lang.Math.random
import java.text.SimpleDateFormat

@AndroidEntryPoint
class Home : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    DashboardWidgetAdapter.DashboardWidgetInterface {

    private val PICKED_FILE_CODE = 100
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var dashboardWidgetAdapter: DashboardWidgetAdapter
    private var userId = ""
    var storageReference: StorageReference? = null
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainDashboard).setToolbarTitle("Meditell")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRvMenus()
        populateMenus()
        storageReference = FirebaseStorage.getInstance().reference
        binding.tvTime.text = convertDMY2EMDY(getTodayDate())

        binding.btnStartDay.setOnClickListener {
            val currentTime = SDF_DMYHMS.format(Date()).toString()

            viewModel.markAttendanceForTheDay(
                userId,
                getTodayDate(),
                currentTime,
                FirebaseDB.CHECKIN
            )
//            findNavController().navigate(R.id.action_home2_to_activeDay)

        }
    }

    private fun populateMenus() {
        val widgetList = mutableListOf<WidgetModel>()
        widgetList.add(
            WidgetModel(
                NavigationEnum.MANAGE_SCHEDULE,
                "Manage Schedule",
                R.drawable.ic_manage_schedule
            )
        )
        widgetList.add(
            WidgetModel(
                NavigationEnum.ADD_NEW_LOCATION,
                "Add new location",
                R.drawable.ic_add_new_location
            )
        )
        widgetList.add(
            WidgetModel(
                NavigationEnum.DAILY_CALL_RECORDING,
                "Daily Doctor Call",
                R.drawable.ic_daily_call_recording
            )
        )
        widgetList.add(
            WidgetModel(
                NavigationEnum.ATTENDANCE_REGISTER,
                "Attendance Register",
                R.drawable.ic_attendance_register
            )
        )
        widgetList.add(
            WidgetModel(
                NavigationEnum.ADD_DOCTOR_PHARMACY,
                "Add Doctor or Pharmacy",
                R.drawable.ic_add_doctor_pharmacy
            )
        )
        widgetList.add(
            WidgetModel(
                NavigationEnum.SEARCH_DOCTOR_PHARMACY,
                "Search Doctor or Pharmacy",
                R.drawable.ic_search_doctor_pharmacy
            )
        )

        widgetList.add(
            WidgetModel(
                NavigationEnum.SEND_REPORT,
                "Send Report to Admin",
                R.drawable.img_send_report
            )
        )

        dashboardWidgetAdapter.setWidgetModelList(widgetList)
    }

    override fun subscribeObservers() {
        viewModel.userId.observe(viewLifecycleOwner, {
            userId = it
        })
        viewModel.markAttendanceResponse.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it) {
                            val intent =
                                Intent(requireActivity(), CurrentActiveDayActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                    }
                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })

        viewModel.reportSubmitted.observe(this, { _result ->
            when (_result.status) {
                Status.SUCCESS -> {
                    _result._data?.let {
                        if (it) {
                            showToast("Report submitted successfully !!!")
                        }

                    }
                    displayLoading(false)
                }
                Status.LOADING -> {
                    displayLoading(true)
                }
                Status.ERROR -> {
                    displayLoading(false)
                    _result.message?.let {
                        displayError(it)
                    }
                }
            }
        })
    }

    private fun setupRvMenus() {
        dashboardWidgetAdapter = DashboardWidgetAdapter(this)
        binding.rvMenus.apply {
            setHasFixedSize(true)
//            val spanCount = lcm(2,2,2,1)
            val layoutManager = GridLayoutManager(context, 2)
            this.layoutManager = layoutManager
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val numberOfColumns: Int = when (position) {
                        6->2
                        else -> 1
                    }
                    return numberOfColumns
                }
            }

//            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = dashboardWidgetAdapter
        }
    }
/*

    override fun onMenuClick(menuId: Int) {
        when(menuId){
            0->{startActivity(Intent(requireActivity(), ManageSchedule::class.java))}
            1->{
                val sheet = AddNewLocation()
                sheet.show(parentFragmentManager,sheet.tag)
            }
        }
    }
*/

    override fun onItemClick(item: WidgetModel) {
        when (item.id) {
            NavigationEnum.MANAGE_SCHEDULE -> {
                startActivity(Intent(requireActivity(), ManageSchedule::class.java))
            }
            NavigationEnum.ADD_DOCTOR_PHARMACY -> {
                startActivity(Intent(requireActivity(), AddDoctorOrPharmacy::class.java))
            }
            NavigationEnum.ADD_NEW_LOCATION -> {
                startActivity(Intent(requireActivity(), AddNewLocation::class.java))
            }
            NavigationEnum.SEARCH_DOCTOR_PHARMACY -> {
                startActivity(Intent(requireActivity(), SearchDoctorPharmacy::class.java))
            }
            NavigationEnum.ATTENDANCE_REGISTER -> {
                findNavController().navigate(HomeDirections.actionHome2ToAttendanceRegister())
            }
            NavigationEnum.DAILY_CALL_RECORDING -> {
                findNavController().navigate(HomeDirections.actionHome2ToDailyCallRecording())
            }
            NavigationEnum.SEND_REPORT->{
//                val mimeTypes = arrayOf(
//                    "image/*",
//                    "application/pdf",
//                    "application/msword",
//                    "application/vnd.ms-powerpoint",
//                    "application/vnd.ms-excel",
//                    "text/plain"
//                )
//
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.addCategory(Intent.CATEGORY_OPENABLE)
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
//                    if (mimeTypes.size > 0) {
//                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//                    }
//                } else {
//                    var mimeTypesStr = ""
//                    for (mimeType in mimeTypes) {
//                        mimeTypesStr += "$mimeType|"
//                    }
//                    intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
//                }

                val gallery = Intent()
                gallery.type = "application/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(gallery, 100)
//                startActivityForResult(Intent.createChooser(gallery, "Choose File"), 0)
            }
            else -> {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICKED_FILE_CODE){
            if (resultCode==RESULT_OK){
                val contentUri: Uri? = data!!.data
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val fileName = "REPORT_" + timeStamp + "." + getFileExt(contentUri!!)
                Log.d("tag", "onActivityResult: Picked file Uri:  $fileName")

                uploadFileToFirebase(fileName, contentUri)
            }
        }

    }

    private fun getFileExt(contentUri: Uri): String? {
        val c: ContentResolver = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c.getType(contentUri))
    }

    private fun uploadFileToFirebase(name: String, contentUri: Uri) {
        val image: StorageReference = storageReference!!.child("pictures/$name")
        image.putFile(contentUri).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener { uri ->
                viewModel.selectedFileUrl = uri.toString()
                viewModel.submitReport(userId, ReportModel(
                    reportMonth = "OCT2021${(1..10000).random()}",
                    uploadedOn = getCurrentDateTime(),
                    reportUrl = viewModel.selectedFileUrl
                ))
                Log.d(
                    "tag",
                    "onSuccess: Uploaded File URl is $uri"
                )
            }
            Toast.makeText(requireContext(), "File is uploaded.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Upload Failled.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}