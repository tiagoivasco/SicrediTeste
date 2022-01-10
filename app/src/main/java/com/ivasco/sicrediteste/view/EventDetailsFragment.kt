package com.ivasco.sicrediteste.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ivasco.sicrediteste.MainActivity
import com.ivasco.sicrediteste.R
import com.ivasco.sicrediteste.databinding.FragmentEventDetailBinding
import com.ivasco.sicrediteste.model.CheckIn
import com.ivasco.sicrediteste.util.Util
import com.ivasco.sicrediteste.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso


class EventDetailsFragment : Fragment(R.layout.fragment_event_detail) {
    private lateinit var binding: FragmentEventDetailBinding
    private var popupInputDialogView: View? = null
    private var homeViewModel = HomeViewModel()
    private lateinit var name: EditText
    private lateinit var email: EditText
    private var mMap: GoogleMap? = null
    private var mapReady = false
    private var eventId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventDetailBinding.bind(view)

        checkInResponse()
        setupMap()
        setData()

        binding.btnCheckIn.setOnClickListener {
            checkInAlertDialog()
        }

        binding.btnShare.setOnClickListener {
            shareFunction()
        }

        val main = activity as MainActivity
        main.setToolbarTitle(getString(R.string.label_event_details))
    }

    private fun setData() {
        eventId = arguments?.getString("eventId").toString()
        val title = arguments?.getString("eventName")
        val date = arguments?.getString("date")
        val description = arguments?.getString("description")
        val price = arguments?.getString("price")
        val imageURL = arguments?.getString("image")

        binding.txtDate.text = date
        binding.txtEventName.text = title
        binding.txtEventDescription.text = description
        binding.txtPrice.text = String.format(getString(R.string.txt_preco), price)

        Picasso.get()
            .load(imageURL)
            .resize(500, 500)
            .into(binding.eventImageDetails)
    }

    private fun shareFunction() {
        val texto = "Evento: " + binding.txtEventName.text + "\nData: " + binding.txtDate.text
        val uri =
            "$texto\nhttp://maps.google.com/maps?saddr=" + requireArguments().getDouble("lat")
                .toString() + "," + requireArguments().getDouble("long")
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, texto)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareTitle)))
    }

    private fun checkInAlertDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        popupInputDialogView = layoutInflater.inflate(R.layout.checkin_alert_dialog, null)
        showCheckInDialog()
    }

    private fun showCheckInDialog() {
        val dialog: AlertDialog = AlertDialog.Builder(requireContext())
            .setView(popupInputDialogView)
            .setTitle(getString(R.string.btn_check_in))
            .setPositiveButton(android.R.string.ok, null)
            .setNegativeButton(
                android.R.string.cancel
            ) { _, _ ->
                hideKeyboard()
            }.create()

        dialog.setOnShowListener {
            val button: Button =
                (dialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {

                name =
                    (popupInputDialogView.let { popupInputDialogView?.findViewById<View>(R.id.edit_nome) as EditText })
                email =
                    (popupInputDialogView.let { popupInputDialogView?.findViewById<View>(R.id.edit_email) as EditText })

                //utilizar resource string nos campos.
                //melhorar essa validação
                if (name.text.isNullOrBlank()) {
                    name.error = "Todos os campos devem ser preenchidos."
                } else if (email.text.isNullOrBlank()) {
                    email.error = "É preciso colocar um email valido."
                } else if (!name.text.isNullOrBlank() && !email.text.isNullOrBlank()) {
                    if (!Util.isValidEmail(email.text.toString())) {
                        email.error = "É preciso colocar um email válido."
                    } else {
                        val postInfo =
                            CheckIn(eventId, name.text.toString(), email.text.toString())
                        homeViewModel.postEvent(postInfo)
                        hideKeyboard()
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mapReady = true
            onMapReady(googleMap)
        }
    }

    private fun onMapReady(googleMap: GoogleMap) {
        val lat = requireArguments().getDouble("lat")
        val long = requireArguments().getDouble("long")
        mMap = googleMap
        val marker = LatLng(lat, long)
        mMap?.addMarker(
            MarkerOptions()
                .position(marker)
                .title("Evento")
        )
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 15.0F))
    }

    private fun dialogConfirmCheckIn() {
        val dialog: AlertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.sucess))
            .setMessage(getString(R.string.checkinSucess))
            .setPositiveButton(android.R.string.ok, null)
            .create()
        dialog.show()
    }

    private fun checkInResponse() {
        homeViewModel.eventPostResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Boolean -> {
                    if (response) dialogConfirmCheckIn()
                    else {
                        dialogErroCheckIn()
                    }
                }
            }
        })
    }

    private fun dialogErroCheckIn() {
        val dialog: AlertDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error))
            .setMessage(getString(R.string.checkinError))
            .setPositiveButton(android.R.string.ok, null)
            .create()
        dialog.show()
    }

    private fun hideKeyboard() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }
}