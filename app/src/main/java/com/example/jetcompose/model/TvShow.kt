package com.example.jetcompose.model

import com.example.jetcompose.R
import java.io.Serializable

data class TvShow(
    val id: Int,
    val name: String,
    val year: String,
    val rating: String,
    val image: Int,
    val overView: String
) : Serializable {
    companion object {
        fun tvShowList(): List<TvShow> {
            return listOf(
                TvShow(
                    id = 1,
                    name = "Lucifer",
                    year = "2016",
                    rating = "8.1",
                    image = R.drawable.image_1,
                    overView = "Hilt code generation needs access to all the Gradle modules that use Hilt. The Gradle module that compiles your Application class needs to have all Hilt modules and constructor-injected classes in its transitive dependencies."
                ),
                TvShow(
                    id = 2,
                    name = "Lucifer",
                    year = "2016",
                    rating = "8.1",
                    image = R.drawable.image_1,
                    overView = "Hilt code generation needs access to all the Gradle modules that use Hilt. The Gradle module that compiles your Application class needs to have all Hilt modules and constructor-injected classes in its transitive dependencies."
                ),
                TvShow(
                    id = 3,
                    name = "Lucifer",
                    year = "2016",
                    rating = "8.1",
                    image = R.drawable.image_1,
                    overView = "Hilt code generation needs access to all the Gradle modules that use Hilt. The Gradle module that compiles your Application class needs to have all Hilt modules and constructor-injected classes in its transitive dependencies."
                )
            )
        }
    }
}
