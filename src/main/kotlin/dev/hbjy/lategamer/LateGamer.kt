package dev.hbjy.lategamer

object LateGamer {
    object Misc {
        val allowMentionPrefix = System.getenv("ALLOW_MENTION_PREFIX").toBoolean()
    }

    object WelcomeMessages {
        val enabled = System.getenv("WELCOME_MESSAGE_ENABLED").toBoolean()
    }
}