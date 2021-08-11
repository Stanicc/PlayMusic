package stanic.playmusic

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.runners.MockitoJUnitRunner
import java.io.File
import java.io.FileFilter
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class FileSaveUnitTest {

    @Rule @JvmField
    val tempFolder = TemporaryFolder()

    @Mock
    lateinit var context: Context

    @Before
    @Throws(IOException::class)
    fun setUp() {
        initMocks(this)
        `when`(context.filesDir).thenReturn(tempFolder.newFolder())
        `when`(context.getDir("tracks", Context.MODE_PRIVATE)).thenReturn(tempFolder.newFolder("tracks"))
    }

    @Test
    fun `save files in internal storage and load it`() {
        val startMS = System.currentTimeMillis()
        //SAVE
        val directory = context.getDir("tracks", Context.MODE_PRIVATE)
        mockMusicList().forEach {
            val file = File(directory, "${it.name}.${it.extension}")
            file.createNewFile()
            println("Saving file: ${file.name}")
        }

        //LOAD
        val list = ArrayList<MusicTestModel>()
        context.getDir("tracks", Context.MODE_PRIVATE).listFiles(FileFilter { it.name.contains(".txt") || it.name.contains(".TXT") })?.toList()?.forEach {
            list.add(MusicTestModel(it.nameWithoutExtension))
            println("Loading file: ${it.name}")
        }

        println("Process took ${System.currentTimeMillis() - startMS}ms")
        assertEquals(7, list.size)
    }

    private fun mockMusicList(): List<MusicTestModel> {
        return arrayListOf(
            MusicTestModel("text"),
            MusicTestModel("text2"),
            MusicTestModel("text3"),
            MusicTestModel("text4"),
            MusicTestModel("text5"),
            MusicTestModel("text6"),
            MusicTestModel("text7")
        )
    }

    class MusicTestModel(
        val name: String,
        val extension: String = "txt"
    )

}
