package tn.core.presentation.listeners

enum class Action {
    SHOW,
    LOAD,
    OPTIONS,
    LIKE,
    COMMENT,
    SHARE,
    DELETE,
    REPORT,
    FOLLOW,
    ACCEPT,
    REFUSE,
    BAN,
    BOOKMARK
}

interface OnInteractListener<T>: OnClickItemListener<T> {
    fun onInteract(item: T, action: Action)
}