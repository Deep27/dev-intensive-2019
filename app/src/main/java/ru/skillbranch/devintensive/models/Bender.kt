package ru.skillbranch.devintensive.models

class Bender(
    var status: Status = Status.NORMAL,
    var question: Question = Question.NAME,
    var wrongAnswers: Int = 0
) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {

        if (question == Question.IDLE) {
            return question.question to status.color
        }

        val errorMessage = question.checkAnswer(answer)
            ?: return if (question.answers.contains(answer)) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                return if (wrongAnswers++ == 3) {
                    wrongAnswers = 0
                    status = Status.NORMAL
                    question = Question.NAME
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else {
                    status = status.nextStatus()
                    "Это неправильный ответ\n${question.question}" to status.color
                }
            }

        return "$errorMessage\n${question.question}" to status.color
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(
        val question: String,
        val answers: List<String>,
        private val answerRules: List<Pair<(String) -> Boolean, String>>
    ) {
        NAME(
            "Как меня зовут?", listOf("Бендер", "Bender"),
            listOf(
                Pair(
                    { answer -> !answer.isEmpty() && answer[0].isUpperCase() || answer.isEmpty() },
                    "Имя должно начинаться с заглавной буквы"
                )
            )
        ) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION(
            "Назови мою профессию?", listOf("сгибальщик", "bender"), listOf(
                Pair(
                    { answer -> !answer.isEmpty() && answer[0].isLowerCase() || answer.isEmpty() },
                    "Профессия должна начинаться со строчной буквы"
                )
            )
        ) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL(
            "Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood"),
            listOf(
                Pair(
                    { answer ->
                        answer.matches("[^\\d]*".toRegex())
                    },
                    "Материал не должен содержать цифр"
                )
            )
        ) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY(
            "Когда меня создали?",
            listOf("2993"),
            listOf(
                Pair(
                    { answer -> answer.matches("\\d+".toRegex()) },
                    "Год моего рождения должен содержать только цифры"
                )
            )
        ) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL(
            "Мой серийный номер?",
            listOf("2716057"),
            listOf(
                Pair(
                    { answer -> answer.matches("\\d{7}".toRegex()) },
                    "Серийный номер содержит только цифры, и их 7"
                )
            )
        ) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf(), listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question

        fun checkAnswer(answer: String): String? {
            for ((predicate, errorMessage) in answerRules) {
                if (!predicate.invoke(answer)) {
                    return errorMessage
                }
            }
            return null
        }
    }
}