package com.gabrielmaz.poda.views.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.controllers.*
import com.gabrielmaz.poda.helpers.gone
import com.gabrielmaz.poda.helpers.visible
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo
import com.gabrielmaz.poda.models.User
import com.gabrielmaz.poda.views.MainActivity
import com.gabrielmaz.poda.views.login.LoginActivity
import com.github.ybq.android.spinkit.style.FadingCircle
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext

class ProfileFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val job = Job()
    private val authController = AuthController()
    private val userController = UserController()
    private val todoController = TodoController()
    private val categoryController = CategoryController()
    private lateinit var user: User
    lateinit var categories: ArrayList<Category>
    private lateinit var tasksCompleted: HashMap<String, Int>
    private lateinit var tasksTotal: HashMap<String, Int>
    lateinit var todos: ArrayList<Todo>

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

            profile_loading_logout.setIndeterminateDrawable(FadingCircle())
            profile_loading.setIndeterminateDrawable(FadingCircle())

            load()

            logout.setOnClickListener {
                profile_loading_logout.visible()
                logout()
            }

            profile_image_add.setOnClickListener {
                listener?.onFragmentInteraction(MainActivity.ProfileFragmentTag, LOAD_IMAGE)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(tag: String, action: String)
    }

    fun setUserData() {

        profile_view.visible()

        name.text = user.name
        joined.text = "${user.createdAt.dayOfWeek}".substring(0, 3).toLowerCase().capitalize() +
                ", ${user.createdAt.dayOfMonth} " +
                "${user.createdAt.month} ".toLowerCase().capitalize() +
                "${user.createdAt.year}"

        categories_used.text = tasksTotal.size.toString()
        total_tasks.text = getCount(tasksTotal).toString()
        tasks_completed.text = getCount(tasksCompleted).toString()
        most_used_category.text =
            if (tasksTotal.isNotEmpty()) tasksTotal.maxBy { it.value }?.key.toString() else "-"

        Glide
            .with(this@ProfileFragment)
            .load("${RetrofitController.baseUrl}/${user.avatar}")
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(profile_image)
    }

    private fun logout() {
        launch(Dispatchers.IO) {
            try {
                authController.logout()
                withContext(Dispatchers.Main) {
                    activity?.let {
                        it.startActivity(Intent(it, LoginActivity::class.java))
                        it.finish()
                    }
                }
            } catch (error: Exception) {

            }
        }
    }

    private fun load() {
        tasksCompleted = HashMap()
        tasksTotal = HashMap()
        launch(Dispatchers.IO) {
            try {
                user = userController.getUser()
                todos = todoController.getTodos()
                categories = categoryController.getCategories()
                tasksCompleted = todoController.getCompletedTasks(todos)
                tasksTotal = todoController.getTotalTasks(todos)

                withContext(Dispatchers.Main) {
                    profile_loading.gone()
                    setUserData()
                }

            } catch (exception: Exception) {
                Log.i("asd", "asd")
            }
        }
    }

    private fun getCount(map: HashMap<String, Int>): Int {
        var acc = 0
        map.forEach { task -> acc += task.value }
        return acc
    }

    fun updateProfileFragment(data: Intent?) {
        profile_image.setImageURI(data?.data)

        Glide
            .with(this@ProfileFragment)
            .load(data?.data)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder)
            .into(profile_image)

        launch(Dispatchers.IO) {
            val drawable = Glide
                .with(this@ProfileFragment)
                .load(data?.data)
                .submit()
                .get()

            val bitmapDrawable = drawable as BitmapDrawable

            val file = File(context?.cacheDir, "Image")
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmetadata = bos.toByteArray()

            val fos = FileOutputStream(file)
            fos.write(bitmetadata)
            fos.flush()
            fos.close()

            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)

            val fileData = MultipartBody.Part.createFormData("avatar", "avatar", requestFile)

            try {
                userController.setPhoto(fileData)
                withContext(Dispatchers.Main) {
                    Toast.makeText(activity, "Ready", Toast.LENGTH_SHORT).show()
                }
            } catch (exception: java.lang.Exception) {
                Toast.makeText(activity, "Exception", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        const val LOAD_IMAGE = "LOAD_IMAGE"
        const val LOAD_DATA = "LOAD_DATA"

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
