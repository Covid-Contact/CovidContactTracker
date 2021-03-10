package cat.covidcontact.tracker.extensions

import android.content.Context

fun Context.getStringWithParams(id: Int, paramsId: IntArray) = getString(id, paramsId)
