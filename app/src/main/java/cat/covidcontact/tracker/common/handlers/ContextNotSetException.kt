package cat.covidcontact.tracker.common.handlers

/**
 * Exception that is being thrown if the context is not set in [ScreenStateHandler].
 */
class ContextNotSetException : Exception(
    "Please do not forget to call super.onCreateView() on your fragments"
)
