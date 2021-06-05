package cat.covidcontact.tracker.common.managers

interface ColorManager<T> {
    fun getColorFromValue(value: T): Int
}
