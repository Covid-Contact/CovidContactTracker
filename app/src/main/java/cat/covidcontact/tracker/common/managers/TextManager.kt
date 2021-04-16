package cat.covidcontact.tracker.common.managers

interface TextManager<T> {
    fun getTextFromValue(value: T): Int
}
