package android.text

class TextUtils {
    companion object {
        fun isEmpty(str: CharSequence?): Boolean {
            return str == null || str.isEmpty()
        }
    }
}