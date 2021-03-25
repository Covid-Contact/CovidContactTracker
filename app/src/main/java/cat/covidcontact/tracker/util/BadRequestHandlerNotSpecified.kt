package cat.covidcontact.tracker.util

class BadRequestHandlerNotSpecified :
    Exception("The use case returned a Bad Request, but there is not any Bad Request Handler")
