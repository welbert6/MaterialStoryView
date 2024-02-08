package com.moreirasoft.storyviewdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moreirasoft.materialstoryview.model.MaterialStory
import com.moreirasoft.materialstoryview.presentation.CarouselStoryView
import com.moreirasoft.materialstoryview.utils.MaterialStoryViewHeaderInfo
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carrosselStoryView: CarouselStoryView = findViewById(R.id.sqwfrwe)
        val story2 = MaterialStory(
            date = Date(),
            imageUrl = "https://m.media-amazon.com/images/I/71bf1B+6wVL._SL1500_.jpg",
            actText = "Visitar Google",
            actUrl = "https://www.google.com.br",
            description = "Conheça o maior buscador de pesquisa do mundo.",
            title = "Google",
        )
        carrosselStoryView.setItems(
            this,
            getModelStorys().apply {
                val materialStoryViewHeaderInfo = MaterialStoryViewHeaderInfo(
                    "Segundo",
                    "Há uma hora atrás",
                    "https://s2-techtudo.glbimg.com/PHWR4IpnXdwfzsE1Pnpx4a-I1Rg=/0x0:1152x642/888x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_08fbf48bc0524877943fe86e43087e7a/internal_photos/bs/2022/Q/B/jsII7RTcinvxCpSjcrRg/divulgacao-warner-bros.jpg",
                    arrayListOf(story2),
                )
                add(materialStoryViewHeaderInfo)
            },
        )
    }

    fun getModelStorys(): ArrayList<MaterialStoryViewHeaderInfo> {
        val myStories = java.util.ArrayList<MaterialStory>()

        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")

        val story1 = MaterialStory(
            Date(),
            "https://images.unsplash.com/photo-1521814728937-009d76026884?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            null,
            null,
            "Titulo story um",
            "Descricao story um",

        )
        myStories.add(story1)

        val story2 = MaterialStory(
            Date(),
            "https://images.unsplash.com/photo-1592806088932-05058af0ad8d?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            null,
            null,
            "Titulo story dois",
            "Descricao story dois",
        )
        myStories.add(story2)

        val story3 = MaterialStory(
            Date(),
            "https://mfiles.alphacoders.com/681/681242.jpg",
            null,
            null,
            "Titulo story tres",
            "Descricao story tres",
        )
        myStories.add(story3)

        val materialStoryViewHeaderInfo = MaterialStoryViewHeaderInfo(
            "Welbert Moreira",
            "Há uma hora atrás",
            "https://m.media-amazon.com/images/I/41cVi9VTAbL._AC_.jpg",
            arrayListOf(story1, story2, story3),
        )

        return arrayListOf(materialStoryViewHeaderInfo)
    }
}
