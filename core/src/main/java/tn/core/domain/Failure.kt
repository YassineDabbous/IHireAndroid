package tn.core.domain

sealed class Failure() {

    class JustMessages(var messages:MutableList<String>? = null) : Failure()
    class ValidationFailure(var validation:Map<String, List<String>>? = null) : Failure()
    class GeneralFailure(var errors:MutableList<String>? = null, var code:Int = 0) : Failure()
    class GsonParsingFailure : Failure()
    class NeedAuthFailure : Failure()
    class UnknownFailure : Failure()
    class PermissionDeniedFailure: Failure()

    class CancelFailure: Failure()
    class NetworkFailure : Failure()

    class NoUserFailure : Failure()
    class ActiveUserRemove : Failure()
    class GPSDisabled: Failure()

    abstract class DetailedFailure: Failure()
}