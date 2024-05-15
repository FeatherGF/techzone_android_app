package com.app.techzone.ui.theme.privacy_policy


interface IPrivacyPolicyParagraph {
    val title: String
    val subParagraphs: List<String>
}

interface IPrivacyPolicyText {
    val paragraphs: List<IPrivacyPolicyParagraph>
}