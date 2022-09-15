package com.ekenya.rnd.auth.ui.fragments.login


//class LoginFragmentqw : BaseDaggerFragment() {
//
//    @Inject
//    lateinit var sharedpreferences : SharedPreferences
//
//    private lateinit var binding: FragmentLoginAuthBinding
//    private lateinit var customKeyboard: MyCustomLoginKeyboard
//    var fourDigitPin = arrayListOf<String>()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentLoginAuthBinding.inflate(layoutInflater)
//
//        return binding.root
//    }
//
////    override fun onResume() {
////        super.onResume()
////        initializeUI()
////    }
////
////    private fun initializeUI() {
////        (activity as AuthMainActivity).hideToolbar()
////        setProgressBarView()
////        val finalLogin = MyApplication(requireContext()).getIsFinalLogin()
////        if (!finalLogin) {
////            binding.loginTxt.text = "Login"
////            binding.name.visibility = GONE
////        } else {
////            binding.name.visibility = VISIBLE
//////                if (checkIfFingerprintRecognitionSupported(requireContext())){
//////                    binding.name.visibility = VISIBLE
//////                } else {
//////                    findNavController().navigate(R.id.action_loginFragment_to_enableVoiceFragment)
//////                }
////        }
////        customKeyboard = binding.keyboard
////        setCustomKeyboard()
////        asteriskPasswordMask()
////
////        binding.forgotPin.setOnClickListener {
////            findNavController().navigate(R.id.action_loginFragment_to_forgotPinFragment)
////        }
////    }
////
////    private fun asteriskPasswordMask() {
////        binding.codeOne.transformationMethod = AsteriskPasswordTransformationMethod()
////        binding.codeTwo.transformationMethod = AsteriskPasswordTransformationMethod()
////        binding.codeThree.transformationMethod = AsteriskPasswordTransformationMethod()
////        binding.codeFour.transformationMethod = AsteriskPasswordTransformationMethod()
////    }
////
////    private fun setCustomKeyboard() {
////        binding.codeOne.requestFocus()
////        customKeyboard.setInputConnection(binding.codeOne.onCreateInputConnection(EditorInfo()))
////
////        pinValueSet()
////        pinValueDeleted()
////
////        customKeyboard.checkBtnClicked.observe(viewLifecycleOwner) {
////            if (it) {
////                findNavController().navigate(R.id.action_loginFragment_to_noBiometricsDialogFragment)
////            }
////        }
////    }
////
////    private fun pinValueSet() {
////        customKeyboard.pinValue.observe(viewLifecycleOwner) {
////            if (it.isNotBlank()) {
////                when (fourDigitPin.size) {
////                    0 -> {
////                        fourDigitPin.add(it)
////                        binding.codeOne.setText(fourDigitPin[0])
////                        binding.codeOne.setBackgroundDrawable(resources.getDrawable(R.drawable.darkblue_circle_background))
////                    }
////                    1 -> {
////                        fourDigitPin.add(it)
////                        binding.codeOne.setText(fourDigitPin[0])
////                        binding.codeTwo.setText(fourDigitPin[1])
////                        binding.codeTwo.setBackgroundDrawable(resources.getDrawable(R.drawable.darkblue_circle_background))
////                    }
////                    2 -> {
////                        fourDigitPin.add(it)
////                        binding.codeOne.setText(fourDigitPin[0])
////                        binding.codeTwo.setText(fourDigitPin[1])
////                        binding.codeThree.setText(fourDigitPin[2])
////                        binding.codeThree.setBackgroundDrawable(resources.getDrawable(R.drawable.darkblue_circle_background))
////                    }
////                    3 -> {
////                        fourDigitPin.add(it)
////                        binding.codeOne.setText(fourDigitPin[0])
////                        binding.codeTwo.setText(fourDigitPin[1])
////                        binding.codeThree.setText(fourDigitPin[2])
////                        binding.codeFour.setText(fourDigitPin[3])
////                        binding.codeFour.setBackgroundDrawable(resources.getDrawable(R.drawable.darkblue_circle_background))
////
////                        val finalLogin = MyApplication(requireContext()).getIsFinalLogin()
////                        lifecycleScope.launch {
////                            binding.progressBarView.visibility = VISIBLE
////                            binding.progressBar.visibility = VISIBLE
////                            delay(1000L)
////                            binding.progressBarView.visibility = GONE
////                            binding.progressBar.visibility = GONE
////                            if (!finalLogin) {
////                                findNavController().navigate(R.id.action_loginFragment_to_newPinFragment)
////                            } else {
////
////                                checkBiometricsSupport()
////
////                            }
////                        }
////                    }
////                    4 -> {
////                        fourDigitPin[3] = it
////                        binding.codeOne.setText(fourDigitPin[0])
////                        binding.codeTwo.setText(fourDigitPin[1])
////                        binding.codeThree.setText(fourDigitPin[2])
////                        binding.codeFour.setText(fourDigitPin[3])
////                        binding.codeFour.setBackgroundDrawable(resources.getDrawable(R.drawable.darkblue_circle_background))
////                    }
////                }
////                customKeyboard.pinValue.value = ""
////            }
////        }
////    }
////
////    private fun checkBiometricsSupport() {
////
////
////
//////        val biometricManager = BiometricManager.from(requireContext())
//////        if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
//////            findNavController().navigate(R.id.action_loginFragment_to_enableFingerprintFragment)
//////        }
////
//////        if (!sharedpreferences.getBoolean("voice",false)){
//////            findNavController().navigate(R.id.action_loginFragment_to_enableVoiceFragment)
//////        }
//////
//////        if (!sharedpreferences.getBoolean("face",false)){
//////           findNavController().navigate(R.id.action_loginFragment_to_enableFaceFragment)
//////        }
////
////    }
////
////    private fun pinValueDeleted() {
////        customKeyboard.deletePinValue.observe(viewLifecycleOwner) {
////            if (it) {
////                when (fourDigitPin.size) {
////                    1 -> {
////                        fourDigitPin.removeLast()
////                        binding.codeOne.setText("")
////                        binding.codeOne.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background))
////                    }
////                    2 -> {
////                        fourDigitPin.removeLast()
////                        binding.codeTwo.setText("")
////                        binding.codeTwo.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background))
////                    }
////                    3 -> {
////                        fourDigitPin.removeLast()
////                        binding.codeThree.setText("")
////                        binding.codeThree.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background))
////                    }
////                    4 -> {
////                        fourDigitPin.removeLast()
////                        binding.codeFour.setText("")
////                        binding.codeFour.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background))
////                    }
////                }
////                customKeyboard.deletePinValue.value = false
////            }
////        }
////    }
////
////    private fun setProgressBarView() {
////        val displaymetrics = DisplayMetrics()
////        requireActivity().windowManager.defaultDisplay.getMetrics(displaymetrics)
////        val height = displaymetrics.heightPixels
////
////        val layout = binding.progressBarView
////        val params = layout.layoutParams
////        params.height = height
////        layout.layoutParams = ConstraintLayout.LayoutParams(params)
////    }
//}