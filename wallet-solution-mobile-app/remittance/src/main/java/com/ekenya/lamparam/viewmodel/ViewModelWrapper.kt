package com.ekenya.kcbpocr.viewModel


sealed class ViewModelWrapper<out T> {
    data class  error( val error: String) : ViewModelWrapper<String>()
    data class response<out T>(val value: T): ViewModelWrapper<T>()

}