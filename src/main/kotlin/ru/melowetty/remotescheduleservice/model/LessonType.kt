package ru.melowetty.remotescheduleservice.model

enum class LessonType(val type: String, private val scheduleFilePattern: String) {
    LECTURE("Лекция", "{type}: {subject}"),
    SEMINAR("Семинар", "{type}: {subject}"),
    EXAM("Экзамен", "{type}: {subject}"),
    INDEPENDENT_EXAM("Независимый экзамен", "{subject}"),
    TEST("Зачёт", "{type}: {subject}"),
    PRACTICE("Практика", "{type}: {subject}"),
    COMMON_MINOR("Майнор", "{type}"),
    MINOR("Майнор", "{type}"),
    COMMON_ENGLISH("Английский", "{type}"),
    ENGLISH("Английский", "{type}"),
    STATEMENT("Ведомость", "{type}: {subject}"),
    ICC("МКД", "{type}: {subject}"),
    UNDEFINED_AED("ДОЦ по выбору", "{subject}"),
    AED("ДОЦ", "{subject}"),
    CONSULT("Консультация", "{subject}"),
    EVENT("Мероприятие", "{type}: {subject}");

    fun toEventSubject(subject: String): String {
        return scheduleFilePattern
            .replace("{type}", type)
            .replace("{subject}", subject)
    }
}