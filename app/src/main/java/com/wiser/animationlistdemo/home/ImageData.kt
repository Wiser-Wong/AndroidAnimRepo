package com.wiser.animationlistdemo.home

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.wiser.animationlistdemo.R

/**
 * @date: 2023/5/10 10:33
 * @author: jiaruihua
 * @desc :
 *
 */
data class ImageData(
    val id: Int,
    val src: Int = 0,
    val title: String = "",
    val bgColor: Int = 0,
    val cover: String? = null,
    var bookMark: String? = null
) {

    fun defaultColor(): Int = Color.parseColor("#cce3c6")

    fun getTvBookMark(): String {
        val st = StringBuilder()
        bookMark?.let {

            it.forEachIndexed { index, c ->

                st.append(c)
//                if (index < it.length - 1)
                    st.append("\n")
            }

        }

        return st.toString()
    }

    fun createPaletteColor(resources: Resources, color: (color: Int?) -> Unit) {
//        val bitmap = BitmapFactory.decodeResource(resources, src)
//        Palette.from(bitmap).generate {
//            println(
//                "ccccccccccccccc--------getLightMutedColor=${
//                    it?.getLightMutedColor(defaultColor())
//                }"
//            )
//
//            println(
//                "ccccccccccccccc--------getMutedColor=${
//                    it?.getMutedColor(defaultColor())
//                }"
//            )
//
//            println(
//                "ccccccccccccccc--------getDominantColor=${
//                    it?.getDominantColor(defaultColor())
//                }"
//            )
//
//            println(
//                "ccccccccccccccc--------getVibrantColor=${
//                    it?.getVibrantColor(defaultColor())
//                }"
//            )
//
//            println(
//                "ccccccccccccccc--------getDarkMutedColor=${
//                    it?.getDarkMutedColor(defaultColor())
//                }"
//            )
//            color.invoke(it?.getLightMutedColor(defaultColor()))
//        }

        color.invoke(bgColor)

    }


}


data class Card(val title: String,val smallCover:String,val cover: String,val desc:String)



object DataSource {

    fun getCards():List<Card> = mutableListOf(

        Card("各司其职","https://cdn.kaishuhezi.com/kstory/lyhd/image/44ab103d-fe01-4b93-95d0-0d0103b32c72_info_w=774_h=630_s=60565.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/ba5aa1bf-d549-4732-9a9e-af00566bf7aa_info_w=826_h=1080_s=423543.png","天宫里住着好多神仙，每个人都有自己负责的工作，各司其职。"),
        Card("人来人往","https://cdn.kaishuhezi.com/kstory/lyhd/image/dfb0d325-fdb7-4ce3-b827-bbd36328f2b0_info_w=774_h=630_s=60793.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/5983e33a-710a-4a1a-8543-6ae14370b5f2_info_w=826_h=1080_s=845880.png","嚯，京城这地儿可真是热闹，人来人往，熙熙攘攘。"),

        Card("张牙舞爪","https://cdn.kaishuhezi.com/kstory/lyhd/image/4640ce11-2e64-4715-874b-478d9518c40c_info_w=774_h=630_s=66228.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/57d1c470-a7a8-4b61-a65a-0c34cfaaf602_info_w=826_h=1080_s=798286.png","龙王一生气，就张牙舞爪地奔孙悟空扑了过去。"),
        Card("神通广大","https://cdn.kaishuhezi.com/kstory/lyhd/image/7770b608-99e5-4e6f-80c2-e1a10190cde6_info_w=774_h=630_s=63147.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/66559f8c-3689-4870-a642-6f50f66e30c9_info_w=826_h=1080_s=570278.png","俺老孙这么神通广大的人物，凭什么要归阎王管？！"),
        Card("奇珍异宝","https://cdn.kaishuhezi.com/kstory/lyhd/image/0660fbab-e508-4abc-ad3a-c238f8424b24_info_w=774_h=630_s=62835.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/3676d22c-f62c-4859-bb5d-095cee9d64ec_info_w=826_h=1080_s=699568.png","天宫这地方，到处都是奇珍异宝，随便一件都是价值连城啊。"),
        Card("无事生非","https://cdn.kaishuhezi.com/kstory/lyhd/image/b57362d1-15b0-4a79-abf8-46c40dad9bf9_info_w=774_h=630_s=59782.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/0a959dd7-54d8-4000-9a9b-6c00b40e09a9_info_w=826_h=1080_s=812770.png","要是不给孙悟空找点儿事儿做，恐怕日子长了他要出乱子，无事生非啊。"),
        Card("天罗地网","https://cdn.kaishuhezi.com/kstory/lyhd/image/95d76621-af34-40d6-9e07-7e6c16e65bca_info_w=774_h=630_s=64127.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/b49ee4cf-bd6a-465a-8e29-29a9a202c4cc_info_w=826_h=1080_s=501165.png","天兵天将听我命令，速速在花果山布下天罗地网，包围孙悟空！"),
        Card("慈眉善目","https://cdn.kaishuhezi.com/kstory/lyhd/image/b3d7ad0f-1600-4872-a6f8-46bd1b446a72_info_w=774_h=630_s=64613.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/0a9f3a24-6c42-4577-8323-e9829be2fff4_info_w=826_h=1080_s=1084769.png","花果山来了一位慈眉善目的客人，此人正是观音菩萨。"),
        Card("一心一意","https://cdn.kaishuhezi.com/kstory/lyhd/image/953a44e6-5580-4409-b575-a340cece316a_info_w=774_h=630_s=50467.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/db95fb50-6c8f-442a-b6d9-e8601b262c34_info_w=826_h=1080_s=513724.png","孙悟空正一心一意地打斗，根本没注意天上掉下个金刚圈。"),
        Card("国泰民安","https://cdn.kaishuhezi.com/kstory/lyhd/image/dcbfde63-ee1f-41c0-8046-b04cbc170403_info_w=774_h=630_s=64336.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/951f2daa-edc3-49a3-b9f9-7dc18ad35a52_info_w=826_h=1080_s=687375.png","贞观年间，唐太宗李世民统治下的唐王朝风调雨顺、国泰民安。"),
        Card("山崩地裂","https://cdn.kaishuhezi.com/kstory/lyhd/image/cca8c306-c89e-4a15-a94c-8f793aa5df4b_info_w=774_h=630_s=66036.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/1e6fc20a-bbb7-4a7d-b8c5-5779a61b441c_info_w=826_h=1080_s=705965.png","唐僧撕下山顶的黄纸条后，一阵轰隆隆巨响，山崩地裂。"),
        Card("坚持不懈","https://cdn.kaishuhezi.com/kstory/lyhd/image/7b339050-b6a3-4c1a-9359-8da803335817_info_w=774_h=630_s=65744.png","https://cdn.kaishuhezi.com/kstory/lyhd/image/0e9c5190-c5f6-4749-8c09-c75ef49f7c92_info_w=826_h=1080_s=411991.png","大圣啊，你应该坚持不懈，尊重师长，这样才能取得真经，修得正果啊！"),


    )

    fun getAudioResource(): List<ImageData> = mutableListOf(

        ImageData(
            3,
            R.drawable.op,
            "诗词来了",
            Color.parseColor("#D0DFB6"),
            bookMark = "品·诗词之美"
        ),
        ImageData(
            2,
            R.drawable.daiyu1,
            "声律启蒙",
            Color.parseColor("#E2C3A0"),
            bookMark = "听·音律之音",
            cover =  "https://cdn.kaishuhezi.com/kstory/androidapkdir/image/a8c4a84c-8ca5-4270-85ee-33af555e0124_info_w=750_h=750_s=725107.png"

    ),
        ImageData(
            3,
            R.drawable.c,
            "诗词来了",
            Color.parseColor("#D0DFB6"),
            bookMark = "品·诗词之美"
        ),
        ImageData(
            2,
            R.drawable.b,
            "声律启蒙",
            Color.parseColor("#E2C3A0"),
            bookMark = "听·音律之音"
        ),
        ImageData(
            1,
            R.drawable.a,
            "麦小米的烦恼",
            Color.parseColor("#F5C3EC"),
            bookMark = "看·少年趣事"
        ),
        ImageData(4, R.drawable.d, "史记", Color.parseColor("#F3E5E1"), bookMark = "学·国学历史"),
        ImageData(
            5,
            R.drawable.e,
            "国学启蒙",
            Color.parseColor("#DB5A37"),
            bookMark = "启·国学启蒙"
        ),
        ImageData(
            1,
            R.drawable.daiyu2,
            "麦小米的烦恼",
            Color.parseColor("#F5C3EC"),
            bookMark = "看·少年趣事"
        )
    )



    fun getImageResource(): List<ImageData> = mutableListOf(

        ImageData(
            3,
            R.drawable.c,
            "诗词来了",
            Color.parseColor("#D0DFB6"),
            bookMark = "品·诗词之美"
        ),
        ImageData(
            2,
            R.drawable.b,
            "声律启蒙",
            Color.parseColor("#E2C3A0"),
            bookMark = "听·音律之音"
        ),
        ImageData(
            1,
            R.drawable.a,
            "麦小米的烦恼",
            Color.parseColor("#F5C3EC"),
            bookMark = "看·少年趣事"
        ),
        ImageData(4, R.drawable.d, "史记", Color.parseColor("#F3E5E1"), bookMark = "学·国学历史"),
        ImageData(
            5,
            R.drawable.e,
            "国学启蒙",
            Color.parseColor("#DB5A37"),
            bookMark = "启·国学启蒙"
        ),
        ImageData(

            6,
            R.drawable.f,
            "孤舟蓑笠翁",
            Color.parseColor("#FAE67E"),
            bookMark = "读·音律诗词"
        ),
        ImageData(7, R.drawable.h, "西游记", Color.parseColor("#E3BB54"), bookMark = "玩·四大名著"),
    )



    fun getBook(): List<ImageData> = mutableListOf(
        ImageData(
            1,
            title = "西游记",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/b1e1b5a2-24c1-4c27-b17a-61a63837df24.jpg"
        ),
        ImageData(
            2,
            title = "三个火枪手",
            cover = "https://cdn.kaishuhezi.com/kstory/merchandise/image/3138d57c-f38e-4e53-8747-6e70086bf95c_info_w=500_h=660_s=117523.png"
        ),
        ImageData(
            3,
            title = "可爱的中国",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/0aa8bee8-a01c-4f56-af7d-c4a50e530740_info_w=500_h=660_s=57758.png"
        ),
        ImageData(
            4,
            title = "少年读中国哲学：老庄篇",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/5dfb6eb1-ebbb-4660-9c94-32e8ee6f3758_info_w=500_h=660_s=68583.png"
        )
        ,
        ImageData(
            5,
            title = "小饼干和围裙妈妈",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/68f83505-f351-4b73-b5df-cddbf77b053f_info_w=500_h=600_s=396332.png"
        )
        ,
        ImageData(
            6,
            title = "福尔摩斯探案全集",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/7ef3fcb6-4805-4d06-a46b-2ab36a109b73_info_w=500_h=660_s=38624.jpg"
        )
        ,
        ImageData(
            7,
            title = "萌新侦探，搞笑破案！",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/2bbea38c-fbe6-4df0-aaf8-eb5bfd8735b1_info_w=500_h=660_s=63584.png"
        )
        ,
        ImageData(
            8,
            title = "火车探案记",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/4f52bb47-2b56-4b4a-bc5d-d8e7e00e4ee6_info_w=500_h=660_s=531824.png"
        )
        ,
        ImageData(
            9,
            title = "重口味研究，有味道的科普",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/40557ed7-8dfa-4c9e-941a-1a5116348dbe_info_w=500_h=660_s=532351.png"
        )
        ,
        ImageData(
            10,
            title = "少年科普知识",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/3be9cf91-c87a-4cf3-8b54-63952cd3df57_info_w=250_h=330_s=78374.png"
        )

    )

    fun getBook2(): List<ImageData> = mutableListOf(
        ImageData(
            1,
            title = "西游记",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/b1e1b5a2-24c1-4c27-b17a-61a63837df24.jpg"
        ),
        ImageData(
            2,
            title = "红楼梦",
            cover = "https://cdn.kaishuhezi.com/kstory/merchandise/image/3138d57c-f38e-4e53-8747-6e70086bf95c_info_w=500_h=660_s=117523.png"
        ),
        ImageData(
            3,
            title = "三国演义",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/915957ef-2bcc-4f52-b0fe-5865c53a6af2.jpg"
        ),
        ImageData(
            4,
            title = "水浒传",
            cover = "https://cdn.kaishuhezi.com/kstory/story/image/5dfb6eb1-ebbb-4660-9c94-32e8ee6f3758_info_w=500_h=660_s=68583.png"
        )

    )
}
