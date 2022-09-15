package com.ekenya.rnd.tijara.ui.auth.registration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.FragmentScannningBinding

class ScannningFragment : Fragment() {
    private lateinit var binding:FragmentScannningBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentScannningBinding.inflate(layoutInflater)
        initUI()
        binding.apply {
            imageBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnPassport.setOnClickListener {
                findNavController().navigate(R.id.action_scannningFragment_to_passportFragment)
            }
        }
        return binding.root
    }
    /**call the fun CardResult from class ScannerInterfaceImp that override two fun*/
/*
    private val cardResultsCallback: ScannerInterfaceImp.CardResults = object : ScannerInterfaceImp.CardResults {
        */
/**OnCardResults we are setting the scan details we have retrieved from the ID Card*//*


        override fun onCardResult(scanResult: IdScanResult?) {
            if (scanResult != null) {
               // val ivCardPrev=binding.imgTemplate
              //  ivCardPrev.visibility = View.GONE
                */
/**set the face image bitmap from the ID as a content of imageview*//*

              //  binding.ivFaceId.setImageBitmap(scanResult.face)
              //  idBitmap = scanResult.face
                extractData(scanResult.details)
            }
        }

        override fun onCardFailure(error: String) {
            toastyInfos( "Error: $error")
        }
    }
*/
   /* *//**this object override 3 method*//*
    private val livenessResultsCallback: ScannerInterfaceImp.LivenessResults = object : ScannerInterfaceImp.LivenessResults {
        override fun isLive(result: Boolean) {
            if (result) {
                showToast(requireContext(), "Face is live")
            } else {
                showToast(requireContext(), "Face is not live")
            }
        }
        override fun checkFailed(error: String) {
            showToast(requireContext(), "Liveness check has failed $error")
        }

        override fun faceImage(face: Bitmap) {
            binding.faceTemplate.visibility = View.GONE
            binding.tvInstruction.visibility = View.GONE
            *//**set the face image from the camera photo*//*
            binding.ivFace.setImageBitmap(face)
            cameraBitmap = face

        }
    }

    *//**this VerificationScore interface has 2 fun *//*
    private val verificationScoreCallback: ScannerInterfaceImp.VerificationScore = object : ScannerInterfaceImp.VerificationScore {
        *//**checks the similarity value *//*
        override fun matchScore(result: Float) {
            showResultsDialog(result)
        }

        override fun matchFailed(error: String) {
            showToast(requireContext(), error)
        }
    }*/


    /**
     * Method for requesting camera permission
     */

/*
    private fun extractData(details: CardDetails) {
        */
/**front side details*//*

        */
/*val fname=result.data?.getStringExtra("FNAME")
        val mname=result.data?.getStringExtra("MNAME")
        val sname=result.data?.getStringExtra("SNAME")
        val idno=result.data?.getStringExtra("IDNO")
        val gender=result.data?.getStringExtra("GENDER")
        val dob=result.data?.getStringExtra("DOB")
        val bitmap=result.data?.getParcelableExtra<Bitmap>("BITMAP")*//*

        val directions=ScannningFragmentDirections.
        actionScannningFragmentToNationalIDFragment(details.firstName,details.middleName,details.surname,
            details.cardNumber,details.gender,details.dob)
        findNavController().navigate(directions)


    }
*/





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initUI() {
        /**On this button click we call the fun ScanId which opens a scanning area
         * params ->context, ID Card and The scanned results form the captured image*/
        binding.btnNationalId.setOnClickListener {
           findNavController().navigate(R.id.action_scannningFragment_to_nationalIDFragment)

        }

    }

    /**
     * Method for requesting camera permission
     */

}