package malidaca.marvellisimo.utilities

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import malidaca.marvellisimo.R





class LoadDialog(var activity: Activity) {

    var dialog: Dialog = Dialog(activity)

    fun showDialog() {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_icon)

        val gifImageView = dialog.findViewById<ImageView>(R.id.loading_view)
        val imageViewTarget = GlideDrawableImageViewTarget(gifImageView)

        Glide.with(activity)
                .load(R.drawable.loading_img)
                .placeholder(R.drawable.loading_img)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget)

        dialog.show()
    }
    fun hideDialog() {
        dialog.dismiss()
    }

}