package com.xeniac.fifaultimateteamcoin_dsfut_sell_fut.core.util

abstract class ResultError {
    object BlankField : ResultError()
    object UncheckedRadioButton : ResultError()
    object UncheckedCheckBox : ResultError()
}