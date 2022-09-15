package com.ekenya.rnd.tmd.ui.fragments.login.speechrecognition

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentVoiceRegistrationBinding
import com.ekenya.rnd.tmd.ui.fragments.login.speechrecognition.speech_animator.adapters.RecognitionListenerAdapter
import com.ekenya.rnd.tmd.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class FragmentVoiceRegistration : BaseDaggerFragment() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var binding: FragmentVoiceRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentVoiceRegistrationBinding.inflate(layoutInflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission()
        }

        val colors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color1)
        )

        val heights = intArrayOf(60, 46, 70, 54)

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())

        val recognitionProgressView = binding.recognitionView
        recognitionProgressView.setSpeechRecognizer(speechRecognizer)
        recognitionProgressView.setRecognitionListener(object : RecognitionListenerAdapter() {

            override fun onReadyForSpeech(p0: Bundle?) {
                binding.textViewStatus.text = "Listening... "
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(p0: Float) {
            }

            override fun onBufferReceived(p0: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(p0: Int) {
                toast("Error")
            }

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                binding.textViewStatus.text = data
                if (data != null && data.lowercase(Locale.getDefault()) == "my voice is my password") {
                    val currentProgress = binding.customProgressBar.getProgress()
                    binding.customProgressBar.setProgressWithAnimation(currentProgress + 25)
                    if (arguments?.getString("intent") == "login") {
                        binding.customProgressBar.setProgressWithAnimation(100f)
                        lifecycleScope.launch {
                            delay(1000)
                            simulateSearching()
                            setFragmentResult("requestKey", bundleOf("voice" to VOICE.SUCCESS))
                            findNavController().navigateUp()
                        }
                    }
                    binding.button.text = "Continue"
                } else {
                    binding.button.text = "Try again"
                }
                recognitionProgressView.stop()
                recognitionProgressView.play()
                speechRecognizer.stopListening()
                if (binding.customProgressBar.getProgress() + 25 >= 90) {
                    lifecycleScope.launch {
                        simulateSearching()
                        setFragmentResult("requestKey", bundleOf("voice" to VOICE.SUCCESS))
                        findNavController().navigateUp()
                    }
                }
            }

            override fun onPartialResults(p0: Bundle?) {
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
            }
        })

        recognitionProgressView.setColors(colors)
        recognitionProgressView.setBarMaxHeightsInDp(heights)
        recognitionProgressView.setCircleRadiusInDp(5)
        recognitionProgressView.setSpacingInDp(5)
        recognitionProgressView.setIdleStateAmplitudeInDp(2)
        recognitionProgressView.setRotationRadiusInDp(10)
        recognitionProgressView.play()

        binding.button.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.button.text = "Listening ... "
                    recognitionProgressView.stop()
                    recognitionProgressView.play()
                    speechRecognizer.stopListening()
                    startRecognition()
                    view.performClick()
                    view.onTouchEvent(motionEvent)
                }

                MotionEvent.ACTION_UP -> {
                    view.performClick()
                    view.onTouchEvent(motionEvent)
                }
            }

            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) Toast.makeText(
                requireContext(),
                "Permission Granted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireActivity().packageName)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizer.startListening(intent)
    }

    companion object {
        const val RecordAudioRequestCode = 1
    }

    private suspend fun simulateSearching() {
        showHideProgress("We are processing your voice")
        delay(2000)
        showHideProgress(null)
    }
}
enum class VOICE {
    SUCCESS, ERROR
}
