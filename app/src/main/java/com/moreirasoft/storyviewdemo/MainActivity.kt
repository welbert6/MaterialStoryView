package com.moreirasoft.storyviewdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moreirasoft.materialstoryview.model.MaterialStory
import com.moreirasoft.materialstoryview.model.MaterialStoryViewHeaderInfo
import com.moreirasoft.materialstoryview.presentation.customviews.CarouselStoryView
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val carrosselStoryView: CarouselStoryView = findViewById(R.id.carouselStoryView)

        carrosselStoryView.initWithActivity(this)
        carrosselStoryView.addStory(getCopasaStories())
        carrosselStoryView.addStory(getCemigStories())
        carrosselStoryView.addStory(getCminStories())
        carrosselStoryView.addStories(getModelStorys())
    }

    fun getCopasaStories(): MaterialStoryViewHeaderInfo {
        val copasaStories = arrayListOf(
            MaterialStory(
                Date(1707791031887),
                "https://media.istockphoto.com/id/505176828/pt/foto/esta%C3%A7%C3%A3o-de-tratamento-de-%C3%A1gua-ao-p%C3%B4r-do-sol.webp?s=2048x2048&w=is&k=20&c=GqHgCT3IfSkaieYpbCqEh3_yoAZIOe9jIFlq7d_X-jQ=",
                "Ir para o site",
                "https://www.copasa.com.br/wps/portal/internet/a-copasa/copasa-em-movimento/!ut/p/z1/rVLJbsIwEP2WHnJ0PCErvaVI7JSKikJ8qYJjgitsh8SQ0q-vg-ihEkSqVF88tt9o3mJM8BoTmZ54nmquZLo354QE71OYxMPJBMbzxagP8WQxH8xHT4vw2cGrCwDurBgwMc8vMOo53gDG0RAiiKPQ6Q574PreT38LgLTPf8MEk4LyDCd-xjbUoRmKzIY8U6JuEGTITT2P-iELvZA1aCp1oXc4qbhmiKoirVKzSc2Omaos4KYqJdNNVWmuj_RihQXpFWzBtYkJJNSJCya1ugppYUrafVo11NohY0z4Rtg1FTbYLoRBN_L9juuFUeD4TVSx3LhRjknJtqxkpX0sTYI7rYvq0QIL6rq2c6XyPbOpEhbcatmpSuP1byROjLbwrra-SfHEWY2XUpXCfJrXP2YyvEhrd89Q5R-HA4lNfk1Wn4bl_wZoJnTKWW9mzChSvUNcbs3lLWQhlksRuWckv6ZshZLxqT67-_zhG3iLzwo!/dz/d5/L2dBISEvZ0FBIS9nQSEh/?urile=wcm%3Apath%3A%2Fsite-copasa-conteudos%2Finternet%2Finstitucional%2Fa-copasa%2Fcopasa-em-movimento",
                " Copasa em Movimento",
                "A Transformação é a essência do nosso negócio.\n" +
                        "Gerir e gerar a transformação é o propósito da nossa marca.",

                ),
            MaterialStory(
                Date(1707891031887),
                "https://images.unsplash.com/photo-1592806088932-05058af0ad8d?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                null,
                null,
                "Transformação no Atendimento ao Cliente",
                "A Copasa tem investido cada vez mais na centralidade, digitalização e modernização para melhorar o relacionamento com o cliente.",
            ),
            MaterialStory(
                Date(1708991031887),
                "https://mfiles.alphacoders.com/681/681242.jpg",
                null,
                null,
                "Transformação nos sistemas de saneamento",
                "A Copasa está investindo 8.1 bilhões entre 2023 e 2027 na ampliação, qualificação e manutenção dos sistemas de engenharia de saneamento.",
            )
        )


        return MaterialStoryViewHeaderInfo(
            "Copasa",
            "https://diariodecaratinga.com.br/wp-content/uploads/2023/05/16-1.jpg",
            copasaStories,
        )
    }

    fun getCemigStories(): MaterialStoryViewHeaderInfo {
        val cemigStories  = arrayListOf(
            MaterialStory(
                Date(1707791031887),
                "https://www.cemig.com.br/wp-content/uploads/2020/12/manutencao-preventiva.jpeg",
                null ,
                null,
                "Manutenção preventiva",
                "Procedimento visa reduzir interrupções no fornecimento de energia e evitar acidentes",
                ),
            MaterialStory(
                Date(1707891031887),
                "https://scontent.fqsc1-1.fna.fbcdn.net/v/t1.6435-9/133848727_1659175390936860_7779711853011003428_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=7f8c78&_nc_eui2=AeFvT-bsdkniVfz8t5FW-_nn-uHzzVpu1RT64fPNWm7VFIi2Tln4jcuSPOwBtDj--p9QNSg3t5Lohms4VoRGSQB_&_nc_ohc=Wou4d0SJieIAX-7aTaz&_nc_ht=scontent.fqsc1-1.fna&oh=00_AfCjy9xT9JCL9BU86Xo9tOVRxTToBRZTCFvM8u_MEz1IjA&oe=65F3744F",
                null,
                null,
                null,
                null,
            ),
        )


        return MaterialStoryViewHeaderInfo(
            "Cemig",
            "https://scontent.fqsc1-1.fna.fbcdn.net/v/t1.6435-9/69257601_2443442875731927_8777711081872162816_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=7a1959&_nc_eui2=AeEhCzKX-odg9JdMI8XbxVudqf1Ohpcba3Wp_U6GlxtrdQGDMH5ZZuJyjTyW8mhZ9gyrj2qVKFiCHHM7e-Dlj437&_nc_ohc=mIYN720BieEAX-EaUTH&_nc_ht=scontent.fqsc1-1.fna&oh=00_AfCJl3NJTC08VUqLiY_JDuSucWCvdBa06hDbpDcSz1mX_A&oe=65F3866A",
            cemigStories,
        )
    }

    fun getCminStories(): MaterialStoryViewHeaderInfo {
        val cminStories  = arrayListOf(
            MaterialStory(
                Date(1707791031887),
                "https://forbes.com.br/wp-content/uploads/2021/02/ForbesMoney-CSNMineracao-IPO-170221-GettyImagesHello-my-names-is-jamesIm-photographer..jpg",
                "Veja o video" ,
                "https://www.youtube.com/watch?v=VA6jE16OsPI",
                "Fazer bem",
                null,
            ),

        )


        return MaterialStoryViewHeaderInfo(
            "CSN",
            "data:image/jpeg;base64,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",
            cminStories,
        )
    }

    fun getMargringStories(): ArrayList<MaterialStoryViewHeaderInfo> {
        return arrayListOf()
    }

    fun getModelStorys(): ArrayList<MaterialStoryViewHeaderInfo> {
        val myStories = java.util.ArrayList<MaterialStory>()

        val story1 = MaterialStory(
            Date(1707791031887),
            "https://images.unsplash.com/photo-1521814728937-009d76026884?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            null,
            null,
            "Titulo story um",
            "Descricao story um",

            )
        myStories.add(story1)

        val story2 = MaterialStory(
            Date(1707791031887),
            "https://images.unsplash.com/photo-1592806088932-05058af0ad8d?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            null,
            null,
            "Titulo story dois",
            "Descricao story dois",
        )
        myStories.add(story2)

        val story3 = MaterialStory(
            Date(1707791031887),
            "https://img.freepik.com/fotos-gratis/bife-suculento-medio-raro-com-especiarias-e-legumes-grelhados_2829-18668.jpg?w=740&t=st=1707868683~exp=1707869283~hmac=7bc76eaff3b47d270a7b67c74f0f56a6308ff1348383f81121bb70a440b10b79",
            null,
            null,
            null,
            null,
        )
        myStories.add(story3)

        val materialStoryViewHeaderInfo = MaterialStoryViewHeaderInfo(
            "Marfrig",
            "https://investidorsardinha.r7.com/wp-content/uploads/2020/04/marfrig-375x281.png",
            arrayListOf(story1, story2, story3),
        )

        return arrayListOf(materialStoryViewHeaderInfo)
    }
}
